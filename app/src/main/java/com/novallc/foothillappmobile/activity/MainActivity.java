package com.novallc.foothillappmobile.activity;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

//import com.mikepenz.fontawesome_typeface_library.FontAwesome;
//import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.crossfadedrawerlayout.view.CrossfadeDrawerLayout;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.MiniDrawer;
import com.mikepenz.materialdrawer.interfaces.ICrossfader;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;
import com.mikepenz.materialdrawer.util.DrawerUIUtils;
import com.mikepenz.materialize.util.UIUtils;
import com.novallc.foothillappmobile.R;
import com.novallc.foothillappmobile.activity.YoutubeRecyclerView.YouTubeActivity;
import com.novallc.foothillappmobile.circularprogress.ArcProgress;
//import com.novallc.foothillappmobile.fragments.StickyListExpandableFragment;
import com.novallc.foothillappmobile.fragments.AboutUsFragment;
import com.novallc.foothillappmobile.fragments.BitmapMapStndView;
import com.novallc.foothillappmobile.fragments.OverviewFragment;
import com.novallc.foothillappmobile.fragments.PrivacyPolicy;
import com.novallc.foothillappmobile.fragments.StickyListExpandableFragment;
import com.novallc.foothillappmobile.fragments.ZFragment;

public class MainActivity extends AppCompatActivity {
    public static FragmentManager mFragmentManager;
    public static FragmentTransaction mFragmentTransaction;
    public static Toolbar toolbar;
    //save our header or result
    private AccountHeader headerResult = null;
    public static Drawer result = null;
    private Fragment fragment;
    private MiniDrawer miniResult = null;
    public static CrossfadeDrawerLayout crossfadeDrawerLayout = null;
    public static ActionBarDrawerToggle drawerToggle;
    private Intent headerUpdateIntent;
    private Intent notifUpdateIntent;
    private Intent listviewIntent;
    private ImageButton bButtonDrawer;
    private ImageButton bButtonHelp;
    private View mDrawerHeader;
    private NotificationCompat.Builder mNotifBuilder;
    private NotificationManager mNotifyMgr;
    public static long drawer_currentPos;
    BroadcastReceiver mBroadcasterReceiver;
    private boolean mExecuted;
    SharedPreferences mPrefs;

    private TextView periodIndicatorContent; private String periodIndicatorContent_display;
    private TextView scheduleIndicatorContent; private String scheduleIndicatorContent_display;
    private ArcProgress arcProgressView; private View progressServiceStart;

    private int _1; private float _2;
    String _str0;
    SpannableString mSpannable0;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_toolbar);
        //Kahuna.getInstance().onAppCreate(this, "APIKEY", "SENDERID");

        //Remove line to test RTL support
        //getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        // Handle Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Create the AccountHeader
        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withTranslucentStatusBar(true)
                .withSelectionListEnabled(false)
                //.withAccountHeader(R.layout.arc_progress)
