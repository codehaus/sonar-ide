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

package org.sonar.ide.idea.editor;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.event.EditorFactoryEvent;
import com.intellij.openapi.editor.event.EditorFactoryListener;
import com.intellij.openapi.editor.markup.MarkupModel;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.ide.idea.utils.IdeaResourceUtils;
import org.sonar.ide.idea.vfs.SonarVirtualFile;

/**
 * @author Evgeny Mandrikov
 */
public class SonarEditorListener implements EditorFactoryListener {
  private static final Logger LOG = LoggerFactory.getLogger(SonarEditorListener.class);

  public void editorCreated(EditorFactoryEvent editorFactoryEvent) {
    LOG.info("Editor created");
    try {
      Editor editor = editorFactoryEvent.getEditor();
      Document document = editor.getDocument();
      Project project = editor.getProject();

      /*
      editor.getGutter().registerTextAnnotation(new TextAnnotationGutterProvider() {

        @Override
        public String getLineText(int i, Editor editor) {
          // TODO
          return null;
        }

        @Override
        public String getToolTip(int i, Editor editor) {
          // TODO
          return null;
        }

        @Override
        public EditorFontType getStyle(int i, Editor editor) {
          // TODO
          return null;
        }

        @Override
        public ColorKey getColor(int i, Editor editor) {
          // TODO
          return null;
        }

        @Override
        public List<AnAction> getPopupActions(Editor editor) {
          // TODO
          return null;
        }

        @Override
        public void gutterClosed() {
          // TODO

        }
      });
      */

      processFile(project, document);
    } catch (Throwable e) { // NOSONAR
      // This is a critical part in file opening procedure in IDEA.
      // Even if we can't do something file should be opened anyway.
      LOG.error(e.getMessage(), e);
    }
  }

  public void editorReleased(EditorFactoryEvent editorFactoryEvent) {
    LOG.info("Editor released");
    try {
      Editor editor = editorFactoryEvent.getEditor();
      Project project = editor.getProject();
      MarkupModel markupModel = editor.getDocument().getMarkupModel(project);
      ShowViolationsTask.removeSonarHighlighters(markupModel);
      ShowCoverageTask.removeSonarHighlighters(markupModel);
    } catch (Throwable e) { // NOSONAR
      // Even if we can't do something editor should be released.
      LOG.error(e.getMessage(), e);
    }
  }

  protected void processFile(Project project, Document document) {
    VirtualFile file = FileDocumentManager.getInstance().getFile(document);

    if (file instanceof SonarVirtualFile) {
      SonarVirtualFile sonarVirtualFile = (SonarVirtualFile) file;
      new ShowViolationsTask(project, document, sonarVirtualFile.getResourceKey()).queue();
      new ShowCoverageTask(project, document, sonarVirtualFile.getResourceKey()).queue();
      return;
    }

    PsiFile psiFile = PsiManager.getInstance(project).findFile(file);
    if (psiFile instanceof PsiJavaFile) {
      PsiJavaFile psiJavaFile = (PsiJavaFile) psiFile;
      final String resourceKey = IdeaResourceUtils.getInstance().getFileKey(psiJavaFile);
      new ShowViolationsTask(project, document, resourceKey).queue();
    }
  }

}
