package com.np.saver.Utils;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import com.np.saver.R;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;

public class Adsload {

    Activity activity;
    public Adsload(Activity activity)
    {
        this.activity=activity;
    }
    public void loadBanner(Activity activity, FrameLayout frameLayout)
    {

        if (frameLayout != null) {
            frameLayout.setVisibility(View.VISIBLE);
            AdView adView = new AdView(activity);
            adView.setAdUnitId(activity.getResources().getString(R.string.banner_ID));

            frameLayout.addView(adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            AdSize adSize = getAdSize(activity);
            adView.setAdSize(adSize);
            adView.loadAd(adRequest);

            adView.setAdListener(new AdListener() {
                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    super.onAdFailedToLoad(loadAdError);

                }
            });
        }
    }
    public void loadBanner1(Activity activity, FrameLayout frameLayout)
    {

        if (frameLayout != null) {
            frameLayout.setVisibility(View.VISIBLE);
            AdView adView = new AdView(activity);
            adView.setAdUnitId(activity.getResources().getString(R.string.banner_ID));

            frameLayout.addView(adView);
            AdRequest adRequest = new AdRequest.Builder().build();
//            AdSize adSize = getAdSize(activity);
            adView.setAdSize(AdSize.LARGE_BANNER);
            adView.loadAd(adRequest);

            adView.setAdListener(new AdListener() {
                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    super.onAdFailedToLoad(loadAdError);

                }
            });
        }
    }
    private AdSize getAdSize(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;
        int adWidth = (int) (widthPixels / density);
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, adWidth);
    }

}
