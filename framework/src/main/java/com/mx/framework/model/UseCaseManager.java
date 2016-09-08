package com.mx.framework.model;

import android.content.Context;

import com.mx.engine.utils.ObjectUtils;


import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import static com.mx.engine.utils.CheckUtils.checkArgument;

@Deprecated
public class UseCaseManager {

    private Set<Class<? extends UseCase>> useCaseTypes;
    private List<Reference<? extends UseCase>> useCaseRefs;
    private Context context;

    public UseCaseManager(Context context) {
        this.context = context;
        useCaseTypes = new HashSet<Class<? extends UseCase>>();
        useCaseRefs = new LinkedList<Reference<? extends UseCase>>();
    }

    public synchronized void register(Class<? extends UseCase> useCaseClass) {
        if (useCaseTypes.contains(useCaseClass)) {
            return;
        }
        useCaseTypes.add(useCaseClass);
    }

    public synchronized <T extends UseCase> T obtainUseCase(Class<T> useCaseClass) {
        checkArgument(useCaseTypes.contains(useCaseClass));

        ListIterator<Reference<? extends UseCase>> iterator = useCaseRefs.listIterator();
        while (iterator.hasNext()) {

            Reference<? extends UseCase> useCaseRef = iterator.next();
            UseCase useCase = useCaseRef.get();

            if (null == useCase) {
                iterator.remove();
            } else if (useCase.getClass().equals(useCaseClass)) {
                useCase.open();
                iterator.remove();
                return (T) useCase;
            }
        }

        T useCase = ObjectUtils.newInstance(useCaseClass);
        useCase.setUseCaseManager(this);
        useCase.setContext(context);
        useCase.open();
        return useCase;
    }

    final synchronized <T extends UseCase> void closeUseCase(T value) {
        checkArgument(useCaseTypes.contains(value.getClass()));
        useCaseRefs.add(new SoftReference<T>(value));
    }

}
