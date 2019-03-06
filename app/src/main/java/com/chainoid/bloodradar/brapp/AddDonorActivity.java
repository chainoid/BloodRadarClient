package com.chainoid.bloodradar.brapp;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.nispok.snackbar.Snackbar;

public class AddDonorActivity extends AppCompatActivity {
    private TextView toolbarTitle,txtFeedback;
    private EditText txtName;
    private EditText txtAddress;
    private EditText txtPhone;
    private EditText txtSSN;
    private EditText txtAge;
    private EditText txtSex;
    private EditText txtBtype;



    private Button btnCreate;
    private String JSON_STRING;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_product);

        txtFeedback=(TextView)findViewById(R.id.txtFeedback);
        toolbarTitle=(TextView)findViewById(R.id.toolbar_title);
        txtName=(EditText)findViewById(R.id.txtName);
        txtAddress=(EditText)findViewById(R.id.txtAddress);
        txtPhone =(EditText)findViewById(R.id.txtPhone);
        txtSSN =(EditText)findViewById(R.id.txtSSN);
        txtAge =(EditText)findViewById(R.id.txtAge);
        txtSex =(EditText)findViewById(R.id.txtSex);
        txtBtype =(EditText)findViewById(R.id.txtBtype);

        validateData();
        btnCreate=(Button)findViewById(R.id.btnOk);

        toolbarTitle.setText(Config.mUserType);
        txtFeedback.setVisibility(View.GONE);

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               validateData();
            }

        });
    }

    private void validateData(){
        if(txtName.getText().toString().equals("")|| txtAddress.getText().toString().equals("")||
                txtPhone.getText().toString().equals("")|| txtSSN.getText().toString().equals("")||
                txtAge.getText().toString().equals("")|| txtSex.getText().toString().equals("")||
                txtBtype.getText().toString().equals("")){
            Snackbar.with(getApplicationContext()).text("All fields are mandatory").show(this);
        }else{
            createProduct();
        }
    }

    private void createProduct(){
        String uri=txtName.getText().toString().trim()+"-"+ txtAddress.getText().toString().trim()+"-"+
                   txtPhone.getText().toString().trim()+"-"+ txtSSN.getText().toString().trim()+"-"+
                   txtAge.getText().toString().trim()+"-"+ txtSex.getText().toString().trim()+"-"+txtBtype.getText().toString().trim();
        final String URL_CREATE_PRODUCT="http://"+Config.ServerIP+":"+Config.Port+"/add_donor/"+uri;
        class GetJSON extends AsyncTask<Void,Void,String> {
            ProgressDialog progressDialog;
            @Override
            protected void onPreExecute() {
                progressDialog=new ProgressDialog(AddDonorActivity.this);
                progressDialog.setMessage("Submitting Transaction");
                progressDialog.show();
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s;
                try{
                    s = rh.sendGetRequest(URL_CREATE_PRODUCT);
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
