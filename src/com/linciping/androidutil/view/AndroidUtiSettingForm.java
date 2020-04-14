package com.linciping.androidutil.view;

import com.intellij.openapi.vfs.VirtualFile;
import com.linciping.androidutil.bean.SettingProjectResDirComponent;
import com.linciping.androidutil.util.CheckUtil;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionListener;

public class AndroidUtiSettingForm {
    private JPanel plRoot;
    private JPanel plCopyRes2Project;
    private JTextField tvResDir;
    private JButton btnChooseResDir;
    private JCheckBox cbIsReflect;

    public AndroidUtiSettingForm(SettingProjectResDirComponent settingProjectResDirComponent) {
        if (CheckUtil.isStringNoEmpty(settingProjectResDirComponent.getResDirPath())){
            tvResDir.setText(settingProjectResDirComponent.getResDirPath());
        }
        cbIsReflect.setSelected(settingProjectResDirComponent.isReflect());
    }

    public void addChooseResDirAction(ActionListener actionListener){
        btnChooseResDir.addActionListener(actionListener);
    }

    public void addReflectChangeListener(ChangeListener changeListener){
        cbIsReflect.addChangeListener(changeListener);
    }

    public JPanel getPlRoot() {
        return plRoot;
    }

    @Nullable
    public String getResDir() {
        if (tvResDir.getText()!=null&&!tvResDir.getText().isEmpty()){
            return tvResDir.getText();
        }
        return null;
    }

    public boolean isReflect() {
        return cbIsReflect.isSelected();
    }

    public void setResDir(String path) {
        tvResDir.setText(path);
    }
}
