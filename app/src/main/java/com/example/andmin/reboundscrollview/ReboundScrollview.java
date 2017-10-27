package com.example.andmin.reboundscrollview;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ScrollView;

/**
 * 创建者:   李翔
 * 创建时间:  2017/8/1 20:17
 * 描述：    支持回弹效果的scrollview
 */
public class ReboundScrollview extends ScrollView {
    // 拖动的距离 size = 4 的意思 只允许拖动屏幕的1/4
    private static final int SIZE = 4;
    private View  mContentView;
    private float mStartY;
    private Rect mRect = new Rect();

    public ReboundScrollview(Context context) {
        super(context);
    }

    public ReboundScrollview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ReboundScrollview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ReboundScrollview(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        if (getChildCount() > 0) {
            mContentView = getChildAt(0);
        }
        super.onFinishInflate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mContentView != null) {
            contentOnToucnEvent(ev);
        }
        return super.onTouchEvent(ev);
    }

    private void contentOnToucnEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mStartY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float nowY = ev.getY();
                // 当滚动到最上或者最下时就不会再滚动，这时移动布局
                if (isNeedMove()) {
                    if (mRect.isEmpty()) {
                        // 保存正常的布局位置
                        mRect.set(mContentView.getLeft(), mContentView.getTop(), mContentView.getRight(), mContentView.getBottom());
                    }
                    //计算距离
                    int tempY = (int) (nowY - mStartY) / SIZE;
                    // 移动布局
                    mContentView.layout(mContentView.getLeft(), mContentView.getTop() + tempY, mContentView.getRight(), mContentView.getBottom() +
                            tempY);
                }
                mStartY = nowY;
                break;
            case MotionEvent.ACTION_UP:
                startAnimation();
                break;
        }
    }

    // 是否需要开启动画
    private void startAnimation() {
        if (!mRect.isEmpty()) {
            int top = mContentView.getTop();
            mContentView.layout(mRect.left, mRect.top, mRect.right, mRect.bottom);
            TranslateAnimation animation = new TranslateAnimation(0, 0, top - mRect.top, 0);
            animation.setDuration(200);
            mContentView.startAnimation(animation);
        }
    }

    // 是否需要移动布局
    private boolean isNeedMove() {
        float scaleY = getScrollY();
        return scaleY == 0 || (mContentView.getMeasuredHeight() - getHeight()) == scaleY;
    }
}
