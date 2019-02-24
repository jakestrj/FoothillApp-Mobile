package com.novallc.foothillappmobile.fragments;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.CountDownTimer;
import android.preference.PreferenceActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.github.aakira.expandablelayout.ExpandableLayoutListener;
import com.github.aakira.expandablelayout.ExpandableLinearLayout;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.github.aakira.expandablelayout.ExpandableWeightLayout;
import com.nhaarman.listviewanimations.appearance.StickyListHeadersAdapterDecorator;
import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;
import com.nhaarman.listviewanimations.util.StickyListHeadersListViewWrapper;
import com.novallc.foothillappmobile.R;
import com.novallc.foothillappmobile.activity.ListViewAssets.Contact_Model;
import com.novallc.foothillappmobile.activity.ListViewAssets.ExpandableLayout;
import com.novallc.foothillappmobile.activity.ListViewAssets.ListAdapter;
import com.novallc.foothillappmobile.activity.ListViewAssets.OnExpandListener;
import com.novallc.foothillappmobile.activity.Login_anim;
import com.novallc.foothillappmobile.activity.MainActivity;
import com.novallc.foothillappmobile.searchviewanimations.JJSearchView;
import com.novallc.foothillappmobile.searchviewanimations.anim.controller.JJChangeArrowController;

import org.w3c.dom.Text;

import java.lang.reflect.Array;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;


public class StickyListExpandableFragment extends Fragment{

    private static final int ALPHA_ANIMATION_DELAY = 300;
    private Context mContext;
    public static View mFragmentView;
    private View mFragmentRow;
    private ListView mListView_temp;
    private boolean mLimited;
    private InputMethodManager imm;
    private boolean mJJreset = false;
    private View toolbar;
    private View mCActionbar;
    private SwipeLayout swipeLayout;

    ValueAnimator mAnimator;
    private StickyListHeadersListView listView;
    private EditText inputSearch;
    private ExpandableLayout expandableLayout_searchview;
    private RelativeLayout mParentExpandableSearchView;
    private InputMethodManager mIm;

    public static String[] t_names_model;
    public static String[] t_emaAddress_model;
    public static String[] t_clsLocation_model;
    public static String[] t_division_model;
    public static String[] t_description_model;
    public static String[] t_tag0_model;
    public static String[] t_tag1_model;

    public StickyListExpandableFragment() {
        // necessary empty public constructor
    }

