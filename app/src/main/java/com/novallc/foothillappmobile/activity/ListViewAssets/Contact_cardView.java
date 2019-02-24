package com.novallc.foothillappmobile.activity.ListViewAssets;


import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.transition.Explode;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.novallc.foothillappmobile.R;
import com.novallc.foothillappmobile.activity.MainActivity;
import com.novallc.foothillappmobile.activity.WebViewAssets.*;
import com.novallc.foothillappmobile.activity.WebViewAssets.BaseActivity;
import com.novallc.foothillappmobile.fragments.BitmapMapStndView;

import java.util.Arrays;
import java.util.List;

import it.sephiroth.android.library.tooltip.Tooltip;

import static com.novallc.foothillappmobile.activity.ListViewAssets.Contact_detailList.ID;
import static com.novallc.foothillappmobile.activity.ListViewAssets.ListAdapter.tagColor_ids;

public class Contact_cardView extends Activity{

    public final static String ANIMID = "ANIMViEW";

    private static String t_emaAddress;
    private String t_name;
    private String t_clsLocation;
    private String t_description;
    public Contact_Model mContact;
    private Context mContext;

    private ImageView mCardViewPhone;

    private TextView mInfoPanelLoc;
    private TextView mInfoPanelEm;
    private TextView mCardviewTitle;
    private TextView mCardViewSubtitle;

    private String[] _fpSplit;
    private Fragment fragment;

