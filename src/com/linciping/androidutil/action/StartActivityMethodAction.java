package com.linciping.androidutil.action;

import com.intellij.codeInsight.CodeInsightActionHandler;
import com.intellij.codeInsight.generation.actions.BaseGenerateAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.search.EverythingGlobalScope;
import com.linciping.androidutil.bean.AndroidUtilComponent;
import com.linciping.androidutil.bean.InstanceMethodBean;
import com.linciping.androidutil.bean.MethodParam;
import com.linciping.androidutil.util.CheckUtil;
import com.linciping.androidutil.util.CodeUtil;
import com.linciping.androidutil.util.Util;
import com.linciping.androidutil.view.StartActivityMethodDialog;
import com.linciping.androidutil.writer.StartActivityMethodWriter;
import org.jetbrains.annotations.NotNull;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class StartActivityMethodAction extends BaseGenerateAction {

    private PsiJavaFile psiJavaFile;
    private PsiClass psiClass;
    private List<MethodParam> methodParamList;
    private DefaultTableModel tableModel;
    private StartActivityMethodDialog startActivityMethodDialog;
    private InstanceMethodBean instanceMethodBean;
    private String constantClassName;

    public StartActivityMethodAction() {
        super(null);
    }

    protected StartActivityMethodAction(CodeInsightActionHandler handler) {
        super(handler);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        psiJavaFile = (PsiJavaFile) e.getData(LangDataKeys.PSI_FILE);
        Editor editor = e.getData(PlatformDataKeys.EDITOR);
        assert editor != null;
        psiClass = getTargetClass(editor, psiJavaFile);
        assert psiClass != null;
        methodParamList = Util.getMetParamList(psiClass);
        constantClassName = getConstantClassName(e.getProject());
        startActivityMethodDialog = new StartActivityMethodDialog();
        tableModel = Util.updateTable(methodParamList, startActivityMethodDialog, tableModelListener);
        startActivityMethodDialog.setTitle("StartActivityMethod");
        startActivityMethodDialog.pack();
        instanceMethodBean = CodeUtil.createStartActivityMethod(methodParamList, psiClass.getName(), constantClassName);
        startActivityMethodDialog.setCode(instanceMethodBean.getCode());
        startActivityMethodDialog.setOkActionListener(e1 -> {
            new StartActivityMethodWriter(psiClass, psiJavaFile, instanceMethodBean).execute();
            startActivityMethodDialog.dispose();
        });
        startActivityMethodDialog.setLocationRelativeTo(WindowManager.getInstance().getFrame(e.getProject()));
        startActivityMethodDialog.setVisible(true);
    }

    private String getConstantClassName(Project project) {
        AndroidUtilComponent androidUtilComponent = AndroidUtilComponent.getInstance(project);
        if (CheckUtil.isStringNoEmpty(androidUtilComponent.getConstantClassPath())) {
            return Util.getFileNameByPath(androidUtilComponent.getConstantClassPath());
        } else {
            return "IntentKey";
        }
    }

    private TableModelListener tableModelListener = new TableModelListener() {
        @Override
        public void tableChanged(TableModelEvent event) {
            if (tableModel == null) {
                return;
            }
            int row = event.getFirstRow();
            int column = event.getColumn();
            if (column == 0) {
                Boolean isSelected = (Boolean) tableModel.getValueAt(row, column);
                methodParamList.get(row).setSelected(isSelected);
                instanceMethodBean = CodeUtil.createStartActivityMethod(methodParamList, psiClass.getName(), constantClassName);
                startActivityMethodDialog.setCode(instanceMethodBean.getCode());
            }
        }
    };

    @Override
    protected boolean isValidForFile(@NotNull Project project, @NotNull Editor editor, @NotNull PsiFile file) {
        boolean result = super.isValidForFile(project, editor, file);
        if (result) {
            PsiClass activityClass = JavaPsiFacade.getInstance(project).findClass(
                    "android.app.Activity", new EverythingGlobalScope(project));
            if (activityClass == null) return false;
            PsiClass psiClass = getTargetClass(editor, file);
            if (psiClass == null) return false;
            return psiClass.isInheritor(activityClass, true);
        }
        return false;
    }
}
