package com.np.saver.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.np.saver.Model.Cardgetset;
import com.np.saver.R;
import com.np.saver.Utils.Adsload;
import com.np.saver.database.DatabaseHelper;
import com.np.saver.imagePicker.DefaultCallback;
import com.np.saver.imagePicker.EasyImage;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.yalantis.ucrop.UCrop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SingleCardActivity extends AppCompatActivity {

    private ImageView ivBack, ivDone, ivDeleteFront, ivDeleteBack, img_back,
            img_front, ivBackAdd, ivfrontAdd, ivSetting;
    TextView txtTitle;
    EditText etName;
    boolean isFirstTimeUpdateCall = true;
    boolean isForInsert;
    boolean isFrontSelected;
    boolean updateSelected = false;
    InputStream iStream;
    byte[] imageByte_back;
    byte[] imageByte_front;
    DatabaseHelper databaseHelper;
    public static File destination_crop;
    public static String cropPath;
    private static final String IMAGE_DIRECTORY_NAME = ".cardwallet";
    public int GALLLERY_INTENT_CODE = 1001;
    Bitmap bitmap;
    Bitmap image;
    LinearLayout lEdittext;
    List<Cardgetset> cardgetsets = new ArrayList<>();
    String cardid;
    String categoryId;
    String categoryName;
    private String mImageFullPathAndName = "";
    BottomSheetDialog dialogSelectImage;
    LinearLayout lGallery, lCamera,lSetting,lEdit,lDelete;
    Adsload adsload;
    FrameLayout frameBanner;
    InterstitialAd mInterstitialAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_card);
        init();
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
        ivBack.setOnClickListener(view -> onBackPressed());

        img_front.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isFrontSelected = true;
                setImageFront();
            }
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isFrontSelected = false;
                setImageBack();
            }
        });

        ivDeleteFront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setupDeleteFront();
            }
        });

        ivDeleteBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setupDelteteBack();
            }
        });

        ivDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mInterstitialAd != null) {
                    mInterstitialAd.show(SingleCardActivity.this);

                    mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            addData();
                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(AdError adError) {
                            addData();
                        }

                        @Override
                        public void onAdShowedFullScreenContent() {
                            mInterstitialAd = null;
                        }
                    });

                } else {
                    addData();
                }


            }
        });

        lDelete.setOnClickListener(view -> {
            deleteData();
        });

        lEdit.setOnClickListener(view -> {
            lSetting.setVisibility(View.GONE);
            ivDone.setVisibility(View.VISIBLE);
            this.updateSelected = true;
            updatedata();
        });

        this.databaseHelper = new DatabaseHelper(this);
        StringBuilder str = new StringBuilder();
        str.append(Environment.getExternalStorageDirectory());
        str.append("/");
        str.append(getString(R.string.app_name));
        str.append("/.crop_gallery_folder");
        destination_crop = new File(str.toString());
        if (destination_crop.exists()) {
            destination_crop.mkdirs();
        }
        Intent bund = getIntent();
        this.isForInsert = bund.getBooleanExtra("isforInsert", true);
        if (this.isForInsert) {
            categoryId = bund.getStringExtra("categoryId");
            categoryName = bund.getStringExtra("categoryName");
            txtTitle.setText(this.categoryName);
        } else {
            cardid = bund.getStringExtra("CardId");
            categoryId = bund.getStringExtra("categoryId");
            getDataForUpdate();
        }
        StringBuilder dle = new StringBuilder();
        dle.append("");
        dle.append(this.databaseHelper.getcard(this.categoryId).size());
        if (this.isForInsert) {
            cardgetsets = this.cardgetsets;
        }

    }

    public void init() {
        ivBack = findViewById(R.id.ivBack);
        ivDone = findViewById(R.id.ivDone);
        txtTitle = findViewById(R.id.txtTitle);
        ivDeleteFront = findViewById(R.id.ivDeleteFront);
        ivSetting = findViewById(R.id.ivSetting);
        ivDeleteBack = findViewById(R.id.ivDeleteBack);
        img_back = findViewById(R.id.img_back);
        img_front = findViewById(R.id.img_front);
        etName = findViewById(R.id.etName);
        ivBackAdd = findViewById(R.id.ivBackAdd);
        ivfrontAdd = findViewById(R.id.ivfrontAdd);
        lEdittext = findViewById(R.id.lEdittext);
        lSetting = findViewById(R.id.lSetting);
        lEdit = findViewById(R.id.lEdit);
        lDelete = findViewById(R.id.lDelete);

        frameBanner=findViewById(R.id.frameBanner);
        adsload=new Adsload(SingleCardActivity.this);
        adsload.loadBanner(SingleCardActivity.this,frameBanner);

        View view = LayoutInflater.from(this).inflate(R.layout.bottomsheet_select_dialog, null);
        dialogSelectImage = new BottomSheetDialog(this, R.style.BottomSheetDialogThemeNew);
        dialogSelectImage.setContentView(view);
        dialogSelectImage.setCanceledOnTouchOutside(false);
        dialogSelectImage.setDismissWithAnimation(true);

        lGallery = view.findViewById(R.id.lGallery);
        lCamera = view.findViewById(R.id.lCamera);
    }

    @SuppressLint("WrongConstant")
    void setImageBack() {
        if (this.isForInsert) {
            setImageFront();
        } else if (this.updateSelected) {
            setImageFront();
        } else {
            Intent intent = new Intent(this, ViewImageActivity.class);
            if (this.imageByte_back != null) {
                if (this.imageByte_front != null) {
                    intent.putExtra("path", tempFileImage(this, getImage(this.imageByte_front), "NAME"));
                    intent.putExtra("NAME", etName.getText().toString());
                }
                if (this.imageByte_back != null) {
                    intent.putExtra("path1", tempFileImage(this, getImage(this.imageByte_back), "name1"));
                    intent.putExtra("NAME", etName.getText().toString());
                }
                intent.putExtra("position", 1);
                intent.setFlags(67108864);
                startActivity(intent);
            }
        }
    }

    public static String tempFileImage(Context context, Bitmap bitmap, String str) {
        File cacheDir = context.getCacheDir();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(str);
        stringBuilder.append(".jpg");
        File strf = new File(cacheDir, stringBuilder.toString());
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(strf);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return strf.getAbsolutePath();
    }

    protected void onResume() {
        super.onResume();
    }

    public void onBackPressed() {
        super.onBackPressed();
        if (this.isForInsert) {
            setResult(2001);
            finish();
            return;
        }
        setResult(1001);
        finish();
    }

    @SuppressLint("WrongConstant")
    void setImageFront() {
        if (this.isForInsert) {
            setUpImageDialogFront();
        } else if (this.updateSelected) {
            setUpImageDialogFront();
        } else {
            Intent intent = new Intent(this, ViewImageActivity.class);
            if (this.imageByte_front != null) {
                intent.putExtra("path", tempFileImage(this, getImage(this.imageByte_front), "NAME"));
                intent.putExtra("NAME", etName.getText().toString());
                if (this.imageByte_back != null) {
                    intent.putExtra("path1", tempFileImage(this, getImage(this.imageByte_back), "name1"));
                    intent.putExtra("NAME", etName.getText().toString());
                }
                intent.putExtra("position", 0);
                intent.setFlags(67108864);
                startActivity(intent);
            }
        }
    }

    void setUpImageDialogFront() {

        if (dialogSelectImage != null) {
            dialogSelectImage.show();
        }
        lGallery.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.d("isClick", "isClick");
                EasyImage.openGallery(SingleCardActivity.this, SingleCardActivity.this.GALLLERY_INTENT_CODE);
                if (dialogSelectImage != null) {
                    dialogSelectImage.dismiss();
                }

            }
        });
        lCamera.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                EasyImage.openCameraForImage(SingleCardActivity.this, SingleCardActivity.this.GALLLERY_INTENT_CODE);
                if (dialogSelectImage != null) {
                    dialogSelectImage.dismiss();
                }
            }
        });
    }

    private void setupDelteteBack() {
        this.img_back.setImageBitmap(null);
        this.imageByte_back = null;
        this.ivDeleteBack.setVisibility(View.GONE);
        this.ivBackAdd.setVisibility(View.VISIBLE);
    }

    @SuppressLint("WrongConstant")
    private void setupDeleteFront() {
        this.img_front.setImageBitmap(null);
        this.imageByte_front = null;
        this.ivDeleteFront.setVisibility(8);
        this.ivfrontAdd.setVisibility(0);
    }

    void addData() {

        Log.e("addData: ", "" + isForInsert);
        if (this.isForInsert) {

            if (etName.getText().toString().trim().equals("")) {
                Toast.makeText(getApplicationContext(), "Enter card title..", Toast.LENGTH_SHORT).show();
                return;
            }
            if (this.imageByte_front == null) {
                if (this.imageByte_back == null) {
                    Toast.makeText(getApplicationContext(), "Card Image can not be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            new DatabaseHelper(this).addEntry(etName.getText().toString(), this.imageByte_front, this.imageByte_back, this.categoryId);
            onBackPressed();
        } else if (etName.getText().toString().trim().equals("")) {
            Toast.makeText(getApplicationContext(), "Enter card title..", Toast.LENGTH_SHORT).show();
        } else {
            if (this.imageByte_front == null) {
                if (this.imageByte_back == null) {
                    Toast.makeText(getApplicationContext(), "Card Image can not be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            if (new DatabaseHelper(this).updatecard(this.cardid, etName.getText().toString(), this.imageByte_front, this.imageByte_back) > 0) {
                this.ivDone.setVisibility(View.GONE);
                this.lEdittext.setVisibility(View.GONE);
                txtTitle.setText(etName.getText().toString());
                this.updateSelected = false;
                this.ivfrontAdd.setVisibility(View.GONE);
                this.ivBackAdd.setVisibility(View.GONE);
                this.ivDeleteFront.setVisibility(View.GONE);
                this.ivDeleteBack.setVisibility(View.GONE);
                lSetting.setVisibility(View.VISIBLE);
            }
        }
    }

    void updatedata() {
        if (!this.isFirstTimeUpdateCall) {
            getDataForUpdate();
        }
        this.isFirstTimeUpdateCall = false;
        setupPlusIcon();
        this.img_front.setEnabled(true);
        this.img_back.setEnabled(true);
        lSetting.setVisibility(View.GONE);
        this.ivDone.setVisibility(View.VISIBLE);
        if ((this.cardgetsets.get(0)).getName() != null) {
            etName.setText((this.cardgetsets.get(0)).getName());
        }
        this.lEdittext.setVisibility(View.VISIBLE);
    }

    @SuppressLint("ResourceType")
    void deleteData() {

        View view = LayoutInflater.from(this).inflate(R.layout.bottomsheet_delete_dialog, null);
        BottomSheetDialog dialogDeleteImage = new BottomSheetDialog(this, R.style.BottomSheetDialogThemeNew);
        dialogDeleteImage.setContentView(view);
        dialogDeleteImage.setCanceledOnTouchOutside(false);
        dialogDeleteImage.setDismissWithAnimation(true);

        LinearLayout ivCloseDelete=dialogDeleteImage.findViewById(R.id.ivCloseDelete);
        LinearLayout ivDoneDelete=dialogDeleteImage.findViewById(R.id.ivDoneDelete);
        dialogDeleteImage.show();

        ivDoneDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseHelper.deletecard(cardid);
                dialogDeleteImage.dismiss();
                setResult(1001);
                finish();
            }
        });

        ivCloseDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogDeleteImage.dismiss();
            }
        });

    }

    public void getDataForUpdate() {
        this.cardgetsets.clear();

        this.cardgetsets.addAll(this.databaseHelper.getForUpdate(this.cardid, this.categoryId));
        Log.e("getDataForUpdate: ", "" + this.cardgetsets);
        this.lEdittext.setVisibility(View.GONE);
        ivDone.setVisibility(View.GONE);
        lSetting.setVisibility(View.VISIBLE);
        if (this.cardgetsets != null) {
            if (this.cardgetsets.size() > 0) {
                etName.setText((this.cardgetsets.get(0)).getName());
            }
            txtTitle.setText((this.cardgetsets.get(0)).getName());
            if (this.cardgetsets.size() > 0) {
                this.imageByte_front = (this.cardgetsets.get(0)).getImage_front();
            }
            if (this.cardgetsets.size() > 0) {
                this.imageByte_back = (this.cardgetsets.get(0)).getImage_back();
            }
            if (this.imageByte_front != null) {
                this.img_front.setImageBitmap(getImage(this.imageByte_front));
            }
            if (this.imageByte_back != null) {
                this.img_back.setImageBitmap(getImage(this.imageByte_back));
            }
            this.ivfrontAdd.setVisibility(View.GONE);
            this.ivBackAdd.setVisibility(View.GONE);
        }
    }

    void setupPlusIcon() {
        if (this.imageByte_front != null) {
            this.ivfrontAdd.setVisibility(View.GONE);
            this.ivDeleteFront.setVisibility(View.VISIBLE);
        } else {
            this.ivfrontAdd.setVisibility(View.VISIBLE);
            this.ivDeleteFront.setVisibility(View.GONE);
        }
        if (this.imageByte_back != null) {
            this.ivBackAdd.setVisibility(View.GONE);
            this.ivDeleteBack.setVisibility(View.VISIBLE);
            return;
        }
        this.ivBackAdd.setVisibility(View.VISIBLE);
        this.ivDeleteBack.setVisibility(View.GONE);
    }

    private static File getOutputMediaFile(int i) {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), IMAGE_DIRECTORY_NAME);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create .cardwallet directory");
                return null;
            }
        }
        String format = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        if (i != 10) {
            return null;
        }
        StringBuilder ii = new StringBuilder();
        ii.append(file.getPath());
        ii.append(File.separator);
        ii.append("image");
        ii.append(format);
        ii.append(".jpg");
        return new File(ii.toString());
    }

    public byte[] getBytes1(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bArr = new byte[1024];
        while (true) {
            int read = inputStream.read(bArr);
            if (read == -1) {
                return byteArrayOutputStream.toByteArray();
            }
            byteArrayOutputStream.write(bArr, 0, read);
        }
    }

    public void setissteam() {
        this.mImageFullPathAndName = cropPath;
        try {
            this.iStream = getContentResolver().openInputStream(Uri.fromFile(new File(this.mImageFullPathAndName)));
            if (this.isFrontSelected) {
                this.imageByte_front = getBytes1(this.iStream);
                this.bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.fromFile(new File(this.mImageFullPathAndName)));
                this.img_front.setImageBitmap(this.bitmap);
                return;
            }
            this.imageByte_back = getBytes1(this.iStream);
            this.bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.fromFile(new File(this.mImageFullPathAndName)));
            this.img_back.setImageBitmap(this.bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("WrongConstant")
    protected void onActivityResult(int i, int i2, @Nullable Intent intent) {
        super.onActivityResult(i, i2, intent);
        EasyImage.handleActivityResult(i, i2, intent, this, new C06489());
        if (i2 == -1 && i == 69) {
            Uri qi = UCrop.getOutput(intent);
            if (i == 0) {
                return;
            }
            if (this.isFrontSelected) {
                try {
                    cropPath = qi.getPath();
                    setissteam();
                    Uri uri = intent.getData();
                    loadFromUri(this, uri);
                    this.ivDeleteFront.setVisibility(0);
                    return;
                } catch (Exception i3) {
                    i3.printStackTrace();
                    return;
                }
            }
            try {
                cropPath = qi.getPath();
                setissteam();
                Uri uri = intent.getData();
                loadFromUri(this, uri);
                this.ivDeleteBack.setVisibility(0);
            } catch (Exception i32) {
                i32.printStackTrace();
            }
        }
    }

    public static Bitmap getImage(byte[] bArr) {
        return BitmapFactory.decodeByteArray(bArr, 0, bArr.length);
    }

    void  loadFromUri (Context context, Uri photoUri){
        try {
            //check version of Android on device
            if (Build.VERSION.SDK_INT > 27) {
                //on newer versions of Android, use the new decodeBitmap method
                ImageDecoder.Source source =
                        ImageDecoder.createSource(getContentResolver(), photoUri);

                image = ImageDecoder.decodeBitmap(source);
            } else {
                //support older versions of Android by using getBitmap
                image = MediaStore.Images.Media.getBitmap(context.getContentResolver(), photoUri);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



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
            UCrop.of(Uri.fromFile((File) list.get(0)), Uri.fromFile(new File(getCacheDir(), stirn))).withOptions(options).withMaxResultSize(16843039, 16843040).start(SingleCardActivity.this);
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

}