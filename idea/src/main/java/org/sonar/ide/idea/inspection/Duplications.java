package org.sonar.ide.idea.inspection;

import com.intellij.codeInspection.InspectionManager;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.sonar.ide.idea.utils.IdeaResourceUtils;
import org.sonar.wsclient.Sonar;
import org.sonar.wsclient.services.Measure;
import org.sonar.wsclient.services.ResourceQuery;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Evgeny Mandrikov
 */
public class Duplications extends AbstractSonarInspectionTool {
  @Override
  protected List<ProblemDescriptor> checkFileBySonar(@NotNull PsiFile file, @NotNull InspectionManager manager, boolean isOnTheFly, Sonar sonar) {
    if (isOnTheFly) {
      return null;
    }

    String metricKey = "duplications_data";
    String resourceKey = IdeaResourceUtils.getInstance().getFileKey(file);
    ResourceQuery resourceQuery = ResourceQuery.createForMetrics(resourceKey, metricKey);
    Measure measure = sonar.find(resourceQuery).getMeasure(metricKey);
    if (measure == null) {
      return null;
    }
    String data = measure.getData();
    getLog().info(data);
    List<ProblemDescriptor> problems = new ArrayList<ProblemDescriptor>();

    Project project = file.getProject();
    PsiDocumentManager documentManager = PsiDocumentManager.getInstance(project);
    com.intellij.openapi.editor.Document document = documentManager.getDocument(file.getContainingFile());

    try {
      SAXBuilder saxBuilder = new SAXBuilder();
      Document xmlDocument = saxBuilder.build(new StringReader(data));
      Element root = xmlDocument.getRootElement();

      for (Object child : root.getChildren()) {
        Element element = (Element) child;

        System.out.println(element.getAttribute("lines"));
        int start = Integer.parseInt(element.getAttributeValue("start"));
        System.out.println(element.getAttribute("target-start"));
        String targetResource = element.getAttributeValue("target-resource");

        problems.add(manager.createProblemDescriptor(
            file,
            getTextRange(document, start),
            "Duplicates " + targetResource,
            ProblemHighlightType.GENERIC_ERROR_OR_WARNING,
            isOnTheFly,
            LocalQuickFix.EMPTY_ARRAY
        ));

      }

    } catch (JDOMException e) {
      getLog().error(e.getMessage(), e);
    } catch (IOException e) {
      getLog().error(e.getMessage(), e);
    }

    return problems;
    // TODO
//    throw new NotImplementedException();
  }

  @Nls
  @NotNull
  @Override
  public String getDisplayName() {
    return "Duplications";
  }

  @NotNull
  @Override
  public String getShortName() {
    return "Sonar-Duplications"; // TODO
  }

  @Override
  public String getStaticDescription() {
    return "Duplications from Sonar";
  }
}
