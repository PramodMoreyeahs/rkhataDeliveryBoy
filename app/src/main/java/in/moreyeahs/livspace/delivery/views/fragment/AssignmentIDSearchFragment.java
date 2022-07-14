package in.moreyeahs.livspace.delivery.views.fragment;

import android.app.Dialog;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import in.moreyeahs.livspace.delivery.R;
import in.moreyeahs.livspace.delivery.databinding.AssignmentSearchBinding;
import in.moreyeahs.livspace.delivery.databinding.RejectResonPopupBinding;
import in.moreyeahs.livspace.delivery.listener.AcceptClickInterface;
import in.moreyeahs.livspace.delivery.model.AcceptModel;
import in.moreyeahs.livspace.delivery.model.SearchAssiIDResponseModel;
import in.moreyeahs.livspace.delivery.utilities.ApiResponse;
import in.moreyeahs.livspace.delivery.utilities.CommonMethods;
import in.moreyeahs.livspace.delivery.utilities.Logger;
import in.moreyeahs.livspace.delivery.utilities.SharePrefs;
import in.moreyeahs.livspace.delivery.utilities.Utils;
import in.moreyeahs.livspace.delivery.viewmodels.AssignmentSearchViewModel;
import in.moreyeahs.livspace.delivery.viewmodels.PendingTaskViewModel;
import in.moreyeahs.livspace.delivery.views.adapter.AssignmentIDSearchAdapter;
import in.moreyeahs.livspace.delivery.views.main.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class AssignmentIDSearchFragment extends Fragment implements AcceptClickInterface {

    private Context context;
    AssignmentSearchBinding mBinding;
    AssignmentSearchViewModel assignmentSearchViewModel;
    private String searchString = "";
    RecyclerView rvassginmentSearch;
    boolean ARFlag;
    PendingTaskViewModel pendingTaskViewModel;
    Dialog customDialog;
    boolean Tasklist;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
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

        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.assignment_search, container, false);

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        setData();
        assignmentSearchViewModel = ViewModelProviders.of(this).get(AssignmentSearchViewModel.class);
        mBinding.searchTxt.setCursorVisible(true);
        mBinding.searchTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    searchString = editable.toString().trim();
                    // Search API call
                    int peopleId = SharePrefs.getInstance(getActivity()).getInt(SharePrefs.PEOPLE_ID);
                    if (!Utils.checkInternetConnection(getActivity()))
                    {
                        Utils.setToast(getActivity(), getResources().getString(R.string.network_error));
                    }
                    else
                        {
                        assignmentSearchViewModel.hitSearchApi(searchString, peopleId);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        assignmentSearchViewModel.searchResponse().observe(this, new Observer<ApiResponse>() {
            @Override
            public void onChanged(@Nullable ApiResponse apiResponse) {

                consumeResponse(apiResponse);
            }
        });
    }

    private void setData() {
        try {

            Bundle bundle = this.getArguments();
            if (bundle != null) {
                Tasklist = bundle.getBoolean("Task");
            }
            pendingTaskViewModel.getAcceptMyTaskData().observe(this, new Observer<Boolean>() {
                @Override
                public void onChanged(@Nullable Boolean value) {
                    Utils.hideProgressDialog(getActivity());
                    if (value) {
                        if (ARFlag) {
                            Utils.setToast(getActivity(), getString(R.string.accept));
                        } else {
                            Utils.setToast(getActivity(), getString(R.string.reject));
                        }
                        startActivity(new Intent(getActivity(), MainActivity.class));
                        int peopleId = SharePrefs.getInstance(getActivity()).getInt(SharePrefs.PEOPLE_ID);

                        if (!Utils.checkInternetConnection(getActivity())) {
                            Utils.setToast(getActivity(), getResources().getString(R.string.network_error));
                        } else {
                            assignmentSearchViewModel.hitSearchApi(searchString, peopleId);
                        }
                    } else {
                        Utils.setToast(getActivity(), getString(R.string.Error));
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        pendingTaskViewModel = ViewModelProviders.of(this).get(PendingTaskViewModel.class);
        rvassginmentSearch = mBinding.rvAssignmentsearch;
        rvassginmentSearch.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        rvassginmentSearch.setHasFixedSize(true);
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
                //Utils.setToast(getActivity(), getResources().getString(R.string.errorString));
                break;

            default:
                break;
        }

    }

    private void renderSuccessResponse(JsonElement response) {
        try {
            if (Utils.isJSONValid(response.toString())) {
                if (!response.isJsonNull()) {
                    Logger.e(CommonMethods.getTag(this), response.toString());
                    try {
                        JSONObject obj = new JSONObject(response.toString());
                        SearchAssiIDResponseModel searchResponseModel = new Gson().fromJson(obj.toString(), SearchAssiIDResponseModel.class);
                        if (searchResponseModel.isStatus()) {
                            if (searchResponseModel != null) {
                                rvassginmentSearch.setVisibility(View.VISIBLE);
                                AssignmentIDSearchAdapter adapter = new AssignmentIDSearchAdapter(getActivity(), searchResponseModel.getDI(), this);
                                rvassginmentSearch.setAdapter(adapter);
                            }
                        } else {
                            rvassginmentSearch.setVisibility(View.GONE);
                            // Utils.setToast(getActivity(), searchResponseModel.getMessage());
                        }

                    } catch (JSONException e) {
                        Logger.e(CommonMethods.getTag(this), e.toString());
                    }
                } else {
                    //  Toast.makeText(getActivity(), getResources().getString(R.string.errorString), Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();

            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void acceptClicked(int deliveryIssuanceId, String aTrue) {

        if (!Utils.checkInternetConnection(context)) {
            Utils.setToast(context, context.getResources().getString(R.string.network_error));
        } else {
            if (!Tasklist) {
                ARFlag = true;
                if (!Utils.checkInternetConnection(getActivity())) {
                    Utils.setToast(getActivity(), getResources().getString(R.string.network_error));
                } else {
                    Utils.showProgressDialog(getActivity());
                    AcceptModel acceptModel = new AcceptModel(deliveryIssuanceId, aTrue, "");
                    pendingTaskViewModel.AcceptPendingMyTaskAdi(acceptModel);
                }
            } else {
                Utils.setToast(getActivity(), getString(R.string.in_complete_task));

            }
        }

    }

    @Override
    public void rejectClicked(int deliveryIssuanceId, String aTrue) {
        if (!Utils.checkInternetConnection(context)) {
            Utils.setToast(context, context.getResources().getString(R.string.network_error));
        } else {
            if (!Tasklist) {
                RejectPopup(deliveryIssuanceId, aTrue);

            } else {
                Utils.setToast(getActivity(), getString(R.string.in_complete_task));

            }
        }

    }

    private void RejectPopup(int deliveryIssuanceId, String aTrue) {
        try {
            LayoutInflater inflater = getLayoutInflater();
            RejectResonPopupBinding rejectResonPopupBinding = DataBindingUtil.inflate(inflater, R.layout.reject_reson_popup, null, false);
            //  final View mView = inflater.inflate(R.layout.reject_reson_popup, null);
            customDialog = new Dialog(getActivity(), R.style.CustomDialog);
            customDialog.setContentView(rejectResonPopupBinding.getRoot());
            Button submit = rejectResonPopupBinding.submit;
            ImageView dissmiss = rejectResonPopupBinding.dissmiss;
            Spinner spReason = rejectResonPopupBinding.spReason;
            ArrayAdapter statusAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.reason, android.R.layout.simple_spinner_dropdown_item);
            spReason.setAdapter(statusAdapter);
            submit.setOnClickListener(v -> {
                String sReason = spReason.getSelectedItem().toString();
                /***
                 * Accept API call
                 * **/
                if (sReason.equalsIgnoreCase("Select Reason")) {
                    Utils.setToast(getActivity(), getResources().getString(R.string.plz_select_reason));
                } else {
                    customDialog.dismiss();
                    ARFlag = false;
                    if (!Utils.checkInternetConnection(getActivity())) {
                        Utils.setToast(getActivity(), getResources().getString(R.string.network_error));
                    } else {
                        Utils.showProgressDialog(getActivity());
                        AcceptModel acceptModel = new AcceptModel(deliveryIssuanceId, aTrue, sReason);
                        pendingTaskViewModel.AcceptPendingMyTaskAdi(acceptModel);
                    }
                }

            });
            dissmiss.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    customDialog.dismiss();
                }
            });
            customDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
