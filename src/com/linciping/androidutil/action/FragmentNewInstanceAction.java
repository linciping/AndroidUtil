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
import com.linciping.androidutil.bean.InstanceMethodBean;
import com.linciping.androidutil.bean.MethodParam;
import com.linciping.androidutil.util.CodeUtil;
import com.linciping.androidutil.util.Util;
import com.linciping.androidutil.view.StartActivityMethodDialog;
import com.linciping.androidutil.writer.FragmentNewInstanceWriter;
import org.jetbrains.annotations.NotNull;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class FragmentNewInstanceAction extends BaseGenerateAction {

    private PsiJavaFile psiJavaFile;
    private PsiClass psiClass;
    private List<MethodParam> methodParamList;
    private DefaultTableModel tableModel;
    private StartActivityMethodDialog startActivityMethodDialog;
    private String constantClassName;
    private Project project;
    private InstanceMethodBean instanceMethodBean;

    public FragmentNewInstanceAction() {
        super(null);
    }

    protected FragmentNewInstanceAction(CodeInsightActionHandler handler) {
        super(handler);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        psiJavaFile = (PsiJavaFile) e.getData(LangDataKeys.PSI_FILE);
        project = e.getProject();
        assert project != null;
        Editor editor = e.getData(PlatformDataKeys.EDITOR);
        assert editor != null;
        psiClass = getTargetClass(editor, psiJavaFile);
        assert psiClass != null;
        methodParamList = Util.getMethodParamList(psiClass);
        constantClassName = Util.getConstantClassName(e.getProject());
        startActivityMethodDialog = new StartActivityMethodDialog();
        tableModel = Util.updateTable(methodParamList, startActivityMethodDialog, tableModelListener);
        startActivityMethodDialog.setTitle("StartActivityMethod");
        startActivityMethodDialog.pack();
        instanceMethodBean = CodeUtil.createFragmentInstanceMethod(methodParamList, psiClass.getName(),
                constantClassName, project);
        String code = instanceMethodBean.getInstanceMethodCode() + "\n\n" + instanceMethodBean.getExtraSettingValueMethodCode();
        startActivityMethodDialog.setCode(code);
        startActivityMethodDialog.setOkActionListener(e1 -> {
            new FragmentNewInstanceWriter(psiClass, psiJavaFile, instanceMethodBean).execute();
            startActivityMethodDialog.dispose();
        });
        startActivityMethodDialog.setLocationRelativeTo(WindowManager.getInstance().getFrame(e.getProject()));
        startActivityMethodDialog.setVisible(true);
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
                instanceMethodBean = CodeUtil.createFragmentInstanceMethod(methodParamList, psiClass.getName(), constantClassName, project);
                String code = instanceMethodBean.getInstanceMethodCode() + "\n\n" + instanceMethodBean.getExtraSettingValueMethodCode();
                startActivityMethodDialog.setCode(code);
            }
        }
    };

    @Override
    protected boolean isValidForFile(@NotNull Project project, @NotNull Editor editor, @NotNull PsiFile file) {
        boolean result = super.isValidForFile(project, editor, file);
        if (result) {
            PsiClass fragmentClass = JavaPsiFacade.getInstance(project).findClass(
                    "android.app.Fragment", new EverythingGlobalScope(project));
            PsiClass supportFragmentClass = JavaPsiFacade.getInstance(project).findClass(
                    "android.support.v4.app.Fragment", new EverythingGlobalScope(project));
            PsiClass androidXFragmentClass = JavaPsiFacade.getInstance(project).findClass(
                    "androidx.fragment.app.Fragment", new EverythingGlobalScope(project));
            PsiClass psiClass = getTargetClass(editor, file);
            if (psiClass == null) return false;
            if (androidXFragmentClass != null && psiClass.isInheritor(androidXFragmentClass, true)) return true;
            if (supportFragmentClass != null && psiClass.isInheritor(supportFragmentClass, true)) return true;
            return fragmentClass != null && psiClass.isInheritor(fragmentClass, true);
        }
        return false;
    }
}
