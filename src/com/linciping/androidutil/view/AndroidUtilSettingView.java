package com.linciping.androidutil.view;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.linciping.androidutil.bean.AndroidUtilComponent;
import com.linciping.androidutil.util.CheckUtil;
import com.linciping.androidutil.util.Util;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class AndroidUtilSettingView implements Configurable {

    private AndroidUtilComponent androidUtilComponent;
    private boolean isModified = false;
    private AndroidUtiSettingForm androidUtiSettingForm;
    private Project project;
    private VirtualFile virtualFile;

    public AndroidUtilSettingView(Project project) {
        this.project = project;
        androidUtilComponent = AndroidUtilComponent.getInstance(project);
        if (CheckUtil.isStringNoEmpty(androidUtilComponent.getResDirPath())) {
            virtualFile = Util.getVirtualFileByFile(androidUtilComponent.getResDirPath());
        }
        androidUtiSettingForm = new AndroidUtiSettingForm(androidUtilComponent);
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
                if (virtualFile != null) {
                    isModified = true;
                    androidUtiSettingForm.setResDir(virtualFile.getPath());
                    androidUtilComponent.setResDirPath(virtualFile.getPath());
                }
            });
        });
        androidUtiSettingForm.addReflectChangeListener(e -> {
            isModified = true;
            JCheckBox checkBox = (JCheckBox) e.getSource();
            androidUtilComponent.setReflect(checkBox.isSelected());
        });
        androidUtiSettingForm.addPresenterGenericChangeListener(e -> {
            isModified=true;
            JCheckBox checkBox = (JCheckBox) e.getSource();
            androidUtilComponent.setPresenterGeneric(checkBox.isSelected());
        });
        androidUtiSettingForm.addChooseConstantAction(e -> {
            Util.showSelectSingleClass(project, "请选择常量类", virtualFile -> {
                if (virtualFile != null) {
                    isModified = true;
                    androidUtiSettingForm.setConstantClassPath(virtualFile.getPath());
                    androidUtilComponent.setConstantClassPath(virtualFile.getPath());
                }
            });
        });
        androidUtiSettingForm.addChooseBasePresenterAction(e -> {
            if (virtualFile != null) {
                isModified = true;
                androidUtiSettingForm.setBasePresenterClassPath(virtualFile.getPath());
                androidUtilComponent.setBasePresenterClassPath(virtualFile.getPath());
            }
        });

        androidUtiSettingForm.addChooseBaseViewAction(e -> {
            if (virtualFile != null) {
                isModified = true;
                androidUtiSettingForm.setBaseViewClassPath(virtualFile.getPath());
                androidUtilComponent.setBaseViewClassPath(virtualFile.getPath());
            }
        });
        androidUtiSettingForm.addChooseBaseActivityAction(e -> {
            if (virtualFile != null) {
                isModified = true;
                androidUtiSettingForm.setBaseActivityClassPath(virtualFile.getPath());
                androidUtilComponent.setBaseActivityClassPath(virtualFile.getPath());
            }
        });
        return androidUtiSettingForm.getPlRoot();
    }

    @Override
    public boolean isModified() {
        return isModified;
    }

    @Override
    public void apply() {
        if (androidUtiSettingForm.getResDir() != null) {
            androidUtilComponent.setResDirPath(androidUtiSettingForm.getResDir());
        }
        if (CheckUtil.isStringNoEmpty(androidUtiSettingForm.getConstantClassPath())) {
            androidUtilComponent.setConstantClassPath(androidUtiSettingForm.getConstantClassPath());
        }
        if (CheckUtil.isStringNoEmpty(androidUtiSettingForm.getBasePresenterClassPath())){
            androidUtilComponent.setBasePresenterClassPath(androidUtiSettingForm.getBasePresenterClassPath());
        }
        if (CheckUtil.isStringNoEmpty(androidUtiSettingForm.getBaseViewClassPath())){
            androidUtilComponent.setBaseViewClassPath(androidUtiSettingForm.getBaseViewClassPath());
        }
        if (CheckUtil.isStringNoEmpty(androidUtiSettingForm.getBaseActivityClassPath())){
            androidUtilComponent.setBaseActivityClassPath(androidUtiSettingForm.getBaseViewClassPath());
        }
        androidUtilComponent.setReflect(androidUtiSettingForm.isReflect());
        isModified = false;
    }
}
