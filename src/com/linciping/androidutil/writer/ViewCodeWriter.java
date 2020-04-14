package com.linciping.androidutil.writer;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiFile;
import com.linciping.androidutil.bean.ViewPart;

import java.util.List;

public class ViewCodeWriter extends FindViewCodeWriter {


    public ViewCodeWriter(List<ViewPart> viewPartList, PsiClass mClass, PsiFile psiFile, boolean isTarget26) {
        super(viewPartList, mClass, psiFile, isTarget26);
    }

    @Override
    protected void buildCode(PsiClass psiClass, List<ViewPart> viewPartList, PsiElementFactory psiElementFactory) {

    }
}
