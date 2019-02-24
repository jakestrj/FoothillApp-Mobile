package com.novallc.foothillappmobile.activity;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.text.SpannableString;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.novallc.foothillappmobile.R;
import com.novallc.foothillappmobile.circularprogress.ArcProgress;

public class NotifUpdater extends Service{

    BroadcastReceiver mBroadcastReciever;
    Intent mUpdaterIntent;
    SharedPreferences mPrefs;
    Boolean isNotifEnabled;
    private static final String PACKAGE = "com.novallc.foothillappmobile.NotificationActivity";

    public static NotificationCompat.Builder mNotifBuilder;
    public static NotificationManager mNotifyMgr;

    private TextView periodIndicatorContent; private String periodIndicatorContent_display;
    private TextView scheduleIndicatorContent; private String scheduleIndicatorContent_display;
    private ArcProgress arcProgressView; private View progressServiceStart;

    private int[] progressIconIds = {R.mipmap._0progress, R.mipmap._1progress, R.mipmap._2progress, R.mipmap._3progress, R.mipmap._4progress,
            R.mipmap._5progress, R.mipmap._6progress, R.mipmap._7progress, R.mipmap._8progress, R.mipmap._9progress, R.mipmap._10progress, R.mipmap._11progress,
            R.mipmap._12progress, R.mipmap._13progress, R.mipmap._14progress, R.mipmap._15progress, R.mipmap._16progress, R.mipmap._17progress, R.mipmap._18progress,
            R.mipmap._19progress, R.mipmap._20progress, R.mipmap._21progress, R.mipmap._22progress, R.mipmap._23progress, R.mipmap._24progress, R.mipmap._25progress,
            R.mipmap._26progress, R.mipmap._27progress, R.mipmap._28progress, R.mipmap._29progress, R.mipmap._30progress, R.mipmap._31progress, R.mipmap._32progress,
            R.mipmap._33progress, R.mipmap._34progress, R.mipmap._35progress, R.mipmap._36progress, R.mipmap._37progress, R.mipmap._38progress, R.mipmap._39progress,
            R.mipmap._40progress, R.mipmap._41progress, R.mipmap._42progress, R.mipmap._43progress, R.mipmap._44progress, R.mipmap._45progress, R.mipmap._46progress,
            R.mipmap._47progress, R.mipmap._48progress, R.mipmap._49progress, R.mipmap._50progress, R.mipmap._51progress, R.mipmap._52progress, R.mipmap._53progress,
            R.mipmap._54progress, R.mipmap._55progress, R.mipmap._56progress, R.mipmap._57progress, R.mipmap._58progress, R.mipmap._59progress, R.mipmap._60progress};

    private int _1; private float _2;
    private long timeDifference;
    private boolean nullPerState = false;
    String _str0;
    SpannableString mSpannable0;

