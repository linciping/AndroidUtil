package com.linciping.androidutil.test;

import com.linciping.androidutil.bean.MethodParam;
import com.linciping.androidutil.constant.Definitions;
import com.linciping.androidutil.util.CheckUtil;

import java.util.List;

public class Sample {

    public static void main(String[] args) {
    }

    public static String createFragmentInstanceMethod(List<MethodParam> methodParamList, String fragmentName) {
        String fragmentInstanceName = paramName(fragmentName);
        StringBuilder methodBuilder = new StringBuilder("public static ").append(fragmentName).append(" newInstance");
        StringBuilder paramBuilder = new StringBuilder();
        StringBuilder intentBuilder = new StringBuilder();
        for (MethodParam methodParam : methodParamList) {
            if (methodParam.isSelected()) {
                String paramType = methodParam.getParamTypeName();
                paramBuilder.append(paramType).append(" ").append(methodParam.getParamName()).append(",");
                String extraType = Definitions.getExtraTypeName(paramType);
                if (CheckUtil.isStringNoEmpty(extraType)) {
                    intentBuilder.append("  bundle.put").append(extraType).append("(\"").append(methodParam.getParamName()).append("\",").append(methodParam.getParamName()).append(");\n");
                } else {
                    intentBuilder.append("  bundle.putSerializable(\"").append(methodParam.getParamName()).append("\",").append(methodParam.getParamName()).append(");\n");
                }
            }
        }
        String param = paramBuilder.toString();
        if (param.length() > 0) {
            param = param.substring(0, param.length() - 1);
        }
        methodBuilder.append("(").append(param).append("){\n");
        methodBuilder.append("  ").append(fragmentName).append(" ").append(fragmentInstanceName).append(" = ").append("new ").append(fragmentName).append("();\n");//声明fragment
        methodBuilder.append("  Bundle bundle = ").append("new ").append("Bundle();\n");
        methodBuilder.append(intentBuilder);
        methodBuilder.append("  ").append(fragmentInstanceName).append(".setArguments(bundle);\n");
        methodBuilder.append("  return ").append(fragmentInstanceName).append(";\n");
        methodBuilder.append("}");
        return methodBuilder.toString();
    }

    public static String paramName(String activityName) {
        String first = activityName.substring(0, 1);
        String realFirst = first.toLowerCase();
        return activityName.replace(first, realFirst);
    }
}
