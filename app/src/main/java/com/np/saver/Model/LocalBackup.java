/*
 *   Copyright 2016 Marco Gomiero
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */

package com.np.saver.Model;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Environment;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.np.saver.R;
import com.np.saver.Activity.SettingActivity;
import com.np.saver.Permissions;
import com.np.saver.database.DatabaseHelper;

import java.io.File;

public class LocalBackup {

    private SettingActivity activity;

    public LocalBackup(SettingActivity activity) {
        this.activity = activity;
    }

    //ask to the user a name for the backup and perform it. The backup will be saved to a custom folder.
    public void performBackup(final DatabaseHelper db, final String outFileName) {

        Permissions.verifyStoragePermissions(activity);

        File folder = new File(Environment.getExternalStorageDirectory() + File.separator + activity.getResources().getString(R.string.app_name));

        boolean success = true;
        if (!folder.exists())
            success = folder.mkdirs();
        if (success) {

            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle("Backup Name");
            final EditText input = new EditText(activity);
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);
            builder.setPositiveButton("Save", (dialog, which) -> {
                String m_Text = input.getText().toString();
                String out = outFileName + m_Text + ".db";
                db.backup(out);
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

            builder.show();
        } else
            Toast.makeText(activity, "Unable to create directory. Retry", Toast.LENGTH_SHORT).show();
    }

    //ask to the user what backup to restore
    public void performRestore(final DatabaseHelper db) {
        Permissions.verifyStoragePermissions(activity);

        File folder = new File(Environment.getExternalStorageDirectory() + File.separator + activity.getResources().getString(R.string.app_name));
        if (folder.exists()) {
            final File[] files = folder.listFiles();
            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(activity, R.layout.spinner_item);
            for (File file : files)
                arrayAdapter.add(file.getName());
            final Dialog dialog = new Dialog(activity);
            dialog.setContentView(R.layout.dialogue_restore);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            TextView cancel = dialog.findViewById(R.id.no);
            Spinner spino = dialog.findViewById(R.id.itemspinner);
            spino.getBackground().setColorFilter(activity.getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
            spino.setAdapter(arrayAdapter);
            spino.setSelection(0, false);
            spino.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        db.importDB(files[position].getPath());
                        dialog.dismiss();
                    } catch (Exception e) {
                        Toast.makeText(activity, "Unable to restore. Retry", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
//            AlertDialog.Builder builderSingle = new AlertDialog.Builder(activity,R.style.AlertDialog);
////            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builderSingle.setTitle(Html.fromHtml("<font color='#FC413F'>Restore Backup</font>"));
//            builderSingle.setCancelable(false);
//            builderSingle.setAdapter(arrayAdapter, (dialog, which) -> {
//                        try {
//
//                            db.importDB(files[which].getPath());
//                        } catch (Exception e) {
//                            Toast.makeText(activity, "Unable to restore. Retry", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//            builderSingle.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int which) {
//                }
//            });
//            builderSingle.show();
        } else {
            Toast.makeText(activity, "Backup folder not present.\nDo a backup before a restore!", Toast.LENGTH_SHORT).show();
        }
    }
}

