package com.linciping.androidutil.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.psi.*;
import com.intellij.psi.impl.file.PsiDirectoryFactory;
import com.linciping.androidutil.writer.MVPCodeWriter;
import org.jetbrains.annotations.NotNull;

public class MVPHelperAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        PsiDirectory psiDirectory = (PsiDirectory) e.getData(LangDataKeys.PSI_ELEMENT);
        Project project = e.getProject();
        assert project != null;
        new MVPCodeWriter(project, psiDirectory).execute();
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
