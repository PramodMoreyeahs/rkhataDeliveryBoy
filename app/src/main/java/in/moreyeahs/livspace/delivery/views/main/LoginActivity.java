package in.moreyeahs.livspace.delivery.views.main;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import in.moreyeahs.livspace.delivery.BuildConfig;
import in.moreyeahs.livspace.delivery.R;
import in.moreyeahs.livspace.delivery.databinding.LoginActivityBinding;
import in.moreyeahs.livspace.delivery.model.ForgetpassresponseModel;
import in.moreyeahs.livspace.delivery.model.LoginModel;
import in.moreyeahs.livspace.delivery.model.LoginResponse;
import in.moreyeahs.livspace.delivery.model.TokenResponse;
import in.moreyeahs.livspace.delivery.model.UserAuth;
import in.moreyeahs.livspace.delivery.utilities.ApiResponse;
import in.moreyeahs.livspace.delivery.utilities.LocaleHelper;
import in.moreyeahs.livspace.delivery.utilities.MarshmallowPermissions;
import in.moreyeahs.livspace.delivery.utilities.SharePrefs;
import in.moreyeahs.livspace.delivery.utilities.TextUtils;
import in.moreyeahs.livspace.delivery.utilities.Utils;
import in.moreyeahs.livspace.delivery.utilities.Validation;
import in.moreyeahs.livspace.delivery.viewmodels.ForgetPasswordModel;
import in.moreyeahs.livspace.delivery.viewmodels.LoginViewModel;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    private LoginActivityBinding mBinding;
    private Utils utils;
    private LoginViewModel loginViewModel;
    private String fcmToken, email, password, android_id;
    private Dialog customDialog;
    private ForgetPasswordModel forgetPasswordModel;
    private String VersionName, deviceOs, Devicename, IMEI;
    private TelephonyManager telephonyManager;
    private final int REQUEST_READ_PHONE_STATE = 1;
    private final int REQUEST_FINE_LOCAtion = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.login_activity);

        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        forgetPasswordModel = ViewModelProviders.of(this).get(ForgetPasswordModel.class);
        mBinding.setLoginViewModel(loginViewModel);
        mBinding.setLifecycleOwner(this);

        initialization();
        permission();
        loginViewModel.loginResponse().observe(this, apiResponse -> consumeResponse(apiResponse));
        loginViewModel.tokenResponse().observe(this, apiResponse -> TokenconsumeResponse(apiResponse));
        forgetPasswordModel.getpassworddata().observe(this, apiResponse -> frogetpassresponse(apiResponse));
        mBinding.tvForgotpassword.setOnClickListener(v -> {
            ForgotPasswordPopup();


        });
        mBinding.btnLogin.setOnClickListener(v -> {
            if (isValid()) {
                if (!Utils.checkInternetConnection(LoginActivity.this)) {
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                } else {
                    LoginModel loginModel = new LoginModel(mBinding.etMobile.getText().toString(), mBinding.etPassword.getText().toString(), android_id, fcmToken, VersionName, deviceOs, Devicename, IMEI);
                    loginViewModel.hitLoginApi(loginModel);
                }
            }
        });
    }

    private void TokenconsumeResponse(ApiResponse apiResponse) {
        switch (apiResponse.status) {
            case LOADING:
                Utils.showProgressDialog(LoginActivity.this);
                break;
            case SUCCESS:
                Utils.hideProgressDialog(LoginActivity.this);
                TokenrenderSuccessResponse(apiResponse.data);
                break;
            case ERROR:
                Utils.hideProgressDialog(LoginActivity.this);
                Utils.setToast(LoginActivity.this, getResources().getString(R.string.errorString));
                break;
            default:
                break;
        }
    }

    private void TokenrenderSuccessResponse(JsonElement response) {
        if (Utils.isJSONValid(response.toString())) {
            if (!response.isJsonNull()) {
                Log.d("response=", response.toString());
                try {
                    JSONObject obj = new JSONObject(response.toString());
                    TokenResponse tokenResponse = new Gson().fromJson(obj.toString(), TokenResponse.class);

                    if (tokenResponse != null) {
                        SharePrefs.getInstance(LoginActivity.this).putString(SharePrefs.TOKEN, tokenResponse.getAccess_token());
                        LoginActivity.this.finish();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        Utils.leftTransaction(this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(LoginActivity.this, getResources().getString(R.string.errorString), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(LoginActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    /*
     * method to handle response
     * */
    private void consumeResponse(ApiResponse apiResponse) {
        switch (apiResponse.status) {
            case LOADING:
                Utils.showProgressDialog(LoginActivity.this);
                break;
            case SUCCESS:
                Utils.hideProgressDialog(LoginActivity.this);
                renderSuccessResponse(apiResponse.data);
                break;
            case ERROR:
                Utils.hideProgressDialog(LoginActivity.this);
                Utils.setToast(LoginActivity.this, getResources().getString(R.string.errorString));
                break;
            default:
                break;
        }
    }

    private void frogetpassresponse(ApiResponse apiResponse) {

        switch (apiResponse.status) {

            case LOADING:
                Utils.showProgressDialog(LoginActivity.this);
                break;

            case SUCCESS:
                Utils.hideProgressDialog(LoginActivity.this);
                passwordsuccessresponse(apiResponse.data);
                break;

            case ERROR:
                Utils.hideProgressDialog(LoginActivity.this);
                Utils.setToast(LoginActivity.this, getResources().getString(R.string.errorString));
                break;

            default:
                break;
        }

    }

    /*
     * validation
     * */
    private boolean isValid() {

        email = mBinding.etMobile.getText().toString().trim();
        password = mBinding.etPassword.getText().toString().trim();

        if (TextUtils.isNullOrEmpty(email)) {
            Toast.makeText(LoginActivity.this, getResources().getString(R.string.enter_mobile_number), Toast.LENGTH_SHORT).show();
            return false;
        } else if (Validation.chkmobileNo(email)) {
            Toast.makeText(LoginActivity.this, getResources().getString(R.string.enter_valid_mobile), Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isNullOrEmpty(password.trim())) {
            Toast.makeText(LoginActivity.this, getResources().getString(R.string.enter_password), Toast.LENGTH_SHORT).show();
            return false;
        } else if (password.trim().length() < 6) {
            Toast.makeText(LoginActivity.this, getResources().getString(R.string.enter_valid_password), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /*
     * variable initialization
     * */
    public void initialization() {
        VersionName = BuildConfig.VERSION_NAME;
        deviceOs = android.os.Build.VERSION.RELEASE;
        System.out.println("version" + deviceOs);
        Devicename = android.os.Build.MODEL;
        customDialog = new Dialog(this, R.style.CustomDialog);
        fcmToken = FirebaseInstanceId.getInstance().getToken();
        android_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        utils = new Utils(this);
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

    }

    private void permission() {
        if (Build.VERSION.SDK_INT >= 23) {
            // Marshmallow+
            if (!(MarshmallowPermissions.checkPhoneReadState(this) && MarshmallowPermissions.checkPermissionLocation(this))) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_READ_PHONE_STATE);
            } else {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    IMEI = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                } else {
                    IMEI = telephonyManager.getDeviceId();
                }
                System.out.println("IMEI11111" + IMEI);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        if (requestCode == REQUEST_READ_PHONE_STATE) {
            if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    IMEI = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                } else {
                    IMEI = telephonyManager.getDeviceId();
                }
                System.out.println("IMEI11111" + IMEI);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void onBackPressed() {
        // super.onBackPressed();
        // startActivity(new Intent(LoginActivity.this, WelcomeActivity.class));
        exitPopup();
    }

    private void exitPopup() {
        if (LoginActivity.this != null) {
            final View mView = getLayoutInflater().inflate(R.layout.delivery_charge_popup, null);
            customDialog = new Dialog(this, R.style.CustomDialog);
            customDialog.setContentView(mView);
            TextView okBtn = mView.findViewById(R.id.ok_btn);
            TextView cancelBtn = mView.findViewById(R.id.cancel_btn);
            TextView description = mView.findViewById(R.id.pd_description);
            TextView title = mView.findViewById(R.id.pd_title);
            title.setText(("Closing Application"));
            description.setText("Do you want Exit APP?");
            okBtn.setOnClickListener(v -> {
                customDialog.dismiss();
                finish();
                finishAffinity();
            });
            cancelBtn.setOnClickListener(v -> customDialog.dismiss());
            customDialog.show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Utils.hideProgressDialog(LoginActivity.this);
    }

    /*
     * method to handle success response
     * */
    private void renderSuccessResponse(JsonElement response) {
        if (Utils.isJSONValid(response.toString())) {
            if (!response.isJsonNull()) {
                Log.d("response=", response.toString());
                try {
                    JSONObject obj = new JSONObject(response.toString());
                    LoginResponse loginResponse = new Gson().fromJson(obj.toString(), LoginResponse.class);
                    if (loginResponse.getStatus()) {
                        SharePrefs.getInstance(LoginActivity.this).putBoolean(SharePrefs.LOGGED, true);
                        SharePrefs.getInstance(LoginActivity.this).putInt(SharePrefs.PEOPLE_ID, loginResponse.getP().getPeopleID());
                        SharePrefs.getInstance(LoginActivity.this).putString(SharePrefs.SK_CODE, loginResponse.getP().getSkcode());
                        SharePrefs.getInstance(LoginActivity.this).putInt(SharePrefs.WAREHOUSE_ID, loginResponse.getP().getWarehouseId());
                        SharePrefs.getInstance(LoginActivity.this).putInt(SharePrefs.COMPANY_ID, loginResponse.getP().getCompanyId());
                        SharePrefs.getInstance(LoginActivity.this).putString(SharePrefs.PEAOPLE_FIRST_NAME, loginResponse.getP().getPeopleFirstName());
                        SharePrefs.getInstance(LoginActivity.this).putString(SharePrefs.PEAOPLE_LAST_NAME, loginResponse.getP().getPeopleLastName());
                        SharePrefs.getInstance(LoginActivity.this).putString(SharePrefs.PEAOPLE_EMAIL, loginResponse.getP().getEmail());
                        SharePrefs.getInstance(LoginActivity.this).putString(SharePrefs.PEAOPLE_IMAGE, loginResponse.getP().getImageUrl());
                        SharePrefs.getInstance(LoginActivity.this).putString(SharePrefs.MOBILE, loginResponse.getP().getMobile());
                        SharePrefs.getInstance(LoginActivity.this).putString(SharePrefs.EMAILID, loginResponse.getP().getEmail());
                        SharePrefs.getInstance(LoginActivity.this).putString(SharePrefs.VEHICLE_NAME, loginResponse.getP().getVehicleName());
                        SharePrefs.getInstance(LoginActivity.this).putString(SharePrefs.VEHICLE_NUMBER, loginResponse.getP().getVehicleNumber());
                        SharePrefs.getInstance(LoginActivity.this).putInt(SharePrefs.ASSIGNMENT_ID, 0);
                        Utils.setToast(LoginActivity.this, "Welcome " + loginResponse.getP().getPeopleFirstName().trim());
                        UserAuth regApk = loginResponse.getP().getRegisteredApk();
                        if (regApk != null) {
                            String Password = regApk.getPassword();
                            String username = regApk.getUserName();
                            SharePrefs.getInstance(LoginActivity.this).putString(SharePrefs.TOKEN_NAME, username);
                            SharePrefs.getInstance(LoginActivity.this).putString(SharePrefs.TOKEN_PASSWORD, Password);
                            if (!Utils.checkInternetConnection(LoginActivity.this)) {
                                Toast.makeText(LoginActivity.this, getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                            } else {
                                loginViewModel.hitTokenAPI("password", username, Password);
                            }
                        }
                    } else {
                        Utils.setToast(LoginActivity.this, loginResponse.getMessage());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(LoginActivity.this, getResources().getString(R.string.errorString), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(LoginActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void passwordsuccessresponse(JsonElement response) {
        if (Utils.isJSONValid(response.toString())) {
            if (!response.isJsonNull()) {
                Log.d("response=", response.toString());
                try {
                    JSONObject obj = new JSONObject(response.toString());
                    ForgetpassresponseModel forgetpassresponseModel = new Gson().fromJson(obj.toString(), ForgetpassresponseModel.class);
                    if (forgetpassresponseModel.getStatus()) {
                        Utils.setToast(LoginActivity.this, forgetpassresponseModel.getMessage());
                        if (customDialog != null) {
                            customDialog.dismiss();
                        }
                        //  forgotPasswordResult();
                    } else {
                        Utils.setToast(LoginActivity.this, forgetpassresponseModel.getMessage());
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(LoginActivity.this, getResources().getString(R.string.errorString), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(LoginActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void ForgotPasswordPopup() {
        LayoutInflater inflater = getLayoutInflater();
        final View mView = inflater.inflate(R.layout.forgot_password_popup, null);
        customDialog = new Dialog(LoginActivity.this, R.style.CustomDialog);
        customDialog.setContentView(mView);
        LinearLayout okBtn = mView.findViewById(R.id.ok_btn);
        LinearLayout cancelBtn = mView.findViewById(R.id.cancel_btn);
        EditText et_Mobile_No = mView.findViewById(R.id.et_Mobile_No);
        okBtn.setOnClickListener(v -> {
            if (et_Mobile_No.getText().toString().trim().equalsIgnoreCase("")) {
                Toast.makeText(LoginActivity.this, getString(R.string.enter_mobile_number), Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isValidMobileNo(et_Mobile_No.getText().toString().trim())) {
                if (!Utils.checkInternetConnection(LoginActivity.this)) {
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                } else {
                    forgetPasswordModel.ForgetpasswordAPI(et_Mobile_No.getText().toString().trim());
                }
            } else {
                // et_Mobile_No.setError("Enter Valid Mobile Number");
                Toast.makeText(LoginActivity.this, getString(R.string.validMobilenumbe), Toast.LENGTH_SHORT).show();
            }
        });
        cancelBtn.setOnClickListener(v -> customDialog.dismiss());
        customDialog.show();
    }

    // method result ppopup
    private void forgotPasswordResult() {
        final View mView = getLayoutInflater().inflate(R.layout.forgot_password_result, null);
        customDialog = new Dialog(LoginActivity.this, R.style.CustomDialog);
        customDialog.setContentView(mView);
        TextView okBtn = mView.findViewById(R.id.ok_btn);

        okBtn.setOnClickListener(v -> customDialog.dismiss());
        customDialog.show();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }
}