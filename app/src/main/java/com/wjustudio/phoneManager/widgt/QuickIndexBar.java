package com.wjustudio.phoneManager.widgt;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class QuickIndexBar extends View {

    private Paint mPaint;
    private int mCellWidth;
    private float mCellHeight;
    private static final String[] LETTERS = new String[] { "#","A", "B", "C", "D", "E", "F", "G", "H",
            "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y",
            "Z" };
    private Rect mBounds;
    private int mCurrentIndex = -1;
    public interface OnLetterChangeListener {
        void onLetterChange(String letter);
    }
    private OnLetterChangeListener mOnLetterChangeListener;

    public OnLetterChangeListener getOnLetterChangeListener() {
        return mOnLetterChangeListener;
    }

    public void setOnLetterChangeListener(OnLetterChangeListener onLetterChangeListener) {
        mOnLetterChangeListener = onLetterChangeListener;
    }

    public QuickIndexBar(Context context) {
        this(context, null);
    }

    public QuickIndexBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QuickIndexBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
        mPaint.setAntiAlias(true);
        mPaint.setTypeface(Typeface.DEFAULT_BOLD);
        mPaint.setTextSize(16f);
        mBounds = new Rect();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < LETTERS.length; i++) {
            String text = LETTERS[i];
            // 获取文本宽度
            float textWidth = mPaint.measureText(text);
            // 获取文本宽高
            mPaint.getTextBounds(text, 0, text.length(), mBounds);
            float textHeight = mBounds.height();
            float x = mCellWidth * 0.5f - textWidth * 0.5f;
            float y = mCellHeight * 0.5f + textHeight * 0.5f + i * mCellHeight;
            mPaint.setColor(mCurrentIndex == i ? Color.BLUE : Color.WHITE);
            canvas.drawText(text, x, y, mPaint);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mCellWidth = getMeasuredWidth();
        mCellHeight = getMeasuredHeight() * 1.0f / LETTERS.length;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float y;
        int index;
        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
            y = event.getY();
            index = (int) (y/mCellHeight);
            if(index < LETTERS.length && mCurrentIndex != index) {
//                ToastUtil.showToast(getContext(), LETTERS[index]);
                mCurrentIndex = index;
                if(mOnLetterChangeListener != null) {
                    mOnLetterChangeListener.onLetterChange(LETTERS[mCurrentIndex]);
                }
            }
            break;
        case MotionEvent.ACTION_MOVE:
            y = event.getY();
            index = (int) (y/mCellHeight);
            if(index < LETTERS.length && mCurrentIndex != index) {
//                ToastUtil.showToast(getContext(), LETTERS[index]);
                mCurrentIndex = index;
                if(mOnLetterChangeListener != null) {
                    mOnLetterChangeListener.onLetterChange(LETTERS[mCurrentIndex]);
                }
            }
            break;
        case MotionEvent.ACTION_UP:
            mCurrentIndex = -1;
            break;
        default:
            break;
        }
        invalidate();
        return true;
    }

}
