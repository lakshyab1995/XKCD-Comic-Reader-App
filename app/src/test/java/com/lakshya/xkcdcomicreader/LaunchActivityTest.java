package com.lakshya.xkcdcomicreader;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.lakshya.xkcdcomicreader.view.ComicListFragment;
import com.lakshya.xkcdcomicreader.view.MainActivity;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class LaunchActivityTest {

    private static MainActivity mMainActivity;

    @Before
    public void setUp() throws Exception {
        mMainActivity = Robolectric.buildActivity(MainActivity.class)
                .create()
                .resume()
                .get();
    }

    @Test
    public void shouldNotBeNull() throws Exception
    {
        Assert.assertNotNull( mMainActivity );
    }

    @Test
    public void shouldHaveListFragment() throws Exception{
        ComicListFragment comicListFragment = Mockito.mock(ComicListFragment.class);
        startFragment(comicListFragment);
        Assert.assertNotNull(comicListFragment);
    }

    public static void startFragment(Fragment fragment){
        FragmentManager fragmentManager = mMainActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add( fragment, null );
        fragmentTransaction.commit();
    }
}
