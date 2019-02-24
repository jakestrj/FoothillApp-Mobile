package com.novallc.foothillappmobile.activity.ListViewAssets;


import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.BundleCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.util.Pair;
import android.transition.Slide;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.nhaarman.listviewanimations.ArrayAdapter;
import com.novallc.foothillappmobile.R;
import com.novallc.foothillappmobile.activity.MainActivity;
import com.novallc.foothillappmobile.fragments.BitmapMapStndView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

import static java.security.AccessController.getContext;

public class ListAdapter extends ArrayAdapter<String> implements StickyListHeadersAdapter, SectionIndexer, Filterable {

    Context mContext;
    Activity mActivity;
    List<String> rowItems_original;
    List<String> rowItems_filtered;
    List<String> rowItems_subtitle;
    List<String> rowItems_tag0s;
    List<String> rowItems_tag1s;
    List<String> listViewHeaders;
    private HashMap<String, Integer> indexer;
    protected LayoutInflater inflater;
    private Resources res;
    private TextView row_text_descr;
    private ImageView row_image_blank_account;
    private Button row_btn_sidemenu;
    private ImageView row_image_arrw;
    private Intent contact_detailList;
    private int[] t_clsLocation;
    private String[] t_names;
    private String[] t_descr;
    private String[] t_division;
    private Fragment fragment;
    private static int MAX_CONTACTS = 0;

    //
    public static int[] tagColor_ids = {R.color.tag_c_admin, R.color.tag_c_art, R.color.tag_c_eng, R.color.tag_c_fl, R.color.tag_c_math,
            R.color.tag_c_pe, R.color.tag_c_sci, R.color.tag_c_sshis, R.color.tag_c_sped, R.color.tag_c_it, R.color.tag_c_couns,
            R.color.tag_c_rgstr, R.color.tag_c_prin, R.color.tag_c_asst, R.color.tag_c_chair, R.color.tag_c_el, R.color.tag_c_ib};

    public static String[] t_names_model;
    public static String[] t_emaAddress_model;
    public static String[] t_clsLocation_model;
    public static String[] t_division_model;
    public static String[] t_description_model;
    public static String[] t_tag0_model;
    public static String[] t_tag1_model;
    public static String[] t_fp_location;
    private String t_emaAddress;
    public Contact_Model contact_model;
    private String[] _fpSplit;
    //private final BitmapCache mMemoryCache;

    public ListAdapter(final Context context, Activity activity) {
        this.mContext = context;
        if(activity != null){
            this.mActivity = activity;
        }
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        t_names_model = mContext.getResources().getStringArray(R.array.t_names);
        t_emaAddress_model = mContext.getResources().getStringArray(R.array.t_email);
        t_clsLocation_model = mContext.getResources().getStringArray(R.array.t_rNmber);
        t_division_model = mContext.getResources().getStringArray(R.array.t_division);
        t_description_model = mContext.getResources().getStringArray(R.array.t_rDescription);
        t_tag0_model = mContext.getResources().getStringArray(R.array.t_tag0);
        t_tag1_model = mContext.getResources().getStringArray(R.array.t_tag1);
        t_fp_location = mContext.getResources().getStringArray(R.array.t_clsLocation);
        MAX_CONTACTS = t_names_model.length;
        //mMemoryCache = new BitmapCache();
        for (int i=0; i<MAX_CONTACTS; i++){
            Contact_Model.CONTACTS.add(new Contact_Model(t_names_model[i], t_emaAddress_model[i], t_clsLocation_model[i], t_division_model[i], t_description_model[i], t_tag0_model[i], t_tag1_model[i], t_fp_location[i]));
        }
        initIndexer();
    }

