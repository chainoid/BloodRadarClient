package com.chainoid.bloodradar.brapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nispok.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class QueryItemsActivity extends AppCompatActivity {

    private String JSON_STRING;
    private String[] bpackId;
    private String[] btype;
    private String[] donorId;
    private String[] donationTS;
    private String[] amount;
    private String[] location;
    private String[] status;

    private TextView toolbarTitle;
    private ImageView imgLogout;
    private RecyclerView mRecyclerView;
    private QueryItemsRecyclerAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Context context=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_items);
        toolbarTitle=(TextView)findViewById(R.id.toolbar_title);
        imgLogout=(ImageView)findViewById(R.id.imgLogout);
        mRecyclerView = (RecyclerView) findViewById(R.id.query_items_recycler_view);

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(10000);
        itemAnimator.setRemoveDuration(10000);
        mRecyclerView.setItemAnimator(itemAnimator);

        toolbarTitle.setText(Config.mUserType);
        imgLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                logout();
            }
        });
        if(Config.IfGetAllDonors){
            getAllItems();
        }else{
            getSingleItem();
        }

    }

    public void getAllItems(){


        final String URL_GET_DONORS_BY_BTYPE="http://"+Config.ServerIP+":"+Config.Port+"/query_bpack_by_btype/"+Config.Btype+"-ALL";
        class GetJSON extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(URL_GET_DONORS_BY_BTYPE);
                return s;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                JSON_STRING = s;

                if (s != null && s.length() > 0 ) {
                    getAllItemsResult();
                    setAllItems();
                } else {
                    showDonorError();
                }
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    private void getAllItemsResult(){
        JSONObject jsonObject = null;

        try {
            JSONArray result = new JSONArray(JSON_STRING);
            bpackId =new String[result.length()];
            btype = new String[result.length()];
            donorId =new String[result.length()];
            donationTS =new String[result.length()];
            amount =new String[result.length()];
            location =new String[result.length()];
            status = new String[result.length()];


            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                try {
                    bpackId[i]=jo.getString("Key");

                    String Record=jo.getString("Record");
                    try{
                        JSONObject joRecord=new JSONObject(Record);
                        btype[i]=joRecord.getString("btype");
                        donorId[i]=joRecord.getString("donorId");
                        donationTS[i]=joRecord.getString("donationTS");
                        amount[i]=joRecord.getString("amount");
                        location[i]=joRecord.getString("location");
                        status[i]=joRecord.getString("status");

                    }catch (Exception e){
                        btype[i]="-";
                        donorId[i]="-";
                        donationTS[i]="-";
                        amount[i]="-";
                        location[i]="-";
                        status[i]="-";
                    }
                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
            showDonorError();
        }
    }

    private void setAllItems(){
        mAdapter = new QueryItemsRecyclerAdapter(bpackId, btype, donorId, donationTS, amount, location, status, context);
        mRecyclerView.setAdapter(mAdapter);
    }

    public  void logout(){
        class LogoutAsync extends AsyncTask<Void, Void, Void> {
            ProgressDialog loadingDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(QueryItemsActivity.this, "Please wait", "Logging out...");
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (loadingDialog!=null && loadingDialog.isShowing())
                {
                    loadingDialog.dismiss();
                    Intent intent = new Intent(QueryItemsActivity.this, LoginActivity.class);
                    finish();
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    overridePendingTransition(R.anim.back_slide_out, R.anim.back_slide_in);
                }
            }

            @Override
            protected Void doInBackground(Void... params) {
                try{
                    SharedPreferences sharedpreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.clear();
                    editor.commit();
                    Thread.sleep(3000);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
                return (Void)null;
            }

        }
        LogoutAsync loa = new LogoutAsync();
        loa.execute();

    }

    public void getSingleItem(){
        final String URL_GET_DONOR="http://"+Config.ServerIP+":"+Config.Port+"/get_donor_by_id/"+Config.DonorID;
        class GetJSONProduct extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(URL_GET_DONOR);
                return s;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                JSON_STRING = s;

                if (s != null && s.length() > 0 ) {
                    getItemResult();
                    setAllItems();
                } else {
                    showDonorError();
                }

            }
        }
        GetJSONProduct gjp = new GetJSONProduct();
        gjp.execute();
    }

    private void getItemResult(){
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(JSON_STRING);
            bpackId =new String[1];
            btype =new String[1];
            donorId =new String[1];
            donationTS =new String[1];
            amount =new String[1];
            location =new String[1];
            status =new String[1];

            bpackId[0]=Config.BpackID;
            btype[0]=jsonObject.getString("btype");
            donorId[0]=jsonObject.getString("donorId");
            donationTS[0]=jsonObject.getString("donationTS");
            amount[0]=jsonObject.getString("amount");
            location[0]=jsonObject.getString("location");
            status[0]=jsonObject.getString("status");


        } catch (JSONException e) {
            btype[0]="-";
            donorId[0]="-";
            donationTS[0]="-";
            amount[0]="-";
            location[0]="-";
            status[0]="-";
         }
    }



    private void showDonorError(){
       Snackbar.with(getApplicationContext()).text("Cannot load data for this donor(s).").show(this);
    }



    @Override
    public void onBackPressed() {
        Intent intent = new Intent(QueryItemsActivity.this, OptionsActivity.class);
        finish();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.back_slide_out, R.anim.back_slide_in);
    }
}
