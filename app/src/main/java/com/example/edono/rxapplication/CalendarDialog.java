package com.example.edono.rxapplication;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by edono on 08.04.2017.
 */

public class CalendarDialog extends Fragment {

    public interface OnCloseListener {

        void onClose();
    }
    private OnCloseListener mListener;


    public void setOnCloseListener(OnCloseListener mListener) {
        this.mListener = mListener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }


}
