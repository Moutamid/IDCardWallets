package com.np.saver.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.KeyguardManager;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fxn.stash.Stash;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.np.saver.Model.LocalBackup;
import com.np.saver.Model.RemoteBackup;
import com.np.saver.Permissions;
import com.np.saver.R;
import com.np.saver.Utils.Constants;
import com.np.saver.database.DatabaseHelper;
import com.saadahmedsoft.popupdialog.PopupDialog;
import com.saadahmedsoft.popupdialog.Styles;
import com.saadahmedsoft.popupdialog.listener.OnDialogButtonClickListener;

import java.io.File;

public class SettingActivity extends AppCompatActivity {

    ImageView ivBack;
    LinearLayout relRateUs, relShare, relPrivacy, relSuggestion, relMore, relbackup, relrestore;
    public static final int REQUEST_CODE_SIGN_IN = 0;
    public static final int REQUEST_CODE_OPENING = 1;
    public static final int REQUEST_CODE_CREATION = 2;
    public static final int REQUEST_CODE_PERMISSIONS = 2;
    private boolean isBackup = true;
    private SettingActivity activity;
    private LocalBackup localBackup;
    private RemoteBackup remoteBackup;

    String currentText = "null";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activity = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        final DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        localBackup = new LocalBackup(this);
        remoteBackup = new RemoteBackup(this);
        init();

        TextView deviceIdTv = findViewById(R.id.deviceIdTextView);
        TextView currentlyUsedIdTv = findViewById(R.id.currentIdTextView);

//        deviceIdTv.setText("Copy Your Device ID (" + Constants.getDeviceID() + ")");
        /*currentlyUsedIdTv.setText("Copy Currently Used ID ("
                + Stash.getString(Constants.DEVICE_ID, Constants.getDeviceID()) + ")");
        */

        LinearLayout currentlyUsedIdLayout = findViewById(R.id.copyCurrentIdLayout);
        LinearLayout deviceIdLayout = findViewById(R.id.copyDeviceIdLayout);

        currentlyUsedIdLayout.setOnClickListener(v -> {
            currentText = Stash.getString(Constants.DEVICE_ID, Constants.getDeviceID());
            if (Build.VERSION.SDK_INT >= 21) {
                @SuppressLint("WrongConstant") KeyguardManager keyguardManager = (KeyguardManager) getSystemService("keyguard");
                if (keyguardManager.isKeyguardSecure()) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Please Enter Your Lock Screen Password/Finger Print/Face Lock\n\n");
//                        sb.append(getString(R.string.secutiry_layout));
                    startActivityForResult(keyguardManager.createConfirmDeviceCredentialIntent("Id Card Wallet", sb.toString()), SYSTEM_LOCK);
                    return;
                }
                copyToClipboard();
            }
        });

        deviceIdLayout.setOnClickListener(v -> {
            currentText = Constants.getDeviceID();
            if (Build.VERSION.SDK_INT >= 21) {
                @SuppressLint("WrongConstant") KeyguardManager keyguardManager = (KeyguardManager) getSystemService("keyguard");
                if (keyguardManager.isKeyguardSecure()) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Please Enter Your Lock Screen Password/Finger Print/Face Lock\n\n");
//                        sb.append(getString(R.string.secutiry_layout));
                    startActivityForResult(keyguardManager.createConfirmDeviceCredentialIntent("Id Card Wallet", sb.toString()), SYSTEM_LOCK);
                    return;
                }
                copyToClipboard();
            }
        });


        ivBack.setOnClickListener(view -> finish());
        relbackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SettingActivity.this, "Data already backed up!", Toast.LENGTH_SHORT).show();

                /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    if (Environment.isExternalStorageManager()) {
                        String outFileName = Environment.getExternalStorageDirectory() + File.separator + getResources().getString(R.string.app_name) + File.separator;
                        Log.d("outFileName", outFileName);
                        localBackup.performBackup(db, outFileName);
                        // If you don't have access, launch a new activity to show the user the system's dialog
                        // to allow access to the external storage
                    } else {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                        Uri uri = Uri.fromParts("package", SettingActivity.this.getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                    }
                }*/