    @Override
    public void onCreate() {
        super.onCreate();

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);
        mPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        mNotifBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap._nullprogress)
                        .setContentTitle("Foothill Mobile")
                        .setContentText("...")
                        .setOngoing(true)
                        .setContentIntent(pIntent);
        mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        isNotifEnabled = mPrefs.getBoolean("mNotifSwitchPref", true);

        mBroadcastReciever = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                int curPer = intent.getIntExtra("curPer", -1);
                int curSch = intent.getIntExtra("curSch", -1);
                int curLngth = intent.getIntExtra("curLngth", 60);
                boolean serviceRestartState = intent.getBooleanExtra("serviceRestartState", false);
                nullPerState = false;
                if(serviceRestartState){
                    //loading updater
                    return;
                }
                timeDifference = intent.getLongExtra("timeDif", -1);
                _1 = (int)timeDifference; _2 = (float)_1/curLngth*100;
                int arcProgressPER = (int) _2;

                if (curSch==0/*regular_NEW*/){
                    scheduleIndicatorContent_display = "REG";
                    //TODO ADD Dialog with information regarding abbreviations and other shortened text strings
                    switch (curPer){
                        case 0: periodIndicatorContent_display=Integer.toString(0);break;
                        case 1: periodIndicatorContent_display=Integer.toString(1);break;
                        case 2: periodIndicatorContent_display=Integer.toString(2);break;
                        case 3: periodIndicatorContent_display="BRK";break;
                        case 4: periodIndicatorContent_display=Integer.toString(3);break;
                        case 5: periodIndicatorContent_display="HMR";break;
                        case 6: periodIndicatorContent_display=Integer.toString(4);break;
                        case 7: periodIndicatorContent_display=Integer.toString(5);break;
                        case 8: periodIndicatorContent_display="LNCH";break;
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
                        case -1:
                            notifyText_update(getString(R.string.arc_nosessions), "", 0);
                            Log.d("CHECK", "FADEINSESSIONS");break;

                    }
                }
                else if(curSch==2){
                    scheduleIndicatorContent_display = "LTE";
                    switch (curPer){
                        case 0: periodIndicatorContent_display=Integer.toString(0);break;
                        case 1: periodIndicatorContent_display=Integer.toString(1);break;
                        case 2: periodIndicatorContent_display=Integer.toString(2);break;
                        case 3: periodIndicatorContent_display="BRK";break;
                        case 4: periodIndicatorContent_display=Integer.toString(3);break;
                        case 5: periodIndicatorContent_display=Integer.toString(4);break;
                        case 6: periodIndicatorContent_display=Integer.toString(5);break;
                        case 7: periodIndicatorContent_display="LNCH";break;
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
                        case -1:
                            notifyText_update(getString(R.string.arc_nosessions), "", 0);break;
                    }
                }else if(curSch==3){
                    scheduleIndicatorContent_display = "MIN";
                    switch (curPer){
                        case 0: periodIndicatorContent_display=Integer.toString(0);break;
                        case 1: periodIndicatorContent_display=Integer.toString(1);break;
                        case 2: periodIndicatorContent_display=Integer.toString(2);break;
                        case 3: periodIndicatorContent_display=Integer.toString(3);break;
                        case 4: periodIndicatorContent_display="BRK";break;
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
                        case -1:
                            notifyText_update(getString(R.string.arc_nosessions), "", 0);break;
                    }
                    //FINALS SCHEDULE = 1-4, 2-5, 3-6/0
                }else if(curSch==4){
                    scheduleIndicatorContent_display = "FIN";
                    switch (curPer){
                        case 0: periodIndicatorContent_display=Integer.toString(1);break;
                        case 1: periodIndicatorContent_display="BRK";break;
                        case 2: periodIndicatorContent_display=Integer.toString(4);break;

                        case 11: fadeInPassingSessions(11, "PER", "BRK"); break; //1>brk
                        case 22: fadeInPassingSessions(44, "BRK", "PER"); break; //brk>4
                        case 998: fadeInPassingSessions(0, "PRE", "PRE"); break; //pre-school start
                        //-1 case, alt null
                        case -1:
                            notifyText_update(getString(R.string.arc_nosessions), "", 0);break;
                    }
                }else if(curSch==5){
                    scheduleIndicatorContent_display = "FIN";
                    switch (curPer){
                        case 0: periodIndicatorContent_display=Integer.toString(2);break;
                        case 1: periodIndicatorContent_display="BRK";break;
                        case 2: periodIndicatorContent_display=Integer.toString(5);break;

                        case 11: fadeInPassingSessions(22, "PER", "BRK"); break; //2>brk
                        case 22: fadeInPassingSessions(55, "BRK", "PER"); break; //brk>5
                        case 998: fadeInPassingSessions(0, "PRE", "PRE"); break; //pre-school start
                        //-1 case, alt null
                        case -1:
                            notifyText_update(getString(R.string.arc_nosessions), "", 0);break;
                    }
                }else if(curSch==6){
                    scheduleIndicatorContent_display = "FIN";
                    switch (curPer){
                        case 0: periodIndicatorContent_display=Integer.toString(3);break;
                        case 1: periodIndicatorContent_display="BRK";break;
                        case 2: periodIndicatorContent_display="6/0";break;

                        case 11: fadeInPassingSessions(33, "PER", "BRK"); break; //3>brk
                        case 22: fadeInPassingSessions(66, "BRK", "6/0"); break; //brk>6/0
                        case 998: fadeInPassingSessions(0, "PRE", "PRE"); break; //pre-school start
                        //-1 case, alt null
                        case -1: break;
                    }
                }else if(curSch==8){
                    notifyText_update(getString(R.string.arc_passingsession_hol), "", 0);
                }
                else {
                    notifyText_update(getString(R.string.arc_nosessions), "", 0);
                    nullPerState = true;
                }

                if(!nullPerState && curPer != -1 && curSch != 8 && curPer <= 9 && curPer > -1) {
                    notifyText_update(String.format(getString(R.string.notif_persch_header), periodIndicatorContent_display, scheduleIndicatorContent_display), String.format(getString(R.string.notif_tremaining_content), Long.toString(timeDifference)), (int)timeDifference);
                    //mNotifBuilder.setSmallIcon(progressIconIds[(int)timeDifference]);
                }
            }
        };

        mUpdaterIntent = new Intent(getApplicationContext(),
                DrawerHeaderUpdater.class);
        startService(mUpdaterIntent);

        registerReceiver(mBroadcastReciever, new IntentFilter(
                DrawerHeaderUpdater.BROADCAST_ACTION));

    }

    /*
      Receieves curPer, if fPer and sPer are periods, then curPer = class arriving to
       else, curPer = only period present
     */
    public void fadeInPassingSessions(int _curPer, String _fPer, String _sPer){

        //BOTH, FP ONLY, SP ONLY, NONE
        if(_fPer!="PER"&&_sPer!="PER"&&_fPer!="PRE"&&_sPer!="PRE"&&_fPer!="HOL"&&_sPer!="HOL"){
            _str0 = String.format(getResources().getString(R.string.arc_passingsession_both), _fPer, _sPer );

        }else if(_fPer=="PER"&&_sPer=="PER"){
            _str0 = String.format(getResources().getString(R.string.arc_passingsession), _fPer, _curPer/11-1, _sPer, _curPer/11 );

        }else if(_fPer!="PER"&&_sPer=="PER"){
            _str0 = String.format(getResources().getString(R.string.arc_passingsession_frst), _fPer, _sPer, _curPer/11 );

        }else if(_fPer=="PER"&&_sPer!="PER"){
            _str0 = String.format(getResources().getString(R.string.arc_passingsession_scnd), _fPer, _curPer/11, _sPer);

        }else if(_fPer=="PRE"&&_sPer=="PRE"){
            _str0 = getResources().getString(R.string.arc_passingsession_pre);

        }else if(_fPer=="HOL"&&_sPer=="HOL"){
            _str0 = getResources().getString(R.string.arc_passingsession_hol);

        }
//        mSpannable0.setSpan(fcs, 20, 21, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
//        mSpannable0.setSpan(fcs2, 28, _str0.length()-1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
//        mSpannable0.setSpan(rss, 22, 23, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        notifyText_update(_str0, String.format(getString(R.string.notif_tremaining_content), Long.toString(timeDifference)), (int)timeDifference);
//        Html.fromHtml(String.format(getResources().getString(R.string.arc_passingsession), _curPer/11-1, _curPer/11))
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopService(mUpdaterIntent);
        unregisterReceiver(mBroadcastReciever);
    }

    public void notifyText_update(String _headerText, String _contentText, int _progressInt){
        mNotifBuilder.setContentTitle(_headerText);
        mNotifBuilder.setContentText(_contentText);
        if((int)timeDifference>0 && !((int)timeDifference>progressIconIds.length-1))
            mNotifBuilder.setSmallIcon(progressIconIds[_progressInt]);
        else if((int)timeDifference>progressIconIds.length-1)
            mNotifBuilder.setSmallIcon(R.mipmap._overflowprogress);
        else
            mNotifBuilder.setSmallIcon(R.mipmap._nullprogress);
        if(isNotifEnabled) mNotifyMgr.notify(0, mNotifBuilder.build()); else mNotifyMgr.cancel(0);
        Log.d("CHECK", "mNotifSwitchPref=" + Boolean.toString(isNotifEnabled));
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
