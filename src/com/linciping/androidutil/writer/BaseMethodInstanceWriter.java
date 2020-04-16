package com.linciping.androidutil.writer;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.linciping.androidutil.bean.AndroidUtilComponent;
import com.linciping.androidutil.bean.InstanceMethodBean;
import com.linciping.androidutil.util.CheckUtil;
import com.linciping.androidutil.util.Util;

import java.util.List;

public abstract class BaseMethodInstanceWriter extends BaseCodeWriter {

    private InstanceMethodBean instanceMethodBean;
    private PsiClass constantClass;

    public BaseMethodInstanceWriter(PsiClass mClass, PsiFile psiFile, InstanceMethodBean instanceMethodBean) {
        super(mClass, psiFile);
        this.instanceMethodBean = instanceMethodBean;
        initConstantClass(getProject());
    }

    @Override
    protected void buildCode(PsiClass psiClass, PsiElementFactory psiElementFactory) {
        PsiMethod psiMethod = psiElementFactory.createMethodFromText(instanceMethodBean.getInstanceMethodCode(), psiClass);
        PsiMethod extraDataMethod = psiElementFactory.createMethodFromText(instanceMethodBean.getExtraSettingValueMethodCode(), psiClass);
        psiClass.add(extraDataMethod);
        psiClass.add(psiMethod);
        javaCodeStyleManager.addImport(psiFile, constantClass);
        for (PsiClass psiClass1 : getImportClass()) {
            javaCodeStyleManager.addImport(psiFile, psiClass1);
        }
        addConstantClass(psiElementFactory);
        formatCode();
    }

    private void addConstantClass(PsiElementFactory psiElementFactory) {
        List<String> constantNameList = instanceMethodBean.getConstantNameList();
        for (String constantName : constantNameList) {
            if (constantClass.findFieldByName(constantName, true) == null) {
                String constantField = String.format("public static final String %s=\"%s\";", constantName, constantName.toLowerCase());
                PsiField psiField = psiElementFactory.createFieldFromText(constantField, constantClass);
                constantClass.add(psiField);
            }
        }
    }

    private void initConstantClass(Project project) {
        AndroidUtilComponent androidUtilComponent = AndroidUtilComponent.getInstance(project);
        if (CheckUtil.isStringNoEmpty(androidUtilComponent.getConstantClassPath())) {
            VirtualFile virtualFile = Util.getVirtualFileByFile(androidUtilComponent.getConstantClassPath());
            if (virtualFile != null) {
                PsiJavaFile constantJavaFile = (PsiJavaFile) PsiManager.getInstance(project).findFile(virtualFile);
                constantClass = constantJavaFile.getClasses()[0];
                return;
            }
        }
        createConstantClass(androidUtilComponent);
    }

    private void createConstantClass(AndroidUtilComponent androidUtilComponent) {
        PsiDirectory psiDirectory = psiFile.getParent();
        assert psiDirectory != null;
        PsiClass psiClass = JavaDirectoryService.getInstance().createInterface(psiDirectory, "IntentKey");
        PsiJavaFile constantJavaFile = (PsiJavaFile) psiClass.getParent();
        androidUtilComponent.setConstantClassPath(constantJavaFile.getVirtualFile().getPath());
        constantClass = constantJavaFile.getClasses()[0];
    }

    protected abstract List<PsiClass> getImportClass();
}
