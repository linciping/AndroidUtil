package com.linciping.androidutil.writer;

import com.intellij.psi.*;
import com.linciping.androidutil.bean.ViewPart;
import com.linciping.androidutil.util.Util;

import java.util.ArrayList;
import java.util.List;

public class ActivityCodeWriter extends ContextCodeWriter {
    public ActivityCodeWriter(List<ViewPart> viewPartList, PsiClass mClass, PsiFile psiFile, boolean isAddInitViewMethod, boolean isHasInitViewMethod, boolean isTarget26) {
        super(viewPartList, mClass, psiFile, isTarget26, isAddInitViewMethod, isHasInitViewMethod);
    }

    @Override
    protected void buildCode(PsiClass psiClass, List<ViewPart> viewPartList, PsiElementFactory psiElementFactory) {
        List<String> codeList = new ArrayList<>(viewPartList.size());
        for (ViewPart viewPart : viewPartList) {
            if (!viewPart.isSelected() || Util.fieldExist(psiClass, viewPart)) {
                continue;
            }
            psiClass.add(psiElementFactory.createFieldFromText(viewPart.getDeclareString(false, false), psiClass));
            codeList.add(viewPart.getFindViewString(isTarget26()));
        }
        if (isAddInitViewMethod()) {
            if (isHasInitViewMethod()) {
                PsiMethod initViewMethod = Util.getInitView(psiClass);
                assert initViewMethod != null;
                assert initViewMethod.getBody() != null;
                for (String code : codeList) {
                    initViewMethod.getBody().add(psiElementFactory.createStatementFromText(code, psiClass));
                }
            } else {
                StringBuilder methodBuilder = new StringBuilder("private void initView() {");
                for (String code : codeList) {
                    methodBuilder.append(code);
                }
                methodBuilder.append("}");
                psiClass.add(psiElementFactory.createMethodFromText(methodBuilder.toString(), psiClass));
            }
        } else {
            if (codeList.size() > 0) {
                PsiMethod createMethod = psiClass.findMethodsByName("onCreate", false)[0];
                PsiCodeBlock psiCodeBlock = createMethod.getBody();
                if (psiCodeBlock != null) {
                    PsiStatement[] psiStatements = psiCodeBlock.getStatements();
                    for (String code : codeList) {
                        if (Util.onCreateMethodHasStatement(code, psiStatements)) {
                            createMethod.getBody().add(psiElementFactory.createStatementFromText(code, psiClass));
                        }
                    }
                }
            }
        }
    }
}
