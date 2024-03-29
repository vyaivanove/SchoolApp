package com.sholastik.schoolapp.ScheduleCode;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.widget.TimePicker;

import com.sholastik.schoolapp.R;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class StartTimePicker extends DialogFragment {

    public static final String EXTRA_START_TIME = "com.sholastik.android.schoolapp.time";
    public static final String EXTRA_BUNDLE = "com.sholastik.andorid.schoolapp.bundle";
    public static final String ARG_DAY_OF_WEEK = "dayOfWeek";
    public static final String ARG_INDEX = "index";

    private TimePicker mTimePicker;

    public static StartTimePicker newInstance(int dayOfWeek, int index) {
        Bundle args = new Bundle();
        args.putInt(ARG_DAY_OF_WEEK, dayOfWeek);
        args.putInt(ARG_INDEX, index);

        StartTimePicker startTimePicker = new StartTimePicker();
        startTimePicker.setArguments(args);
        return startTimePicker;
    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Calendar time = Calendar.getInstance();
        int dayOfWeek, index;
        if (getArguments() != null) {
            dayOfWeek = getArguments().getInt(ARG_DAY_OF_WEEK);
            index = getArguments().getInt(ARG_INDEX);
            time.setTime(new Date(Objects.requireNonNull(QueryHandler.getLesson(getContext(), dayOfWeek, index)).mStartTime));
        }

        mTimePicker = (TimePicker) LayoutInflater.from(getActivity())
                .inflate(R.layout.start_time_picker, null);
        mTimePicker.setIs24HourView(true);
        int hours = 0;
        int minutes = 0;
        if (time != null) {
            hours = time.get(Calendar.HOUR);
            minutes = time.get(Calendar.MINUTE);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mTimePicker.setHour(hours);
            mTimePicker.setMinute(minutes);
        } else {
            mTimePicker.setCurrentHour(hours);
            mTimePicker.setCurrentMinute(minutes);
        }


        return new AlertDialog.Builder(Objects.requireNonNull(getActivity()))
                .setView(mTimePicker)
                .setTitle(R.string.start_time_picker)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int hours, minutes;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                            hours = mTimePicker.getHour();
                            minutes = mTimePicker.getMinute();
                        } else {
                            hours = mTimePicker.getCurrentHour();
                            minutes = mTimePicker.getCurrentMinute();
                        }
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.HOUR_OF_DAY, hours);
                        calendar.set(Calendar.MINUTE, minutes);
                        setResult(Activity.RESULT_OK, calendar);
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .create();
    }

    private void setResult(int resultCode, Calendar calendar) {
        if (getTargetFragment() == null) return;

        Intent intent = new Intent();
        intent.putExtra(EXTRA_START_TIME, calendar.getTime().getTime());
        intent.putExtra(EXTRA_BUNDLE, getArguments());

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
