package com.linciping.androidutil.writer;

import com.intellij.codeInsight.actions.ReformatCodeProcessor;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;

public class MVPCodeWriter extends WriteCommandAction.Simple {

    private PsiDirectory psiDirectory;
    private Project project;

    public MVPCodeWriter(Project project, PsiDirectory psiDirectory) {
        super(project, "mvp code writer");
        this.project=project;
        this.psiDirectory = psiDirectory;
    }

    @Override
    protected void run() throws Throwable {
        PsiFile psiFile = psiDirectory.createFile("DemoActivity.java");
        PsiElementFactory psiElementFactory = JavaPsiFacade.getElementFactory(project);
        PsiClass psiClass = psiElementFactory.createClass("DemoActivity");
        psiFile.add(psiClass);

        JavaCodeStyleManager styleManager = JavaCodeStyleManager.getInstance(project);
        styleManager.optimizeImports(psiFile);
        styleManager.shortenClassReferences(psiClass);
        new ReformatCodeProcessor(project, psiClass.getContainingFile(), null, false).runWithoutProgress();
    }
}
