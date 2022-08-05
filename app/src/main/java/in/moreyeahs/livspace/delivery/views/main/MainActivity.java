package in.moreyeahs.livspace.delivery.views.main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonElement;
import in.moreyeahs.livspace.delivery.BuildConfig;
import in.moreyeahs.livspace.delivery.R;
import in.moreyeahs.livspace.delivery.api.RestClient;
import in.moreyeahs.livspace.delivery.databinding.ActivityMainBinding;
import in.moreyeahs.livspace.delivery.listener.MainListener;
import in.moreyeahs.livspace.delivery.service.MyService;
import in.moreyeahs.livspace.delivery.utilities.GpsUtils;
import in.moreyeahs.livspace.delivery.utilities.LocaleHelper;
import in.moreyeahs.livspace.delivery.utilities.MarshmallowPermissions;
import in.moreyeahs.livspace.delivery.utilities.RxBus;
import in.moreyeahs.livspace.delivery.utilities.SharePrefs;
import in.moreyeahs.livspace.delivery.utilities.TextUtils;
import in.moreyeahs.livspace.delivery.utilities.Utils;
import in.moreyeahs.livspace.delivery.viewmodels.MainModel;
import in.moreyeahs.livspace.delivery.views.fragment.AssginmentSettleFragment;
import in.moreyeahs.livspace.delivery.views.fragment.AssignmentBillingFragment;
import in.moreyeahs.livspace.delivery.views.fragment.CashCollectionFragment;
import in.moreyeahs.livspace.delivery.views.fragment.HistoryFragment;
import in.moreyeahs.livspace.delivery.views.fragment.MultipleAssignmentFragment;
import in.moreyeahs.livspace.delivery.views.fragment.MyAssignmentFragment;
import in.moreyeahs.livspace.delivery.views.fragment.PoAssignmentFragment;
import in.moreyeahs.livspace.delivery.views.fragment.ProfileFragment;
import in.moreyeahs.livspace.delivery.views.fragment.RejectAssginmentFragment;
import in.moreyeahs.livspace.delivery.views.fragment.SettleAssignmentFragment;
import in.moreyeahs.livspace.delivery.views.returnorder.ReturnOrderListActivity;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {
    private ActivityMainBinding mBinding;
    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;
    public ActionBarDrawerToggle toggle;
    private MultipleAssignmentFragment multipleAssignmentFragment;
    private SharedPreferences sharedPreferences;
    public RelativeLayout titlebar;
    public LinearLayout orderItemView;
    public TextView orderID, AssignmentID, tvTimmer;
    public ImageView ivBack;
    private Disposable acceptAssignDes;
    private Handler handler = new Handler();
    private boolean doubleBackToExitPressedOnce = false;
    private boolean apiType=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        permission();
        handleAcceptAssignMessage();
        new GpsUtils(this).turnGPSOn(isGPSEnable -> {
            // turn on GPS
        });

        if (handler != null) {
            // Auto Refresh
            handler.post(timedTask);
        }

        setValue();
        listeners();
        getNotificationEvent();
        createViews();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (SharePrefs.getInstance(getApplicationContext()).getBoolean(SharePrefs.HAS_RETURN_ORDER)) {
            mBinding.ivReturnCount.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        GetCurrentAPKVersion();
    }

    public void GetCurrentAPKVersion() {
        final CompositeDisposable disposables = new CompositeDisposable();
        DisposableObserver<JsonElement> observerDes = RestClient.getInstance().getService().getCurrentVersion()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<JsonElement>() {
                    @Override
                    public void onNext(JsonElement result) {
                        if (result != null) {
                            if (result.getAsJsonObject().get("status").getAsBoolean()) {
                                if (!result.getAsJsonObject().get("deliveryAppVersion").getAsString().equalsIgnoreCase(BuildConfig.VERSION_NAME)) {
                                    @SuppressLint("RestrictedApi") AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                                    alertDialogBuilder.setTitle(getString(R.string.youAreNotUpdatedTitle));
                                    alertDialogBuilder.setMessage(getString(R.string.youAreNotUpdatedMessage) + " " + result.getAsJsonObject().get("deliveryAppVersion").getAsString() + getString(R.string.youAreNotUpdatedMessage1));
                                    alertDialogBuilder.setCancelable(false);
                                    alertDialogBuilder.setPositiveButton(R.string.update, (dialog, id) -> {
                                        logout();
                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(BuildConfig.playStoreURL)));
                                        dialog.cancel();
                                    });
                                    alertDialogBuilder.setNegativeButton("Cancel", (dialog, i) -> {
                                        dialog.cancel();
                                        MainActivity.this.finish();
                                        //  ContinueToHomeScreen();
                                    });
                                    alertDialogBuilder.show();
                                }
                            }
                        }
                        disposables.clear();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        disposables.clear();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        disposables.add(observerDes);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        if (id == R.id.my_task) {
            return false;
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.drawer_menu:
                openDrawer();
                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mBinding.appbarMain.drawerMenu.getWindowToken(), 0);
                break;
            case R.id.profile:
                switchContentWithStack(new ProfileFragment());
                closeDrawer();
                break;
            case R.id.li_return_order:
                closeDrawer();
                startActivity(new Intent(getApplicationContext(), ReturnOrderListActivity.class));
                SharePrefs.getInstance(getApplicationContext()).putBoolean(SharePrefs.HAS_RETURN_ORDER, false);
                mBinding.ivReturnCount.setVisibility(View.GONE);
                Utils.leftTransaction(this);
                break;
        }
    }

    private Runnable timedTask = new Runnable() {
        @Override
        public void run() {
            try {
                if (BuildConfig.DEBUG) {
                    if (!Utils.checkInternetConnection(MainActivity.this)) {
                        Utils.setToast(MainActivity.this, getString(R.string.network_error));
                    } else {
                        MainActivity.this.startService(new Intent(MainActivity.this, MyService.class));
                    }
                    handler.postDelayed(timedTask, 1000);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private void getNotificationEvent() {
        RxBus.getInstance().getEvent().observeOn(AndroidSchedulers.mainThread()).subscribe(new DisposableObserver<Object>() {
            @Override
            public void onNext(Object o) {
                if (o instanceof String) {
                    String str = (String) o;
                    if (str.equalsIgnoreCase("true")) {
                        logout();
                        Utils.setToast(MainActivity.this, "you are logged in on Another Device");
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void listeners() {
        mBinding.setMainListener(new MainListener() {
            @Override
            public void myTaskClicked() {
                checkBackStackStatus();
                closeDrawer();
            }

            @Override
            public void myAssignmentClicked() {
                try {
                    if (!Utils.checkInternetConnection(MainActivity.this)) {
                        Utils.setToast(MainActivity.this, getResources().getString(R.string.network_error));
                    } else {
                        switchContentWithStack(new MyAssignmentFragment());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //  checkBackStackStatus();
                closeDrawer();
            }

            @Override
            public void myCashCollectionClicked() {
                apiType=true;
                try {
                    if (!Utils.checkInternetConnection(MainActivity.this)) {
                        Utils.setToast(MainActivity.this, getResources().getString(R.string.network_error));
                    } else {
                        Bundle bundle= new Bundle();
                        Fragment fragment= new CashCollectionFragment();
                        bundle.putBoolean("type",apiType);
                        fragment.setArguments(bundle);
                        switchContentWithStack(fragment);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //  checkBackStackStatus();
                closeDrawer();
            }

            @Override
            public void myCreditCollectionClicked() {
                   apiType=false;
                try {
                    if (!Utils.checkInternetConnection(MainActivity.this)) {
                        Utils.setToast(MainActivity.this, getResources().getString(R.string.network_error));
                    } else {
                        Bundle bundle= new Bundle();
                        Fragment fragment= new CashCollectionFragment();
                        bundle.putBoolean("type",apiType);
                        fragment.setArguments(bundle);
                        switchContentWithStack(fragment);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //  checkBackStackStatus();
                closeDrawer();

            }

            @Override
            public void deliveryboy_location() {
                startActivity(new Intent(MainActivity.this,DrivingDirectionActivity.class));
            }

            @Override
            public void myAcceptedAssignmentClicked() {
                checkBackStackStatus();
                closeDrawer();
            }

            @Override
            public void orderCurrencyClicked() {
                if (!Utils.checkInternetConnection(MainActivity.this)) {
                    Utils.setToast(MainActivity.this, getResources().getString(R.string.network_error));
                } else {
                    switchContentWithStack(new SettleAssignmentFragment());
                    // checkBackStackStatus();
                }
                closeDrawer();
            }

            @Override
            public void historyClicked() {
                if (!Utils.checkInternetConnection(MainActivity.this)) {
                    Utils.setToast(MainActivity.this, getResources().getString(R.string.network_error));
                } else {
                    switchContentWithStack(new HistoryFragment());
                    // checkBackStackStatus();
                }
                closeDrawer();
            }

            @Override
            public void performanceClicked() {

            }

            @Override
            public void rateUsClicked() {

            }

            @Override
            public void settingClicked() {

            }

            @Override
            public void logOutClicked() {
                logout();
                closeDrawer();
            }

            @Override
            public void AssignmentBilling() {
                if (!Utils.checkInternetConnection(MainActivity.this)) {
                    Utils.setToast(MainActivity.this, getResources().getString(R.string.network_error));
                } else {
                    switchContentWithStack(new PoAssignmentFragment());
                }
                closeDrawer();
            }

            @Override
            public void ChangeLanguage() {
                startActivity(new Intent(MainActivity.this, ChangeLanguageActivity.class));
            }

            @Override
            public void RejectAssginmentClicked() {
                if (!Utils.checkInternetConnection(MainActivity.this)) {
                    Utils.setToast(MainActivity.this, getResources().getString(R.string.network_error));
                } else {
                    switchContentWithStack(new RejectAssginmentFragment());
                }
                closeDrawer();
            }

            @Override
            public void AssginmentSettle() {
                if (!Utils.checkInternetConnection(MainActivity.this)) {
                    Utils.setToast(MainActivity.this, getResources().getString(R.string.network_error));
                } else {
                    switchContentWithStack(new AssginmentSettleFragment());
                }
                closeDrawer();
            }
        });
    }

    private void setValue() {
        try {
            String imageProfile = SharePrefs.getInstance(MainActivity.this).getString(SharePrefs.PEAOPLE_IMAGE);
            String name = SharePrefs.getInstance(MainActivity.this).getString(SharePrefs.PEAOPLE_FIRST_NAME);
            String mobile = SharePrefs.getInstance(MainActivity.this).getString(SharePrefs.MOBILE);
            MainModel mainModel = new MainModel(this);
            mainModel.setName(name);
            mainModel.setNumber("" + mobile);
            if (!TextUtils.isNullOrEmpty(imageProfile)) {
                mainModel.setProfileImage(imageProfile);
            }
            mBinding.Home.setMainModel(mainModel);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createViews() {
        mDrawerLayout = mBinding.container;
        navigationView = mBinding.navView;
        toggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        configureDrawerMenuItem();

        /*Set home screen*/
        if (multipleAssignmentFragment == null)
            multipleAssignmentFragment = new MultipleAssignmentFragment();

        setDefaultFragment(multipleAssignmentFragment);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        // setView
        viewVisibleTextView(false);
    }

    private void permission() {
        if (Build.VERSION.SDK_INT >= 23) {
            // Marshmallow+
            if (!MarshmallowPermissions.checkPermissionLocation(MainActivity.this)) {
                MarshmallowPermissions.requestPermissionLocation(MainActivity.this, MarshmallowPermissions.PERMISSION_REQUEST_CODE_LOCATION);
            }
        }
    }

    public void configureDrawerMenuItem() {
        ImageView drawerMenuIV = mBinding.appbarMain.drawerMenu;
        navigationView.setNavigationItemSelectedListener(this);
        drawerMenuIV.setOnClickListener(this);
        mBinding.Home.profile.setOnClickListener(this);
        mBinding.liReturnOrder.setOnClickListener(this);
    }

    public void setDefaultFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.content, fragment)
                .addToBackStack(fragment.getClass().getSimpleName())
                .commit();
    }

    public void switchContent(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, fragment).
                addToBackStack(null)
                .commit();
    }

    public void switchContentWithStack(Fragment fragment) {

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, fragment)

                .addToBackStack(fragment.getClass().getSimpleName())
                .commit();
    }

    public void checkBackStackStatus() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack(new MultipleAssignmentFragment().getClass().getSimpleName(), 0);
        }
    }

    public void viewVisibleTextView(boolean llHide) {
        titlebar = mBinding.appbarMain.titlebar;
        tvTimmer = mBinding.appbarMain.tvTimmer;
        orderItemView = mBinding.appbarMain.llOderIdView;
        if (llHide) {
            titlebar.setVisibility(View.GONE);
            orderItemView.setVisibility(View.VISIBLE);
        } else {
            titlebar.setVisibility(View.VISIBLE);
            orderItemView.setVisibility(View.GONE);
        }
        AssignmentID = mBinding.appbarMain.tvAssitId;
        orderID = mBinding.appbarMain.tvOrderno;
        ivBack = mBinding.appbarMain.ivBack;
    }

    public void closeDrawer() {
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

    public void openDrawer() {
        mDrawerLayout.openDrawer(GravityCompat.START);
    }

    @Override
    public void onBackPressed() {
        mDrawerLayout = mBinding.container;
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else if (getSupportFragmentManager().getBackStackEntryCount() <= 1) {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                finishAffinity();
                return;
            }
            doubleBackToExitPressedOnce = true;
            Snackbar.make(mDrawerLayout, "Please click back again to exit", Snackbar.LENGTH_SHORT).show();
            new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
        } else {
            super.onBackPressed();
        }
    }

    private void logout() {
        sharedPreferences.edit().clear().apply();
        Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
        Utils.rightTransaction(this);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    /**
     * get accept assigned condition
     ***/
    public void handleAcceptAssignMessage() {
        acceptAssignDes = RxBus.getInstance().getEvent()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {

                });
    }

    @Override
    public void onDestroy() {
        if (acceptAssignDes != null) {
            acceptAssignDes.dispose();
        }
        super.onDestroy();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }
}