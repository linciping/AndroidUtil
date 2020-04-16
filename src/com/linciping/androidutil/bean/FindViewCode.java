package com.linciping.androidutil.bean;

import java.io.Serializable;
import java.util.List;

public class FindViewCode implements Serializable {

    private List<String> fieldCodeList;
    private boolean isAddMethod;
    private String methodCode;
    private List<String> statementCodeList;

    public List<String> getFieldCodeList() {
        return fieldCodeList;
    }

    public void setFieldCodeList(List<String> fieldCodeList) {
        this.fieldCodeList = fieldCodeList;
    }

    public boolean isAddMethod() {
        return isAddMethod;
    }

    public void setAddMethod(boolean addMethod) {
        isAddMethod = addMethod;
    }

    public String getMethodCode() {
        return methodCode;
    }

    public void setMethodCode(String methodCode) {
        this.methodCode = methodCode;
    }

    public List<String> getStatementCodeList() {
        return statementCodeList;
    }

    public void setStatementCodeList(List<String> statementCodeList) {
        this.statementCodeList = statementCodeList;
    }
}
