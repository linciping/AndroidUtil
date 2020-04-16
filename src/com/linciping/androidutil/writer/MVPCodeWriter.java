package com.linciping.androidutil.writer;

import com.intellij.openapi.fileTypes.StdFileTypes;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.linciping.androidutil.bean.AndroidUtilComponent;
import com.linciping.androidutil.util.CheckUtil;
import com.linciping.androidutil.util.CodeUtil;
import com.linciping.androidutil.util.Util;

public class MVPCodeWriter extends BaseWriterRunnable {

    private PsiDirectory psiDirectory;
    private Project project;
    private String activityClassName;
    private String presenterClassName;
    private String viewClassName;
    private String presenterMethodName;
    private String viewMethodName;

    public MVPCodeWriter(Project project, PsiDirectory psiDirectory, String activityClassName, String presenterClassName, String viewClassName, String presenterMethodName, String viewMethodName) {
        super(project);
        this.project = project;
        this.psiDirectory = psiDirectory;
        this.activityClassName = activityClassName;
        this.presenterClassName = presenterClassName;
        this.viewClassName = viewClassName;
        this.presenterMethodName = presenterMethodName;
        this.viewMethodName = viewMethodName;
    }

    @Override
    public void run() {
        PsiElementFactory psiElementFactory = JavaPsiFacade.getElementFactory(project);
        PsiDirectory[] psiDirectorys= psiDirectory.getSubdirectories();
        PsiDirectory presenterDirectory=null;
        PsiDirectory viewDirectory=null;
        if (psiDirectorys.length > 0){
            for (PsiDirectory psiDirectory:psiDirectorys){
                if (psiDirectory.getName().equals("p")||psiDirectory.getName().equals("presenter")){
                    presenterDirectory=psiDirectory;
                }
                if (psiDirectory.getName().equals("v")||psiDirectory.getName().equals("view")){
                    viewDirectory=psiDirectory;
                }
            }
        }
        if (presenterDirectory==null){
            presenterDirectory= psiDirectory.createSubdirectory("p");
        }
        if (viewDirectory==null){
            viewDirectory= psiDirectory.createSubdirectory("v");
        }
        AndroidUtilComponent androidUtilComponent=AndroidUtilComponent.getInstance(project);
        String activitySuperClassName="";
        if (CheckUtil.isStringNoEmpty(androidUtilComponent.getBaseActivityClassPath())){
            activitySuperClassName= Util.getFileNameByPath(androidUtilComponent.getBaseActivityClassPath());
        }
        PsiFile activityClass= CodeUtil.createJavaClass(project,activityClassName,"com.",activitySuperClassName);
        String presenterSuperClassName="";
        if (CheckUtil.isStringNoEmpty(androidUtilComponent.getBasePresenterClassPath())){
            presenterSuperClassName= Util.getFileNameByPath(androidUtilComponent.getBasePresenterClassPath());
        }
        psiDirectory.add(activityClass);
        PsiFile presenterClass= CodeUtil.createJavaClass(project,presenterClassName,"com.",presenterSuperClassName);
        presenterDirectory.add(presenterClass);
        String viewSuperClassName="";
        if (CheckUtil.isStringNoEmpty(androidUtilComponent.getBaseViewClassPath())){
            viewSuperClassName= Util.getFileNameByPath(androidUtilComponent.getBaseViewClassPath());
        }
        PsiFile viewInterface= CodeUtil.createJavaInterface(project,viewClassName,"com.",viewSuperClassName);
        viewDirectory.add(viewInterface);
    }
}
