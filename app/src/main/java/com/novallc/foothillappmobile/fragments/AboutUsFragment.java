package com.novallc.foothillappmobile.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.novallc.foothillappmobile.R;
import com.novallc.foothillappmobile.activity.WebViewAssets.BaseActivity;

/*Copyright [2016] [Jake Johnson]

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.*/

public class AboutUsFragment extends Fragment {
    public static AboutUsFragment newInstance() {
        return new AboutUsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        final android.support.v7.app.ActionBar mActionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        mActionBar.setDisplayShowCustomEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(true);
        mActionBar.setTitle("About");
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View aboutUsView = inflater.inflate(R.layout.about_us, container, false);
        TextView subtitle = (TextView) aboutUsView.findViewById(R.id.about_us_subtitle);
        TextView moto = (TextView) aboutUsView.findViewById(R.id.about_us_moto);
        TextView description = (TextView) aboutUsView.findViewById(R.id.about_us_description);
        ((TextView) aboutUsView.findViewById(R.id.about_us_company_name)).setTypeface(BaseActivity.sRobotoThin);
        moto.setText(Html.fromHtml(getString(R.string.about_us_subtitle)));
        subtitle.setTypeface(BaseActivity.sRobotoBlack);
        moto.setTypeface(BaseActivity.sRobotoLight);
        description.setTypeface(BaseActivity.sRobotoLight);
        return aboutUsView;
    }
}
