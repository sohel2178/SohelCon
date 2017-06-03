package com.cpsdbd.sohelcon.Volley;

import android.app.ProgressDialog;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Map;

/**
 * Created by Sohel on 9/21/2016.
 */
public class MyPostVolley {

    private Context context;
    private Map<String,String> params;
    private String url;
    private String progressDialogMessage;

    // Listener
    private VolleyPostListener volleyPostListener;


    public MyPostVolley(Context context, Map<String,String> params, String url, String progressDialogMessage){
        this.context=context;
        this.params = params;
        this.url =url;
        this.progressDialogMessage = progressDialogMessage;
    }

    public void  setVolleyPostListener(VolleyPostListener volleyPostListener){
        this.volleyPostListener =volleyPostListener;
    }

    public void applyPostVolley(){
        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage(progressDialogMessage);
        dialog.show();


        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();

                        //Log.d("BALLLLLLL",response);

                        if(volleyPostListener!=null){
                            volleyPostListener.getResposefromVolley(response);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {



                return params;
            }
        };
        Volley.newRequestQueue(context).add(postRequest);
    }


    public interface VolleyPostListener{
        public void getResposefromVolley(String response);
    }



}
