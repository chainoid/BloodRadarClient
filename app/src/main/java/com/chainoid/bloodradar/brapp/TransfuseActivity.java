package com.chainoid.bloodradar.brapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.nispok.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

public class TransfuseActivity extends AppCompatActivity {

    private TextView toolbarTitle;
    private TextView txtFeedback;

    private EditText txtTS;
    private EditText txtStatus;
    private EditText txtBpackId;
    private EditText txtBtype;
    private EditText txtAmount;

    private Button btnUpdate;
    private String JSON_STRING;

    private String sourceTS;
    private String status;
    private String bpackId;
    private String btype;
    private String amount;

    private Context context=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfuse);

        txtFeedback=(TextView)findViewById(R.id.txtFeedback);
        toolbarTitle=(TextView)findViewById(R.id.toolbar_title);
        txtTS =(EditText)findViewById(R.id.txtTS);
        txtStatus =(EditText)findViewById(R.id.txtStatus);
        txtBpackId=(EditText)findViewById(R.id.txtBpackId);
        txtBtype=(EditText)findViewById(R.id.txtBtype);
        txtAmount=(EditText)findViewById(R.id.txtAmount);
        btnUpdate=(Button)findViewById(R.id.btnOk);

        toolbarTitle.setText(Config.mUserType);
        txtFeedback.setVisibility(View.GONE);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }

        });

        getItemDetails();
    }

    private void validateData(){
        if(txtBpackId.getText().toString().equals("")){
            Snackbar.with(getApplicationContext()).text("Bpack ID is mandatory").show(this);
        }else{
            doTransfuse();
        }
    }


    public void getItemDetails(){
        final String URL_GET_DONOR="http://"+Config.ServerIP+":"+Config.Port+"/get_bpack_by_id/"+Config.BpackID;
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
                    getItemResult();
                    setItem();
                } else {
                    showDonorError();
                }

            }
        }
        GetJSONProduct gjp = new GetJSONProduct();
        gjp.execute();
    }

    private void showDonorError(){
        Snackbar.with(getApplicationContext()).text("Cannot load data for this bpack.").show(this);
    }

    private void getItemResult(){
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(JSON_STRING);

            sourceTS = new String();
            status   = new String();
            bpackId  = new String();
            btype    = new String();
            btype    = new String();
            amount   = new String();

            bpackId =Config.BpackID;
            sourceTS =jsonObject.getString("donationTS");
            status =jsonObject.getString("status");
            btype=jsonObject.getString("btype");
            amount=jsonObject.getString("amount");

        } catch (JSONException e) {
            sourceTS = "-";
            status ="-";
            btype="-";
            amount="-";
        }
    }

    private void setItem(){

        txtTS.setText(sourceTS);
        txtBpackId.setText(bpackId);
        txtStatus.setText(status);
        txtBtype.setText(btype);
        txtAmount.setText(amount);
    }

    private void doTransfuse(){
        String uri=Config.BpackID;

        final String URL_UPDATE_DONOR="http://"+Config.ServerIP+":"+Config.Port+"/do_transfuse/"+uri;
        class GetJSON extends AsyncTask<Void,Void,String> {
            ProgressDialog progressDialog;
            @Override
            protected void onPreExecute() {
                progressDialog=new ProgressDialog(TransfuseActivity.this);
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
        if(JSON_STRING.contains("Error")){
            txtFeedback.setVisibility(View.VISIBLE);
            txtFeedback.setText("Failure :" + JSON_STRING);
         }else{
            txtFeedback.setVisibility(View.VISIBLE);
            txtFeedback.setText("Success! TxID : "+JSON_STRING);
        }
    }

}
