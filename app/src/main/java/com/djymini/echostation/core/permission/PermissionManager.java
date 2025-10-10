package com.djymini.echostation.core.permission;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.activity.result.ActivityResultLauncher;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionManager {
    private final Activity activity;
    private final String permission;
    private final ActivityResultLauncher<String> permissionLauncher;

    public interface Callback {
        void onPermissionGranted();
        void onPermissionDenied(boolean shouldShowRationale);
    }

    public PermissionManager(Activity activity, String permission, ActivityResultLauncher<String> launcher) {
        this.activity = activity;
        this.permission = permission;
        this.permissionLauncher = launcher;
    }

    public boolean isPermissionGranted() {
        return ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public boolean shouldShowRationale() {
        return ActivityCompat.shouldShowRequestPermissionRationale(activity, permission);
    }

    public void requestPermission() {
        permissionLauncher.launch(permission);
    }

    public void checkAndRequestPermission(Callback callback) {
        if (isPermissionGranted()) {
            callback.onPermissionGranted();
        } else if (shouldShowRationale()) {
            callback.onPermissionDenied(true);
        } else {
            requestPermission();
        }
    }

    public static String getRequiredPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return Manifest.permission.READ_MEDIA_AUDIO;
        } else {
            return Manifest.permission.READ_EXTERNAL_STORAGE;
        }
    }
}
