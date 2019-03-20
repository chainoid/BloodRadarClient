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

    private String[] btype;
    private String[] donorId;
    private String[] donationTS;
    private String[] amount;
    private String[] location;
    private String[] status;


    private String[] txTS;

    private String[] name;
    private String[] address;

    private String[] holder;


    private TextView toolbarTitle;
    private ImageView imgLogout;
    private RecyclerView mRecyclerView;
    private ItemHistoryRecyclerAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
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

            //btype = new String[result.length()];

            //amount =new String[result.length()];
            //location =new String[result.length()];


            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                try {
                    //bpackId[i]=jo.getString("Key");

                    String Record=jo.getString("Record");

                    String transTS=jo.getString("TxTS");

                    try{

                        JSONObject joRecord=new JSONObject(Record);

                        //btype[i]=joRecord.getString("btype");
                        //donorId[i]=joRecord.getString("donorId");
                        //donationTS[i]=joRecord.getString("donationTS");
                        //amount[i]=joRecord.getString("amount");

                        bpackId[i]=Config.BpackID;
                        holder[i]=joRecord.getString("holder");
                        status[i]=joRecord.getString("status");

                        txTS[i]=transTS;


                    }catch (Exception e){
                        //btype[i]="-";
                        //donorId[i]="-";
                        //donationTS[i]="-";
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
        final String URL_GET_ITEM="http://"+Config.ServerIP+":"+Config.Port+"/donor_history/"+Config.DonorID;
        class GetJSONProduct extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(URL_GET_ITEM);
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
     //   mAdapter = new DonorHistoryRecyclerAdapter(name, address, context);
      //  mRecyclerView.setAdapter(mAdapter);
    }


    private void getDonorHistoryResult(){
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(JSON_STRING);
            name =new String[1];
            address =new String[1];
            //donorId =new String[1];
            //donationTS =new String[1];
            //amount =new String[1];
            //location =new String[1];
            //status =new String[1];

            //bpackId[0]=Config.BpackID;
            name[0]=jsonObject.getString("name");
            address[0]=jsonObject.getString("address");
         //   donationTS[0]=jsonObject.getString("donationTS");
         //   amount[0]=jsonObject.getString("amount");
         //   location[0]=jsonObject.getString("location");
         //   status[0]=jsonObject.getString("status");

        } catch (JSONException e) {
            name[0]="-";
            address[0]="-";
         //   donationTS[0]="-";
         //   amount[0]="-";
         //   location[0]="-";
         //   status[0]="-";
         }
    }



    private void showDonorError(){
       Snackbar.with(getApplicationContext()).text("Cannot load data for this donor(s).").show(this);
    }


    private void showDebug(String debugInfo ){
        Snackbar.with(getApplicationContext()).text(debugInfo).show(this);
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
