package com.np.saver.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.KeyguardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fxn.stash.Stash;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.np.saver.R;
import com.np.saver.Utils.Constants;

public class SplashActivity extends AppCompatActivity {

    LinearLayout lGetStarted;
    public int STORAGE_PERMISSION_REQUEST_CODE = 12;
    InterstitialAd mInterstitialAd;
    public static int SYSTEM_LOCK = 35;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_splash);
        InterstitialAd.load(this, getString(R.string.interstitial_ID), new AdRequest.Builder().build(), new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                mInterstitialAd = interstitialAd;
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                mInterstitialAd = null;

            }
        });


        lGetStarted = findViewById(R.id.lGetStarted);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (Build.VERSION.SDK_INT >= 21) {
                    @SuppressLint("WrongConstant") KeyguardManager keyguardManager = (KeyguardManager) getSystemService("keyguard");
                    if (keyguardManager.isKeyguardSecure()) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("Please Enter Your Lock Screen Password/Finger Print/Face Lock\n\n");
//                        sb.append(getString(R.string.secutiry_layout));
                        startActivityForResult(keyguardManager.createConfirmDeviceCredentialIntent("Id Card Wallet", sb.toString()), SYSTEM_LOCK);
                        return;
                    }
                    lGetStarted.setVisibility(View.VISIBLE);
                }
            }
        }, 1000);

        Dialog dialog = new Dialog(SplashActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_device_id);
        dialog.setCancelable(true);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        TextView customBtn = dialog.findViewById(R.id.customBtn);
        EditText customEditText = dialog.findViewById(R.id.customEt);

        customBtn.setOnClickListener(v -> {
            String id = customEditText.getText().toString();

            if (id.isEmpty()){
                return;
            }

            Stash.put(Constants.DEVICE_ID, id);

            launchMainActivity();
        });

        dialog.findViewById(R.id.currentBtn).setOnClickListener(v -> {
            Stash.put(Constants.DEVICE_ID, Constants.getDeviceID());

            launchMainActivity();
        });

        lGetStarted.setOnClickListener(view -> {
            dialog.show();
            dialog.getWindow().setAttributes(layoutParams);
        });

    }

    private void launchMainActivity() {
        if (mInterstitialAd != null) {
            mInterstitialAd.show(SplashActivity.this);

            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    if (isWriteStoragePermissionGranted()) {
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish();
                    }
                }

                @Override
                public void onAdFailedToShowFullScreenContent(AdError adError) {
                    if (isWriteStoragePermissionGranted()) {
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish();
                    }
                }

                @Override
                public void onAdShowedFullScreenContent() {
                    mInterstitialAd = null;
                }
            });

        } else {
            if (isWriteStoragePermissionGranted()) {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        }
    }

    public boolean isWriteStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(SplashActivity.this,
                    new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.CAMERA},
                    STORAGE_PERMISSION_REQUEST_CODE);
            return true;
        } else if (Build.VERSION.SDK_INT >= 23) {
            if ((checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED)
                    && (checkSelfPermission(Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED)) {
                return true;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA}, STORAGE_PERMISSION_REQUEST_CODE);
                return false;
            }
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 12) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                intent.putExtra("which", "WA");
                startActivity(intent);

            } else {
                showDailog();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SYSTEM_LOCK) {
            if (resultCode == -1) {
                lGetStarted.setVisibility(View.VISIBLE);
            }
            if (resultCode == 0) {
                finish();
            }
        }
    }

    public void showDailog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
        builder.setMessage("You need to give permission to access feature.");
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton("GIVE PERMISSION", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

                ActivityCompat.requestPermissions(SplashActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                        STORAGE_PERMISSION_REQUEST_CODE);
            }
        });
        builder.show();
    }
}