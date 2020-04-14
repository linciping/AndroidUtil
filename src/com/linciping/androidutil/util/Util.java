package com.linciping.androidutil.util;

import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.ProjectJdkTable;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.search.EverythingGlobalScope;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.ui.awt.RelativePoint;
import com.intellij.util.Consumer;
import com.linciping.androidutil.bean.MethodParam;
import com.linciping.androidutil.bean.ViewPart;
import com.linciping.androidutil.view.StartActivityMethodDialog;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Util {

    private final static String[] HEADERS = {"selected", "type", "name"};

    /**
     * 更新 View 表格
     */
    public static DefaultTableModel updateTable(List<MethodParam> methodParamList, StartActivityMethodDialog startActivityMethodDialog, TableModelListener tableModelListener) {
        if (methodParamList == null || methodParamList.size() == 0) {
            return null;
        }
        DefaultTableModel tableModel = Util.getMethodInstanceModel(methodParamList, tableModelListener);
        startActivityMethodDialog.setModel(tableModel);
        return tableModel;
    }

    public static DefaultTableModel getMethodInstanceModel(List<MethodParam> methodParamList, TableModelListener tableModelListener) {
        DefaultTableModel tableModel;
        int size = methodParamList.size();
        Object[][] cellData = new Object[size][3];
        for (int i = 0; i < size; i++) {
            MethodParam methodParam = methodParamList.get(i);
            for (int j = 0; j < 3; j++) {
                switch (j) {
                    case 0:
                        cellData[i][j] = methodParam.isSelected();
                        break;
                    case 1:
                        cellData[i][j] = methodParam.getParamType();
                        break;
                    case 2:
                        cellData[i][j] = methodParam.getParamName();
                        break;
                }
            }
        }


        tableModel = new DefaultTableModel(cellData, HEADERS) {
            final Class[] typeArray = {Boolean.class, String.class, String.class};

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0;
            }

            @SuppressWarnings("rawtypes")
            public Class getColumnClass(int column) {
                return typeArray[column];
            }
        };
        tableModel.addTableModelListener(tableModelListener);
        return tableModel;
    }

    public static String paramName(String activityName) {
        String first = activityName.substring(0, 1);
        String realFirst = first.toLowerCase();
        return activityName.replace(first, realFirst);
    }

    public static List<MethodParam> getMetParamList(PsiClass psiClass) {
        PsiField[] psiFields = psiClass.getFields();
        List<MethodParam> methodParamList = new ArrayList<>(psiFields.length);
        for (PsiField psiField : psiFields) {
            MethodParam methodParam = new MethodParam(psiField.getType().getPresentableText(), psiField.getName());
            methodParamList.add(methodParam);
        }
        return methodParamList;
    }

    public static void showErrorNotification(String message, Project project) {
        NotificationGroup notificationGroup = new NotificationGroup("File Diff", NotificationDisplayType.TOOL_WINDOW, true);
        Notification notification = notificationGroup.createNotification(message, NotificationType.ERROR);
        notification.notify(project);
    }

    public static void showNotification(String message, Project project) {
        NotificationGroup notificationGroup = new NotificationGroup("File Diff", NotificationDisplayType.TOOL_WINDOW, true);
        Notification notification = notificationGroup.createNotification(message, NotificationType.INFORMATION);
        notification.notify(project);
    }

    public static void showSelectSingleFile(Project project, String title, VirtualFile virtualFile, Consumer<VirtualFile> onFileSelectResult) {
        FileChooserDescriptor fileChooserDescriptor = FileChooserDescriptorFactory.createSingleFolderDescriptor();
        fileChooserDescriptor.setTitle(title);
        FileChooser.chooseFile(fileChooserDescriptor, project, virtualFile, onFileSelectResult);
    }

    public static void showSelectSingleFile(Project project, String title, Consumer<VirtualFile> onFileSelectResult) {
        FileChooserDescriptor fileChooserDescriptor = FileChooserDescriptorFactory.createSingleFolderDescriptor();
        fileChooserDescriptor.setTitle(title);
        FileChooser.chooseFile(fileChooserDescriptor, project, project.getProjectFile(), onFileSelectResult);
    }

    /**
     * Is using Android SDK?
     */
    public static Sdk findAndroidSDK() {
        Sdk[] allJDKs = ProjectJdkTable.getInstance().getAllJdks();
        for (Sdk sdk : allJDKs) {
            if (sdk.getSdkType().getName().toLowerCase().contains("android")) {
                return sdk;
            }
        }
        return null; // no Android SDK found
    }

    /**
     * Try to find layout XML file in current source on cursor's position
     *
     * @param editor
     * @param file
     * @return
     */
    public static PsiFile getLayoutFileFromCaret(Editor editor, PsiFile file) {
        int offset = editor.getCaretModel().getOffset();

        PsiElement candidateA = file.findElementAt(offset);
        PsiElement candidateB = file.findElementAt(offset - 1);

        PsiFile layout = findLayoutResource(candidateA);
        if (layout != null) {
            return layout;
        }

        return findLayoutResource(candidateB);
    }

    /**
     * Try to find layout XML file in selected element
     *
     * @param element
     * @return
     */
    public static PsiFile findLayoutResource(PsiElement element) {
        if (element == null) {
            return null; // nothing to be used
        }
        if (!(element instanceof PsiIdentifier) || !(element instanceof LeafPsiElement)) {
            return null; // nothing to be used
        }

        PsiElement layout = element.getParent().getFirstChild();
        if (layout == null) {
            return null; // no file to process
        }
        if (!"R.layout".equals(layout.getText())) {
            return null; // not layout file
        }

        Project project = element.getProject();
        String name = String.format("%s.xml", element.getText());
        return resolveLayoutResourceFile(element, project, name);
    }

    private static PsiFile resolveLayoutResourceFile(PsiElement element, Project project, String name) {
        // restricting the search to the current module - searching the whole project could return wrong layouts
        Module module = ModuleUtil.findModuleForPsiElement(element);
        PsiFile[] files = null;
        if (module != null) {
            GlobalSearchScope moduleScope = module.getModuleWithDependenciesAndLibrariesScope(false);
            files = FilenameIndex.getFilesByName(project, name, moduleScope);
        }
        if (files == null || files.length <= 0) {
            // fallback to search through the whole project
            // useful when the project is not properly configured - when the resource directory is not configured
            files = FilenameIndex.getFilesByName(project, name, new EverythingGlobalScope(project));
            if (files.length <= 0) {
                return null; //no matching files
            }
        }

        // TODO - we have a problem here - we still can have multiple layouts (some coming from a dependency)
        // we need to resolve R class properly and find the proper layout for the R class
        return files[0];
    }

    /**
     * Try to find layout XML file by name
     *
     * @param file
     * @param project
     * @param fileName
     * @return
     */
    public static PsiFile findLayoutResource(PsiFile file, Project project, String fileName) {
        String name = String.format("%s.xml", fileName);
        // restricting the search to the module of layout that includes the layout we are seaching for
        return resolveLayoutResourceFile(file, project, name);
    }


    /**
     * Get layout name from XML identifier (@layout/....)
     *
     * @param layout
     * @return
     */
    public static String getLayoutName(String layout) {
        if (layout == null || !layout.startsWith("@") || !layout.contains("/")) {
            return null; // it's not layout identifier
        }

        String[] parts = layout.split("/");
        if (parts.length != 2) {
            return null; // not enough parts
        }

        return parts[1];
    }

    /**
     * Display simple notification - information
     *
     * @param project
     * @param text
     */
    public static void showInfoNotification(Project project, String text) {
        showNotification(project, MessageType.INFO, text);
    }

    /**
     * Display simple notification - error
     *
     * @param project
     * @param text
     */
    public static void showErrorNotification(Project project, String text) {
        showNotification(project, MessageType.ERROR, text);
    }

    /**
     * Display simple notification of given type
     *
     * @param project
     * @param type
     * @param text
     */
    public static void showNotification(Project project, MessageType type, String text) {
        StatusBar statusBar = WindowManager.getInstance().getStatusBar(project);
        JBPopupFactory.getInstance()
                .createHtmlTextBalloonBuilder(text, type, null)
                .setFadeoutTime(7500)
                .createBalloon()
                .show(RelativePoint.getCenterOf(statusBar.getComponent()), Balloon.Position.atRight);
    }

    /**
     * Easier way to check if string is empty
     *
     * @param text
     * @return
     */
    public static boolean isEmptyString(String text) {
        return (text == null || text.trim().length() == 0);
    }

    /**
     * Check whether classpath of a module that corresponds to a {@link PsiElement} contains given class.
     *
     * @param project    Project
     * @param psiElement Element for which we check the class
     * @param className  Class name of the searched class
     * @return True if the class is present on the classpath
     * @since 1.3
     */
    public static boolean isClassAvailableForPsiFile(@NotNull Project project, @NotNull PsiElement psiElement, @NotNull String className) {
        Module module = ModuleUtil.findModuleForPsiElement(psiElement);
        if (module == null) {
            return false;
        }
        GlobalSearchScope moduleScope = module.getModuleWithDependenciesAndLibrariesScope(false);
        PsiClass classInModule = JavaPsiFacade.getInstance(project).findClass(className, moduleScope);
        return classInModule != null;
    }

    /**
     * Check whether classpath of a the whole project contains given class.
     * This is only fallback for wrongly setup projects.
     *
     * @param project   Project
     * @param className Class name of the searched class
     * @return True if the class is present on the classpath
     * @since 1.3.1
     */
    public static boolean isClassAvailableForProject(@NotNull Project project, @NotNull String className) {
        PsiClass classInModule = JavaPsiFacade.getInstance(project).findClass(className,
                new EverythingGlobalScope(project));
        return classInModule != null;
    }

    /**
     * 弹窗
     *
     * @param msg
     */
    public static void alert(String msg) {
        Messages.showMessageDialog(msg, "FindViewByMe", Messages.getInformationIcon());
    }

    /**
     * @param src 主串
     * @param sub 字串（模式串）
     */
    public static int bruteFore(String src, String sub) {
        int i = src.toUpperCase().indexOf(sub.toUpperCase());
        return i;
    }

    /**
     * 判断是否已存在控件对应字段
     *
     * @param part 控件
     * @return 是否存在字段
     */
    public static boolean fieldExist(PsiClass psiClass, ViewPart part) {
        PsiField[] fields = psiClass.getAllFields();
        for (PsiField field : fields) {
            if (field.getName().equals(part.getName())) {
                return true;
            }
        }
        return false;
    }

    public static boolean onCreateMethodHasStatement(String code, PsiStatement[] psiStatements) {
        if (psiStatements == null || psiStatements.length == 0) return true;
        for (PsiStatement psiStatement : psiStatements) {
            if (psiStatement.getText().equals(code)) return false;
        }
        return true;
    }

    /**
     * get initView method
     *
     * @return
     */
    public static PsiMethod getInitView(PsiClass psiClass) {
        return findMethod(psiClass, "initView");
    }

    public static PsiParameter findViewParameter(PsiParameter[] psiParameters) {
        for (PsiParameter psiParameter : psiParameters) {
            if (psiParameter.getType().getCanonicalText().equals("android.view.View")) {
                return psiParameter;
            }
        }
        return null;
    }

    public static PsiClass findViewHolderClass(PsiClass psiClass) {
        PsiClass[] psiClasses = psiClass.getInnerClasses();
        for (PsiClass innerClass : psiClasses) {
            if (innerClass.getName().contains("Holder")) {
                return innerClass;
            }
        }
        return null;
    }

    public static boolean hasViewHolderClass(PsiClass psiClass) {
        PsiClass[] psiClasses = psiClass.getInnerClasses();
        for (PsiClass innerClass : psiClasses) {
            if (innerClass.getName().contains("Holder")) {
                return true;
            }
        }
        return false;
    }

    public static PsiMethod findMethod(PsiClass psiClass, String methodName) {
        PsiMethod[] methods = psiClass.findMethodsByName(methodName, true);
        if (methods.length > 0) {
            return methods[0];
        }
        return null;
    }

    public static boolean hasRecyclerViewHolderClass(PsiClass psiClass) {
        PsiClass[] psiClasses = psiClass.getInnerClasses();
        for (PsiClass innerClass : psiClasses) {
            if (innerClass.getSuperClass() != null && innerClass.getSuperClass().getName().equals("ViewHolder")) {
                return true;
            }
        }
        return false;
    }

    public static boolean canFindLayoutResourceElement(PsiFile file, Editor editor, boolean isKotlin) {
        int offset = editor.getCaretModel().getOffset();

        PsiElement candidateA = file.findElementAt(offset);
        PsiElement candidateB = file.findElementAt(offset - 1);

        PsiElement element = findLayoutResourceElement(candidateA, isKotlin);
        if (element == null)
            element = findLayoutResourceElement(candidateB, isKotlin);

        return element != null;
    }

    private static PsiElement findLayoutResourceElement(PsiElement element, boolean isKotlin) {
        if (element == null)
            return null;

        PsiElement layout;
        if (isKotlin) {
            //element.getParent().getParent(): R.layout.activity_main
            layout = element.getParent().getParent().getFirstChild();
            if (layout == null) {
                return null; // no file to process
            }
        } else {
            //element.getParent(): R.layout.activity_main
            layout = element.getParent().getFirstChild();
            if (layout == null) {
                return null; // no file to process
            }
        }

        if (!"R.layout".equals(layout.getText())) {
            return null; // not layout file
        }

        return layout;
    }

    public static void showSelectSingleClass(Project project, String title, Consumer<VirtualFile> onFileSelectResult) {
        FileChooserDescriptor fileChooserDescriptor = FileChooserDescriptorFactory.createSingleFileDescriptor(JavaFileType.INSTANCE);
        fileChooserDescriptor.setTitle(title);
        FileChooser.chooseFile(fileChooserDescriptor, project, project.getWorkspaceFile(), onFileSelectResult);
    }

    @Nullable
    public static VirtualFile getVirtualFileByFile(String path) {
        return getVirtualFileByFile(new File(path));
    }

    @Nullable
    public static VirtualFile getVirtualFileByFile(File file) {
        VirtualFile virtualFile = LocalFileSystem.getInstance().findFileByIoFile(file);
        if (virtualFile == null) {
            virtualFile = LocalFileSystem.getInstance().refreshAndFindFileByIoFile(file);
        }
        return virtualFile;
    }

    public static String getFileNameByPath(String path) {
        int index = path.lastIndexOf("/");
        String fileName = path.substring(index + 1);
        System.out.println("constant class name=" + fileName);
        return fileName.split("\\.")[0];
    }
}
