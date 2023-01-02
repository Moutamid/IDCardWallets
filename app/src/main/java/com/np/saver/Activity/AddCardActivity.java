package com.np.saver.Activity;

import static com.bumptech.glide.Glide.with;
import static com.bumptech.glide.load.engine.DiskCacheStrategy.DATA;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.fxn.stash.Stash;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.np.saver.Model.CardModel;
import com.np.saver.R;
import com.np.saver.Utils.Constants;
import com.np.saver.databinding.ActivityAddCardBinding;
import com.np.saver.imagePicker.DefaultCallback;
import com.np.saver.imagePicker.EasyImage;
import com.yalantis.ucrop.UCrop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

public class AddCardActivity extends AppCompatActivity {
    private static final String TAG = "AddCardActivity";

    private ActivityAddCardBinding b;
    BottomSheetDialog dialogSelectImage;
    LinearLayout lGallery, lCamera;
    private static final int FRONT_CODE = 9990;
    private static final int GALLERY_CODE = 9996;
    private static final int CAMERA_CODE = 9992;
    int CURRENT_CODE = 1234;
    private static final int BACK_CODE = 9997;
    private ProgressDialog progressDialog;

    CardModel currentModel = new CardModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityAddCardBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        Log.d(TAG, "onCreate: ");

        b.txtTitle.setText(getIntent().getStringExtra(Constants.NAME));

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");

        View view = LayoutInflater.from(this).inflate(R.layout.bottomsheet_select_dialog, null);
        dialogSelectImage = new BottomSheetDialog(this, R.style.BottomSheetDialogThemeNew);
        dialogSelectImage.setContentView(view);
        dialogSelectImage.setCanceledOnTouchOutside(false);
        dialogSelectImage.setDismissWithAnimation(true);

        lGallery = view.findViewById(R.id.lGallery);
        lCamera = view.findViewById(R.id.lCamera);

