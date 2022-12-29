package com.np.saver.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.np.saver.Adapter.CardCustomAdapter;
import com.np.saver.Model.Cardgetset;
import com.np.saver.R;
import com.np.saver.Utils.Adsload;
import com.np.saver.Utils.RecyclerItemClick;
import com.np.saver.database.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class CardCustomListActivity extends AppCompatActivity implements RecyclerItemClick {

    private ImageView ivBack, ivDone,ivDelete;
    private RecyclerView rvList;
    private TextView txtTitle;
    public static int CARD_InSERT_CODE = 2001;
    String card;
    List<Cardgetset> cardgetsets = new ArrayList<>();
    CardCustomAdapter customAdapter;
    DatabaseHelper databaseHelper;
    String title_for_toolbar;
    Adsload adsload;
    FrameLayout frameBanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customcard_list);
        init();

        cardgetsets = new ArrayList();
        databaseHelper = new DatabaseHelper(this);
        card = getIntent().getStringExtra("card");
        title_for_toolbar = getIntent().getStringExtra("cardget");
        txtTitle.setText(title_for_toolbar);
        Log.e("setupView: ", "" + card);

        cardgetsets.addAll(databaseHelper.getcard1(card));
        setAdapter();
        if (!databaseHelper.totalcardisAvailable1(card)) {
            Intent intent = new Intent(this, SingleCustomCardActivity.class);
            intent.putExtra("categoryId", card);
            intent.putExtra("categoryName", title_for_toolbar);
            intent.putExtra("isforInsert", true);
            startActivityForResult(intent, CARD_InSERT_CODE);
        }

        ivBack.setOnClickListener(view -> finish());

        ivDone.setOnClickListener(view -> {
            Intent intent = new Intent(this, SingleCustomCardActivity.class);
            intent.putExtra("categoryId", card);
            intent.putExtra("categoryName", title_for_toolbar);
            intent.putExtra("isforInsert", true);
            intent.setFlags(67108864);
            startActivityForResult(intent, CARD_InSERT_CODE);
        });
        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseHelper.deletecategory(Integer.parseInt(getIntent().getStringExtra("card")));
                Toast.makeText(CardCustomListActivity.this, "Deleted Category", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(CardCustomListActivity.this, MainActivity.class));
               setResult(1001);
               finish();
            }
        });

    }

    public void init() {
        txtTitle = findViewById(R.id.txtTitle);
        ivBack = findViewById(R.id.ivBack);
        rvList = findViewById(R.id.rvList);
        ivDone = findViewById(R.id.ivDone);
        ivDelete = findViewById(R.id.ivDelete);
        frameBanner=findViewById(R.id.frameBanner);
        adsload=new Adsload(CardCustomListActivity.this);
        adsload.loadBanner(CardCustomListActivity.this,frameBanner);

    }

    private void setAdapter() {
        customAdapter = new CardCustomAdapter(this, cardgetsets, this);
        rvList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rvList.setAdapter(customAdapter);
    }

    @SuppressLint("WrongConstant")
    public void onClick(int i, String str) {
        Intent intent = new Intent(this, SingleCustomCardActivity.class);
        intent.putExtra("CardId", String.valueOf(i));
        intent.putExtra("categoryId", String.valueOf(str));
        intent.putExtra("isforInsert", false);
        intent.setFlags(67108864);
        startActivityForResult(intent, 1001);
    }

    protected void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == CARD_InSERT_CODE) {
            notifyDatabase();
        }
        if (i == 1001) {
            notifyDatabase();
        }
    }

    void notifyDatabase() {
        cardgetsets.clear();
        cardgetsets.addAll(databaseHelper.getcard1(card));
        if (cardgetsets.size() == 0) {
            finish();
        } else {
            customAdapter.notifyDataSetChanged();
        }
    }

}