package com.example.patrickstar.resultsthequery.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.patrickstar.resultsthequery.R;

import java.util.List;

/**
 * Created by PatrickStar on 2018/1/30.
 */

public class ViewHolderAdapter extends BaseAdapter {
    private List<String> mData;
    private LayoutInflater mInflater;

    public ViewHolderAdapter(List<String> data, Context context) {
        mData = data;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        //判断是否缓存
        if (convertView == null) {
            holder = new ViewHolder();
            //通过LayoutInflater实例化布局
            holder.img = (ImageView) convertView.findViewById(R.id.imageView);
            convertView.setTag(holder);
        } else {
            //通过Tag找到缓存布局
            holder = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }

    public class ViewHolder {
        public ImageView img;
    }
}
