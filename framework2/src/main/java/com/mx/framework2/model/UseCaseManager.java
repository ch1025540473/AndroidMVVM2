package com.mx.framework2.model;

import android.content.Context;

import com.mx.engine.utils.ObjectUtils;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.mx.engine.utils.CheckUtils.checkArgument;

public class UseCaseManager {

    private Set<Class<? extends UseCase>> useCaseTypes;
    private Map<String, UseCase> useCaseRefs;
    private Context context;

    public UseCaseManager(Context context) {
        this.context = context;
        useCaseTypes = new HashSet<>();
        useCaseRefs = new HashMap<>();
    }

    public synchronized void register(Class<? extends UseCase> useCaseClass) {
        if (useCaseTypes.contains(useCaseClass)) {
            return;
        }
        useCaseTypes.add(useCaseClass);
    }

    public synchronized <T extends UseCase> T obtainUseCase(Class<T> useCaseClass, UseCaseHolder useCaseHolder) {
        checkArgument(useCaseTypes.contains(useCaseClass));
        T useCase = (T) useCaseRefs.get(useCaseClass.getName());
        if (null == useCase) {
            useCase = ObjectUtils.newInstance(useCaseClass);
            useCase.setContext(context);
            useCase.open(useCaseHolder);
            useCaseRefs.put(useCaseClass.getName(), useCase);
        }
        return useCase;
    }

    public final synchronized <T extends UseCase> void closeUseCase(T value, UseCaseHolder useCaseHolder) {
        checkArgument(useCaseTypes.contains(value.getClass()));
        checkArgument(useCaseRefs.get(value.getClass().getName()) != null);
        value.close(useCaseHolder);
    }

}
