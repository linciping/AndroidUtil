package com.linciping.androidutil.writer;

import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.intellij.psi.search.EverythingGlobalScope;
import com.linciping.androidutil.bean.InstanceMethodBean;

import java.util.ArrayList;
import java.util.List;

public class FragmentNewInstanceWriter extends BaseMethodInstanceWriter {
    public FragmentNewInstanceWriter(PsiClass mClass, PsiFile psiFile, InstanceMethodBean instanceMethodBean) {
        super(mClass, psiFile, instanceMethodBean);
    }

    @Override
    protected List<PsiClass> getImportClass() {
        List<PsiClass> psiClassList = new ArrayList<>(2);
        PsiClass psiClass = JavaPsiFacade.getInstance(mProject).findClass(
                "android.os.Bundle", new EverythingGlobalScope(mProject));
        psiClassList.add(psiClass);
        psiClass = JavaPsiFacade.getInstance(mProject).findClass(
                "java.io.Serializable", new EverythingGlobalScope(mProject));
        psiClassList.add(psiClass);
        return psiClassList;
    }
}
