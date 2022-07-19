package in.moreyeahs.livspace.delivery.views.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import in.moreyeahs.livspace.delivery.R;
import in.moreyeahs.livspace.delivery.databinding.FragmentCashCollectionBinding;
import in.moreyeahs.livspace.delivery.databinding.FragmentPenddingBinding;
import in.moreyeahs.livspace.delivery.databinding.RejectResonPopupBinding;
import in.moreyeahs.livspace.delivery.listener.AcceptClickInterface;
import in.moreyeahs.livspace.delivery.model.AcceptModel;
import in.moreyeahs.livspace.delivery.model.CashCollectionResponse;
import in.moreyeahs.livspace.delivery.model.PendingTaskModel;
import in.moreyeahs.livspace.delivery.utilities.ApiResponse;
import in.moreyeahs.livspace.delivery.utilities.CommonMethods;
import in.moreyeahs.livspace.delivery.utilities.Logger;
import in.moreyeahs.livspace.delivery.utilities.SharePrefs;
import in.moreyeahs.livspace.delivery.utilities.Utils;
import in.moreyeahs.livspace.delivery.viewmodels.PendingTaskViewModel;
import in.moreyeahs.livspace.delivery.views.adapter.CashCollectionAdapter;
import in.moreyeahs.livspace.delivery.views.adapter.PendingTaskAdapter;
import in.moreyeahs.livspace.delivery.views.main.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class CashCollectionFragment extends Fragment implements AcceptClickInterface {
    private FragmentCashCollectionBinding mBinding;
    private RecyclerView rvPendingRackRV;
    private PendingTaskViewModel pendingTaskViewModel;
    private Dialog customDialog;
    private boolean ARFlag;
    Boolean apiType=false;
    double totoalAmount=0;
    TextView text;
    int page = 1, limit = 5;
    ArrayList<CashCollectionResponse.ListData> cashList= new ArrayList<>();

    public CashCollectionFragment() {
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
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_cash_collection, container, false);
        return mBinding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setData();
        setActionBarConfiguration();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initView() {
        rvPendingRackRV = mBinding.rvPenddingTask;
        rvPendingRackRV.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        rvPendingRackRV.setHasFixedSize(true);
        getCashList();

        mBinding.nsvAddress.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    // in this method we are incrementing page number,
                    // making progress bar visible and calling get data method.
                    limit++;
                    mBinding.progressBottom.setVisibility(View.VISIBLE);
                  getCashList();
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("FragmentLiveDataObserve")
    private void setData() {
         apiType = getArguments().getBoolean("type");
        pendingTaskViewModel = ViewModelProviders.of(this).get(PendingTaskViewModel.class);
        mBinding.setPendingTaskViewModel(pendingTaskViewModel);
        mBinding.setLifecycleOwner(this);

        pendingTaskViewModel.getCashData().observe(this, apiResponse -> consumeResponse(apiResponse));
        initView();
        /***
         * Pending Task API call
         * **/

        /***
         * Accept response
         * **/

     /*   pendingTaskViewModel.getAcceptMyTaskData().observe(this, value -> {
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
                    pendingTaskViewModel.cashCollection(SharePrefs.getInstance(getActivity()).getString(SharePrefs.MOBILE));
                }
            } else {
                Utils.setToast(getActivity(), getString(R.string.Error));
            }
        });*/
    }

    /*
     * method to handle response
     * */

    public void getCashList(){
        if (!Utils.checkInternetConnection(getActivity())) {
            Utils.setToast(getActivity(), getResources().getString(R.string.network_error));
        } else {

            if(apiType)
            {
                pendingTaskViewModel.cashCollection(SharePrefs.getInstance(getActivity()).getString(SharePrefs.MOBILE),page, limit);
            }
            else
            {
                pendingTaskViewModel.creditCollection(SharePrefs.getInstance(getActivity()).getString(SharePrefs.MOBILE),page, limit);
            }


        }
    }
    private void consumeResponse(ApiResponse apiResponse) {
        switch (apiResponse.status) {
            case LOADING:
                mBinding.proRelatedItem.setVisibility(View.VISIBLE);

                break;
            case SUCCESS:
                mBinding.proRelatedItem.setVisibility(View.GONE);
                mBinding.progressBottom.setVisibility(View.GONE);
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
                    CashCollectionResponse pendingTaskModel = new Gson().fromJson(obj.toString(), CashCollectionResponse.class);
                    totoalAmount=pendingTaskModel.getTotalCollection();
                    cashList.clear();
                    cashList.addAll(pendingTaskModel.getData());
                    CashCollectionAdapter adapter = new CashCollectionAdapter(getActivity(),cashList, this);
                    rvPendingRackRV.setAdapter(adapter);
                    Log.e("CASH list Size *******",""+cashList.size());
                  /*  for (int i=0;i<cashList.size();i++)
                    {
                        totoalAmount=totoalAmount+cashList.get(i).getAmount();
                    }*/
                    text.setTextColor(apiType?Color.parseColor("#38a561"):
                            Color.parseColor("#FF0000"));
                    if(apiType)
                    {
                        text.setText("₹"+totoalAmount);
                    }
                    else
                    {
                        text.setText("₹ - "+totoalAmount);
                    }

                } catch (Exception e) {
                    Logger.e(CommonMethods.getTag(this), e.toString());
                }
            } else {
                Toast.makeText(getActivity(), getResources().getString(R.string.errorString), Toast.LENGTH_SHORT).show();
            }
            Log.e("CASH list Size *******",""+cashList.size());
        } else {
            Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();

        }
        Log.e("CASH list Size *******",""+cashList.size());
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

    private void setActionBarConfiguration() {
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        //actionBar.setLogo(R.drawable.ic_action_bar_logo);
        final DrawerLayout drawer = getActivity().findViewById(R.id.container);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
        TextView tittleTextView = getActivity().findViewById(R.id.toolbar_title);
        TextView timer = getActivity().findViewById(R.id.tv_timer);
       text = getActivity().findViewById(R.id.tv_amount);
        TextView tv_assignmentid = getActivity().findViewById(R.id.assignmentid);
        RelativeLayout rvTiltleLayout = getActivity().findViewById(R.id.title_layout);
        tv_assignmentid.setVisibility(View.GONE);
        text.setVisibility(View.VISIBLE);
        timer.setVisibility(View.GONE);
        text.setText("0.00");


        tittleTextView.setVisibility(View.VISIBLE);

        tittleTextView.setText(apiType?getActivity().getString(R.string.my_cash_collection):
                getActivity().getString(R.string.my_credit_collection));
        ImageView menu = getActivity().findViewById(R.id.drawer_menu);
        menu.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_menu_three_dot));
        toolbar.setNavigationOnClickListener(v -> {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                drawer.openDrawer(GravityCompat.START);
            }
        });
        ((MainActivity) getActivity()).toggle.syncState();
    }
}