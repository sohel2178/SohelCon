package net.optimusbs.sohelcon.DialogFragments;


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

import com.google.gson.Gson;


import net.optimusbs.sohelcon.BaseClass.StoreData;
import net.optimusbs.sohelcon.CustomView.MyTextView;
import net.optimusbs.sohelcon.Fragments.MainFragment.NewStoreFragment;
import net.optimusbs.sohelcon.R;
import net.optimusbs.sohelcon.Response.RegisterResponse;
import net.optimusbs.sohelcon.Utility.Constant;
import net.optimusbs.sohelcon.Utility.FontUtils;
import net.optimusbs.sohelcon.Utility.URLs;
import net.optimusbs.sohelcon.Volley.MyPostVolley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class StoreReceivedConfirmDialog extends DialogFragment implements View.OnClickListener{
    private MyTextView tvDate,tvParticular,tvReceivedFrom,tvChallanNo,tvMRRNO,tvQuantity,
                        date,particular,receivedFrom,challanNo,mrrNo,quantity;
    private ImageButton btnNo,btnConfirm;

    private TextView title;

    private Gson gson;
    private StoreData data;

    public StoreReceivedConfirmDialog() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gson = new Gson();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_store_received_confirm_dialog, container, false);
        initView(view);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        if(getArguments()!=null){
            data = (StoreData) getArguments().getSerializable(Constant.GET_STORE);

            if(data!=null){
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


    }

    private void initView(View view) {
        tvDate = (MyTextView) view.findViewById(R.id.store_received_dialog_date);
        tvParticular = (MyTextView) view.findViewById(R.id.store_received_dialog_particular);
        tvReceivedFrom = (MyTextView) view.findViewById(R.id.store_received_dialog_received_from);
        tvChallanNo = (MyTextView) view.findViewById(R.id.store_received_dialog_challan_no);
        tvMRRNO = (MyTextView) view.findViewById(R.id.store_received_dialog_mrr_no);
        tvQuantity = (MyTextView) view.findViewById(R.id.store_received_dialog_quantity);

        //,,,,,;
        date = (MyTextView) view.findViewById(R.id.date);
        particular = (MyTextView) view.findViewById(R.id.particular);
        receivedFrom = (MyTextView) view.findViewById(R.id.received_from);
        challanNo = (MyTextView) view.findViewById(R.id.challan_no);
        mrrNo = (MyTextView) view.findViewById(R.id.mrr_no);
        quantity = (MyTextView) view.findViewById(R.id.quantity);

        title = (TextView) view.findViewById(R.id.title);
        title.setText(Constant.STORE_RECEIVED_DIALOG_TITLE);

        btnNo = (ImageButton) view.findViewById(R.id.store_received_dialog_no);
        btnConfirm = (ImageButton) view.findViewById(R.id.store_received_dialog_confirm);

        btnNo.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);

        List<View> views = generateViewList();
        // Set Font
        FontUtils.setFonts(views,Constant.T_RAILWAY_REGULAR);

    }

    private List<View> generateViewList() {
        List<View> viewList = new ArrayList<>();
        viewList.add(tvDate);
        viewList.add(tvParticular);
        viewList.add(tvReceivedFrom);
        viewList.add(tvChallanNo);
        viewList.add(tvMRRNO);
        viewList.add(tvQuantity);
        viewList.add(date);
        viewList.add(particular);
        viewList.add(receivedFrom);
        viewList.add(challanNo);
        viewList.add(mrrNo);
        viewList.add(quantity);
        viewList.add(btnConfirm);
        viewList.add(btnNo);
        viewList.add(title);

        return viewList;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.store_received_dialog_no:
                getDialog().dismiss();
                break;

            case R.id.store_received_dialog_confirm:
                if(data!=null){
                    Map<String,String> map = new HashMap<>();
                    map.put("project_id", String.valueOf(data.getStore_project_id()));
                    map.put("user_id", String.valueOf(data.getStore_user_id()));
                    map.put("rec_con_date",data.getStore_rec_con_date());
                    map.put("particular",data.getStore_particular());
                    map.put("received_from", String.valueOf(data.getStore_received_from()));
                    map.put("challan_no",data.getStore_challan_no());
                    map.put("mrr_no",data.getStore_mrr_no());
                    map.put("quantity", String.valueOf(data.getStore_quantity()));
                    map.put("issue_to","");
                    map.put("where_used","");
                    map.put("status", String.valueOf(data.getStore_status()));

                    MyPostVolley myPostVolley = new MyPostVolley(getActivity(), map, URLs.INSERT_STORE_DATA, Constant.INSERT_STORE_DATA);
                    myPostVolley.applyPostVolley();
                    myPostVolley.setVolleyPostListener(new MyPostVolley.VolleyPostListener() {
                        @Override
                        public void getResposefromVolley(String response) {
                            RegisterResponse rr = gson.fromJson(response, RegisterResponse.class);
                            if (rr.getSuccess() == 1) {
                                getFragmentManager().popBackStack();
                                getFragmentManager().beginTransaction().replace(R.id.main_container, new NewStoreFragment()).commit();
                                getDialog().dismiss();
                            }
                        }
                    });
                }
                break;
        }
    }
}
