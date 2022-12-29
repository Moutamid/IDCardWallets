package com.np.saver.Adapter;

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
import com.np.saver.Activity.CardListActivity;
import com.np.saver.Model.CategoryModel;
import com.np.saver.R;
import com.np.saver.Utils.Constants;
import com.np.saver.database.DatabaseHelper;

import java.util.ArrayList;

public class MainDataAdapter extends Adapter<MainDataAdapter.MyViewHolder> {
    private ArrayList<CategoryModel> categoryModelArrayList;
    Activity con;
//    DatabaseHelper databaseHelper;
    InterstitialAd mInterstitialAd;

    public class MyViewHolder extends ViewHolder {
        public ImageView img_card;
        public LinearLayout rel_card;
        public TextView txt_cat;
        public TextView txt_size;

        public MyViewHolder(View view) {
            super(view);
            this.txt_cat = (TextView) view.findViewById(R.id.txtTitle);
            this.txt_size = (TextView) view.findViewById(R.id.txtCounter);
            this.img_card = (ImageView) view.findViewById(R.id.ivIcon);
            this.rel_card = (LinearLayout) view.findViewById(R.id.lCard);
        }
    }

    public MainDataAdapter(Activity context, ArrayList<CategoryModel> arrayList) {
        this.categoryModelArrayList = arrayList;
        this.con = context;
        loadAds();
    }

    private void loadAds() {
        InterstitialAd.load(con, con.getString(R.string.interstitial_ID), new AdRequest.Builder().build(), new InterstitialAdLoadCallback() {
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
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_box, viewGroup, false));
    }

    public void onBindViewHolder(MyViewHolder myViewHolder, final int i) {
        CategoryModel categoryModel = categoryModelArrayList.get(i);

        myViewHolder.txt_cat.setText(categoryModel.name);
        myViewHolder.txt_size.setText(categoryModel.total_count+"");

        myViewHolder.img_card.setImageResource(categoryModel.image);

        myViewHolder.rel_card.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInterstitialAd != null) {
                    mInterstitialAd.show(con);

                    mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            loadAds();
                            Intent viewq = new Intent(MainDataAdapter.this.con, CardListActivity.class);
                            viewq.putExtra(Constants.PARAMS, categoryModel.push_key);
                            viewq.putExtra(Constants.NAME, categoryModel.name);
                            MainDataAdapter.this.con.startActivity(viewq);
                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(AdError adError) {
                            Intent viewq = new Intent(MainDataAdapter.this.con, CardListActivity.class);
                            viewq.putExtra(Constants.PARAMS, categoryModel.push_key);
                            viewq.putExtra(Constants.NAME, categoryModel.name);
                            MainDataAdapter.this.con.startActivity(viewq);
                        }

                        @Override
                        public void onAdShowedFullScreenContent() {
                            mInterstitialAd = null;
                        }
                    });

                } else {
                    Intent viewq = new Intent(MainDataAdapter.this.con, CardListActivity.class);
                    viewq.putExtra(Constants.PARAMS, categoryModel.push_key);
                    viewq.putExtra(Constants.NAME, categoryModel.name);
                    MainDataAdapter.this.con.startActivity(viewq);
                }

            }
        });

        /*img_card
                rel_card
        txt_cat
                txt_size*/



       /* this.databaseHelper = new DatabaseHelper(this.con);
        myViewHolder.img_card.setImageResource(getImageUsingType(this.categoryModelArrayList.get(i).getImageType()));
        TextView textView = myViewHolder.txt_size;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("");
        DatabaseHelper databaseHelper = this.databaseHelper;
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append((this.categoryModelArrayList.get(i)).getId());
        stringBuilder2.append("");
        stringBuilder.append(databaseHelper.getcard(stringBuilder2.toString()).size());
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
                            Intent viewq = new Intent(MainDataAdapter.this.con, CardListActivity.class);
                            viewq.putExtra("card", String.valueOf((MainDataAdapter.this.categoryModelArrayList.get(i)).getId()));
                            viewq.putExtra("cardget", (MainDataAdapter.this.categoryModelArrayList.get(i)).getName());
                            viewq.setFlags(67108864);
                            MainDataAdapter.this.con.startActivity(viewq);
                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(AdError adError) {
                            Intent viewq = new Intent(MainDataAdapter.this.con, CardListActivity.class);
                            viewq.putExtra("card", String.valueOf((MainDataAdapter.this.categoryModelArrayList.get(i)).getId()));
                            viewq.putExtra("cardget", (MainDataAdapter.this.categoryModelArrayList.get(i)).getName());
                            viewq.setFlags(67108864);
                            MainDataAdapter.this.con.startActivity(viewq);
                        }

                        @Override
                        public void onAdShowedFullScreenContent() {
                            mInterstitialAd = null;
                        }
                    });

                } else {
                    Intent viewq = new Intent(MainDataAdapter.this.con, CardListActivity.class);
                    viewq.putExtra("card", String.valueOf((MainDataAdapter.this.categoryModelArrayList.get(i)).getId()));
                    viewq.putExtra("cardget", (MainDataAdapter.this.categoryModelArrayList.get(i)).getName());
                    viewq.setFlags(67108864);
                    MainDataAdapter.this.con.startActivity(viewq);
                }


            }
        });
        myViewHolder.txt_cat.setText((this.categoryModelArrayList.get(i)).getName());*/
    }

    public int getItemCount() {
        return this.categoryModelArrayList.size();
    }

   /* public static Bitmap getImage(byte[] bArr) {
        return BitmapFactory.decodeByteArray(bArr, 0, bArr.length);
    }

    public static int getImageUsingType(int i) {
        switch (i) {
            case 1:
                return R.drawable.icon_aadhar;
            case 2:
                return R.drawable.icon_pan_card;
            case 3:
                return R.drawable.icon_voting_card;
            case 4:
                return R.drawable.icon_driving_lice;
            case 5:
                return R.drawable.icon_passport;
            case 6:
                return R.drawable.icon_bu_card;
            case 7:
                return R.drawable.icon_rc_book;
            case 8:
                return R.drawable.icon_office_id;
            case 9:
                return R.drawable.icon_debit_card;
            case 10:
                return R.drawable.icon_credit_card;
            case 11:
                return R.drawable.icon_transport_card;
            case 12:
                return R.drawable.icon_insurance_card;
            case 13:
                return R.drawable.icon_shopping_card;
            case 14:
                return R.drawable.ic_baseline_how_to_vote_24;
            case 15:
                return R.drawable.icon_other_card;
        }
        return i;
    }*/
}
