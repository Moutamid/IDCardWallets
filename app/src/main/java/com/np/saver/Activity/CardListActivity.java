package com.np.saver.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.np.saver.Adapter.CardAdapter;
import com.np.saver.Model.CardModel;
import com.np.saver.R;
import com.np.saver.Utils.Adsload;
import com.np.saver.Utils.Constants;
import com.np.saver.Utils.RecyclerItemClick;

import java.util.ArrayList;
import java.util.List;

public class CardListActivity extends AppCompatActivity implements RecyclerItemClick {

    private ImageView ivBack, ivDone;
    private RecyclerView rvList;
    private TextView txtTitle;
    public static int CARD_InSERT_CODE = 2001;
//    String card;
    List<CardModel> cardModelArrayList = new ArrayList<>();
    CardAdapter customAdapter;
//    DatabaseHelper databaseHelper;
//    String title_for_toolbar;
    Adsload adsload;
    FrameLayout frameBanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_list);
        init();

//        cardgetsets = new ArrayList();
//        databaseHelper = new DatabaseHelper(this);
//        card = getIntent().getStringExtra("card");
//        title_for_toolbar = getIntent().getStringExtra("cardget");
        txtTitle.setText(getIntent().getStringExtra(Constants.NAME));
//        Log.e("setupView: ", "" + card);
//        cardgetsets.addAll(databaseHelper.getcard(card));
        setAdapter();
        /*if (!databaseHelper.totalcardisAvailable(card)) {
            Intent intent = new Intent(this, SingleCardActivity.class);
            intent.putExtra("categoryId", card);
            intent.putExtra("categoryName", title_for_toolbar);
            intent.putExtra("isforInsert", true);
            startActivityForResult(intent, CARD_InSERT_CODE);
        }*/

        ivBack.setOnClickListener(view -> finish());

        /*ivDone.setOnClickListener(view -> {
            Intent intent = new Intent(this, SingleCardActivity.class);
            intent.putExtra("categoryId", card);
            intent.putExtra("categoryName", title_for_toolbar);
            intent.putExtra("isforInsert", true);
            intent.setFlags(67108864);
            startActivityForResult(intent, CARD_InSERT_CODE);
        });*/

        ivDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CardListActivity.this, AddCardActivity.class)
                        .putExtra(Constants.IS_EDIT, false)
                        .putExtra(Constants.PARAMS, getIntent().getStringExtra(Constants.PARAMS))
                        .putExtra(Constants.NAME, getIntent().getStringExtra(Constants.NAME)));
            }
        });

    }

    public void init() {
        txtTitle = findViewById(R.id.txtTitle);
        ivBack = findViewById(R.id.ivBack);
        rvList = findViewById(R.id.rvList);
        ivDone = findViewById(R.id.ivDone);
        frameBanner=findViewById(R.id.frameBanner);
        adsload=new Adsload(CardListActivity.this);
        adsload.loadBanner(CardListActivity.this,frameBanner);

    }

    private void setAdapter() {
        Constants.databaseReference().child(Constants.CARDS)
                .child(getIntent().getStringExtra(Constants.PARAMS))
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists()){

                            cardModelArrayList.clear();

                            for (DataSnapshot dataSnapshot:snapshot.getChildren()){

                                cardModelArrayList.add(dataSnapshot.getValue(CardModel.class));

                            }

                            customAdapter = new CardAdapter(CardListActivity.this, cardModelArrayList, CardListActivity.this);
                            rvList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            rvList.setAdapter(customAdapter);

                        }else {
                            startActivity(new Intent(CardListActivity.this, AddCardActivity.class)
                                    .putExtra(Constants.IS_EDIT, false)
                                    .putExtra(Constants.PARAMS, getIntent().getStringExtra(Constants.PARAMS))
                                    .putExtra(Constants.NAME, getIntent().getStringExtra(Constants.NAME)));
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    @SuppressLint("WrongConstant")
    public void onClick(int i, String str) {
        /*Intent intent = new Intent(this, SingleCardActivity.class);
        intent.putExtra("CardId", String.valueOf(i));
        intent.putExtra("categoryId", String.valueOf(str));
        intent.putExtra("isforInsert", false);
        intent.setFlags(67108864);
        startActivityForResult(intent, 1001);*/
    }

   /* protected void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == CARD_InSERT_CODE) {
            notifyDatabase();
        }
        if (i == 1001) {
            notifyDatabase();
        }
    }*/

    void notifyDatabase() {
        /*cardModelArrayList.clear();
        cardModelArrayList.addAll(databaseHelper.getcard(card));
        if (cardModelArrayList.size() == 0) {
            finish();
        } else {
            customAdapter.notifyDataSetChanged();
        }*/
    }

}