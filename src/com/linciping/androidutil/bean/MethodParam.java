package com.linciping.androidutil.bean;

import com.intellij.psi.PsiType;

import java.io.Serializable;

public class MethodParam implements Serializable {

    private boolean isSelected = true;
    private PsiType paramType;
    private String paramTypeName;
    private String paramName;

    public MethodParam(PsiType paramType, String paramName) {
        this.paramType = paramType;
        this.paramTypeName = paramType.getPresentableText();
        if (paramName.startsWith("m")) {
            paramName = paramName.replace("m", "");
            String first = paramName.substring(0, 1);
            String realFirst = first.toLowerCase();
            paramName = paramName.replace(first, realFirst);
        }
        this.paramName = paramName;
    }

    public PsiType getParamType() {
        return paramType;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getParamTypeName() {
        return paramTypeName;
    }

    public void setParamTypeName(String paramTypeName) {
        this.paramTypeName = paramTypeName;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }
}
