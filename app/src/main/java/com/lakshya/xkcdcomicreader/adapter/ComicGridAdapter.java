package com.lakshya.xkcdcomicreader.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.lakshya.xkcdcomicreader.ComicModel;
import com.lakshya.xkcdcomicreader.R;
import com.lakshya.xkcdcomicreader.view.ComicActivity;
import com.lakshya.xkcdcomicreader.view.ComicGridFragment;

import java.util.List;

public class ComicGridAdapter extends RecyclerView.Adapter<ComicGridAdapter.ViewHolder> {

    private static final String TAG = ComicGridFragment.class.getSimpleName();
    private List<ComicModel> mComicModels;
    private Context mContext;
    private int mCount;
    private boolean mIsAdTrigger = false;

    public ComicGridAdapter(Context context, List<ComicModel> comicModels){
        mContext = context;
        mComicModels = comicModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.row_item_grid, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        ComicModel comicModel = mComicModels.get(position);
        Glide.with(mContext)
                .load(comicModel.getComicImageUrl())
                .into(viewHolder.mComicIcon);
        viewHolder.mComicIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,"Comic Grid Item Clicked");
                mCount++;
                if(mCount % 5 == 0){
                    mIsAdTrigger = true;
                }
                else {
                    mIsAdTrigger = false;
                }
                Intent intent = new Intent(mContext, ComicActivity.class);
                intent.putExtra("ComicObject", mComicModels.get(0));
                intent.putExtra("isAdTriggered", mIsAdTrigger);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mComicModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView mComicIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mComicIcon = itemView.findViewById(R.id.comicGridIcon);
        }
    }
}
