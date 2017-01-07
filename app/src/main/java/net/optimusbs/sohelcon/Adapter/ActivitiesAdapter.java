package net.optimusbs.sohelcon.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.joanzapata.iconify.widget.IconButton;


import net.optimusbs.sohelcon.BaseClass.ActivityData;
import net.optimusbs.sohelcon.R;
import net.optimusbs.sohelcon.Utility.Constant;
import net.optimusbs.sohelcon.Utility.FontUtils;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Sohel on 10/4/2016.
 */
public class ActivitiesAdapter extends RecyclerView.Adapter<ActivitiesAdapter.MyActivitiesHolder> implements Serializable {

    private Context context;
    private List<ActivityData> activityDataList;
    private LayoutInflater inflater;

    private ActivityItemClickListener listener;

    public ActivitiesAdapter(Context context, List<ActivityData> activityDataList){
        this.context = context;
        this.activityDataList = activityDataList;
        this.inflater = LayoutInflater.from(context);
    }


    @Override
    public MyActivitiesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.single_activity,parent,false);

        MyActivitiesHolder holder = new MyActivitiesHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(MyActivitiesHolder holder, int position) {

        holder.tvActivityName.setText(activityDataList.get(position).getActivity_name());
        holder.tvVolumeOfWorks.setText(String.valueOf(activityDataList.get(position).getActivity_volume_of_works()));
        holder.tvVolumeOfWorkdone.setText(String.valueOf(activityDataList.get(position).getActivity_volume_of_work_done()));

        // Set The Font

        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,holder.tvActivityName);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,holder.tvVolumeOfWorks);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,holder.tvVolumeOfWorkdone);

        //MyUtils.animate(holder);

    }

    public void setActivityItemClickListener(ActivityItemClickListener listener){
        this.listener = listener;
    }

    public void removeItem(ActivityData data){
        int position = activityDataList.indexOf(data);
        activityDataList.remove(position);
        notifyItemRemoved(position);
    }

    public void addItem(ActivityData data){
        int position = activityDataList.indexOf(data);
        activityDataList.add(data);
        notifyItemInserted(position);
    }

    @Override
    public int getItemCount() {
        return activityDataList.size();
    }

    class MyActivitiesHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{

        TextView tvActivityName,tvVolumeOfWorks,tvVolumeOfWorkdone;

        IconButton btnDelete,btnUpdate,btnWorkDone;

        public MyActivitiesHolder(View itemView) {
            super(itemView);

            tvActivityName = (TextView) itemView.findViewById(R.id.single_activity_activity_name);
            tvVolumeOfWorks = (TextView) itemView.findViewById(R.id.single_activity_volume_of_works);
            tvVolumeOfWorkdone = (TextView) itemView.findViewById(R.id.single_activity_volume_of_workdone);

            btnDelete = (IconButton) itemView.findViewById(R.id.single_activity_btn_delete);
            btnUpdate = (IconButton) itemView.findViewById(R.id.single_activity_btn_update);
            btnWorkDone = (IconButton) itemView.findViewById(R.id.single_activity_btn_work_done);

            btnDelete.setOnClickListener(this);
            btnUpdate.setOnClickListener(this);
            btnWorkDone.setOnClickListener(this);

            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {

            switch (view.getId()){
                case R.id.single_activity_btn_delete:
                    if(listener!=null){
                        listener.deteteItem(getAdapterPosition());
                    }
                    break;

                case R.id.single_activity_btn_update:
                    if(listener!=null){
                        listener.updateItem(getAdapterPosition());
                    }
                    break;

                case R.id.single_activity_btn_work_done:
                    if(listener!=null){
                        listener.workDone(getAdapterPosition());
                    }
                    break;
            }

        }

        @Override
        public boolean onLongClick(View view) {
            if(listener!= null){
                listener.showDetail(getAdapterPosition());
            }
            return true;
        }
    }

    public interface ActivityItemClickListener{
        public void deteteItem(int position);
        public void updateItem(int position);
        public void showDetail(int position);
        public void workDone(int position);
    }
}
