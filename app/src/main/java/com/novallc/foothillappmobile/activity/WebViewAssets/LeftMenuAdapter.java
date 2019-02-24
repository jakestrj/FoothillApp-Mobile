package com.novallc.foothillappmobile.activity.WebViewAssets;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.novallc.foothillappmobile.R;
import com.novallc.foothillappmobile.activity.model.LeftMenuItem;

import java.util.List;

public class LeftMenuAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<LeftMenuItem> leftMenuItems;

    private static class ViewHolder {
        public TextView name;

        private ViewHolder() {
        }
    }

    public LeftMenuAdapter(Context context, List<LeftMenuItem> leftMenuItems) {
        this.leftMenuItems = leftMenuItems;
        this.inflater = (LayoutInflater) context.getSystemService("layout_inflater");
    }

    public int getCount() {
        return this.leftMenuItems.size();
    }

    public Object getItem(int position) {
        return this.leftMenuItems.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = this.inflater.inflate(R.layout.left_menu_item, parent, false);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.left_menu_item_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText(((LeftMenuItem) this.leftMenuItems.get(position)).getName());
        holder.name.setTypeface(BaseActivity.sRobotoThin);
        holder.name.setCompoundDrawablesWithIntrinsicBounds(((LeftMenuItem) this.leftMenuItems.get(position)).getIcon(), 0, 0, 0);
//        if (position > 2) {
//            holder.name.setPadding(54, 23, 26, 23);
//        }
        return convertView;
    }
}