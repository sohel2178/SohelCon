package net.optimusbs.sohelcon.Navigation;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.joanzapata.iconify.widget.IconTextView;

import com.squareup.picasso.Picasso;

import net.optimusbs.sohelcon.BaseClass.User;
import net.optimusbs.sohelcon.HomeActivity;
import net.optimusbs.sohelcon.R;
import net.optimusbs.sohelcon.Response.RegisterResponse;
import net.optimusbs.sohelcon.UploadPhotoActivity;
import net.optimusbs.sohelcon.Utility.Constant;
import net.optimusbs.sohelcon.Utility.FontUtils;
import net.optimusbs.sohelcon.Utility.MyUtils;
import net.optimusbs.sohelcon.Utility.URLs;
import net.optimusbs.sohelcon.Utility.UserLocalStore;
import net.optimusbs.sohelcon.Volley.MyPostVolley;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class UpdateUserProfile extends Fragment implements View.OnClickListener{

    private EditText etemail,etPhone,etAddress,etDesignation;
    private TextView tvCompanyName,tvCompanyAddress,tvDesignation,tvName,tvTitle;
    private CircleImageView profile_pic;
    private IconTextView updateProfileImage;
    private Button btnUpdate;

    private UserLocalStore userLocalStore;
    private Gson gson;

    private int user_id;


    public UpdateUserProfile() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Iconify
                .with(new FontAwesomeModule());

        userLocalStore = new UserLocalStore(getActivity());

        user_id= userLocalStore.getLoggedInUser().getUser_id();

        gson= new Gson();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_update_user_profile, container, false);

        initView(view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Update Profile");
    }

    private void initView(View view) {

        tvCompanyName= (TextView) view.findViewById(R.id.tv_companyName);
        tvCompanyAddress= (TextView) view.findViewById(R.id.tv_address);
        tvDesignation= (TextView) view.findViewById(R.id.designation);
        tvName= (TextView) view.findViewById(R.id.name);
        tvTitle= (TextView) view.findViewById(R.id.form_title);

        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,tvCompanyName);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,tvCompanyAddress);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,tvDesignation);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,tvName);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,tvTitle);

        User user = userLocalStore.getLoggedInUser();
        String name = user.getUser_name();
        String companyName = user.getUser_companyname();
        String designation = user.getUser_designation();
        String address = user.getUser_address();

        // SetText to the User
        tvCompanyName.setText(companyName);
        tvCompanyAddress.setText(address);
        tvDesignation.setText(designation);
        tvName.setText(name);
        tvTitle.setText("Update User");

        //Get Data from Local Store Update

        String email= user.getUser_email();
        String phone= user.getUser_phone();
        String add= user.getUser_address();
        String des= user.getUser_designation();


        etemail= (EditText) view.findViewById(R.id.et_user_email);
        etPhone= (EditText) view.findViewById(R.id.et_phone_no);
        etAddress= (EditText) view.findViewById(R.id.et_user_address);
        etDesignation= (EditText) view.findViewById(R.id.et_designation);

        //Assign in Edit Text
        etemail.setText(email);
        etPhone.setText(phone);
        etAddress.setText(add);
        etDesignation.setText(des);

        updateProfileImage = (IconTextView) view.findViewById(R.id.update_profile_image);

        profile_pic = (CircleImageView) view.findViewById(R.id.profile_pic);

        if(user.getUser_photo().equals("")){
            profile_pic.setImageResource(R.drawable.placeholder);
        }else{
            Picasso.with(getActivity())
                    .load(user.getUser_photo())
                    .placeholder(R.drawable.placeholder)
                    .into(profile_pic);
        }


        if(userLocalStore.getLoggedInUser().getUser_type()==4){
            etDesignation.setVisibility(View.VISIBLE);
        }

        btnUpdate = (Button) view.findViewById(R.id.button_update_user);


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        btnUpdate.setOnClickListener(this);
        updateProfileImage.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.update_profile_image:
                Intent intent = new Intent(getActivity(),UploadPhotoActivity.class);
                startActivity(intent);
                break;

            case R.id.button_update_user:

                String email = etemail.getText().toString().trim();
                String phoneNo = etPhone.getText().toString().trim();
                String address = etAddress.getText().toString().trim();
                String designation = "";

                if(userLocalStore.getLoggedInUser().getUser_type()==4){
                    designation=etDesignation.getText().toString().trim();
                }else{
                    designation=userLocalStore.getLoggedInUser().getUser_designation();
                }


                if(!MyUtils.validateEmail(email)){
                    //Log.d("Test","Test");
                    etemail.requestFocus();
                    Toast.makeText(getActivity(), "Please Enter an Valid Email Address", Toast.LENGTH_SHORT).show();
                    return;
                }


                updateUser(user_id,email,phoneNo,address,designation);


                break;
        }
    }

    private void updateUser(int user_id, final String email, final String phoneNo, final String address, final String designation) {

        Map<String,String> map = new HashMap<>();
        map.put("user_id", String.valueOf(user_id));
        map.put("email", String.valueOf(email));
        map.put("phoneNo", String.valueOf(phoneNo));
        map.put("address", String.valueOf(address));
        map.put("designation", String.valueOf(designation));

        MyPostVolley myPostVolley= new MyPostVolley(getActivity(),map, URLs.UPDATE_USER,"Please Wait While Updating User Data");
        myPostVolley.applyPostVolley();
        myPostVolley.setVolleyPostListener(new MyPostVolley.VolleyPostListener() {
            @Override
            public void getResposefromVolley(String response) {
                RegisterResponse rr = gson.fromJson(response,RegisterResponse.class);
                if(rr.getSuccess()==1){
                    User user = userLocalStore.getLoggedInUser();
                    user.setUser_email(email);
                    user.setUser_phone(phoneNo);
                    user.setUser_address(address);
                    user.setUser_designation(designation);

                    userLocalStore.storeUserData(user);

                    Intent intent = new Intent(getActivity(), HomeActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}
