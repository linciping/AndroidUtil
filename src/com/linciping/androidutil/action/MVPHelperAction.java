package com.linciping.androidutil.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.file.PsiDirectoryFactory;
import com.linciping.androidutil.util.Util;
import com.linciping.androidutil.view.MVPHelperDialog;
import com.linciping.androidutil.writer.MVPCodeWriter;
import org.jetbrains.annotations.NotNull;

public class MVPHelperAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        PsiDirectory psiDirectory = (PsiDirectory) e.getData(LangDataKeys.PSI_ELEMENT);
        Project project = e.getProject();
        assert project != null;
        MVPHelperDialog mvpHelperDialog=new MVPHelperDialog(project);
        mvpHelperDialog.addOKAction(dataArray -> {
            String moduleName=dataArray[0];
            String presenterMethodName=dataArray[1];
            String viewMethodName=dataArray[2];
            String activityClassName=moduleName+"Activity";
            String presenterClassName=moduleName+"Presenter";
            String viewClassName="I"+moduleName+"View";
            Util.executeWriteCommand(new MVPCodeWriter(project, psiDirectory, activityClassName, presenterClassName, viewClassName, presenterMethodName, viewMethodName));
        });
        mvpHelperDialog.pack();
        mvpHelperDialog.setLocationRelativeTo(WindowManager.getInstance().getFrame(project));
        mvpHelperDialog.setVisible(true);
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        Presentation presentation = e.getPresentation();
        Project project = e.getProject();
        if (project == null) {
            presentation.setEnabled(false);
            return;
        }
        PsiElement psiElement = e.getData(LangDataKeys.PSI_ELEMENT);
        if (psiElement instanceof PsiDirectory) {
            PsiDirectory psiDirectory = (PsiDirectory) psiElement;
            PsiDirectoryFactory psiDirectoryFactory = PsiDirectoryFactory.getInstance(project);
            presentation.setEnabled(psiDirectoryFactory.isPackage(psiDirectory));
            return;
        }
        presentation.setEnabled(false);
    }
}
