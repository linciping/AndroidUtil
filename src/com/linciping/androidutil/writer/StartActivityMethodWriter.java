package com.linciping.androidutil.writer;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.search.EverythingGlobalScope;
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
        PsiClass constantClass=constantJavaFile.getClasses()[0];
        PsiClass contextClass = JavaPsiFacade.getInstance(mProject).findClass(
                "android.app.Activity", new EverythingGlobalScope(mProject));
        PsiClass intentClass = JavaPsiFacade.getInstance(mProject).findClass(
                "android.app.Activity", new EverythingGlobalScope(mProject));
        javaCodeStyleManager.addImport(psiFile,constantClass);
        if (contextClass!=null){
            javaCodeStyleManager.addImport(psiFile,contextClass);
        }
        if (intentClass!=null){
            javaCodeStyleManager.addImport(psiFile,intentClass);
        }
        formatCode();
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
