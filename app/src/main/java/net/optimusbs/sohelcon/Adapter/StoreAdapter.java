package net.optimusbs.sohelcon.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import net.optimusbs.sohelcon.BaseClass.StoreData;
import net.optimusbs.sohelcon.CustomView.MyTextView;
import net.optimusbs.sohelcon.R;
import net.optimusbs.sohelcon.Utility.Constant;
import net.optimusbs.sohelcon.Utility.FontUtils;
import net.optimusbs.sohelcon.Utility.MyUtils;

import java.util.List;

/**
 * Created by Sohel on 10/2/2016.
 */
public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.MyStoreHolder> {

    private Context context;
    private LayoutInflater inflater;
    private List<StoreData> storeDataList;
    private StoreLongClickListener listener;
    private int previousposition =0;

    private String gravitySetter;

    public StoreAdapter(Context context, List<StoreData> storeDataList){
        this.context = context;
        this.storeDataList = storeDataList;
        inflater = LayoutInflater.from(context);
    }

    public void setGravitySetter(String gravitySetter){
        this.gravitySetter=gravitySetter;
    }

    @Override
    public MyStoreHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType){
            case 0:
                view=inflater.inflate(R.layout.single_store_received,parent,false);
                break;
            case 1:
                view=inflater.inflate(R.layout.single_store_consumed,parent,false);
                break;
        }

        MyStoreHolder holder = new MyStoreHolder(view);

        return holder;
    }

    public void setStoreLongClickListener(StoreLongClickListener listener){
        this.listener =listener;
    }

    @Override
    public void onBindViewHolder(MyStoreHolder holder, int position) {



        holder.tvDate.setText(storeDataList.get(position).getStore_rec_con_date());
        holder.tvParticular.setText(storeDataList.get(position).getStore_particular());
        holder.tvQuantity.setText(String.valueOf(storeDataList.get(position).getStore_quantity()));

        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,holder.tvDate);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,holder.tvParticular);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,holder.tvQuantity);

        if(gravitySetter!=null){
            if(gravitySetter.equals(Constant.GRAVITY_END)){
                holder.tvQuantity.setGravity(Gravity.END);
            }
        }

        //Animation Part Start Here
        if(position>previousposition){
            //Scrolling Down
            MyUtils.animate(context,holder,true);

        }else{
            //Scrolling Up
            MyUtils.animate(context,holder,false);
        }

        previousposition=position;


    }



    @Override
    public int getItemViewType(int position) {
        StoreData data = storeDataList.get(position);
        if(data.getStore_status()==0){
            return 0;
        }else{
            return 1;
        }
    }

    @Override
    public int getItemCount() {
        return storeDataList.size();
    }

    class MyStoreHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        MyTextView tvDate,tvParticular,tvQuantity;

        public MyStoreHolder(View itemView) {
            super(itemView);

            tvDate = (MyTextView) itemView.findViewById(R.id.single_store_rec_con_date);
            tvParticular = (MyTextView) itemView.findViewById(R.id.single_store_particular);
            tvQuantity = (MyTextView) itemView.findViewById(R.id.single_store_quantity);

            itemView.setOnClickListener(this);
        }



        @Override
        public void onClick(View view) {
            if(listener!=null){
                listener.onLongClick(getAdapterPosition());
            }


        }
    }

    public interface StoreLongClickListener{
        public void onLongClick(int position);
    }
}
