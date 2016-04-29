package com.wjustudio.phoneManager.widgt;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wjustudio.phoneManager.R;

/**
 * songwenju on 16-4-29 : 17 : 23.
 * 邮箱：songwenju@outlook.com
 */
public class CommonTitleLayout extends RelativeLayout {

    private ImageView mImgSetting;
    private TextView mCommonTitle;
    private OnSettingImgClickListener mSettingImgClickListener;

    //设置图标点击的回调
    public interface OnSettingImgClickListener {
        void onSettingImgClick();
    }

    public void setOnSettingImgClickListener(OnSettingImgClickListener listener){
        mSettingImgClickListener = listener;
    }
    public CommonTitleLayout(Context context) {
        this(context, null);
    }

    public CommonTitleLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommonTitleLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }

    private void init() {
        View view = View.inflate(getContext(), R.layout.common_title, this);
        mImgSetting = (ImageView) view.findViewById(R.id.img_setting);
        mCommonTitle = (TextView) view.findViewById(R.id.common_title);
        mImgSetting.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mSettingImgClickListener.onSettingImgClick();
            }
        });
    }


    /**
     * 设置标题
     * @param title
     */
    public void setTitle(String title){
        mCommonTitle.setText(title);
    }

    /**
     * 设置图标是否可见
     * @param visible
     */
    public void setImgSettingVisible(boolean visible){
        mImgSetting.setVisibility(visible ? VISIBLE : GONE);
    }
}
