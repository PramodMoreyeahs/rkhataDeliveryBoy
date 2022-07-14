package in.moreyeahs.livspace.delivery.views.main;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import in.moreyeahs.livspace.delivery.R;
import in.moreyeahs.livspace.delivery.databinding.ActivityScanBarcodeBinding;
import in.moreyeahs.livspace.delivery.utilities.LocaleHelper;

import java.io.IOException;

public class ScannedBarcodeActivity extends AppCompatActivity {


    private SurfaceView surfaceView;
    private TextView txtBarcodeValue;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    private Button btnAction;
    private String intentData = "";
    private boolean isEmail = false;
    private ActivityScanBarcodeBinding mBinding;
    private MainActivity activity;
    private int orderid;
    private String number="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_scan_barcode);
        /*if (savedInstanceState != null) {
            Log.e("Stage", "1");

        } else {

            IntentIntegrator integrator = new IntentIntegrator(this);
            integrator.setOrientationLocked(false);
            integrator.initiateScan();
        }*/
        initViews();

    }

    private void initViews() {
        Intent intent = getIntent();
        orderid = intent.getIntExtra("orderID", 0);
        txtBarcodeValue = findViewById(R.id.txtBarcodeValue);
        surfaceView = findViewById(R.id.surfaceView);
        btnAction = findViewById(R.id.btnAction);
        mBinding.tvOrderno.setText(getString(R.string.OrderID) + String.valueOf(orderid));
        mBinding.btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBinding.orderid.getText().toString().trim().equalsIgnoreCase(String.valueOf(orderid))) {
                    Intent intent = new Intent();
                    String str = mBinding.orderid.getText().toString().trim();
                    intent.putExtra("result", str);
                    setResult(2, intent);
                    finish();
                } else {

                    Toast.makeText(ScannedBarcodeActivity.this, "Barcode Does not Match with Order ID", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    private void initialiseDetectorsAndSources() {

        Toast.makeText(getApplicationContext(), "Barcode scanner started", Toast.LENGTH_SHORT).show();

        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(ScannedBarcodeActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(ScannedBarcodeActivity.this, new
                                String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
               // Toast.makeText(getApplicationContext(), "To prevent memory leaks barcode scanner has been stopped", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {


                    txtBarcodeValue.post(new Runnable() {

                        @Override
                        public void run() {

                            if (barcodes.valueAt(0).email != null) {
                                txtBarcodeValue.removeCallbacks(null);
                                intentData = barcodes.valueAt(0).email.address;
                                number= intentData.replaceFirst("^0+(?!$)", "");
                                txtBarcodeValue.setText(number);
                                isEmail = true;
                                mBinding.orderid.setText(number);

                            } else {
                                isEmail = false;
                                intentData = barcodes.valueAt(0).displayValue;
                                number= intentData.replaceFirst("^0+(?!$)", "");
                                txtBarcodeValue.setText(number);
                                mBinding.orderid.setText(number);
                            }
                        }
                    });

                }
            }
        });

    }

    /*
        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (result != null) {
                if (result.getContents() == null) {
                    Log.d("QRCodeScanActivity", "Cancelled scan");
                    Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Log.d("QRCodeScanActivity", "Scanned");
                    String Scan = result.getContents();
                    if(Scan!=null){
                       mBinding.orderid.setText(Scan);
                    }


                }
            } else {
                Log.e("Stage", "3");
                super.onActivityResult(requestCode, resultCode, data);
            }
        }*/


    @Override
    protected void onPause() {
        super.onPause();
        cameraSource.release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initialiseDetectorsAndSources();


    }
    @Override
    protected void attachBaseContext(Context newBase)
    {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }
}
