package net.optimusbs.sohelcon.DetailDialogFragmentLongPress;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import net.optimusbs.sohelcon.BaseClass.StoreData;
import net.optimusbs.sohelcon.CustomView.MyTextView;
import net.optimusbs.sohelcon.R;
import net.optimusbs.sohelcon.Utility.Constant;
import net.optimusbs.sohelcon.Utility.FontUtils;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShowDetailStoreReceivedDialog extends DialogFragment implements View.OnClickListener {

    private MyTextView tvDate,tvParticular,tvReceivedFrom,tvChallanNo,tvMRRNO,tvQuantity,
                date,particular,receivedFrom,challanNo,mrrNo,quantity;

    private TextView title;
    private ImageButton btnOK;

    private StoreData data;


    public ShowDetailStoreReceivedDialog() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments()!= null){
            data = (StoreData) getArguments().getSerializable(Constant.GET_STORE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_show_detail_store_received_dialog, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        tvDate = (MyTextView) view.findViewById(R.id.show_store_received_dialog_date);
        tvParticular = (MyTextView) view.findViewById(R.id.show_store_received_dialog_particular);
        tvReceivedFrom = (MyTextView) view.findViewById(R.id.show_store_received_dialog_received_from);
        tvChallanNo = (MyTextView) view.findViewById(R.id.show_store_received_dialog_challan_no);
        tvMRRNO = (MyTextView) view.findViewById(R.id.show_store_received_dialog_mrr_no);
        tvQuantity = (MyTextView) view.findViewById(R.id.show_store_received_dialog_quantity);

        title = (TextView) view.findViewById(R.id.title);
        date = (MyTextView) view.findViewById(R.id.date);
        particular = (MyTextView) view.findViewById(R.id.particular);
        receivedFrom = (MyTextView) view.findViewById(R.id.received_from);
        challanNo = (MyTextView) view.findViewById(R.id.challan_no);
        mrrNo = (MyTextView) view.findViewById(R.id.mrr_no);
        quantity = (MyTextView) view.findViewById(R.id.quantity);

        btnOK = (ImageButton) view.findViewById(R.id.show_store_received_dialog_ok);

        // Set Font
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,tvDate);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,tvParticular);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,tvReceivedFrom);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,tvChallanNo);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,tvMRRNO);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,tvQuantity);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,title);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,date);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,particular);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,receivedFrom);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,challanNo);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,mrrNo);
        FontUtils.setFont(Constant.T_RAILWAY_REGULAR,quantity);
        //FontUtils.setFont(Constant.T_RAILWAY_REGULAR,btnOK);

        btnOK.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();

        if(data!= null){
            String date = data.getStore_rec_con_date();
            String particular = data.getStore_particular();
            String received_from = data.getStore_received_from();
            String challan_no = data.getStore_challan_no();
            String mrr_no = data.getStore_mrr_no();
            double quantity = data.getStore_quantity();

            tvDate.setText(date);
            tvParticular.setText(particular);
            tvReceivedFrom.setText(received_from);
            tvChallanNo.setText(challan_no);
            tvMRRNO.setText(mrr_no);
            tvQuantity.setText(String.valueOf(quantity));

        }
    }

    @Override
    public void onClick(View view) {
        getDialog().dismiss();
    }
}
