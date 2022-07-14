package in.moreyeahs.livspace.delivery.views.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.gson.reflect.TypeToken;
import in.moreyeahs.livspace.delivery.R;
import in.moreyeahs.livspace.delivery.databinding.FragmentMultipleAssignmentBinding;
import in.moreyeahs.livspace.delivery.listener.AssginmentSettleViewDetailInterface;
import in.moreyeahs.livspace.delivery.model.AssginmentSettleResponseModel;
import in.moreyeahs.livspace.delivery.utilities.ApiResponse;
import in.moreyeahs.livspace.delivery.utilities.CommonMethods;
import in.moreyeahs.livspace.delivery.utilities.Logger;
import in.moreyeahs.livspace.delivery.utilities.SharePrefs;
import in.moreyeahs.livspace.delivery.utilities.Utils;
import in.moreyeahs.livspace.delivery.viewmodels.AssginmentSettleViewModel;
import in.moreyeahs.livspace.delivery.views.adapter.AssginmentsettleAdapter;
import in.moreyeahs.livspace.delivery.views.main.MainActivity;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;

import java.util.ArrayList;

public class AssginmentSettleFragment extends Fragment implements AssginmentSettleViewDetailInterface {
    private FragmentMultipleAssignmentBinding mBinding;
    private MainActivity activity;
    AssginmentSettleViewModel AssignmentViewModel;
    AssginmentsettleAdapter adapter;
    ArrayList<AssginmentSettleResponseModel> model;


    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        this.activity = (MainActivity) context;

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_multiple_assignment, container, false);
        initialization();
        return mBinding.getRoot();
    }


    private void consumeResponseAssignment(ApiResponse apiResponseObj) {
        try {
            switch (apiResponseObj.status) {
                case LOADING:
                    mBinding.progressBid.setVisibility(View.VISIBLE);
                    break;
                case SUCCESS:
                    mBinding.progressBid.setVisibility(View.GONE);
                    renderSuccessResponseAssignment(apiResponseObj.data);
                    break;
                case ERROR:
                    mBinding.progressBid.setVisibility(View.GONE);
                    Utils.setToast(activity, getResources().getString(R.string.errorString));
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void renderSuccessResponseAssignment(JsonElement response) {
        try {
            if (Utils.isJSONValid(response.toString())) {

                if (response.isJsonArray()) {
                    Logger.e(CommonMethods.getTag(this), response.toString());
                    try {
                        JSONArray obj = new JSONArray(response.toString());
                        model = new Gson().fromJson(String.valueOf(obj), new TypeToken<ArrayList<AssginmentSettleResponseModel>>() {
                        }.getType());
                        if (model.size() > 0) {
                            mBinding.tvMyTask.setVisibility(View.GONE);
                            mBinding.rvMyTask.setVisibility(View.VISIBLE);
                            adapter = new AssginmentsettleAdapter(activity, model, this);
                            mBinding.rvMyTask.setAdapter(adapter);
                        } else {
                            mBinding.tvMyTask.setVisibility(View.VISIBLE);
                            mBinding.rvMyTask.setVisibility(View.GONE);
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

    private void initialization() {
        setActionBarConfiguration();
        mBinding.rvMyTask.setLayoutManager(new LinearLayoutManager(getActivity()));
        mBinding.rvMyTask.setHasFixedSize(true);
        mBinding.SearchBar.setVisibility(View.GONE);
        AssignmentViewModel = ViewModelProviders.of(activity).get(AssginmentSettleViewModel.class);

        AssignmentViewModel.getAssignment().observe(activity, apiResponse -> {
            if (apiResponse != null) {
                consumeResponseAssignment(apiResponse);
            }
        });

        if (!Utils.checkInternetConnection(activity)) {
            Utils.setToast(activity, getResources().getString(R.string.network_error));
        } else {
            AssignmentViewModel.hitAssignmentApi(SharePrefs.getInstance(getActivity()).getInt(SharePrefs.PEOPLE_ID));
        }
    }

    private void setActionBarConfiguration() {
        final DrawerLayout drawer = getActivity().findViewById(R.id.container);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
        TextView tittleTextView = getActivity().findViewById(R.id.toolbar_title);
        TextView assignmentid = getActivity().findViewById(R.id.assignmentid);
        assignmentid.setVisibility(View.GONE);
        tittleTextView.setVisibility(View.VISIBLE);
        tittleTextView.setText("Assignment Settle");
        TextView starttimer = getActivity().findViewById(R.id.start_timer);
        starttimer.setVisibility(View.VISIBLE);
        starttimer.setText(R.string.history);
        TextView timer = getActivity().findViewById(R.id.tv_timer);
        timer.setVisibility(View.GONE);
        toolbar.setNavigationOnClickListener(v -> {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                drawer.openDrawer(GravityCompat.START);
            }
        });
        starttimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.switchContentWithStack(new AssginmentSettleHistoryFragment());


            }
        });
        ((MainActivity) getActivity()).toggle.syncState();
    }


    @Override
    public void viewDetailClick(int id) {
        Bundle bundle = new Bundle();
        Fragment fragment = new AssginmentSettleDetailFragment();
        for (int i = 0; i < model.size(); i++) {
            if (model.get(i).getId() == id) {
                bundle.putSerializable("list", model.get(i).getDBoyAssignmentDeposits());
                bundle.putInt("id", model.get(i).getId());
                bundle.putString("pdfurl", model.get(i).getIsUNSignOffUrl());
                break;
            }
        }
        fragment.setArguments(bundle);
        activity.switchContentWithStack(fragment);
    }

    @Override
    public void saveCommentClick(ArrayList<AssginmentSettleResponseModel.DBoyAssignmentDeposits> deliveryIssuanceId) {

    }
}
