package com.gokids.yoda_tech.gokids.utils;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

public class WrapContentHeightViewPager extends ViewPager {

    public WrapContentHeightViewPager(Context context) {
        super(context);
    }

    public WrapContentHeightViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int height = 0;

        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);

            child.measure(widthMeasureSpec, View.MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

            int h = child.getMeasuredHeight();

            if (h > height) height = h;
        }

        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


/*
    public boolean onTouch(View v, MotionEvent event) {
        int dragthreshold = 30;

        int downX = 0;

        int downY = 0;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) event.getRawX();

                downY = (int) event.getRawY();

                break;

            case MotionEvent.ACTION_MOVE:
                int distanceX = Math.abs((int) event.getRawX() - downX);

                int distanceY = Math.abs((int) event.getRawY() - downY);

                if (distanceY > distanceX && distanceY > dragthreshold) {
                    mViewPager.getParent().requestDisallowInterceptTouchEvent(false);

                    mScrollView.getParent().requestDisallowInterceptTouchEvent(true);
                } else if (distanceX > distanceY && distanceX > dragthreshold) {
                    mViewPager.getParent().requestDisallowInterceptTouchEvent(true);

                    mScrollView.getParent().requestDisallowInterceptTouchEvent(false);
                }

                break;
            case MotionEvent.ACTION_UP:
                mScrollView.getParent().requestDisallowInterceptTouchEvent(false);

                mViewPager.getParent().requestDisallowInterceptTouchEvent(false);

                break;
        }

        return false;
    }
*/
}