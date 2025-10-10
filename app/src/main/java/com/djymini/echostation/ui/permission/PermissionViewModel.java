package com.djymini.echostation.ui.permission;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.djymini.echostation.core.permission.PermissionManager;

public class PermissionViewModel extends ViewModel {
    private final MutableLiveData<Boolean> isPermissionGranted = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> shouldShowRationale = new MutableLiveData<>(false);

    private final PermissionManager permissionManager;

    public PermissionViewModel(PermissionManager permissionManager) {
        this.permissionManager = permissionManager;
    }

    public LiveData<Boolean> getIsPermissionGranted() {
        return isPermissionGranted;
    }

    public LiveData<Boolean> getShouldShowRationale() {
        return shouldShowRationale;
    }

    public void checkPermission() {
        boolean granted = permissionManager.isPermissionGranted();
        boolean rationale = permissionManager.shouldShowRationale();

        // On met à jour en thread-safe
        isPermissionGranted.postValue(granted);
        shouldShowRationale.postValue(rationale);
    }

    public void requestPermission() {
        permissionManager.requestPermission();
    }

    public void checkAndRequestPermission() {
        permissionManager.checkAndRequestPermission(new PermissionManager.Callback() {
            @Override
            public void onPermissionGranted() {
                isPermissionGranted.postValue(true);
            }

            @Override
            public void onPermissionDenied(boolean rationale) {
                isPermissionGranted.postValue(false);
                shouldShowRationale.postValue(rationale);
            }
        });
    }
}
