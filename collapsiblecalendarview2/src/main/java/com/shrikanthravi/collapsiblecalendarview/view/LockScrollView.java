package com.shrikanthravi.collapsiblecalendarview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

import androidx.core.view.GestureDetectorCompat;

import com.shrikanthravi.collapsiblecalendarview.widget.CalendarSwipeListener;
import com.shrikanthravi.collapsiblecalendarview.widget.CollapsibleCalendar;

/**
 * Created by shrikanthravi on 07/03/18.
 */

public class LockScrollView extends ScrollView {
    private GestureDetectorCompat gestureDetectorCompat;

    public LockScrollView(Context context) {
        super(context);
    }

    public LockScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LockScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetectorCompat.onTouchEvent(event);
        return true;
    }

    public void enableSwipe(CollapsibleCalendar calendar) {
        CalendarSwipeListener gestureListener = new CalendarSwipeListener(calendar);
        gestureDetectorCompat = new GestureDetectorCompat(getContext(), gestureListener);
    }
}
