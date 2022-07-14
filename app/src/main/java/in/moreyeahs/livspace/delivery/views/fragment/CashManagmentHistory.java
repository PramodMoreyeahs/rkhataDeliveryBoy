package in.moreyeahs.livspace.delivery.views.fragment;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import in.moreyeahs.livspace.delivery.R;
import in.moreyeahs.livspace.delivery.databinding.CashCollectionHistoryBinding;
import in.moreyeahs.livspace.delivery.listener.HistoryEditListener;
import in.moreyeahs.livspace.delivery.model.CurrencyCollectionDc;
import in.moreyeahs.livspace.delivery.model.CurrencyHistoryDataModel;
import in.moreyeahs.livspace.delivery.model.CurrencyHistoryModel;
import in.moreyeahs.livspace.delivery.utilities.ApiResponse;
import in.moreyeahs.livspace.delivery.utilities.SharePrefs;
import in.moreyeahs.livspace.delivery.utilities.Utils;
import in.moreyeahs.livspace.delivery.viewmodels.CashHistoryViewModel;
import in.moreyeahs.livspace.delivery.views.adapter.CashHistoryAdapter;
import in.moreyeahs.livspace.delivery.views.main.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static in.moreyeahs.livspace.delivery.utilities.Utils.hideProgressDialog;

public class CashManagmentHistory extends Fragment implements HistoryEditListener {

    CashCollectionHistoryBinding mBinding;
    private MainActivity activity;
    CashHistoryViewModel cashHistoryViewModel;
    CashHistoryAdapter cashHistoryAdapter;
    ArrayList<CurrencyCollectionDc> currencyCollectionDcs;
    int peopleId;
    int warehouseId;
    int CurrenctCollectionID;

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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.cash_collection_history, container, false);
/**
 *
 *init
 * **/

        initView();
        cashHistoryViewModel = ViewModelProviders.of(this).get(CashHistoryViewModel.class);

        cashHistoryViewModel.getcashHistory().observe(this, new Observer<ApiResponse>() {
            @Override
            public void onChanged(@Nullable ApiResponse apiResponse) {
                if (apiResponse != null) {
                    consumeResponse(apiResponse);
                }
            }
        });
        cashHistoryViewModel.getHistorydata().observe(this, new Observer<ApiResponse>() {
            @Override
            public void onChanged(@Nullable ApiResponse apiResponse) {
                if (apiResponse != null) {
                    historyRespose(apiResponse);
                }
            }


        });
        cashHistoryViewModel.hitCashHistorydata(peopleId, warehouseId);

        return mBinding.getRoot();
    }

    private void historyRespose(ApiResponse apiResponse) {
        switch (apiResponse.status) {
            case LOADING:
                mBinding.progressBid.setVisibility(View.VISIBLE);
                break;
            case SUCCESS:
                mBinding.progressBid.setVisibility(View.GONE);
                SuccessResponseofHistory(apiResponse.data);
                break;

            case ERROR:
                mBinding.progressBid.setVisibility(View.GONE);
                Utils.setToast(activity, getResources().getString(R.string.errorString));
                break;

            default:
                break;
        }


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

    private void renderSuccessResponse(JsonElement response) {

        if (Utils.isJSONValid(response.toString())) {
            if (!response.isJsonNull()) {
                Log.d("response=", response.toString());
                try {

                    JSONObject obj = new JSONObject(response.toString());
                    CurrencyHistoryModel currencyHistoryModel = new Gson().fromJson(obj.toString(), CurrencyHistoryModel.class);
                    if (currencyHistoryModel.getStatus()) {

                        cashHistoryAdapter = new CashHistoryAdapter(activity, currencyHistoryModel.getCurrencyCollectionSummaryDcs(), this);
                        mBinding.rvHistory.setAdapter(cashHistoryAdapter);


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(activity, getResources().getString(R.string.errorString), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(activity, response.toString(), Toast.LENGTH_SHORT).show();
        }


    }

    private void SuccessResponseofHistory(JsonElement response) {
        if (Utils.isJSONValid(response.toString())) {
            if (!response.isJsonNull()) {
                Log.d("response=", response.toString());
                try {

                    JSONObject obj = new JSONObject(response.toString());
                    CurrencyHistoryDataModel currencyHistoryDataModel = new Gson().fromJson(obj.toString(), CurrencyHistoryDataModel.class);
                    if (currencyHistoryDataModel.getStatus()) {
                        Gson gson = new Gson();
                        SharePrefs.setCashmanagmentSharedPreference(activity, SharePrefs.HISTORY_MODEL, gson.toJson(currencyHistoryDataModel.getCurrencyCollectionDc()));

                        SharePrefs.setCashmanagmentSharedPreference(activity, SharePrefs.CHEQUE_AMOUNT, gson.toJson(currencyHistoryDataModel.getCurrencyCollectionDc().getChequeCollections()));
                        SharePrefs.setCashmanagmentSharedPreference(activity, SharePrefs.CASH_AMOUNT_JSON, gson.toJson(currencyHistoryDataModel.getCurrencyCollectionDc().getCashCollections()));
                        SharePrefs.setCashmanagmentSharedPreference(activity, SharePrefs.ONLINE_AMOUNT, gson.toJson(currencyHistoryDataModel.getCurrencyCollectionDc().getOnlineCollections()));
                        activity.switchContentWithStack(new AssignmentCashManagment());
                    } else {
                        Toast.makeText(activity, currencyHistoryDataModel.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(activity, getResources().getString(R.string.errorString), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(activity, response.toString(), Toast.LENGTH_SHORT).show();
        }


    }

    private void initView() {
        currencyCollectionDcs = new ArrayList<>();
        peopleId = SharePrefs.getInstance(activity).getInt(SharePrefs.PEOPLE_ID);
        warehouseId = SharePrefs.getInstance(activity).getInt(SharePrefs.WAREHOUSE_ID);
        mBinding.rvHistory.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        mBinding.rvHistory.setHasFixedSize(true);
        mBinding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onBackPressed();
            }
        });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setActionBarConfiguration();
    }

    private void setActionBarConfiguration() {
        final DrawerLayout drawer = getActivity().findViewById(R.id.container);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.GONE);
        ((MainActivity) getActivity()).toggle.syncState();
    }

    @Override
    public void onResume() {
        super.onResume();
        SharePrefs.setCashmanagmentSharedPreference(activity, SharePrefs.ASSIGN_MODEL, null);
        SharePrefs.getInstance(activity).putString(SharePrefs.ASSIGN_ID, null);
        SharePrefs.setCashmanagmentSharedPreference(activity, SharePrefs.CASH_AMOUNT_JSON, null);
        SharePrefs.setCashmanagmentSharedPreference(activity, SharePrefs.CHEQUE_AMOUNT, null);
        SharePrefs.setCashmanagmentSharedPreference(activity, SharePrefs.ONLINE_AMOUNT, null);
        SharePrefs.getInstance(activity).putInt(SharePrefs.CASH_AMOUNT, 0);
    }

    @Override
    public void Editclicked(int ID) {
        cashHistoryViewModel.hitgetHistorydata(ID);
    }
}
