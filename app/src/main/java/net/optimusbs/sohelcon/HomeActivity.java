package net.optimusbs.sohelcon;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;

import com.google.gson.Gson;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import net.optimusbs.sohelcon.Fragments.DashBoardFragment;
import net.optimusbs.sohelcon.Utility.Constant;
import net.optimusbs.sohelcon.Utility.FragName;
import net.optimusbs.sohelcon.Utility.UserLocalStore;

public class HomeActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
    private FragmentManager manager;

    private UserLocalStore userLocalStore;
    private Gson gson;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Iconify
                .with(new FontAwesomeModule());

        gson = new Gson();
        userLocalStore = new UserLocalStore(this);

        manager = getSupportFragmentManager();


        //Toolbar Code
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);




        //Drawer Layout Code
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);



        NavigationDrawer drawerFragment =
                (NavigationDrawer) manager.findFragmentById(R.id.fragment_navigation_drawer);


        drawerFragment.setUp(R.id.fragment_navigation_drawer, mDrawerLayout, toolbar);
        getSupportActionBar().setTitle(Constant.HOME);

        mDrawerLayout.closeDrawer(Gravity.LEFT);

        TextView textView = new TextView(this);
        //textView.setText();

        if(!userLocalStore.getUserLoggedIn()){
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
            finish();
        }else{
            manager.beginTransaction().add(R.id.main_container,new DashBoardFragment(),Constant.DEFAULAT_FRAGMENT_TAG).addToBackStack(FragName.DASHBOARD).commit();

        }



    }

    /*private void setUpFloatingActionButtons(Activity activity){
        // Create an icon
        ImageView icon = new ImageView(activity);
        icon.setImageResource(R.drawable.house_icon2);

        button = new FloatingActionButton.Builder(activity)
                .setContentView(icon)
                .build();

        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(activity);

        ImageView item1 = new ImageView(activity);
        item1.setImageResource(R.drawable.icon_activity);

        ImageView item2 = new ImageView(activity);
        item2.setImageResource(R.drawable.icon_finance);

        ImageView item3 = new ImageView(activity);
        item3.setImageResource(R.drawable.icon_store);



        buttonActivity = itemBuilder.setContentView(item1).build();

        buttonFinace = itemBuilder.setContentView(item2).build();
        buttonStore = itemBuilder.setContentView(item3).build();

        buttonActivity.setPadding(Constant.PADDING,Constant.PADDING,Constant.PADDING,Constant.PADDING);
        buttonFinace.setPadding(Constant.PADDING,Constant.PADDING,Constant.PADDING,Constant.PADDING);
        buttonStore.setPadding(Constant.PADDING,Constant.PADDING,Constant.PADDING,Constant.PADDING);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            buttonActivity.setBackgroundTintList(ResourcesCompat.getColorStateList(getResources(),R.color.colorPrimary,null));
            buttonFinace.setBackgroundTintList(ResourcesCompat.getColorStateList(getResources(),R.color.colorPrimary,null));
            buttonStore.setBackgroundTintList(ResourcesCompat.getColorStateList(getResources(),R.color.colorPrimary,null));
        }




        //attach the sub buttons to the main button
        FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(activity)
                .addSubActionView(buttonActivity)
                .addSubActionView(buttonFinace)
                .addSubActionView(buttonStore)
                .attachTo(button)
                .build();
    }*/


    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();


        /*android.support.v4.app.Fragment fragment= manager.findFragmentById(R.id.main_container);
        if(fragment instanceof DashBoardFragment){
            Log.d("TESTING","YES");
        }else{
            Log.d("TESTING","NO");

        }*/
    }
}
