package com.cpsdbd.sohelcon.Navigation;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import com.cpsdbd.sohelcon.BaseClass.User;
import com.cpsdbd.sohelcon.R;
import com.cpsdbd.sohelcon.Utility.Constant;
import com.cpsdbd.sohelcon.Utility.FontUtils;
import com.cpsdbd.sohelcon.Utility.UserLocalStore;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewUserProfile extends Fragment {

    private TextView tvCompanyName,tvCompanyAddress,tvDesignation,tvName,tvTitle,
                    tvEmail,tvEmailText,tvPhone,tvPhoneText,tvAddress,tvAddressText;
    private CircleImageView profile_pic;

    private UserLocalStore userLocalStore;

    private User user;


    public ViewUserProfile() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments()!= null){
           user = (User) getArguments().getSerializable(Constant.USER);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("User Profile");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_user_profile, container, false);

        initView(view);

        return view;
    }

    private void initView(View view) {



        tvCompanyName= (TextView) view.findViewById(R.id.tv_companyName);
        tvCompanyAddress= (TextView) view.findViewById(R.id.tv_address);
        tvDesignation= (TextView) view.findViewById(R.id.designation);
        tvName= (TextView) view.findViewById(R.id.name);
        tvTitle= (TextView) view.findViewById(R.id.form_title);


        String name = user.getUser_name();
        String companyName = user.getUser_companyname();
        String designation = user.getUser_designation();
        String address = user.getUser_address();
        String email = user.getUser_email();
        String phone = user.getUser_phone();

        tvCompanyName.setText(companyName);
        tvCompanyAddress.setText(address);
        tvDesignation.setText(designation);
        tvName.setText(name);
        tvTitle.setText("User");


    /*,,,,,*/

        tvEmail= (TextView) view.findViewById(R.id.email);
        tvEmailText= (TextView) view.findViewById(R.id.email_text);
        tvPhone= (TextView) view.findViewById(R.id.phone);
        tvPhoneText= (TextView) view.findViewById(R.id.phone_text);
        tvAddress= (TextView) view.findViewById(R.id.address);
        tvAddressText= (TextView) view.findViewById(R.id.address_text);

        tvEmail.setText(email);
        tvPhone.setText(phone);
        tvAddress.setText(address);

        // Set Font

        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,tvCompanyName);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,tvCompanyAddress);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,tvDesignation);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,tvName);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,tvTitle);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,tvEmail);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,tvEmailText);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,tvPhone);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,tvPhoneText);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,tvAddress);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,tvAddressText);



        profile_pic = (CircleImageView) view.findViewById(R.id.profile_pic);



        if(user.getUser_photo().equals("")){
            profile_pic.setImageResource(R.drawable.placeholder);
        }else{
            Picasso.with(getActivity())
                    .load(user.getUser_photo())
                    .placeholder(R.drawable.placeholder)
                    .into(profile_pic);
        }




    }



}
