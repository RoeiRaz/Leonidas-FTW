package il.org.spartan.ispartanizer.plugin;

import com.intellij.codeInsight.daemon.DaemonCodeAnalyzer;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;

/**
 * @author michalcohen
 * @since 03-01-2017
 */
public class SpartanizerEnableAction extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent e) {
        PsiFile psiFile = e.getData(LangDataKeys.PSI_FILE);
        Project p = psiFile.getProject();
        Toolbox tb = Toolbox.getInstance();
        if (tb.checkExcluded(psiFile)) {
            tb.includeFile(psiFile);
        } else {
            tb.excludeFile(psiFile);
        }
        Presentation presentation = e.getPresentation();
        if (Toolbox.getInstance().checkExcluded(e.getData(LangDataKeys.PSI_FILE))) {
            presentation.setText("Enable Spartanization");
        } else {
            presentation.setText("Disable Spartanization");
        }
        DaemonCodeAnalyzer.getInstance(p).restart();
    }
}
