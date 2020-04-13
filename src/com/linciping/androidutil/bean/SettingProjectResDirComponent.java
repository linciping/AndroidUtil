package com.linciping.androidutil.bean;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

@State(name = "ProjectResDirConfiguration",storages = {@Storage(value = "projectResDirConfiguration.xml")})
public class SettingProjectResDirComponent implements ProjectComponent, Serializable, PersistentStateComponent<SettingProjectResDirComponent> {

    private String resDirPath="";
    private boolean isReflect=true;


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

    public static SettingProjectResDirComponent getInstance(Project project){
        return project.getComponent(SettingProjectResDirComponent.class);
    }

    @Nullable
    @Override
    public SettingProjectResDirComponent getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull SettingProjectResDirComponent settingProjectResDirComponent) {
        XmlSerializerUtil.copyBean(settingProjectResDirComponent,this);
    }
}
