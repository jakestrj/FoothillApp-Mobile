package com.novallc.foothillappmobile.activity;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DrawerHeaderUpdater extends Service{
    public static final String BROADCAST_ACTION = "com.novallc.foothillappmobile.activity.DrawerHeaderUpdater";
    private static final int PRE_CLASS_CONSTANT_LNGTH = 30;
    private Date date;
    //curPerREG_old = 0=P0; 1=P1; 2=P2; 3=break; 4=P3; 5=P4; 6=P5; 7=lunch; 8=P6
    //curPerREG_new = 0=P0; 1=P1; 2=P2; 3=break; 4=P3; 5=HR(Homeroom); 6=P4; 7=P5; 8=Lunch; 9=P6
    //curPerTUT = 0=P0; 1=P1; 2=P2; 3=break; 4=Tutorial; 5=P3; 6=P4; 7=P5; 8=LUNCH; 9=P6
    private final Handler handler = new Handler();
    private Updater updater;
    private Intent intent;
    //End times and comparison constants
    private int c_curDay = -1;
    private String c_curDate;
    private int c_schState;
    private int c_curPer;
    private int c_perLngth;
    long timeDifference;
    private boolean serviceState = false;

    private static final String inputFormat="HH:mm";
//    private String endsReg[]={"07:40","08:41","09:42","09:55","11:00","12:01","13:02","13:32","14:33"};
//    private String startsReg[]={"06:45","07:45","08:46","09:42","10:00","11:05","12:06","13:02","13:37"};
    public static final String holConstantDates[] = {"2017/09/04", "2017/10/30", "2017/11/10", "2017/11/20", "2017/11/21", "2017/11/22", "2017/11/23", "2017/11/24", "2017/12/22", "2017/12/25", "2017/12/26", "2017/12/27", "2017/12/28",
        "2017/12/29", "2017/12/30", "2017/12/31", "2018/01/01", "2018/01/02", "2018/01/03", "2018/01/04", "2018/01/05", "2018/01/15", "2018/02/12", "2018/02/19", "2018/03/26", "2018/03/27", "2018/03/28", "2018/03/29", "2018/03/30", "2018/05/28"};
    public static final String ltConstantDates[] = {"2017/09/13", "2017/09/25", "2017/10/11", "2017/10/25", "2017/11/08", "2017/12/13", "2018/01/24", "2018/02/07", "2018/02/21", "2018/03/07", "2018/03/21", "2018/04/04", "2018/05/02", "2018/05/16"}; //in format 'yyyy/MM/dd'
    public static final String minConstantDates[] = {"2017/09/22", "2017/10/27", "2018/04/06"};
    public static final String finConstantDates0[] = {"2017/12/19", "2018/06/05"};
    public static final String finConstantDates1[] = {"2017/01/20", "2018/06/06"};
    public static final String finConstantDates2[] = {"2017/01/21", "2018/06/07"};
    public static final String summConstantDates[] = {"2018/06/07", "2018/09/22"};

    private final String endsReg[]={  "07:40","08:37","09:34","09:44","10:41","11:11","12:08","13:05","13:35","14:32"};
    private final String startsReg[]={"06:48","07:45","08:42","09:34","09:49","10:46","11:16","12:13","13:05","13:40"};
    private final String endTut[] = {"07:40", "08:36", "09:32", "09:44", "10:41", "11:15", "12:11", "13:07", "13:37", "14:33"};
    private final String startTut[]={"06:45", "07:45", "08:41", "09:32", "09:49", "10:46", "11:20", "12:16", "13:07", "13:42"};
    private final String startAss[] = {"06:45", "07:45", "08:37", "09:37", "10:27", "10:44", "11:36", "12:28", "13:14", "13:49"};
    private final String endAss[] =   {"07:40", "08:32", "09:27", "10:27", "10:39", "11:31", "12:23", "13:14", "13:44", "14:35"};
    private final String startLt[] = {"08:40", "09:25", "10:10", "10:50", "11:05", "11:53", "12:38", "13:18", "13:53"};
    private final String endLt[] = {"09:20", "10:05", "10:50", "11:00", "11:48", "12:33", "13:18", "13:48", "14:33"};
    private final String startMin[] = {"07:02", "07:45", "08:27", "09:09", "09:46", "10:03", "10:45", "11:27"};
    private final String endMin[] = {"07:40", "08:22", "09:04", "09:46", "09:58", "10:40", "11:22", "12:05"};
    private final String startFinals[] = {"07:45", "09:45", "10:00"};
    private final String endFinals[] = {"09:45", "09:55", "12:00"};
    private final String startFinals_PRE = "07:15";
    private final String startMin_PRE = "06:32";
    private final String startLt_PRE = "08:10";
    private final String startAss_PRE = "06:15";
    private final String startReg_PRE = "06:18";
    SimpleDateFormat inputParser = new SimpleDateFormat(inputFormat, Locale.US);

    @Override
    public void onCreate() {
        updater = new Updater();
        Log.d("acd", "Updater Created");
        intent = new Intent(BROADCAST_ACTION);
    }

    class Updater extends Thread {

        public boolean isRunning = false;
        public long DELAY = 10000;

        @Override
        public void run() {
            super.run();

            isRunning = true;
            //Send broadcast for start of service progress bar
            while(isRunning){
                Log.d("acd", "Running... DisplayLoggingInfo");
                // sendbroadcast and create instances
                compareDates();
                DisplayLoggingInfo();
                try {
                    Thread.sleep(DELAY);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    isRunning = false;
                }
            }
        } // run end

        public boolean isRunning() {
            return this.isRunning;
        }

    } // inner class end

    public void DisplayLoggingInfo() {
        intent.putExtra("curPer", c_curPer);
        intent.putExtra("curSch", c_schState);
        intent.putExtra("curLngth", c_perLngth);
        intent.putExtra("timeDif", timeDifference);
        sendBroadcast(intent);
    }

    private void compareDates(){
        //Calender/Time handler
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdFormat = new SimpleDateFormat("yyyy/MM/dd") ;
        c_curDay = calendar.get(Calendar.DAY_OF_WEEK);
        c_curDate = mdFormat.format(calendar.getTime());
        try {

            //0=reg; 1=tut; 2=misc
            //TODO Add features for dates, ie. finals, min day, late start

            switch(c_curDay){
                case Calendar.MONDAY:
                case Calendar.TUESDAY:
                case Calendar.WEDNESDAY:
                case Calendar.THURSDAY:
                case Calendar.FRIDAY:
//                Log.d("COMPARE", "c_schState: 0");
                    c_schState=0;
                    break;
                default:
//                Log.d("COMPARE", "c_schState: 2");
                    c_schState=7;
                    break;
            }
            if(Arrays.asList(ltConstantDates).contains(c_curDate))
                c_schState = 2;
            else if(Arrays.asList(minConstantDates).contains(c_curDate))
                c_schState = 3;
            else if(Arrays.asList(holConstantDates).contains(c_curDate))
                c_schState = 8;
            else if(mdFormat.parse(c_curDate).after(mdFormat.parse(summConstantDates[0]))
                    && mdFormat.parse(c_curDate).before(mdFormat.parse(summConstantDates[1])))
                c_schState = 7;
            //FINAL SCHEDULE 3 days = 1-4 (4), 2-5 (5), 3-6/0 (6)
            else if(Arrays.asList(finConstantDates0).contains(c_curDate))
                c_schState = 4;
            else if(Arrays.asList(finConstantDates1).contains(c_curDate))
                c_schState = 5;
            else if(Arrays.asList(finConstantDates2).contains(c_curDate))
                c_schState = 6;
            //c_schState=1;
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

//        dateCMP1=parseDate(startsReg[0]);
//        dateCMP2=parseDate(endsReg[0]);
            date=parseDate(hour+":"+minute);//(hour+":"+minute)
        }catch (ParseException e){
            e.printStackTrace();
        }
        //Debugging procedures
        //minute = minute+1;
//        date=parseDate("10:46");
//        c_schState=0;
        //Log.d("CHECK", "date/time = " + date);
        //TODO Compress first timeDifference update to reduce length
        switch(c_schState){
            case 0:
                //Regular times
                if(date.before(parseDate(endsReg[0]))&&date.after(parseDate(startsReg[0]))||date.equals(parseDate(startsReg[0]))) {
                    c_curPer=0;checkRemainingTimeReg();
                }
                else if(date.before(parseDate(endsReg[1]))&&date.after(parseDate(startsReg[1]))||date.equals(parseDate(startsReg[1]))) {
                    c_curPer=1;checkRemainingTimeReg();
                }
                else if(date.before(parseDate(endsReg[2]))&&date.after(parseDate(startsReg[2]))||date.equals(parseDate(startsReg[2]))) {
                    c_curPer=2;checkRemainingTimeReg();
                }
                else if(date.before(parseDate(endsReg[3]))&&date.after(parseDate(startsReg[3]))||date.equals(parseDate(startsReg[3]))) {
                    c_curPer=3;checkRemainingTimeReg();
                }
                else if(date.before(parseDate(endsReg[4]))&&date.after(parseDate(startsReg[4]))||date.equals(parseDate(startsReg[4]))) {
                    c_curPer=4;checkRemainingTimeReg();
                }
                else if(date.before(parseDate(endsReg[5]))&&date.after(parseDate(startsReg[5]))||date.equals(parseDate(startsReg[5]))) {
                    c_curPer=5;checkRemainingTimeReg();
                }
                else if(date.before(parseDate(endsReg[6]))&&date.after(parseDate(startsReg[6]))||date.equals(parseDate(startsReg[6]))) {
                    c_curPer=6;checkRemainingTimeReg();
                }
                else if(date.before(parseDate(endsReg[7]))&&date.after(parseDate(startsReg[7]))||date.equals(parseDate(startsReg[7]))) {
                    c_curPer=7;checkRemainingTimeReg();
                }
                else if(date.before(parseDate(endsReg[8]))&&date.after(parseDate(startsReg[8]))||date.equals(parseDate(startsReg[8]))) {
                    c_curPer=8;checkRemainingTimeReg();
                }
                else if(date.before(parseDate(endsReg[9]))&&date.after(parseDate(startsReg[9]))||date.equals(parseDate(startsReg[9]))) {
                    c_curPer=9;checkRemainingTimeReg();
                }
                //Passing period times (c_curPer to specified period)
                else if(date.after(parseDate(endsReg[0]))&&date.before(parseDate(startsReg[1]))||date.equals(parseDate(endsReg[0]))) {
                    c_curPer=11;checkRemainingTimePassReg();
                }
                else if(date.after(parseDate(endsReg[1]))&&date.before(parseDate(startsReg[2]))||date.equals(parseDate(endsReg[1]))) {
                    c_curPer=22;checkRemainingTimePassReg();
                }
                else if(date.after(parseDate(endsReg[2]))&&date.before(parseDate(startsReg[3]))||date.equals(parseDate(endsReg[2]))) {
                    c_curPer=33;checkRemainingTimePassReg();
                }
                else if(date.after(parseDate(endsReg[3]))&&date.before(parseDate(startsReg[4]))||date.equals(parseDate(endsReg[3]))) {
                    c_curPer=44;checkRemainingTimePassReg();
                }
                else if(date.after(parseDate(endsReg[4]))&&date.before(parseDate(startsReg[5]))||date.equals(parseDate(endsReg[4]))) {
                    c_curPer=55;checkRemainingTimePassReg();
                }
                else if(date.after(parseDate(endsReg[5]))&&date.before(parseDate(startsReg[6]))||date.equals(parseDate(endsReg[5]))) {
                    c_curPer=66;checkRemainingTimePassReg();
                }
                else if(date.after(parseDate(endsReg[6]))&&date.before(parseDate(startsReg[7]))||date.equals(parseDate(endsReg[6]))) {
                    c_curPer=77;checkRemainingTimePassReg();
                }
                else if(date.after(parseDate(endsReg[7]))&&date.before(parseDate(startsReg[8]))||date.equals(parseDate(endsReg[7]))) {
                    c_curPer=88;checkRemainingTimePassReg();
                }
                else if(date.after(parseDate(endsReg[8]))&&date.before(parseDate(startsReg[9]))||date.equals(parseDate(endsReg[8]))) {
                    c_curPer=99;checkRemainingTimePassReg();
                }
                //Pre-school start - log at 30min prev.
                else if(date.before(parseDate(startsReg[0]))&&date.after(parseDate(startReg_PRE))||date.equals(parseDate(startReg_PRE))){
                    c_curPer=998; checkTimePreSessionReg();
                }
                else
                    c_curPer = -1;
                break;
            case 1: break; //assembly schedule, not set dates for assemblies
            case 2:
                //Late start times
                if(date.before(parseDate(endLt[0]))&&date.after(parseDate(startLt[0]))||date.equals(parseDate(startLt[0]))) {
                    c_curPer=0;checkRemainingTimeLt();
                }
                else if(date.before(parseDate(endLt[1]))&&date.after(parseDate(startLt[1]))||date.equals(parseDate(startLt[1]))) {
                    c_curPer=1;checkRemainingTimeLt();
                }
                else if(date.before(parseDate(endLt[2]))&&date.after(parseDate(startLt[2]))||date.equals(parseDate(startLt[2]))) {
                    c_curPer=2;checkRemainingTimeLt();
                }
                else if(date.before(parseDate(endLt[3]))&&date.after(parseDate(startLt[3]))||date.equals(parseDate(startLt[3]))) {
                    c_curPer=3;checkRemainingTimeLt();
                }
                else if(date.before(parseDate(endLt[4]))&&date.after(parseDate(startLt[4]))||date.equals(parseDate(startLt[4]))) {
                    c_curPer=4;checkRemainingTimeLt();
                }
                else if(date.before(parseDate(endLt[5]))&&date.after(parseDate(startLt[5]))||date.equals(parseDate(startLt[5]))) {
                    c_curPer=5;checkRemainingTimeLt();
                }
                else if(date.before(parseDate(endLt[6]))&&date.after(parseDate(startLt[6]))||date.equals(parseDate(startLt[6]))) {
                    c_curPer=6;checkRemainingTimeLt();
                }
                else if(date.before(parseDate(endLt[7]))&&date.after(parseDate(startLt[7]))||date.equals(parseDate(startLt[7]))) {
                    c_curPer=7;checkRemainingTimeLt();
                }
                else if(date.before(parseDate(endLt[8]))&&date.after(parseDate(startLt[8]))||date.equals(parseDate(startLt[8]))) {
                    c_curPer=8;checkRemainingTimeLt();
                }
                //Passing period times (c_curPer to specified period)
                else if(date.after(parseDate(endLt[0]))&&date.before(parseDate(startLt[1]))||date.equals(parseDate(endLt[0]))) {
                    c_curPer=11;checkRemainingTimePassLt();
                }
                else if(date.after(parseDate(endLt[1]))&&date.before(parseDate(startLt[2]))||date.equals(parseDate(endLt[1]))) {
                    c_curPer=22;checkRemainingTimePassLt();
                }
                else if(date.after(parseDate(endLt[2]))&&date.before(parseDate(startLt[3]))||date.equals(parseDate(endLt[2]))) {
                    c_curPer=33;checkRemainingTimePassLt();
                }
                else if(date.after(parseDate(endLt[3]))&&date.before(parseDate(startLt[4]))||date.equals(parseDate(endLt[3]))) {
                    c_curPer=44;checkRemainingTimePassLt();
                }
                else if(date.after(parseDate(endLt[4]))&&date.before(parseDate(startLt[5]))||date.equals(parseDate(endLt[4]))) {
                    c_curPer=55;checkRemainingTimePassLt();
                }
                else if(date.after(parseDate(endLt[5]))&&date.before(parseDate(startLt[6]))||date.equals(parseDate(endLt[5]))) {
                    c_curPer=66;checkRemainingTimePassLt();
                }
                else if(date.after(parseDate(endLt[6]))&&date.before(parseDate(startLt[7]))||date.equals(parseDate(endLt[6]))) {
                    c_curPer=77;checkRemainingTimePassLt();
                }
                else if(date.after(parseDate(endLt[7]))&&date.before(parseDate(startLt[8]))||date.equals(parseDate(endLt[7]))) {
                    c_curPer=88;checkRemainingTimePassLt();
                }
                //Pre-school start - log at 30min prev.
                else if(date.before(parseDate(startLt[0]))&&date.after(parseDate(startLt_PRE))||date.equals(parseDate(startLt_PRE))){
                    c_curPer=998; checkTimePreSessionLt();
                }
                else
                    c_curPer = -1;
                break;
            case 3:
                //Minimum day times
                if(date.before(parseDate(endMin[0]))&&date.after(parseDate(startMin[0]))||date.equals(parseDate(startMin[0]))) {
                    c_curPer=0;checkRemainingTimeMin();
                }
                else if(date.before(parseDate(endMin[1]))&&date.after(parseDate(startMin[1]))||date.equals(parseDate(startMin[1]))) {
                    c_curPer=1;checkRemainingTimeMin();
                }
                else if(date.before(parseDate(endMin[2]))&&date.after(parseDate(startMin[2]))||date.equals(parseDate(startMin[2]))) {
                    c_curPer=2;checkRemainingTimeMin();
                }
                else if(date.before(parseDate(endMin[3]))&&date.after(parseDate(startMin[3]))||date.equals(parseDate(startMin[3]))) {
                    c_curPer=3;checkRemainingTimeMin();
                }
                else if(date.before(parseDate(endMin[4]))&&date.after(parseDate(startMin[4]))||date.equals(parseDate(startMin[4]))) {
                    c_curPer=4;checkRemainingTimeMin();
                }
                else if(date.before(parseDate(endMin[5]))&&date.after(parseDate(startMin[5]))||date.equals(parseDate(startMin[5]))) {
                    c_curPer=5;checkRemainingTimeMin();
                }
                else if(date.before(parseDate(endMin[6]))&&date.after(parseDate(startMin[6]))||date.equals(parseDate(startMin[6]))) {
                    c_curPer=6;checkRemainingTimeMin();
                }
                else if(date.before(parseDate(endMin[7]))&&date.after(parseDate(startMin[7]))||date.equals(parseDate(startMin[7]))) {
                    c_curPer=7;checkRemainingTimeMin();
                }
                //Passing period times (c_curPer to specified period)
                else if(date.after(parseDate(endMin[0]))&&date.before(parseDate(startMin[1]))||date.equals(parseDate(endMin[0]))) {
                    c_curPer=11;checkRemainingTimePassMin();
                }
                else if(date.after(parseDate(endMin[1]))&&date.before(parseDate(startMin[2]))||date.equals(parseDate(endMin[1]))) {
                    c_curPer=22;checkRemainingTimePassMin();
                }
                else if(date.after(parseDate(endMin[2]))&&date.before(parseDate(startMin[3]))||date.equals(parseDate(endMin[2]))) {
                    c_curPer=33;checkRemainingTimePassMin();
                }
                else if(date.after(parseDate(endMin[3]))&&date.before(parseDate(startMin[4]))||date.equals(parseDate(endMin[3]))) {
                    c_curPer=44;checkRemainingTimePassMin();
                }
                else if(date.after(parseDate(endMin[4]))&&date.before(parseDate(startMin[5]))||date.equals(parseDate(endMin[4]))) {
                    c_curPer=55;checkRemainingTimePassMin();
                }
                else if(date.after(parseDate(endMin[5]))&&date.before(parseDate(startMin[6]))||date.equals(parseDate(endMin[5]))) {
                    c_curPer=66;checkRemainingTimePassMin();
                }
                else if(date.after(parseDate(endMin[6]))&&date.before(parseDate(startMin[7]))||date.equals(parseDate(endMin[6]))) {
                    c_curPer=77;checkRemainingTimePassMin();
                }
                //Pre-school start - log at 30min prev.
                else if(date.before(parseDate(startMin[0]))&&date.after(parseDate(startMin_PRE))||date.equals(parseDate(startMin_PRE))){
                    c_curPer=998; checkTimePreSessionMin();
                }
                else
                    c_curPer = -1;
                break;
            case 4:
            case 5:
            case 6:
                //final times
                if(date.before(parseDate(endFinals[0]))&&date.after(parseDate(startFinals[0]))||date.equals(parseDate(startFinals[0]))) {
                    c_curPer=0;checkRemainingTimeFin();
                }
                else if(date.before(parseDate(endFinals[1]))&&date.after(parseDate(startFinals[1]))||date.equals(parseDate(startFinals[1]))) {
                    c_curPer=1;checkRemainingTimeFin();
                }
                else if(date.before(parseDate(endFinals[2]))&&date.after(parseDate(startFinals[2]))||date.equals(parseDate(startFinals[2]))) {
                    c_curPer=2;checkRemainingTimeFin();
                }
                //Passing period times (c_curPer to specified period)
                else if(date.after(parseDate(endFinals[0]))&&date.before(parseDate(startFinals[1]))||date.equals(parseDate(endFinals[0]))) {
                    c_curPer=11;checkRemainingTimePassFin();
                }
                else if(date.after(parseDate(endFinals[1]))&&date.before(parseDate(startFinals[2]))||date.equals(parseDate(endFinals[1]))) {
                    c_curPer=22;checkRemainingTimePassFin();
                }
                //Pre-school start - log at 30min prev.
                else if(date.before(parseDate(startFinals[0]))&&date.after(parseDate(startFinals_PRE))||date.equals(parseDate(startFinals_PRE))){
                    c_curPer=998; checkTimePreSessionFin();
                }
                else
                    c_curPer = -1;
                break;
            case 7:
                c_curPer=-1;
                break;
            case 8:
                c_curPer=-1;
                break;
        }
    }

    private Date parseDate(String date){
        try{
            return inputParser.parse(date);
        }catch (java.text.ParseException e){
            return new Date(0);
        }
    }

    private boolean updateContentText(String timeString, String updateString){
//        notificationBuilder.setContentText(Html.fromHtml("<b>"+timeString+"</b> " + updateString));
//        mNotificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
        return true;
    }

    //Regular
    private boolean checkRemainingTimeReg(){
        final int _curPer=c_curPer;
        //Has to be re-instantiated, or decl. final
        timeDifference = (parseDate(endsReg[_curPer]).getTime() / 60000 - date.getTime() / 60000);
        c_perLngth = (int) (parseDate(endsReg[_curPer]).getTime() / 60000 - parseDate(startsReg[_curPer]).getTime() / 60000);
        Log.d("COMPARE", "timedifferenceREG = "+Long.toString(timeDifference));
        Log.d("COMPARE", "c_perLngth = "+Integer.toString(c_perLngth));
        return true;
    }

    private boolean checkRemainingTimePassReg(){
        final int _curPer = c_curPer/11;
        timeDifference = (parseDate(startsReg[_curPer]).getTime() / 60000 - date.getTime() / 60000);
        c_perLngth = (int) (parseDate(startsReg[_curPer]).getTime() / 60000 - parseDate(endsReg[_curPer-1]).getTime() / 60000);
        Log.d("COMPARE", "timedifferenceREG_PASS = "+Long.toString(timeDifference));
        Log.d("COMPARE", "c_perLngth = "+Integer.toString(c_perLngth));
        return true;
    }

    //LT
    private boolean checkRemainingTimeLt(){
        final int _curPer=c_curPer;
        //Has to be re-instantiated, or decl. final
        timeDifference = (parseDate(endLt[_curPer]).getTime() / 60000 - date.getTime() / 60000);
        c_perLngth = (int) (parseDate(endLt[_curPer]).getTime() / 60000 - parseDate(startLt[_curPer]).getTime() / 60000);
        Log.d("COMPARE", "timedifferenceREG = "+Long.toString(timeDifference));
        Log.d("COMPARE", "c_perLngth = "+Integer.toString(c_perLngth));
        return true;
    }

    private boolean checkRemainingTimePassLt(){
        final int _curPer = c_curPer/11;
        timeDifference = (parseDate(startLt[_curPer]).getTime() / 60000 - date.getTime() / 60000);
        c_perLngth = (int) (parseDate(startLt[_curPer]).getTime() / 60000 - parseDate(endLt[_curPer-1]).getTime() / 60000);
        Log.d("COMPARE", "timedifferenceREG_PASS = "+Long.toString(timeDifference));
        Log.d("COMPARE", "c_perLngth = "+Integer.toString(c_perLngth));
        return true;
    }

    //Minimum day
    private boolean checkRemainingTimeMin(){
        final int _curPer=c_curPer;
        //Has to be re-instantiated, or decl. final
        timeDifference = (parseDate(endMin[_curPer]).getTime() / 60000 - date.getTime() / 60000);
        c_perLngth = (int) (parseDate(endMin[_curPer]).getTime() / 60000 - parseDate(startMin[_curPer]).getTime() / 60000);
        Log.d("COMPARE", "timedifferenceREG = "+Long.toString(timeDifference));
        Log.d("COMPARE", "c_perLngth = "+Integer.toString(c_perLngth));
        return true;
    }

    private boolean checkRemainingTimePassMin(){
        final int _curPer = c_curPer/11;
        timeDifference = (parseDate(startMin[_curPer]).getTime() / 60000 - date.getTime() / 60000);
        c_perLngth = (int) (parseDate(startMin[_curPer]).getTime() / 60000 - parseDate(endMin[_curPer-1]).getTime() / 60000);
        Log.d("COMPARE", "timedifferenceREG_PASS = "+Long.toString(timeDifference));
        Log.d("COMPARE", "c_perLngth = "+Integer.toString(c_perLngth));
        return true;
    }

    //Finals
    private boolean checkRemainingTimeFin(){
        final int _curPer=c_curPer;
        //Has to be re-instantiated, or decl. final
        timeDifference = (parseDate(endFinals[_curPer]).getTime() / 60000 - date.getTime() / 60000);
        c_perLngth = (int) (parseDate(endFinals[_curPer]).getTime() / 60000 - parseDate(startFinals[_curPer]).getTime() / 60000);
        Log.d("COMPARE", "timedifferenceREG = "+Long.toString(timeDifference));
        Log.d("COMPARE", "c_perLngth = "+Integer.toString(c_perLngth));
        return true;
    }

    private boolean checkRemainingTimePassFin(){
        final int _curPer = c_curPer/11;
        timeDifference = (parseDate(startFinals[_curPer]).getTime() / 60000 - date.getTime() / 60000);
        c_perLngth = (int) (parseDate(startFinals[_curPer]).getTime() / 60000 - parseDate(endFinals[_curPer-1]).getTime() / 60000);
        Log.d("COMPARE", "timedifferenceREG_PASS = "+Long.toString(timeDifference));
        Log.d("COMPARE", "c_perLngth = "+Integer.toString(c_perLngth));
        return true;
    }

    //Pre-sessions
    private boolean checkTimePreSessionReg(){
        final int _curPer = c_curPer;
        timeDifference = (parseDate(startsReg[0]).getTime() / 60000 - date.getTime() / 60000);
        c_perLngth = PRE_CLASS_CONSTANT_LNGTH;
        Log.d("COMPARE", "timedifferenceTUT_PASS = "+Long.toString(timeDifference));
        Log.d("COMPARE", "c_perLngth = "+Integer.toString(c_perLngth));
        return true;
    }

    private boolean checkTimePreSessionLt(){
        final int _curPer = c_curPer;
        timeDifference = (parseDate(startLt[0]).getTime() / 60000 - date.getTime() / 60000);
        c_perLngth = PRE_CLASS_CONSTANT_LNGTH;
        Log.d("COMPARE", "timedifferenceTUT_PASS = "+Long.toString(timeDifference));
        Log.d("COMPARE", "c_perLngth = "+Integer.toString(c_perLngth));
        return true;
    }

    private boolean checkTimePreSessionMin(){
        final int _curPer = c_curPer;
        timeDifference = (parseDate(startMin[0]).getTime() / 60000 - date.getTime() / 60000);
        c_perLngth = PRE_CLASS_CONSTANT_LNGTH;
        Log.d("COMPARE", "timedifferenceTUT_PASS = "+Long.toString(timeDifference));
        Log.d("COMPARE", "c_perLngth = "+Integer.toString(c_perLngth));
        return true;
    }

    private boolean checkTimePreSessionFin(){
        final int _curPer = c_curPer;
        timeDifference = (parseDate(startFinals[0]).getTime() / 60000 - date.getTime() / 60000);
        c_perLngth = PRE_CLASS_CONSTANT_LNGTH;
        Log.d("COMPARE", "timedifferenceTUT_PASS = "+Long.toString(timeDifference));
        Log.d("COMPARE", "c_perLngth = "+Integer.toString(c_perLngth));
        return true;
    }

    @Override
    public IBinder onBind(Intent arg0) {
        //Log.i(TAG, "Service onBind");
        return null;
    }

    //Eliminates deprecation of onStart method
    @Override
    public synchronized int onStartCommand(Intent intent, int flags, int startId) {
        if (!updater.isRunning() && intent!=null && getApplicationContext()!=null) {
            updater.start();
            Log.d("acd", "Updater Started");
            updater.isRunning = true;
            serviceState=true;
            intent.putExtra("serviceRestartState", serviceState);
            getApplicationContext().sendBroadcast(intent);
            serviceState=false;
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        /*if (updater.isRunning) {
            updater.interrupt();
            Log.d("acd", "Updater Destroyed");
            updater.isRunning = false;
            updater = null;
            serviceState = false;
        }*/
    }

}
