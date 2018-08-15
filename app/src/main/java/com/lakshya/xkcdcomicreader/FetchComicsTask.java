package com.lakshya.xkcdcomicreader;

import android.os.AsyncTask;

import java.util.List;

public abstract class FetchComicsTask extends AsyncTask<String, Void, ComicModel>{

    @Override
    protected ComicModel doInBackground(String... params) {
        JsonHelper jsonHelper = new JsonHelper();
        return jsonHelper.getDataFromUrl(params[0]);
    }
}
