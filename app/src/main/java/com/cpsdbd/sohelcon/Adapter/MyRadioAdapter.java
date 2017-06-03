package com.cpsdbd.sohelcon.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.cpsdbd.sohelcon.BaseClass.User;
import com.cpsdbd.sohelcon.R;
import com.cpsdbd.sohelcon.Utility.Constant;
import com.cpsdbd.sohelcon.Utility.FontUtils;

import java.util.List;

/**
 * Created by Sohel on 11/30/2016.
 */

public class MyRadioAdapter extends RecyclerView.Adapter<MyRadioAdapter.MyHolder> {

    private Context context;
    private LayoutInflater inflater;
    private List<User> userList;
    private int selectedPos;
    private MyRadioListener listener;

    public MyRadioAdapter(Context context,List<User> userList,int selectedPos){
        this.context =context;
        this.userList =userList;
        this.selectedPos =selectedPos;

        inflater =LayoutInflater.from(context);
    }

    public void setMyRadioListener(MyRadioListener listener){
        this.listener=listener;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.single_radio_holder,parent,false);

        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {

        if(userList.get(position).getUser_id()==selectedPos){
            holder.radio.setChecked(true);
        }else{
            holder.radio.setChecked(false);
        }


        holder.name.setText(userList.get(position).getUser_name());

        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,holder.name);

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private RadioButton radio;
        private TextView name;


        public MyHolder(View itemView) {
            super(itemView);

            radio = (RadioButton) itemView.findViewById(R.id.radio);
            name = (TextView) itemView.findViewById(R.id.text);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            User user = userList.get(getAdapterPosition());
            selectedPos =user.getUser_id();
            notifyDataSetChanged();

            if(listener!= null){
                listener.onClickRadio(selectedPos);
            }

        }
    }

    public interface MyRadioListener{
        public void onClickRadio(int id);
    }
}
