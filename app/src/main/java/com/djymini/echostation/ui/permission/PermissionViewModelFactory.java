package com.djymini.echostation.ui.permission;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.djymini.echostation.core.permission.PermissionManager;

public class PermissionViewModelFactory implements ViewModelProvider.Factory{
    private final PermissionManager permissionManager;

    public PermissionViewModelFactory(PermissionManager permissionManager) {
        this.permissionManager = permissionManager;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(PermissionViewModel.class)) {
            return (T) new PermissionViewModel(permissionManager);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
