package com.wjustudio.phoneManager.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wjustudio.phoneManager.R;
import com.wjustudio.phoneManager.base.BaiscAdapter;
import com.wjustudio.phoneManager.javaBean.IconInfo;

import java.util.List;

/**
 * AppListAdapter列表适配器
 */
public class HomeMainAdapter extends BaiscAdapter {
    private Context mContext ;
    private List<IconInfo> mIcons;
    private int[] mIconArray = {R.mipmap.icon_safe, R.mipmap.icon_contacts,
            R.mipmap.icon_progress, R.mipmap.icon_app, R.mipmap.icon_cache};
    private int mWindowHeight;
    public HomeMainAdapter(Context context,List list, int windowHeight) {
        super(list);
        mContext = context;
        mIcons = list;
        mWindowHeight = windowHeight;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_home, null);
            holder = new ViewHolder();
            holder.icon = (ImageView) convertView.findViewById(R.id.img_icon);
            holder.iconName = (TextView) convertView.findViewById(R.id.tv_icon_name);
            convertView.setTag(holder);
            RelativeLayout.LayoutParams params =
                    (RelativeLayout.LayoutParams) holder.icon.getLayoutParams();
            params.height = mWindowHeight * 4 / (getCount() * 9);
            convertView.setLayoutParams(params);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.icon.setImageResource(mIconArray[position]);
        holder.iconName.setText(mIcons.get(position).iconName);
        return convertView;
    }

    private static class ViewHolder {
        public ImageView icon;
        public TextView iconName;
    }
}

