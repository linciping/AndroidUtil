package com.linciping.androidutil.bean;

import java.io.Serializable;
import java.util.List;

public class InstanceMethodBean implements Serializable {

    private String code;
    private List<String> constantNameList;

    public InstanceMethodBean(String code, List<String> constantNameList) {
        this.code = code;
        this.constantNameList = constantNameList;
    }

    public String getCode() {
        return code;
    }

    public List<String> getConstantNameList() {
        return constantNameList;
    }
}
