package com.linciping.androidutil.action;

import com.intellij.codeInsight.CodeInsightActionHandler;
import com.intellij.codeInsight.generation.actions.BaseGenerateAction;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.psi.*;
import com.intellij.psi.search.EverythingGlobalScope;
import com.intellij.psi.util.PsiTreeUtil;
import com.linciping.androidutil.bean.PropertiesKey;
import com.linciping.androidutil.bean.ViewPart;
import com.linciping.androidutil.constant.Constant;
import com.linciping.androidutil.util.ActionUtil;
import com.linciping.androidutil.util.Util;
import com.linciping.androidutil.util.ViewSaxHandler;
import com.linciping.androidutil.view.FindViewDialog;
import com.linciping.androidutil.writer.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class FindViewByIdAction extends BaseGenerateAction {

    private boolean isViewHolder;
    private boolean isTarget26;

    private boolean isAddRootView = false;
    private boolean isAddInitViewMethod = false;
    private boolean isHasInitViewMethod = false;
    private boolean isHasViewHolderClass = false;
    private String rootViewStr = "";

    private ViewSaxHandler viewSaxHandler;
    private FindViewDialog findViewDialog;
    private List<ViewPart> viewPartList;
    private DefaultTableModel tableModel;

    private PsiFile psiFile;
    private PsiClass psiClass;

    private int viewCodeType = Constant.VIEW_CODE_WRITER;


    public FindViewByIdAction() {
        super(null);
    }

    protected FindViewByIdAction(CodeInsightActionHandler handler) {
        super(handler);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        isViewHolder = false;
        isTarget26 = false;
        viewSaxHandler = new ViewSaxHandler();
        if (findViewDialog == null) {
            findViewDialog = new FindViewDialog();
        }
        if (!getViewList(anActionEvent)) {
            return;
        }
        ActionUtil.switchAddM(viewPartList, PropertiesComponent.getInstance().getBoolean(PropertiesKey.SAVE_ADD_M_ACTION, false));
        isTarget26 = PropertiesComponent.getInstance().getBoolean(PropertiesKey.IS_TARGET_26, false);
        updateTable();
        findViewDialog.setTitle("FindViewById");
        findViewDialog.btnCopyCode.setText("OK");
        findViewDialog.setOnClickListener(onClickListener);
        findViewDialog.pack();
        findViewDialog.setLocationRelativeTo(WindowManager.getInstance().getFrame(anActionEvent.getProject()));
        findViewDialog.setVisible(true);
    }

    @Override
    public void update(AnActionEvent event) {
        event.getPresentation().setEnabledAndVisible(enabledAndVisible(event));
    }

    private boolean enabledAndVisible(AnActionEvent event) {
        Project project = event.getProject();
        PsiFile psiFile = event.getData(CommonDataKeys.PSI_FILE);
        Editor editor = event.getData(CommonDataKeys.EDITOR);
        if (project == null || psiFile == null || editor == null)
            return false;
        boolean isAllow = psiFile instanceof PsiJavaFile;
        return isAllow && Util.canFindLayoutResourceElement(psiFile, editor, false);
    }

    @Nullable
    private PsiClass getPsiClass(Editor editor, PsiFile file) {
        int offset = editor.getCaretModel().getOffset();
        PsiElement element = file.findElementAt(offset);
        if (element == null) {
            element = file.findElementAt(offset - 1);
        }
        if (element == null) {
            return null;
        } else {
            PsiClass target = PsiTreeUtil.getParentOfType(element, PsiClass.class);
            if (target == null) {
                element = file.findElementAt(offset - 1);
                if (element == null)
                    return null;
                target = PsiTreeUtil.getParentOfType(element, PsiClass.class);
            }
            return target instanceof SyntheticElement ? null : target;
        }
    }

    private boolean getViewList(AnActionEvent event) {
        psiFile = event.getData(LangDataKeys.PSI_FILE);
        Editor editor = event.getData(PlatformDataKeys.EDITOR);
        if (psiFile == null || editor == null) {
            return false;
        }
        psiClass = getPsiClass(editor, psiFile);
        if (psiClass == null) {
            return false;
        }
        buildViewCodeType();
        PsiFile layout = Util.getLayoutFileFromCaret(editor, psiFile);
        String contentStr = psiFile.getText();
        if (layout != null) {
            contentStr = layout.getText();
            String layoutPath = layout.getContainingDirectory().toString().replace("PsiDirectory:", "");
            viewSaxHandler.setLayoutPath(layoutPath);
            viewSaxHandler.setProject(event.getProject());
        }
        viewPartList = ActionUtil.getViewPartList(viewSaxHandler, contentStr);
        return true;
    }

    private void buildViewCodeType() {
        Project project = psiFile.getProject();
        PsiClass activityClass = JavaPsiFacade.getInstance(project).findClass("android.app.Activity", new EverythingGlobalScope(project));
        PsiClass fragmentClass = JavaPsiFacade.getInstance(project).findClass("android.app.Fragment", new EverythingGlobalScope(project));
        PsiClass supportFragmentClass = JavaPsiFacade.getInstance(project).findClass("android.support.v4.app.Fragment", new EverythingGlobalScope(project));
        PsiClass androidXFragmentClass = JavaPsiFacade.getInstance(project).findClass("androidx.fragment.app.Fragment", new EverythingGlobalScope(project));
        PsiClass listAdapterClass = JavaPsiFacade.getInstance(project).findClass("android.widget.BaseAdapter", new EverythingGlobalScope(project));
        PsiClass recyclerViewClass = JavaPsiFacade.getInstance(project).findClass("android.support.v7.widget.RecyclerView", new EverythingGlobalScope(project));
        PsiClass androidXRecyclerViewClass = JavaPsiFacade.getInstance(project).findClass("androidx.recyclerview.widget.RecyclerView", new EverythingGlobalScope(project));
        PsiClass recyclerAdapterClass = null;
        if (recyclerViewClass != null) {
            recyclerAdapterClass = recyclerViewClass.findInnerClassByName("Adapter", true);
        } else if (androidXRecyclerViewClass != null) {
            recyclerAdapterClass = androidXRecyclerViewClass.findInnerClassByName("Adapter", true);
        }

        if (activityClass != null && psiClass.isInheritor(activityClass, true)) {
            viewCodeType = Constant.ACTIVITY_CODE_WRITER;
            PsiMethod[] psiMethods = psiClass.findMethodsByName("onCreate", false);
            PsiMethod initMethod = Util.getInitView(psiClass);
            if (psiMethods.length > 0) {
                isAddInitViewMethod = false;
                isHasInitViewMethod = false;
            } else if (initMethod != null) {
                isAddInitViewMethod = true;
                isHasInitViewMethod = true;
            } else {
                isAddInitViewMethod = true;
                isHasInitViewMethod = false;
            }
        } else if ((fragmentClass != null && psiClass.isInheritor(fragmentClass, true))
                || (supportFragmentClass != null && psiClass.isInheritor(supportFragmentClass, true))
                || (androidXFragmentClass != null && psiClass.isInheritor(androidXFragmentClass, true))) {
            isAddRootView = true;
            viewCodeType = Constant.FRAGMENT_CODE_WRITER;
            PsiMethod[] psiMethods = psiClass.findMethodsByName("onCreateView", false);
            PsiMethod initMethod = Util.getInitView(psiClass);
            if (psiMethods.length > 0) {
                isAddInitViewMethod = false;
                isHasInitViewMethod = false;
                PsiMethod onCreateViewMethod = psiMethods[0];
                PsiCodeBlock psiCodeBlock = onCreateViewMethod.getBody();
                if (psiCodeBlock != null) {
                    PsiStatement contentViewPsiStatement = psiCodeBlock.getStatements()[0];
                    String[] expressionItems = contentViewPsiStatement.getText().split("=");
                    String[] statementItems = expressionItems[0].split(" ");
                    rootViewStr = statementItems[1];
                }
            } else if (initMethod != null) {
                isAddInitViewMethod = true;
                PsiParameter[] psiParameters = initMethod.getParameterList().getParameters();
                if (psiParameters.length == 0) {
                    isHasInitViewMethod = false;
                    rootViewStr = "contentView";
                } else {
                    PsiParameter psiParameter = Util.findViewParameter(psiParameters);
                    if (psiParameter == null) {
                        isHasInitViewMethod = false;
                        rootViewStr = "contentView";
                    } else {
                        isHasInitViewMethod = true;
                        rootViewStr = psiParameter.getName();
                    }
                }
            } else {
                isAddInitViewMethod = true;
                isHasInitViewMethod = false;
                rootViewStr = "contentView";
            }
        } else if (listAdapterClass != null && psiClass.isInheritor(listAdapterClass, true)) {
            viewCodeType = Constant.LIST_ADAPTER_VIEW_HOLDER_CODE_WRITER;
            isHasViewHolderClass = Util.hasViewHolderClass(psiClass);
        } else if (recyclerAdapterClass != null && psiClass.isInheritor(recyclerAdapterClass, true)) {
            viewCodeType = Constant.RECYCLER_ADAPTER_VIEW_HOLDER_CODE_WRITER;
            isHasViewHolderClass = Util.hasRecyclerViewHolderClass(psiClass);
        } else {
            viewCodeType = Constant.VIEW_CODE_WRITER;
        }
    }

    /**
     * FindViewByMe 对话框回调
     */
    private FindViewDialog.OnClickListener onClickListener = new FindViewDialog.OnClickListener() {
        @Override
        public void onOK() {
            BaseCodeWriter codeWriter;
            switch (viewCodeType) {
                case Constant.ACTIVITY_CODE_WRITER:
                    codeWriter = new ActivityCodeWriter(viewPartList, psiClass, psiFile, isAddInitViewMethod, isHasInitViewMethod, isTarget26);
                    break;
                case Constant.FRAGMENT_CODE_WRITER:
                    codeWriter = new FragmentCodeWriter(viewPartList, psiClass, psiFile, isTarget26, isAddInitViewMethod, isHasInitViewMethod, rootViewStr);
                    break;
                case Constant.LIST_ADAPTER_VIEW_HOLDER_CODE_WRITER:
                    codeWriter = new ListAdapterViewHolderCodeWriter(viewPartList, psiClass, psiFile, isTarget26, isHasViewHolderClass);
                    break;
                case Constant.RECYCLER_ADAPTER_VIEW_HOLDER_CODE_WRITER:
                    codeWriter = new RecyclerViewHolderCodeWriter(viewPartList, psiClass, psiFile, isTarget26, isHasViewHolderClass);
                    break;
                default:
                    codeWriter = new ViewCodeWriter(viewPartList, psiClass, psiFile, isTarget26);
            }
            Util.executeWriteCommand(codeWriter);
        }

        @Override
        public void onSelectAll() {
            for (ViewPart viewPart : viewPartList) {
                viewPart.setSelected(true);
            }
            updateTable();
        }

        @Override
        public void onSelectNone() {
            for (ViewPart viewPart : viewPartList) {
                viewPart.setSelected(false);
            }
            updateTable();
        }

        @Override
        public void onNegativeSelect() {
            for (ViewPart viewPart : viewPartList) {
                viewPart.setSelected(!viewPart.isSelected());
            }
            updateTable();
        }

        @Override
        public void onSwitchAddM(boolean isAddM) {
            ActionUtil.switchAddM(viewPartList, isAddM);
            updateTable();
        }

        @Override
        public void onSwitchIsViewHolder(boolean viewHolder) {
            isViewHolder = viewHolder;
            generateCode();
        }

        @Override
        public void onSwitchIsTarget26(boolean target26) {
            isTarget26 = target26;
            generateCode();
        }

        @Override
        public void onFinish() {
            viewPartList = null;
            viewSaxHandler = null;
            findViewDialog = null;
        }
    };

    /**
     * 生成FindViewById代码
     */
    private void generateCode() {
        findViewDialog.setTextCode(ActionUtil.generateCode(viewPartList, isViewHolder, isTarget26, isAddRootView, rootViewStr));
    }

    /**
     * 更新 View 表格
     */
    private void updateTable() {
        if (viewPartList == null || viewPartList.size() == 0) {
            return;
        }
        tableModel = ActionUtil.getTableModel(viewPartList, tableModelListener);
        findViewDialog.setModel(tableModel);
        generateCode();
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
                viewSaxHandler.getViewPartList().get(row).setSelected(isSelected);
                FindViewByIdAction.this.generateCode();
            }
        }
    };
}