    private void initIndexer() {
        indexer = new HashMap<>();
        res = mContext.getResources();
        t_names = res.getStringArray(R.array.t_names);
        t_descr = res.getStringArray(R.array.t_rDescription);

        rowItems_original = new ArrayList<>(Arrays.asList(t_names));
        rowItems_filtered = new ArrayList<>(Arrays.asList(t_names));
        rowItems_subtitle = new ArrayList<>(Arrays.asList(t_descr));
        rowItems_tag0s = new ArrayList<>(Arrays.asList(t_tag0_model));
        rowItems_tag1s = new ArrayList<>(Arrays.asList(t_tag1_model));

        Collections.sort(rowItems_filtered);
        Collections.sort(rowItems_original);

        for (int i = 0; i < rowItems_filtered.size(); i++) {
            if(indexer.get(rowItems_filtered.get(i).toUpperCase().charAt(0) + "") == null)
            {
                indexer.put(rowItems_filtered.get(i).toUpperCase().charAt(0) + "", i);
            }
        }

        Set<String> keys = indexer.keySet();
        listViewHeaders = new ArrayList<>(keys);
        Collections.sort(listViewHeaders);
    }

    public void setCurrentItems(ArrayList<String> currentItems) {
        this.rowItems_filtered = currentItems;
    }

    @Override
    public View getHeaderView(int i, View convertView, ViewGroup parent) {
        DividerViewHolder holder;

        if (convertView == null) {
            holder = new DividerViewHolder();
            convertView = inflater.from(mContext).inflate(R.layout.list_header, parent, false);
            holder.groupName = (TextView) convertView;
            convertView.setTag(holder);
        } else {
            holder = (DividerViewHolder) convertView.getTag();
        }

        holder.groupName.setText("" + rowItems_filtered.get(i).toUpperCase().charAt(0));

        return convertView;
    }

    @Override
    public long getHeaderId(int i) {
        return rowItems_filtered.get(i).toUpperCase().charAt(0);
    }

