package com.linciping.androidutil.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionListener;

public class StartActivityMethodDialog extends JDialog {
    private JPanel contentPane;
    private JButton btnOk;
    private JButton btnCancel;
    private JTable tlField;
    private JTextArea tvCode;

    public StartActivityMethodDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(btnOk);
        btnCancel.addActionListener(e -> {
            dispose();
        });
    }

    public void setOkActionListener(ActionListener actionListener){
        btnOk.addActionListener(actionListener);
    }

    public void setModel(DefaultTableModel tableModel) {
        tlField.setModel(tableModel);
    }

    public void setCode(String startActivityMethod) {
        tvCode.setText(startActivityMethod);
    }

    public String getCode(){
       return tvCode.getText();
    }
}
