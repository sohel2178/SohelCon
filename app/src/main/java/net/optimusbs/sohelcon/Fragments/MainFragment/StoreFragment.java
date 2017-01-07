package net.optimusbs.sohelcon.Fragments.MainFragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;


import net.optimusbs.sohelcon.Adapter.StoreAdapter;
import net.optimusbs.sohelcon.BaseClass.StoreData;
import net.optimusbs.sohelcon.ChartAndGraph.MyListener;
import net.optimusbs.sohelcon.DetailDialogFragmentLongPress.ShowDetailStoreConsumedDialog;
import net.optimusbs.sohelcon.DetailDialogFragmentLongPress.ShowDetailStoreReceivedDialog;
import net.optimusbs.sohelcon.R;
import net.optimusbs.sohelcon.Utility.Constant;
import net.optimusbs.sohelcon.Utility.UserLocalStore;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class StoreFragment extends Fragment implements StoreAdapter.StoreLongClickListener{

    private RecyclerView rvStore;

    private UserLocalStore userLocalStore;
    private Gson gson;

    private StoreAdapter adapter;
    private List<StoreData> storeDataList;

    private MyListener listener;






    public StoreFragment() {
        // Required empty public constructor

    }

    public void setMyListener(MyListener listener){
        this.listener=listener;
    }

    @Override
    public void onResume() {
        super.onResume();

        if(listener!= null){
            listener.fragmentChange(5);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userLocalStore = new UserLocalStore(getActivity());
        gson = new Gson();

        if(getArguments() != null){
            storeDataList = (List<StoreData>) getArguments().getSerializable(Constant.STORE_DATA_LIST);
            adapter = new StoreAdapter(getActivity(),storeDataList);
        }






    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_store, container, false);
        initView(view);
        rvStore.setLayoutManager(new LinearLayoutManager(getActivity()));

        if(adapter!=null){
            rvStore.setAdapter(adapter);
        }

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Long Press Item
        adapter.setStoreLongClickListener(this);


    }

    private void initView(View view) {
        rvStore = (RecyclerView) view.findViewById(R.id.rv_store);

    }





    @Override
    public void onLongClick(int position) {
        StoreData data = storeDataList.get(position);

        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.GET_STORE,data);

        if(data.getStore_status()==0){
            ShowDetailStoreReceivedDialog rd = new ShowDetailStoreReceivedDialog();
            rd.setArguments(bundle);
            rd.show(getFragmentManager(),Constant.DEFAULAT_FRAGMENT_TAG);
        }else{
            ShowDetailStoreConsumedDialog rc = new ShowDetailStoreConsumedDialog();
            rc.setArguments(bundle);
            rc.show(getFragmentManager(),Constant.DEFAULAT_FRAGMENT_TAG);
        }
    }


}
