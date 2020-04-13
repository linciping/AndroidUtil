package com.linciping.androidutil.view;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.linciping.androidutil.bean.SettingProjectResDirComponent;
import com.linciping.androidutil.util.CheckUtil;
import com.linciping.androidutil.util.Util;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.File;

public class AndroidUtilSettingView implements Configurable {

    private SettingProjectResDirComponent settingProjectResDirComponent;
    private boolean isModified=false;
    private AndroidUtiSettingForm androidUtiSettingForm;
    private Project project;
    private VirtualFile virtualFile;

    public AndroidUtilSettingView(Project project) {
        this.project=project;
        settingProjectResDirComponent=SettingProjectResDirComponent.getInstance(project);
        if (CheckUtil.isStringNoEmpty(settingProjectResDirComponent.getResDirPath())){
            File file=new File(settingProjectResDirComponent.getResDirPath());
            virtualFile= LocalFileSystem.getInstance().findFileByIoFile(file);
            if (virtualFile==null){
                virtualFile= LocalFileSystem.getInstance().refreshAndFindFileByIoFile(file);
            }
        }
        androidUtiSettingForm=new AndroidUtiSettingForm(settingProjectResDirComponent);
    }

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "AndroidUtilSetting";
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        androidUtiSettingForm.addChooseResDirAction(e -> {
            Util.showSelectSingleFile(project,"请选择默认资源目录", virtualFile,virtualFile -> {
                if (virtualFile!=null){
                    isModified=true;
                    androidUtiSettingForm.setResDir(virtualFile.getPath());
                }
            });
        });
        androidUtiSettingForm.addReflectChangeListener(e -> {
            isModified=true;
            JCheckBox checkBox = (JCheckBox) e.getSource();
            settingProjectResDirComponent.setReflect(checkBox.isSelected());
        });
        return androidUtiSettingForm.getPlRoot();
    }

    @Override
    public boolean isModified() {
        return isModified;
    }

    @Override
    public void apply() throws ConfigurationException {
        if (androidUtiSettingForm.getResDir()!=null){
            settingProjectResDirComponent.setResDirPath(androidUtiSettingForm.getResDir());
        }
        settingProjectResDirComponent.setReflect(androidUtiSettingForm.isReflect());
        isModified=false;
    }
}
