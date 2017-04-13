package com.example.edono.rxapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.transition.Fade;
import android.transition.Slide;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.CalendarView;
import android.widget.TextView;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Eugeny.Martinenko on 10.04.2017.
 */

public class CalendarActivity extends AppCompatActivity implements CalendarView.OnDateChangeListener {

    public static final int REQUEST_CODE = 0;

    private static final String EXTRA_CHOSEN_DATE = "EXTRA_CHOSEN_DATE";

    @BindView(R.id.layout_root) ViewGroup mRoot;
    @BindView(R.id.view_calendar) CalendarView mCalendar;
    @BindView(R.id.view_calendar_card) ViewGroup mCalendarCard;
    @BindView(R.id.layout_calendar_container) ViewGroup mCalendarContainer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_calendar);
        ButterKnife.bind(this);

        android.transition.Fade fade = new android.transition.Fade();
        fade.addTarget(mCalendarCard);
        fade.addTarget(R.id.text_calendar_title);
        fade.setDuration(300);
        fade.setInterpolator(AnimationUtils.loadInterpolator(this, android.R.interpolator.linear_out_slow_in));

        getWindow().setEnterTransition(fade);

        mCalendar.setOnDateChangeListener(this);
    }

    @Override
    public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);

        Intent result = new Intent();
        result.putExtra(EXTRA_CHOSEN_DATE, calendar);

        setResult(RESULT_OK, result);
        finishAfterTransition();
    }

    public static Calendar extractResult(Intent intent) {
        return (Calendar) intent.getSerializableExtra(EXTRA_CHOSEN_DATE);
    }
}
