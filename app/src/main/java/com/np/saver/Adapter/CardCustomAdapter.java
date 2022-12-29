package com.np.saver.Adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.np.saver.Activity.CardCustomListActivity;
import com.np.saver.Model.Cardgetset;
import com.np.saver.Utils.RecyclerItemClick;
import com.np.saver.database.DatabaseHelper;
import com.np.saver.R;

import java.util.List;

public class CardCustomAdapter extends RecyclerView.Adapter<CardCustomAdapter.ViewHolder> {
    private List<Cardgetset> cardgetsetList;
    private Activity context;
    DatabaseHelper db;
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
            view.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    itemClick.onClick((cardgetsetList.get(ViewHolder.this.getAdapterPosition())).getId(), ((Cardgetset) cardgetsetList.get(ViewHolder.this.getAdapterPosition())).getCard());
                }
            });
        }
    }

    public CardCustomAdapter(Activity context, List<Cardgetset> list, CardCustomListActivity recyclerItemClick) {
        this.cardgetsetList = list;
        this.context = context;
        this.itemClick = recyclerItemClick;
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_card, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        this.db = new DatabaseHelper(this.context);
        Cardgetset cardgetset = (Cardgetset) this.cardgetsetList.get(i);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("onBindViewHolder: ");
        stringBuilder.append(cardgetset.getName());
        Log.i("kkk1", stringBuilder.toString());
        viewHolder.txtHeader.setText(cardgetset.getName());
        viewHolder.number.setText(String.valueOf(i + 1));
    }

    public int getItemCount() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("getItemCount: ");
        stringBuilder.append(this.cardgetsetList.size());
        Log.i("kkkk", stringBuilder.toString());
        return this.cardgetsetList.size();
    }
}
