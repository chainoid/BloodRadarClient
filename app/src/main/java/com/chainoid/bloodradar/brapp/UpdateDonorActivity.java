package com.chainoid.bloodradar.brapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import android.support.v7.widget.RecyclerView;
import com.nispok.snackbar.Snackbar;
import org.json.JSONException;
import org.json.JSONObject;

public class UpdateDonorActivity extends AppCompatActivity {

    private TextView toolbarTitle;

    private TextView txtFeedback;
    private EditText txtName;
    private EditText txtAddress;
    private EditText txtPhone;
    private EditText txtAge;

    private Button btnUpdate;
    private String JSON_STRING;

    private String[] donorID;
    private String[] name;
    private String[] address;
    private String[] phone;
    private String[] age;

    private UpdateDonorRecyclerAdapter mAdapter;
    private RecyclerView mRecyclerView;

    private Context context=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_donor);

        toolbarTitle=(TextView)findViewById(R.id.toolbar_title);
        txtName=(EditText)findViewById(R.id.txtName);
        txtAddress=(EditText)findViewById(R.id.txtAddress);
        txtPhone=(EditText)findViewById(R.id.txtPhone);
        txtAge=(EditText)findViewById(R.id.txtAge);


        btnUpdate=(Button)findViewById(R.id.btnOk);

        toolbarTitle.setText(Config.mUserType);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }

        });

        getSingleDonor();
    }

    private void validateData(){
        if(txtAddress.getText().toString().equals("") || txtName.getText().toString().equals("")){
            Snackbar.with(getApplicationContext()).text("All fields are mandatory").show(this);
        }else{
            updateDonor();
        }
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

                if (s != null && s.length() > 0 && !s.contains("No data found")) {
                    getDonorResult();
                    setDonor();
                } else {
                    showDonorError();
                }

            }
        }
        GetJSONProduct gjp = new GetJSONProduct();
        gjp.execute();
    }

    private void showDonorError(){
        Snackbar.with(getApplicationContext()).text("Cannot load data for this donor(s).").show(this);
    }

    private void getDonorResult(){
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(JSON_STRING);

            donorID =new String[1];
            name =new String[1];
            phone =new String[1];
            address =new String[1];
            age =new String[1];

            donorID[0]=Config.DonorID;
            name[0]=jsonObject.getString("name");
            address[0]=jsonObject.getString("address");
            phone[0]=jsonObject.getString("phone");
            age[0]=jsonObject.getString("age");

        } catch (JSONException e) {
            name[0]="-";
            address[0]="-";
            phone[0]="-";
            age[0]="-";
        }
    }

    private void setDonor(){

        txtName.setText(name[0]);
        txtAddress.setText(address[0]);
        txtPhone.setText(phone[0]);
        txtAge.setText(age[0]);
    }

    private void updateDonor(){
        String uri=txtName.getText().toString().trim()+"-"+txtAddress.getText().toString().trim();
        final String URL_UPDATE_DONOR="http://"+Config.ServerIP+":"+Config.Port+"/update_donor/"+uri;
        class GetJSON extends AsyncTask<Void,Void,String> {
            ProgressDialog progressDialog;
            @Override
            protected void onPreExecute() {
                progressDialog=new ProgressDialog(UpdateDonorActivity.this);
                progressDialog.setMessage("Submitting Transaction");
                progressDialog.show();
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s;
                try{
                    s = rh.sendGetRequest(URL_UPDATE_DONOR);
                }catch (Exception e){
                    s="";
                    progressDialog.dismiss();
                }

                return s;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                JSON_STRING = s;
                progressDialog.dismiss();
                setResult();
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    private void setResult(){
        if(JSON_STRING.equals("")){
            txtFeedback.setVisibility(View.VISIBLE);
            txtFeedback.setText("Failure");
        }else{
            txtFeedback.setVisibility(View.VISIBLE);
            txtFeedback.setText("Success! TxID : "+JSON_STRING);
        }
    }

}
