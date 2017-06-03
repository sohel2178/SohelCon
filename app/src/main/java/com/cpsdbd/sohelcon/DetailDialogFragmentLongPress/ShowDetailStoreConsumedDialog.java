package com.cpsdbd.sohelcon.DetailDialogFragmentLongPress;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cpsdbd.sohelcon.BaseClass.StoreData;
import com.cpsdbd.sohelcon.CustomView.MyTextView;
import com.cpsdbd.sohelcon.R;
import com.cpsdbd.sohelcon.Utility.Constant;
import com.cpsdbd.sohelcon.Utility.FontUtils;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShowDetailStoreConsumedDialog extends DialogFragment {
    private MyTextView tvDate,tvParticular,tvIssueto,tvWhereUsed,tvQuantity,
                date,particular,issueTo,whereUsed,quantity;

    private TextView title;
    private ImageButton btnOk;

    private StoreData data;


    public ShowDetailStoreConsumedDialog() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        View view = inflater.inflate(R.layout.fragment_show_detail_store_consumed_dialog, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        tvDate = (MyTextView) view.findViewById(R.id.show_store_consumed_dialog_date);
        tvParticular = (MyTextView) view.findViewById(R.id.show_store_consumed_dialog_particular);
        tvIssueto = (MyTextView) view.findViewById(R.id.show_store_consumed_dialog_issue_to);
        tvWhereUsed = (MyTextView) view.findViewById(R.id.show_store_consumed_dialog_where_used);
        tvQuantity = (MyTextView) view.findViewById(R.id.show_store_consumed_dialog_quantity);

        title = (TextView) view.findViewById(R.id.title);
        date = (MyTextView) view.findViewById(R.id.date);
        particular = (MyTextView) view.findViewById(R.id.particular);
        issueTo = (MyTextView) view.findViewById(R.id.issue_to);
        whereUsed = (MyTextView) view.findViewById(R.id.where_used);
        quantity = (MyTextView) view.findViewById(R.id.quantity);
        btnOk = (ImageButton) view.findViewById(R.id.show_store_consumed_dialog_ok);

        // Set Font
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,tvDate);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,tvParticular);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,tvIssueto);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,tvWhereUsed);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,tvQuantity);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,title);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,date);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,particular);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,issueTo);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,whereUsed);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,quantity);
        //FontUtils.setFont(Constant.T_RAILWAY_REGULAR,btnOk);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if(getArguments()!=null){
            data = (StoreData) getArguments().getSerializable(Constant.GET_STORE);

            if(data!=null){
                String date = data.getStore_rec_con_date();
                String particular = data.getStore_particular();
                String issue_to = data.getStore_issue_to();
                String where_used = data.getStore_where_used();
                double quantity = data.getStore_quantity();

                tvDate.setText(date);
                tvParticular.setText(particular);
                tvIssueto.setText(issue_to);
                tvWhereUsed.setText(where_used);
                tvQuantity.setText(String.valueOf(quantity));
            }



        }
    }
}
