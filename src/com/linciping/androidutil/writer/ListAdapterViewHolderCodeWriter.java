package com.linciping.androidutil.writer;

import com.intellij.psi.*;
import com.linciping.androidutil.bean.ViewPart;
import com.linciping.androidutil.util.Util;

import java.util.List;

public class ListAdapterViewHolderCodeWriter extends FindViewCodeWriter {

    private boolean isHasViewHolderClass;

    public ListAdapterViewHolderCodeWriter(List<ViewPart> viewPartList, PsiClass mClass, PsiFile psiFile, boolean isTarget26, boolean isHasViewHolderClass) {
        super(viewPartList, mClass, psiFile, isTarget26);
        this.isHasViewHolderClass = isHasViewHolderClass;
    }

    @Override
    protected void buildCode(PsiClass psiClass, List<ViewPart> viewPartList, PsiElementFactory psiElementFactory) {
        if (isHasViewHolderClass) {
            PsiClass viewHolderClass = Util.findViewHolderClass(psiClass);
            assert viewHolderClass != null;
            String name = viewHolderClass.getName();
            PsiMethod viewHolderMethod = Util.findMethod(viewHolderClass, name);
            if (viewHolderMethod != null) {
                PsiParameter psiParameter = Util.findViewParameter(viewHolderMethod.getParameterList().getParameters());
                if (psiParameter != null) {
                    String viewContentStr = psiParameter.getName();
                    for (ViewPart viewPart : viewPartList) {
                        if (!viewPart.isSelected() || Util.fieldExist(viewHolderClass, viewPart)) {
                            continue;
                        }
                        viewHolderClass.add(psiElementFactory.createFieldFromText(viewPart.getDeclareString(true, false), viewHolderClass));
                        viewHolderMethod.getBody().add(psiElementFactory.createStatementFromText(viewPart.getFindViewStringWithRootView(viewContentStr, isTarget26()), viewHolderClass));
                    }
                } else {
                    addViewHolderMethodAndField(viewPartList, viewHolderClass, psiElementFactory);
                }
            } else {
                addViewHolderMethodAndField(viewPartList, viewHolderClass, psiElementFactory);
            }
        } else {
            PsiClass viewHolderClass = psiElementFactory.createClass("ViewHolder");
            addViewHolderMethodAndField(viewPartList, viewHolderClass, psiElementFactory);
            psiClass.add(viewHolderClass);
        }
    }

    private void addViewHolderMethodAndField(List<ViewPart> viewPartList, PsiClass viewHolderClass, PsiElementFactory psiElementFactory) {
        String viewContentStr = "itemView";
        StringBuilder methodBuilder = new StringBuilder("ViewHolder(View ").append(viewContentStr).append("){");
        for (ViewPart viewPart : viewPartList) {
            if (!viewPart.isSelected()) {
                continue;
            }
            viewHolderClass.add(psiElementFactory.createFieldFromText(viewPart.getDeclareString(true, false), viewHolderClass));
            methodBuilder.append(viewPart.getFindViewStringWithRootView(viewContentStr, isTarget26()));
        }
        methodBuilder.append("}");
        viewHolderClass.add(psiElementFactory.createMethodFromText(methodBuilder.toString(), viewHolderClass));
    }
}
