package net.optimusbs.sohelcon;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.squareup.picasso.Picasso;

import net.optimusbs.sohelcon.BaseClass.Project;
import net.optimusbs.sohelcon.BaseClass.User;
import net.optimusbs.sohelcon.Fragments.DashBoardFragment;
import net.optimusbs.sohelcon.Fragments.ParticularProjectFragment;
import net.optimusbs.sohelcon.Navigation.CoordinatorListFragment;
import net.optimusbs.sohelcon.Navigation.CreateCoordinatorFragment;
import net.optimusbs.sohelcon.Navigation.CreateProjectFragment;
import net.optimusbs.sohelcon.Navigation.UpdateUserProfile;
import net.optimusbs.sohelcon.Navigation.ViewUserProfile;
import net.optimusbs.sohelcon.Response.ProjectResponse;
import net.optimusbs.sohelcon.Utility.Constant;
import net.optimusbs.sohelcon.Utility.FieldConstant;
import net.optimusbs.sohelcon.Utility.FontUtils;
import net.optimusbs.sohelcon.Utility.FragName;
import net.optimusbs.sohelcon.Utility.URLs;
import net.optimusbs.sohelcon.Utility.UserLocalStore;
import net.optimusbs.sohelcon.Volley.MyPostVolley;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class NavigationDrawer extends Fragment implements View.OnClickListener {


    public static final String PREF_NAME ="mypref";
    public static final String KEY_USER_LEARNED_DRAWERR="user_learned_drawer";

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;

    private boolean mUserLearnedDrawer;
    private boolean mFromSavedInstanceState;

    private View containerView;

    private CircleImageView profileImage;

    private TextView tvUserName,tvCompanyName,tvChild,tvChildList,tvHome,tvCreate_project,tvLogout,tvUpdateUserProfile;

    private UserLocalStore userLocalStore;

    private LinearLayout logOut,container_createNewProject,container_createChild,containe_list_of_child,home_container,update_user_profile;

    private int user_type;

    private Gson gson;


    public NavigationDrawer() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gson = new Gson();

        Iconify
                .with(new FontAwesomeModule());

        userLocalStore = new UserLocalStore(getActivity());
        user_type = userLocalStore.getLoggedInUser().getUser_type();

        mUserLearnedDrawer = Boolean.valueOf(readFromPreferences(getActivity(),KEY_USER_LEARNED_DRAWERR,"false"));

        // if saveInstanceState is not null its coming back from rotation
        if(savedInstanceState!=null){
            mFromSavedInstanceState=true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        //Initialize View
        initView(view);
        return view;
    }

    private void initView(View view){
        tvCompanyName = (TextView) view.findViewById(R.id.tvCompanyName);
        tvUserName = (TextView) view.findViewById(R.id.tvDisplayName);
        tvHome = (TextView) view.findViewById(R.id.tv_nav_home);
        tvCreate_project = (TextView) view.findViewById(R.id.tv_nav_create_project);
        tvLogout = (TextView) view.findViewById(R.id.tv_nav_logout);
        tvChild = (TextView) view.findViewById(R.id.tv_create_child);
        tvChildList = (TextView) view.findViewById(R.id.tv_list_of_child);
        tvUpdateUserProfile = (TextView) view.findViewById(R.id.tv_update_user_profile);

        // Set Font of Text View
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,tvCompanyName);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,tvUserName);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,tvHome);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,tvCreate_project);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,tvLogout);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,tvChild);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,tvChildList);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,tvUpdateUserProfile);


        tvCompanyName.setText(userLocalStore.getLoggedInUser().getUser_companyname());
        tvUserName.setText(userLocalStore.getLoggedInUser().getUser_name());

        profileImage = (CircleImageView) view.findViewById(R.id.profile_image);

        logOut = (LinearLayout) view.findViewById(R.id.log_out);

        home_container=(LinearLayout) view.findViewById(R.id.nav_home_container);
        container_createNewProject = (LinearLayout) view.findViewById(R.id.create_new_project);
        container_createChild = (LinearLayout) view.findViewById(R.id.container_create_child);
        containe_list_of_child = (LinearLayout) view.findViewById(R.id.container_list_of_child);
        update_user_profile= (LinearLayout) view.findViewById(R.id.update_user_profile);

        String url = userLocalStore.getLoggedInUser().getUser_photo();


        if(!url.equals("")){
            Picasso.with(getActivity())
                    .load(url)
                    .placeholder(R.drawable.placeholder)
                    .into(profileImage);
        }else{
            profileImage.setImageResource(R.drawable.placeholder);
        }


        if(user_type==1){
            container_createNewProject.setVisibility(View.VISIBLE);
            tvChild.setText(Constant.CREATE_COORDINATOR);
            tvChildList.setText(Constant.COORDINATOR_LIST);

        }else if(user_type==2){
            container_createNewProject.setVisibility(View.GONE);
            tvChild.setText(Constant.CREATE_MANAGER);
            tvChildList.setText(Constant.MANAGER_LIST);
        } else if(user_type==3){
            container_createNewProject.setVisibility(View.GONE);
            tvChild.setText(Constant.CREATE_HAND);
            tvChildList.setText(Constant.HAND_LIST);
        }else{
            container_createNewProject.setVisibility(View.GONE);
            container_createChild.setVisibility(View.GONE);
            containe_list_of_child.setVisibility(View.GONE);
        }





    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        logOut.setOnClickListener(this);
        container_createNewProject.setOnClickListener(this);
        container_createChild.setOnClickListener(this);
        containe_list_of_child.setOnClickListener(this);
        home_container.setOnClickListener(this);
        update_user_profile.setOnClickListener(this);
        profileImage.setOnClickListener(this);
    }

    public void setUp(int fragmentId, DrawerLayout layout, final Toolbar toolbar) {

        containerView = getActivity().findViewById(fragmentId);

        mDrawerLayout = layout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(),mDrawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                //if user gonna not seen the drawer before thats mean the drawer is open for the first time

                if(!mUserLearnedDrawer){
                    mUserLearnedDrawer=true;
                    // save it in sharedpreferences
                    saveToPreferences(getActivity(),KEY_USER_LEARNED_DRAWERR,mUserLearnedDrawer+"");

                    getActivity().invalidateOptionsMenu();
                }

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                //Log.d("Sohel","Offset "+slideOffset);
                /*if(slideOffset<0.4){
                    toolbar.setAlpha(1-slideOffset);
                }*/
            }
        };

        if(!mUserLearnedDrawer && !mFromSavedInstanceState){
            mDrawerLayout.openDrawer(containerView);
        }

        mDrawerLayout.addDrawerListener(mDrawerToggle);

        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
    }

    public static void saveToPreferences(Context context, String key, String prefValue){
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key,prefValue);
        editor.apply();
    }

    public static String readFromPreferences(Context context, String key, String defaultValue){
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        return pref.getString(key,defaultValue);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.log_out:
                userLocalStore.clearUserData();
                getActivity().finish();

                Intent intent = new Intent(getActivity(),HomeActivity.class);
                startActivity(intent);
                break;

            case R.id.nav_home_container:
                //ToDo for the Next Time
                mDrawerLayout.closeDrawer(GravityCompat.START);
                if(user_type==1 || user_type==2){
                    getFragmentManager().beginTransaction().replace(R.id.main_container,new DashBoardFragment()).addToBackStack(null).commit();
                }else{

                    getCurrentProject(userLocalStore.getCurrentProject().getProject_id());


                }
                break;

            case R.id.create_new_project:
                mDrawerLayout.closeDrawer(GravityCompat.START);
                getFragmentManager().beginTransaction().replace(R.id.main_container,new CreateProjectFragment()).addToBackStack(FragName.CREATE_PROJECT).commit();
                break;

            case R.id.container_create_child:
                mDrawerLayout.closeDrawer(GravityCompat.START);
                getFragmentManager().beginTransaction().replace(R.id.main_container,new CreateCoordinatorFragment()).addToBackStack(FragName.CREATE_COORDINATOR).commit();
                break;

            case R.id.container_list_of_child:
                mDrawerLayout.closeDrawer(GravityCompat.START);
                getFragmentManager().beginTransaction().replace(R.id.main_container,new CoordinatorListFragment()).addToBackStack(FragName.COORDINATOR_LIST).commit();
                break;

            case R.id.update_user_profile:
                mDrawerLayout.closeDrawer(GravityCompat.START);
                getFragmentManager().beginTransaction().replace(R.id.main_container,new UpdateUserProfile()).addToBackStack(null).commit();
                break;

            case R.id.profile_image:
                mDrawerLayout.closeDrawer(GravityCompat.START);
                User user = userLocalStore.getLoggedInUser();
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constant.USER,user);
                ViewUserProfile viewUserProfile = new ViewUserProfile();
                viewUserProfile.setArguments(bundle);

                getFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_top,R.anim.exit_to_bottom)
                        .replace(R.id.main_container,viewUserProfile).addToBackStack(null).commit();
                break;
        }

    }

    private void getCurrentProject(int project_id) {

        Map<String,String> map = new HashMap<>();
        map.put(FieldConstant.PROJECT_ID, String.valueOf(project_id));

        MyPostVolley myPostVolley = new MyPostVolley(getActivity(),map, URLs.GET_CURRENT_PROJECT,Constant.FETCHING_PROJECT_DATA);
        myPostVolley.applyPostVolley();
        myPostVolley.setVolleyPostListener(new MyPostVolley.VolleyPostListener() {
            @Override
            public void getResposefromVolley(String response) {
                ProjectResponse pr = gson.fromJson(response,ProjectResponse.class);

                if(pr.getSuccess()==1){
                    List<Project> projectList = pr.getProjects();

                    Project project = projectList.get(0);

                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constant.PROJECT,project);

                    ParticularProjectFragment ppf = new ParticularProjectFragment();
                    ppf.setArguments(bundle);

                    getFragmentManager().beginTransaction().replace(R.id.main_container,ppf).addToBackStack(null).commit();
                }
            }
        });

    }

}
