package com.linciping.androidutil.writer;

import com.intellij.codeInsight.actions.ReformatCodeProcessor;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;

public abstract class BaseCodeWriter extends WriteCommandAction.Simple {

    private Project mProject;
    private PsiClass mClass;
    private PsiElementFactory mFactory;
    private PsiFile psiFile;


    public BaseCodeWriter(PsiClass mClass, PsiFile psiFile) {
        super(mClass.getProject(),"");
        this.mProject = getProject();
        this.mClass = mClass;
        this.mFactory = JavaPsiFacade.getElementFactory(mProject);
        this.psiFile = psiFile;
    }

    @Override
    protected void run() throws Throwable {
        buildCode(mClass,mFactory);
        JavaCodeStyleManager styleManager = JavaCodeStyleManager.getInstance(mProject);
        styleManager.optimizeImports(psiFile);
        styleManager.shortenClassReferences(mClass);
        new ReformatCodeProcessor(mProject, mClass.getContainingFile(), null, false).runWithoutProgress();
    }

    protected abstract void buildCode(PsiClass psiClass,PsiElementFactory psiElementFactory);
}
