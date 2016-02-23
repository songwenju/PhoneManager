package com.wjustudio.phoneManager.base;

import android.widget.BaseAdapter;

import java.util.List;

/**
 *Adapter的公共父类.这里没有实现BaseAdapter的getView类,让子类去实现.
 * 作者：songwenju on 2016/2/19 23:19
 * 邮箱：songwenju01@163.com
 */
public abstract class BaiscAdapter<T> extends BaseAdapter {
    protected List<T> mList;

    public BaiscAdapter(List<T> list) {
        mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
