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

public class ItemHistoryActivity extends AppCompatActivity {

    private String JSON_STRING;

    private String[] bpackId;

    private String[] donorId;

    private String[] donationTS;
    private String[] amount;
    private String[] location;
    private String[] status;


    private String[] txTS;

    private String[] name;
    private String[] address;
    private String[] phone;

    private String[] holder;


    private TextView toolbarTitle;
    private ImageView imgLogout;
    private RecyclerView mRecyclerView;
    private ItemHistoryRecyclerAdapter mAdapter;
    private DonorHistoryRecyclerAdapter mAdapter2;

    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.LayoutManager mLayoutManager2;

    private Context context=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_history);
        toolbarTitle=(TextView)findViewById(R.id.toolbar_title);
        imgLogout=(ImageView)findViewById(R.id.imgLogout);
        mRecyclerView = (RecyclerView) findViewById(R.id.item_history_recycler_view);

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
        if (Config.mUserType.toLowerCase().equals("hospital")){
            getItemHistory();
        }else if (Config.mUserType.toLowerCase().equals("donor")){
            getDonorHistory();
        }

    }

    public void getItemHistory(){


        final String URL_GET_ITEM_HISTORY="http://"+Config.ServerIP+":"+Config.Port+"/bpack_history/"+Config.BpackID;
        class GetJSON extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(URL_GET_ITEM_HISTORY);
                return s;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                JSON_STRING = s;

                if (s != null && s.length() > 0 && !s.contains("No data found")) {
                    getItemHitoryResult();
                    setItemHistory();
                } else {
                    showDonorError();
                }
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    private void getItemHitoryResult(){
        JSONObject jsonObject = null;

        try {
            JSONArray result = new JSONArray(JSON_STRING);

            bpackId = new String[result.length()];
            txTS    = new String[result.length()];
            holder  = new String[result.length()];
            status  = new String[result.length()];

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                try {

                    String Record=jo.getString("Record");
                    String transTS=jo.getString("TxTS");

                    try{

                        JSONObject joRecord=new JSONObject(Record);

                        //amount[i]=joRecord.getString("amount");

                        bpackId[i]=Config.BpackID;
                        holder[i]=joRecord.getString("holder");
                        status[i]=joRecord.getString("status");

                        txTS[i]=transTS;

                    }catch (Exception e){
                        //btype[i]="-";
                        //donorId[i]="-";
                        bpackId[i]="-";
                        holder[i]="-";
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

    private void setItemHistory(){
        mAdapter = new ItemHistoryRecyclerAdapter(bpackId, txTS, holder, status, context);
        mRecyclerView.setAdapter(mAdapter);
    }

    public  void logout(){
        class LogoutAsync extends AsyncTask<Void, Void, Void> {
            ProgressDialog loadingDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(ItemHistoryActivity.this, "Please wait", "Logging out...");
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (loadingDialog!=null && loadingDialog.isShowing())
                {
                    loadingDialog.dismiss();
                    Intent intent = new Intent(ItemHistoryActivity.this, LoginActivity.class);
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

    public void getDonorHistory(){
        final String URL_GET_DONOR_HISTORY="http://"+Config.ServerIP+":"+Config.Port+"/donor_history/"+Config.DonorID;
        class GetJSONProduct extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(URL_GET_DONOR_HISTORY);
                return s;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                JSON_STRING = s;

                if (s != null && s.length() > 0  && !s.contains("No data found")) {
                    getDonorHistoryResult();
                    setDonorHistory();
                } else {
                    showDonorError();
                }
            }
        }
        GetJSONProduct gjp = new GetJSONProduct();
        gjp.execute();
    }


    private void setDonorHistory(){
        mAdapter2 = new DonorHistoryRecyclerAdapter(txTS, name, address, phone, context);
        mRecyclerView.setAdapter(mAdapter2);
    }


    private void getDonorHistoryResult(){

        JSONObject jsonObject = null;

        try {

            JSONArray result = new JSONArray(JSON_STRING);


            donorId = new String[result.length()];
            name    = new String[result.length()];
            address  = new String[result.length()];
            phone    = new String[result.length()];
            txTS    = new String[result.length()];


            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                try {

                    String transactionTS=jo.getString("TxTS");

                    String Record=jo.getString("Record");

                    try{

                        JSONObject joRecord=new JSONObject(Record);

                       donorId[i]=Config.DonorID;
                       name[i]=joRecord.getString("name");
                       address[i]=joRecord.getString("address");
                       phone[i]=joRecord.getString("phone");
                       txTS[i]=transactionTS;

                    }catch (Exception e){

                        donorId[i]="-";
                        name[i]="-";
                        address[i]="-";
                        phone[i]="-";
                        txTS[i]="-";
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


    private void showDonorError(){
       Snackbar.with(getApplicationContext()).text("Cannot load data for this donor(s).").show(this);
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ItemHistoryActivity.this, OptionsActivity.class);
        finish();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.back_slide_out, R.anim.back_slide_in);
    }
}
