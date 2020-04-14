package com.linciping.androidutil.view;

import com.linciping.androidutil.bean.AndroidUtilComponent;
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
    private JTextField tvConstantClass;
    private JButton btnChooseConstantClass;

    public AndroidUtiSettingForm(AndroidUtilComponent androidUtilComponent) {
        if (CheckUtil.isStringNoEmpty(androidUtilComponent.getResDirPath())) {
            tvResDir.setText(androidUtilComponent.getResDirPath());
        }
        if (CheckUtil.isStringNoEmpty(androidUtilComponent.getConstantClassPath())) {
            tvConstantClass.setText(androidUtilComponent.getConstantClassPath());
        }
        cbIsReflect.setSelected(androidUtilComponent.isReflect());
    }

    public void addChooseResDirAction(ActionListener actionListener) {
        btnChooseResDir.addActionListener(actionListener);
    }

    public void addReflectChangeListener(ChangeListener changeListener) {
        cbIsReflect.addChangeListener(changeListener);
    }

    public void addChooseConstantAction(ActionListener actionListener) {
        btnChooseConstantClass.addActionListener(actionListener);
    }

    public JPanel getPlRoot() {
        return plRoot;
    }

    @Nullable
    public String getResDir() {
        if (tvResDir.getText() != null && !tvResDir.getText().isEmpty()) {
            return tvResDir.getText();
        }
        return null;
    }

    public void setResDir(String path) {
        tvResDir.setText(path);
    }

    public boolean isReflect() {
        return cbIsReflect.isSelected();
    }

    @Nullable
    public String getConstantClassPath() {
        if (tvConstantClass.getText() != null && !tvConstantClass.getText().isEmpty()) {
            return tvConstantClass.getText();
        }
        return null;
    }

    public void setConstantClassPath(String path) {
        tvConstantClass.setText(path);
    }
}