    //Split variables for string modific.
    private List<String> _nameSplit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_card);

        mInfoPanelLoc = (TextView) findViewById(R.id.cardview_locText);
        mInfoPanelEm = (TextView) findViewById(R.id.cardview_emailText);
        mCardviewTitle = (TextView) findViewById(R.id.cardview_title);
        mCardViewSubtitle = (TextView) findViewById(R.id.cardview_subtitle);

        mCardViewPhone = (ImageView) findViewById(R.id.cardview_infoimg0);

        mContext = getApplicationContext();
        mContact = Contact_Model.getItem(getIntent().getIntExtra(ID, 0));

        t_clsLocation = mContact.get(Contact_Model.Field.CLASS);
        t_emaAddress = mContact.get(Contact_Model.Field.EMAIL);
        t_name = mContact.get(Contact_Model.Field.NAME);
        t_description = mContact.get(Contact_Model.Field.DESCRIPTION);

        mInfoPanelLoc.setText(String.format(getResources().getString(R.string.cardview_loc), t_clsLocation));
        mInfoPanelEm.setText(t_emaAddress);

        _nameSplit = Arrays.asList(t_name.split(","));
        mCardviewTitle.setText(_nameSplit.get(1) + " " + _nameSplit.get(0));
        mCardViewSubtitle.setText(t_description);

        ViewHolder cardInfoHolder;

        //if it is a new item
        cardInfoHolder = new ViewHolder();
        cardInfoHolder.tag0TextView = (TextView)findViewById(R.id.cardview_tag0);
        cardInfoHolder.tag1TextView = (TextView)findViewById(R.id.cardview_tag1);
        cardInfoHolder.tag0_shape = (GradientDrawable)cardInfoHolder.tag0TextView.getBackground();
        cardInfoHolder.tag1_shape = (GradientDrawable)cardInfoHolder.tag1TextView.getBackground();
        cardInfoHolder.tag0TextView.setVisibility(View.VISIBLE);
        cardInfoHolder.tag1TextView.setVisibility(View.VISIBLE);

        cardInfoHolder.tag0TextView.setText(mContact.get(Contact_Model.Field.TAG0TEXT));
        cardInfoHolder.tag1TextView.setText(mContact.get(Contact_Model.Field.TAG1TEXT));

        switch (mContact.get(Contact_Model.Field.TAG0TEXT).toLowerCase()) {
            case "admin":
                cardInfoHolder.tag0_shape.setColor(mContext.getResources().getColor(tagColor_ids[0])); break;
            case "art":
                cardInfoHolder.tag0_shape.setColor(mContext.getResources().getColor(tagColor_ids[1])); break;
            case "eng":
                cardInfoHolder.tag0_shape.setColor(mContext.getResources().getColor(tagColor_ids[2])); break;
            case "fl":
                cardInfoHolder.tag0_shape.setColor(mContext.getResources().getColor(tagColor_ids[3])); break;
            case "math":
                cardInfoHolder.tag0_shape.setColor(mContext.getResources().getColor(tagColor_ids[4])); break;
            case "pe":
                cardInfoHolder.tag0_shape.setColor(mContext.getResources().getColor(tagColor_ids[5])); break;
            case "sci":
                cardInfoHolder.tag0_shape.setColor(mContext.getResources().getColor(tagColor_ids[6])); break;
            case "ss/his":
                cardInfoHolder.tag0_shape.setColor(mContext.getResources().getColor(tagColor_ids[7])); break;
            case "special ed.":
                cardInfoHolder.tag0_shape.setColor(mContext.getResources().getColor(tagColor_ids[8])); break;
            case "te":
                cardInfoHolder.tag0_shape.setColor(mContext.getResources().getColor(tagColor_ids[9])); break;
            case "couns":
                cardInfoHolder.tag0_shape.setColor(mContext.getResources().getColor(tagColor_ids[10])); break;
            case "rgstr":
                cardInfoHolder.tag0_shape.setColor(mContext.getResources().getColor(tagColor_ids[11])); break;
            default:/*case nil*/
                cardInfoHolder.tag0TextView.setVisibility(View.GONE); break;
        }
        switch (mContact.get(Contact_Model.Field.TAG1TEXT).toLowerCase()) {
            case "prin":
                cardInfoHolder.tag1_shape.setColor(mContext.getResources().getColor(tagColor_ids[12])); break;
            case "asst":
                cardInfoHolder.tag1_shape.setColor(mContext.getResources().getColor(tagColor_ids[13])); break;
            case "chair":
                cardInfoHolder.tag1_shape.setColor(mContext.getResources().getColor(tagColor_ids[14])); break;
            case "el":
                cardInfoHolder.tag1_shape.setColor(mContext.getResources().getColor(tagColor_ids[15])); break;
            case "ib":
                cardInfoHolder.tag1_shape.setColor(mContext.getResources().getColor(tagColor_ids[16])); break;
            default:/*case nil*/
                cardInfoHolder.tag1TextView.setVisibility(View.GONE); break;
        }

        Tooltip.make(this,
                new Tooltip.Builder(101)
                        .anchor(mCardViewPhone, Tooltip.Gravity.BOTTOM)
                        .closePolicy(new Tooltip.ClosePolicy()
                                .insidePolicy(true, false)
                                .outsidePolicy(true, false), 3000)
                        .activateDelay(800)
                        .showDelay(300)
                        .text(getString(R.string.tooltip_cardview))
                        .maxWidth(500)
                        .withArrow(true)
                        .withOverlay(true)
                        .typeface(BaseActivity.sRobotoBlack)
                        .floatingAnimation(Tooltip.AnimationBuilder.DEFAULT)
                        .build()
        ).show();

    }

    private class ViewHolder {
        TextView tag0TextView;
        TextView tag1TextView;
        GradientDrawable tag0_shape;
        GradientDrawable tag1_shape;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }

    public void cardviewRouteExit(View view){
        onBackPressed();
    }

    public void cardviewRoutePhone(View view){
        Class fragmentClass = BitmapMapStndView.class;
        try {
            if (fragment == null) {
                _fpSplit = mContact.get(Contact_Model.Field.FLOATLOC).split(",");
                _fpSplit[0] = _fpSplit[0].replaceAll("\\s+","");
                _fpSplit[1] = _fpSplit[1].replaceAll("\\s+","");
                Bundle bundle = new Bundle();
                bundle.putBoolean("directed_lAdapter", true);
                bundle.putFloat("x_pos", Integer.parseInt(_fpSplit[0]));
                bundle.putFloat("y_pos", Integer.parseInt(_fpSplit[1]));
                fragment = (Fragment) fragmentClass.newInstance();
                fragment.setArguments(bundle);
            }
            MainActivity.mFragmentTransaction = MainActivity.mFragmentManager.beginTransaction();
            MainActivity.mFragmentTransaction.replace(R.id.frame_container, fragment);
            MainActivity.mFragmentTransaction.commitAllowingStateLoss();
            MainActivity.toolbar.setVisibility(View.GONE);
            onBackPressed();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cardviewRouteEmail(View view){
        Intent mEmailIntent = new Intent(Intent.ACTION_VIEW);
        mEmailIntent.setData(Uri.parse("mailto:" + t_emaAddress));
        startActivity(mEmailIntent);
    }

    public void cardviewRouteFullCard(View view){
        Intent contact_fullcard = new Intent(mContext, Contact_detailList.class);
        contact_fullcard.putExtra(Contact_detailList.ID, getIntent().getIntExtra(ID, 0));

        Bundle b = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            b = ActivityOptions.makeScaleUpAnimation(view, 0, 0, view.getWidth(),
                    view.getHeight()).toBundle();
        }

        startActivity(contact_fullcard, b);
        //Toast.makeText(mContext, "Icon onClickListener", Toast.LENGTH_LONG).show();
    }
}
