package com.chainoid.bloodradar.brapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class GetAllDonorsActivity extends AppCompatActivity {

    private String JSON_STRING;
    private String[] donorID;
    private String[] name;
    private String[] address;
    private String[] phone;
    private String[] ssn;
    private String[] age;
    private String[] sex;
    private String[] btype;
    private TextView toolbarTitle;
    private ImageView imgLogout;
    private RecyclerView mRecyclerView;
    private GetAllRecyclerAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Context context=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_all_products);
        toolbarTitle=(TextView)findViewById(R.id.toolbar_title);
        imgLogout=(ImageView)findViewById(R.id.imgLogout);
        mRecyclerView = (RecyclerView) findViewById(R.id.get_products_recycler_view);

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
            getAllDonors();
        }else{
            getSingleDonor();
        }

    }

    public void getAllDonors(){


        final String URL_GET_DONORS_BY_BTYPE="http://"+Config.ServerIP+":"+Config.Port+"/get_donors_by_btype/"+Config.Btype;
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
                    getAllDonorsResult();
                    setAllDonors();
                } else {
                    showDonorError();
                }
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    private void getAllDonorsResult(){
        JSONObject jsonObject = null;
        try {
            JSONArray result = new JSONArray(JSON_STRING);
            donorID =new String[result.length()];
            ssn =new String[result.length()];
            name =new String[result.length()];
            phone =new String[result.length()];
            address =new String[result.length()];
            age = new String[result.length()];
            sex = new String[result.length()];
            btype = new String[result.length()];


            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                try {
                    donorID[i]=jo.getString("Key");

                    String Record=jo.getString("Record");
                    try{
                        JSONObject joRecord=new JSONObject(Record);
                        name[i]=joRecord.getString("name");
                        address[i]=joRecord.getString("address");
                        phone[i]=joRecord.getString("phone");
                        ssn[i]=joRecord.getString("ssn");
                        age[i]=joRecord.getString("age");
                        sex[i]=joRecord.getString("sex");
                        btype[i]=joRecord.getString("btype");

                    }catch (Exception e){
                        name[i]="-";
                        address[i]="-";
                        phone[i]="-";
                        ssn[i]="-";
                        age[i]="-";
                        sex[i]="-";
                        btype[i]="-";
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

    private void setAllDonors(){
        mAdapter = new GetAllRecyclerAdapter(donorID, name, phone, address, ssn, age, sex, context);
        mRecyclerView.setAdapter(mAdapter);
    }

    public  void logout(){
        class LogoutAsync extends AsyncTask<Void, Void, Void> {
            ProgressDialog loadingDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(GetAllDonorsActivity.this, "Please wait", "Logging out...");
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (loadingDialog!=null && loadingDialog.isShowing())
                {
                    loadingDialog.dismiss();
                    Intent intent = new Intent(GetAllDonorsActivity.this, LoginActivity.class);
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

    public void getSingleDonor(){
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
                    getDonorResult();
                    setAllDonors();
                } else {
                    showDonorError();
                }

            }
        }
        GetJSONProduct gjp = new GetJSONProduct();
        gjp.execute();
    }

    private void getDonorResult(){
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(JSON_STRING);
            donorID =new String[1];
            ssn =new String[1];
            name =new String[1];
            phone =new String[1];
            address =new String[1];
            age =new String[1];
            sex =new String[1];
            btype =new String[1];

            donorID[0]=Config.DonorID;
            name[0]=jsonObject.getString("name");
            address[0]=jsonObject.getString("address");
            phone[0]=jsonObject.getString("phone");
            ssn[0]=jsonObject.getString("ssn");
            age[0]=jsonObject.getString("age");
            sex[0]=jsonObject.getString("sex");
            btype[0]=jsonObject.getString("btype");


        } catch (JSONException e) {
            name[0]="-";
            address[0]="-";
            phone[0]="-";
            ssn[0]="-";
            age[0]="-";
            sex[0]="-";
            btype[0]="-";
        }
    }



    private void showDonorError(){
       Snackbar.with(getApplicationContext()).text("Cannot load data for this donor(s).").show(this);
    }



    @Override
    public void onBackPressed() {
        Intent intent = new Intent(GetAllDonorsActivity.this, OptionsActivity.class);
        finish();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.back_slide_out, R.anim.back_slide_in);
    }
}
