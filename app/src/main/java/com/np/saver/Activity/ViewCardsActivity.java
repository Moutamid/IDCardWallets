package com.np.saver.Activity;

import static com.bumptech.glide.Glide.with;
import static com.bumptech.glide.load.engine.DiskCacheStrategy.DATA;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.fxn.stash.Stash;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.np.saver.Model.CardModel;
import com.np.saver.R;
import com.np.saver.Utils.Constants;
import com.np.saver.databinding.ActivityViewCardsBinding;

public class ViewCardsActivity extends AppCompatActivity {

    private ActivityViewCardsBinding b;
    CardModel cardModel = (CardModel) Stash.getObject(Constants.CURRENT_CARD, CardModel.class);
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityViewCardsBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");

        b.txtTitle.setText(cardModel.name);

        with(getApplicationContext())
                .asBitmap()
                .load(cardModel.img_back)
                /*.apply(new RequestOptions()
                        .placeholder(R.color.lighterGrey)
                        .error(R.color.lighterGrey)
                )*/
                .diskCacheStrategy(DATA)
                .into(b.imgBack);

        with(getApplicationContext())
                .asBitmap()
                .load(cardModel.img_front)
                /*.apply(new RequestOptions()
                        .placeholder(R.color.lighterGrey)
                        .error(R.color.lighterGrey)
                )*/
                .diskCacheStrategy(DATA)
                .into(b.imgFront);

        b.imgBack.setOnClickListener(v -> {
            startActivity(new Intent(ViewCardsActivity.this, ViewImageActivity.class)
                    .putExtra(Constants.IMG_CLICKED, 1));
        });


        b.imgFront.setOnClickListener(v -> {
            startActivity(new Intent(ViewCardsActivity.this, ViewImageActivity.class)
                    .putExtra(Constants.IMG_CLICKED, 0));
        });

        b.lDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View view = LayoutInflater.from(ViewCardsActivity.this).inflate(R.layout.bottomsheet_delete_dialog, null);
                BottomSheetDialog dialogDeleteImage = new BottomSheetDialog(ViewCardsActivity.this, R.style.BottomSheetDialogThemeNew);
                dialogDeleteImage.setContentView(view);
                dialogDeleteImage.setCanceledOnTouchOutside(false);
                dialogDeleteImage.setDismissWithAnimation(true);

                LinearLayout ivCloseDelete = dialogDeleteImage.findViewById(R.id.ivCloseDelete);
                LinearLayout ivDoneDelete = dialogDeleteImage.findViewById(R.id.ivDoneDelete);

                ivDoneDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        progressDialog.show();
                        Constants.databaseReference().child(Constants.CATEGORIES)
                                .child(getIntent().getStringExtra(Constants.PARAMS))
                                .child("total_count")
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        Log.d("TAG", "onDataChange: ");
//                                    if (snapshot.exists()){

                                        int totalCount = snapshot.getValue(Integer.class);
                                        totalCount--;
                                        Constants.databaseReference().child(Constants.CATEGORIES)
                                                .child(getIntent().getStringExtra(Constants.PARAMS))
                                                .child("total_count")
                                                .setValue(totalCount);

                                        Constants.databaseReference().child(Constants.CARDS)
                                                .child(getIntent().getStringExtra(Constants.PARAMS))
                                                .child(cardModel.push_key)
                                                .removeValue();
                                        progressDialog.dismiss();

                                        Toast.makeText(ViewCardsActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                        finish();
//                                    }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        progressDialog.dismiss();
                                        Toast.makeText(ViewCardsActivity.this, error.toException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });

                ivCloseDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogDeleteImage.dismiss();
                    }
                });
                dialogDeleteImage.show();

            }
        });

        b.lEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ViewCardsActivity.this, AddCardActivity.class)
                        .putExtra(Constants.IS_EDIT, true)
                        .putExtra(Constants.PARAMS, getIntent().getStringExtra(Constants.PARAMS))
                        .putExtra(Constants.NAME, cardModel.name));
            }
        });

        b.ivBack.setOnClickListener(v -> {
            finish();
        });

    }
}