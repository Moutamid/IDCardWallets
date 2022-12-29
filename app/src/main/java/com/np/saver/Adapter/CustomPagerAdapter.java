package com.np.saver.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.viewpager.widget.PagerAdapter;


import java.util.ArrayList;
import com.np.saver.R;

import uk.co.senab.photoview.PhotoViewAttacher;

public class CustomPagerAdapter extends PagerAdapter {
    private ArrayList<String> IMAGES;
    Context context;
    LayoutInflater layoutInflater;

    @SuppressLint("WrongConstant")
    public CustomPagerAdapter(Context context, ArrayList<String> arrayList) {
        this.context = context;
        this.IMAGES = arrayList;
        this.layoutInflater = (LayoutInflater) context.getSystemService("layout_inflater");
    }

    public int getCount() {
        return this.IMAGES.size();
    }

    public boolean isViewFromObject(View view, Object obj) {
        return view == (obj);
    }

    public Object instantiateItem(ViewGroup viewGroup, int i) {
        View inflate = this.layoutInflater.inflate(R.layout.item, viewGroup, false);
        ImageView imageView = inflate.findViewById(R.id.imageView);
        imageView.setImageBitmap(BitmapFactory.decodeFile(this.IMAGES.get(i)));
        new PhotoViewAttacher(imageView).update();
        viewGroup.addView(inflate);
        return inflate;
    }

    public void destroyItem(ViewGroup viewGroup, int i, Object obj) {
        viewGroup.removeView((LinearLayout) obj);
    }
}
