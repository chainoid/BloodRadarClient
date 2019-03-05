package com.chainoid.bloodradar.brapp;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.nispok.snackbar.Snackbar;

public class LoginActivity extends AppCompatActivity {

    private Button btnLogin;
    private EditText inputUsername,inputPassword;
    private ImageView btnSettings;
    public SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin=(Button)findViewById(R.id.btn_login);
        inputUsername=(EditText)findViewById(R.id.input_username);
        inputPassword=(EditText)findViewById(R.id.input_password);
        btnSettings=(ImageView)findViewById(R.id.ip_settings);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                checkData();
            }
        });

        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                showCustomDialog();
            }
        });
    }

    private void checkData(){
        if(inputPassword.getText().toString().equals("")&& inputUsername.getText().toString().equals("")){
            Snackbar.with(getApplicationContext()).text("Username | Password can't be empty").show(this);
        }else if(inputUsername.getText().toString().toLowerCase().equals("admin")){
            Config.mUserType="Admin";
            gotoOptionsActivity();
        }else if(inputUsername.getText().toString().toLowerCase().equals("hospital")){
            Config.mUserType="Hospital";
            gotoOptionsActivity();
        }else if(inputUsername.getText().toString().toLowerCase().equals("camp")){
            Config.mUserType="Camp";
            gotoOptionsActivity();
        } else if(inputUsername.getText().toString().toLowerCase().equals("bank")){
            Config.mUserType="Bank";
            gotoOptionsActivity();
        } else if(inputUsername.getText().toString().toLowerCase().equals("donor")){
            Config.mUserType="Donor";
            gotoOptionsActivity();
        }else{
            Snackbar.with(getApplicationContext()).text("Invalid User").show(this);
        }
    }

    private void gotoOptionsActivity(){
        Intent intent=new Intent(LoginActivity.this,OptionsActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_out,R.anim.slide_in);
        finish();
    }

    private void showCustomDialog(){
        // Create custom dialog object
        final Dialog dialog = new Dialog(LoginActivity.this);
        // remove dialog title
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Include custom_dialog.xml file
        dialog.setContentView(R.layout.custom_ip_popup);

        Button btnOk=(Button)dialog.findViewById(R.id.btnOk);
        final EditText txtIP=(EditText)dialog.findViewById(R.id.txtIP);
        final EditText txtPort=(EditText)dialog.findViewById(R.id.txtPort);

        txtIP.setText((prefs.getString("Ip", "146.0.81.109")));
        txtPort.setText((prefs.getString("Port", "9000")));
        dialog.show();
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Config.ServerIP=txtIP.getText().toString().trim();
                Config.Port=txtPort.getText().toString().trim();
                prefs.edit().putString("Ip", Config.ServerIP).commit();
                prefs.edit().putString("Port", Config.Port).commit();
                dialog.dismiss();
            }

        });
    }
}
