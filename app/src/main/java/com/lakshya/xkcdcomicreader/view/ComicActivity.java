package com.lakshya.xkcdcomicreader.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lakshya.xkcdcomicreader.ComicModel;
import com.lakshya.xkcdcomicreader.R;

public class ComicActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = ComicActivity.class.getSimpleName();
    private ImageView mComicImg;
    private TextView mComicTitle;
    private TextView mComicDate;
    private ComicModel mComicModel;
    private Toast mToast;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comic);
        mComicImg = findViewById(R.id.comicImg);
        mComicTitle = findViewById(R.id.comicText);
        mComicDate = findViewById(R.id.comicDate);
        mComicModel = (ComicModel) getIntent().getSerializableExtra("ComicObject");
        Glide.with(this)
                .load(mComicModel.getComicImageUrl())
                .into(mComicImg);
        mComicTitle.setText(mComicModel.getComicTitle());
        mComicDate.setText(mComicModel.getDate());
        mComicImg.setOnClickListener(this);
        mToast = Toast.makeText(this, mComicModel.getImgAltText(), Toast.LENGTH_LONG);
    }

    @Override
    public void onClick(View view) {
        if(mToast != null){
            mToast.show();
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
}