    @Override
    public int getPositionForSection(int section) {
        try {
            return indexer.get(listViewHeaders.get(section));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int getSectionForPosition(int position) {
        for (int i = 0; i < listViewHeaders.size(); i++) {
            if (position < indexer.get(listViewHeaders.get(i))) {
                return i - 1;
            }
        }
        return listViewHeaders.size() - 1;
    }

    @Override
    public Object[] getSections() {
        return listViewHeaders.toArray();
    }

    @Override
    public int getCount() {
        return rowItems_filtered.size();
    }

//    @Override
//    public In getItem(int position) {
//        return currentItems.get(position);
//    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        //if it is a new item
        if (convertView == null) {
            convertView = inflater.from(mContext).inflate(R.layout.list_row, parent, false);

            holder = new ViewHolder();
            holder.titleTextView = (TextView)convertView.findViewById(R.id.list_row_draganddrop_textview);
            holder.subTitleTextView = (TextView)convertView.findViewById(R.id.list_row_subtitletext);
            holder.tag0TextView = (TextView)convertView.findViewById(R.id.tag0);
            holder.tag1TextView = (TextView)convertView.findViewById(R.id.tag1);
            holder.tag0_shape = (GradientDrawable)holder.tag0TextView.getBackground();
            holder.tag1_shape = (GradientDrawable)holder.tag1TextView.getBackground();
            holder.tag0TextView.setVisibility(View.VISIBLE);
            holder.tag1TextView.setVisibility(View.VISIBLE);

            convertView.setTag(holder);
        } else {
            //recycle ViewHolder by using older one
            holder = (ViewHolder) convertView.getTag();
        }
        final Contact_Model mContact = Contact_Model.getItem(rowItems_filtered.get(position).hashCode());

        holder.titleTextView.setText(rowItems_filtered.get(position)); //_nameSplit[1] + " " + _nameSplit[0]
        holder.subTitleTextView.setText(mContact.get(Contact_Model.Field.DESCRIPTION));
        holder.tag0TextView.setText(mContact.get(Contact_Model.Field.TAG0TEXT));
        holder.tag1TextView.setText(mContact.get(Contact_Model.Field.TAG1TEXT));

        switch (mContact.get(Contact_Model.Field.TAG0TEXT).toLowerCase()) {
            case "admin":
                holder.tag0_shape.setColor(mContext.getResources().getColor(tagColor_ids[0]));
                break;
            case "art":
                holder.tag0_shape.setColor(mContext.getResources().getColor(tagColor_ids[1]));
                break;
            case "eng":
                holder.tag0_shape.setColor(mContext.getResources().getColor(tagColor_ids[2]));
                break;
            case "fl":
                holder.tag0_shape.setColor(mContext.getResources().getColor(tagColor_ids[3]));
                break;
            case "math":
                holder.tag0_shape.setColor(mContext.getResources().getColor(tagColor_ids[4]));
                break;
            case "pe":
                holder.tag0_shape.setColor(mContext.getResources().getColor(tagColor_ids[5]));
                break;
            case "sci":
                holder.tag0_shape.setColor(mContext.getResources().getColor(tagColor_ids[6]));
                break;
            case "ss/his":
                holder.tag0_shape.setColor(mContext.getResources().getColor(tagColor_ids[7]));
                break;
            case "special ed.":
                holder.tag0_shape.setColor(mContext.getResources().getColor(tagColor_ids[8]));
                break;
            case "te":
                holder.tag0_shape.setColor(mContext.getResources().getColor(tagColor_ids[9]));
                break;
            case "couns":
                holder.tag0_shape.setColor(mContext.getResources().getColor(tagColor_ids[10]));
                break;
            case "rgstr":
                holder.tag0_shape.setColor(mContext.getResources().getColor(tagColor_ids[11]));
                break;
            default:/*case nil*/
                holder.tag0TextView.setVisibility(View.GONE);
                break;
        }
        switch (mContact.get(Contact_Model.Field.TAG1TEXT).toLowerCase()) {
            case "prin":
                holder.tag1_shape.setColor(mContext.getResources().getColor(tagColor_ids[12]));
                break;
            case "asst":
                holder.tag1_shape.setColor(mContext.getResources().getColor(tagColor_ids[13]));
                break;
            case "chair":
                holder.tag1_shape.setColor(mContext.getResources().getColor(tagColor_ids[14]));
                break;
            case "el":
                holder.tag1_shape.setColor(mContext.getResources().getColor(tagColor_ids[15]));
                break;
            case "ib":
                holder.tag1_shape.setColor(mContext.getResources().getColor(tagColor_ids[16]));
                break;
            default:/*case nil*/
                holder.tag1TextView.setVisibility(View.GONE);
                break;
        }

//        View toolbar = convertView.findViewById(R.id.toolbar_row);
        //Prevents full expansion - no commit
        //((LinearLayout.LayoutParams) toolbar.getLayoutParams()).bottomMargin = -50;
//        toolbar.setVisibility(View.GONE);


        row_text_descr = (TextView) convertView.findViewById(R.id.row_exp);
        RelativeLayout mFrameLayout = (RelativeLayout) convertView.findViewById(R.id.frame_surfaceView);
        ImageButton row_btn_new_message = (ImageButton) convertView.findViewById(R.id.btn_new_message);
        ImageButton row_btn_retrieve_loc = (ImageButton) convertView.findViewById(R.id.btn_retrieve_loc);
//        TextView testView = (TextView)convertView.findViewById(R.id.list_row_draganddrop_textview);
        row_image_blank_account = (ImageView) convertView.findViewById(R.id.i_profileImage);
        row_btn_sidemenu = (Button) convertView.findViewById(R.id.btn_sidemenu);
        row_btn_sidemenu.setBackground(mContext.getResources().getDrawable(R.drawable.ic_vertical_dots));
        row_image_blank_account.setImageResource(R.drawable.ic_account_blank);
        mFrameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                row_image_arrw_handler(v, position);
            }
        });

        row_btn_new_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t_emaAddress = mContact.get(Contact_Model.Field.EMAIL);
                Intent mEmailIntent = new Intent(Intent.ACTION_VIEW);
                mEmailIntent.setData(Uri.parse("mailto:" + t_emaAddress));
                mContext.startActivity(mEmailIntent);
            }
        });
        row_btn_retrieve_loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    MainActivity.mFragmentTransaction.commit();
                    MainActivity.toolbar.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });




        //tv2.setText(new String(Character.toChars(0x25B6)));
        return convertView;
    }

    private void row_image_arrw_handler(View _selectedFlPos, int position){
        contact_detailList = new Intent(mContext, Contact_cardView.class);
        contact_detailList.putExtra(Contact_detailList.ID, rowItems_filtered.get(position).hashCode());

        //Applied transitions
        /*if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            ActivityOptionsCompat mOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    //context from StickListExpandableFragment.class
                    mActivity,
                    //Value of transition attribute
                    new Pair<View, String>(_selectedFlPos.findViewById(R.id.i_profileImage),
                        mActivity.getResources().getString(R.string.transition_name_circle))
//                                            new Pair<View, String>(view.findViewById(R.id.title),
//                                                    getString(R.string.transition_name_name)),
//                                            new Pair<View, String>(view.findViewById(R.id.ivContactItem1),
//                                                    getString(R.string.transition_name_phone))
            );
            ActivityCompat.startActivity(mActivity, contact_detailList, mOptions.toBundle());
        }else*/
//            mContext.startActivity(contact_detailList);
//            mActivity.overridePendingTransition(R.anim.zoom_in, 0);

            Bundle b = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                b = ActivityOptions.makeScaleUpAnimation(_selectedFlPos, 0, 0, _selectedFlPos.getWidth(),
                        _selectedFlPos.getHeight()).toBundle();
//                b = ActivityOptions.makeThumbnailScaleUpAnimation(view, bitmap, 0, 0).toBundle();
            }
            mContext.startActivity(contact_detailList, b);
        //Toast.makeText(mContext, "Icon onClickListener", Toast.LENGTH_LONG).show();
    }

    private class ViewHolder {
        TextView titleTextView;
        TextView subTitleTextView;
        TextView tag0TextView;
        TextView tag1TextView;
        GradientDrawable tag0_shape;
        GradientDrawable tag1_shape;
    }

    public static class DividerViewHolder {
        TextView groupName;
    }

    /*
        Needed to maintain positional context
    */
    @Override
    public int getViewTypeCount() {
        return getCount();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public Filter getFilter() {
        // TODO Auto-generated method stub
        ItemFilter filter = new ItemFilter();
        return filter;
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {


            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            // if constraint is empty return the original names
            if(constraint.length() == 0 ){
                results.values = rowItems_original;
                results.count = rowItems_original.size();
                return results;
            }

            final List<String> list = rowItems_original;
            final List<String> list_tag0 = rowItems_tag0s;
            final List<String> list_tag1 = rowItems_tag1s;

            int count = list.size();
            final ArrayList<String> nlist = new ArrayList<String>(count);

            String filterableString;
            String filterableString1;
            String filterableString2;

            Contact_Model mContact;

            for (int i = 0; i < count; i++) {
                filterableString = list.get(i);
                filterableString1 = list_tag0.get(i);
                filterableString2 = list_tag1.get(i);
                if (filterableString.toLowerCase().contains(filterString)) {
                    nlist.add(filterableString);
                }/*else if(filterableString1.toLowerCase().contains(filterString)){
                    mContact = Contact_Model.getItem_tagPARAMS0(filterableString1.hashCode());
                    if(!rowItems_filtered.contains(mContact.get(Contact_Model.Field.NAME))) {
                        nlist.add(mContact.get(Contact_Model.Field.NAME));
                        Log.d("TAG0_NAME", mContact.get(Contact_Model.Field.NAME));
                    }
                }else if(filterableString2.toLowerCase().contains(filterString)){
                    mContact = Contact_Model.getItem_tagPARAMS1(filterableString2.hashCode());
                    if(!rowItems_filtered.contains(mContact.get(Contact_Model.Field.NAME))) {
                        nlist.add(mContact.get(Contact_Model.Field.NAME));
                        Log.d("TAG1_NAME", mContact.get(Contact_Model.Field.NAME));
                    }
                }*/
            }

            results.values = nlist;
            results.count = nlist.size();
            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            rowItems_filtered = (ArrayList<String>) results.values;
            if(rowItems_filtered.size()>0){getHeaderView(0, null, null);}
            notifyDataSetChanged();
        }

    }





    //    @Override
//    public View getDropDownView(int position, View convertView, ViewGroup parent) {
//        return super.getDropDownView(position, convertView, parent);
//    }
}