package com.wjustudio.phoneManager.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wjustudio.phoneManager.R;
import com.wjustudio.phoneManager.utils.LogUtil;
import com.wjustudio.phoneManager.utils.ToastUtil;

/**
 * Fragment的基类
 * 1.规范代码结构
 * 2.提供公共方法，精简代码
 */
public abstract class BaseFragment extends Fragment implements View.OnClickListener{
    public Context mContext;
    public View mView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = View.inflate(mContext,getLayoutID(),null);
        onInitView(mView);
        onIntListener();
        onInitData();
        onSetViewData();
        registerCommonButton(mView);
        return mView;
    }

    /**
     * 多个界面公共的Button
     * @param root
     */
    private void registerCommonButton(View root) {
        View view = root.findViewById(R.id.back);
        if (view != null){
            view.setOnClickListener(this);
        }
    }

    /**
     * 设置Activity的布局文件
     * @return
     */
    protected abstract int getLayoutID();

    /**
     * 初始化view
     * @param view
     */
    protected abstract void onInitView(View view);


    /**
     * 初始化数据
     */
    protected abstract void onInitData();

    /**
     * 设置view的数据
     */
    protected abstract void onSetViewData();

    /**
     * 初始化listener
     */
    protected abstract void onIntListener();

    /**
     * BaseActivity没有处理的点击事件，将在这个里面处理
     * @param v
     */
    protected abstract void processClick(View v);


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                getFragmentManager().popBackStack();
                break;
            default:
                processClick(v);
                break;
        }
    }

    /**
     * 弹toast
     * @param msg
     */
    public void toast(String msg){
        ToastUtil.showToast(msg);
    }

    /**
     *显示d级别的log
     * @param msg
     */
    public void logd(String msg){
        LogUtil.d(getClass(),msg);
    }

    /**
     * 显示e级别的log
     * @param msg
     */
    public void loge(String msg){
        LogUtil.e(getClass(), msg);
    }
}
