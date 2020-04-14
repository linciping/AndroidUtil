package com.linciping.androidutil.writer;

import com.intellij.psi.*;
import com.linciping.androidutil.bean.ViewPart;
import com.linciping.androidutil.util.Util;

import java.util.ArrayList;
import java.util.List;

public class FragmentCodeWriter extends ContextCodeWriter {

    private String rootViewStr;

    public FragmentCodeWriter(List<ViewPart> viewPartList, PsiClass mClass, PsiFile psiFile, boolean isTarget26, boolean isAddInitViewMethod, boolean isHasInitViewMethod, String rootViewStr) {
        super(viewPartList, mClass, psiFile, isTarget26, isAddInitViewMethod, isHasInitViewMethod);
        this.rootViewStr = rootViewStr;
    }

    @Override
    protected void buildCode(PsiClass psiClass, List<ViewPart> viewPartList, PsiElementFactory psiElementFactory) {
        List<String> codeList = new ArrayList<>();
        for (ViewPart viewPart : viewPartList) {
            if (!viewPart.isSelected() || Util.fieldExist(psiClass, viewPart)) {
                continue;
            }
            psiClass.add(psiElementFactory.createFieldFromText(viewPart.getDeclareString(false, false), psiClass));
            codeList.add(viewPart.getFindViewStringWithRootView(rootViewStr, isTarget26()));
        }
        if (!isAddInitViewMethod()) {
            PsiMethod createMethod = psiClass.findMethodsByName("onCreateView", false)[0];
            PsiCodeBlock psiCodeBlock = createMethod.getBody();
            if (psiCodeBlock != null) {
                PsiStatement[] psiStatements = psiCodeBlock.getStatements();
                PsiStatement endStatement = psiStatements[psiStatements.length - 1];
                for (String code : codeList) {
                    if (Util.onCreateMethodHasStatement(code, psiStatements)) {
                        psiCodeBlock.addBefore(psiElementFactory.createStatementFromText(code, psiClass), endStatement);
                    }
                }
            }
        } else {
            if (isHasInitViewMethod()) {
                PsiMethod initViewMethod = Util.getInitView(psiClass);
                assert initViewMethod != null;
                assert initViewMethod.getBody() != null;
                for (String code : codeList) {
                    initViewMethod.getBody().add(psiElementFactory.createStatementFromText(code, psiClass));
                }
            } else {
                StringBuilder methodBuilder = new StringBuilder("private void initView(View " + rootViewStr + ") {");
                for (String code : codeList) {
                    methodBuilder.append(code);
                }
                methodBuilder.append("}");
                psiClass.add(psiElementFactory.createMethodFromText(methodBuilder.toString(), psiClass));
            }
        }
    }
}
