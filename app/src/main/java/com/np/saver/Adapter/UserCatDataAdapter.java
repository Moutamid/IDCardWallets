package com.np.saver.Adapter;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.np.saver.Activity.CardCustomListActivity;
import com.np.saver.Model.UserCat;
import com.np.saver.database.DatabaseHelper;
import com.np.saver.R;

import java.util.ArrayList;


public class UserCatDataAdapter extends Adapter<UserCatDataAdapter.MyViewHolder> {
    private ArrayList<UserCat> bookList;
    Activity con;
    DatabaseHelper databaseHelper;
    InterstitialAd mInterstitialAd;
    public class MyViewHolder extends ViewHolder {
        public ImageView img_card,delete;
        public LinearLayout rel_card;
        public TextView txt_cat;
        public TextView txt_size;

        public MyViewHolder(View view) {
            super(view);
            this.txt_cat = (TextView) view.findViewById(R.id.txtTitle);
            this.txt_size = (TextView) view.findViewById(R.id.txtCounter);
            this.img_card = (ImageView) view.findViewById(R.id.ivIcon);
            this.rel_card = (LinearLayout) view.findViewById(R.id.lCard);
            this.delete = (ImageView) view.findViewById(R.id.delete);
        }
    }

    public UserCatDataAdapter(Activity context, ArrayList<UserCat> arrayList) {
        this.bookList = arrayList;
        this.con = context;
        loadAds();
    }

    private void loadAds() {
        InterstitialAd.load(con,con.getString(R.string.interstitial_ID), new AdRequest.Builder().build(), new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                mInterstitialAd = interstitialAd;
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                mInterstitialAd = null;

            }
        });
    }

    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_box1, viewGroup, false));
    }

    public void onBindViewHolder(MyViewHolder myViewHolder, final int i) {
        this.databaseHelper = new DatabaseHelper(this.con);
//        myViewHolder.img_card.setImageResource(getImageUsingType(this.bookList.get(i).getImageType()));
        myViewHolder.img_card.setImageBitmap(getImage(bookList.get(i).getImage_icon()));
        TextView textView = myViewHolder.txt_size;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("");
        DatabaseHelper databaseHelper = this.databaseHelper;
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append((this.bookList.get(i)).getId());
        stringBuilder2.append("");
        stringBuilder.append(databaseHelper.getcard1(stringBuilder2.toString()).size());
        textView.setText(stringBuilder.toString());
        myViewHolder.rel_card.setTag(i);
        myViewHolder.rel_card.setOnClickListener(new OnClickListener() {
            @SuppressLint("WrongConstant")
            public void onClick(View view) {

                if (mInterstitialAd != null) {
                    mInterstitialAd.show(con);

                    mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            loadAds();
                            Intent viewq = new Intent(UserCatDataAdapter.this.con, CardCustomListActivity.class);
                            viewq.putExtra("card", String.valueOf((UserCatDataAdapter.this.bookList.get(i)).getId()));
                            viewq.putExtra("cardget", (UserCatDataAdapter.this.bookList.get(i)).getName());
                            viewq.setFlags(67108864);
                            UserCatDataAdapter.this.con.startActivity(viewq);
                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(AdError adError) {
                            Intent viewq = new Intent(UserCatDataAdapter.this.con, CardCustomListActivity.class);
                            viewq.putExtra("card", String.valueOf((UserCatDataAdapter.this.bookList.get(i)).getId()));
                            viewq.putExtra("cardget", (UserCatDataAdapter.this.bookList.get(i)).getName());
                            viewq.setFlags(67108864);
                            UserCatDataAdapter.this.con.startActivity(viewq);
                        }

                        @Override
                        public void onAdShowedFullScreenContent() {
                            mInterstitialAd = null;
                        }
                    });

                } else {
                    Intent viewq = new Intent(UserCatDataAdapter.this.con, CardCustomListActivity.class);
                    viewq.putExtra("card", String.valueOf((UserCatDataAdapter.this.bookList.get(i)).getId()));
                    viewq.putExtra("cardget", (UserCatDataAdapter.this.bookList.get(i)).getName());
                    viewq.setFlags(67108864);
                    UserCatDataAdapter.this.con.startActivity(viewq);
                }


            }
        });
        myViewHolder.txt_cat.setText((this.bookList.get(i)).getName());


    }

    public int getItemCount() {
        return this.bookList.size();
    }

    public static Bitmap getImage(byte[] bArr) {
        return BitmapFactory.decodeByteArray(bArr, 0, bArr.length);
    }
}
