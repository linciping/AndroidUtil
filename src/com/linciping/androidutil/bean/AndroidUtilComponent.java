package com.linciping.androidutil.bean;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

@State(name = "ProjectResDirConfiguration", storages = {@Storage(value = "projectResDirConfiguration.xml")})
public class AndroidUtilComponent implements ProjectComponent, Serializable, PersistentStateComponent<AndroidUtilComponent> {

    private String resDirPath = "";
    private boolean isReflect = true;
    private String constantClassPath = "";
    private String basePresenterClassPath="";
    private String baseViewClassPath="";
    private boolean presenterGeneric;

    public String getBasePresenterClassPath() {
        return basePresenterClassPath;
    }

    public void setBasePresenterClassPath(String basePresenterClassPath) {
        this.basePresenterClassPath = basePresenterClassPath;
    }

    public String getBaseViewClassPath() {
        return baseViewClassPath;
    }

    public void setBaseViewClassPath(String baseViewClassPath) {
        this.baseViewClassPath = baseViewClassPath;
    }

    public String getConstantClassPath() {
        return constantClassPath;
    }

    public void setConstantClassPath(String constantClassPath) {
        this.constantClassPath = constantClassPath;
    }

    public boolean isReflect() {
        return isReflect;
    }

    public void setReflect(boolean reflect) {
        isReflect = reflect;
    }

    public String getResDirPath() {
        return resDirPath;
    }

    public void setResDirPath(String resDirPath) {
        this.resDirPath = resDirPath;
    }

    public static AndroidUtilComponent getInstance(Project project) {
        return project.getComponent(AndroidUtilComponent.class);
    }

    public boolean isPresenterGeneric() {
        return presenterGeneric;
    }

    public void setPresenterGeneric(boolean presenterGeneric) {
        this.presenterGeneric = presenterGeneric;
    }

    @Nullable
    @Override
    public AndroidUtilComponent getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull AndroidUtilComponent androidUtilComponent) {
        XmlSerializerUtil.copyBean(androidUtilComponent, this);
    }
}
