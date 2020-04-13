package com.linciping.androidutil.bean;

import com.intellij.psi.PsiType;

import java.io.Serializable;

public class MethodParam implements Serializable {

    private boolean isSelected=true;
    private String paramType;
    private String paramName;

    public MethodParam(String paramType, String paramName) {
        this.paramType = paramType;
        if (paramName.startsWith("m")) {
            paramName = paramName.replace("m", "");
            String first = paramName.substring(0, 1);
            String realFirst = first.toLowerCase();
            paramName = paramName.replace(first, realFirst);
        }
        this.paramName = paramName;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getParamType() {
        return paramType;
    }

    public void setParamType(String paramType) {
        this.paramType = paramType;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }
}
