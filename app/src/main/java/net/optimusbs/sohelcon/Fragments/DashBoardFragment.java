package net.optimusbs.sohelcon.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;


import net.optimusbs.sohelcon.Adapter.ProjectAdapter;
import net.optimusbs.sohelcon.BaseClass.Project;
import net.optimusbs.sohelcon.BaseClass.User;
import net.optimusbs.sohelcon.CreateProjectActivity;
import net.optimusbs.sohelcon.DialogFragments.UserListDialogFragment;
import net.optimusbs.sohelcon.R;
import net.optimusbs.sohelcon.Response.ChildResponse;
import net.optimusbs.sohelcon.Response.ProjectResponse;
import net.optimusbs.sohelcon.Response.RegisterResponse;
import net.optimusbs.sohelcon.Utility.Constant;
import net.optimusbs.sohelcon.Utility.FieldConstant;
import net.optimusbs.sohelcon.Utility.MyDialog;
import net.optimusbs.sohelcon.Utility.URLs;
import net.optimusbs.sohelcon.Utility.UserLocalStore;
import net.optimusbs.sohelcon.Volley.MyPostVolley;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import jp.wasabeef.recyclerview.animators.ScaleInAnimator;

/**
 * A simple {@link Fragment} subclass.
 */
public class DashBoardFragment extends Fragment implements ProjectAdapter.ProjectItemClickListener,View.OnClickListener {



    private UserLocalStore userLocalStore;
    private Gson gson;

    private RecyclerView rvProject;
    private ProjectAdapter adapter;
    private List<Project> projectList;

    private List<User> userList;
    //private FloatingActionButton btnAdd;

    private int this_user_id;
    private int user_type;
    private int updated_project_id;

    private int selected_id;




    public DashBoardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userLocalStore = new UserLocalStore(getActivity());
        gson = new Gson();
        projectList= new ArrayList<>();
        user_type = userLocalStore.getLoggedInUser().getUser_type();
        adapter = new ProjectAdapter(getActivity(),projectList,user_type);
        adapter.setProjectItemClickListener(this);

        this_user_id = userLocalStore.getLoggedInUser().getUser_id();
        int parent_id = userLocalStore.getLoggedInUser().getParent_id();

