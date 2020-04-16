package com.linciping.androidutil.util;

import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.search.EverythingGlobalScope;
import com.linciping.androidutil.bean.InstanceMethodBean;
import com.linciping.androidutil.bean.MethodParam;
import com.linciping.androidutil.constant.Definitions;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CodeUtil {

    public static InstanceMethodBean createStartActivityMethod(List<MethodParam> methodParamList, String activityName, String constantClassName, Project project) {
        PsiClass psiSerializableClass = JavaPsiFacade.getInstance(project).findClass("java.io.Serializable", new EverythingGlobalScope(project));
        PsiClass psiParcelableClass = JavaPsiFacade.getInstance(project).findClass("android.os.Parcelable", new EverythingGlobalScope(project));
        StringBuilder startActivityMethodBuilder = new StringBuilder("public static void startActivity");
        StringBuilder extraSettingValueMethodCodeBuilder = new StringBuilder("public void initExtraData(){\n");
        StringBuilder paramBuilder = new StringBuilder("Context context");
        StringBuilder intentBuilder = new StringBuilder();
        final String getIntentFlat = "getIntent().";
        StringBuilder extraDataStatementBuilder = new StringBuilder();
        List<String> constantList = new ArrayList<>();
        for (MethodParam methodParam : methodParamList) {
            if (methodParam.isSelected()) {
                String extraType = Definitions.getExtraTypeName(methodParam.getParamTypeName());
                paramBuilder.append(",").append(methodParam.getParamTypeName()).append(" ").append(methodParam.getParamName());
                String paramName = buildConstantParamName(methodParam.getParamName());
                String constantName = constantClassName + "." + paramName;
                constantList.add(paramName);
                if (CheckUtil.isStringNoEmpty(extraType)) {
                    extraDataStatementBuilder.append("   ").append(methodParam.getParamName()).append(" = ")
                            .append(getIntentFlat).append("get").append(extraType).append("Extra(").append(constantName).append(");\n");
                } else {
                    String paramTypeClassName = methodParam.getParamType().getCanonicalText();
                    int index = paramTypeClassName.indexOf("<");
                    if (index != -1) {
                        paramTypeClassName = paramTypeClassName.substring(0, index);
                    }
                    PsiClass psiClass = JavaPsiFacade.getInstance(project).findClass(paramTypeClassName, new EverythingGlobalScope(project));
                    if (psiClass != null) {
                        if (psiSerializableClass != null && psiClass.isInheritor(psiSerializableClass, true)) {
                            extraDataStatementBuilder.append("   ").append(methodParam.getParamName()).append(" = ").append("(").append(methodParam.getParamTypeName()).append(")")
                                    .append(getIntentFlat).append("getSerializableExtra(").append(constantName).append(");\n");
                        } else if (psiParcelableClass != null && psiClass.isInheritor(psiParcelableClass, true)) {
                            extraDataStatementBuilder.append("   ").append(methodParam.getParamName()).append(" = ").append(getIntentFlat).append("(getParcelableExtra").append(constantName).append(");\n");
                        } else {
                            extraDataStatementBuilder.append("   ").append(methodParam.getParamName()).append(" = ").append("(").append(methodParam.getParamTypeName()).append(")\n")
                                    .append(getIntentFlat).append("getSerializableExtra(").append(constantName).append(");");
                        }
                    } else {
                        extraDataStatementBuilder.append("   ").append(methodParam.getParamName()).append(" = ").append("(").append(methodParam.getParamTypeName()).append(")\n")
                                .append(getIntentFlat).append("getSerializableExtra(").append(constantName).append(");");
                    }
                }
                intentBuilder.append("  intent.putExtra(").append(constantName).append(",").append(methodParam.getParamName()).append(");\n");
            }
        }
        String param = paramBuilder.toString();
        startActivityMethodBuilder.append("(").append(param).append("){\n");
        startActivityMethodBuilder.append("  Intent intent = ").append("new ").append("Intent(").append("context,").append(activityName).append(".class").append(");\n");
        startActivityMethodBuilder.append(intentBuilder);
        startActivityMethodBuilder.append("  context.startActivity(intent);\n");
        startActivityMethodBuilder.append("}");

        extraSettingValueMethodCodeBuilder.append(extraDataStatementBuilder);
        extraSettingValueMethodCodeBuilder.append("}");
        return new InstanceMethodBean(startActivityMethodBuilder.toString(), extraSettingValueMethodCodeBuilder.toString(), constantList);
    }

    public static String buildConstantParamName(String content) {
        List<String> paramNameList = getParamNameList(content);
        StringBuilder constantParamName = new StringBuilder();
        for (String paramName : paramNameList) {
            constantParamName.append(paramName.toUpperCase()).append("_");
        }
        return constantParamName.substring(0, constantParamName.length() - 1);
    }

    public static List<String> getParamNameList(String paramName) {
        List<String> paramNameList = new ArrayList<>();
        Pattern pattern = Pattern.compile("([a-z])([A-Z])");
        Matcher matcher = pattern.matcher(paramName);
        int start = 0, end = 0;
        while (matcher.find()) {
            start = end;
            end = matcher.start() + 1;
            paramNameList.add(paramName.substring(start, end));
        }
        int length = paramName.length();
        if (end < length) {
            paramNameList.add(paramName.substring(end, length));
        }
        return paramNameList;
    }

    public static InstanceMethodBean createFragmentInstanceMethod(List<MethodParam> methodParamList, String fragmentName, String constantClassName, Project project) {
        StringBuilder extraSettingValueMethodCodeBuilder = new StringBuilder("public void initExtraData(Bundle args){\n");
        final String argsFlag = "args.";
        StringBuilder extraDataStatementBuilder = new StringBuilder();
        PsiClass psiSerializableClass = JavaPsiFacade.getInstance(project).findClass("java.io.Serializable", new EverythingGlobalScope(project));
        PsiClass psiParcelableClass = JavaPsiFacade.getInstance(project).findClass("android.os.Parcelable", new EverythingGlobalScope(project));
        String fragmentInstanceName = Util.paramName(fragmentName);
        StringBuilder methodBuilder = new StringBuilder("public static ").append(fragmentName).append(" newInstance");
        StringBuilder paramBuilder = new StringBuilder();
        StringBuilder intentBuilder = new StringBuilder();
        List<String> constantList = new ArrayList<>();
        for (MethodParam methodParam : methodParamList) {
            if (methodParam.isSelected()) {
                String paramType = methodParam.getParamTypeName();
                paramBuilder.append(paramType).append(" ").append(methodParam.getParamName()).append(",");
                String extraType = Definitions.getExtraTypeName(paramType);
                String paramName = buildConstantParamName(methodParam.getParamName());
                String constantName = constantClassName + "." + paramName;
                constantList.add(paramName);
                if (CheckUtil.isStringNoEmpty(extraType)) {
                    intentBuilder.append("  bundle.put").append(extraType).append("(").append(constantName).append(",").append(methodParam.getParamName()).append(");\n");
                    extraDataStatementBuilder.append("   ").append(methodParam.getParamName()).append(" = ")
                            .append(argsFlag).append("get").append(extraType).append("(").append(constantName).append(");\n");
                } else {
                    String paramTypeClassName = methodParam.getParamType().getCanonicalText();
                    int index = paramTypeClassName.indexOf("<");
                    if (index != -1) {
                        paramTypeClassName = paramTypeClassName.substring(0, index);
                    }
                    PsiClass psiClass = JavaPsiFacade.getInstance(project).findClass(paramTypeClassName, new EverythingGlobalScope(project));
                    if (psiClass != null) {
                        if (psiSerializableClass != null && psiClass.isInheritor(psiSerializableClass, true)) {
                            intentBuilder.append("  bundle.putSerializable(").append(constantName).append(", ").append(methodParam.getParamName()).append(");\n");
                            extraDataStatementBuilder.append("   ").append(methodParam.getParamName()).append(" = ").append("(").append(methodParam.getParamTypeName()).append(")")
                                    .append(argsFlag).append("getSerializable(").append(constantName).append(");\n");
                        } else if (psiParcelableClass != null && psiClass.isInheritor(psiParcelableClass, true)) {
                            intentBuilder.append("  bundle.putParcelable(").append(constantName).append(", ").append(methodParam.getParamName()).append(");\n");
                            extraDataStatementBuilder.append("   ").append(methodParam.getParamName()).append(" = ").append(argsFlag).append("(getParcelable").append(constantName).append(");\n");
                        } else {
                            intentBuilder.append("  bundle.putSerializable(").append(constantName).append(",").append(" (Serializable) ").append(methodParam.getParamName()).append(");\n");
                            extraDataStatementBuilder.append("   ").append(methodParam.getParamName()).append(" = ").append("(").append(methodParam.getParamTypeName()).append(")")
                                    .append(argsFlag).append("getSerializable(").append(constantName).append(");\n");
                        }
                    } else {
                        intentBuilder.append("  bundle.putSerializable(").append(constantName).append(",").append(" (Serializable) ").append(methodParam.getParamName()).append(");\n");
                        extraDataStatementBuilder.append("   ").append(methodParam.getParamName()).append(" = ").append("(").append(methodParam.getParamTypeName()).append(")")
                                .append(argsFlag).append("getSerializable(").append(constantName).append(");\n");
                    }
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

        extraSettingValueMethodCodeBuilder.append(extraDataStatementBuilder);
        extraSettingValueMethodCodeBuilder.append("}");
        return new InstanceMethodBean(methodBuilder.toString(), extraSettingValueMethodCodeBuilder.toString(), constantList);
    }
}
