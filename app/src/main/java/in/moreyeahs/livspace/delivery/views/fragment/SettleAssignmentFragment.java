package in.moreyeahs.livspace.delivery.views.fragment;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import in.moreyeahs.livspace.delivery.R;
import in.moreyeahs.livspace.delivery.databinding.SettleAssignmentBinding;
import in.moreyeahs.livspace.delivery.model.AssignmentIdDataModel;
import in.moreyeahs.livspace.delivery.utilities.ApiResponse;
import in.moreyeahs.livspace.delivery.utilities.SharePrefs;
import in.moreyeahs.livspace.delivery.utilities.Utils;
import in.moreyeahs.livspace.delivery.viewmodels.SettleAssignmentViewModel;
import in.moreyeahs.livspace.delivery.views.main.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static in.moreyeahs.livspace.delivery.utilities.Utils.hideProgressDialog;

public class SettleAssignmentFragment extends Fragment {


    SettleAssignmentBinding mBinding;
    private AssignmentIdDataModel assignmentIdDataModel;
    private ArrayList<String> AssignmentIDArray = new ArrayList<>();
    private int peopleId;
    private int warehouseId;
    private String selectedAssignmentid;
    private MainActivity activity;

    public SettleAssignmentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (MainActivity) context;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mBinding = DataBindingUtil.inflate(inflater, R.layout.settle_assignment, container, false);
        /**
         *
         *init
         * **/
        initView();
        if (!Utils.checkInternetConnection(activity)) {
            Utils.setToast(activity, getResources().getString(R.string.network_error));
        } else {
            SettleAssignmentViewModel settleAssignmentViewModel = ViewModelProviders.of(this).get(SettleAssignmentViewModel.class);
            mBinding.setLifecycleOwner(this);
            settleAssignmentViewModel.getAssignmentIDResponse().observe(this, new Observer<ApiResponse>() {
                @Override
                public void onChanged(@Nullable ApiResponse apiResponse) {
                    if (apiResponse != null) {
                        consumeResponse(apiResponse);
                    }
                }
            });
            mBinding.proRelatedItem.setVisibility(View.VISIBLE);
            settleAssignmentViewModel.hitAssignmentId(peopleId, warehouseId);
            mBinding.spAssignmentList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        selectedAssignmentid = AssignmentIDArray.get(position);
                        if (position != 0)
                        {
                            System.out.println("selectedAssignmentid" + selectedAssignmentid);
                            Gson gson = new Gson();
                            SharePrefs.setCashmanagmentSharedPreference(activity, SharePrefs.ASSIGN_MODEL, gson.toJson(assignmentIdDataModel.getDeliveryIssuanceDcs()));
                            SharePrefs.getInstance(activity).putString(SharePrefs.ASSIGN_ID, selectedAssignmentid);
                            SharePrefs.getInstance(activity).putBoolean(SharePrefs.FLAG, true);
                            Fragment fragment = new AssignmentCashManagment();
                            activity.switchContentWithStack(fragment);
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }


                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        return mBinding.getRoot();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        SharePrefs.setCashmanagmentSharedPreference(activity, SharePrefs.ASSIGN_MODEL, null);
        SharePrefs.getInstance(activity).putString(SharePrefs.ASSIGN_ID, null);
        SharePrefs.setCashmanagmentSharedPreference(activity, SharePrefs.CASH_AMOUNT_JSON, null);
        SharePrefs.setCashmanagmentSharedPreference(activity, SharePrefs.EDIT_AMOUNT_JSON, null);
        SharePrefs.setCashmanagmentSharedPreference(activity, SharePrefs.CHEQUE_AMOUNT, null);
        SharePrefs.setCashmanagmentSharedPreference(activity, SharePrefs.ONLINE_AMOUNT, null);
        SharePrefs.getInstance(activity).putInt(SharePrefs.CASH_AMOUNT, 0);
        SharePrefs.getInstance(activity).putBoolean(SharePrefs.FLAG, true);
        SharePrefs.getInstance(activity).putBoolean(SharePrefs.UPDATE_DATA, false);


    }

    private void consumeResponse(ApiResponse apiResponse)
    {
        switch (apiResponse.status)
        {
            case LOADING:
                mBinding.proRelatedItem.setVisibility(View.VISIBLE);
                break;
            case SUCCESS:
                mBinding.proRelatedItem.setVisibility(View.GONE);
                renderSuccessResponse(apiResponse.data);
                break;

            case ERROR:
                mBinding.proRelatedItem.setVisibility(View.GONE);
                Utils.setToast(activity, getResources().getString(R.string.errorString));
                break;

            default:
                break;
        }
    }

    private void renderSuccessResponse(JsonElement response)
    {
        if (Utils.isJSONValid(response.toString())) {
            if (!response.isJsonNull()) {
                mBinding.proRelatedItem.setVisibility(View.GONE);
                AssignmentIDArray.clear();
                Log.d("response=", response.toString());
                try {
                    JSONObject obj = new JSONObject(response.toString());
                    assignmentIdDataModel = new Gson().fromJson(obj.toString(), AssignmentIdDataModel.class);
                    if (assignmentIdDataModel.getStatus()) {
                        AssignmentIDArray.add(getContext().getResources().getString(R.string.select_assignment));
                        for (int i = 0; i < assignmentIdDataModel.getDeliveryIssuanceDcs().size(); i++)
                        {
                            String AssignmentID = String.valueOf(assignmentIdDataModel.getDeliveryIssuanceDcs().get(i).getDeliveryIssuanceId());
                            AssignmentIDArray.add(AssignmentID);

                        }

                        ArrayAdapter adapter = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, AssignmentIDArray);
                        mBinding.spAssignmentList.setAdapter(adapter);

                    } else {

                        Toast.makeText(activity, assignmentIdDataModel.getMessage(), Toast.LENGTH_SHORT).show();

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


    private void initView()
    {
        peopleId = SharePrefs.getInstance(activity).getInt(SharePrefs.PEOPLE_ID);
        warehouseId = SharePrefs.getInstance(activity).getInt(SharePrefs.WAREHOUSE_ID);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        setActionBarConfiguration();

    }

    private void setActionBarConfiguration() {
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        //actionBar.setLogo(R.drawable.ic_action_bar_logo);
        final DrawerLayout drawer = getActivity().findViewById(R.id.container);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
        TextView tittleTextView = getActivity().findViewById(R.id.toolbar_title);
        TextView starttimer = getActivity().findViewById(R.id.start_timer);
        TextView tv_assignmentid = getActivity().findViewById(R.id.assignmentid);
        RelativeLayout rvTiltleLayout = getActivity().findViewById(R.id.title_layout);
        tv_assignmentid.setVisibility(View.GONE);
        starttimer.setText(R.string.history);
        TextView timer = getActivity().findViewById(R.id.tv_timer);
        timer.setVisibility(View.GONE);
        starttimer.setVisibility(View.VISIBLE);
        tittleTextView.setVisibility(View.VISIBLE);
        tittleTextView.setText(R.string.settle_assignment);
        ImageView menu = (ImageView) getActivity().findViewById(R.id.drawer_menu);
        menu.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_menu_three_dot));
        starttimer.setEnabled(true);


        starttimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.switchContentWithStack(new CashManagmentHistory());
                SharePrefs.getInstance(activity).putBoolean(SharePrefs.FLAG, false);

            }
        });
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
