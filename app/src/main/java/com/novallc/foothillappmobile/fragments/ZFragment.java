package com.novallc.foothillappmobile.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nhaarman.listviewanimations.appearance.StickyListHeadersAdapterDecorator;
import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;
import com.nhaarman.listviewanimations.util.StickyListHeadersListViewWrapper;
import com.novallc.foothillappmobile.R;
import com.novallc.foothillappmobile.activity.ListViewAssets.ListAdapter;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class ZFragment extends Fragment{

    public ZFragment() {
        // necessary empty public constructor
    }

    public static ZFragment newInstance(int index) {
        ZFragment f = new ZFragment();
        Bundle args = new Bundle();
        args.putInt("index", index);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        android.support.v7.app.ActionBar mActionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        mActionBar.setDisplayShowCustomEnabled(false);
        return inflater.inflate(R.layout.activity_main_temp, container, false);
    }

}