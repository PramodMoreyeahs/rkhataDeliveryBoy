package in.moreyeahs.livspace.delivery.views.main;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import in.moreyeahs.livspace.delivery.BuildConfig;
import in.moreyeahs.livspace.delivery.R;
import in.moreyeahs.livspace.delivery.databinding.ActivityWelcomeBinding;
import in.moreyeahs.livspace.delivery.model.LoginResponse;
import in.moreyeahs.livspace.delivery.model.OTPModel;
import in.moreyeahs.livspace.delivery.model.TokenResponse;
import in.moreyeahs.livspace.delivery.model.UserAuth;
import in.moreyeahs.livspace.delivery.utilities.ApiResponse;
import in.moreyeahs.livspace.delivery.utilities.ExtendedViewPager;
import in.moreyeahs.livspace.delivery.utilities.LocaleHelper;
import in.moreyeahs.livspace.delivery.utilities.MarshmallowPermissions;
import in.moreyeahs.livspace.delivery.utilities.RxBus;
import in.moreyeahs.livspace.delivery.utilities.SharePrefs;
import in.moreyeahs.livspace.delivery.utilities.TextUtils;
import in.moreyeahs.livspace.delivery.utilities.Utils;
import in.moreyeahs.livspace.delivery.viewmodels.OTPViewModel;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;

public class WelcomeActivity extends AppCompatActivity {
    private ActivityWelcomeBinding mBinding;
    private ViewPager viewPager;

    private Spinner cityspinner;
    private LinearLayout dotsLayout;

    private Button btnback, btnNext;
    private EditText phoneno, otp;
    private TextView tvResendOtpTimer;
    private TextView resendotp;
    private TextView tv_oldnumber;

    private int[] layouts;
    private int customerid;
    private String Mobileno = "Mobileno";
    private String comFromClass;
    private String selectcity_Id = "";
    private CountDownTimer cTimer = null;
    private String fcmToken, deviceID;
    private OTPViewModel otpViewModel;
    private String OTPNumberString;
    private Dialog customDialog;
    private Button login, enterbypotp;
    private final int REQUEST_READ_PHONE_STATE = 1;
    private String IMEI;
    private TelephonyManager telephonyManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Checking for first time launch - before calling setContentView()

