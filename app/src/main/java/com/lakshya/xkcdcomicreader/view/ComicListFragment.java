package com.lakshya.xkcdcomicreader.view;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
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
import com.lakshya.xkcdcomicreader.NetworkUtils;
import com.lakshya.xkcdcomicreader.adapter.ComicListAdapter;
import com.lakshya.xkcdcomicreader.ComicModel;
import com.lakshya.xkcdcomicreader.FetchComicsTask;
import com.lakshya.xkcdcomicreader.R;

import java.util.ArrayList;
import java.util.List;

public class ComicListFragment extends Fragment{

    private static final String TAG = ComicListFragment.class.getSimpleName();
    private static final String COMIC_GET_URL = "https://xkcd.com/info.0.json";
    private static final String AD_UNIT_ID = "2Pwo1otj1C5T8y6Uuz9v-xbY1aB09x8rWKvsJ-HI";
    private List<ComicModel> mComicModelList = new ArrayList<>();
    private RecyclerView mComicList;
    private ComicListAdapter mComicListAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private boolean isScrolling = false;
    private int currentItems, totalItems, scrollOutItems;
    private ComicModel mComicModel;
    private ANRecyclerAdapter mANRecyclerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_comic, container, false);
        mComicList = rootView.findViewById(R.id.comicList);
        mComicList.setHasFixedSize(true);
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        mComicList.setLayoutManager(mLinearLayoutManager);
        mComicList.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        mComicList.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                currentItems = mLinearLayoutManager.getChildCount();
                totalItems = mLinearLayoutManager.getItemCount();
                scrollOutItems = mLinearLayoutManager.findFirstVisibleItemPosition();
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
            new FetchComicsList().execute(COMIC_GET_URL);
        }
        else {
            Toast.makeText(getContext(), NetworkUtils.ERROR_STATUS, Toast.LENGTH_SHORT).show();
        }
        Log.d(TAG,"List Fragment created");
        return rootView;
    }

    /* Since API is giving same data when making request several times, so using same data, not making another
       request in order to save API load
    */
    private void fetchData(){
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                if(mComicModel != null){
                    for(int i = 0; i < 10; i++){
                        mComicModelList.add(mComicModel);
                    }
                    mComicListAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    public class FetchComicsList extends FetchComicsTask {

        @Override
        protected void onPostExecute(ComicModel comicModel) {
            super.onPostExecute(comicModel);
            mComicModel = comicModel;
            for(int i=0; i<10; i++){
                mComicModelList.add(mComicModel);
            }
            final ANAdViewBinder anAdViewBinder = new ANAdViewBinder.Builder(R.layout.list_native_ad_layout)
                    .bindTitle(R.id.ad_title)
                    .bindIconImage(R.id.ad_icon)
                    .bindCallToAction(R.id.ad_cta)
                    .bindPromotedBy(R.id.ad_promoted_by)
                    .build();
            ANAdPositions.ClientPositions clientPositions = ANAdPositions.clientPositioning();
            // add fixed positions
            clientPositions.addFixedPosition(5).addFixedPosition(10);
            // add repeating position interval
            clientPositions.enableRepeatingPositions(8);
            mComicListAdapter = new ComicListAdapter(getContext(), mComicModelList);
            mANRecyclerAdapter = new ANRecyclerAdapter(getContext(), mComicListAdapter, AD_UNIT_ID, clientPositions);
            // Register the renderer with the ANRecyclerAdapter
            mANRecyclerAdapter.registerViewBinder(anAdViewBinder);
            mComicList.setAdapter(mANRecyclerAdapter);
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
