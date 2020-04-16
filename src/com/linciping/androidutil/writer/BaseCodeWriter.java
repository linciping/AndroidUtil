package com.linciping.androidutil.writer;

import com.intellij.codeInsight.actions.ReformatCodeProcessor;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;

public abstract class BaseCodeWriter extends BaseWriterRunnable {

    private PsiClass mClass;
    private PsiElementFactory mFactory;
    protected PsiJavaFile psiFile;
    protected JavaCodeStyleManager javaCodeStyleManager;

    public BaseCodeWriter(PsiClass mClass, PsiFile psiFile) {
        super(mClass.getProject());
        this.mClass = mClass;
        this.mFactory = JavaPsiFacade.getElementFactory(mProject);
        this.psiFile = (PsiJavaFile) psiFile;
    }

    @Override
    public void run() {
        javaCodeStyleManager = JavaCodeStyleManager.getInstance(mProject);
        buildCode(mClass, mFactory);
    }

    protected void formatCode(){
        javaCodeStyleManager.optimizeImports(psiFile);
        javaCodeStyleManager.shortenClassReferences(mClass);
        new ReformatCodeProcessor(mProject, mClass.getContainingFile(), null, false).runWithoutProgress();
    }

    protected abstract void buildCode(PsiClass psiClass,PsiElementFactory psiElementFactory);
}
