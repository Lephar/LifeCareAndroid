package com.shrikanthravi.collapsiblecalendarview.widget;

import android.view.GestureDetector;
import android.view.MotionEvent;

public class CalendarSwipeListener extends GestureDetector.SimpleOnGestureListener {

    // Minimal x and y axis swipe distance.
    private static int MIN_SWIPE_DISTANCE_X = 100;
    private static int MIN_SWIPE_DISTANCE_Y = 100;

    // Maximal x and y axis swipe distance.
    private static int MAX_SWIPE_DISTANCE_X = 1000;
    private static int MAX_SWIPE_DISTANCE_Y = 1000;

    private CollapsibleCalendar calendar;

    public CalendarSwipeListener(CollapsibleCalendar calendar) {
        this.calendar = calendar;
    }

    /* This method is invoked when a swipe gesture happened. */
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

        // Get swipe delta value in x axis.
        float deltaX = e1.getX() - e2.getX();

        // Get swipe delta value in y axis.
        float deltaY = e1.getY() - e2.getY();

        // Get absolute value.
        float deltaXAbs = Math.abs(deltaX);
        float deltaYAbs = Math.abs(deltaY);

        // Only when swipe distance between minimal and maximal distance value then we treat it as effective swipe
        if ((deltaXAbs >= MIN_SWIPE_DISTANCE_X) && (deltaXAbs <= MAX_SWIPE_DISTANCE_X)) {
            if (deltaX > 0) {
                if (calendar.isCollapsed())
                    calendar.prevWeek();
                else
                    calendar.prevMonth();
            } else {
                if (calendar.isCollapsed())
                    calendar.nextWeek();
                else
                    calendar.nextMonth();
            }
        }

        if ((deltaYAbs >= MIN_SWIPE_DISTANCE_Y)) {
            if (deltaY > 0 && !calendar.isCollapsed()) {
                calendar.collapse(400);
            } else if (deltaY <= 0 && calendar.isCollapsed()) {
                calendar.expand(400);
            }
        }

        return true;
    }
}
