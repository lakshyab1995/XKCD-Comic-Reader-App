package com.lakshya.xkcdcomicreader;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.net.MalformedURLException;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class JsonHelperTest {

    @Test
    public void testFetchDataApiWhenEmptyUrl() throws MalformedURLException{
        JsonHelper jsonHelper = new JsonHelper();
        jsonHelper.getDataFromUrl("");
    }
}
