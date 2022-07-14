package in.moreyeahs.livspace.delivery.views.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import in.moreyeahs.livspace.delivery.R;
import in.moreyeahs.livspace.delivery.databinding.AssignmentcashshowingBinding;
import in.moreyeahs.livspace.delivery.model.AssignmentID;
import in.moreyeahs.livspace.delivery.model.AssignmentIdDataModel;
import in.moreyeahs.livspace.delivery.model.CurrencyPostDataModel;
import in.moreyeahs.livspace.delivery.model.DeliveryIssuanceDetail;
import in.moreyeahs.livspace.delivery.model.PostCashCollection;
import in.moreyeahs.livspace.delivery.model.PostChequeCollectionModel;
import in.moreyeahs.livspace.delivery.model.PostOnlineCollectionModel;
import in.moreyeahs.livspace.delivery.utilities.ApiResponse;
import in.moreyeahs.livspace.delivery.utilities.SharePrefs;
import in.moreyeahs.livspace.delivery.utilities.Utils;
import in.moreyeahs.livspace.delivery.viewmodels.AssignmentCashViewModel;
import in.moreyeahs.livspace.delivery.views.main.ChequeCollectionActivity;
import in.moreyeahs.livspace.delivery.views.main.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static in.moreyeahs.livspace.delivery.R.drawable;
import static in.moreyeahs.livspace.delivery.R.id;
import static in.moreyeahs.livspace.delivery.R.string;
import static in.moreyeahs.livspace.delivery.utilities.Utils.hideProgressDialog;

public class AssignmentCashManagment extends Fragment implements View.OnClickListener {

    private MainActivity activity;
    AssignmentcashshowingBinding mBinding;
    AssignmentCashViewModel assignmentCashViewModel;
    private ArrayList<Integer> AssignmentIDArray = new ArrayList<>();
    private ArrayList<Integer> OrderIDArray = new ArrayList<>();
    private AssignmentIdDataModel assignmentIdDataModel;

    private int peopleId;
    private int warehouseId;
    String selectedAssignmentId;
    private double TotalAmount;
    private Bundle bundle;
    double totalCollectedAmount;
    double totalUsedCheckAmnt, totalusedCashAmnt, totalUsedOnlineAmnt;
    double cashAmount, ChequeAmount, OnlineAmount;
    ArrayList<PostChequeCollectionModel> postChequeCollectionModelArrayList;
    ArrayList<PostCashCollection> postCashCollectionArrayList;
    ArrayList<PostOnlineCollectionModel> postOnlineCollectionModelArrayList;
    int ID = 0;
    public Dialog customDialog;
    boolean flag;
    boolean isRefresh = false;

    public AssignmentCashManagment() {

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (MainActivity) context;

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.assignmentcashshowing, container, false);
        /**
         *
         *init
         * **/
        initView();
        assignmentCashViewModel = ViewModelProviders.of(this).get(AssignmentCashViewModel.class);
        mBinding.setLifecycleOwner(this);
        assignmentCashViewModel.getPostCurrencyData().observe(this, new Observer<ApiResponse>() {
            @Override
            public void onChanged(@Nullable ApiResponse apiResponse) {
                if (apiResponse != null) {
                    ResponseofPostData(apiResponse);
                }
            }
        });
        flag = SharePrefs.getInstance(activity).getBoolean(SharePrefs.FLAG);
        if (flag) {
            String Assignmodel = SharePrefs.getCashmanagmentSharedPreferences(activity, SharePrefs.ASSIGN_MODEL);
            selectedAssignmentId = SharePrefs.getInstance(activity).getString(SharePrefs.ASSIGN_ID);
            ArrayList<AssignmentID> DeliveryIssuanceDcs = new Gson().fromJson(Assignmodel, new TypeToken<ArrayList<AssignmentID>>() {
            }.getType());
            mBinding.assignId.setText(selectedAssignmentId);
            setData(selectedAssignmentId, DeliveryIssuanceDcs);

        } else {
            String historyModel = SharePrefs.getCashmanagmentSharedPreferences(activity, SharePrefs.HISTORY_MODEL);
            setHistoryData(historyModel);
        }

        RetrieveData();
        return mBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

