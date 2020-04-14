package com.linciping.androidutil.writer;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiFile;
import com.linciping.androidutil.bean.ViewPart;

import java.util.List;

public abstract class FindViewCodeWriter extends BaseCodeWriter {

    private List<ViewPart> viewPartList;
    private boolean isTarget26;

    public FindViewCodeWriter(List<ViewPart> viewPartList, PsiClass mClass, PsiFile psiFile, boolean isTarget26) {
        super(mClass, psiFile);
        this.isTarget26 = isTarget26;
        this.viewPartList = viewPartList;
    }

    public boolean isTarget26() {
        return isTarget26;
    }

    @Override
    protected void buildCode(PsiClass psiClass, PsiElementFactory psiElementFactory) {
        buildCode(psiClass, viewPartList, psiElementFactory);
    }

    protected abstract void buildCode(PsiClass psiClass, List<ViewPart> viewPartList, PsiElementFactory psiElementFactory);
}