    public static StickyListExpandableFragment newInstance(int index) {
        StickyListExpandableFragment f = new StickyListExpandableFragment();
        Bundle args = new Bundle();
        args.putInt("index", index);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
//        inflater.inflate(R.menu.main_listview_expandable, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final FrameLayout.LayoutParams mLayoutParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        Drawable img_search_icon = getResources().getDrawable(R.drawable.ic_search);
        //SwipeLayout swipeLayout = (SwipeLayout)mFragmentRow.findViewById(R.id.swipeLayout);
        /*if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            DrawableCompat.setTint(img_search_icon, getResources().getColor(R.color.yellow_accent));
        }*/
        switch (item.getItemId()){
            case R.id.item_search:
                //search anims here and handler
                if(expandableLayout_searchview.isExpanded()){
                    expandableLayout_searchview.collapse();
                    expandableLayout_searchview.setOnExpandListener(new OnExpandListener() {
                        @Override
                        public void onExpanded(ExpandableLayout view) {

                        }
                        @Override
                        public void onCollapsed(ExpandableLayout view) {
                            mParentExpandableSearchView.setPadding(0,0,0,0);
                            mLayoutParams.setMargins(0, 0, 0, 0);
                            listView.setLayoutParams(mLayoutParams);
                        }
                    });
                }else{
                    mParentExpandableSearchView.setPadding(30,24,30,24);
                    mLayoutParams.setMargins(0, 162, 0, 0);
                    listView.setLayoutParams(mLayoutParams);
                    expandableLayout_searchview.expand();
                    swipeLayout.close(true, false);
                }
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mFragmentView = inflater.inflate(R.layout.fragment_stickylistheaders, container, false);
        mFragmentRow = inflater.inflate(R.layout.list_row, container, false);
        listView = (StickyListHeadersListView) mFragmentView.findViewById(R.id.activity_stickylistheaders_listview);
        FrameLayout.LayoutParams mFrameParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        expandableLayout_searchview = (ExpandableLayout)mFragmentView.findViewById(R.id.expandableLayout_searchView);
        mParentExpandableSearchView = (RelativeLayout)mFragmentView.findViewById(R.id.mParentExpandableSearchView);

        final android.support.v7.app.ActionBar mActionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
//        mCActionbar = inflater.inflate(R.layout.stickylist_customactionbar, null);
        mActionBar.setCustomView(mCActionbar);
        mActionBar.setDisplayShowCustomEnabled(true);
        mActionBar.setDisplayShowTitleEnabled(false);
        mActionBar.setCustomView(R.layout.stickylist_customactionbar);
        mActionBar.setElevation(8);

        final View _customView = mActionBar.getCustomView();
        final EditText cab_editText = (EditText)_customView.findViewById(R.id.cab_edittext);
        final ImageButton imgB_clearTxt = (ImageButton)_customView.findViewById(R.id.imgB_clearTxt);
        final JJSearchView cab_JJSearchView = (JJSearchView) _customView.findViewById(R.id.cab_jjsv);
        final View cab_JJSearchView_modelclick = _customView.findViewById(R.id.cab_jjsv_modelclick);
        final TextView titleText_srcView = (TextView)_customView.findViewById(R.id.titleText_srcView);
        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        final Animation anim_transSrc = AnimationUtils.loadAnimation(getContext(), R.anim.translate_srcview_for);
        final Animation anim_transSrc_bck = AnimationUtils.loadAnimation(getContext(), R.anim.translate_srcview_bck);
        final Animation fadeIn = AnimationUtils.loadAnimation(getContext(), R.anim.fadein);
        final Animation fadeOut = AnimationUtils.loadAnimation(getContext(), R.anim.fadeout);

        cab_JJSearchView.setController(new JJChangeArrowController());
        titleText_srcView.setText(getResources().getString(R.string.drawer_item_second));

        //listView.setFitsSystemWindows(true);
        final ListAdapter adapter = new ListAdapter(getContext(), getActivity());
        AlphaInAnimationAdapter animationAdapter = new AlphaInAnimationAdapter(adapter);
        final StickyListHeadersAdapterDecorator stickyListHeadersAdapterDecorator = new StickyListHeadersAdapterDecorator(animationAdapter);
        stickyListHeadersAdapterDecorator.setListViewWrapper(new StickyListHeadersListViewWrapper(listView));
//        stickyListHeadersAdapterDecorator.getListViewWrapper().getListView();

        assert animationAdapter.getViewAnimator() != null;
        animationAdapter.getViewAnimator().setInitialDelayMillis(ALPHA_ANIMATION_DELAY);

        assert stickyListHeadersAdapterDecorator.getViewAnimator() != null;
        stickyListHeadersAdapterDecorator.getViewAnimator().setInitialDelayMillis(ALPHA_ANIMATION_DELAY);

        listView.setAdapter(stickyListHeadersAdapterDecorator);
        stickyListHeadersAdapterDecorator.notifyDataSetChanged();

        swipeLayout = (SwipeLayout) mFragmentRow.findViewById(R.id.swipeLayout);
        /*if(expandableLayout.isExpanded()){
            expandableLayout.collapse();
        }else{
            expandableLayout.expand();
        }
        expandableLayout.setOnExpandListener(new OnExpandListener() {
            @Override
            public void onExpanded(ExpandableLayout view) {
                if(position==listView.getCount()-1&&!(listView.getCount()==1)){
                    listView.smoothScrollToPosition(listView.getCount()-1);
                }
            }

            @Override
            public void onCollapsed(ExpandableLayout view) {

            }
        });*/
        swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
        swipeLayout.addDrag(SwipeLayout.DragEdge.Left, getActivity().findViewById(R.id.bottom_wrapper));
        swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
            @Override
            public void onClose(SwipeLayout layout) {
                //when the SurfaceView totally cover the BottomView.
            }

            @Override
            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {
                //you are swiping.
            }

            @Override
            public void onStartOpen(SwipeLayout layout) {
            }

            @Override
            public void onOpen(SwipeLayout layout) {
                //when the BottomView totally show.
            }

            @Override
            public void onStartClose(SwipeLayout layout) {
            }

            @Override
            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
                //when user's hand released.
            }
        });

        //Add onPreDrawListener
