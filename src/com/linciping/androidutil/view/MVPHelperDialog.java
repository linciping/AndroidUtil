package com.linciping.androidutil.view;

import com.intellij.openapi.project.Project;
import com.linciping.androidutil.listener.OnClickListener;
import com.linciping.androidutil.util.CheckUtil;
import com.linciping.androidutil.util.Util;

import javax.swing.*;

public class MVPHelperDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField tvModuleName;
    private JTextField tvPresenterMethod;
    private JTextField tvViewMethod;

    private Project project;

    public MVPHelperDialog(Project project) {
        this.project = project;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        buttonCancel.addActionListener(e -> dispose());
    }

    public void addOKAction(OnClickListener<String> onClickListener){
        buttonOK.addActionListener(e -> {
            if (CheckUtil.isStringEmpty(tvModuleName.getText())){
                Util.showNotification("模块名不能为空",project);
                return;
            }
            if (CheckUtil.isStringEmpty(tvPresenterMethod.getText())){
                Util.showNotification("presenter方法不能为空",project);
                return;
            }
            if (CheckUtil.isStringEmpty(tvViewMethod.getText())){
                Util.showNotification("view方法不能为空",project);
                return;
            }
            onClickListener.onClick(tvModuleName.getText(),tvPresenterMethod.getText(),tvViewMethod.getText());
            dispose();
        });
    }
}
