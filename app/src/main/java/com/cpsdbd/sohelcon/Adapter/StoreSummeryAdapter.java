package com.cpsdbd.sohelcon.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.cpsdbd.sohelcon.BaseClass.SummeryStore;
import com.cpsdbd.sohelcon.R;
import com.cpsdbd.sohelcon.Utility.Constant;
import com.cpsdbd.sohelcon.Utility.FontUtils;

import java.util.List;

/**
 * Created by Sohel on 10/6/2016.
 */

public class StoreSummeryAdapter extends RecyclerView.Adapter<StoreSummeryAdapter.MyStoreSummeryHolder> {

    private Context context;
    private List<SummeryStore> summeryStoreList;
    private LayoutInflater inflater;

    private StoreSummeryListener listener;

    public StoreSummeryAdapter(Context context, List<SummeryStore> summeryStoreList){
        this.context=context;
        this.summeryStoreList=summeryStoreList;
        inflater = LayoutInflater.from(context);
    }

    public void setStoreSummeryListener(StoreSummeryListener listener){
        this.listener = listener;
    }


    @Override
    public MyStoreSummeryHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.single_store_summery,parent,false);

        MyStoreSummeryHolder holder = new MyStoreSummeryHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyStoreSummeryHolder holder, int position) {
        holder.tvItem.setText(summeryStoreList.get(position).getItem());
        holder.tvReceived.setText(String.valueOf(summeryStoreList.get(position).getTotal_received()));
        holder.tvConsumed.setText(String.valueOf(summeryStoreList.get(position).getTotal_consumed()));
        holder.tvBalance.setText(String.valueOf(summeryStoreList.get(position).getBalance()));

        // Set the Font
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,holder.tvItem);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,holder.tvReceived);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,holder.tvConsumed);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,holder.tvBalance);

    }

    @Override
    public int getItemCount() {
        return summeryStoreList.size();
    }

    class MyStoreSummeryHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView tvItem,tvReceived,tvConsumed,tvBalance;

        public MyStoreSummeryHolder(View itemView) {
            super(itemView);

            tvItem = (TextView) itemView.findViewById(R.id.store_item);
            tvReceived = (TextView) itemView.findViewById(R.id.store_received);
            tvConsumed = (TextView) itemView.findViewById(R.id.store_consumed);
            tvBalance = (TextView) itemView.findViewById(R.id.store_balance);

            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            if(listener!= null){
                listener.onLongClickItem(summeryStoreList.get(getAdapterPosition()));
            }

        }
    }

    public  interface StoreSummeryListener{
        public void onLongClickItem(SummeryStore store);
    }
}
