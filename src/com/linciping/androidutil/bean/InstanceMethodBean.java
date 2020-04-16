package com.linciping.androidutil.bean;

import java.io.Serializable;
import java.util.List;

public class InstanceMethodBean implements Serializable {

    private String instanceMethodCode;
    private String extraSettingValueMethodCode;
    private List<String> constantNameList;

    public InstanceMethodBean(String instanceMethodCode, String extraSettingValueMethodCode, List<String> constantNameList) {
        this.instanceMethodCode = instanceMethodCode;
        this.extraSettingValueMethodCode = extraSettingValueMethodCode;
        this.constantNameList = constantNameList;
    }

    public String getExtraSettingValueMethodCode() {
        return extraSettingValueMethodCode;
    }

    public String getInstanceMethodCode() {
        return instanceMethodCode;
    }

    public List<String> getConstantNameList() {
        return constantNameList;
    }
}