//                .addProfiles(
//                        profile, profile2
//                )
                .withSavedInstance(savedInstanceState)
                .build();


        View mHeader = (View)getLayoutInflater().inflate(R.layout.arc_progress,null);
        //Create the drawer
        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withDrawerLayout(R.layout.crossfade_material_drawer)
                .withHasStableIds(true)
                .withDrawerWidthDp(72)
                //Add header attr with arc progress
                .withHeader(mHeader)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.drawer_item_first).withIcon(R.drawable.ic_action_ic_home_select).withSelectedIconColor(ContextCompat.getColor(getApplicationContext(), R.color.colorStatusBar)).withIconTintingEnabled(true).withIdentifier(0),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_second).withIcon(R.drawable.ic_leftmenu_gradebook).withSelectedIconColor(ContextCompat.getColor(getApplicationContext(), R.color.colorStatusBar)).withIconTintingEnabled(true).withIdentifier(1),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_third).withIcon(R.drawable.ic_grades_drawer).withSelectedIconColor(ContextCompat.getColor(getApplicationContext(), R.color.colorStatusBar)).withIconTintingEnabled(true).withIdentifier(2),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_fourth).withIcon(R.drawable.ic_map_marker).withSelectedIconColor(ContextCompat.getColor(getApplicationContext(), R.color.colorStatusBar)).withIconTintingEnabled(true).withIdentifier(3),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_fifth).withIcon(R.drawable.ic_video_playlist).withSelectedIconColor(ContextCompat.getColor(getApplicationContext(), R.color.colorStatusBar)).withIconTintingEnabled(true).withIdentifier(4),
                        //new PrimaryDrawerItem().withName(R.string.drawer_item_fifth).withSelectedIconColor(ContextCompat.getColor(getApplicationContext(), R.color.colorStatusBar)).withIconTintingEnabled(true).withIcon(R.drawable.ic_layers).withIdentifier(4),
                        //new PrimaryDrawerItem().withName(R.string.drawer_item_sixth).withIcon(R.drawable.ic_layers).withSelectedIconColor(ContextCompat.getColor(getApplicationContext(), R.color.colorStatusBar)).withIconTintingEnabled(true).withIdentifier(5),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_sixth).withIconTintingEnabled(true).withIcon(R.drawable.ic_layers).withSelectedIconColor(ContextCompat.getColor(getApplicationContext(), R.color.colorStatusBar)).withIdentifier(5).withSelectable(false),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_eighth).withIconTintingEnabled(true).withIcon(R.drawable.ic_layers).withSelectedIconColor(ContextCompat.getColor(getApplicationContext(), R.color.colorStatusBar)).withIdentifier(7).withSelectable(false)
                )
                .addStickyDrawerItems(
                        new SecondaryDrawerItem().withName(R.string.drawer_item_seventh).withIconTintingEnabled(true).withIcon(R.drawable.ic_settings).withIdentifier(6).withSelectable(false)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        Fragment fragment = null;
                        Class fragmentClass = ZFragment.class;
                        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                        Boolean activityStart = false;
                        /*
                            Currently unassigned fragments, 0, 4, 5, 6, 7(hidden max drawer only fragment)
                         */
                        switch((int) drawerItem.getIdentifier()){
                            case 0:
                                fragmentClass = OverviewFragment.class;
                                activityStart=false;
                                try {
                                    fragment = (Fragment) fragmentClass.newInstance();
                                    toolbar.setTitle("Overview");

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                result.closeDrawer();
                                toolbar.setVisibility(View.VISIBLE);
                                drawer_currentPos = result.getCurrentSelection();
                                break;
                            case 1:
                                fragmentClass = StickyListExpandableFragment.class;
                                activityStart=false;
                                    try {
                                        fragment = (Fragment) fragmentClass.newInstance();
                                        toolbar.setTitle("");

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                result.closeDrawer();
                                toolbar.setVisibility(View.VISIBLE);
                                drawer_currentPos = result.getCurrentSelection();
                                break;
                            case 2:
                                    activityStart = true;
                                    try {
                                        headerUpdateIntent = new Intent(getApplicationContext(),
                                                DrawerHeaderUpdater.class);
                                        startActivity(new Intent(getApplicationContext(), Login_anim.class));
                                        overridePendingTransition(R.anim.lga_in_right,R.anim.lga_out_left);
                                        //Add isRunning state, prevent null service
                                        //stopService(headerUpdateIntent);
                                        //toolbar.setTitle("Login Aeries");

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                result.closeDrawer();
                                result.setSelection(drawer_currentPos, false);
                                result.getMiniDrawer().setSelection(drawer_currentPos);
                                break;
                            case 3:
                                fragmentClass = BitmapMapStndView.class;
                                activityStart = false;
                                try {
                                    fragment = (Fragment) fragmentClass.newInstance();
                                    Bundle bundle = new Bundle();
                                    bundle.putBoolean("directed_lAdapter", false);
                                    fragment.setArguments(bundle);
                                    toolbar.setTitle("Campus Map");

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                result.closeDrawer();
                                //result setfullscreen
                                toolbar.setVisibility(View.GONE);
                                drawer_currentPos = result.getCurrentSelection();
                                break;
                            case 4:
                                activityStart = true;
                                try {
                                    startActivity(new Intent(getApplicationContext(), YouTubeActivity.class));
                                    //toolbar.setTitle("Login Aeries");

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                result.closeDrawer();
                                result.setSelection(drawer_currentPos, false);
                                result.getMiniDrawer().setSelection(drawer_currentPos);
                                break;
                            case 5:
                                fragmentClass = AboutUsFragment.class;
                                activityStart=false;
                                try {
                                    fragment = (Fragment) fragmentClass.newInstance();
                                    toolbar.setTitle("About");

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                result.closeDrawer();
                                toolbar.setVisibility(View.VISIBLE);
                                drawer_currentPos = result.getCurrentSelection();
                                break;
                            case 6:
                                activityStart = true;
                                try {
                                    startActivity(new Intent(getApplicationContext(), HomePreferenceActivity.class));

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                result.closeDrawer();
                                result.setSelection(drawer_currentPos, false);
                                result.getMiniDrawer().setSelection(drawer_currentPos);
                                break;
                            case 7:
                                fragmentClass = PrivacyPolicy.class;
                                activityStart=false;
                                try {
                                    fragment = (Fragment) fragmentClass.newInstance();
                                    toolbar.setTitle("Privacy Policy");

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                result.closeDrawer();
                                drawer_currentPos = result.getCurrentSelection();
                                toolbar.setVisibility(View.VISIBLE);
                                break;
                            default:
                                if (drawerItem instanceof Nameable) {
                                    Toast.makeText(MainActivity.this, ((Nameable) drawerItem).getName().getText(MainActivity.this), Toast.LENGTH_SHORT).show();
                                }
                                break;
                            }

                        mFragmentManager = getSupportFragmentManager();
                        if(!activityStart) {
                            mFragmentTransaction = mFragmentManager.beginTransaction();
                            mFragmentTransaction.replace(R.id.frame_container, fragment);
                            mFragmentTransaction.commit();
                        }
                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .withShowDrawerOnFirstLaunch(true)
                .withGenerateMiniDrawer(true)
                .build();

        //get out our drawerLayout
        crossfadeDrawerLayout = (CrossfadeDrawerLayout) result.getDrawerLayout();
        //define maxDrawerWidth
        crossfadeDrawerLayout.setMaxWidthPx(DrawerUIUtils.getOptimalDrawerWidth(this));
        //add second view (which is the miniDrawer)
        MiniDrawer miniResult = result.getMiniDrawer();
        //build the view for the MiniDrawer
        View view = miniResult.build(this);
        //set the background of the MiniDrawer as this would be transparent
        view.setBackgroundColor(UIUtils.getThemeColorFromAttrOrRes(this, com.mikepenz.materialdrawer.R.attr.material_drawer_background, com.novallc.foothillappmobile.R.color.material_fragment_background));
        //we do not have the MiniDrawer view during CrossfadeDrawerLayout creation so we will add it here
        crossfadeDrawerLayout.getSmallView().addView(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //Setup ActionBarToggle with DrawerLayout
        drawerToggle = setupDrawerToggle();
        crossfadeDrawerLayout.addDrawerListener(drawerToggle);

        android.support.v7.app.ActionBar mActionBar = this.getSupportActionBar();
        mActionBar.setDisplayShowCustomEnabled(true);

        mDrawerHeader = result.getHeader();
        //build header and configure padding
        result.getHeader().setPadding(0, getStatusBarHeight(), 0, 0);
        result.getHeader().findViewById(R.id.period_indicator).setPadding(0,((getStatusBarHeight()+30)*2)+67, 0, 0);
        result.getHeader().findViewById(R.id.period_content).setPadding(105,((getStatusBarHeight()+30)*2)+103, 0, 0);
        result.getHeader().findViewById(R.id.schedule_indicator).setPadding(0,((getStatusBarHeight()+30)*2)+67, 0, 0);
        result.getHeader().findViewById(R.id.schedule_content).setPadding(0,((getStatusBarHeight()+30)*2)+103, 0, 0);
        result.getHeader().findViewById(R.id.session_indicator).setVisibility(View.INVISIBLE);
        bButtonDrawer = (ImageButton)result.getHeader().findViewById(R.id.bDrawerClose);
        bButtonHelp = (ImageButton)result.getHeader().findViewById(R.id.bHelpAbbrevs);
        bButtonDrawer.getDrawable().setColorFilter(0xE6FCCF0D, PorterDuff.Mode.MULTIPLY);
        bButtonDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result.closeDrawer();
            }
        });
        bButtonHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        MainActivity.this);
                alertDialogBuilder.setTitle("Schedule Abbreviations");

                // set dialog message
                alertDialogBuilder
                        .setView(R.layout.dialog_alert_help)
                        .setIcon(getResources().getDrawable(R.drawable.ic_question_mark))
                        .setCancelable(false)
                        .setNegativeButton("Ok",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.show();
                TextView messageText0 = (TextView)alertDialog.findViewById(R.id.alert_help_text_0);
                TextView messageText1 = (TextView)alertDialog.findViewById(R.id.alert_help_text_1);
                messageText0.setText(Html.fromHtml(getString(R.string.alert_abbrev_help_0)));
                messageText1.setText(Html.fromHtml(getString(R.string.alert_abbrev_help_1)));
                alertDialog.show();
            }
        });

        //define the crossfader to be used with the miniDrawer. This is required to be able to automatically toggle open / close
        miniResult.withCrossFader(new ICrossfader() {
            @Override
            public void crossfade() {
                boolean isFaded = isCrossfaded();
                crossfadeDrawerLayout.crossfade(400);
                //only close the drawer if we were already faded and want to close it now
                if (isFaded) {
                    result.getDrawerLayout().closeDrawer(GravityCompat.START);
                }
            }

            @Override
            public boolean isCrossfaded() {
                return crossfadeDrawerLayout.isCrossfaded();
            }
        });


        /*
            BEGIN WITH ZFRAGMENT
            */
        mFragmentManager = getSupportFragmentManager();
        Class fragmentClass = OverviewFragment.class;
        try {
            if(fragment == null){
                fragment = (Fragment) fragmentClass.newInstance();
            }
            mFragmentTransaction = mFragmentManager.beginTransaction();
            mFragmentTransaction.replace(R.id.frame_container, fragment);
            mFragmentTransaction.commit();
            toolbar.setVisibility(View.VISIBLE);
            toolbar.setTitle("Overview");
        }catch (Exception e){e.printStackTrace();}

            //hook to the crossfade event
            crossfadeDrawerLayout.withCrossfadeListener(new CrossfadeDrawerLayout.CrossfadeListener() {
                @Override
                public void onCrossfade(View containerView, float currentSlidePercentage, int slideOffset) {
                    //Log.e("CrossfadeDrawerLayout", "crossfade: " + currentSlidePercentage + " - " + slideOffset);
                }
            });
                mBroadcasterReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                progressServiceStart = findViewById(R.id.progress_serviceStart);
                //boolean serviceStateDifference = getIntent().getBooleanExtra("serviceRunState", false);
                periodIndicatorContent = (TextView)findViewById(R.id.period_content);
                scheduleIndicatorContent = (TextView)findViewById(R.id.schedule_content);
                arcProgressView = (ArcProgress)findViewById(R.id.arc_progress);
                int curPer = intent.getIntExtra("curPer", -1);
                int curSch = intent.getIntExtra("curSch", -1);
                int curLngth = intent.getIntExtra("curLngth", 60);
                boolean serviceRestartState = intent.getBooleanExtra("serviceRestartState", false);
                if(serviceRestartState){
                    result.getHeader().findViewById(R.id.progress_serviceStart).setVisibility(View.VISIBLE);
                    return;
                }
                long timeDifference = intent.getLongExtra("timeDif", -1);
                _1 = (int)timeDifference; _2 = (float)_1/curLngth*100;
                int arcProgressPER = (int) _2;
                result.getHeader().findViewById(R.id.period_content).setPadding(105,((getStatusBarHeight()+30)*2)+103, 0, 0);
                fadeInSessionsOcc();
                if (curSch==0/*regular_NEW*/){
                    scheduleIndicatorContent_display = "REG";
                    //TODO ADD Dialog with information regarding abbreviations and other shortened text strings
                    switch (curPer){
                        case 0: periodIndicatorContent_display=Integer.toString(0);break;
                        case 1: periodIndicatorContent_display=Integer.toString(1);break;
                        case 2: periodIndicatorContent_display=Integer.toString(2);break;
                        case 3: periodIndicatorContent_display="BRK";result.getHeader().findViewById(R.id.period_content).setPadding(35+38,((getStatusBarHeight()+30)*2)+103, 0, 0);break;
                        case 4: periodIndicatorContent_display=Integer.toString(3);break;
                        case 5: periodIndicatorContent_display="HMR";result.getHeader().findViewById(R.id.period_content).setPadding(35+30,((getStatusBarHeight()+30)*2)+103, 0, 0);break;
                        case 6: periodIndicatorContent_display=Integer.toString(4);break;
                        case 7: periodIndicatorContent_display=Integer.toString(5);break;
                        case 8: periodIndicatorContent_display="LNCH";result.getHeader().findViewById(R.id.period_content).setPadding(35+15,((getStatusBarHeight()+30)*2)+103, 0, 0);break;
                        case 9: periodIndicatorContent_display=Integer.toString(6);break;

                        case 11: fadeInPassingSessions(11, "PER", "PER"); break; //0>1
                        case 22: fadeInPassingSessions(22, "PER", "PER"); break; //1>2
                        case 33: fadeInPassingSessions(22, "PER", "BRK"); break; //2>brk
                        case 44: fadeInPassingSessions(33, "BRK", "PER"); break; //brk>3
                        case 55: fadeInPassingSessions(33, "PER", "HMR"); break;  //3>hr
                        case 66: fadeInPassingSessions(44, "HMR", "PER"); break; //hr>4
                        case 77: fadeInPassingSessions(55, "PER", "PER"); break; //4>5
                        case 88: fadeInPassingSessions(55, "PER", "LCH"); break; //5>lch
                        case 99: fadeInPassingSessions(66, "LCH", "PER"); break; //lnch>6
                        case 998: fadeInPassingSessions(0, "PRE", "PRE"); break; //pre-school start
                            //-1 case, alt null
                        case -1:fadeInNoSessions(getString(R.string.arc_nosessions));
                            Log.d("CHECK", "FADEINSESSIONS");break;

                    }
                }
                else if(curSch==2){
                    scheduleIndicatorContent_display = "LTE";
                    switch (curPer){
                        case 0: periodIndicatorContent_display=Integer.toString(0);break;
                        case 1: periodIndicatorContent_display=Integer.toString(1);break;
                        case 2: periodIndicatorContent_display=Integer.toString(2);break;
                        case 3: periodIndicatorContent_display="BRK";result.getHeader().findViewById(R.id.period_content).setPadding(35+38,((getStatusBarHeight()+30)*2)+103, 0, 0);break;
                        case 4: periodIndicatorContent_display=Integer.toString(3);break;
                        case 5: periodIndicatorContent_display=Integer.toString(4);break;
                        case 6: periodIndicatorContent_display=Integer.toString(5);break;
                        case 7: periodIndicatorContent_display="LNCH";result.getHeader().findViewById(R.id.period_content).setPadding(35+15,((getStatusBarHeight()+30)*2)+103, 0, 0);break;
                        case 8: periodIndicatorContent_display=Integer.toString(5);break;

                        case 11: fadeInPassingSessions(11, "PER", "PER"); break; //0>1
                        case 22: fadeInPassingSessions(22, "PER", "PER"); break; //1>2
                        case 33: fadeInPassingSessions(22, "PER", "BRK"); break; //2>brk
                        case 44: fadeInPassingSessions(33, "BRK", "PER"); break; //brk>3
                        case 55: fadeInPassingSessions(44, "PER", "PER"); break; //3>4
                        case 66: fadeInPassingSessions(55, "PER", "PER"); break; //4>5
                        case 77: fadeInPassingSessions(55, "PER", "LCH"); break; //5>lnch
                        case 88: fadeInPassingSessions(66, "LCH", "PER"); break; //lnch>6
                        case 998: fadeInPassingSessions(0, "PRE", "PRE"); break; //pre-school start
                        //-1 case, alt null
                        case -1: fadeInNoSessions(getString(R.string.arc_nosessions));
                            break;
                    }
                }else if(curSch==3){
                    scheduleIndicatorContent_display = "MIN";
                    switch (curPer){
                        case 0: periodIndicatorContent_display=Integer.toString(0);break;
                        case 1: periodIndicatorContent_display=Integer.toString(1);break;
                        case 2: periodIndicatorContent_display=Integer.toString(2);break;
                        case 3: periodIndicatorContent_display=Integer.toString(3);break;
                        case 4: periodIndicatorContent_display="BRK";result.getHeader().findViewById(R.id.period_content).setPadding(35+38,((getStatusBarHeight()+30)*2)+103, 0, 0);break;
                        case 5: periodIndicatorContent_display=Integer.toString(4);break;
                        case 6: periodIndicatorContent_display=Integer.toString(5);break;
                        case 7: periodIndicatorContent_display=Integer.toString(6);break;

                        case 11: fadeInPassingSessions(11, "PER", "PER"); break; //0>1
                        case 22: fadeInPassingSessions(22, "PER", "PER"); break; //1>2
                        case 33: fadeInPassingSessions(33, "PER", "PER"); break; //2>3
                        case 44: fadeInPassingSessions(33, "PER", "BRK"); break; //3>brk
                        case 55: fadeInPassingSessions(44, "BRK", "PER"); break; //brk>4
                        case 66: fadeInPassingSessions(55, "PER", "PER"); break; //4>5
                        case 77: fadeInPassingSessions(66, "PER", "PER"); break; //5>6
                        case 998: fadeInPassingSessions(0, "PRE", "PRE"); break; //pre-school start
                        //-1 case, alt null
                        case -1: fadeInNoSessions(getString(R.string.arc_nosessions));
                            break;
                    }
                    //FINALS SCHEDULE = 1-4, 2-5, 3-6/0
                }else if(curSch==4){
                    scheduleIndicatorContent_display = "FIN";
                    switch (curPer){
                        case 0: periodIndicatorContent_display=Integer.toString(1);break;
                        case 1: periodIndicatorContent_display="BRK";result.getHeader().findViewById(R.id.period_content).setPadding(35+38,((getStatusBarHeight()+30)*2)+103, 0, 0);break;
                        case 2: periodIndicatorContent_display=Integer.toString(4);break;

                        case 11: fadeInPassingSessions(11, "PER", "BRK"); break; //1>brk
                        case 22: fadeInPassingSessions(44, "BRK", "PER"); break; //brk>4
                        case 998: fadeInPassingSessions(0, "PRE", "PRE"); break; //pre-school start
                        //-1 case, alt null
                        case -1: fadeInNoSessions(getString(R.string.arc_nosessions));
                            break;
                    }
                }else if(curSch==5){
                    scheduleIndicatorContent_display = "FIN";
                    switch (curPer){
                        case 0: periodIndicatorContent_display=Integer.toString(2);break;
                        case 1: periodIndicatorContent_display="BRK";result.getHeader().findViewById(R.id.period_content).setPadding(35+38,((getStatusBarHeight()+30)*2)+103, 0, 0);break;
                        case 2: periodIndicatorContent_display=Integer.toString(5);break;

                        case 11: fadeInPassingSessions(22, "PER", "BRK"); break; //2>brk
                        case 22: fadeInPassingSessions(55, "BRK", "PER"); break; //brk>5
                        case 998: fadeInPassingSessions(0, "PRE", "PRE"); break; //pre-school start
                        //-1 case, alt null
                        case -1: fadeInNoSessions(getString(R.string.arc_nosessions));
                            break;
                    }
                }else if(curSch==6){
                    scheduleIndicatorContent_display = "FIN";
                    switch (curPer){
                        case 0: periodIndicatorContent_display=Integer.toString(3);break;
                        case 1: periodIndicatorContent_display="BRK";result.getHeader().findViewById(R.id.period_content).setPadding(35+38,((getStatusBarHeight()+30)*2)+103, 0, 0);break;
                        case 2: periodIndicatorContent_display="6/0";result.getHeader().findViewById(R.id.period_content).setPadding(35+43,((getStatusBarHeight()+30)*2)+103, 0, 0);break;

                        case 11: fadeInPassingSessions(33, "PER", "BRK"); break; //3>brk
                        case 22: fadeInPassingSessions(66, "BRK", "6/0"); break; //brk>6/0
                        case 998: fadeInPassingSessions(0, "PRE", "PRE"); break; //pre-school start
                        //-1 case, alt null
                        case -1: fadeInNoSessions(getString(R.string.arc_nosessions));break;
                    }
                }else if(curSch==8){
                    fadeInNoSessions(getString(R.string.arc_passingsession_hol));
                }
                else
                   fadeInNoSessions(getString(R.string.arc_nosessions));

                if(periodIndicatorContent!=null&&scheduleIndicatorContent!=null&&arcProgressView!=null) {
                    periodIndicatorContent.setText(periodIndicatorContent_display);
                    scheduleIndicatorContent.setText(scheduleIndicatorContent_display);
                    arcProgressView.setProgressText(Long.toString(timeDifference));
                    arcProgressView.setProgress(arcProgressPER);
                    Log.d("CHECK", "arcProgressPER = " + _2);
                    progressServiceStart.setVisibility(View.INVISIBLE);
                }else{}

                registerReceiver(mBroadcasterReceiver, new IntentFilter(
                        DrawerHeaderUpdater.BROADCAST_ACTION));

            }
        };
    }

    public ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, crossfadeDrawerLayout, (Toolbar) findViewById(R.id.toolbar), R.string.drawer_open,  R.string.drawer_close);
    }

        @Override
        protected void onResume() {
            super.onResume();

            headerUpdateIntent = new Intent(getApplicationContext(),
                    DrawerHeaderUpdater.class);
            startService(headerUpdateIntent);

            notifUpdateIntent = new Intent(getApplicationContext(),
                    NotifUpdater.class);
            startService(notifUpdateIntent);

            registerReceiver(mBroadcasterReceiver, new IntentFilter(
                    DrawerHeaderUpdater.BROADCAST_ACTION));
        }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {

        return super.onCreateView(name, context, attrs);
    }

    @Override
        protected void onPause() {
            super.onPause();

            stopService(headerUpdateIntent);
            unregisterReceiver(mBroadcasterReceiver);

            result.getHeader().findViewById(R.id.progress_serviceStart).setVisibility(View.VISIBLE);

        }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public void fadeInNoSessions(String mText){
            result.getHeader().findViewById(R.id.arc_progress).setVisibility(View.INVISIBLE);
            result.getHeader().findViewById(R.id.schedule_content).setVisibility(View.INVISIBLE);
            result.getHeader().findViewById(R.id.schedule_indicator).setVisibility(View.INVISIBLE);
            result.getHeader().findViewById(R.id.period_content).setVisibility(View.INVISIBLE);
            result.getHeader().findViewById(R.id.period_indicator).setVisibility(View.INVISIBLE);
            result.getHeader().findViewById(R.id.passingsession_indicator).setVisibility(View.GONE);
            result.getHeader().findViewById(R.id.session_indicator).setVisibility(View.VISIBLE);
            TextView mTextView = (TextView) result.getHeader().findViewById(R.id.session_indicator);
            mTextView.setText(mText);
    }



    public void fadeInSessionsOcc(){
            result.getHeader().findViewById(R.id.arc_progress).setVisibility(View.VISIBLE);
            result.getHeader().findViewById(R.id.schedule_content).setVisibility(View.VISIBLE);
            result.getHeader().findViewById(R.id.schedule_indicator).setVisibility(View.VISIBLE);
            result.getHeader().findViewById(R.id.period_content).setVisibility(View.VISIBLE);
            result.getHeader().findViewById(R.id.period_indicator).setVisibility(View.VISIBLE);
            result.getHeader().findViewById(R.id.passingsession_indicator).setVisibility(View.GONE);
            result.getHeader().findViewById(R.id.session_indicator).setVisibility(View.INVISIBLE);
    }

    /*
      Receieves curPer, if fPer and sPer are periods, then curPer = class arriving to
       else, curPer = only period present
     */
    public void fadeInPassingSessions(int _curPer, String _fPer, String _sPer){
        final ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(252,207,13));
        final ForegroundColorSpan fcs2 = new ForegroundColorSpan(Color.rgb(252,207,13));
        final RelativeSizeSpan rss = new RelativeSizeSpan(1.5f);
        result.getHeader().findViewById(R.id.arc_progress).setVisibility(View.VISIBLE);
        result.getHeader().findViewById(R.id.schedule_content).setVisibility(View.INVISIBLE);
        result.getHeader().findViewById(R.id.schedule_indicator).setVisibility(View.INVISIBLE);
        result.getHeader().findViewById(R.id.period_content).setVisibility(View.INVISIBLE);
        result.getHeader().findViewById(R.id.period_indicator).setVisibility(View.INVISIBLE);
        result.getHeader().findViewById(R.id.session_indicator).setVisibility(View.INVISIBLE);

        TextView passingSessionIND = (TextView) result.getHeader().findViewById(R.id.passingsession_indicator);
        passingSessionIND.setVisibility(View.VISIBLE);

        //BOTH, FP ONLY, SP ONLY, NONE
        if(_fPer!="PER"&&_sPer!="PER"&&_fPer!="PRE"&&_sPer!="PRE"&&_fPer!="HOL"&&_sPer!="HOL"){
            _str0 = String.format(getResources().getString(R.string.arc_passingsession_both), _fPer, _sPer );
            mSpannable0 = new SpannableString(_str0);

            mSpannable0.setSpan(fcs, 16, 19, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            mSpannable0.setSpan(fcs2, 22, _str0.length()-1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            mSpannable0.setSpan(rss, 20, 21, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }else if(_fPer=="PER"&&_sPer=="PER"){
            _str0 = String.format(getResources().getString(R.string.arc_passingsession), _fPer, _curPer/11-1, _sPer, _curPer/11 );
            mSpannable0 = new SpannableString(_str0);

            mSpannable0.setSpan(fcs, 20, 21, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            mSpannable0.setSpan(fcs2, 28, _str0.length()-1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            mSpannable0.setSpan(rss, 22, 23, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }else if(_fPer!="PER"&&_sPer=="PER"){
            _str0 = String.format(getResources().getString(R.string.arc_passingsession_frst), _fPer, _sPer, _curPer/11 );
            mSpannable0 = new SpannableString(_str0);

            mSpannable0.setSpan(fcs, 16, 19, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            mSpannable0.setSpan(fcs2, 25, _str0.length()-1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            mSpannable0.setSpan(rss, 20, 21, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }else if(_fPer=="PER"&&_sPer!="PER"){
            _str0 = String.format(getResources().getString(R.string.arc_passingsession_scnd), _fPer, _curPer/11, _sPer);
            mSpannable0 = new SpannableString(_str0);

            mSpannable0.setSpan(fcs, 20, 21, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            mSpannable0.setSpan(fcs2, 24, _str0.length()-1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            mSpannable0.setSpan(rss, 22, 23, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }else if(_fPer=="PRE"&&_sPer=="PRE"){
            _str0 = getResources().getString(R.string.arc_passingsession_pre);
            mSpannable0 = new SpannableString(_str0); //no spannable necessary
        }else if(_fPer=="HOL"&&_sPer=="HOL"){
            _str0 = getResources().getString(R.string.arc_passingsession_hol);
            mSpannable0 = new SpannableString(_str0); //no spannable necessary
        }
//        mSpannable0.setSpan(fcs, 20, 21, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
//        mSpannable0.setSpan(fcs2, 28, _str0.length()-1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
//        mSpannable0.setSpan(rss, 22, 23, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        passingSessionIND.setText(TextUtils.concat(mSpannable0));
//        Html.fromHtml(String.format(getResources().getString(R.string.arc_passingsession), _curPer/11-1, _curPer/11))
    }

    //Event handler for layer icon in toolbar
    public void openDrawer(View view){
        boolean isFaded = crossfadeDrawerLayout.isCrossfaded();
        result.getDrawerLayout().openDrawer(Gravity.LEFT);
        crossfadeDrawerLayout.crossfade(0);
        if(isFaded){
            result.getDrawerLayout().closeDrawer(GravityCompat.START);
            mExecuted=true;
        }
        if(mExecuted){
            result.getDrawerLayout().openDrawer(Gravity.LEFT);
            mExecuted=false;
            Log.d("FADED", "openDrawer: ");
        }
    }

    /*
        Return size of drawer layout
     */
    private int getDrawerLayoutSizeinDp(){
        return 240-56;
    }

    // `onPostCreate` called when activity start-up is complete after `onStart()`
    // NOTE! Make sure to override the method with only a single `Bundle` argument
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //add the values which need to be saved from the drawer to the bundle
        outState = result.saveInstanceState(outState);
        //add the values which need to be saved from the accountHeader to the bundle
        outState = headerResult.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

}