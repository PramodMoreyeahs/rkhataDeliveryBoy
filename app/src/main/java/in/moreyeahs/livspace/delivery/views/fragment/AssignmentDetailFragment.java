package in.moreyeahs.livspace.delivery.views.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import in.moreyeahs.livspace.delivery.R;
import in.moreyeahs.livspace.delivery.databinding.FragmentMyTaskBinding;
import in.moreyeahs.livspace.delivery.listener.orderdetailClick;
import in.moreyeahs.livspace.delivery.model.CurrentEndTimeModel;
import in.moreyeahs.livspace.delivery.model.MyTaskModel;
import in.moreyeahs.livspace.delivery.model.MyTaskModelMain;
import in.moreyeahs.livspace.delivery.utilities.ApiResponse;
import in.moreyeahs.livspace.delivery.utilities.CommonMethods;
import in.moreyeahs.livspace.delivery.utilities.CustomRunnable;
import in.moreyeahs.livspace.delivery.utilities.GPSTracker;
import in.moreyeahs.livspace.delivery.utilities.Logger;
import in.moreyeahs.livspace.delivery.utilities.SharePrefs;
import in.moreyeahs.livspace.delivery.utilities.Utils;
import in.moreyeahs.livspace.delivery.viewmodels.MyTaskViewModel;
import in.moreyeahs.livspace.delivery.views.adapter.MyTaskAdapter;
import in.moreyeahs.livspace.delivery.views.main.MainActivity;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class AssignmentDetailFragment extends Fragment implements orderdetailClick {
    private FragmentMyTaskBinding mBinding;
    private MyTaskViewModel myTaskViewModel;
    private MainActivity activity;
    private TextView tv_assignmentid;
    private String time, type = "";
    private CustomRunnable customRunnable;
    private Handler handler;
    private int assignmentID;
    private boolean isRefresh = false;

    private double lat, lg;

    public AssignmentDetailFragment() {

    }

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        this.activity = (MainActivity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (handler != null) {
            handler.removeCallbacks(customRunnable);
            //customRunnable.stop();
            customRunnable = null;
            handler = null;
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_task, container, false);
        View view = mBinding.getRoot();
        Bundle bundle = this.getArguments();
        assignmentID = bundle.getInt("deliveryIssuanceId");
        time = bundle.getString("time");
        type = bundle.getString("type");
        intView();

        myTaskViewModel.getMyTaskData().observe(this, apiResponse -> {
            if (apiResponse != null) {
                consumeResponse(apiResponse);
            }
        });
        myTaskViewModel.getRejectedOrder().observe(this, apiResponse -> {
            if (apiResponse != null) {
                consumeResponseRejectedOrder(apiResponse);
            }
        });
        myTaskViewModel.getStartStopTimerDetails().observe(this, apiResponse -> {
            if (apiResponse != null) {
                startStopTimeResponse(apiResponse);
            }
        });

        if (handler != null) {
            if (customRunnable != null) {
                handler.removeCallbacks(customRunnable);
                //customRunnable.stop();
                customRunnable = null;
            }
            handler = null;
        }
        if (type!=null&&type.equalsIgnoreCase("rejectAssign")) {
            mBinding.SearchBar.setVisibility(View.GONE);
            myTaskViewModel.hitRejectedOrderApi(assignmentID, SharePrefs.getInstance(activity).getString(SharePrefs.MOBILE));

        }
        else{
            myTaskViewModel.hitMyTaskApi(assignmentID, SharePrefs.getInstance(activity).getString(SharePrefs.MOBILE));

            mBinding.SearchBar.setVisibility(View.VISIBLE);
        }
        mBinding.searchTxt.setOnClickListener(v -> {
            Fragment fragment = new SearchFragment();
            activity.switchContentWithStack(fragment);
        });
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mBinding.searchTxt.getWindowToken(), 0);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (type==null) {
            if (isRefresh) {
                isRefresh = false;
                myTaskViewModel.hitMyTaskApi(assignmentID, SharePrefs.getInstance(activity).getString(SharePrefs.MOBILE));
            }

        }
        else{
            if (isRefresh) {
                isRefresh = false;
                myTaskViewModel.hitRejectedOrderApi(assignmentID, SharePrefs.getInstance(activity).getString(SharePrefs.MOBILE));
            }
        }
        setActionBarConfiguration();
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mBinding.searchTxt.getWindowToken(), 0);

    }

    private void consumeResponse(ApiResponse apiResponse) {
        switch (apiResponse.status) {
            case LOADING:
                mBinding.progressBid.setVisibility(View.VISIBLE);
                break;
            case SUCCESS:
                mBinding.progressBid.setVisibility(View.GONE);
                renderSuccessResponse(apiResponse.data);
                break;
            case ERROR:
                mBinding.progressBid.setVisibility(View.GONE);
                Utils.setToast(activity, getResources().getString(R.string.errorString));
                break;
            default:
                break;
        }
    }
    private void consumeResponseRejectedOrder(ApiResponse apiResponse) {
        switch (apiResponse.status) {
            case LOADING:
                mBinding.progressBid.setVisibility(View.VISIBLE);
                break;
            case SUCCESS:
                mBinding.progressBid.setVisibility(View.GONE);
                renderSuccessResponseRejectedOrder(apiResponse.data);
                break;
            case ERROR:
                mBinding.progressBid.setVisibility(View.GONE);
                Utils.setToast(activity, getResources().getString(R.string.errorString));
                break;
            default:
                break;
        }
    }

    private void renderSuccessResponse(JsonElement response) {
        if (Utils.isJSONValid(response.toString())) {
            if (!response.isJsonNull()) {
                try {
                    JSONObject obj = new JSONObject(response.toString());
                    MyTaskModelMain myTaskModelMain = new Gson().fromJson(obj.toString(), MyTaskModelMain.class);
                    if (myTaskModelMain.getStatus()) {
                        boolean status = true;
                        for (int i = 0; i < myTaskModelMain.getOrderDispatchedObj().size(); i++) {
                            if (!myTaskModelMain.getOrderDispatchedObj().get(i).getStatus().equalsIgnoreCase("Delivered") && !myTaskModelMain.getOrderDispatchedObj().get(i).getStatus().equalsIgnoreCase("Delivery Canceled") && !myTaskModelMain.getOrderDispatchedObj().get(i).getStatus().equalsIgnoreCase("Delivery Redispatch")) {
                                status = true;
                                break;
                            } else {
                                status = false;
                            }
                        }
                        if (status) {
                            tv_assignmentid.setText(activity.getString(R.string.assignment_id) + myTaskModelMain.getOrderDispatchedObj().get(0).getDeliveryIssuanceId());
                            ArrayList<MyTaskModel> list = myTaskModelMain.getOrderDispatchedObj();
                            GPSTracker gpsTracker = new GPSTracker(activity);
                            double latitude = gpsTracker.getLatitude();
                            double longitutde = gpsTracker.getLongitude();
                            System.out.println("Current  lat" + latitude);
                            System.out.println("Current  lag" + longitutde);
                            Location locationB = new Location("point B");
                            locationB.setLatitude(latitude);
                            locationB.setLongitude(longitutde);
                            float distance = 0f;
                            for (int i = 0; i < myTaskModelMain.getOrderDispatchedObj().size(); i++) {
                                lat = myTaskModelMain.getOrderDispatchedObj().get(i).getLat();
                                lg = myTaskModelMain.getOrderDispatchedObj().get(i).getLg();
                                if (lat != 0 && lg != 0) {
                                    Location locationA = new Location("point A");
                                    locationA.setLatitude(lat);
                                    locationA.setLongitude(lg);
                                    distance = locationB.distanceTo(locationA) / 1000;
                                    System.out.println("Distance" + distance);
                                    myTaskModelMain.getOrderDispatchedObj().get(i).setDistance(distance);
                                }
                            }
                            Collections.sort(list, (o1, o2) -> (int) (o1.getDistance() - o2.getDistance()));
                            MyTaskAdapter adapter = new MyTaskAdapter(activity, list, type, time, this);
                            mBinding.rvMyTask.setAdapter(adapter);
                        } else {
                            if (!Utils.checkInternetConnection(activity)) {
                                Utils.setToast(activity, getResources().getString(R.string.network_error));
                            } else {
                                CurrentEndTimeModel currentEndTimeModel = new CurrentEndTimeModel(myTaskModelMain.getOrderDispatchedObj().get(0).getDeliveryIssuanceId(), SharePrefs.getInstance(activity).getInt(SharePrefs.PEOPLE_ID), false, "0", String.valueOf(Utils.getTemp(activity)), true);
                                myTaskViewModel.hitPostTimerApi(currentEndTimeModel);
                            }
                        }
                    } else {
                        Toast.makeText(activity, myTaskModelMain.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            Toast.makeText(activity, response.toString(), Toast.LENGTH_SHORT).show();
        }
    }   private void renderSuccessResponseRejectedOrder(JsonElement response) {
        if (Utils.isJSONValid(response.toString())) {
            if (!response.isJsonNull()) {
                try {
                    JSONObject obj = new JSONObject(response.toString());
                    MyTaskModelMain myTaskModelMain = new Gson().fromJson(obj.toString(), MyTaskModelMain.class);
                    if (myTaskModelMain.getStatus()) {
                        tv_assignmentid.setText(activity.getString(R.string.assignment_id) + myTaskModelMain.getOrderDispatchedObj().get(0).getDeliveryIssuanceId());
                            ArrayList<MyTaskModel> list = myTaskModelMain.getOrderDispatchedObj();
                            GPSTracker gpsTracker = new GPSTracker(activity);
                            double latitude = gpsTracker.getLatitude();
                            double longitutde = gpsTracker.getLongitude();
                            System.out.println("Current  lat" + latitude);
                            System.out.println("Current  lag" + longitutde);
                            Location locationB = new Location("point B");
                            locationB.setLatitude(latitude);
                            locationB.setLongitude(longitutde);
                            float distance = 0f;
                            for (int i = 0; i < myTaskModelMain.getOrderDispatchedObj().size(); i++) {
                                lat = myTaskModelMain.getOrderDispatchedObj().get(i).getLat();
                                lg = myTaskModelMain.getOrderDispatchedObj().get(i).getLg();
                                if (lat != 0 && lg != 0) {
                                    Location locationA = new Location("point A");
                                    locationA.setLatitude(lat);
                                    locationA.setLongitude(lg);
                                    distance = locationB.distanceTo(locationA) / 1000;
                                    System.out.println("Distance" + distance);
                                    myTaskModelMain.getOrderDispatchedObj().get(i).setDistance(distance);
                                }
                            }
                            Collections.sort(list, (o1, o2) -> (int) (o1.getDistance() - o2.getDistance()));
                            MyTaskAdapter adapter = new MyTaskAdapter(activity, list, type, "",this);
                            mBinding.rvMyTask.setAdapter(adapter);
                        }
                    else {
                        activity.onBackPressed();
                        Toast.makeText(activity, myTaskModelMain.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else{
             activity.onBackPressed();
            }
        } else {
            Toast.makeText(activity, response.toString(), Toast.LENGTH_SHORT).show();
            activity.onBackPressed();
        }
    }

    private void startStopTimeResponse(ApiResponse apiResponse) {
        switch (apiResponse.status) {
            case LOADING:
                mBinding.progressBid.setVisibility(View.VISIBLE);
                break;
            case SUCCESS:
                mBinding.progressBid.setVisibility(View.GONE);
                postStartTimeResponseModel(apiResponse.data);
                break;
            case ERROR:
                mBinding.progressBid.setVisibility(View.GONE);
                Utils.setToast(activity, getResources().getString(R.string.errorString));
                break;
            default:
                break;
        }
    }

    private void postStartTimeResponseModel(JsonElement response) {
        try {
            if (Utils.isJSONValid(response.toString())) {
                if (!response.isJsonNull() && response != null) {
                    Logger.e(CommonMethods.getTag(this), response.toString());
                    JSONObject obj = new JSONObject(response.toString());
                    try {
                        if (obj.getBoolean("Status")) {
                            startActivity(new Intent(activity, MainActivity.class));
                        } else {
                            Toast.makeText(activity, obj.getString("Message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(activity, getResources().getString(R.string.errorString), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(activity, response.toString(), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void intView() {
        try {
            mBinding.rvMyTask.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
            mBinding.rvMyTask.setHasFixedSize(true);
            myTaskViewModel = ViewModelProviders.of(this).get(MyTaskViewModel.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void timer(String time, TextView timer) {
        long millse = 0;
        try {
            long currMillis = System.currentTimeMillis();
            SimpleDateFormat sdf1 = new SimpleDateFormat(Utils.myFormat, Locale.getDefault());
            sdf1.setTimeZone(TimeZone.getDefault());
            if (time != null) {
                Date startTime = sdf1.parse(time);
                long startEpoch = startTime.getTime();
                millse = currMillis - startEpoch;
            }
            if (handler != null) {
                handler = null;
            }
            if (customRunnable != null) {
                customRunnable = null;
            }
            handler = new Handler();
            customRunnable = new CustomRunnable(handler, timer, 10000);
            handler.removeCallbacks(customRunnable);
            customRunnable.holder = timer;
            customRunnable.millisUntilFinished = millse; //Current time - received time
            handler.postDelayed(customRunnable, 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setActionBarConfiguration() {
        final DrawerLayout drawer = getActivity().findViewById(R.id.container);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        RelativeLayout layout = getActivity().findViewById(R.id.titlebar);
        layout.setVisibility(View.VISIBLE);
        LinearLayout linearLayout = getActivity().findViewById(R.id.ll_oder_id_view);
        linearLayout.setVisibility(View.GONE);
        TextView startTimerbtn = getActivity().findViewById(R.id.start_timer);
        TextView timer = getActivity().findViewById(R.id.tv_timer);
        TextView tv_timmer = getActivity().findViewById(R.id.tv_timmer);
        tv_timmer.setVisibility(View.GONE);
        tv_assignmentid = getActivity().findViewById(R.id.assignmentid);
        TextView tittleTextView = getActivity().findViewById(R.id.toolbar_title);
        startTimerbtn.setVisibility(View.GONE);
        tittleTextView.setVisibility(View.GONE);
        tv_assignmentid.setVisibility(View.VISIBLE);
        if (type!=null&&type.equalsIgnoreCase("rejectAssign")) {
            timer.setVisibility(View.GONE);
        }
        else{
            timer.setVisibility(View.VISIBLE);
        }
        timer(time, timer);

        toolbar.setNavigationOnClickListener(v -> {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                drawer.openDrawer(GravityCompat.START);
            }
        });
    }

    @Override
    public void onlineLayHideUnHideClicked(boolean param) {
        this.isRefresh = param;
    }
}