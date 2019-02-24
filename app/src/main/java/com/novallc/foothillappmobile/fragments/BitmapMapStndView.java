package com.novallc.foothillappmobile.fragments;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.novallc.foothillappmobile.R;
import com.novallc.foothillappmobile.activity.MainActivity;
import com.novallc.foothillappmobile.activity.util.PinView;
import com.squareup.picasso.Picasso;

import java.util.Random;

public class BitmapMapStndView extends Fragment {

    private View inflatedView;
    private PinView pinView;
    private SharedPreferences mPrefs = null;
    private com.melnykov.fab.FloatingActionButton fab_undo;

    public BitmapMapStndView() {
        // necessary empty public constructor
    }

    public static BitmapMapStndView newInstance(int index) {
        BitmapMapStndView f = new BitmapMapStndView();
        Bundle args = new Bundle();
        args.putInt("index", index);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPrefs = getContext().getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        android.support.v7.app.ActionBar mActionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        mActionBar.setDisplayShowCustomEnabled(false);
        //TODO hide toolbar for more streamline fragment
        // Inflate the layout for this fragment
        inflatedView = inflater.inflate(R.layout.fragment_imageview_map, container, false);
        inflatedView.findViewById(R.id.progress_imageview_load).setVisibility(View.VISIBLE);
        fab_undo = (com.melnykov.fab.FloatingActionButton)inflatedView.findViewById(R.id.fab_undo);
        fab_undo.setVisibility(View.GONE);

        initialiseImage();
        pinView.setOnImageEventListener(new SubsamplingScaleImageView.OnImageEventListener() {
            @Override
            public void onReady() {
                float density = getResources().getDisplayMetrics().densityDpi;
                inflatedView.findViewById(R.id.progress_imageview_load).setVisibility(View.GONE);
                //final PinView pinView = (PinView)inflatedView.findViewById(R.id.imageView);
                if(getArguments().getBoolean("directed_lAdapter")){
                    fab_undo.setVisibility(View.VISIBLE);
                    float _xpos = getArguments().getFloat("x_pos"); float _ypos = getArguments().getFloat("y_pos");
                    if(pinView.isReady()) {
                        Log.d("CHECK", "pinView ready");
                        float maxScale = pinView.getMaxScale();
                        float minScale = pinView.getMinScale();
                        float scale = (float)(0.5 * (maxScale - minScale)) + minScale;
                        //float point coordinates of .svgs, comparative of ratio between source image and scaled image
                        float _xconst = 2.97f/* * pinView.getScale()*/;
                        float _yconst = 3f/* * pinView.getScale()*/;
                        PointF center = new PointF( (_xpos*_xconst/2975)*pinView.getSWidth() , ((_ypos*_yconst/2231)*pinView.getSHeight()) ); //_xpos * _xconst, _ypos * _yconst
                        if(_xpos!=0&&_ypos!=0) {
                            Log.d("CHECK", String.format("Width: %d, Height: %d", pinView.getSWidth(), pinView.getSHeight()));
                            pinView.setPin(center);
                            SubsamplingScaleImageView.AnimationBuilder animationBuilder = pinView.animateScaleAndCenter(scale, center);
                            animationBuilder.withInterruptible(false);
                            animationBuilder.withDuration(750).start();
                        }

                        //Toast.makeText(getActivity(), String.format("fX=%s fY=%s fScale=%s", Float.toString(pinView.getScaleX()), Float.toString(pinView.getScaleY()), Float.toString(pinView.getScale())), Toast.LENGTH_LONG).show();
                        Log.d("CHECK", String.format("fX=%s fY=%s fScale=%s", Float.toString(pinView.getScaleX()), Float.toString(pinView.getScaleY()), Float.toString(pinView.getScale())));
                    }
                    fab_undo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                Class fragmentClass = StickyListExpandableFragment.class;
                                final Fragment fragment = (Fragment) fragmentClass.newInstance();
                                MainActivity.mFragmentTransaction = MainActivity.mFragmentManager.beginTransaction();
                                MainActivity.mFragmentTransaction.replace(R.id.frame_container, fragment);
                                MainActivity.mFragmentTransaction.commit();
                                MainActivity.toolbar.setVisibility(View.VISIBLE);
                            }catch (Exception e){}
                        }
                    });
                }
            }

            @Override
            public void onImageLoaded() {

            }
            @Override
            public void onPreviewLoadError(Exception e) {
                Log.d("CHECK", "onPreviewLoadError: ");

            }

            @Override
            public void onImageLoadError(Exception e) {
                Log.d("CHECK", "onImageLoadError: ");

            }

            @Override
            public void onTileLoadError(Exception e) {
                Log.d("CHECK", "onTileLoadError: ");

            }
        });

        Button testButton = (Button)inflatedView.findViewById(R.id.testButton);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pinView.isReady()) {
                    float maxScale = pinView.getMaxScale();
                    float minScale = pinView.getMinScale();
                    float scale = (1 * (maxScale - minScale)) + minScale;
                    PointF center = new PointF((float)(2309), (float)(1125));
                    pinView.setPin(center);
                    SubsamplingScaleImageView.AnimationBuilder animationBuilder = pinView.animateScaleAndCenter(scale, center);
                    animationBuilder.withDuration(750).start();
                }
            }
        });

        /*imageView_map.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int x = (int) event.getX()*2;
                int y = (int) event.getY(); //imageView_map.getLeft/Right();

                if (event.getAction() == MotionEvent.ACTION_DOWN*//* &&
                        getLocationOnScreen(imageView_map).contains(x, y)*//*) {
                    Log.d("IMAGEVIEWTOUCH", String.format("X = '%d' | Y = '%d'", x, y));
                }
                return false;
            }
        });*/
        return inflatedView;
    }

    private void initialiseImage() {
        Bitmap testImage = BitmapFactory.decodeResource(this.getResources(), R.drawable.campus_map);
        testImage.setDensity(Bitmap.DENSITY_NONE);
        //Bitmap.createScaledBitmap(testImage, 1000, 750, true);
        pinView = (PinView) inflatedView.findViewById(R.id.imageView);
        pinView.setImage(ImageSource.bitmap(testImage)); //ImageSource.resource(R.drawable.campus_map) //ImageSource.bitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.campus_map))
        pinView.setPanLimit(SubsamplingScaleImageView.PAN_LIMIT_INSIDE);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mPrefs.getBoolean("isFirstRun", true)){
            //preload bitmaps with imagesource or imageloader
            final ImageView tutView0 = (ImageView) inflatedView.findViewById(R.id.imageTutMain);
            final ImageView tutView1 = (ImageView) inflatedView.findViewById(R.id.imageTapToDismiss);
            final View imageTutOverlay = inflatedView.findViewById(R.id.layoutInstructionOverlay);
            Picasso.with(getContext()).load(R.drawable.testoverlayimage).into(tutView0, new com.squareup.picasso.Callback(){
                @Override
                public void onSuccess() {
                    imageTutOverlay.setVisibility(View.VISIBLE);
                    imageTutOverlay.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            imageTutOverlay.setVisibility(View.GONE);
                            return true;
                        }
                    });
                }

                @Override
                public void onError() {

                }
            });
            Picasso.with(getContext()).load(R.drawable.taptodismiss).into(tutView1);

            mPrefs.edit().putBoolean("isFirstRun", false).apply();
        }
    }

    private Rect getLocationOnScreen(View mView) {
        Rect mRect = new Rect();
        int[] location = new int[2];

        mView.getLocationOnScreen(location);

        mRect.left = location[0];
        mRect.top = location[1];
        mRect.right = location[0] + mView.getWidth();
        mRect.bottom = location[1] + mView.getHeight();

        return mRect;
    }
}