//                isBackup = true;
//                remoteBackup.connectToDrive(isBackup);
            }
        });
        Dialog dialog = new Dialog(SettingActivity.this);
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

            if (id.isEmpty()) {
                return;
            }

            Stash.put(Constants.DEVICE_ID, id);

        });

        dialog.findViewById(R.id.currentBtn).setOnClickListener(v -> {
            Stash.put(Constants.DEVICE_ID, Constants.getDeviceID());
        });

        relrestore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                localBackup.performRestore(db);
                dialog.show();
                dialog.getWindow().setAttributes(layoutParams);
            }
        });
        relRateUs.setOnClickListener(view -> {
            try {
                Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName()));
                startActivity(intent1);
            } catch (Exception ex) {
                Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName()));
                startActivity(intent1);
            }
        });
        relShare.setOnClickListener(view -> {
            Intent intentShare = new Intent(Intent.ACTION_SEND);
            intentShare.setType("text/plain");
            intentShare.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            intentShare.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=" + getPackageName());
            startActivity(intentShare);
        });
        relPrivacy.setOnClickListener(view -> {
            popUp();

//            Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse("YOUR_PRIVACY_POLICY_URL"));//replace with your privacy policy url
//            startActivity(intent1);
        });

        relSuggestion.setOnClickListener(view -> {

            Intent intent = new Intent(Intent.ACTION_SEND);
            String[] recipients = {"jaibhavaniservices1@gmail.com"};
            intent.putExtra(Intent.EXTRA_EMAIL, recipients);
//            intent.putExtra(Intent.EXTRA_SUBJECT,"Subject text here...");
//            intent.putExtra(Intent.EXTRA_TEXT,"Body of the content here...");
//            intent.putExtra(Intent.EXTRA_CC,"mailcc@gmail.com");
            intent.setType("text/html");
            intent.setPackage("com.google.android.gm");
            startActivity(Intent.createChooser(intent, "Send mail"));

//            try {
//                Intent intent = new Intent("android.intent.action.SEND");
//                intent.putExtra("android.intent.extra.EMAIL", new String[]{"jaibhavaniservices1@gmail.com"});
//                intent.setType("text/plain");
//                ResolveInfo resolveInfo = null;
//                for (ResolveInfo resolveInfo2 : getPackageManager().queryIntentActivities(intent, 0)) {
//                    if (resolveInfo2.activityInfo.packageName.endsWith(".gm") || resolveInfo2.activityInfo.name.toLowerCase().contains("gmail")) {
//                        resolveInfo = resolveInfo2;
//                    }
//                }
//                if (resolveInfo != null) {
//                    intent.setClassName(resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name);
//                }
//                startActivity(intent);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        });
        relMore.setOnClickListener(view -> {
            popUpMore();
//            try {
//                startActivity(new Intent("android.intent.action.VIEW", Uri.parse("YOUR_GOOGLE_PLAY_STORE_ACCOUNT_URL")));
//            } catch (ActivityNotFoundException e) {
//                e.printStackTrace();
//            }
        });
    }
    public static int SYSTEM_LOCK = 35;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SYSTEM_LOCK) {
            if (resultCode == -1) {
                copyToClipboard();
            }
            if (resultCode == 0) {
//                finish();
            }
        }
    }

    private void copyToClipboard(){
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Copied Text", currentText);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(activity, "Copied!", Toast.LENGTH_SHORT).show();
    }

    public void init() {
        ivBack = findViewById(R.id.ivBack);
        relRateUs = findViewById(R.id.relRateUs);
        relShare = findViewById(R.id.relShare);
        relPrivacy = findViewById(R.id.relPrivacy);
        relSuggestion = findViewById(R.id.relSuggestion);
        relMore = findViewById(R.id.relMore);
        relrestore = findViewById(R.id.relrestore);
        relbackup = findViewById(R.id.relbackup);
    }

    public void performRestore(final DatabaseHelper db) {

        Permissions.verifyStoragePermissions(activity);

        File folder = new File(Environment.getExternalStorageDirectory() + File.separator + activity.getResources().getString(R.string.app_name));
        if (folder.exists()) {

            final File[] files = folder.listFiles();

            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(activity, android.R.layout.select_dialog_item);
            for (File file : files)
                arrayAdapter.add(file.getName());

            AlertDialog.Builder builderSingle = new AlertDialog.Builder(activity);
            builderSingle.setTitle("Restore:");
            builderSingle.setNegativeButton(
                    "cancel",
                    (dialog, which) -> dialog.dismiss());
            builderSingle.setAdapter(
                    arrayAdapter,
                    (dialog, which) -> {
                        try {
                            db.importDB(files[which].getPath());
                        } catch (Exception e) {
                            Toast.makeText(activity, "Unable to restore. Retry", Toast.LENGTH_SHORT).show();
                        }
                    });
            builderSingle.show();
        } else
            Toast.makeText(activity, "Backup folder not present.\nDo a backup before a restore!", Toast.LENGTH_SHORT).show();
    }

    public void popUp() {

        PopupDialog.getInstance(this)
                .setStyle(Styles.SUCCESS)
                .setHeading("Privacy Policy")
                .setDescription("We are committed to maintaining the accuracy, confidentiality, and security of your personally identifiable information (\"Personal Information\"). As part of this commitment, our privacy policy governs our actions as they relate to the collection, use and disclosure of Personal Information.")
                .setCancelable(false)
                .showDialog(new OnDialogButtonClickListener() {
                    @Override
                    public void onPositiveClicked(Dialog dialog) {
                        super.onPositiveClicked(dialog);
                    }

                    @Override
                    public void onNegativeClicked(Dialog dialog) {
                        super.onNegativeClicked(dialog);
                    }

                    @Override
                    public void onDismissClicked(Dialog dialog) {
                        super.onDismissClicked(dialog);
                    }
                });
    }

    public void popUpMore() {

        PopupDialog.getInstance(this)
                .setStyle(Styles.ALERT)
                .setHeading("More Apps")
                .setDescription("Our team working on this Module. Waiting For More Apps")
                .setCancelable(false)
                .showDialog(new OnDialogButtonClickListener() {
                    @Override
                    public void onPositiveClicked(Dialog dialog) {
                        super.onPositiveClicked(dialog);
                    }

                    @Override
                    public void onNegativeClicked(Dialog dialog) {
                        super.onNegativeClicked(dialog);
                    }

                    @Override
                    public void onDismissClicked(Dialog dialog) {
                        super.onDismissClicked(dialog);
                    }
                });
    }

}