        if(user_type==4){

            getProjects(parent_id,user_type);
        }else{
            getProjects(this_user_id,user_type);

        }


    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.all_project));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard_fragments, container, false);
        //btnAdd = (FloatingActionButton) view.findViewById(R.id.action_button);

        rvProject= (RecyclerView) view.findViewById(R.id.rv_all_project);
        rvProject.setLayoutManager(new LinearLayoutManager(getActivity()));
        ScaleInAnimator animator = new ScaleInAnimator();
        animator.setAddDuration(1000);
        animator.setRemoveDuration(3000);
        rvProject.setItemAnimator(animator);
        rvProject.setAdapter(adapter);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //btnAdd.setOnClickListener(this);
    }

    private void getProjects(int userId, final int type){



        Map<String,String> map = new HashMap<>();
        map.put("id", String.valueOf(userId));
        map.put("user_type", String.valueOf(type));

        MyPostVolley myPostVolley = new MyPostVolley(getActivity(),map, URLs.GET_PROJECTS, Constant.FETCHING_PROJECT_DATA);
        myPostVolley.applyPostVolley();
        myPostVolley.setVolleyPostListener(new MyPostVolley.VolleyPostListener() {
            @Override
            public void getResposefromVolley(String response) {

                ProjectResponse projectResponse = gson.fromJson(response,ProjectResponse.class);

                if(projectResponse.getSuccess()==1){
                    List<Project> projects = projectResponse.getProjects();

                    if(type==1 || type ==2){
                        for (Project x: projects){
                            adapter.addItem(x);
                        }
                    }else{
                        Project project = projects.get(0);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(Constant.PROJECT,project);
                        ParticularProjectFragment ppf = new ParticularProjectFragment();
                        ppf.setArguments(bundle);
                        getFragmentManager().beginTransaction().replace(R.id.main_container,ppf).commit();

                    }


                }else{
                    new MyDialog(getActivity(), "Projects!!!!",projectResponse.getMessage());

                }
            }
        });


    }


    @Override
    public void updateProject(int position) {
        Project project = projectList.get(position);
        loadUpdateProjectFragment(project);
    }

    @Override
    public void deteteProject(int position) {
        Project project = projectList.get(position);
        dialogPopUpandDeleteProject(project.getProject_id(),position);
    }



    @Override
    public void openProject(int position) {
        Project project = projectList.get(position);

        openParticularProjectFragment(project);



    }

    @Override
    public void assignCoordinator(int position) {
        updated_project_id = projectList.get(position).getProject_id();
        selected_id = projectList.get(position).getProject_coordinator_id();
        getAllUsers(this_user_id);
    }

    @Override
    public void assignProjectManager(int position) {
        updated_project_id = projectList.get(position).getProject_id();
        selected_id = projectList.get(position).getProject_manager_id();
        getAllUsers(this_user_id);
    }

    private void loadUpdateProjectFragment(Project project){
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.PROJECT, (Serializable) project);
        UpdateProjectFragment updateProjectFragment = new UpdateProjectFragment();
        updateProjectFragment.setArguments(bundle);

        getFragmentManager().beginTransaction().replace(R.id.main_container,updateProjectFragment).commit();


    }

    private void deleteProject(int project_id, final int position) {

        Map<String,String> map = new HashMap<>();
        map.put(FieldConstant.PROJECT_ID, String.valueOf(project_id));

        MyPostVolley myPostVolley = new MyPostVolley(getActivity(),map,URLs.DELETE_PROJECT,Constant.DELETE_PROJECT);
        myPostVolley.applyPostVolley();
        myPostVolley.setVolleyPostListener(new MyPostVolley.VolleyPostListener() {
            @Override
            public void getResposefromVolley(String response) {

                Log.d("MyRESSS",response);
                RegisterResponse registerResponse = gson.fromJson(response,RegisterResponse.class);

                if(registerResponse.getSuccess()==1){
                    projectList.remove(position);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(getActivity(), registerResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getActivity(), registerResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });

    }


    @Override
    public void onClick(View view) {

        Intent intent = new Intent(getActivity(),CreateProjectActivity.class);
        startActivity(intent);
    }

    // Method Popup Dialog and Delete Project
    private void dialogPopUpandDeleteProject(final int project_id, final int position){
        new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("Do You Really Want to delete the Activity!!!!")
                .setCancelText("No,cancel plx!")
                .setConfirmText("Yes,delete it!")
                .showCancelButton(true)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                        deleteProject(project_id,position);
                    }
                })
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                    }
                })
                .show();
    }

    //Open Particular Fragments
    private void openParticularProjectFragment(Project project) {

        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.PROJECT,project);

        ParticularProjectFragment ppf = new ParticularProjectFragment();
        ppf.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.main_container,ppf).addToBackStack(null).commit();
    }


    private void getAllUsers(int parent_id) {
        Map<String,String> map = new HashMap<>();
        map.put(FieldConstant.PARENT_ID, String.valueOf(parent_id));

        MyPostVolley myPostVolley = new MyPostVolley(getActivity(),map, URLs.GET_ALL_CHILD, Constant.GET_ALL_CHILD);
        myPostVolley.applyPostVolley();
        myPostVolley.setVolleyPostListener(new MyPostVolley.VolleyPostListener() {
            @Override
            public void getResposefromVolley(String response) {

                ChildResponse cr = gson.fromJson(response,ChildResponse.class);




                if(cr.getSuccess()==1){

                    userList= cr.getUsers();
                    Bundle bundle= new Bundle();
                    bundle.putSerializable(Constant.USERS, (Serializable) userList);
                    bundle.putInt(Constant.PROJECT_ID,updated_project_id);
                    bundle.putInt(Constant.SELECTED_ID,selected_id);

                    UserListDialogFragment uldf = new UserListDialogFragment();
                    uldf.setTargetFragment(DashBoardFragment.this,Constant.REQUEST_CODE);
                    uldf.setArguments(bundle);

                    uldf.show(getFragmentManager(),Constant.DEFAULAT_FRAGMENT_TAG);

                }else{
                    new MyDialog(getActivity(),"Project Assignment","No Coordinator Found Please Create New Coordinator");
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==Constant.REQUEST_CODE){

            if(resultCode==Constant.REQUEST_CODE) {
                int project_id = data.getIntExtra(FieldConstant.PROJECT_ID,0);
                int user_id = data.getIntExtra(FieldConstant.ID,0);
                String name = data.getStringExtra(FieldConstant.NAME);


                Project project = getProject(project_id);

                if(user_type==1){
                    project.setProject_coordinator_id(user_id);
                    project.setProject_coordinator_name(name);
                }else if(user_type==2){
                    project.setProject_manager_id(user_id);
                    project.setProject_manager_name(name);
                }


                adapter.notifyDataSetChanged();






            }

        }
    }

    private Project getProject(int project_id){
        Project project = null;

        for(Project x :projectList){
            if(x.getProject_id()==project_id){
                project=x;
                break;
            }
        }
        return project;
    }
}
