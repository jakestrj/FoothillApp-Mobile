package com.novallc.foothillappmobile.activity.YoutubeRecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.youtube.YouTube;
import com.novallc.foothillappmobile.R;
import com.novallc.foothillappmobile.activity.Login_anim;
import com.novallc.foothillappmobile.activity.util.ConnectivityClass;
import com.novallc.foothillappmobile.activity.util.ConnectivityReceiver;
import com.novallc.foothillappmobile.activity.util.WeakRunnable;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Handler;

/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class YouTubeActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener{
    private static final String[] YOUTUBE_PLAYLISTS = {
            "UU97q81Re_4pxmXYuzHNK6LQ",
            "PLDbGjSQKkbg1GcyjSV4sUgNugdCYgnfH6",
            "PLDbGjSQKkbg2x0yoiYbnjg0mg1YcCxLaS",
            "PLDbGjSQKkbg0f4JOLMb5YAsBoFWy-R3cB",
            "PLDbGjSQKkbg1DegWUe5w7HWJmnl8LIUje",
            "PLDbGjSQKkbg2jl6GXMGASvcr7kt0w-Fv4",
            "PLDbGjSQKkbg1SZj_c6ykbLLXq-xQnWQyv"
    };
    private YouTube mYoutubeDataApi;
    public static SwipeRefreshLayout mSwipeLayout; //may result in mem. leak -- TODO
    private android.support.v7.app.AlertDialog alertDialog;
    private Runnable longRunningTask;
    private ExecutorService threadPoolExecutor;
    private Bundle bundlePublic;

    private Future runningTaskFuture;
    private final GsonFactory mJsonFactory = new GsonFactory();
    private final HttpTransport mTransport = AndroidHttp.newCompatibleTransport();


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.youtube_activity);
        getSupportActionBar().setTitle(getString(R.string.title_youtube));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.youtube_swipelayout);
        FrameLayout mFrame = (FrameLayout)findViewById(R.id.container);
        bundlePublic = savedInstanceState;
        threadPoolExecutor = Executors.newSingleThreadExecutor();

        longRunningTask = new Runnable() {@Override public void run() {
            TypedValue typed_value = new TypedValue();
            getTheme().resolveAttribute(android.support.v7.appcompat.R.attr.actionBarSize, typed_value, true);
            mSwipeLayout.setProgressViewOffset(false, 0, getResources().getDimensionPixelSize(typed_value.resourceId));
        }};

        if(!checkConnection()) {
            createConnectivityDialog();
        }else {
            retryConnectivity();
        }
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.you_tube, menu);
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        /*if (id == R.id.action_recyclerview) {
            reloadFragment();
            return true;
        }*/
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }


    @Override
    protected void onDestroy() {
        runningTaskFuture.cancel(true);
        Log.d("acd", "Destroyed... runningTaskFuture(mSwipeLayout)");
        super.onDestroy();
    }

    //Connectivity status method
    private boolean checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        return isConnected;
    }

    @Override
    protected void onResume() {
        ConnectivityClass.getInstance().setConnectivityListener(this);
        super.onResume();
    }

    public void createConnectivityDialog(){
        android.support.v7.app.AlertDialog.Builder mAlertBuilder = new android.support.v7.app.AlertDialog.Builder(new ContextThemeWrapper(this, R.style.dialog_nowindowtitle));
        View mErrorView = getLayoutInflater().inflate(R.layout.dialog_blank, null);
        // set dialog message
        mAlertBuilder
                .setCancelable(false)
                .setView(mErrorView)
                .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setPositiveButton("Retry", null);
        alertDialog = mAlertBuilder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {

                Button b = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        retryConnectivity();
                    }
                });
            }
        });
        alertDialog.show();
    }

    public void retryConnectivity(){
        if(checkConnection()) {
            if(alertDialog!=null)
                alertDialog.dismiss();
            if (ApiKey.YOUTUBE_API_KEY.startsWith("YOUR_API_KEY")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this)
                        .setMessage("Edit ApiKey.java and replace \"YOUR_API_KEY\" with your Applications Browser API Key")
                        .setTitle("Missing API Key")
                        .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();

            } else if (bundlePublic == null) {
                mYoutubeDataApi = new YouTube.Builder(mTransport, mJsonFactory, null)
                        .setApplicationName(getResources().getString(R.string.app_name))
                        .build();

                reloadFragment();
            }
            if (mSwipeLayout != null) {
                mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        if (bundlePublic == null) {
                            mSwipeLayout.setRefreshing(true);
                            if(!checkConnection()) {
                                createConnectivityDialog();
                                mSwipeLayout.setRefreshing(false);
                            }else {
                                reloadFragment();
                                mSwipeLayout.post(longRunningTask);
                            }
                        }
                    }
                });
            }
        }else
            return;
    }

    /**
     * Callback trigger on network change
     */
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        checkConnection();
    }

    @Override
    protected void onStart() {
        Log.d("acd", "Started... runningTaskFuture(mSwipeLayout)");
        runningTaskFuture = threadPoolExecutor.submit(longRunningTask);
        super.onStart();
    }

    private void reloadFragment(){
        try{
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, YouTubeRecyclerViewFragment.newInstance(mYoutubeDataApi, YOUTUBE_PLAYLISTS))
                    .commit();
        }catch (Exception e){e.printStackTrace();}
    }
}
