package com.np.saver.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fxn.stash.Stash;
import com.np.saver.Activity.SingleCardActivity;
import com.np.saver.Activity.ViewCardsActivity;
import com.np.saver.Model.CardModel;
import com.np.saver.R;

import androidx.recyclerview.widget.RecyclerView;

import com.np.saver.Activity.CardListActivity;
import com.np.saver.Model.Cardgetset;
import com.np.saver.Utils.Constants;
import com.np.saver.Utils.RecyclerItemClick;
import com.np.saver.database.DatabaseHelper;

import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {
    private List<CardModel> cardgetsetList;
    private Activity context;
//    DatabaseHelper db;
    private RecyclerItemClick itemClick;
    int countvideo;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public View layout;
        public TextView number;
        public TextView txtHeader;

        public ViewHolder(View view) {
            super(view);
            this.layout = view;
            this.txtHeader = (TextView) view.findViewById(R.id.txtTitle);
            this.number = (TextView) view.findViewById(R.id.number);
            /*view.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
//                    itemClick.onClick((cardgetsetList.get(ViewHolder.this.getAdapterPosition())).getId(), ((Cardgetset) cardgetsetList.get(ViewHolder.this.getAdapterPosition())).getCard());
                }
            });*/
        }
    }

    public CardAdapter(Activity context, List<CardModel> list, CardListActivity recyclerItemClick) {
        this.cardgetsetList = list;
        this.context = context;
        this.itemClick = recyclerItemClick;
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_card, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
//        this.db = new DatabaseHelper(this.context);
        /*Cardgetset cardgetset = (Cardgetset) this.cardgetsetList.get(i);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("onBindViewHolder: ");
        stringBuilder.append(cardgetset.getName());
        Log.i("kkk1", stringBuilder.toString());
        viewHolder.txtHeader.setText(cardgetset.getName());*/

        CardModel cardModel = cardgetsetList.get(i);

        viewHolder.txtHeader.setText(cardModel.name);

        viewHolder.number.setText(String.valueOf(i + 1));

        viewHolder.layout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Stash.put(Constants.CURRENT_CARD, cardModel);
                context.startActivity(new Intent(context, ViewCardsActivity.class)
                        .putExtra(Constants.PARAMS, context.getIntent().getStringExtra(Constants.PARAMS)));
            }
        });
    }

    public int getItemCount() {
        /*StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("getItemCount: ");
        stringBuilder.append(this.cardgetsetList.size());
        Log.i("kkkk", stringBuilder.toString());*/
        return this.cardgetsetList.size();
    }
}
