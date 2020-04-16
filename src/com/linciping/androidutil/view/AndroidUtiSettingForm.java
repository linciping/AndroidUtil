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
    private JTextField tvPresenterBase;
    private JTextField tvViewBase;
    private JButton btnChoosePresenterBase;
    private JButton btnChooseViewBase;
    private JCheckBox cbPresenterGeneric;

    public AndroidUtiSettingForm(AndroidUtilComponent androidUtilComponent) {
        if (CheckUtil.isStringNoEmpty(androidUtilComponent.getResDirPath())) {
            tvResDir.setText(androidUtilComponent.getResDirPath());
        }
        if (CheckUtil.isStringNoEmpty(androidUtilComponent.getConstantClassPath())) {
            tvConstantClass.setText(androidUtilComponent.getConstantClassPath());
        }
        if (CheckUtil.isStringNoEmpty(androidUtilComponent.getBasePresenterClassPath())){
            tvPresenterBase.setText(androidUtilComponent.getBasePresenterClassPath());
        }
        if (CheckUtil.isStringNoEmpty(androidUtilComponent.getBaseViewClassPath())){
            tvViewBase.setText(androidUtilComponent.getBaseViewClassPath());
        }
        cbIsReflect.setSelected(androidUtilComponent.isReflect());
        cbPresenterGeneric.setSelected(androidUtilComponent.isPresenterGeneric());
    }

    public void addChooseResDirAction(ActionListener actionListener) {
        btnChooseResDir.addActionListener(actionListener);
    }

    public void addReflectChangeListener(ChangeListener changeListener) {
        cbIsReflect.addChangeListener(changeListener);
    }

    public void addPresenterGenericChangeListener(ChangeListener changeListener) {
        cbPresenterGeneric.addChangeListener(changeListener);
    }

    public void addChooseConstantAction(ActionListener actionListener) {
        btnChooseConstantClass.addActionListener(actionListener);
    }

    public void addChooseBasePresenterAction(ActionListener actionListener){
        btnChoosePresenterBase.addActionListener(actionListener);
    }

    public void addChooseBaseViewAction(ActionListener actionListener){
        btnChooseViewBase.addActionListener(actionListener);
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

    public boolean isPresenterGeneric(){
        return cbPresenterGeneric.isSelected();
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

    @Nullable
    public String getBasePresenterClassPath(){
        if (CheckUtil.isStringNoEmpty(tvPresenterBase.getText())){
            return tvPresenterBase.getText();
        }
        return null;
    }

    public void setBasePresenterClassPath(String basePresenterClassPath){
        tvPresenterBase.setText(basePresenterClassPath);
    }

    @Nullable
    public String getBaseViewClassPath(){
        if (CheckUtil.isStringNoEmpty(tvViewBase.getText())){
            return tvViewBase.getText();
        }
        return null;
    }

    public void setBaseViewClassPath(String baseViewClassPath){
        tvViewBase.setText(baseViewClassPath);
    }
}
