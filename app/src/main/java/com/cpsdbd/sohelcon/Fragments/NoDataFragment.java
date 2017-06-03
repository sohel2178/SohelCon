package com.cpsdbd.sohelcon.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cpsdbd.sohelcon.R;
import com.cpsdbd.sohelcon.Utility.Constant;
import com.cpsdbd.sohelcon.Utility.FontUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class NoDataFragment extends Fragment {
    private TextView tvDisplay;

    String text;


    public NoDataFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments()!= null){
            text = getArguments().getString(Constant.TEXT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_no_data, container, false);

        tvDisplay = (TextView) view.findViewById(R.id.displayText);
        tvDisplay.setText(text);

        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,tvDisplay);

        return view;
    }

}
