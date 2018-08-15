package com.lakshya.xkcdcomicreader.view;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Toast;

import com.adsnative.ads.ANAdPositions;
import com.adsnative.ads.ANAdViewBinder;
import com.adsnative.ads.ANRecyclerAdapter;
import com.lakshya.xkcdcomicreader.ComicModel;
import com.lakshya.xkcdcomicreader.FetchComicsTask;
import com.lakshya.xkcdcomicreader.NetworkUtils;
import com.lakshya.xkcdcomicreader.R;
import com.lakshya.xkcdcomicreader.adapter.ComicGridAdapter;

import java.util.ArrayList;
import java.util.List;

public class ComicGridFragment extends Fragment {

    private static final String TAG = ComicGridFragment.class.getSimpleName();
    private static final String COMIC_GET_URL = "https://xkcd.com/info.0.json";
    private static final String AD_UNIT_ID = "2Pwo1otj1C5T8y6Uuz9v-xbY1aB09x8rWKvsJ-HI";
    private RecyclerView mComicGrid;
    private GridLayoutManager mGridLayoutManager;
    private boolean isScrolling;
    private int currentItems, totalItems, scrollOutItems;
    private ComicModel mComicModel;
    private List<ComicModel> mComicModelList = new ArrayList<>();
    private ComicGridAdapter mComicGridAdapter;
    private ANRecyclerAdapter mANRecyclerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_comic, container, false);
        mComicGrid = rootView.findViewById(R.id.comicList);
        mGridLayoutManager = new GridLayoutManager(getContext(),2);
        mComicGrid.setHasFixedSize(true);
        mComicGrid.setLayoutManager(mGridLayoutManager);
        mComicGrid.setItemAnimator(new DefaultItemAnimator());
        //mComicGrid.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.HORIZONTAL));
        mComicGrid.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItems = mGridLayoutManager.getChildCount();
                totalItems = mGridLayoutManager.getItemCount();
                scrollOutItems = mGridLayoutManager.findFirstVisibleItemPosition();
                if(isScrolling && (currentItems + scrollOutItems == totalItems)){
                    isScrolling = false;
                    if(NetworkUtils.isNetworkAvailable(getContext())){
                        fetchData();
                    }
                    else {
                        Toast.makeText(getContext(), NetworkUtils.ERROR_STATUS, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        //Fetching Comics
        if(NetworkUtils.isNetworkAvailable(getContext())){
            new FetchComicsGrid().execute(COMIC_GET_URL);
        }
        else {
            Toast.makeText(getContext(), NetworkUtils.ERROR_STATUS, Toast.LENGTH_SHORT).show();
        }
        Log.d(TAG,"Grid Fragment created");
        return rootView;
    }

    private void fetchData() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                if(mComicModel != null){
                    for(int i = 0; i < 10; i++){
                        mComicModelList.add(mComicModel);
                    }
                    mComicGridAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    public class FetchComicsGrid extends FetchComicsTask{

        @Override
        protected void onPostExecute(ComicModel comicModel) {
            super.onPostExecute(comicModel);
            mComicModel = comicModel;
            for(int i = 0; i < 10; i++){
                mComicModelList.add(mComicModel);
            }

            final ANAdViewBinder anAdViewBinder = new ANAdViewBinder.Builder(R.layout.grid_native_ad_layout)
                    .bindTitle(R.id.ad_title)
                    .bindIconImage(R.id.ad_icon)
                    .bindPromotedBy(R.id.ad_promoted_by)
                    .build();
            ANAdPositions.ClientPositions clientPositions = ANAdPositions.clientPositioning();
            // add fixed positions
            clientPositions.addFixedPosition(5).addFixedPosition(10);
            // add repeating position interval
            clientPositions.enableRepeatingPositions(8);

            mComicGridAdapter = new ComicGridAdapter(getContext(), mComicModelList);
            mANRecyclerAdapter = new ANRecyclerAdapter(getContext(), mComicGridAdapter, AD_UNIT_ID, clientPositions);
            // Register the renderer with the ANRecyclerAdapter
            mANRecyclerAdapter.registerViewBinder(anAdViewBinder);

            mComicGrid.setAdapter(mANRecyclerAdapter);
            // Start loading ads
            mANRecyclerAdapter.loadAds();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mANRecyclerAdapter != null){
            mANRecyclerAdapter.loadAds();
        }
    }
}
