package com.linciping.androidutil.writer;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.linciping.androidutil.bean.AndroidUtilComponent;
import com.linciping.androidutil.bean.InstanceMethodBean;
import com.linciping.androidutil.util.CheckUtil;
import com.linciping.androidutil.util.Util;

public class StartActivityMethodWriter extends BaseCodeWriter {

    private InstanceMethodBean instanceMethodBean;
    private PsiJavaFile constantJavaFile;

    public StartActivityMethodWriter(PsiClass mClass, PsiFile psiFile, InstanceMethodBean instanceMethodBean) {
        super(mClass, psiFile);
        this.instanceMethodBean = instanceMethodBean;
        initConstantClass(getProject());
    }

    @Override
    protected void buildCode(PsiClass psiClass, PsiElementFactory psiElementFactory) {
        PsiMethod psiMethod = psiElementFactory.createMethodFromText(instanceMethodBean.getCode(), psiClass);
        psiClass.add(psiMethod);
    }

    private void initConstantClass(Project project) {
        AndroidUtilComponent androidUtilComponent = AndroidUtilComponent.getInstance(project);
        if (CheckUtil.isStringNoEmpty(androidUtilComponent.getConstantClassPath())) {
            VirtualFile virtualFile = Util.getVirtualFileByFile(androidUtilComponent.getConstantClassPath());
            if (virtualFile != null) {
                constantJavaFile = (PsiJavaFile) PsiManager.getInstance(project).findFile(virtualFile);
                return;
            }
        }
        createConstantClass(androidUtilComponent);
    }

    private void createConstantClass(AndroidUtilComponent androidUtilComponent) {
        PsiDirectory psiDirectory = psiFile.getParent();
        assert psiDirectory != null;
        PsiClass psiClass = JavaDirectoryService.getInstance().createInterface(psiDirectory, "IntentKey");
        constantJavaFile = (PsiJavaFile) psiClass.getParent();
        androidUtilComponent.setConstantClassPath(constantJavaFile.getVirtualFile().getPath());
    }

}