        if(isRefresh){
            isRefresh=false;
           /* if (flag) {
                String Assignmodel = SharePrefs.getCashmanagmentSharedPreferences(activity, SharePrefs.ASSIGN_MODEL);
                selectedAssignmentId = SharePrefs.getInstance(activity).getString(SharePrefs.ASSIGN_ID);
                ArrayList<AssignmentID> DeliveryIssuanceDcs = new Gson().fromJson(Assignmodel, new TypeToken<ArrayList<AssignmentID>>() {
                }.getType());
                mBinding.assignId.setText(selectedAssignmentId);
                setData(selectedAssignmentId, DeliveryIssuanceDcs);

            } else {
                String historyModel = SharePrefs.getCashmanagmentSharedPreferences(activity, SharePrefs.HISTORY_MODEL);
                setHistoryData(historyModel);
            }*/

            RetrieveData();
        }
    }

    private void initView() {
        try {
            totalUsedCheckAmnt = 0.0;
            totalusedCashAmnt = 0.0;
            totalUsedOnlineAmnt = 0.0;
            cashAmount = 0.0;
            ChequeAmount = 0.0;
            OnlineAmount = 0.0;
            totalCollectedAmount = 0.0;
            Utils utils = new Utils(activity);
            peopleId = SharePrefs.getInstance(activity).getInt(SharePrefs.PEOPLE_ID);
            warehouseId = SharePrefs.getInstance(activity).getInt(SharePrefs.WAREHOUSE_ID);
            bundle = new Bundle();
            mBinding.cashLayout.setOnClickListener(this);
            mBinding.chequeLayout.setOnClickListener(this);
            mBinding.paymentButton.setOnClickListener(this);
            mBinding.back.setOnClickListener(this);
            mBinding.onlineLayout.setOnClickListener(this);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setHistoryData(String historyModel) {
        try {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<PostChequeCollectionModel>>() {
            }.getType();
            Type type1 = new TypeToken<ArrayList<PostCashCollection>>() {
            }.getType();
            Type type2 = new TypeToken<ArrayList<PostOnlineCollectionModel>>() {
            }.getType();
            JSONObject object = new JSONObject(historyModel);

            String Cashamount = SharePrefs.getCashmanagmentSharedPreferences(activity, SharePrefs.CASH_AMOUNT_JSON);
            String CheckAmount = SharePrefs.getCashmanagmentSharedPreferences(activity, SharePrefs.CHEQUE_AMOUNT);
            String onlineAmount = SharePrefs.getCashmanagmentSharedPreferences(activity, SharePrefs.ONLINE_AMOUNT);

            postChequeCollectionModelArrayList = gson.fromJson(CheckAmount, type);
            postCashCollectionArrayList = gson.fromJson(Cashamount, type1);
            postOnlineCollectionModelArrayList = gson.fromJson(onlineAmount, type2);

            if (postChequeCollectionModelArrayList.size() > 0 && postChequeCollectionModelArrayList != null) {
                for (int i = 0; i < postChequeCollectionModelArrayList.size(); i++) {
                    String Orderid = postChequeCollectionModelArrayList.get(i).getOrderid();
                    OrderIDArray.add(Integer.valueOf(Orderid));
                }
            }


            mBinding.assignId.setText(String.valueOf(object.get("Deliveryissueid")));
            TotalAmount = object.getDouble("TotalDeliveryissueAmt");
            selectedAssignmentId = String.valueOf(object.get("Deliveryissueid"));
            ID = object.getInt("Id");
            cashAmount = object.getDouble("TotalCollectionCashAmt");
            ChequeAmount = object.getDouble("TotalCollectionCheckAmt");
            OnlineAmount = object.getDouble("TotalCollectionOnlineAmt");

            totalusedCashAmnt = object.getDouble("TotalCashAmt");
            totalUsedCheckAmnt = object.getDouble("TotalCheckAmt");
            totalUsedOnlineAmnt = object.getDouble("TotalOnlineAmt");


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setData(String assignmentID, ArrayList<AssignmentID> assignmentIdDataModel) {

        OrderIDArray.clear();
        try {
            for (int i = 0; i < assignmentIdDataModel.size(); i++) {
                String AssignmentID = String.valueOf(assignmentIdDataModel.get(i).getDeliveryIssuanceId());
                if (AssignmentID.equalsIgnoreCase(assignmentID)) {
                    ArrayList<DeliveryIssuanceDetail> deliveryIssuanceDetailsList = assignmentIdDataModel.get(i).getDeliveryIssuanceDetailDcs();
                    for (int j = 0; j < deliveryIssuanceDetailsList.size(); j++) {
                        for (int k = 0; k < deliveryIssuanceDetailsList.get(j).getPaymentDetails().size(); k++) {
                            if (deliveryIssuanceDetailsList.get(j).getPaymentDetails().get(k).getPaymentFrom().equalsIgnoreCase("Cash")) {
                                cashAmount = cashAmount + deliveryIssuanceDetailsList.get(j).getPaymentDetails().get(k).getAmount();
                            }
                            if (deliveryIssuanceDetailsList.get(j).getPaymentDetails().get(k).getPaymentFrom().equalsIgnoreCase("Cheque")) {
                                int Orderid = deliveryIssuanceDetailsList.get(j).getOrderId();
                                OrderIDArray.add(Orderid);
                                ChequeAmount = ChequeAmount + deliveryIssuanceDetailsList.get(j).getPaymentDetails().get(k).getAmount();
                            }
                            if (!deliveryIssuanceDetailsList.get(j).getPaymentDetails().get(k).getPaymentFrom().equalsIgnoreCase("Cheque") && !deliveryIssuanceDetailsList.get(j).getPaymentDetails().get(k).getPaymentFrom().equalsIgnoreCase("Cash")) {
                                OnlineAmount = OnlineAmount + deliveryIssuanceDetailsList.get(j).getPaymentDetails().get(k).getAmount();
                            }
                        }
                        /*if (deliveryIssuanceDetailsList.get(j).getCheckAmount() != 0) {
                            int Orderid = deliveryIssuanceDetailsList.get(j).getOrderId();
                            OrderIDArray.add(Orderid);
                        }
*/
                    }


                }

            }
            TotalAmount = cashAmount + ChequeAmount + OnlineAmount;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.cash_layout:
                Fragment fragment;
                if (cashAmount > 0) {
                    fragment = new PoAssignmentFragment();
                    bundle.putDouble("cashAmount", cashAmount);
                    fragment.setArguments(bundle);
                    activity.switchContentWithStack(fragment);
                } else {
                    Toast.makeText(activity, R.string.cash_0, Toast.LENGTH_SHORT).show();

                }

                break;

            case R.id.cheque_layout:
                if (OrderIDArray.size() > 0) {
                    isRefresh=true;
                  /*  fragment = new ChequeCollectionFragment();
                    fragment.setArguments(bundle);
                    activity.switchContentWithStack(fragment);*/
                    Intent intent = new Intent(activity, ChequeCollectionActivity.class);
                    startActivity(intent);

                } else {
                    Toast.makeText(activity, R.string.Cheque_0, Toast.LENGTH_SHORT).show();

                }
                break;

            case R.id.payment_button:

                if (cashAmount > 0 && totalusedCashAmnt == 0.0)
                {
                    Toast.makeText(activity, "Please Verify Cash Amount", Toast.LENGTH_SHORT).show();
                }
                else  if (ChequeAmount > 0 && totalUsedCheckAmnt == 0.0) {
                    Toast.makeText(activity, "Please Verify Cheque Amount", Toast.LENGTH_SHORT).show();

                } else if (OnlineAmount > 0 && totalUsedOnlineAmnt == 0.0) {
                    Toast.makeText(activity, "Please Verify Online Amount", Toast.LENGTH_SHORT).show();

                }
                else if (TotalAmount !=totalCollectedAmount) {
                    Toast.makeText(activity, "Submitted Amount should be equals to "+TotalAmount, Toast.LENGTH_SHORT).show();

                }
                else {
                    SubmitPopup();
                }

                break;

            case R.id.back:
                activity.onBackPressed();
                break;

            case R.id.online_layout:
                if (OnlineAmount > 0) {
                    fragment = new OnlinePaymentFragment();
                    bundle.putDouble("OnlineAmount", OnlineAmount);
                    fragment.setArguments(bundle);
                    activity.switchContentWithStack(fragment);
                } else {
                    Toast.makeText(activity, string.online_0, Toast.LENGTH_SHORT).show();

                }


                break;

            default:
                break;
        }
    }

    private void SubmitPopup() {
        LayoutInflater inflater = getLayoutInflater();
        if (activity != null && inflater != null) {
            final View mView = inflater.inflate(R.layout.confirmation_popup, null);
            customDialog = new Dialog(activity, R.style.CustomDialog);
            customDialog.setContentView(mView);
            TextView okBtn = mView.findViewById(R.id.btn_yes);
            TextView cancelBtn = mView.findViewById(R.id.btn_no);
            TextView textMesaage = mView.findViewById(R.id.tv_txt);
            textMesaage.setText("Please Confirm to Submit ");
            okBtn.setOnClickListener(v -> {
                customDialog.dismiss();
                if (postData() != null) {
                    mBinding.proRelatedItem.setVisibility(View.VISIBLE);
                    assignmentCashViewModel.hitcurrencyPostData(postData());

                } else {
                    Toast.makeText(activity, "No  Data", Toast.LENGTH_SHORT).show();
                }
            });
            cancelBtn.setOnClickListener(v -> customDialog.dismiss());
            customDialog.show();
        }
    }

    public CurrencyPostDataModel postData() {
        CurrencyPostDataModel currencyPostDataModel = null;

        currencyPostDataModel = new CurrencyPostDataModel(postOnlineCollectionModelArrayList == null ? new ArrayList<PostOnlineCollectionModel>() : postOnlineCollectionModelArrayList, postChequeCollectionModelArrayList == null ? new ArrayList<PostChequeCollectionModel>() : postChequeCollectionModelArrayList, postCashCollectionArrayList == null ? new ArrayList<PostCashCollection>() : postCashCollectionArrayList, peopleId, TotalAmount, totalUsedCheckAmnt, totalUsedOnlineAmnt, totalusedCashAmnt, selectedAssignmentId, peopleId, warehouseId, ID);
        return currencyPostDataModel;
        // return totalCollectedAmount > 0 ? currencyPostDataModel : null;
    }

    public void RetrieveData() {
        try {
            //setData
            if (flag) {
                totalUsedCheckAmnt = 0.0;
                totalusedCashAmnt = 0.0;
                totalUsedOnlineAmnt = 0.0;
                mBinding.assignAmnt.setText(String.format("₹ %s", String.valueOf(TotalAmount)));
                mBinding.etCaseAmount.setText(String.format("₹ %s", String.valueOf(cashAmount)));
                mBinding.etChequeAmount.setText(String.format("₹ %s", String.valueOf(ChequeAmount)));
                mBinding.etOnlineAmount.setText(String.format("₹ %s", String.valueOf(OnlineAmount)));

                mBinding.collectedCashamnt.setText(String.format("₹ %s", String.valueOf(cashAmount)));
                mBinding.collectedOnlineamnt.setText(String.format("₹ %s", String.valueOf(OnlineAmount)));
                mBinding.collectedChequeamnt.setText(String.format("₹ %s", String.valueOf(ChequeAmount)));
                //retriveData From sahrepref

                String Cashamount = SharePrefs.getCashmanagmentSharedPreferences(activity, SharePrefs.CASH_AMOUNT_JSON);
                String CheckAmount = SharePrefs.getCashmanagmentSharedPreferences(activity, SharePrefs.CHEQUE_AMOUNT);
                String onlineAmount = SharePrefs.getCashmanagmentSharedPreferences(activity, SharePrefs.ONLINE_AMOUNT);

                //  totalusedCashAmnt = SharePrefs.getInstance(activity).getInt(SharePrefs.CASH_AMOUNT);
                postChequeCollectionModelArrayList = new Gson().fromJson(CheckAmount, new TypeToken<ArrayList<PostChequeCollectionModel>>() {
                }.getType());
                postCashCollectionArrayList = new Gson().fromJson(Cashamount, new TypeToken<ArrayList<PostCashCollection>>() {
                }.getType());
                postOnlineCollectionModelArrayList = new Gson().fromJson(onlineAmount, new TypeToken<ArrayList<PostOnlineCollectionModel>>() {
                }.getType());

                if (postOnlineCollectionModelArrayList != null) {
                    for (int i = 0; i < postOnlineCollectionModelArrayList.size(); i++) {
                        totalUsedOnlineAmnt = totalUsedOnlineAmnt + postOnlineCollectionModelArrayList.get(i).getPaymentGetwayAmt() + postOnlineCollectionModelArrayList.get(i).getMposAmt();
                    }
                }
                if (postCashCollectionArrayList != null) {
                    for (int i = 0; i < postCashCollectionArrayList.size(); i++) {
                        totalusedCashAmnt = totalusedCashAmnt + (Integer.parseInt(postCashCollectionArrayList.get(i).getValue()) * postCashCollectionArrayList.get(i).getCurrencyCountByDBoy());
                    }
                }
                if (postChequeCollectionModelArrayList != null) {
                    for (int i = 0; i < postChequeCollectionModelArrayList.size(); i++) {
                        totalUsedCheckAmnt = totalUsedCheckAmnt + postChequeCollectionModelArrayList.get(i).getUsedChequeAmt();
                    }
                }
                totalCollectedAmount = totalUsedCheckAmnt + totalusedCashAmnt + totalUsedOnlineAmnt;
                mBinding.totalSubmittedAmnt.setText(String.format("₹ %s", String.valueOf(totalCollectedAmount)));


                mBinding.collectedDepositeamnt.setText(String.format("₹ %s", String.valueOf(totalusedCashAmnt)));
                mBinding.collectedChequedepositeamnt.setText(String.format("₹ %s", String.valueOf(totalUsedCheckAmnt)));
                mBinding.collectedOnlinedepositeamnt.setText(String.format("₹ %s", String.valueOf(totalUsedOnlineAmnt)));


                //Apply Conditions to set Check Icon
                if (Double.parseDouble(mBinding.etCaseAmount.getText().toString().replace("₹ ", "")) == totalusedCashAmnt && totalusedCashAmnt > 0) {
                    mBinding.cashImg.setImageResource(drawable.ic_check_green);
                } else {
                    mBinding.cashImg.setImageResource(drawable.ic_check_grey);

                }
                if (Double.parseDouble(mBinding.etChequeAmount.getText().toString().replace("₹ ", "")) == totalUsedCheckAmnt && totalUsedCheckAmnt > 0) {
                    mBinding.checkImg.setImageResource(drawable.ic_check_green);
                } else {
                    mBinding.checkImg.setImageResource(drawable.ic_check_grey);

                }
                if (Double.parseDouble(mBinding.etOnlineAmount.getText().toString().replace("₹ ", "")) == totalUsedOnlineAmnt && totalUsedOnlineAmnt > 0) {
                    mBinding.onlineImg.setImageResource(drawable.ic_check_green);
                } else {
                    mBinding.onlineImg.setImageResource(drawable.ic_check_grey);

                }

            } else {
                if (SharePrefs.getInstance(activity).getBoolean(SharePrefs.UPDATE_DATA)) {
                    totalUsedCheckAmnt = 0.0;
                    totalusedCashAmnt = 0.0;
                    totalUsedOnlineAmnt = 0.0;

                    mBinding.assignAmnt.setText(String.format("₹ %s", String.valueOf(TotalAmount)));
                    mBinding.etCaseAmount.setText(String.format("₹ %s", String.valueOf(cashAmount)));
                    mBinding.etChequeAmount.setText(String.format("₹ %s", String.valueOf(ChequeAmount)));
                    mBinding.etOnlineAmount.setText(String.format("₹ %s", String.valueOf(OnlineAmount)));

                    mBinding.collectedCashamnt.setText(String.format("₹ %s", String.valueOf(cashAmount)));
                    mBinding.collectedOnlineamnt.setText(String.format("₹ %s", String.valueOf(OnlineAmount)));
                    mBinding.collectedChequeamnt.setText(String.format("₹ %s", String.valueOf(ChequeAmount)));

                    String Cashamount = SharePrefs.getCashmanagmentSharedPreferences(activity, SharePrefs.CASH_AMOUNT_JSON);
                    String CheckAmount = SharePrefs.getCashmanagmentSharedPreferences(activity, SharePrefs.CHEQUE_AMOUNT);
                    String onlineAmount = SharePrefs.getCashmanagmentSharedPreferences(activity, SharePrefs.ONLINE_AMOUNT);

                    //  totalusedCashAmnt = SharePrefs.getInstance(activity).getInt(SharePrefs.CASH_AMOUNT);
                    postChequeCollectionModelArrayList = new Gson().fromJson(CheckAmount, new TypeToken<ArrayList<PostChequeCollectionModel>>() {
                    }.getType());
                    postCashCollectionArrayList = new Gson().fromJson(Cashamount, new TypeToken<ArrayList<PostCashCollection>>() {
                    }.getType());
                    postOnlineCollectionModelArrayList = new Gson().fromJson(onlineAmount, new TypeToken<ArrayList<PostOnlineCollectionModel>>() {
                    }.getType());

                    if (postOnlineCollectionModelArrayList != null) {
                        for (int i = 0; i < postOnlineCollectionModelArrayList.size(); i++) {
                            totalUsedOnlineAmnt = totalUsedOnlineAmnt + postOnlineCollectionModelArrayList.get(i).getPaymentGetwayAmt() + postOnlineCollectionModelArrayList.get(i).getMposAmt();
                        }
                    }
                    if (postCashCollectionArrayList != null) {
                        for (int i = 0; i < postCashCollectionArrayList.size(); i++) {
                            totalusedCashAmnt = totalusedCashAmnt + (Integer.parseInt(postCashCollectionArrayList.get(i).getValue()) * postCashCollectionArrayList.get(i).getCurrencyCountByDBoy());
                        }
                    }
                    if (postChequeCollectionModelArrayList != null) {
                        for (int i = 0; i < postChequeCollectionModelArrayList.size(); i++) {
                            totalUsedCheckAmnt = totalUsedCheckAmnt + postChequeCollectionModelArrayList.get(i).getUsedChequeAmt();
                        }
                    }
                    totalCollectedAmount = totalUsedCheckAmnt + totalusedCashAmnt + totalUsedOnlineAmnt;
                    mBinding.totalSubmittedAmnt.setText(String.format("₹ %s", String.valueOf(totalCollectedAmount)));

                    mBinding.collectedDepositeamnt.setText(String.format("₹ %s", String.valueOf(totalusedCashAmnt)));
                    mBinding.collectedChequedepositeamnt.setText(String.format("₹ %s", String.valueOf(totalUsedCheckAmnt)));
                    mBinding.collectedOnlinedepositeamnt.setText(String.format("₹ %s", String.valueOf(totalUsedOnlineAmnt)));


                    //Apply Conditions to set Check Icon
                    if (Double.parseDouble(mBinding.etCaseAmount.getText().toString().replace("₹ ", "")) == totalusedCashAmnt && totalusedCashAmnt > 0) {
                        mBinding.cashImg.setImageResource(drawable.ic_check_green);
                    } else {
                        mBinding.cashImg.setImageResource(drawable.ic_check_grey);

                    }
                    if (Double.parseDouble(mBinding.etChequeAmount.getText().toString().replace("₹ ", "")) == totalUsedCheckAmnt && totalUsedCheckAmnt > 0) {
                        mBinding.checkImg.setImageResource(drawable.ic_check_green);
                    } else {
                        mBinding.checkImg.setImageResource(drawable.ic_check_grey);

                    }
                    if (Double.parseDouble(mBinding.etOnlineAmount.getText().toString().replace("₹ ", "")) == totalUsedOnlineAmnt && totalUsedOnlineAmnt > 0) {
                        mBinding.onlineImg.setImageResource(drawable.ic_check_green);
                    } else {
                        mBinding.onlineImg.setImageResource(drawable.ic_check_grey);

                    }
                } else {
                    mBinding.assignAmnt.setText(String.format("₹ %s", String.valueOf(TotalAmount)));
                    mBinding.etCaseAmount.setText(String.format("₹ %s", String.valueOf(cashAmount)));
                    mBinding.etChequeAmount.setText(String.format("₹ %s", String.valueOf(ChequeAmount)));
                    mBinding.etOnlineAmount.setText(String.format("₹ %s", String.valueOf(OnlineAmount)));

                    mBinding.collectedCashamnt.setText(String.format("₹ %s", String.valueOf(cashAmount)));
                    mBinding.collectedOnlineamnt.setText(String.format("₹ %s", String.valueOf(OnlineAmount)));
                    mBinding.collectedChequeamnt.setText(String.format("₹ %s", String.valueOf(ChequeAmount)));


                    mBinding.collectedDepositeamnt.setText(String.format("₹ %s", String.valueOf(totalusedCashAmnt)));
                    mBinding.collectedChequedepositeamnt.setText(String.format("₹ %s", String.valueOf(totalUsedCheckAmnt)));
                    mBinding.collectedOnlinedepositeamnt.setText(String.format("₹ %s", String.valueOf(totalUsedOnlineAmnt)));

                    totalCollectedAmount = totalUsedCheckAmnt + totalusedCashAmnt + totalUsedOnlineAmnt;
                    mBinding.totalSubmittedAmnt.setText(String.format("₹ %s", String.valueOf(totalCollectedAmount)));


                    //Apply Conditions to set Check Icon
                    if (Double.parseDouble(mBinding.etCaseAmount.getText().toString().replace("₹ ", "")) == totalusedCashAmnt && totalusedCashAmnt > 0) {
                        mBinding.cashImg.setImageResource(drawable.ic_check_green);
                    } else {
                        mBinding.cashImg.setImageResource(drawable.ic_check_grey);

                    }
                    if (Double.parseDouble(mBinding.etChequeAmount.getText().toString().replace("₹ ", "")) == totalUsedCheckAmnt && totalUsedCheckAmnt > 0) {
                        mBinding.checkImg.setImageResource(drawable.ic_check_green);
                    } else {
                        mBinding.checkImg.setImageResource(drawable.ic_check_grey);

                    }
                    if (Double.parseDouble(mBinding.etOnlineAmount.getText().toString().replace("₹ ", "")) == totalUsedOnlineAmnt && totalUsedOnlineAmnt > 0) {
                        mBinding.onlineImg.setImageResource(drawable.ic_check_green);
                    } else {
                        mBinding.onlineImg.setImageResource(drawable.ic_check_grey);

                    }
                }

            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    private void ResponseofPostData(ApiResponse apiResponse) {

        switch (apiResponse.status) {
            case LOADING:
                mBinding.proRelatedItem.setVisibility(View.VISIBLE);
                break;
            case SUCCESS:
                mBinding.proRelatedItem.setVisibility(View.GONE);
                SuccessResponseofPostData(apiResponse.data);
                break;

            case ERROR:
                mBinding.proRelatedItem.setVisibility(View.GONE);
                Utils.setToast(activity, getResources().getString(string.errorString));
                break;

            default:
                break;
        }
    }

    private void SuccessResponseofPostData(JsonElement response) {
        if (Utils.isJSONValid(response.toString())) {
            if (!response.isJsonNull()) {

                Log.d("response=", response.toString());
                try {

                    JSONObject obj = new JSONObject(response.toString());
                    assignmentIdDataModel = new Gson().fromJson(obj.toString(), AssignmentIdDataModel.class);
                    if (obj.get("status").equals(true)) {

                        Toast.makeText(activity, obj.get("Message").toString(), Toast.LENGTH_SHORT).show();
                        SharePrefs.setCashmanagmentSharedPreference(activity, SharePrefs.ASSIGN_MODEL, null);
                        SharePrefs.getInstance(activity).putString(SharePrefs.ASSIGN_ID, null);
                        SharePrefs.setCashmanagmentSharedPreference(activity, SharePrefs.CASH_AMOUNT_JSON, null);
                        SharePrefs.setCashmanagmentSharedPreference(activity, SharePrefs.CHEQUE_AMOUNT, null);
                        SharePrefs.setCashmanagmentSharedPreference(activity, SharePrefs.ONLINE_AMOUNT, null);
                        SharePrefs.getInstance(activity).putInt(SharePrefs.CASH_AMOUNT, 0);
                        //activity.onBackPressed();
                        startActivity(new Intent(activity, MainActivity.class));

                    } else {
                        Toast.makeText(activity, obj.get("Message").toString(), Toast.LENGTH_SHORT).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(activity, getResources().getString(string.errorString), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(activity, response.toString(), Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setActionBarConfiguration();
    }

    private void setActionBarConfiguration() {
        final DrawerLayout drawer = getActivity().findViewById(id.container);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        Toolbar toolbar = getActivity().findViewById(id.toolbar);
        toolbar.setVisibility(View.GONE);
        ((MainActivity) getActivity()).toggle.syncState();
    }


}