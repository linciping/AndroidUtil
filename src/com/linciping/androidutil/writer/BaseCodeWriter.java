package com.linciping.androidutil.writer;

import com.intellij.codeInsight.actions.ReformatCodeProcessor;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;

public abstract class BaseCodeWriter extends WriteCommandAction.Simple {

    protected Project mProject;
    private PsiClass mClass;
    private PsiElementFactory mFactory;
    protected PsiJavaFile psiFile;
    protected JavaCodeStyleManager javaCodeStyleManager;

    public BaseCodeWriter(PsiClass mClass, PsiFile psiFile) {
        super(mClass.getProject(),"");
        this.mProject = getProject();
        this.mClass = mClass;
        this.mFactory = JavaPsiFacade.getElementFactory(mProject);
        this.psiFile = (PsiJavaFile) psiFile;
    }

    @Override
    protected void run() throws Throwable {
        javaCodeStyleManager = JavaCodeStyleManager.getInstance(mProject);
        buildCode(mClass,mFactory);
    }

    protected void formatCode(){
        javaCodeStyleManager.optimizeImports(psiFile);
        javaCodeStyleManager.shortenClassReferences(mClass);
        new ReformatCodeProcessor(mProject, mClass.getContainingFile(), null, false).runWithoutProgress();
    }

    protected abstract void buildCode(PsiClass psiClass,PsiElementFactory psiElementFactory);
}
