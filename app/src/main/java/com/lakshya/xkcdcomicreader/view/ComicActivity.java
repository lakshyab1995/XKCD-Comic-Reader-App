package com.lakshya.xkcdcomicreader.view;

import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adsnative.ads.ANAdListener;
import com.adsnative.ads.ANAdViewBinder;
import com.adsnative.ads.ANNativeAd;
import com.adsnative.ads.NativeAdUnit;
import com.bumptech.glide.Glide;
import com.lakshya.xkcdcomicreader.ComicModel;
import com.lakshya.xkcdcomicreader.R;

public class ComicActivity extends AppCompatActivity implements View.OnClickListener, ANAdListener{

    private static final String TAG = ComicActivity.class.getSimpleName();
    private static final String AD_UNIT_ID = "2Pwo1otj1C5T8y6Uuz9v-xbY1aB09x8rWKvsJ-HI";
    private ImageView mComicImg;
    private TextView mComicTitle;
    private TextView mComicDate;
    private ComicModel mComicModel;
    private Toast mToast;
    private ANNativeAd mANNativeAd;
    private ANAdViewBinder mANAdViewBinder;
    private ViewGroup mLayoutContainer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comic);
        mComicImg = findViewById(R.id.comicImg);
        mComicTitle = findViewById(R.id.comicText);
        mComicDate = findViewById(R.id.comicDate);
        mLayoutContainer = findViewById(R.id.layout_container);
        mComicModel = (ComicModel) getIntent().getSerializableExtra("ComicObject");
        boolean isAdTriggered = getIntent().getBooleanExtra("isAdTriggered", false);
        Glide.with(this)
                .load(mComicModel.getComicImageUrl())
                .into(mComicImg);
        mComicTitle.setText(mComicModel.getComicTitle());
        mComicDate.setText(mComicModel.getDate());
        mComicImg.setOnClickListener(this);
        mToast = Toast.makeText(this, mComicModel.getImgAltText(), Toast.LENGTH_LONG);
        if(isAdTriggered){
            mANNativeAd = new ANNativeAd(this, AD_UNIT_ID);
            mANNativeAd.setNativeAdListener(this);
            mANAdViewBinder = new ANAdViewBinder.Builder(R.layout.list_native_ad_layout)
                    .bindTitle(R.id.ad_title)
                    .bindIconImage(R.id.ad_icon)
                    .bindCallToAction(R.id.ad_cta)
                    .bindPromotedBy(R.id.ad_promoted_by)
                    .build();
            mANNativeAd.registerViewBinder(mANAdViewBinder);
        }
    }

    @Override
    public void onClick(View view) {
        if(mToast != null){
            mToast.show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mANNativeAd != null){
            mANNativeAd.loadAd();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Destroy Called");
        if(mToast != null){
            mToast.cancel();
        }
    }

    @Override
    public void onAdLoaded(NativeAdUnit nativeAdUnit) {
        mLayoutContainer.removeAllViews();
        View nativeAdView = mANNativeAd.renderAdView(nativeAdUnit);
        mLayoutContainer.setVisibility(View.VISIBLE);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        nativeAdView.setLayoutParams(layoutParams);
        mLayoutContainer.addView(nativeAdView);
    }


    @Override
    public void onAdFailed(String s) {
        mLayoutContainer.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onAdImpressionRecorded() {

    }

    @Override
    public boolean onAdClicked(NativeAdUnit nativeAdUnit) {
        return false;
    }
}
