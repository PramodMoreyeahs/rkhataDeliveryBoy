package in.moreyeahs.livspace.delivery.views.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import in.moreyeahs.livspace.delivery.R;
import in.moreyeahs.livspace.delivery.databinding.FragmentPenddingBinding;
import in.moreyeahs.livspace.delivery.databinding.RejectResonPopupBinding;
import in.moreyeahs.livspace.delivery.listener.AcceptClickInterface;
import in.moreyeahs.livspace.delivery.model.AcceptModel;
import in.moreyeahs.livspace.delivery.model.PendingTaskModel;
import in.moreyeahs.livspace.delivery.utilities.ApiResponse;
import in.moreyeahs.livspace.delivery.utilities.CommonMethods;
import in.moreyeahs.livspace.delivery.utilities.Logger;
import in.moreyeahs.livspace.delivery.utilities.SharePrefs;
import in.moreyeahs.livspace.delivery.utilities.Utils;
import in.moreyeahs.livspace.delivery.viewmodels.PendingTaskViewModel;
import in.moreyeahs.livspace.delivery.views.adapter.PendingTaskAdapter;
import in.moreyeahs.livspace.delivery.views.main.MainActivity;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class PendingFragment extends Fragment implements AcceptClickInterface {
    private FragmentPenddingBinding mBinding;
    private RecyclerView rvPendingRackRV;
    private PendingTaskViewModel pendingTaskViewModel;
    private Dialog customDialog;
    private boolean ARFlag;


    public PendingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
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
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_pendding, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setData();
    }

    private void initView() {
        rvPendingRackRV = mBinding.rvPenddingTask;
        rvPendingRackRV.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        rvPendingRackRV.setHasFixedSize(true);
    }

    private void setData() {

        pendingTaskViewModel = ViewModelProviders.of(this).get(PendingTaskViewModel.class);
        mBinding.setPendingTaskViewModel(pendingTaskViewModel);
        mBinding.setLifecycleOwner(this);
        initView();
        pendingTaskViewModel.getPendingTaskData().observe(this, apiResponse -> consumeResponse(apiResponse));

        /***
         * Pending Task API call
         * **/
        if (!Utils.checkInternetConnection(getActivity())) {
            Utils.setToast(getActivity(), getResources().getString(R.string.network_error));
        } else {
            pendingTaskViewModel.pendingMyTaskAdi(SharePrefs.getInstance(getActivity()).getInt(SharePrefs.PEOPLE_ID));
        }
        /***
         * Accept response
         * **/

        pendingTaskViewModel.getAcceptMyTaskData().observe(this, value -> {
            mBinding.proRelatedItem.setVisibility(View.GONE);
            if (value) {
                if (ARFlag) {
                    Utils.setToast(getActivity(), getString(R.string.accept));
                } else {
                    Utils.setToast(getActivity(), getString(R.string.reject));
                }
                startActivity(new Intent(getActivity(), MainActivity.class));
                if (!Utils.checkInternetConnection(getActivity())) {
                    Utils.setToast(getActivity(), getResources().getString(R.string.network_error));
                } else {
                    pendingTaskViewModel.pendingMyTaskAdi(SharePrefs.getInstance(getActivity()).getInt(SharePrefs.PEOPLE_ID));
                }
            } else {
                Utils.setToast(getActivity(), getString(R.string.Error));
            }
        });
    }

    /*
     * method to handle response
     * */
    private void consumeResponse(ApiResponse apiResponse) {
        switch (apiResponse.status) {
            case LOADING:
                mBinding.proRelatedItem.setVisibility(View.VISIBLE);

                break;
            case SUCCESS:
                mBinding.proRelatedItem.setVisibility(View.GONE);

                renderSuccessResponse(apiResponse.data);
                break;

            case ERROR:
                mBinding.proRelatedItem.setVisibility(View.GONE);
                Utils.setToast(getActivity(), getResources().getString(R.string.errorString));
                break;

            default:
                break;
        }
    }

    /*
     * method to handle success response
     * */
    private void renderSuccessResponse(JsonElement response) {
        if (Utils.isJSONValid(response.toString())) {
            if (!response.isJsonNull()) {
                mBinding.proRelatedItem.setVisibility(View.GONE);

                Logger.e(CommonMethods.getTag(this), response.toString());
                try {
                    JSONObject obj = new JSONObject(response.toString());
                    PendingTaskModel pendingTaskModel = new Gson().fromJson(obj.toString(), PendingTaskModel.class);
                    if (pendingTaskModel.isStatus()) {
                        mBinding.rvPenddingTask.setVisibility(View.VISIBLE);
                        PendingTaskAdapter adapter = new PendingTaskAdapter(getActivity(), pendingTaskModel.getDI(), this);
                        rvPendingRackRV.setAdapter(adapter);
                    } else {
                        // Utils.setToast(getActivity(), getResources().getString(R.string.no_data_error));
                        mBinding.tvMyTask.setVisibility(View.VISIBLE);
                        mBinding.rvPenddingTask.setVisibility(View.GONE);
                    }

                } catch (JSONException e) {
                    Logger.e(CommonMethods.getTag(this), e.toString());
                }
            } else {
                Toast.makeText(getActivity(), getResources().getString(R.string.errorString), Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void acceptClicked(int deliveryIssuanceId, String arValue) {
        /***
         * Accept API call
         * **/
        ARFlag = true;
        if (!Utils.checkInternetConnection(getActivity())) {
            Utils.setToast(getActivity(), getResources().getString(R.string.network_error));
        } else {
            Utils.showProgressDialog(getActivity());
            AcceptModel acceptModel = new AcceptModel(deliveryIssuanceId, arValue, "");
            pendingTaskViewModel.AcceptPendingMyTaskAdi(acceptModel);
            mBinding.proRelatedItem.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void rejectClicked(int deliveryIssuanceId, String aTrue) {
        RejectPopup(deliveryIssuanceId, aTrue);
    }

    /**
     * Reject popup
     * @param deliveryIssuanceId
     * @param aTrue
     */
    private void RejectPopup(int deliveryIssuanceId, String aTrue) {
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
    }
}