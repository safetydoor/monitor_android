package com.amenuo.monitor.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amenuo.monitor.R;
import com.amenuo.monitor.view.MainLumpView;

/**
 * Created by laps on 7/19/16.
 */
public class MainAdFragment extends Fragment {

    private static MainAdFragment instance;

    public static  MainAdFragment getInstance(){
        if(instance==null){
            instance = new MainAdFragment();
        }
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        MainLumpView lumpView = new MainLumpView(this.getActivity());
        lumpView.setImageResource(R.drawable.main_lump_background_1);
        lumpView.setText("天气预报");
        return lumpView;
    }
}