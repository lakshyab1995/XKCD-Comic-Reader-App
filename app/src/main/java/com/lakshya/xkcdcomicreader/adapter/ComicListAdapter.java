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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lakshya.xkcdcomicreader.ComicModel;
import com.lakshya.xkcdcomicreader.R;
import com.lakshya.xkcdcomicreader.view.ComicActivity;

import java.util.List;

public class ComicListAdapter extends RecyclerView.Adapter<ComicListAdapter.ViewHolder> {

    private static final String TAG = ComicListAdapter.class.getSimpleName();
    private List<ComicModel> mComicModels;
    private Context mContext;

    public ComicListAdapter(Context context, List<ComicModel> comicModels){
        mContext = context;
        mComicModels = comicModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view  = layoutInflater.inflate(R.layout.row_item_list, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        ComicModel comicModel = mComicModels.get(position);
        Glide.with(mContext)
                .load(comicModel.getComicImageUrl())
                .into(viewHolder.mComicImg);
        viewHolder.mComicTitle.setText(comicModel.getComicTitle());
        viewHolder.mParentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,"Comic List Item Clicked");
                Intent intent = new Intent(mContext, ComicActivity.class);
                intent.putExtra("ComicObject", mComicModels.get(0));
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mComicModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView mComicImg;
        private TextView mComicTitle;
        private ViewGroup mParentView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mComicImg = itemView.findViewById(R.id.comicIcon);
            mComicTitle = itemView.findViewById(R.id.comicTitle);
            mParentView = itemView.findViewById(R.id.comicItemList);
        }
    }
}