        if (getIntent() != null) {
            comFromClass = getIntent().getStringExtra("ComeFrom");

        }
        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_welcome);
        otpViewModel = ViewModelProviders.of(this).get(OTPViewModel.class);
        mBinding.setLifecycleOwner(this);
        /**
         * init
         * **/
        initialization();
        permission();
        otpViewModel.OtpResponse().observe(this, apiResponse -> consumeResponse(apiResponse));
        otpViewModel.tokenResponse().observe(this, apiResponse -> TokenconsumeResponse(apiResponse));

        final ExtendedViewPager cvp = findViewById(R.id.view_pager_view);
        MyViewPagerAdapter myViewPagerAdapter = new MyViewPagerAdapter(WelcomeActivity.this, cvp, cityspinner, layouts);
        cvp.setAdapter(myViewPagerAdapter);
        cvp.setOffscreenPageLimit(5);
        addBottomDots(0);
        changeStatusBarColor();
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);
        btnNext.setVisibility(View.GONE);

        if (comFromClass != null) {
            if (comFromClass.equals("LoginActivity")) {
                viewPager.setCurrentItem(2);
            }
        }

        btnback.setOnClickListener(v -> {
            int current = getItem(-1);
            if (current < layouts.length) {
                // move to next screen
                viewPager.setCurrentItem(current);
            }
        });
        btnNext.setOnClickListener(v -> {
            try {
                // customerid = SharePrefs.getInstance(WelcomeActivity.this).getInt(SharePrefs.CUSTOMER_ID);
                int current = getItem(+1);
                if (current < layouts.length) {
                    viewPager.setCurrentItem(current);
                } else {
                    if (selectcity_Id == null) {
                        Toast.makeText(WelcomeActivity.this, "Please select city", Toast.LENGTH_SHORT).show();
                    } else if (customerid == 0) {
                        Toast.makeText(WelcomeActivity.this, "Please Register your Number", Toast.LENGTH_SHORT).show();
                    } else if (Utils.isNetworkAvailable()) {
                        /*if (commonClassForAPI != null)
                        {
                            commonClassForAPI.PostCityId(PostcityId, selectcity_Id, customerid);
                        }*/
                    } else {
                        Utils.setToast(WelcomeActivity.this, getString(R.string.internet_connection));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void initialization() {
        viewPager = mBinding.viewPagerView;
        btnback = mBinding.btnBack;
        btnNext = mBinding.btnNext;
        dotsLayout = mBinding.layoutDots;
        viewPager.beginFakeDrag();
        layouts = new int[]{
                R.layout.view_welcome_screen,
                R.layout.view_otp_genrate,
                R.layout.view_otp_verify,
        };
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
    }


    private void permission() {
            if (!MarshmallowPermissions.checkPermissionLocation(WelcomeActivity.this)) {
                ActivityCompat.requestPermissions(WelcomeActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
            } else {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    IMEI = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                } else {
                    IMEI = telephonyManager.getDeviceId();
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
            }
        }
    }

    private void addBottomDots(int currentPage) {
        try {
            TextView[] dots = new TextView[layouts.length];
            int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
            int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);
            dotsLayout.removeAllViews();
            for (int i = 0; i < dots.length; i++) {
                dots[i] = new TextView(this);
                dots[i].setText(Html.fromHtml("&#8226;"));
                dots[i].setTextSize(35);
                dots[i].setTextColor(colorsInactive[currentPage]);
                dotsLayout.addView(dots[i]);
            }
            if (dots.length > 0)
                dots[currentPage].setTextColor(colorsActive[currentPage]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);
            if (position == 0) {
                btnback.setVisibility(View.GONE);
            } else if (position == layouts.length - 1) {
                // last page. make button text to GOT IT
                btnNext.setVisibility(View.GONE);
                // btnNext.setText(getString(R.string.finish));
            } else if (position == 1 || position == 2 || position == 3) {
                // still pages are left
                btnNext.setVisibility(View.GONE);
                btnback.setVisibility(View.GONE);
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    @Override
    public void onResume() {
        super.onResume();

        //MyApplication.getInstance().trackScreenView("Welcome Activity");

    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;
        Context context;
        String phonenumber;
        ExtendedViewPager cvp;
        Spinner cityspinner;


        public MyViewPagerAdapter(Context context, ExtendedViewPager cvp, Spinner cityspinner, int[] layouts) {
            this.context = context;
            this.cvp = cvp;
            this.cityspinner = cityspinner;
            // this.layouts =layouts;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(layouts[position], container, false);
            String VersionName;
            String deviceOs;
            String Devicename;
            VersionName = BuildConfig.VERSION_NAME;
            deviceOs = android.os.Build.VERSION.RELEASE;

            Devicename = android.os.Build.MODEL;
            if (position == 1) {
                phoneno = view.findViewById(R.id.et_phoneno);
                ImageView submit_number = view.findViewById(R.id.submit_number);
                submit_number.setOnClickListener(v -> {
                    phonenumber = phoneno.getText().toString();
                    if (phonenumber.equals("")) {
                        Utils.setToast(WelcomeActivity.this, getString(R.string.entermobilenumber));
                    } else if (!TextUtils.isValidMobileNo(phonenumber)) {
                        Utils.setToast(WelcomeActivity.this, getString(R.string.validMobilenumbe));
                    } else if (Utils.isNetworkAvailable()) {

                        fcmToken = FirebaseInstanceId.getInstance().getToken();
                        deviceID = Settings.Secure.getString(WelcomeActivity.this.getContentResolver(), Settings.Secure.ANDROID_ID);

                        try {
                            String versionName = WelcomeActivity.this.getPackageManager().getPackageInfo(WelcomeActivity.this.getPackageName(), 0).versionName;
                            String androidOS = Build.VERSION.RELEASE;
                            String deviceName = Build.MODEL;
                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }

                        OTPModel otpModel = new OTPModel(phonenumber, deviceID, fcmToken, VersionName, deviceOs, Devicename, IMEI);
                        otpViewModel.hitOTPApi(otpModel);
                    } else {
                        Utils.setToast(WelcomeActivity.this, getString(R.string.internet_connection));
                    }
                });
            } else if (position == 0) {
                login = view.findViewById(R.id.login);
                enterbypotp = view.findViewById(R.id.enterbypotp);
                enterbypotp.setBackgroundColor(Color.parseColor("#FFFF4500"));
                cvp.setAllowedSwipeDirection(ExtendedViewPager.SwipeDirection.NONE);
                setChecked(enterbypotp);
                setUnChecked(login);
                enterbypotp.setOnClickListener(v -> {
                    setChecked(enterbypotp);
                    setUnChecked(login);
                    int current = getItem(+1);
                    if (current < layouts.length) {
                        // move to next screen
                        viewPager.setCurrentItem(current);
                    }
                });
                login.setOnClickListener(v -> {
                    setChecked(login);
                    setUnChecked(enterbypotp);
                    startActivity(new Intent(context, LoginActivity.class));
                    finish();
                });


            } else if (position == 2) {
                otp = view.findViewById(R.id.otp);
                tvResendOtpTimer = view.findViewById(R.id.resend_otp_timer);
                resendotp = view.findViewById(R.id.resendotp);
                tv_oldnumber = view.findViewById(R.id.tv_oldnumber);
                TextView changenumber = view.findViewById(R.id.btn_chngnumber);
                ImageView verify_otp = view.findViewById(R.id.verify_otp);


                RxBus.getInstance().getEvent().observeOn(AndroidSchedulers.mainThread()).subscribe(new DisposableObserver<Object>() {
                    @Override
                    public void onNext(Object o) {
                        if (o instanceof String) {
                            String str = (String) o;
                            if (str.equalsIgnoreCase(Mobileno)) {
                                tv_oldnumber.setText(phonenumber);
                                resendotp.setEnabled(false);
                                resendotp.setTextColor(context.getResources().getColor(R.color.grey));
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

                changenumber.setOnClickListener(v -> {
                    otp.setText("");
                    int current = getItem(-1);
                    if (current < layouts.length) {
                        // move to next screen
                        viewPager.setCurrentItem(current);
                    }
                    cancelTimer();
                });
                verify_otp.setOnClickListener(v -> {
                    if (otp.getText().toString().equalsIgnoreCase("")) {
                        Utils.setToast(WelcomeActivity.this, getString(R.string.enteotp));
                    } else if (OTPNumberString.equals(otp.getText().toString())) {
                        SharePrefs.getInstance(WelcomeActivity.this).putBoolean(SharePrefs.LOGGED, true);
                        otp.setText("");
                        Utils.setToast(WelcomeActivity.this, getString(R.string.verified));

                        startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                        finish();
                    } else {
                        otp.setText("");
                        //cvp.setAllowedSwipeDirection(ExtendedViewPager.SwipeDirection.NONE);
                        Toast.makeText(context, getString(R.string.enter_correct_otp), Toast.LENGTH_SHORT).show();
                    }
                });
                resendotp.setOnClickListener(v -> {
                    otp.setText("");
                    int current = getItem(-1);
                    if (current < layouts.length) {
                        // move to next screen
                        viewPager.setCurrentItem(current);
                    }
                    cancelTimer();
                });
            }
            container.addView(view);
            return view;
        }

        //cancel timer
        public void cancelTimer() {
            if (cTimer != null)
                cTimer.cancel();
        }


        @Override
        public int getCount() {
            return layouts.length;
        }


        @Override
        public boolean isViewFromObject(@NotNull View view, @NotNull Object obj) {
            return view == obj;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, @NotNull Object object) {
            View view = (View) object;
            container.removeView(view);
        }

        public void setChecked(Button button) {
            button.setBackground(getResources().getDrawable(R.drawable.orangerectangle));
            button.setTextColor(getResources().getColor(R.color.white));
        }

        public void setUnChecked(Button button) {
            button.setBackground(getResources().getDrawable(R.drawable.border_green));
            button.setTextColor(getResources().getColor(R.color.colorAccent));
        }
    }

    /*
     * method to handle response
     * */
    private void consumeResponse(ApiResponse apiResponse) {
        switch (apiResponse.status) {
            case LOADING:
                Utils.showProgressDialog(this);
                break;
            case SUCCESS:
                Utils.hideProgressDialog(this);
                renderSuccessResponse(apiResponse.data);
                break;
            case ERROR:
                Utils.hideProgressDialog(this);
                Utils.setToast(this, getResources().getString(R.string.errorString));
                break;
            default:
                break;
        }
    }

    private void TokenconsumeResponse(ApiResponse apiResponse) {
        switch (apiResponse.status) {
            case LOADING:
                Utils.showProgressDialog(this);
                break;
            case SUCCESS:
                Utils.hideProgressDialog(this);
                TokenrenderSuccessResponse(apiResponse.data);
                break;
            case ERROR:
                Utils.hideProgressDialog(this);
                Utils.setToast(this, getResources().getString(R.string.errorString));
                break;
            default:
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    public void onBackPressed() {
        // super.onBackPressed();
        customDialog = new Dialog(this, R.style.CustomDialog);
        final View mView = getLayoutInflater().inflate(R.layout.delivery_charge_popup, null);
        customDialog.setContentView(mView);
        TextView okBtn = mView.findViewById(R.id.ok_btn);
        TextView cancelBtn = mView.findViewById(R.id.cancel_btn);
        TextView description = mView.findViewById(R.id.pd_description);
        TextView title = mView.findViewById(R.id.pd_title);
        title.setText(("Closing Application"));
        description.setText("Do you want Exit APP?");
        okBtn.setOnClickListener(v -> {
            finish();
            finishAffinity();
        });

        cancelBtn.setOnClickListener(v -> {
            if (customDialog != null) {
                customDialog.dismiss();
            }
        });
        customDialog.show();
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
                        SharePrefs.getInstance(WelcomeActivity.this).putInt(SharePrefs.PEOPLE_ID, loginResponse.getP().getPeopleID());
                        SharePrefs.getInstance(WelcomeActivity.this).putString(SharePrefs.SK_CODE, loginResponse.getP().getSkcode());
                        SharePrefs.getInstance(WelcomeActivity.this).putInt(SharePrefs.WAREHOUSE_ID, loginResponse.getP().getWarehouseId());
                        SharePrefs.getInstance(WelcomeActivity.this).putInt(SharePrefs.COMPANY_ID, loginResponse.getP().getCompanyId());
                        SharePrefs.getInstance(WelcomeActivity.this).putString(SharePrefs.PEAOPLE_FIRST_NAME, loginResponse.getP().getPeopleFirstName());
                        SharePrefs.getInstance(WelcomeActivity.this).putString(SharePrefs.PEAOPLE_LAST_NAME, loginResponse.getP().getPeopleLastName());
                        SharePrefs.getInstance(WelcomeActivity.this).putString(SharePrefs.PEAOPLE_EMAIL, loginResponse.getP().getEmail());
                        SharePrefs.getInstance(WelcomeActivity.this).putString(SharePrefs.PEAOPLE_IMAGE, loginResponse.getP().getImageUrl());
                        SharePrefs.getInstance(WelcomeActivity.this).putString(SharePrefs.MOBILE, loginResponse.getP().getMobile());
                        SharePrefs.getInstance(WelcomeActivity.this).putString(SharePrefs.EMAILID, loginResponse.getP().getEmail());
                        SharePrefs.getInstance(WelcomeActivity.this).putString(SharePrefs.VEHICLE_NAME, loginResponse.getP().getVehicleName());
                        SharePrefs.getInstance(WelcomeActivity.this).putString(SharePrefs.VEHICLE_NUMBER, loginResponse.getP().getVehicleNumber());
                        //SharePrefs.getInstance(WelcomeActivity.this).putString(SharePrefs.OTP_NUMBER, loginResponse.getP().getOtpNumbers());
                        SharePrefs.getInstance(WelcomeActivity.this).putInt(SharePrefs.ASSIGNMENT_ID, 0);
                        ///utils.setToast(WelcomeActivity.this, "Welcome " + loginResponse.getP().getPeopleFirstName().trim());

                        OTPNumberString = loginResponse.getP().getOtpNumbers();
                        String mobileNumberSTString = loginResponse.getP().getMobile();
                        tv_oldnumber.setText(mobileNumberSTString);
                        Log.e("OTP", "OTP" + OTPNumberString);
                        UserAuth regApk = loginResponse.getP().getRegisteredApk();
                        if (regApk != null) {
                            String Password = regApk.getPassword();
                            String username = regApk.getUserName();
                            if (!Utils.checkInternetConnection(WelcomeActivity.this)) {
                                Toast.makeText(WelcomeActivity.this, getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                            } else {
                                otpViewModel.hitTokenAPI("password", username, Password);
                            }
                        }

                    } else {
                        Utils.setToast(WelcomeActivity.this, loginResponse.getMessage());
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(WelcomeActivity.this, getResources().getString(R.string.errorString), Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(WelcomeActivity.this, response.toString(), Toast.LENGTH_SHORT).show();

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
                        startTimer(tvResendOtpTimer, resendotp);
                        SharePrefs.getInstance(WelcomeActivity.this).putString(SharePrefs.TOKEN, tokenResponse.getAccess_token());
                        int current = getItem(+1);
                        if (current < layouts.length) {
                            // move to next screen
                            viewPager.setCurrentItem(current);
                        }
                        Utils.leftTransaction(this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(WelcomeActivity.this, getResources().getString(R.string.errorString), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(WelcomeActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void startTimer(TextView tvResendOtpTimer, TextView resendotp) {

        cTimer = new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                //  Logger.logD("WelcomeActivity", "Timer:" + millisUntilFinished / 1000);
                tvResendOtpTimer.setText(getString(R.string.resendotp) + ":" + "" + millisUntilFinished / 1000);
            }

            public void onFinish() {

                resendotp.setEnabled(true);
                resendotp.setBackgroundResource(R.drawable.rectangle);
                resendotp.setPadding(8, 8, 8, 8);
                //resendotp.setTextColor(context.getResources().getColor(R.color.colorAccent));
                //Toast.makeText(context, getString(R.string.resendotp), Toast.LENGTH_SHORT).show();
            }
        };
        cTimer.start();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }
}