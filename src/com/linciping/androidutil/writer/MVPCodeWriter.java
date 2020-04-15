package com.linciping.androidutil.writer;

import com.intellij.codeInsight.actions.ReformatCodeProcessor;
import com.intellij.ide.actions.NewActionGroup;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.fileTypes.StdFileTypes;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import com.intellij.psi.impl.file.PsiDirectoryFactory;
import com.linciping.androidutil.bean.AndroidUtilComponent;
import com.linciping.androidutil.util.CheckUtil;
import com.linciping.androidutil.util.Util;

public class MVPCodeWriter extends WriteCommandAction.Simple {

    private PsiDirectory psiDirectory;
    private Project project;
    private String activityClassName;
    private String presenterClassName;
    private String viewClassName;
    private String presenterMethodName;
    private String viewMethodName;

    public MVPCodeWriter(Project project, PsiDirectory psiDirectory, String activityClassName, String presenterClassName, String viewClassName, String presenterMethodName, String viewMethodName) {
        super(project, "mvp code writer");
        this.project = project;
        this.psiDirectory = psiDirectory;
        this.activityClassName = activityClassName;
        this.presenterClassName = presenterClassName;
        this.viewClassName = viewClassName;
        this.presenterMethodName = presenterMethodName;
        this.viewMethodName = viewMethodName;
    }

    @Override
    protected void run() throws Throwable {
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
