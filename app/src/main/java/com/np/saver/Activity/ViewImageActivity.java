package com.np.saver.Activity;

import static com.bumptech.glide.Glide.with;
import static com.bumptech.glide.load.engine.DiskCacheStrategy.DATA;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.fxn.stash.Stash;
import com.np.saver.Adapter.CustomPagerAdapter;
import com.np.saver.Model.CardModel;
import com.np.saver.R;
import com.np.saver.Utils.Adsload;
import com.np.saver.Utils.Constants;
import com.np.saver.databinding.ActivityViewImageBinding;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Objects;

public class ViewImageActivity extends AppCompatActivity {

    CardModel cardModel = (CardModel) Stash.getObject(Constants.CURRENT_CARD, CardModel.class);

    //    private ImageView ivBack,ivShare;
//    TextView txtTitle;
//    ViewPager viewPager;
//    int pos;
//    private ArrayList<String> ImagesArray = new ArrayList();
//    CustomPagerAdapter myCustomPagerAdapter;
//    Adsload adsload;
//    FrameLayout frameBanner;
    private ActivityViewImageBinding b;
    String[] images = {cardModel.img_front, cardModel.img_back};
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityViewImageBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");

        ViewPagerAdapter mViewPagerAdapter = new ViewPagerAdapter(this, images);

        b.viewPagerViewImage.setAdapter(mViewPagerAdapter);

        b.viewPagerViewImage.setCurrentItem(getIntent().getIntExtra(Constants.IMG_CLICKED, 0), true);

        b.ivBack.setOnClickListener(v -> finish());

        b.txtTitle.setText(cardModel.name);

        b.ivShare.setOnClickListener(v -> {
            progressDialog.show();
            Glide.with(this)
                    .asBitmap()
                    .load(images[b.viewPagerViewImage.getCurrentItem()])
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            Bitmap icon = resource;
                            Intent share = new Intent(Intent.ACTION_SEND);
                            share.setType("image/jpeg");

                            ContentValues values = new ContentValues();
                            values.put(MediaStore.Images.Media.TITLE, "title");
                            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                            Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                    values);

                            OutputStream outstream;
                            try {
                                outstream = getContentResolver().openOutputStream(uri);
                                icon.compress(Bitmap.CompressFormat.JPEG, 100, outstream);
                                outstream.close();
                            } catch (Exception e) {
                                System.err.println(e.toString());
                            }

                            share.putExtra(Intent.EXTRA_STREAM, uri);
                            progressDialog.dismiss();
                            startActivity(Intent.createChooser(share, "Share Image"));
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                            progressDialog.dismiss();
                        }
                    });

        });

//        init();

        /*ivBack.setOnClickListener(view -> {
            finish();
        });*/

        /*pos = getIntent().getIntExtra("position", -1);
        if (getIntent().getStringExtra("path") != null) {
            ImagesArray.add(getIntent().getStringExtra("path"));
        }
        if (getIntent().getStringExtra("path1") != null) {
            ImagesArray.add(getIntent().getStringExtra("path1"));
        }
        myCustomPagerAdapter = new CustomPagerAdapter(this, ImagesArray);
        viewPager.setAdapter(myCustomPagerAdapter);
        if (ImagesArray.size() == 2) {
            viewPager.setCurrentItem(pos);
        }

        ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareImages();
            }
        });*/
    }
    /*public void init()
    {
        ivBack=findViewById(R.id.ivBack);
        txtTitle=findViewById(R.id.txtTitle);
        ivShare=findViewById(R.id.ivShare);
        viewPager=findViewById(R.id.viewPagerViewImage);
        frameBanner=findViewById(R.id.frameBanner);
        adsload=new Adsload(ViewImageActivity.this);
        adsload.loadBanner(ViewImageActivity.this,frameBanner);
        txtTitle.setText(cardModel.name);
    }
*/
    /*void shareImages() {
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("image/*");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        intent.putExtra("android.intent.extra.STREAM", getImageUri(this, BitmapFactory.decodeFile(new File((String) ImagesArray.get(viewPager.getCurrentItem())).getAbsolutePath())));
        try {
            startActivity(Intent.createChooser(intent, "My Profile ..."));
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }
    public Uri getImageUri(Context context, Bitmap bitmap) {
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new ByteArrayOutputStream());
        return Uri.parse(MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Title", null));
    }*/

    class ViewPagerAdapter extends PagerAdapter {
        Context context;
        String[] images;
        LayoutInflater mLayoutInflater;

        public ViewPagerAdapter(Context context, String[] images) {
            this.context = context;
            this.images = images;
            mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return images.length;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == ((LinearLayout) object);
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, final int position) {
            View itemView = mLayoutInflater.inflate(R.layout.item, container, false);

            ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);

            with(getApplicationContext())
                    .asBitmap()
                    .load(images[position])
                    /*.apply(new RequestOptions()
                            .placeholder(R.color.lighterGrey)
                            .error(R.color.lighterGrey)
                    )*/
                    .diskCacheStrategy(DATA)
                    .into(imageView);

            // Adding the View
            Objects.requireNonNull(container).addView(itemView);

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }
    }

}