        lGallery.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.d(TAG, "onClick: lGallery");

                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_CODE);

                if (dialogSelectImage != null) {
                    dialogSelectImage.dismiss();
                }

            }
        });
        lCamera.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.d(TAG, "onClick: lCamera");

                /*Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_CODE);*/

                EasyImage.openCameraForImage(AddCardActivity.this, CAMERA_CODE);


                if (dialogSelectImage != null) {
                    dialogSelectImage.dismiss();
                }
            }
        });

        b.imgFront.setOnClickListener(v -> {
            Log.d(TAG, "onClick: imgFront");
            if (dialogSelectImage != null) {
                CURRENT_CODE = FRONT_CODE;
                dialogSelectImage.show();
            }
        });
        b.imgBack.setOnClickListener(v -> {
            Log.d(TAG, "onClick: imgBack");
            if (dialogSelectImage != null) {
                CURRENT_CODE = BACK_CODE;
                dialogSelectImage.show();
            }
        });

        b.frontPlaceHolderLayout.setOnClickListener(v -> {
            Log.d(TAG, "onClick: imgFront");
            if (dialogSelectImage != null) {
                CURRENT_CODE = FRONT_CODE;
                dialogSelectImage.show();
            }
        });

        b.backPlaceHolderLayout.setOnClickListener(v -> {
            Log.d(TAG, "onClick: imgBack");
            if (dialogSelectImage != null) {
                CURRENT_CODE = BACK_CODE;
                dialogSelectImage.show();
            }
        });

        b.ivDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (b.etName.getText().toString().isEmpty()) {
                    Toast.makeText(AddCardActivity.this, "Please enter name!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    Log.d(TAG, "onClick: name added");
                    currentModel.name = b.etName.getText().toString();
                }

                if (currentModel.img_back.isEmpty()) {
                    Toast.makeText(AddCardActivity.this, "Please add back card!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (currentModel.img_front.isEmpty()) {
                    Toast.makeText(AddCardActivity.this, "Please add front card!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // IF NEW CARD THEN INCREASE TOTAL COUNT OF CATEGORY
                if (currentModel.push_key == null) {
                    Log.d(TAG, "onClick: if (currentModel.push_key.isEmpty()) {");
                    currentModel.push_key = Constants.databaseReference().push().getKey();
                    progressDialog.show();
                    Constants.databaseReference().child(Constants.CATEGORIES)
                            .child(getIntent().getStringExtra(Constants.PARAMS))
                            .child("total_count")
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    Log.d(TAG, "onDataChange: ");
//                                    if (snapshot.exists()){

                                    int totalCount = snapshot.getValue(Integer.class);
                                    totalCount++;
                                    Constants.databaseReference().child(Constants.CATEGORIES)
                                            .child(getIntent().getStringExtra(Constants.PARAMS))
                                            .child("total_count")
                                            .setValue(totalCount);

                                    Constants.databaseReference().child(Constants.CARDS)
                                            .child(getIntent().getStringExtra(Constants.PARAMS))
                                            .child(currentModel.push_key)
                                            .setValue(currentModel);
                                    progressDialog.dismiss();
                                    Toast.makeText(AddCardActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                    finish();
//                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    progressDialog.dismiss();
                                    Toast.makeText(AddCardActivity.this, error.toException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                } else {
                    Log.d(TAG, "onClick: } else {");

                    Constants.databaseReference().child(Constants.CARDS)
                            .child(getIntent().getStringExtra(Constants.PARAMS))
                            .child(currentModel.push_key)
                            .setValue(currentModel);

                    Toast.makeText(AddCardActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

        b.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // IT IS IN EDIT MODE
        if (getIntent().getBooleanExtra(Constants.IS_EDIT, false)) {
            Log.d(TAG, "onCreate: if (getIntent().getBooleanExtra(Constants.PARAMS, false)) {");
            currentModel = (CardModel) Stash.getObject(Constants.CURRENT_CARD, CardModel.class);

            b.frontPlaceHolderLayout.setVisibility(View.GONE);
            b.backPlaceHolderLayout.setVisibility(View.GONE);
            b.frontImageLayout.setVisibility(View.VISIBLE);
            b.backImageLayout.setVisibility(View.VISIBLE);

            b.etName.setText(currentModel.name);

            with(getApplicationContext())
                    .asBitmap()
                    .load(currentModel.img_back)
                    /*.apply(new RequestOptions()
                            .placeholder(R.color.lighterGrey)
                            .error(R.color.lighterGrey)
                    )*/
                    .diskCacheStrategy(DATA)
                    .into(b.imgBack);

            with(getApplicationContext())
                    .asBitmap()
                    .load(currentModel.img_front)
                    /*.apply(new RequestOptions()
                            .placeholder(R.color.lighterGrey)
                            .error(R.color.lighterGrey)
                    )*/
                    .diskCacheStrategy(DATA)
                    .into(b.imgFront);

        }

    }
    Bitmap photo;

    class C06489 extends DefaultCallback {
        C06489() {
        }

        public void onImagePickerError(Exception exception, EasyImage.ImageSource imageSource, int i) {

            Log.e("Exception--)",""+exception.getMessage());
            exception.printStackTrace();
        }

        public void onImagesPicked(List<File> list, EasyImage.ImageSource imageSource, int i) {
            String stirn = "SampleCropImage";
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(imageSource);
            stringBuilder.append(".jpg");
            stirn = stringBuilder.toString();
            UCrop.Options options = new UCrop.Options();
            options.setFreeStyleCropEnabled(true);
            options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
            Log.e("Picked--)",""+stirn);
            UCrop.of(Uri.fromFile((File) list.get(0)), Uri.fromFile(new File(getCacheDir(), stirn)))
                    .withOptions(options).withMaxResultSize(16843039, 16843040)
                    .start(AddCardActivity.this);
        }

        public void onCanceled(EasyImage.ImageSource imageSource, int i) {
            if (imageSource == EasyImage.ImageSource.CAMERA_IMAGE) {
                File file = EasyImage.lastlyTakenButCanceledPhoto(getApplicationContext());
                if (file != null) {
                    file.delete();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new C06489());
        if (resultCode == RESULT_OK) {
            Log.d(TAG, "onActivityResult: if (resultCode == RESULT_OK) {");

            UCrop.Options options = new UCrop.Options();
            options.setFreeStyleCropEnabled(true);
            options.setCompressionFormat(Bitmap.CompressFormat.PNG);
            options.setCompressionQuality(100);

            if (requestCode == GALLERY_CODE) {
                Log.d(TAG, "onActivityResult: if (requestCode == CODE) {");
                Uri imageUri = data.getData();

                UCrop.of(imageUri, Uri.fromFile(new File(getCacheDir(), "image.png")))
                        .withOptions(options).withMaxResultSize(16843039, 16843040)
                        .start(AddCardActivity.this);
            }
            /*if (requestCode == CAMERA_CODE) {
                Log.d(TAG, "onActivityResult: if (requestCode == CODE) {");
                photo = (Bitmap) data.getExtras().get("data");

                Log.d(TAG, "onActivityResult: CAMERA_RESULT: "+data.getExtras().get("data"));
                Uri imageUri = getImageUri(photo);

                UCrop.of(imageUri, Uri.fromFile(new File(getCacheDir(), "image.png")))
                        .withOptions(options).withMaxResultSize(16843039, 16843040)
                        .start(AddCardActivity.this);
            }*/

            if (requestCode == UCrop.REQUEST_CROP) {
                Log.d(TAG, "onActivityResult: if (requestCode == UCrop.REQUEST_CROP) {");

                Uri imageUri = UCrop.getOutput(data);

                StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("cards");

                progressDialog.show();

                final StorageReference filePath = storageReference
                        .child(System.currentTimeMillis() + imageUri.getLastPathSegment());
                filePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri photoUrl) {
                                Log.d(TAG, "onSuccess: ");
                                if (CURRENT_CODE == FRONT_CODE) {
                                    Log.d(TAG, "onSuccess: if (CURRENT_CODE == FRONT_CODE) {");
                                    currentModel.img_front = photoUrl.toString();
                                    b.imgFront.setImageURI(imageUri);

                                    b.frontPlaceHolderLayout.setVisibility(View.GONE);
                                    b.frontImageLayout.setVisibility(View.VISIBLE);
                                }
                                if (CURRENT_CODE == BACK_CODE) {
                                    Log.d(TAG, "onSuccess: if (CURRENT_CODE == BACK_CODE) {");
                                    currentModel.img_back = photoUrl.toString();
                                    b.imgBack.setImageURI(imageUri);

                                    b.backPlaceHolderLayout.setVisibility(View.GONE);
                                    b.backImageLayout.setVisibility(View.VISIBLE);
                                }

                                progressDialog.dismiss();
                                Toast.makeText(AddCardActivity.this, "Success", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(AddCardActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    public Uri getImageUri(Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

}