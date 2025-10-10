package com.djymini.echostation.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.djymini.echostation.R;
import com.djymini.echostation.core.permission.PermissionManager;
import com.djymini.echostation.ui.permission.PermissionViewModel;
import com.djymini.echostation.ui.permission.PermissionViewModelFactory;

public class MainActivity extends AppCompatActivity {
    private PermissionViewModel permissionViewModel;
    RelativeLayout authorizationLayout;
    private Button confirmButton, quitButton;

    private final ActivityResultLauncher<String> permissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    permissionViewModel.checkPermission();
                } else {
                    permissionViewModel.checkPermission();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        authorizationLayout = findViewById(R.id.authorization_layout);
        confirmButton = findViewById(R.id.confirm_button);
        quitButton = findViewById(R.id.quit_button);

        setButton();
        setupViewModel();
        setupObservers();

        permissionViewModel.checkPermission();
    }

    private void setButton(){
        confirmButton.setOnClickListener(v -> {
            permissionViewModel.requestPermission();
        });

        quitButton.setOnClickListener(v -> {
            finish();
        });
    }

    private void setupViewModel() {
        String permission = PermissionManager.getRequiredPermission();
        PermissionManager permissionManager = new PermissionManager(this, permission, permissionLauncher);
        PermissionViewModelFactory factory = new PermissionViewModelFactory(permissionManager);
        permissionViewModel = new ViewModelProvider(this, factory).get(PermissionViewModel.class);
    }

    private void setupObservers() {
        permissionViewModel.getIsPermissionGranted().observe(this, granted -> {
            if (granted) {
                authorizationLayout.setVisibility(View.GONE);
            } else {
                authorizationLayout.setVisibility(View.VISIBLE);
            }
        });

        permissionViewModel.getShouldShowRationale().observe(this, shouldShow -> {
            if (shouldShow) {
                showRationaleDialog();
            }
        });
    }

    private void showRationaleDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Permission requise")
                .setMessage("L’application a besoin d’accéder à vos fichiers audio pour afficher votre bibliothèque musicale.")
                .setPositiveButton("Autoriser", (dialog, which) -> permissionViewModel.requestPermission())
                .setNegativeButton("Annuler", null)
                .show();
    }
}