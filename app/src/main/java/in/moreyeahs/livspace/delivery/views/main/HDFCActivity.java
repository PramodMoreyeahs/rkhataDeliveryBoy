package in.moreyeahs.livspace.delivery.views.main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import in.moreyeahs.livspace.delivery.R;
import in.moreyeahs.livspace.delivery.databinding.ActivityHdfcBinding;
import in.moreyeahs.livspace.delivery.utilities.AvenuesParams;
import in.moreyeahs.livspace.delivery.utilities.RSAUtility;
import in.moreyeahs.livspace.delivery.utilities.ServiceUtility;
import in.moreyeahs.livspace.delivery.utilities.SharePrefs;
import in.moreyeahs.livspace.delivery.utilities.Utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class HDFCActivity extends AppCompatActivity {

    private ActivityHdfcBinding mBinding;
    private Intent mainIntent;
    private String encVal;
    private String vResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_hdfc);
        mainIntent = getIntent();
        if (getIntent().getExtras() != null) {
            vResponse = getIntent().getStringExtra(AvenuesParams.RSA_KEY);
            // get rsa key method
            new RenderView().execute();
        }
    }

    @Override
    public void onBackPressed() {
        show_alert("transaction will cancel");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            Intent intent = new Intent();
            intent.putExtra(AvenuesParams.ORDER_ID, data.getStringExtra(AvenuesParams.ORDER_ID));
            intent.putExtra(AvenuesParams.TRANSACTION_ID, data.getStringExtra(AvenuesParams.TRANSACTION_ID));
            intent.putExtra(AvenuesParams.AMOUNT, data.getStringExtra(AvenuesParams.AMOUNT));
            intent.putExtra(AvenuesParams.STATUS, data.getStringExtra(AvenuesParams.STATUS));
            intent.putExtra(AvenuesParams.RESPONSE, data.getStringExtra(AvenuesParams.RESPONSE));
            setResult(Activity.RESULT_OK, intent);
        }
        finish();
    }

    public void show_alert(String msg) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Error!!!");
        if (msg.contains("\n"))
            msg = msg.replaceAll("\\\n", "");

        alertDialog.setMessage(msg);
        alertDialog.setButton(Dialog.BUTTON_POSITIVE, "OK", (dialog, which) -> {
            Intent intent = new Intent();
            setResult(Activity.RESULT_CANCELED, intent);
            finish();
        });
        alertDialog.setButton(Dialog.BUTTON_NEGATIVE, "Cancel", (dialog, which) -> dialog.cancel());
        alertDialog.show();
    }

    @SuppressLint("StaticFieldLeak")
    private class RenderView extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Utils.showProgressDialog(HDFCActivity.this);
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            if (!ServiceUtility.chkNull(vResponse).equals("") && !ServiceUtility.chkNull(vResponse).toString().contains("ERROR")) {
                StringBuilder vEncVal = new StringBuilder();
                vEncVal.append(ServiceUtility.addToPostParams(AvenuesParams.AMOUNT, mainIntent.getStringExtra(AvenuesParams.AMOUNT)));
                vEncVal.append(ServiceUtility.addToPostParams(AvenuesParams.CURRENCY, mainIntent.getStringExtra(AvenuesParams.CURRENCY)));
                encVal = RSAUtility.encrypt(vEncVal.substring(0, vEncVal.length() - 1), vResponse);  // encrypt amount and currency
                System.out.println("encVal"+encVal);
            }

            return null;
        }

        @SuppressLint("SetJavaScriptEnabled")
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            final WebView webview = findViewById(R.id.webview);
            webview.getSettings().setJavaScriptEnabled(true);
            webview.getSettings().setDomStorageEnabled(true);
            webview.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(webview, url);
                    System.out.println(url);
                    if (url.indexOf("/CcAvenueResponse") != -1) {
//                        webview.loadUrl("javascript:window.HTMLOUT.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
                        webview.evaluateJavascript("document.getElementById('decResp').value", value -> {
                            System.out.println("value " + value);
                            value = value.replaceAll("\\\\", "");
                            value = value.substring(1, value.length() - 1);
//                            System.out.println("value " + value);

                            Intent intent = new Intent();
                            intent.putExtra(AvenuesParams.RESPONSE, value);
                            setResult(Activity.RESULT_OK, intent);
                            finish();
                        });
                    }
                }

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                }
            });
            try {
                String postData = AvenuesParams.ACCESS_CODE + "=" + URLEncoder.encode(mainIntent.getStringExtra(AvenuesParams.ACCESS_CODE),
                        "UTF-8") + "&" + AvenuesParams.MERCHANT_ID + "=" + URLEncoder.encode(mainIntent.getStringExtra(AvenuesParams.MERCHANT_ID),
                        "UTF-8") + "&" + AvenuesParams.ORDER_ID + "=" + URLEncoder.encode(mainIntent.getStringExtra(AvenuesParams.ORDER_ID),
                        "UTF-8") + "&" + AvenuesParams.REDIRECT_URL + "=" + URLEncoder.encode(mainIntent.getStringExtra(AvenuesParams.REDIRECT_URL),
                        "UTF-8") + "&" + AvenuesParams.CANCEL_URL + "=" + URLEncoder.encode(mainIntent.getStringExtra(AvenuesParams.CANCEL_URL),
                        "UTF-8") + "&" + AvenuesParams.DELIVERY_NAME + "=" + URLEncoder.encode(mainIntent.getStringExtra(AvenuesParams.DELIVERY_NAME ),
                        "UTF-8") + "&" + AvenuesParams.DELIVERY_ADDRESS + "=" + URLEncoder.encode(mainIntent.getStringExtra(AvenuesParams.DELIVERY_ADDRESS),
                        "UTF-8") + "&" + AvenuesParams.DELIVERY_TEL + "=" + URLEncoder.encode(mainIntent.getStringExtra(AvenuesParams.DELIVERY_TEL),
                        "UTF-8") + "&" + AvenuesParams.DELIVERY_STATE + "=" + URLEncoder.encode("Madhya Pradesh",
                        "UTF-8") + "&" + AvenuesParams.DELIVERY_ZIP + "=" + URLEncoder.encode("452001",
                        "UTF-8") + "&" + AvenuesParams.DELIVERY_COUNTRY + "=" + URLEncoder.encode("India",
                        "UTF-8") + "&" + AvenuesParams.ENC_VAL + "=" + URLEncoder.encode(encVal, "UTF-8");
                webview.postUrl(SharePrefs.getInstance(HDFCActivity.this).getString(SharePrefs.GATWAY_URL), postData.getBytes());

                Utils.hideProgressDialog(HDFCActivity.this);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }
}
