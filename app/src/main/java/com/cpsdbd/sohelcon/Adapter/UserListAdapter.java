package com.cpsdbd.sohelcon.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import com.cpsdbd.sohelcon.BaseClass.User;
import com.cpsdbd.sohelcon.R;
import com.cpsdbd.sohelcon.Utility.Constant;
import com.cpsdbd.sohelcon.Utility.FontUtils;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Sohel on 10/31/2016.
 */

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.MyUserHolder> {

    private Context context;
    private List<User> userList;
    private LayoutInflater inflater;


    private StatusChangeListener listener;
    private UserClickListener clickListener;

    public UserListAdapter(Context context, List<User> userList) {
        this.userList = userList;
        this.context=context;
        inflater= LayoutInflater.from(context);

    }

    @Override
    public MyUserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.single_user,parent,false);
        MyUserHolder holder = new MyUserHolder(view);
        return holder;
    }

    public void setUserClickListener(UserClickListener userClickListener){
        this.clickListener=userClickListener;
    }

    @Override
    public void onBindViewHolder(MyUserHolder holder, int position) {
        holder.tvName.setText(userList.get(position).getUser_name());
        holder.tvDesignation.setText(userList.get(position).getUser_designation());

        String photo_path = userList.get(position).getUser_photo();

        if(photo_path.equals("")){
            holder.profile_image.setImageResource(R.drawable.placeholder);
        }else{
            Picasso.with(context)
                    .load(userList.get(position).getUser_photo())
                    .placeholder(R.drawable.placeholder)
                    .fit()
                    .into(holder.profile_image);
        }



        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,holder.tvName);
        //FontUtils.setFont(Constant.T_RAILWAY_REGULAR,holder.tvDesignation);

        int user_status = userList.get(position).getUser_status();
        int user_type = userList.get(position).getUser_type();

        if(user_status==1){
            holder.status.setChecked(true);
        }else{
            holder.status.setChecked(false);
        }



    }

    public void setStatusChangeListener(StatusChangeListener listener){
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public void removeItem(User data){
        int position = userList.indexOf(data);
        userList.remove(position);
        notifyItemRemoved(position);
    }

    public void addItem(User data){
        int position = userList.indexOf(data);
        userList.add(data);
        notifyItemInserted(position);
    }

    class MyUserHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener,View.OnClickListener{
        TextView tvName,tvDesignation;
        SwitchCompat status;
        CircleImageView profile_image;
        int howMany;

        public MyUserHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.single_user_user_name);
            tvDesignation = (TextView) itemView.findViewById(R.id.single_user_designation);
            profile_image = (CircleImageView) itemView.findViewById(R.id.single_user_profile_image);

            status= (SwitchCompat) itemView.findViewById(R.id.single_user_stutus);

            status.setOnCheckedChangeListener(this);

            itemView.setOnClickListener(this);

            howMany=0;
        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {



            if(listener!= null){
                listener.statusChange(b,getAdapterPosition());
            }



        }


        @Override
        public void onClick(View view) {
            if(clickListener!= null){
                clickListener.onClickUser(getAdapterPosition());
            }
        }
    }

    public interface StatusChangeListener{
        public void statusChange(boolean status, int position);

    }

    public interface UserClickListener{
        public void onClickUser(int position);
    }
}
