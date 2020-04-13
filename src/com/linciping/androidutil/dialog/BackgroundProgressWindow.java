package com.linciping.androidutil.dialog;

import com.intellij.openapi.progress.TaskInfo;
import com.intellij.openapi.progress.util.ProgressWindow;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.IdeFrame;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.openapi.wm.ex.StatusBarEx;
import com.intellij.openapi.wm.ex.WindowManagerEx;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BackgroundProgressWindow extends ProgressWindow {

    private StatusBarEx myStatusBar;

    public BackgroundProgressWindow(@Nullable Project project, String title) {
        super(true, true, project);
        setTitle(title);
        final Project nonDefaultProject = project == null || project.isDisposed() || project.isDefault() ? null : project;
        final IdeFrame frame = ((WindowManagerEx) WindowManager.getInstance()).findFrameFor(nonDefaultProject);
        myStatusBar = frame != null ? (StatusBarEx) frame.getStatusBar() : null;
    }

    @Override
    public void background() {
        super.background();
        doBackground();
    }

    private void doBackground() {
        if (myStatusBar != null) { //not welcome screen
            myStatusBar.addProgress(this, new TaskInfo() {
                @Nls(capitalization = Nls.Capitalization.Title)
                @NotNull
                @Override
                public String getTitle() {
                    return BackgroundProgressWindow.this.getTitle();
                }

                @Nls(capitalization = Nls.Capitalization.Title)
                @Override
                public String getCancelText() {
                    return "Cancel";
                }

                @Nls(capitalization = Nls.Capitalization.Sentence)
                @Override
                public String getCancelTooltipText() {
                    return "正在取消";
                }

                @Override
                public boolean isCancellable() {
                    return true;
                }
            });
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        myStatusBar.dispose();
    }
}
