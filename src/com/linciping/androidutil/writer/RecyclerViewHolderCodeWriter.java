package com.linciping.androidutil.writer;

import com.intellij.psi.*;
import com.linciping.androidutil.bean.ViewPart;
import com.linciping.androidutil.util.Util;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewHolderCodeWriter extends FindViewCodeWriter {

    private boolean isHasViewHolderClass;
    private PsiClass recyclerViewHolderClass;
    private List<PsiClass> importClassList=new ArrayList<>();

    public RecyclerViewHolderCodeWriter(List<ViewPart> viewPartList, PsiClass mClass, PsiFile psiFile, boolean isTarget26, boolean isHasViewHolderClass) {
        super(viewPartList, mClass, psiFile, isTarget26);
        this.isHasViewHolderClass = isHasViewHolderClass;
    }

    public void setRecyclerViewHolderClass(PsiClass recyclerViewHolderClass) {
        this.recyclerViewHolderClass = recyclerViewHolderClass;
    }

    @Override
    protected List<PsiClass> getImportClass() {
        return importClassList;
    }

    @Override
    protected void buildCode(PsiClass psiClass, List<ViewPart> viewPartList, PsiElementFactory psiElementFactory) {
        if (isHasViewHolderClass) {
            PsiClass viewHolderClass = Util.findRecyclerViewHolderClass(recyclerViewHolderClass, psiClass);
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
                        PsiClass viewClass=Util.findClassForName(mProject,viewPart.getTypeFull());
                        if (viewClass!=null){
                            importClassList.add(viewClass);
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
        String className = viewHolderClass.getName();
        StringBuilder methodBuilder = new StringBuilder().append(className).append("(View ").append(viewContentStr).append("){");
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
