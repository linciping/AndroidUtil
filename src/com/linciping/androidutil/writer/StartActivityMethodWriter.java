package com.linciping.androidutil.writer;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiMethod;
import com.linciping.androidutil.bean.MethodParam;

import java.util.List;

public class StartActivityMethodWriter extends BaseCodeWriter {

    private String methodCode;

    public StartActivityMethodWriter(PsiClass mClass, PsiFile psiFile, String code) {
        super(mClass, psiFile);
        this.methodCode = code;
    }

    @Override
    protected void buildCode(PsiClass psiClass, PsiElementFactory psiElementFactory) {
        PsiMethod psiMethod = psiElementFactory.createMethodFromText(methodCode, psiClass);
        psiClass.add(psiMethod);
    }
}
