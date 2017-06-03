package com.cpsdbd.sohelcon.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.cpsdbd.sohelcon.BaseClass.FinanceData;
import com.cpsdbd.sohelcon.CustomView.MyTextView;
import com.cpsdbd.sohelcon.R;
import com.cpsdbd.sohelcon.Utility.Constant;
import com.cpsdbd.sohelcon.Utility.FontUtils;
import com.cpsdbd.sohelcon.Utility.MyUtils;

import java.util.List;

/**
 * Created by Sohel on 9/27/2016.
 */
public class FinanceAdapter extends RecyclerView.Adapter<FinanceAdapter.MyFinanceHolder>{

    private Context context;
    private LayoutInflater inflater;
    private List<FinanceData> financeDataList;
    private FinanceLongClickListener listener;
    private int previousposition =0;

    public FinanceAdapter(Context context, List<FinanceData> financeDataList){
        this.context=context;
        this.financeDataList= financeDataList;
        inflater= LayoutInflater.from(context);
    }

    public void setFinanceLongClickListener(FinanceLongClickListener listener){
        this.listener = listener;
    }

    @Override
    public MyFinanceHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = null;
        switch (viewType){
            case 0:
                view=inflater.inflate(R.layout.single_debit,parent,false);
                break;
            case 1:
                view=inflater.inflate(R.layout.single_credit,parent,false);
                break;
        }

        MyFinanceHolder holder = new MyFinanceHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(MyFinanceHolder holder, int position) {

        holder.tvDate.setText(financeDataList.get(position).getFinance_transaction_date());

        holder.tvParticular.setText(financeDataList.get(position).getFinance_particular());

        holder.tvAmount.setText(String.valueOf(financeDataList.get(position).getFinance_amount()));

        //Set Font
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,holder.tvDate);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,holder.tvParticular);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,holder.tvAmount);



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
    public int getItemCount() {
        return financeDataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        FinanceData data = financeDataList.get(position);
        if(data.getFinance_status()==0){
            return 0;
        }else{
            return 1;
        }
    }


    class MyFinanceHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        MyTextView tvDate,tvParticular,tvAmount;

        public MyFinanceHolder(View itemView) {
            super(itemView);
            tvDate = (MyTextView) itemView.findViewById(R.id.single_tran_date);
            tvParticular = (MyTextView) itemView.findViewById(R.id.single_tran_particular);
            tvAmount = (MyTextView) itemView.findViewById(R.id.single_tran_amount);

            itemView.setOnClickListener(this);
        }




        @Override
        public void onClick(View view) {

            if(listener!= null){
                listener.onItemLongClick(getAdapterPosition());
            }

        }
    }

    public interface FinanceLongClickListener{
        public void onItemLongClick(int position);
    }

}
