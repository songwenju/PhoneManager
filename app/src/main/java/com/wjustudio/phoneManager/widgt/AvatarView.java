package com.wjustudio.phoneManager.widgt;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.wjustudio.phoneManager.R;
import com.wjustudio.phoneManager.utils.CommonUtil;

import org.kymjs.kjframe.KJBitmap;
import org.kymjs.kjframe.bitmap.BitmapCallBack;


/**
 * 头像的View
 */
public class AvatarView extends CircleImageView {
    public static final String AVATAR_SIZE_REG = "_[0-9]{1,3}";
    public static final String MIDDLE_SIZE = "_100";
    public static final String LARGE_SIZE = "_200";

    private int id;
    private String name;

    public AvatarView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public AvatarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AvatarView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(name)) {
//                    UIHelper.showUserCenter(getContext(), id, name);
                }
            }
        });
    }

    public void setUserInfo(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public void setAvatarUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            setImageResource(R.mipmap.login_dface);
            return;
        }
        // 由于头像地址默认加了一段参数需要去掉
        int end = url.indexOf('?');
        final String headUrl;
        if (end > 0) {
            headUrl = url.substring(0, end);
        } else {
            headUrl = url;
        }

        new KJBitmap().display(this, headUrl, R.mipmap.login_dface, 0, 0,
                new BitmapCallBack() {
                    @Override
                    public void onFailure(Exception e) {
                        super.onFailure(e);
                        CommonUtil.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setImageResource(R.mipmap.login_dface);
                            }
                        });
                        setImageResource(R.mipmap.login_dface);
                    }
                });
    }

    public static String getSmallAvatar(String source) {
        return source;
    }

    public static String getMiddleAvatar(String source) {
        if (source == null)
            return "";
        return source.replaceAll(AVATAR_SIZE_REG, MIDDLE_SIZE);
    }

    public static String getLargeAvatar(String source) {
        if (source == null)
            return "";
        return source.replaceAll(AVATAR_SIZE_REG, LARGE_SIZE);
    }
}
