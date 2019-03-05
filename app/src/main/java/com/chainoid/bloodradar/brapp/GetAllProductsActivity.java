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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GetAllProductsActivity extends AppCompatActivity {

    private String JSON_STRING;
    private String[] productID;
    private String[] owner;
    private String[] location;
    private String[] timestamp;
    private String[] uniqueID;
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
        if(Config.IfGetAllProducts){
            getAllProducts();
        }else{
            getSingleProduct();
        }

    }

    public void getAllProducts(){
        final String URL_GET_ALL_PRODUCTS="http://"+Config.ServerIP+":"+Config.Port+"/get_all_tuna";
        class GetJSON extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(URL_GET_ALL_PRODUCTS);
                return s;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                JSON_STRING = s;
                getAllProductsResult();
                setAllProducts();
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    private void getAllProductsResult(){
        JSONObject jsonObject = null;
        try {
            JSONArray result = new JSONArray(JSON_STRING);
            productID=new String[result.length()];
            uniqueID=new String[result.length()];
            owner=new String[result.length()];
            timestamp=new String[result.length()];
            location=new String[result.length()];

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                try {
                    productID[i]=jo.getString("Key");
                    String Record=jo.getString("Record");
                    try{
                        JSONObject joRecord=new JSONObject(Record);
                        owner[i]=joRecord.getString("holder");
                        location[i]=joRecord.getString("location");
                        timestamp[i]=joRecord.getString("timestamp");
                        uniqueID[i]=joRecord.getString("vessel");

                    }catch (Exception e){
                        owner[i]="-";
                        location[i]="-";
                        timestamp[i]="-";
                        uniqueID[i]="-";
                    }
                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setAllProducts(){
        mAdapter = new GetAllRecyclerAdapter(productID,owner,timestamp,location,uniqueID,context);
        mRecyclerView.setAdapter(mAdapter);
    }

    public  void logout(){
        class LogoutAsync extends AsyncTask<Void, Void, Void> {
            ProgressDialog loadingDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(GetAllProductsActivity.this, "Please wait", "Logging out...");
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (loadingDialog!=null && loadingDialog.isShowing())
                {
                    loadingDialog.dismiss();
                    Intent intent = new Intent(GetAllProductsActivity.this, LoginActivity.class);
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

    public void getSingleProduct(){
        final String URL_GET_PRODUCT="http://"+Config.ServerIP+":"+Config.Port+"/get_tuna/"+Config.ProductID;
        class GetJSONProduct extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(URL_GET_PRODUCT);
                return s;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                JSON_STRING = s;
                getProductResult();
                setAllProducts();
            }
        }
        GetJSONProduct gjp = new GetJSONProduct();
        gjp.execute();
    }

    private void getProductResult(){
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(JSON_STRING);
            productID=new String[1];
            uniqueID=new String[1];
            owner=new String[1];
            timestamp=new String[1];
            location=new String[1];

            productID[0]=Config.ProductID;
            owner[0]=jsonObject.getString("holder");
            location[0]=jsonObject.getString("location");
            timestamp[0]=jsonObject.getString("timestamp");
            uniqueID[0]=jsonObject.getString("vessel");


        } catch (JSONException e) {
            owner[0]="-";
            location[0]="-";
            timestamp[0]="-";
            uniqueID[0]="-";
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(GetAllProductsActivity.this, OptionsActivity.class);
        finish();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.back_slide_out, R.anim.back_slide_in);
    }
}
