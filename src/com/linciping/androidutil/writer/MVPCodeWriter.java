package com.linciping.androidutil.writer;

import com.intellij.openapi.fileTypes.StdFileTypes;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;

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
        StringBuilder classContent = new StringBuilder("package com.linciping.copyres;\n");
        classContent.append("public class Demo{\n");
        classContent.append("}");
        PsiFile psiFile = PsiFileFactory.getInstance(project).createFileFromText("Demo.java", StdFileTypes.JAVA, classContent.toString());
        psiDirectory.add(psiFile);
//        PsiDirectory[] psiDirectorys= psiDirectory.getSubdirectories();
//        PsiDirectory presenterDirectory=null;
//        PsiDirectory viewDirectory=null;
//        if (psiDirectorys.length > 0){
//            for (PsiDirectory psiDirectory:psiDirectorys){
//                if (psiDirectory.getName().equals("p")||psiDirectory.getName().equals("presenter")){
//                    presenterDirectory=psiDirectory;
//                }
//                if (psiDirectory.getName().equals("v")||psiDirectory.getName().equals("view")){
//                    viewDirectory=psiDirectory;
//                }
//            }
//        }
//        if (presenterDirectory==null){
//            presenterDirectory= psiDirectory.createSubdirectory("p");
//        }
//        if (viewDirectory==null){
//            viewDirectory= psiDirectory.createSubdirectory("v");
//        }
//        AndroidUtilComponent androidUtilComponent=AndroidUtilComponent.getInstance(project);
//        PsiClass presenterClass= JavaDirectoryService.getInstance().createClass(presenterDirectory,presenterClassName);
//        if (CheckUtil.isStringNoEmpty(androidUtilComponent.getBasePresenterClassPath())){
//            String className= Util.getFileNameByPath(androidUtilComponent.getBasePresenterClassPath());
//            presenterClass.getSuperClass()
//        }
//        PsiClass viewInterface= JavaDirectoryService.getInstance().createInterface(viewDirectory,viewClassName);
    }
}
