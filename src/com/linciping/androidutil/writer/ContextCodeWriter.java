package com.linciping.androidutil.writer;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.linciping.androidutil.bean.ViewPart;

import java.util.List;

public abstract class ContextCodeWriter extends FindViewCodeWriter {

    private boolean isAddInitViewMethod;
    private boolean isHasInitViewMethod;

    public ContextCodeWriter(List<ViewPart> viewPartList, PsiClass mClass, PsiFile psiFile, boolean isTarget26, boolean isAddInitViewMethod, boolean isHasInitViewMethod) {
        super(viewPartList, mClass, psiFile, isTarget26);
        this.isAddInitViewMethod = isAddInitViewMethod;
        this.isHasInitViewMethod = isHasInitViewMethod;
    }

    public boolean isAddInitViewMethod() {
        return isAddInitViewMethod;
    }

    public boolean isHasInitViewMethod() {
        return isHasInitViewMethod;
    }

}
