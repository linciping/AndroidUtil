package com.linciping.androidutil.util;

import com.linciping.androidutil.bean.MethodParam;

import java.util.List;

public class CodeUtil {

    public static String createStartActivityMethod(List<MethodParam> methodParamList, String activityName) {
        StringBuilder methodBuilder = new StringBuilder("public static void startActivity");
        StringBuilder paramBuilder = new StringBuilder("Context context");
        StringBuilder intentBuilder = new StringBuilder("");
        for (MethodParam methodParam : methodParamList) {
            if (methodParam.isSelected()) {
                paramBuilder.append(",").append(methodParam.getParamType()).append(" ").append(methodParam.getParamName());
                intentBuilder.append("  intent.putExtra(\"").append(methodParam.getParamName()).append("\",").append(methodParam.getParamName()).append(");\n");
            }
        }
        String param = paramBuilder.toString();
        methodBuilder.append("(").append(param).append("){\n");
        methodBuilder.append("  Intent intent = ").append("new ").append("Intent(").append("context,").append(activityName).append(".class").append(");\n");
        methodBuilder.append(intentBuilder);
        methodBuilder.append("  context.startActivity(intent);\n");
        methodBuilder.append("}");
        return methodBuilder.toString();
    }


    public static String createFragmentInstanceMethod(List<MethodParam> methodParamList, String fragmentName) {
        StringBuilder methodBuilder = new StringBuilder("public static void newInstance");
        StringBuilder paramBuilder = new StringBuilder("");
        StringBuilder intentBuilder = new StringBuilder("");
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
