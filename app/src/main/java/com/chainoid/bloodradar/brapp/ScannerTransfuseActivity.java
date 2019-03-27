package com.chainoid.bloodradar.brapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScannerTransfuseActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private static final int ZXING_CAMERA_PERMISSION = 1;
    private Class<?> mClss;
    private ZXingScannerView mScannerView;
    private TextView toolbarTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        toolbarTitle=(TextView)findViewById(R.id.toolbar_title);
        toolbarTitle.setText(Config.mUserType);

        ViewGroup contentFrame = (ViewGroup) findViewById(R.id.content_frame);
        mScannerView = new ZXingScannerView(this);
        contentFrame.addView(mScannerView);
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();

    }

    @Override
    protected void onResume() {
        super.onResume();
        mScannerView.resumeCameraPreview(ScannerTransfuseActivity.this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //mScannerView.resumeCameraPreview(SimpleScannerActivity.this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
        //mScannerView.resumeCameraPreview(SimpleScannerActivity.this);
        mScannerView.setFlash(false);
        mScannerView.stopCamera();
    }
    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void handleResult(Result rawResult) {
        // Note:
        // * Wait 2 seconds to resume the preview.
        // * On older devices continuously stopping and resuming camera preview can result in freezing the app.

        Config.BpackID = rawResult.getText().toString().trim();

        Intent intent =  new Intent(ScannerTransfuseActivity.this, TransfuseActivity.class);;

        finish();
        startActivity(intent);
        overridePendingTransition(R.anim.slide_out,R.anim.slide_in);

    }

}
