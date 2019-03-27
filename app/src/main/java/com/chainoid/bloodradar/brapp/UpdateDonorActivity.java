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

    private String donorID;
    private String name;
    private String address;
    private String phone;
    private String age;

    private Context context=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_donor);

        txtFeedback=(TextView)findViewById(R.id.txtFeedback);
        toolbarTitle=(TextView)findViewById(R.id.toolbar_title);
        txtName=(EditText)findViewById(R.id.txtName);
        txtAddress=(EditText)findViewById(R.id.txtAddress);
        txtPhone=(EditText)findViewById(R.id.txtPhone);
        txtAge=(EditText)findViewById(R.id.txtAge);
        btnUpdate=(Button)findViewById(R.id.btnOk);

        toolbarTitle.setText(Config.mUserType);
        txtFeedback.setVisibility(View.GONE);

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

            donorID =new String();
            name =new String();
            phone =new String();
            address =new String();
            age =new String();

            donorID=Config.DonorID;
            name=jsonObject.getString("name");
            address=jsonObject.getString("address");
            phone=jsonObject.getString("phone");
            age=jsonObject.getString("age");

        } catch (JSONException e) {
            name="-";
            address="-";
            phone="-";
            age="-";
        }
    }

    private void setDonor(){

        txtName.setText(name);
        txtAddress.setText(address);
        txtPhone.setText(phone.replaceAll("-",""));
        txtAge.setText(age);
    }

    private void updateDonor(){
        String uri=Config.DonorID+"-"+
                txtName.getText().toString().trim()+"-"+
                txtAddress.getText().toString().trim()+"-"+
                txtPhone.getText().toString().trim()+"-"+
                txtAge.getText().toString().trim();

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
