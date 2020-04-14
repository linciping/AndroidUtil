package com.linciping.androidutil.view;

import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.Consumer;
import com.linciping.androidutil.bean.SettingProjectResDirComponent;
import com.linciping.androidutil.dialog.BackgroundProgressWindow;
import com.linciping.androidutil.util.CheckUtil;
import com.linciping.androidutil.util.Util;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class CopyRes2ProjectDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTable tbRes;
    private JButton btnHdpi;
    private JButton btnXXHdpi;
    private JButton btnXHdpi;

    private final static String[] HEADERS = {"resId", "hdpi路径", "xhdpi路径", "xxhdpi路径"};

    private Project project;
    private String projectPath = "";
    private SettingProjectResDirComponent settingProjectResDirComponent;
    private String hdpiDir, xhdpiDir, xxhdpiDir;
    private List<String> hdpiPaths = new ArrayList<>();
    private List<String> hdpiNames = new ArrayList<>();
    private List<String> xhdpiPaths = new ArrayList<>();
    private List<String> xhdpiNames = new ArrayList<>();
    private List<String> xxhdpiPaths = new ArrayList<>();
    private List<String> xxhdpiNames = new ArrayList<>();
    private Map<Integer, String> resIdMap = new HashMap<>();

    public CopyRes2ProjectDialog(Project project) {
        this.project = project;
        settingProjectResDirComponent = SettingProjectResDirComponent.getInstance(project);
        projectPath = project.getBasePath();
        hdpiDir = projectPath + File.separator + "app\\src\\main\\res" + File.separator + "drawable-hdpi";
        xhdpiDir = projectPath + File.separator + "app\\src\\main\\res" + File.separator + "drawable-xhdpi";
        xxhdpiDir = projectPath + File.separator + "app\\src\\main\\res" + File.separator + "drawable-xxhdpi";

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setTitle("拷贝图片资源到Android项目");

        btnHdpi.addActionListener(e -> {
            showSelectSingleFile("请选择hdpi文件", virtualFiles -> {
                for (VirtualFile file : virtualFiles) {
                    String path = file.getPath();
                    if (hdpiPaths.isEmpty() || !hdpiPaths.contains(path)) {
                        hdpiPaths.add(file.getPath());
                        hdpiNames.add(file.getName());
                    }
                }
                showReflect();
                showTable();
            });
        });

        btnXHdpi.addActionListener(e -> {
            showSelectSingleFile("请选择xhdpi文件", virtualFiles -> {
                for (VirtualFile file : virtualFiles) {
                    String path = file.getPath();
                    if (xhdpiPaths.isEmpty() || !xhdpiPaths.contains(path)) {
                        xhdpiPaths.add(file.getPath());
                        xhdpiNames.add(file.getName());
                    }
                }
                showTable();
            });
        });

        btnXXHdpi.addActionListener(e -> {
            showSelectSingleFile("请选择xxhdpi文件", virtualFiles -> {
                for (VirtualFile file : virtualFiles) {
                    String path = file.getPath();
                    if (xxhdpiPaths.isEmpty() || !xxhdpiPaths.contains(path)) {
                        xxhdpiPaths.add(file.getPath());
                        xxhdpiNames.add(file.getName());
                    }
                }
                showTable();
            });
        });
        buttonOK.addActionListener(e -> {
            final BackgroundProgressWindow progressWindow = new BackgroundProgressWindow(project, "拷贝文件中...");
            new Thread(() -> {
                copyRes();
                SwingUtilities.invokeLater(() -> {
                    progressWindow.stop();
                    CopyRes2ProjectDialog.this.dispose();
                });
            }).start();
            progressWindow.start();
        });
        buttonCancel.addActionListener(e -> dispose());
    }

    private void showReflect() {
        if (isShowReflect()) {
            if (!hdpiDir.isEmpty()) {
                for (int i = 0; i < hdpiPaths.size(); i++) {
                    String path = hdpiPaths.get(i);
                    String fileName = hdpiNames.get(i);
                    xhdpiPaths.add(path.replace("hdpi", "xhdpi"));
                    xhdpiNames.add(fileName);
                    xxhdpiPaths.add(path.replace("hdpi", "xxhdpi"));
                    xxhdpiNames.add(fileName);
                }
            } else if (!xhdpiDir.isEmpty()) {
                for (int i = 0; i < xhdpiPaths.size(); i++) {
                    String path = xhdpiPaths.get(i);
                    String fileName = xhdpiNames.get(i);
                    hdpiPaths.add(path.replace("xhdpi", "hdpi"));
                    hdpiNames.add(fileName);
                    xxhdpiPaths.add(path.replace("xhdpi", "xxhdpi"));
                    xxhdpiNames.add(fileName);
                }
            } else if (!xxhdpiDir.isEmpty()) {
                for (int i = 0; i < xxhdpiPaths.size(); i++) {
                    String path = xxhdpiPaths.get(i);
                    String fileName = xxhdpiNames.get(i);
                    hdpiPaths.add(path.replace("xxhdpi", "hdpi"));
                    hdpiNames.add(fileName);
                    xhdpiPaths.add(path.replace("xxhdpi", "xhdpi"));
                    xhdpiNames.add(fileName);
                }
            }
            showTable();
        }
    }

    private boolean isShowReflect() {
        if (!settingProjectResDirComponent.isReflect()){
            return false;
        }
        int noEmptySize = 0;
        int noEmptyIndex = 0;
        if (!hdpiPaths.isEmpty()) {
            noEmptySize++;
            noEmptyIndex = 0;
        }
        if (!xhdpiPaths.isEmpty()) {
            noEmptySize++;
            noEmptyIndex = 1;
        }
        if (!xxhdpiPaths.isEmpty()) {
            noEmptySize++;
            noEmptyIndex = 2;
        }
        if (noEmptySize <= 1) {
            switch (noEmptyIndex) {
                case 0:
                    String path = hdpiPaths.get(0);
                    String[] paths = path.split("/");
                    if (paths.length <= 2) {
                        return false;
                    }
                    String pxPath = paths[paths.length - 2];
                    return pxPath.contains("hdpi");
                case 1:
                    path = xhdpiPaths.get(0);
                    paths = path.split("/");
                    if (paths.length <= 2) {
                        return false;
                    }
                    pxPath = paths[paths.length - 2];
                    return pxPath.contains("xhdpi");
                case 2:
                    path = xxhdpiPaths.get(0);
                    paths = path.split("/");
                    if (paths.length <= 2) {
                        return false;
                    }
                    pxPath = paths[paths.length - 2];
                    return pxPath.contains("xxhdpi");
            }
            return true;
        }
        return false;
    }

    private void copyRes() {
        if (resIdMap.isEmpty()) {
            Util.showErrorNotification("资源ID不能为空", project);
            return;
        }
        File hdpiFileDir = new File(hdpiDir);
        if (!hdpiFileDir.exists() && !hdpiPaths.isEmpty()) {
            boolean result = hdpiFileDir.mkdir();
            if (!result) {
                return;
            }
        }
        File xhdpiFileDir = new File(xhdpiDir);
        if (!xhdpiFileDir.exists() && !xhdpiPaths.isEmpty()) {
            boolean result = xhdpiFileDir.mkdir();
            if (!result) {
                return;
            }
        }
        File xxhdpiFileDir = new File(xxhdpiDir);
        if (!xxhdpiFileDir.exists() && !xxhdpiPaths.isEmpty()) {
            boolean result = xxhdpiFileDir.mkdir();
            if (!result) {
                return;
            }
        }
        Set<Integer> keySet = resIdMap.keySet();
        for (int index : keySet) {
            String id = resIdMap.get(index);
            String hdpiResPath = getResPath(hdpiPaths, index);
            String xhdpiResPath = getResPath(xhdpiPaths, index);
            String xxhdpiResPath = getResPath(xxhdpiPaths, index);
            try {
                if (hdpiResPath != null && !hdpiResPath.isEmpty()) {
                    File hdpiFile = new File(hdpiFileDir.getPath(), id + ".png");
                    FileUtils.copyFile(new File(hdpiResPath), hdpiFile);
                }
                if (xhdpiResPath != null && !xhdpiResPath.isEmpty()) {
                    File xhdpiFile = new File(xhdpiFileDir.getPath(), id + ".png");
                    FileUtils.copyFile(new File(xhdpiResPath), xhdpiFile);
                }
                if (xxhdpiResPath != null && !xxhdpiResPath.isEmpty()) {
                    File xxhdpiFile = new File(xxhdpiFileDir.getPath(), id + ".png");
                    FileUtils.copyFile(new File(xxhdpiResPath), xxhdpiFile);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        Util.showNotification("拷贝完成", project);
    }

    private String getResPath(List<String> pathList, int index) {
        if (pathList.size() < index) {
            return null;
        }
        return pathList.get(index);
    }

    private void showTable() {
        int size = getMaxSize();
        String[][] cellData = new String[size][4];
        for (int i = 0; i < size; i++) {
            if (!resIdMap.isEmpty()) {
                if (resIdMap.size() > i) {
                    cellData[i][0] = resIdMap.get(i);
                } else {
                    cellData[i][0] = "";
                }
            }
            if (!hdpiNames.isEmpty()) {
                if (hdpiNames.size() > i) {
                    cellData[i][1] = hdpiNames.get(i);
                } else {
                    cellData[i][1] = "";
                }
            }
            if (!xhdpiNames.isEmpty()) {
                if (xhdpiNames.size() > i) {
                    cellData[i][2] = xhdpiNames.get(i);
                } else {
                    cellData[i][2] = "";
                }
            }
            if (!xxhdpiNames.isEmpty()) {
                if (xxhdpiNames.size() > i) {
                    cellData[i][3] = xxhdpiNames.get(i);
                } else {
                    cellData[i][3] = "";
                }
            }
        }
        DefaultTableModel tableModel = new DefaultTableModel(cellData, HEADERS) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return String.class;
            }
        };
        tableModel.addTableModelListener(e -> {
            int row = e.getFirstRow();
            int col = e.getColumn();
            if (col == 0) {
                String value = tableModel.getValueAt(row, col).toString();
                resIdMap.put(row, value);
            }
        });
        tbRes.setModel(tableModel);
    }

    private int getMaxSize() {
        int maxSize = resIdMap.size();
        if (maxSize < hdpiPaths.size()) {
            maxSize = hdpiPaths.size();
        }
        if (maxSize < xhdpiPaths.size()) {
            maxSize = xhdpiPaths.size();
        }
        if (maxSize < xxhdpiPaths.size()) {
            maxSize = xxhdpiPaths.size();
        }
        return maxSize;
    }

    private void showSelectSingleFile(String title, Consumer<List<VirtualFile>> onFileSelectResult) {
        FileChooserDescriptor fileChooserDescriptor = FileChooserDescriptorFactory.createMultipleFilesNoJarsDescriptor();
        fileChooserDescriptor.setTitle(title);
        VirtualFile virtualFile=null;
        if (CheckUtil.isStringNoEmpty(settingProjectResDirComponent.getResDirPath())){
            File file=new File(settingProjectResDirComponent.getResDirPath());
            virtualFile= LocalFileSystem.getInstance().findFileByIoFile(file);
            if (virtualFile==null){
                virtualFile= LocalFileSystem.getInstance().refreshAndFindFileByIoFile(file);
            }
        }
        if (virtualFile != null) {
            FileChooser.chooseFiles(fileChooserDescriptor, project, virtualFile, onFileSelectResult);
        } else {
            FileChooser.chooseFiles(fileChooserDescriptor, project, project.getProjectFile(), onFileSelectResult);
        }
    }
}
