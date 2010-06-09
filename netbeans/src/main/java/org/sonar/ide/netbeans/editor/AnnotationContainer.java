/*
 * Copyright (C) 2010 Evgeny Mandrikov
 *
 * Sonar-IDE is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * Sonar-IDE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Sonar-IDE; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */

package org.sonar.ide.netbeans.editor;

import org.openide.cookies.EditorCookie;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.*;

/**
 * The class holding the annotations for the single file object.
 */
public final class AnnotationContainer {
  private static final Logger LOG = LoggerFactory.getLogger(AnnotationContainer.class);

  private static final Map<FileObject, AnnotationContainer> CONTAINERS = new HashMap<FileObject, AnnotationContainer>();

  private final FileObject fileObject;

  private final List<SonarAnnotation> annotations = new ArrayList<SonarAnnotation>();

  private AnnotationContainer(FileObject fileObject) {
    this.fileObject = fileObject;
  }

  /**
   * Returns the annotation container for the given file object. If it
   * exists returns existing one, otherwise return newly created holder.
   *
   * @param fileObject the file for which we want to get the annotation holder
   * @return the annotation container for the given file object (existing
   *         or newly created one).
   */
  public static synchronized AnnotationContainer getInstance(FileObject fileObject) {
    AnnotationContainer annotationContainer = CONTAINERS.get(fileObject);

    if (annotationContainer != null) {
      return annotationContainer;
    }

    try {
      EditorCookie.Observable editorCookie = DataObject.find(fileObject).getCookie(EditorCookie.Observable.class);

      if (editorCookie == null) {
        return null;
      }

      annotationContainer = new AnnotationContainer(fileObject);

      CloseHandler handler = new CloseHandler(annotationContainer, editorCookie);
      editorCookie.addPropertyChangeListener(handler);
      SwingUtilities.invokeLater(handler);

      CONTAINERS.put(fileObject, annotationContainer);

      return annotationContainer;
    } catch (DataObjectNotFoundException ex) {
      LOG.warn(ex.getMessage(), ex);
      return null;
    }
  }

  /**
   * Resets the factory clearing all holder and all annotations stored.
   */
  public static synchronized void reset() {
    for (AnnotationContainer annotationContainer : CONTAINERS.values()) {
      annotationContainer.setAnnotations(Collections.<SonarAnnotation>emptyList());
    }
    CONTAINERS.clear();
  }

  /**
   * Returns the file object to which annotations belong to.
   *
   * @return the file object to which annotations belong to
   */
  public FileObject getFileObject() {
    return fileObject;
  }

  /**
   * Sets the new bunch of annotations to the file object. The old annotations
   * are removed and detached. This method is <i>thread safe</i>.
   *
   * @param newAnnotations the fresh annotations to attach
   */
  public synchronized void setAnnotations(List<SonarAnnotation> newAnnotations) {
    Runnable updater = new AnnotationUpdater(fileObject, newAnnotations, annotations);

    annotations.clear();
    annotations.addAll(newAnnotations);

    if (SwingUtilities.isEventDispatchThread()) {
      updater.run();
    } else {
      SwingUtilities.invokeLater(updater);
    }
  }

  /**
   * Returns the list of annotations currently used.
   *
   * @return unmodifiable list of current used annotations
   */
  public synchronized List<SonarAnnotation> getAnnotations() {
    return Collections.unmodifiableList(annotations);
  }

  private static class AnnotationUpdater implements Runnable {

    private final FileObject fileObject;

    private final List<SonarAnnotation> annotationsToAdd;

    private final List<SonarAnnotation> annotationsToRemove;

    public AnnotationUpdater(FileObject fileObject, List<SonarAnnotation> annotationsToAdd,
                             List<SonarAnnotation> annotationsToRemove) {

      this.fileObject = fileObject;
      this.annotationsToAdd = new ArrayList<SonarAnnotation>(annotationsToAdd);
      this.annotationsToRemove = new ArrayList<SonarAnnotation>(annotationsToRemove);
    }

    public void run() {
      for (SonarAnnotation annotation : annotationsToRemove) {
        annotation.documentDetach();
      }

      for (SonarAnnotation annotation : annotationsToAdd) {
        annotation.documentAttach();
      }
    }
  }

  private static class CloseHandler implements PropertyChangeListener, Runnable {

    private final AnnotationContainer container;

    private final EditorCookie.Observable editorCookie;

    public CloseHandler(AnnotationContainer container, EditorCookie.Observable editorCookie) {

      this.container = container;
      this.editorCookie = editorCookie;
    }

    public void propertyChange(PropertyChangeEvent evt) {
      if (evt.getPropertyName() == null || EditorCookie.Observable.PROP_OPENED_PANES.equals(evt.getPropertyName())) {
        run();
      }
    }

    public void run() {
      if (editorCookie.getOpenedPanes() == null) {
        synchronized (AnnotationContainer.class) {
          CONTAINERS.remove(container.getFileObject());
        }

        container.setAnnotations(Collections.<SonarAnnotation>emptyList());
        editorCookie.removePropertyChangeListener(this);
      }
    }
  }
}
