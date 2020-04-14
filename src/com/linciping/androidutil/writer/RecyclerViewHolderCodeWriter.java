package com.linciping.androidutil.writer;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiFile;
import com.linciping.androidutil.bean.ViewPart;

import java.util.List;

public class RecyclerViewHolderCodeWriter extends FindViewCodeWriter {

    private boolean isHasViewHolderClass;

    public RecyclerViewHolderCodeWriter(List<ViewPart> viewPartList, PsiClass mClass, PsiFile psiFile, boolean isTarget26, boolean isHasViewHolderClass) {
        super(viewPartList, mClass, psiFile, isTarget26);
        this.isHasViewHolderClass = isHasViewHolderClass;
    }

    @Override
    protected void buildCode(PsiClass psiClass, List<ViewPart> viewPartList, PsiElementFactory psiElementFactory) {
        if (isHasViewHolderClass) {

        } else {

        }
    }
}
