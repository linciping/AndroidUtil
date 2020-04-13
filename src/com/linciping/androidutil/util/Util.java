package com.linciping.androidutil.util;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.util.Consumer;
import com.linciping.androidutil.bean.MethodParam;
import com.linciping.androidutil.view.StartActivityMethodDialog;

import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
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

    public static DefaultTableModel getMethodInstanceModel(List<MethodParam> methodParamList, TableModelListener tableModelListener){
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

    public static void showSelectSingleFile(Project project,String title,VirtualFile virtualFile, Consumer<VirtualFile> onFileSelectResult) {
        FileChooserDescriptor fileChooserDescriptor = FileChooserDescriptorFactory.createSingleFolderDescriptor();
        fileChooserDescriptor.setTitle(title);
        FileChooser.chooseFile(fileChooserDescriptor, project, virtualFile, onFileSelectResult);
    }

    public static void showSelectSingleFile(Project project,String title, Consumer<VirtualFile> onFileSelectResult) {
        FileChooserDescriptor fileChooserDescriptor = FileChooserDescriptorFactory.createSingleFolderDescriptor();
        fileChooserDescriptor.setTitle(title);
        FileChooser.chooseFile(fileChooserDescriptor, project, project.getProjectFile(), onFileSelectResult);
    }
}
