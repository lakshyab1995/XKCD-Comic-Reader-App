package com.lakshya.xkcdcomicreader;

import java.io.Serializable;

public class ComicModel implements Serializable{

    private String mComicImageUrl;
    private String mComicTitle;
    private String mDay;
    private String mMonth;
    private String mYear;
    private String mImgAltText;

    public String getComicImageUrl() {
        return mComicImageUrl;
    }

    public void setComicImageUrl(String comicImageUrl) {
        mComicImageUrl = comicImageUrl;
    }

    public String getComicTitle() {
        return mComicTitle;
    }

    public void setComicTitle(String comicTitle) {
        mComicTitle = comicTitle;
    }

    public void setDate(String day, String month, String year){
        mDay = day;
        mMonth = month;
        mYear = year;
    }

    public String getDate(){
        return mDay + "/" + mMonth + "/" + mYear;
    }


    public String getImgAltText() {
        return mImgAltText;
    }

    public void setImgAltText(String imgAltText) {
        mImgAltText = imgAltText;
    }
}
