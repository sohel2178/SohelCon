package com.cpsdbd.sohelcon.Adapter;

import android.content.Context;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import com.joanzapata.iconify.widget.IconTextView;

import com.cpsdbd.sohelcon.BaseClass.Project;
import com.cpsdbd.sohelcon.R;
import com.cpsdbd.sohelcon.Utility.Constant;
import com.cpsdbd.sohelcon.Utility.FontUtils;
import com.cpsdbd.sohelcon.Utility.MyUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by Sohel on 9/25/2016.
 */
public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.MyProjectHolder> {

    private Context context;
    private LayoutInflater inflater;
    private List<Project> projectList;
    private ProjectItemClickListener listener;
    private int user_type;

    public ProjectAdapter(Context context, List<Project> projectList, int user_type){
        this.context=context;
        this.projectList=projectList;
        inflater= LayoutInflater.from(context);
        this.user_type=user_type;
    }

    public void setProjectItemClickListener(ProjectItemClickListener listener){
        this.listener=listener;
    }

    @Override
    public MyProjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.single_project,parent,false);
        switch (viewType){
            case 0:
                view.setBackgroundColor(ResourcesCompat.getColor(context.getResources(),R.color.odd_row,null));
                break;
            case 1:
                view.setBackgroundColor(ResourcesCompat.getColor(context.getResources(),R.color.even_row,null));
                break;
        }
        MyProjectHolder holder = new MyProjectHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyProjectHolder holder, int position) {
        String project_name = projectList.get(position).getProject_name();
        String siteLocation = projectList.get(position).getProject_location();
        int coordinator_id = projectList.get(position).getProject_coordinator_id();
        int manager_id = projectList.get(position).getProject_manager_id();
        String coordinator = projectList.get(position).getProject_coordinator_name();
        String manager = projectList.get(position).getProject_manager_name();
        double physical_progress = projectList.get(position).getProject_physical_progress();

        String completion_dateStr = projectList.get(position).getProject_completion_date();
        String start_date = projectList.get(position).getProject_start_date();


        Date compDate = MyUtils.getDateFromString(completion_dateStr);
        String daysLeft = String.valueOf(MyUtils.dayDiff(new Date(),compDate));

        if(user_type==1){
            // Visibility for Owner
            holder.btnDelete.setVisibility(View.VISIBLE);
            holder.btnUpdate.setVisibility(View.VISIBLE);
            holder.btnAssignManager.setVisibility(View.GONE);
            if(coordinator_id==0){
                holder.btnAssignCoordinator.setText(context.getResources().getText(R.string.icon_cross));
            }else {
                holder.btnAssignCoordinator.setText(context.getResources().getText(R.string.icon_tick));
            }
        }

        if(user_type==2){
            // Visibility for Co-ordinator
            holder.btnAssignCoordinator.setVisibility(View.GONE);
            if(manager_id==0){
                holder.btnAssignManager.setText(context.getResources().getText(R.string.icon_cross));

            }else {
                holder.btnAssignManager.setText(context.getResources().getText(R.string.icon_tick));
            }
        }

        holder.tvProjectName.setText(project_name);
        holder.tvSiteLocation.setText(siteLocation);
        holder.tvCoordinatorName.setText(coordinator);
        holder.tvProjectManagerName.setText(manager);
        holder.tvPhysicalProgress.setText(String.valueOf(physical_progress*100)+" %");
        holder.tvDaysLeft.setText(daysLeft);
        holder.tvStartDate.setText(start_date);
        holder.tvCompletionDate.setText(completion_dateStr);




    }

    public void removeItem(Project project){
        int position = projectList.indexOf(project);
        projectList.remove(position);
        notifyItemRemoved(position);
    }

    public void addItem(Project project){
        int position = projectList.indexOf(project);
        projectList.add(project);
        notifyItemInserted(position);
    }

    public void addItem(Project project,int position){
        projectList.add(position,project);
        notifyItemInserted(position);
    }

    @Override
    public int getItemCount() {
        return projectList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(position%2==0){
            return 0;
        }else{
            return 1;
        }
    }

    class MyProjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView tvProjectName,tvSiteLocation,tvCoordinatorName,tvProjectManagerName,tvPhysicalProgress,tvDaysLeft,tvStartDate,tvCompletionDate,
                        completed,daysLeft,startDate,completeDate,coordinator,manager;
        Button btnUpdate,btnDelete;

        IconTextView btnAssignCoordinator,btnAssignManager;

        public MyProjectHolder(View itemView) {
            super(itemView);
            tvProjectName = (TextView) itemView.findViewById(R.id.single_project_project_name);
            tvSiteLocation = (TextView) itemView.findViewById(R.id.single_project_site_location);
            tvPhysicalProgress = (TextView) itemView.findViewById(R.id.tv_physical_progress);
            tvCoordinatorName = (TextView) itemView.findViewById(R.id.tv_coordinator_name);
            tvProjectManagerName = (TextView) itemView.findViewById(R.id.tv_project_manager_name);
            tvDaysLeft = (TextView) itemView.findViewById(R.id.tv_days_left);
            tvStartDate = (TextView) itemView.findViewById(R.id.tv_start_date_of_project);
            tvCompletionDate = (TextView) itemView.findViewById(R.id.tv_completion_date_of_project);


            completed = (TextView) itemView.findViewById(R.id.completed);
            daysLeft = (TextView) itemView.findViewById(R.id.days_left);
            startDate = (TextView) itemView.findViewById(R.id.start_date);
            completeDate = (TextView) itemView.findViewById(R.id.completion_date);
            coordinator = (TextView) itemView.findViewById(R.id.coordinator);
            manager = (TextView) itemView.findViewById(R.id.manager);




            btnUpdate = (Button) itemView.findViewById(R.id.single_project_update);
            btnDelete = (Button) itemView.findViewById(R.id.single_project_delete);
            //btnOpen = (Button) itemView.findViewById(R.id.single_project_open);

            btnAssignCoordinator = (IconTextView) itemView.findViewById(R.id.btn_assign_coordinator);
            btnAssignManager = (IconTextView) itemView.findViewById(R.id.btn_assign_project_manager);


            // Set the Font in Here
            FontUtils.setFont(Constant.T_RAILWAY_REGULAR,tvProjectName);
            FontUtils.setFont(Constant.T_RAILWAY_REGULAR,tvSiteLocation);
            FontUtils.setFont(Constant.T_RAILWAY_REGULAR,tvPhysicalProgress);
            FontUtils.setFont(Constant.T_RAILWAY_REGULAR,tvCoordinatorName);
            FontUtils.setFont(Constant.T_RAILWAY_REGULAR,tvProjectManagerName);
            FontUtils.setFont(Constant.T_RAILWAY_REGULAR,tvDaysLeft);
            FontUtils.setFont(Constant.T_RAILWAY_REGULAR,tvStartDate);
            FontUtils.setFont(Constant.T_RAILWAY_REGULAR,tvCompletionDate);
            FontUtils.setFont(Constant.T_RAILWAY_REGULAR,btnAssignCoordinator);
            FontUtils.setFont(Constant.T_RAILWAY_REGULAR,btnAssignManager);


            FontUtils.setFont(Constant.T_RAILWAY_REGULAR,completed);
            FontUtils.setFont(Constant.T_RAILWAY_REGULAR,daysLeft);
            FontUtils.setFont(Constant.T_RAILWAY_REGULAR,startDate);
            FontUtils.setFont(Constant.T_RAILWAY_REGULAR,completeDate);
            FontUtils.setFont(Constant.T_RAILWAY_REGULAR,coordinator);
            FontUtils.setFont(Constant.T_RAILWAY_REGULAR,manager);



            btnUpdate.setOnClickListener(this);
            btnDelete.setOnClickListener(this);
            //btnOpen.setOnClickListener(this);
            btnAssignCoordinator.setOnClickListener(this);
            btnAssignManager.setOnClickListener(this);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {

            switch (view.getId()){
                case R.id.single_project_update:
                    if(listener!=null){
                        listener.updateProject(getAdapterPosition());
                    }
                    break;
                case R.id.single_project_delete:
                    if(listener!=null){
                        listener.deteteProject(getAdapterPosition());
                    }
                    break;
                /*case R.id.single_project_open:
                    if(listener!=null){
                        listener.openProject(getAdapterPosition());
                    }
                    break;*/
                case R.id.btn_assign_coordinator:
                    if(listener!=null){
                        listener.assignCoordinator(getAdapterPosition());
                    }
                    break;
                case R.id.btn_assign_project_manager:
                    if(listener!=null){
                        listener.assignProjectManager(getAdapterPosition());
                    }
                    break;

                default:
                    if(listener!=null){
                        listener.openProject(getAdapterPosition());
                    }
                    break;
            }

        }
    }

    public interface ProjectItemClickListener{
        public void updateProject(int position);
        public void deteteProject(int position);
        public void openProject(int position);
        public void assignCoordinator(int position);
        public void assignProjectManager(int position);
    }
}