//        listView.getViewTreeObserver().addOnPreDrawListener(
//                new ViewTreeObserver.OnPreDrawListener() {
//                    @Override
//                    public boolean onPreDraw() {
//                        Log.d("CHECK", "onPreDraw: ");
//                        listView.getViewTreeObserver().removeOnPreDrawListener(this);
//                        toolbar.setVisibility(View.GONE);
//
//                        final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
//                        final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
//                        toolbar.measure(widthSpec, heightSpec);
//
//                        mAnimator = slideAnimator(0, toolbar.getMeasuredHeight());
//                        return true;
//                    }
//                });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                toolbar = view.findViewById(R.id.swipeLayout);
                //toggle_contents(view, toolbar, position);
            }
        });

        imgB_clearTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cab_editText.getText().clear();
            }
        });

        cab_JJSearchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mJJreset&&cab_JJSearchView.isClickable()) {
                    mJJreset = true;
                    cab_JJSearchView.startAnim();
                    cab_JJSearchView.startAnimation(anim_transSrc);
                    cab_JJSearchView.setClickable(false);
                    titleText_srcView.setVisibility(View.GONE);
                    MainActivity.result.getActionBarDrawerToggle().setDrawerIndicatorEnabled(false);

                    anim_transSrc.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            cab_JJSearchView.setClickable(false);
                            cab_editText.setVisibility(View.VISIBLE);
                            cab_editText.startAnimation(fadeIn);
                            cab_JJSearchView_modelclick.setVisibility(View.VISIBLE);
                            imgB_clearTxt.setVisibility(View.VISIBLE);
                            imgB_clearTxt.startAnimation(fadeIn);

                            cab_editText.requestFocus();
                            imm.showSoftInput(cab_editText, InputMethodManager.SHOW_IMPLICIT);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                }
            }
        });

        cab_JJSearchView_modelclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mJJreset&&cab_JJSearchView_modelclick.isClickable()) {
                    mJJreset = false;
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                    cab_JJSearchView.resetAnim();
                    cab_JJSearchView.startAnimation(anim_transSrc_bck);
                    cab_JJSearchView.setClickable(false);
                    cab_JJSearchView_modelclick.setVisibility(View.GONE);
                    cab_editText.setVisibility(View.GONE);
                    cab_editText.clearFocus();
                    cab_editText.setText("");
                    imgB_clearTxt.setVisibility(View.GONE);

                    anim_transSrc_bck.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            cab_JJSearchView.setClickable(true);
                            MainActivity.result.getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);
                            MainActivity.crossfadeDrawerLayout.addDrawerListener(setupDrawerToggle());
                            titleText_srcView.startAnimation(fadeIn);
                            titleText_srcView.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                }
            }
        });

        /*
         TODO scroll searchjs icon to
         */
        cab_editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
                listView.setSelectionAfterHeaderView();
            }

            @Override
            public void afterTextChanged(Editable s) {
                adapter.getFilter().filter(s);
                listView.setSelectionAfterHeaderView();
            }
        });

        //return inflated fragment
        return mFragmentView;
    }

    public ActionBarDrawerToggle setupDrawerToggle() {
        MainActivity mActivity = new MainActivity();
        return new ActionBarDrawerToggle(mActivity, MainActivity.crossfadeDrawerLayout, (Toolbar) getActivity().findViewById(R.id.toolbar), R.string.drawer_open,  R.string.drawer_close);
    }

    /**
     * onClick handler toggle view
     */
    public void toggle_contents(View _ctx, View v, final int position){
//        expandableLayout = (ExpandableRelativeLayout)v.findViewById(R.id.expandableLayout);
//        expandableLayout.toggle();
//        expandableLayout.setListener(new ExpandableLayoutListener() {
//            @Override
//            public void onAnimationStart() {}
//            @Override
//            public void onAnimationEnd() {
//                if(position==listView.getCount()-1){
//                    listView.smoothScrollToPosition(listView.getCount()-1);
//                }
//            }
//            @Override
//            public void onPreOpen() {}
//            @Override
//            public void onPreClose() {}
//            @Override
//            public void onOpened() {}
//            @Override
//            public void onClosed() {}
//        });

        ExpandableLayout expandableLayout = (ExpandableLayout)v.findViewById(R.id.expandableLayout);
        swipeLayout = (SwipeLayout)v.findViewById(R.id.swipeLayout);
        /*if(expandableLayout.isExpanded()){
            expandableLayout.collapse();
        }else{
            expandableLayout.expand();
        }
        expandableLayout.setOnExpandListener(new OnExpandListener() {
            @Override
            public void onExpanded(ExpandableLayout view) {
                if(position==listView.getCount()-1&&!(listView.getCount()==1)){
                    listView.smoothScrollToPosition(listView.getCount()-1);
                }
            }

            @Override
            public void onCollapsed(ExpandableLayout view) {

            }
        });*/
        swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
        swipeLayout.addDrag(SwipeLayout.DragEdge.Left, v.findViewById(R.id.bottom_wrapper));
        swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
            @Override
            public void onClose(SwipeLayout layout) {
                //when the SurfaceView totally cover the BottomView.
            }

            @Override
            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {
                //you are swiping.
                imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
            }

            @Override
            public void onStartOpen(SwipeLayout layout) {
            }

            @Override
            public void onOpen(SwipeLayout layout) {
                //when the BottomView totally show.
            }

            @Override
            public void onStartClose(SwipeLayout layout) {
            }

            @Override
            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
                //when user's hand released.
            }
        });
    }

    @Override
    public void onDestroy() {
        imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        MainActivity.result.getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        super.onDestroy();
    }

    public View getViewByPosition(StickyListHeadersListView listView, int pos) {
        try {
            final int firstListItemPosition = listView
                    .getFirstVisiblePosition();
            final int lastListItemPosition = firstListItemPosition
                    + listView.getChildCount() - 1;

            if (pos < firstListItemPosition || pos > lastListItemPosition) {
                //This may occur using Android Monkey, else will work otherwise
                return listView.getAdapter().getView(pos, null, listView);
            } else {
                final int childIndex = pos - firstListItemPosition;
                return listView.getChildAt(childIndex);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Calculate height of the list view by calling {@link android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)}
     * and measuring its height for all items in adapter
     * It also adds divider height for all items
     * */
    /*public static void setListViewHeightBasedOnChildren(StickyListHeadersListView listView, StickyListHeadersAdapterDecorator stickyListHeadersAdapterDecorator, int group) {
            int totalHeight = 0;
            int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                    View.MeasureSpec.EXACTLY);
            for (int i = 0; i < stickyListHeadersAdapterDecorator.getCount(); i++) {
                View groupItem = stickyListHeadersAdapterDecorator.getView(i, false, null, listView);
                groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

                totalHeight += groupItem.getMeasuredHeight();

                if (((listView.isGroupExpanded(i)) && (i != group))
                        || ((!listView.isGroupExpanded(i)) && (i == group))) {
                    for (int j = 0; j < stickyListHeadersAdapterDecorator.getCount(i); j++) {
                        View listItem = stickyListHeadersAdapterDecorator.getChildView(i, j, false, null,
                                listView);
                        listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

                        totalHeight += listItem.getMeasuredHeight();

                    }
                }
            }

            ViewGroup.LayoutParams params = listView.getLayoutParams();
            int height = totalHeight
                    + (listView.getDividerHeight() * (stickyListHeadersAdapterDecorator.getGroupCount() - 1));
            if (height < 10)
                height = 200;
            params.height = height;
            listView.setLayoutParams(params);
            listView.requestLayout();
    }*/

    /**
     *
     * @param ctx
     * @param v
     */
    public void slide_down(Context ctx, View v, final int position){

        Animation a = AnimationUtils.loadAnimation(ctx, R.anim.slide_down);
        if(a != null){
            a.reset();
            if(v != null){
                v.clearAnimation();
                v.startAnimation(a);
            }
        }
        a.setAnimationListener(new Animation.AnimationListener(){
            @Override
            public void onAnimationStart(Animation arg0) {
            }
            @Override
            public void onAnimationRepeat(Animation arg0) {
            }
            @Override
            public void onAnimationEnd(Animation arg0) {
                if(position==listView.getCount()-1){
                    listView.smoothScrollToPosition(listView.getCount()-1);
                }
            }
        });
    }

    /**
     *
     * @param ctx
     * @param v
     */
    public void slide_up(Context ctx, View v, int position){

        Animation a = AnimationUtils.loadAnimation(ctx, R.anim.slide_up);
        if(a != null){
            a.reset();
            if(v != null){
                v.clearAnimation();
                v.startAnimation(a);
            }
        }
    }

    private void expand(View _toolbar, int position, long _idselect) {
        //set Visible
        _toolbar.setVisibility(View.VISIBLE);

        final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        _toolbar.measure(widthSpec, 300);

        mAnimator = slideAnimator(0, 300, _toolbar);
        mAnimator.start();

        final StickyListHeadersListView listView = (StickyListHeadersListView) mFragmentView.findViewById(R.id.activity_stickylistheaders_listview);
        if(_idselect==listView.getCount()-1){
            listView.smoothScrollToPosition((listView.getCount()-2));
            listView.smoothScrollToPosition(23);
        }

    }

    private void collapse(final View summary) {
        int finalHeight = summary.getHeight();

        ValueAnimator mAnimator = slideAnimator(finalHeight, 0, summary);

        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animator) {
                //Height=0, but it set visibility to GONE
                summary.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        mAnimator.start();
    }



    private ValueAnimator slideAnimator(int start, int end, final View summary) {

        ValueAnimator animator = ValueAnimator.ofInt(start, end);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                //Update Height
                int value = (Integer) valueAnimator.getAnimatedValue();

                ViewGroup.LayoutParams layoutParams = summary.getLayoutParams();
                layoutParams.height = value;
                summary.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }


}