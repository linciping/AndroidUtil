package com.linciping.androidutil.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.wm.WindowManager;
import com.linciping.androidutil.view.CopyRes2ProjectDialog;
import org.jetbrains.annotations.NotNull;

public class CopyRes2ProjectAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        CopyRes2ProjectDialog copyResDialog=new CopyRes2ProjectDialog(event.getProject());
        copyResDialog.pack();
        copyResDialog.setLocationRelativeTo(WindowManager.getInstance().getFrame(event.getProject()));
        copyResDialog.setVisible(true);
    }
}
