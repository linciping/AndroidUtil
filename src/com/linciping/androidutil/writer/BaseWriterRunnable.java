package com.linciping.androidutil.writer;

import com.intellij.openapi.project.Project;

public abstract class BaseWriterRunnable implements Runnable {

    protected Project mProject;

    public BaseWriterRunnable(Project project) {
        this.mProject = project;
    }

    public Project getProject() {
        return mProject;
    }
}
