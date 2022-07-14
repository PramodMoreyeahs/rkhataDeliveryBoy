package in.moreyeahs.livspace.delivery.views.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.JsonObject;

import in.moreyeahs.livspace.delivery.R;
import in.moreyeahs.livspace.delivery.databinding.ActivityDeliveryRequestsBinding;
import in.moreyeahs.livspace.delivery.model.AcceptRejectDC;
import in.moreyeahs.livspace.delivery.model.AssignmentListResponse;
import in.moreyeahs.livspace.delivery.utilities.CommonClassForAPI;
import in.moreyeahs.livspace.delivery.utilities.SharePrefs;
import in.moreyeahs.livspace.delivery.utilities.Utils;
import in.moreyeahs.livspace.delivery.views.adapter.AssignmentListAdapter;
import in.moreyeahs.livspace.delivery.views.main.MainActivity;
import io.reactivex.observers.DisposableObserver;

public class PoAssignmentFragment extends Fragment implements AssignmentListAdapter.AssignmentListInterface {
    private MainActivity activity;
    private ActivityDeliveryRequestsBinding mBinding;
    Utils utils;
    CommonClassForAPI commonClassForAPI;
    AssignmentListAdapter adapter;

    DisposableObserver<AssignmentListResponse> assignmentDis = new DisposableObserver<AssignmentListResponse>() {
        @Override
        public void onNext(AssignmentListResponse response) {
            Utils.hideProgressDialog();
            try {
                if (response.getMessage().equalsIgnoreCase("Success.")) {
                    adapter.setAssignmentList(response.getDeliveryissuancepo());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }

        @Override
        public void onComplete() {
        }
    };

    DisposableObserver<JsonObject> acceptRejectOrder = new DisposableObserver<JsonObject>() {
        @Override
        public void onNext(JsonObject response) {
            Utils.hideProgressDialog();
            try {
                loadAssignmentList();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }

        @Override
        public void onComplete() {
        }
    };

    DisposableObserver<Boolean> submitAssignment = new DisposableObserver<Boolean>() {
        @Override
        public void onNext(Boolean response) {
            Utils.hideProgressDialog();
            try {
                if (response.booleanValue()){
                    loadAssignmentList();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
            Utils.hideProgressDialog();
            Utils.setToast(activity, "Some error occurred, Please try again!");
        }

        @Override
        public void onComplete() {
        }
    };

    public PoAssignmentFragment() {
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (MainActivity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.activity_delivery_requests, container, false);
        return mBinding.getRoot();
    }

    public void initialization() {
        commonClassForAPI = CommonClassForAPI.getInstance(activity);
        utils = new Utils(activity);

        LinearLayoutManager lm = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        adapter = new AssignmentListAdapter(activity, PoAssignmentFragment.this);
        mBinding.rvAssignmentList.setLayoutManager(lm);
        mBinding.rvAssignmentList.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setActionBarConfiguration();
        initialization();
        loadAssignmentList();
    }

    private void setActionBarConfiguration() {
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        //actionBar.setLogo(R.drawable.ic_action_bar_logo);
        final DrawerLayout drawer = getActivity().findViewById(R.id.container);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
        TextView tittleTextView = getActivity().findViewById(R.id.toolbar_title);
        TextView assignmentid = getActivity().findViewById(R.id.assignmentid);
        assignmentid.setVisibility(View.GONE);
        tittleTextView.setVisibility(View.VISIBLE);
        tittleTextView.setText("PO Assignment");
        TextView starttimer = getActivity().findViewById(R.id.start_timer);
        starttimer.setVisibility(View.GONE);
        TextView timer = getActivity().findViewById(R.id.tv_timer);
        timer.setVisibility(View.GONE);
        toolbar.setNavigationOnClickListener(v -> {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                drawer.openDrawer(GravityCompat.START);
            }
        });
        ((MainActivity) getActivity()).toggle.syncState();
    }

    public void loadAssignmentList() {
        if (utils.isNetworkAvailable()) {
            if (commonClassForAPI != null) {
                commonClassForAPI.fetchAssignmentList(assignmentDis, SharePrefs.getInstance(activity).getInt(SharePrefs.PEOPLE_ID));
            }
        } else {
            utils.setToast(activity, getString(R.string.internet_connection));
        }
    }

    @Override
    public void onButtonClick(int poId, Boolean isAccepted) {
        if (utils.isNetworkAvailable()) {
            if (commonClassForAPI != null) {
                commonClassForAPI.acceptRejectOrder(acceptRejectOrder, new AcceptRejectDC(poId, isAccepted, ""));
            }
        } else {
            utils.setToast(activity, getString(R.string.internet_connection));
        }
    }

    @Override
    public void submitAssignment(int poId) {
        if (utils.isNetworkAvailable()) {
            if (commonClassForAPI != null) {
                commonClassForAPI.submitCompletedAssignment(submitAssignment, poId);
            }
        } else {
            utils.setToast(activity, getString(R.string.internet_connection));
        }
    }
}