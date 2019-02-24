package com.novallc.foothillappmobile.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.novallc.foothillappmobile.R;
import com.novallc.foothillappmobile.activity.DrawerHeaderUpdater;
import com.novallc.foothillappmobile.activity.WebViewAssets.BaseActivity;
import com.novallc.foothillappmobile.circularprogress.ArcProgress;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class OverviewFragment extends Fragment{

    public static final String PACKAGE = "com.novallc.foothillappmobile.fragments.OverviewFragment";
    private TextView sdo_modeltext, sdo_modeltext0, sdo_modeltext1, sdo_modeltext2, sdo_modeltext3, sdo_modeltext4, sdo_modeltext5;
    private TextView scheduleview_classtext0, scheduleview_classtext1, scheduleview_classtext2, scheduleview_classtext3, scheduleview_classtext4, scheduleview_classtext5, scheduleview_classtext6;
    private TextView scheduleview_timetext0, scheduleview_timetext1, scheduleview_timetext2, scheduleview_timetext3, scheduleview_timetext4, scheduleview_timetext5, scheduleview_timetext6;
    private ArcProgress arcProgressView;
    private final String[] schedulearr_reg = {"6:48 AM - 7:40 AM", "7:45 AM - 8:37 AM", "8:42 AM - 9:34 AM", "9:49 AM - 10:41 AM", "11:16 AM - 12:08 PM", "12:13 PM - 1:05 PM", "1:40 PM - 2:32 PM"};
    private final String[] schedulearr_lte = {"8:40 AM - 9:20 AM", "9:25 AM - 10:05 AM", "10:10 AM - 10:50 AM", "11:05 AM - 11:48 AM", "11:53 AM - 12:33 PM", "12:38 PM - 1:18 PM", "1:53 PM - 2:33 PM"};
    private final String[] schedulearr_ass = {"6:45 AM - 7:40 AM", "7:45 AM - 8:32 AM", "8:37|9:37 AM - 9:27|10:27 AM", "10:44 AM - 11:31 AM", "11:36 AM - 12:23 PM", "12:28 PM - 1:14 PM", "1:49 PM - 2:35 PM"};
    private final String[] schedulearr_min = {"7:02 AM - 7:40 AM", "7:45 AM - 8:22 AM", "8:27 AM - 9:04 AM", "9:09 AM - 9:46 AM", "10:03 AM - 10:40 AM", "10:45 AM - 11:22 AM", "11:27 AM - 12:05 AM"};
    private final String[] schedulearr_fin = {"7:45 AM - 9:45 AM", "7:45 AM - 9:45 AM", "7:45 AM - 9:45 AM", "10:00 AM - 12:00 PM", "10:00 AM - 12:00 PM", "10:00 AM - 12:00 PM", "10:00 AM - 12:00 PM"};
    BroadcastReceiver mBroadcastReciever;
    Intent mUpdaterIntent;
    private Boolean[] mIsScheduleVoid = {false, false, false, false, false, false, false};
    private Boolean isProjectingDate = false;

    private ImageView soliddrawoutline;

    SimpleDateFormat mdFormat = new SimpleDateFormat("yyyy/MM/dd");


    private int schLOCAL = 0;
    private int c_curDay;
    private String c_curDate;

    public OverviewFragment() {
        // necessary empty public constructor
    }

    public static OverviewFragment newInstance(int index) {
        OverviewFragment f = new OverviewFragment();
        Bundle args = new Bundle();
        args.putInt("index", index);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final android.support.v7.app.ActionBar mActionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        mActionBar.setDisplayShowCustomEnabled(false);
        mActionBar.setElevation(0);
        final View root = inflater.inflate(R.layout.fragment_overview, container, false);
        final SharedPreferences mPrefs = getContext().getSharedPreferences(PACKAGE, Context.MODE_PRIVATE);
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        arcProgressView = (ArcProgress) root.findViewById(R.id.circular_progress);

        sdo_modeltext = (TextView)root.findViewById(R.id.sdo_model_text); sdo_modeltext0 = (TextView)root.findViewById(R.id.sdo_model0_text);
        sdo_modeltext1 = (TextView)root.findViewById(R.id.sdo_model1_text); sdo_modeltext2 = (TextView)root.findViewById(R.id.sdo_model2_text);
        sdo_modeltext3 = (TextView)root.findViewById(R.id.sdo_model3_text); sdo_modeltext4 = (TextView)root.findViewById(R.id.sdo_model4_text);
        sdo_modeltext5 = (TextView)root.findViewById(R.id.sdo_model5_text);
        soliddrawoutline = (ImageView)root.findViewById(R.id.soliddrawoutline);

        scheduleview_classtext0 = (TextView)root.findViewById(R.id.scheduleview_classtext0); scheduleview_classtext1 = (TextView)root.findViewById(R.id.scheduleview_classtext1);
        scheduleview_classtext2 = (TextView)root.findViewById(R.id.scheduleview_classtext2); scheduleview_classtext3 = (TextView)root.findViewById(R.id.scheduleview_classtext3);
        scheduleview_classtext4 = (TextView)root.findViewById(R.id.scheduleview_classtext4); scheduleview_classtext5 = (TextView)root.findViewById(R.id.scheduleview_classtext5);
        scheduleview_classtext6 = (TextView)root.findViewById(R.id.scheduleview_classtext6);

        scheduleview_timetext0 = (TextView)root.findViewById(R.id.scheduleview_timetext0); scheduleview_timetext1 = (TextView)root.findViewById(R.id.scheduleview_timetext1);
        scheduleview_timetext2 = (TextView)root.findViewById(R.id.scheduleview_timetext2); scheduleview_timetext3 = (TextView)root.findViewById(R.id.scheduleview_timetext3);
        scheduleview_timetext4 = (TextView)root.findViewById(R.id.scheduleview_timetext4); scheduleview_timetext5 = (TextView)root.findViewById(R.id.scheduleview_timetext5);
        scheduleview_timetext6 = (TextView)root.findViewById(R.id.scheduleview_timetext6);

        sdo_modeltext.setText(getProjectedDate(0)); sdo_modeltext0.setText(getProjectedDate(+1));
        sdo_modeltext1.setText(getProjectedDate(+2)); sdo_modeltext2.setText(getProjectedDate(+3));
        sdo_modeltext3.setText(getProjectedDate(-1)); sdo_modeltext4.setText(getProjectedDate(-2));
        sdo_modeltext5.setText(getProjectedDate(-3));

        //Drawable[] cmpDrawables_sdo = sdo_modeltext.getCompoundDrawables();
        //Bitmap bitmap = ((BitmapDrawable)cmpDrawables_sdo[1]).getBitmap();
        soliddrawoutline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetCmpDrawables();
                soliddrawoutline.setImageResource(R.drawable.ic_solidcircle_outline);
                sdo_modeltext.setTextColor(getResources().getColor(R.color.white));
                isProjectingDate = false;
                updateSubText(0);
            }
        });
        sdo_modeltext0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance(); resetCmpDrawables();
                sdo_modeltext0.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_solidcircle_edit, 0, 0);
                sdo_modeltext0.setTextColor(getResources().getColor(R.color.white));
                isProjectingDate = true;
                calendar.add(Calendar.DAY_OF_WEEK, 1);
                c_curDay = calendar.get(Calendar.DAY_OF_WEEK);
                c_curDate = mdFormat.format(calendar.getTime());
                updateSubText(0);
            }
        });
        sdo_modeltext1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance(); resetCmpDrawables();
                sdo_modeltext1.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_solidcircle_edit, 0, 0);
                sdo_modeltext1.setTextColor(getResources().getColor(R.color.white));
                isProjectingDate = true;
                calendar.add(Calendar.DAY_OF_WEEK, 2);
                c_curDay = calendar.get(Calendar.DAY_OF_WEEK);
                c_curDate = mdFormat.format(calendar.getTime());
                updateSubText(0);
            }
        });
        sdo_modeltext2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance(); resetCmpDrawables();
                sdo_modeltext2.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_solidcircle_edit, 0, 0);
                sdo_modeltext2.setTextColor(getResources().getColor(R.color.white));
                isProjectingDate = true;
                calendar.add(Calendar.DAY_OF_WEEK, 3);
                c_curDay = calendar.get(Calendar.DAY_OF_WEEK);
                c_curDate = mdFormat.format(calendar.getTime());
                updateSubText(0);
            }
        });
        sdo_modeltext3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance(); resetCmpDrawables();
                sdo_modeltext3.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_solidcircle_edit, 0, 0);
                sdo_modeltext3.setTextColor(getResources().getColor(R.color.white));
                isProjectingDate = true;
                calendar.add(Calendar.DAY_OF_WEEK, -1);
                c_curDay = calendar.get(Calendar.DAY_OF_WEEK);
                c_curDate = mdFormat.format(calendar.getTime());
                updateSubText(0);
            }
        });
        sdo_modeltext4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance(); resetCmpDrawables();
                sdo_modeltext4.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_solidcircle_edit, 0, 0);
                sdo_modeltext4.setTextColor(getResources().getColor(R.color.white));
                isProjectingDate = true;
                calendar.add(Calendar.DAY_OF_WEEK, -2);
                c_curDay = calendar.get(Calendar.DAY_OF_WEEK);
                c_curDate = mdFormat.format(calendar.getTime());
                updateSubText(0);
            }
        });
        sdo_modeltext5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance(); resetCmpDrawables();
                sdo_modeltext5.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_solidcircle_edit, 0, 0);
                sdo_modeltext5.setTextColor(getResources().getColor(R.color.white));
                isProjectingDate = true;
                calendar.add(Calendar.DAY_OF_WEEK, -3);
                c_curDay = calendar.get(Calendar.DAY_OF_WEEK);
                c_curDate = mdFormat.format(calendar.getTime());
                updateSubText(0);
            }
        });


        root.findViewById(R.id.btn_editschedulerow0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        getContext());
                final View editDialogView = getLayoutInflater(savedInstanceState).inflate(R.layout.dialog_et, null);
                final EditText input = (EditText) editDialogView.findViewById(R.id.et_dialog);
                final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                alertDialogBuilder.setTitle("Change Class Session Name");
                // set dialog message
                alertDialogBuilder
                        .setView(editDialogView)
                        .setCancelable(false)
                        .setNeutralButton("No 0 Period?", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mPrefs.edit().remove("clsSession0_text").apply();
                                mPrefs.edit().putString("clsSession0_text", " ").apply();

                                scheduleview_classtext0.setText(mPrefs.getString("clsSession0_text", null));
                                mIsScheduleVoid[0]=true;
                            }
                        })
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mPrefs.edit().remove("clsSession0_text").apply();
                                mPrefs.edit().putString("clsSession0_text", (!input.getText().toString().matches("^\\s*$")
                                        ? input.getText().toString()
                                        : getString(R.string.scheduleview_modeltext0))).apply();

                                scheduleview_classtext0.setText(mPrefs.getString("clsSession0_text", null));
                                mIsScheduleVoid[0]=false;
                            }
                        })
                        .setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });
                final AlertDialog alertDialog = alertDialogBuilder.show();
                alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                alertDialog.show();
                input.setFocusable(true);
                input.requestFocus();
            }
        });

        root.findViewById(R.id.btn_editschedulerow1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        getContext());
                final View editDialogView = getLayoutInflater(savedInstanceState).inflate(R.layout.dialog_et, null);
                final EditText input = (EditText) editDialogView.findViewById(R.id.et_dialog);
                final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                alertDialogBuilder.setTitle("Change Class Session Name");
                // set dialog message
                alertDialogBuilder
                        .setView(editDialogView)
                        .setCancelable(false)
                        .setNeutralButton("No 1st Period?", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mPrefs.edit().remove("clsSession1_text").apply();
                                mPrefs.edit().putString("clsSession1_text", " ").apply();

                                scheduleview_classtext1.setText(mPrefs.getString("clsSession1_text", null));
                                mIsScheduleVoid[1]=true;
                            }
                        })
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mPrefs.edit().remove("clsSession1_text").apply();
                                mPrefs.edit().putString("clsSession1_text", (!input.getText().toString().matches("^\\s*$")
                                        ? input.getText().toString()
                                        : getString(R.string.scheduleview_modeltext1))).apply();

                                scheduleview_classtext1.setText(mPrefs.getString("clsSession1_text", null));
                                mIsScheduleVoid[1]=false;

                            }
                        })
                        .setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });
                final AlertDialog alertDialog = alertDialogBuilder.show();
                alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                input.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean focused) {
                        /*if (focused) {
                            imm.showSoftInput(input, InputMethodManager.SHOW_IMPLICIT);
                        }*/
                    }
                });
                alertDialog.show();
                input.setFocusable(true);
                input.requestFocus();
            }
        });

        root.findViewById(R.id.btn_editschedulerow2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        getContext());
                final View editDialogView = getLayoutInflater(savedInstanceState).inflate(R.layout.dialog_et, null);
                final EditText input = (EditText) editDialogView.findViewById(R.id.et_dialog);
                final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                alertDialogBuilder.setTitle("Change Class Session Name");
                // set dialog message
                alertDialogBuilder
                        .setView(editDialogView)
                        .setCancelable(false)
                        .setNeutralButton("No 2nd Period?", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mPrefs.edit().remove("clsSession2_text").apply();
                                mPrefs.edit().putString("clsSession2_text", " ").apply();

                                scheduleview_classtext2.setText(mPrefs.getString("clsSession2_text", null));
                                mIsScheduleVoid[2]=true;
                            }
                        })
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mPrefs.edit().remove("clsSession2_text").apply();
                                mPrefs.edit().putString("clsSession2_text", (!input.getText().toString().matches("^\\s*$")
                                        ? input.getText().toString()
                                        : getString(R.string.scheduleview_modeltext2))).apply();

                                scheduleview_classtext2.setText(mPrefs.getString("clsSession2_text", null));
                                mIsScheduleVoid[2]=false;

                            }
                        })
                        .setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });
                final AlertDialog alertDialog = alertDialogBuilder.show();
                alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                input.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean focused) {
                        /*if (focused) {
                            imm.showSoftInput(input, InputMethodManager.SHOW_IMPLICIT);
                        }*/
                    }
                });
                alertDialog.show();
                input.setFocusable(true);
                input.requestFocus();
            }
        });

        root.findViewById(R.id.btn_editschedulerow3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        getContext());
                final View editDialogView = getLayoutInflater(savedInstanceState).inflate(R.layout.dialog_et, null);
                final EditText input = (EditText) editDialogView.findViewById(R.id.et_dialog);
                final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                alertDialogBuilder.setTitle("Change Class Session Name");
                // set dialog message
                alertDialogBuilder
                        .setView(editDialogView)
                        .setCancelable(false)
                        .setNeutralButton("No 3rd Period?", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mPrefs.edit().remove("clsSession3_text").apply();
                                mPrefs.edit().putString("clsSession3_text", " ").apply();

                                scheduleview_classtext3.setText(mPrefs.getString("clsSession3_text", null));
                                mIsScheduleVoid[3]=true;
                            }
                        })
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mPrefs.edit().remove("clsSession3_text").apply();
                                mPrefs.edit().putString("clsSession3_text", (!input.getText().toString().matches("^\\s*$")
                                        ? input.getText().toString()
                                        : getString(R.string.scheduleview_modeltext3))).apply();

                                scheduleview_classtext3.setText(mPrefs.getString("clsSession3_text", null));
                                mIsScheduleVoid[3]=false;

                            }
                        })
                        .setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });
                final AlertDialog alertDialog = alertDialogBuilder.show();
                alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                input.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean focused) {
                        /*if (focused) {
                            imm.showSoftInput(input, InputMethodManager.SHOW_IMPLICIT);
                        }*/
                    }
                });
                alertDialog.show();
                input.setFocusable(true);
                input.requestFocus();
            }
        });

        root.findViewById(R.id.btn_editschedulerow4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        getContext());
                final View editDialogView = getLayoutInflater(savedInstanceState).inflate(R.layout.dialog_et, null);
                final EditText input = (EditText) editDialogView.findViewById(R.id.et_dialog);
                final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                alertDialogBuilder.setTitle("Change Class Session Name");
                // set dialog message
                alertDialogBuilder
                        .setView(editDialogView)
                        .setCancelable(false)
                        .setNeutralButton("No 4th Period?", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mPrefs.edit().remove("clsSession4_text").apply();
                                mPrefs.edit().putString("clsSession4_text", " ").apply();

                                scheduleview_classtext4.setText(mPrefs.getString("clsSession4_text", null));
                                mIsScheduleVoid[4]=true;
                            }
                        })
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mPrefs.edit().remove("clsSession4_text").apply();
                                mPrefs.edit().putString("clsSession4_text", (!input.getText().toString().matches("^\\s*$")
                                        ? input.getText().toString()
                                        : getString(R.string.scheduleview_modeltext4))).apply();

                                scheduleview_classtext4.setText(mPrefs.getString("clsSession4_text", null));
                                mIsScheduleVoid[4]=false;

                            }
                        })
                        .setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });
                final AlertDialog alertDialog = alertDialogBuilder.show();
                alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                input.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean focused) {
                        /*if (focused) {
                            imm.showSoftInput(input, InputMethodManager.SHOW_IMPLICIT);
                        }*/
                    }
                });
                alertDialog.show();
                input.setFocusable(true);
                input.requestFocus();
            }
        });
        root.findViewById(R.id.btn_editschedulerow5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        getContext());
                final View editDialogView = getLayoutInflater(savedInstanceState).inflate(R.layout.dialog_et, null);
                final EditText input = (EditText) editDialogView.findViewById(R.id.et_dialog);
                final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                alertDialogBuilder.setTitle("Change Class Session Name");
                // set dialog message
                alertDialogBuilder
                        .setView(editDialogView)
                        .setCancelable(false)
                        .setNeutralButton("No 5th Period?", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mPrefs.edit().remove("clsSession5_text").apply();
                                mPrefs.edit().putString("clsSession5_text", " ").apply();

                                scheduleview_classtext5.setText(mPrefs.getString("clsSession5_text", null));
                                mIsScheduleVoid[5]=true;
                            }
                        })
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mPrefs.edit().remove("clsSession5_text").apply();
                                mPrefs.edit().putString("clsSession5_text", (!input.getText().toString().matches("^\\s*$")
                                        ? input.getText().toString()
                                        : getString(R.string.scheduleview_modeltext5))).apply();

                                scheduleview_classtext5.setText(mPrefs.getString("clsSession5_text", null));
                                mIsScheduleVoid[5]=false;

                            }
                        })
                        .setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });
                final AlertDialog alertDialog = alertDialogBuilder.show();
                alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                input.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean focused) {
                        /*if (focused) {
                            imm.showSoftInput(input, InputMethodManager.SHOW_IMPLICIT);
                        }*/
                    }
                });
                alertDialog.show();
                input.setFocusable(true);
                input.requestFocus();
            }
        });

        root.findViewById(R.id.btn_editschedulerow6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        getContext());
                final View editDialogView = getLayoutInflater(savedInstanceState).inflate(R.layout.dialog_et, null);
                final EditText input = (EditText) editDialogView.findViewById(R.id.et_dialog);
                final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                alertDialogBuilder.setTitle("Change Class Session Name");
                // set dialog message
                alertDialogBuilder
                        .setView(editDialogView)
                        .setCancelable(false)
                        .setNeutralButton("No 6th Period?", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mPrefs.edit().remove("clsSession6_text").apply();
                                mPrefs.edit().putString("clsSession6_text", " ").apply();

                                scheduleview_classtext6.setText(mPrefs.getString("clsSession6_text", null));
                                mIsScheduleVoid[6]=true;
                            }
                        })
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mPrefs.edit().remove("clsSession6_text").apply();
                                mPrefs.edit().putString("clsSession6_text", (!input.getText().toString().matches("^\\s*$")
                                        ? input.getText().toString()
                                        : getString(R.string.scheduleview_modeltext6))).apply();

                                scheduleview_classtext6.setText(mPrefs.getString("clsSession6_text", null));
                                mIsScheduleVoid[6]=false;

                            }
                        })
                        .setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });
                final AlertDialog alertDialog = alertDialogBuilder.show();
                alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                input.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean focused) {
                        /*if (focused) {
                            imm.showSoftInput(input, InputMethodManager.SHOW_IMPLICIT);
                        }*/
                    }
                });
                alertDialog.show();
                input.setFocusable(true);
                input.requestFocus();
            }
        });

        scheduleview_classtext0.setText( ( (mPrefs.getString("clsSession0_text", null)!=null || mIsScheduleVoid[0]) )
                ? mPrefs.getString("clsSession0_text", null)
                : getString(R.string.scheduleview_modeltext0));

        scheduleview_classtext1.setText( ( (mPrefs.getString("clsSession1_text", null)!=null || mIsScheduleVoid[1]) )
                ? mPrefs.getString("clsSession1_text", null)
                : getString(R.string.scheduleview_modeltext1));

        scheduleview_classtext2.setText( ( (mPrefs.getString("clsSession2_text", null)!=null || mIsScheduleVoid[2]) )
                ? mPrefs.getString("clsSession2_text", null)
                : getString(R.string.scheduleview_modeltext2));

        scheduleview_classtext3.setText( ( (mPrefs.getString("clsSession3_text", null)!=null || mIsScheduleVoid[3]) )
                ? mPrefs.getString("clsSession3_text", null)
                : getString(R.string.scheduleview_modeltext3));

        scheduleview_classtext4.setText( ( (mPrefs.getString("clsSession4_text", null)!=null || mIsScheduleVoid[4]) )
                ? mPrefs.getString("clsSession4_text", null)
                : getString(R.string.scheduleview_modeltext4));

        scheduleview_classtext5.setText( ( (mPrefs.getString("clsSession5_text", null)!=null || mIsScheduleVoid[5]) )
                ? mPrefs.getString("clsSession5_text", null)
                : getString(R.string.scheduleview_modeltext5));

        scheduleview_classtext6.setText( ( (mPrefs.getString("clsSession6_text", null)!=null || mIsScheduleVoid[6]) )
                ? mPrefs.getString("clsSession6_text", null)
                : getString(R.string.scheduleview_modeltext6));



        root.findViewById(R.id.scheduleViewProgressInd).bringToFront();

        mBroadcastReciever = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int curPer = intent.getIntExtra("curPer", -1);
                int curSch = intent.getIntExtra("curSch", -1);
                int curLngth = intent.getIntExtra("curLngth", 60);
                long timeDifference = intent.getLongExtra("timeDif", -1);
                boolean serviceRestartState = intent.getBooleanExtra("serviceRestartState", false);

                if(serviceRestartState){
                    root.findViewById(R.id.scheduleViewProgressInd).setVisibility(View.VISIBLE);
                    return;
                }else
                    root.findViewById(R.id.scheduleViewProgressInd).setVisibility(View.GONE);

                int _1 = (int)timeDifference; float _2 = (float)_1/curLngth*100;
                int arcProgressPER = (int) _2;

                if (arcProgressView != null && !isProjectingDate){
                    if(timeDifference==0){
                        arcProgressView.setProgressText("");
                        arcProgressView.setProgress(0);
                    }else{
                        if(timeDifference>=100) arcProgressView.setTextSize(getResources().getDimension(R.dimen.tSize_arc_progress_small));
                        else arcProgressView.setTextSize(getResources().getDimension(R.dimen.tSize_arc_progress_large));
                        arcProgressView.setProgressText(Long.toString(timeDifference));
                        arcProgressView.setProgress(arcProgressPER);
                    }

                    updateSubText(curSch);
                }
            }
        };

        mUpdaterIntent = new Intent(getContext(),
                DrawerHeaderUpdater.class);
        getContext().startService(mUpdaterIntent);

        getContext().registerReceiver(mBroadcastReciever, new IntentFilter(
                DrawerHeaderUpdater.BROADCAST_ACTION));
        return root;
    }

    private String getProjectedDate(int _prj){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE", Locale.US);
        cal.add(Calendar.DATE, _prj);
        return cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.US).toUpperCase();
    }

    public static float dpToPx(Context context, float valueInDp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics);
    }

    private void updateSubText(int _sch){
        this.schLOCAL = _sch;
        try {
            if(!isProjectingDate) {
                Calendar calendar = Calendar.getInstance();
                c_curDay = calendar.get(Calendar.DAY_OF_WEEK);
                c_curDate = mdFormat.format(calendar.getTime());
            }

            switch(c_curDay){
                case Calendar.MONDAY:
                case Calendar.TUESDAY:
                case Calendar.WEDNESDAY:
                case Calendar.THURSDAY:
                case Calendar.FRIDAY:
                    schLOCAL=0;
                    break;
                default:
                    schLOCAL=7;
                    break;
            }
            if(Arrays.asList(DrawerHeaderUpdater.ltConstantDates).contains(c_curDate))
                schLOCAL = 2;
            else if(Arrays.asList(DrawerHeaderUpdater.minConstantDates).contains(c_curDate))
                schLOCAL = 3;
            else if(Arrays.asList(DrawerHeaderUpdater.holConstantDates).contains(c_curDate))
                schLOCAL = 8;
            else if(mdFormat.parse(c_curDate).after(mdFormat.parse(DrawerHeaderUpdater.summConstantDates[0]))
                    && mdFormat.parse(c_curDate).before(mdFormat.parse(DrawerHeaderUpdater.summConstantDates[1])))
                schLOCAL = 7;
            else if(Arrays.asList(DrawerHeaderUpdater.finConstantDates0).contains(c_curDate))
                schLOCAL = 4;
            else if(Arrays.asList(DrawerHeaderUpdater.finConstantDates1).contains(c_curDate))
                schLOCAL = 5;
            else if(Arrays.asList(DrawerHeaderUpdater.finConstantDates2).contains(c_curDate))
                schLOCAL = 6;

            switch (schLOCAL){
                case 0:
                    scheduleview_timetext0.setText(schedulearr_reg[0]);scheduleview_timetext1.setText(schedulearr_reg[1]);scheduleview_timetext2.setText(schedulearr_reg[2]);scheduleview_timetext3.setText(schedulearr_reg[3]);scheduleview_timetext4.setText(schedulearr_reg[4]);scheduleview_timetext5.setText(schedulearr_reg[5]);scheduleview_timetext6.setText(schedulearr_reg[6]);
                    break;
                case 2:
                    scheduleview_timetext0.setText(schedulearr_lte[0]);scheduleview_timetext1.setText(schedulearr_lte[1]);scheduleview_timetext2.setText(schedulearr_lte[2]);scheduleview_timetext3.setText(schedulearr_lte[3]);scheduleview_timetext4.setText(schedulearr_lte[4]);scheduleview_timetext5.setText(schedulearr_lte[5]);scheduleview_timetext6.setText(schedulearr_lte[6]);
                    break;
                case 3:
                    scheduleview_timetext0.setText(schedulearr_min[0]);scheduleview_timetext1.setText(schedulearr_min[1]);scheduleview_timetext2.setText(schedulearr_min[2]);scheduleview_timetext3.setText(schedulearr_min[3]);scheduleview_timetext4.setText(schedulearr_min[4]);scheduleview_timetext5.setText(schedulearr_min[5]);scheduleview_timetext6.setText(schedulearr_min[6]);
                    break;
                case 4:
                case 5:
                case 6:
                    scheduleview_timetext0.setText(schedulearr_fin[0]);scheduleview_timetext1.setText(schedulearr_fin[1]);scheduleview_timetext2.setText(schedulearr_fin[2]);scheduleview_timetext3.setText(schedulearr_fin[3]);scheduleview_timetext4.setText(schedulearr_fin[4]);scheduleview_timetext5.setText(schedulearr_fin[5]);scheduleview_timetext6.setText(schedulearr_fin[6]);
                    break;
                default:
                    scheduleview_timetext0.setText("");scheduleview_timetext1.setText("");scheduleview_timetext2.setText("");scheduleview_timetext3.setText("");scheduleview_timetext4.setText("");scheduleview_timetext5.setText("");scheduleview_timetext6.setText("");
                    break;
            }
        }catch (ParseException e){
            e.printStackTrace();
        }
    }

    private void resetCmpDrawables(){
        soliddrawoutline.setImageResource(R.drawable.ic_solidcircle_outline_edit);
        sdo_modeltext0.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_solidcircle, 0, 0);
        sdo_modeltext1.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_solidcircle, 0, 0);
        sdo_modeltext2.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_solidcircle, 0, 0);
        sdo_modeltext3.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_solidcircle, 0, 0);
        sdo_modeltext4.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_solidcircle, 0, 0);
        sdo_modeltext5.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_solidcircle, 0, 0);

        sdo_modeltext.setTextColor(getResources().getColor(R.color.dim_white));
        sdo_modeltext0.setTextColor(getResources().getColor(R.color.dim_white));
        sdo_modeltext1.setTextColor(getResources().getColor(R.color.dim_white));
        sdo_modeltext2.setTextColor(getResources().getColor(R.color.dim_white));
        sdo_modeltext3.setTextColor(getResources().getColor(R.color.dim_white));
        sdo_modeltext4.setTextColor(getResources().getColor(R.color.dim_white));
        sdo_modeltext5.setTextColor(getResources().getColor(R.color.dim_white));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getContext().unregisterReceiver(mBroadcastReciever);

        Log.d("CHECK", "Unregister Reciever... OverviewFragment.class");
    }
}