package com.np.saver.Adapter;

import static com.np.saver.Activity.MainActivity.selectedImage;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.Visibility;

import com.fxn.stash.Stash;
import com.np.saver.Model.CatImage;
import com.np.saver.R;
import com.np.saver.Utils.Constants;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    Context context;
    List<CatImage> catImageList;

    public CustomAdapter(Context context, List<CatImage> catImageList) {
        this.context = context;
        this.catImageList = catImageList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item, parent, false);
        return new ViewHolder(view);
    }

//    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        holder.imageView1.setImageResource(getImageUsingType(this.catImageList.get(position).getImageType()));
        holder.imageView1.setImageResource(this.catImageList.get(position).getImageType());
        holder.imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int image=catImageList.get(position).getImageType();
                Log.d("image", String.valueOf(catImageList.get(position).getImageType()));
                Log.d("image", String.valueOf(catImageList.get(position).getImageType()));
                selectedImage.setImageResource(image);

                Stash.put(Constants.CURRENT_POSITION, position);

            }
        });

    }

    @Override
    public int getItemCount() {
        return catImageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView1, imageView2, imageView3;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView1=itemView.findViewById(R.id.imageView1);
        }
    }
}

