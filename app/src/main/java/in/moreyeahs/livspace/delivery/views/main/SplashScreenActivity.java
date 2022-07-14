package in.moreyeahs.livspace.delivery.views.main;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import in.moreyeahs.livspace.delivery.BuildConfig;
import in.moreyeahs.livspace.delivery.R;
import in.moreyeahs.livspace.delivery.databinding.ActivitySplashScreenBinding;
import in.moreyeahs.livspace.delivery.model.AppVersionModel;
import in.moreyeahs.livspace.delivery.utilities.ApiResponseList;
import in.moreyeahs.livspace.delivery.utilities.SharePrefs;
import in.moreyeahs.livspace.delivery.utilities.Utils;
import in.moreyeahs.livspace.delivery.viewmodels.SplashViewModel;

import java.util.ArrayList;
import java.util.List;

public class SplashScreenActivity extends AppCompatActivity {
    private SplashViewModel splashViewModel;
    public Dialog customDialog;
    private String App_version;
    private boolean isCompulsory;
    private boolean isPresent = false;
    private SharedPreferences sharedPreferences;
    private Utils utils;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ActivitySplashScreenBinding mBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash_screen);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        customDialog = new Dialog(this, R.style.CustomDialog);
        utils = new Utils(this);

        splashViewModel = ViewModelProviders.of(this).get(SplashViewModel.class);
        mBinding.setSplashViewModel(splashViewModel);
        mBinding.setLifecycleOwner(this);

        // check version api response
        splashViewModel.versionResponse().observe(this, this::consumeResponse);

        new Handler().postDelayed(() -> {
            if (!Utils.checkInternetConnection(this)) {
                Utils.setToast(this, getString(R.string.network_error));
            } else {
                // check version api call
                splashViewModel.hitVersionApi();
            }
        }, 5000);
    }

    private void launchHomeScreen() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        Utils.leftTransaction(this);
        finish();
    }

    /*
     * method to handle response
     * */
    private void consumeResponse(ApiResponseList apiResponse) {
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
                Utils.setToast(getApplicationContext(), getResources().getString(R.string.errorString));
                break;
            default:
                break;
        }
    }

    /*
     * method to handle success response
     * */
    private void renderSuccessResponse(JsonArray response) {
        Log.d("response= ", "" + response);
        if (Utils.isJSONValid(response.toString())) {
            try {
                ArrayList<AppVersionModel> versionList = new Gson().fromJson(response.toString(), new TypeToken<List<AppVersionModel>>() {
                }.getType());
                if (versionList != null && versionList.size() != 0) {
                    for (int i = 0; i < versionList.size(); i++) {
                        App_version = versionList.get(i).getApp_version();
                        isCompulsory = versionList.get(i).isCompulsory();

                        if (BuildConfig.VERSION_NAME.equalsIgnoreCase(App_version) && isCompulsory) {
                            isPresent = true;
                            break;
                        } else {
                            isPresent = false;
                        }
                    }
                    if (isPresent) {
                        if (SharePrefs.getInstance(getApplicationContext()).getBoolean(SharePrefs.LOGGED)) {
                            launchHomeScreen();
                            finish();
                        } else {
                            startActivity(new Intent(getApplicationContext(), WelcomeActivity.class));
                            Utils.leftTransaction(this);
                            finish();
                        }
                    } else {
                        if (isCompulsory) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(SplashScreenActivity.this);
                            builder.setTitle(getString(R.string.youAreNotUpdatedTitle));
                            builder.setMessage(getString(R.string.youAreNotUpdatedMessage) + " " + App_version + getString(R.string.youAreNotUpdatedMessage1));
                            builder.setCancelable(false);
                            builder.setPositiveButton(R.string.update, (dialog, id) -> {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(BuildConfig.playStoreURL)));
                                logout();
                                dialog.cancel();
                            });
                            builder.setNegativeButton("Cancel", (dialog, i) -> {
                                dialog.cancel();
                                SplashScreenActivity.this.finish();
                            });
                            builder.show();
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(SplashScreenActivity.this);
                            builder.setTitle(getString(R.string.youAreNotUpdatedTitle));
                            builder.setMessage(getString(R.string.youAreNotUpdatedMessage) + " " + App_version + getString(R.string.youAreNotUpdatedMessage1));
                            builder.setCancelable(false);
                            builder.setPositiveButton(R.string.update, (dialog, id) -> {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(BuildConfig.playStoreURL)));
                                logout();
                                dialog.cancel();
                            });
                            builder.setNegativeButton("Cancel", (dialog, i) -> {
                                dialog.cancel();
                                finish();
                            });
                            builder.show();
                        }
                    }
                } else {
                    Utils.setToast(getApplicationContext(), getString(R.string.improper_response_server));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Utils.setToast(getApplicationContext(), response.toString());
        }
    }

    private void logout() {
        sharedPreferences.edit().clear().apply();
    }
}