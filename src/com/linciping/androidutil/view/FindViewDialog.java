package com.linciping.androidutil.view;

import com.intellij.ide.util.PropertiesComponent;
import com.linciping.androidutil.bean.PropertiesKey;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;

public class FindViewDialog extends JDialog {
    private JPanel contentPane;
    public JButton btnCopyCode;
    public JButton btnClose;
    public JTextArea textCode;
    public JCheckBox chbAddM;
    public JTable tableViews;
    public JButton btnSelectAll;
    public JButton btnSelectNone;
    public JButton btnNegativeSelect;
    private JCheckBox chbIsViewHolder;
    private JCheckBox chbIsTarget26;

    private OnClickListener onClickListener;

    public FindViewDialog() {
        setContentPane(contentPane);
        setModal(true);
        initStatus();
        btnCopyCode.addActionListener(e -> {
            if (onClickListener != null) {
                onClickListener.onOK();
            }
            onCancel();
        });
        chbAddM.addChangeListener(e -> {
            if (onClickListener != null) {
                onClickListener.onSwitchAddM(chbAddM.isSelected());
                PropertiesComponent.getInstance().setValue(PropertiesKey.SAVE_ADD_M_ACTION, chbAddM.isSelected());
            }
        });
        chbIsViewHolder.addChangeListener(e -> onClickListener.onSwitchIsViewHolder(chbIsViewHolder.isSelected()));
        chbIsTarget26.addChangeListener(e -> {
            if (onClickListener != null) {
                onClickListener.onSwitchIsTarget26(chbIsTarget26.isSelected());
                PropertiesComponent.getInstance().setValue(PropertiesKey.IS_TARGET_26, chbIsTarget26.isSelected());
            }
        });
        btnClose.addActionListener(e -> FindViewDialog.this.onCancel());
        btnSelectAll.addActionListener(e -> onClickListener.onSelectAll());
        btnSelectNone.addActionListener(e -> onClickListener.onSelectNone());
        btnNegativeSelect.addActionListener(e -> onClickListener.onNegativeSelect());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });
        contentPane.registerKeyboardAction(e -> FindViewDialog.this.onCancel(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        contentPane.registerKeyboardAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (onClickListener != null) {
                    onClickListener.onOK();
                }
                FindViewDialog.this.onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    private void initStatus() {
        chbAddM.setSelected(PropertiesComponent.getInstance().getBoolean(PropertiesKey.SAVE_ADD_M_ACTION, false));
        chbIsTarget26.setSelected(PropertiesComponent.getInstance().getBoolean(PropertiesKey.IS_TARGET_26, false));
    }

    private void onCancel() {
        dispose();
        if (onClickListener != null) {
            onClickListener.onFinish();
        }
    }

    public void setTextCode(String codeStr) {
        textCode.setText(codeStr);
    }

    public interface OnClickListener {
        void onOK();

        void onSelectAll();

        void onSelectNone();

        void onNegativeSelect();

        void onSwitchAddM(boolean addM);

        void onSwitchIsViewHolder(boolean isViewHolder);

        void onSwitchIsTarget26(boolean target26);

        void onFinish();
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setModel(DefaultTableModel model) {
        tableViews.setModel(model);
        tableViews.getColumnModel().getColumn(0).setPreferredWidth(20);
    }
}
