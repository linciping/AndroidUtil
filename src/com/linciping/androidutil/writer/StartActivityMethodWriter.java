package com.linciping.androidutil.writer;

import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.intellij.psi.search.EverythingGlobalScope;
import com.linciping.androidutil.bean.InstanceMethodBean;

import java.util.ArrayList;
import java.util.List;

public class StartActivityMethodWriter extends BaseMethodInstanceWriter {


    public StartActivityMethodWriter(PsiClass mClass, PsiFile psiFile, InstanceMethodBean instanceMethodBean) {
        super(mClass, psiFile, instanceMethodBean);
    }

    @Override
    protected List<PsiClass> getImportClass() {
        List<PsiClass> psiClassList = new ArrayList<>(2);
        PsiClass contextClass = JavaPsiFacade.getInstance(mProject).findClass(
                "android.content.Context", new EverythingGlobalScope(mProject));
        psiClassList.add(contextClass);
        PsiClass intentClass = JavaPsiFacade.getInstance(mProject).findClass(
                "android.content.Intent", new EverythingGlobalScope(mProject));
        psiClassList.add(intentClass);
        return psiClassList;
    }
}
