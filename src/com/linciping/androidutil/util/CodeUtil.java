package com.linciping.androidutil.util;

import com.linciping.androidutil.bean.InstanceMethodBean;
import com.linciping.androidutil.bean.MethodParam;

import java.util.ArrayList;
import java.util.List;

public class CodeUtil {

    public static InstanceMethodBean createStartActivityMethod(List<MethodParam> methodParamList, String activityName, String contentClassName) {
        StringBuilder methodBuilder = new StringBuilder("public static void startActivity");
        StringBuilder paramBuilder = new StringBuilder("Context context");
        StringBuilder intentBuilder = new StringBuilder();
        List<String> constantList = new ArrayList<>();
        for (MethodParam methodParam : methodParamList) {
            if (methodParam.isSelected()) {
                paramBuilder.append(",").append(methodParam.getParamType()).append(" ").append(methodParam.getParamName());
                String constantName = buildConstantName(contentClassName, methodParam.getParamName());
                constantList.add(constantName);
                intentBuilder.append("  intent.putExtra(").append(buildConstantName(contentClassName, methodParam.getParamName())).append(",").append(methodParam.getParamName()).append(");\n");
            }
        }
        String param = paramBuilder.toString();
        methodBuilder.append("(").append(param).append("){\n");
        methodBuilder.append("  Intent intent = ").append("new ").append("Intent(").append("context,").append(activityName).append(".class").append(");\n");
        methodBuilder.append(intentBuilder);
        methodBuilder.append("  context.startActivity(intent);\n");
        methodBuilder.append("}");
        return new InstanceMethodBean(methodBuilder.toString(), constantList);
    }


    private static String buildConstantName(String contentClassName, String paramName) {
        paramName = paramName.toUpperCase();
        return contentClassName + "." + paramName;
    }


    public static String createFragmentInstanceMethod(List<MethodParam> methodParamList, String fragmentName) {
        StringBuilder methodBuilder = new StringBuilder("public static void newInstance");
        StringBuilder paramBuilder = new StringBuilder();
        StringBuilder intentBuilder = new StringBuilder();
        for (MethodParam methodParam : methodParamList) {
            if (methodParam.isSelected()) {
                paramBuilder.append(methodParam.getParamType()).append(" ").append(methodParam.getParamName()).append(",");
                intentBuilder.append("  intent.putExtra(\"").append(methodParam.getParamName()).append("\",").append(methodParam.getParamName()).append(");\n");
            }
        }
        String param = paramBuilder.toString();
        methodBuilder.append("(").append(param).append("){\n");
        methodBuilder.append("  Intent intent = ").append("new ").append("Intent(").append("context,").append(fragmentName).append(".class").append(");\n");
        methodBuilder.append(intentBuilder);
        methodBuilder.append("  context.startActivity(intent);\n");
        methodBuilder.append("}");
        return methodBuilder.toString();
    }
}
