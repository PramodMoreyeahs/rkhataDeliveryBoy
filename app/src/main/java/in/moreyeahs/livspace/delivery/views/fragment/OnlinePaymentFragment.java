package in.moreyeahs.livspace.delivery.views.fragment;

import android.app.Dialog;
import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import in.moreyeahs.livspace.delivery.R;
import in.moreyeahs.livspace.delivery.databinding.OnlinePaymentBinding;
import in.moreyeahs.livspace.delivery.listener.OnlinePaymentListener;
import in.moreyeahs.livspace.delivery.model.AssignmentID;
import in.moreyeahs.livspace.delivery.model.DeliveryIssuanceDetail;
import in.moreyeahs.livspace.delivery.model.PostOnlineCollectionModel;
import in.moreyeahs.livspace.delivery.utilities.SharePrefs;
import in.moreyeahs.livspace.delivery.utilities.TextUtils;
import in.moreyeahs.livspace.delivery.views.adapter.MposPaymentAdapter;
import in.moreyeahs.livspace.delivery.views.adapter.OnlinepaymentAdapter;
import in.moreyeahs.livspace.delivery.views.main.MainActivity;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class OnlinePaymentFragment extends Fragment {
    private MainActivity activity;
    OnlinepaymentAdapter adapter;
    MposPaymentAdapter mposPaymentAdapter;
    private OnlinePaymentBinding mBinding;
    int AssignmentID;
    double AllAmount = 0.0;
    double mposAmount = 0.0;
    double onlineAmount = 0.0;
    ArrayList<PostOnlineCollectionModel> onlineModellist;
    ArrayList<PostOnlineCollectionModel> mposModellist;
    ArrayList<PostOnlineCollectionModel> savemposModellist;
    ArrayList<PostOnlineCollectionModel> saveonlineModellist;
    ArrayList<PostOnlineCollectionModel> saveList;
    ArrayList<String> Datelist;
    private ArrayList<DeliveryIssuanceDetail> deliveryIssuanceDetailsList;
    String historyModel, TransRefNo;
    public Dialog customDialog;
    boolean isCheckDetilsFlag;
    boolean isMposDetilsFlag;
    boolean isFlag = true;

    public OnlinePaymentFragment() {
        // Required empty public constructor
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
        // Inflate the layout for this fragment
        try {

            mBinding = DataBindingUtil.inflate(inflater, R.layout.online_payment, container, false);
            Bundle bundle = this.getArguments();
            if (bundle != null) {
                AssignmentID = bundle.getInt("AssignmentID");
                AllAmount = bundle.getDouble("OnlineAmount");
            }
            initView();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mBinding.getRoot();
    }

    private void initView() {
        //mBinding.recyclerMpos.setLayoutManager(new LinearLayoutManager(activity));
        onlineModellist = new ArrayList<>();
        mposModellist = new ArrayList<>();
        Datelist = new ArrayList<>();
        deliveryIssuanceDetailsList = new ArrayList<>();
        mBinding.recyclerMpos.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mBinding.recyclerMpos.addItemDecoration(new DividerItemDecoration(activity, DividerItemDecoration.VERTICAL));
        mBinding.recyclerOnline.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mBinding.recyclerOnline.addItemDecoration(new DividerItemDecoration(activity, DividerItemDecoration.VERTICAL));
        boolean flag = SharePrefs.getInstance(activity).getBoolean(SharePrefs.FLAG);
        if (flag) {
            mBinding.onlinePaidamnt.setText(String.format("₹ %s", String.valueOf(AllAmount)));
            String Assignmodel = SharePrefs.getCashmanagmentSharedPreferences(activity, SharePrefs.ASSIGN_MODEL);
            String selectedAssignmentId = SharePrefs.getInstance(activity).getString(SharePrefs.ASSIGN_ID);
            ArrayList<in.moreyeahs.livspace.delivery.model.AssignmentID> DeliveryIssuanceDcs = new Gson().fromJson(Assignmodel, new TypeToken<ArrayList<AssignmentID>>() {
            }.getType());

            String OnlineAmount = SharePrefs.getCashmanagmentSharedPreferences(activity, SharePrefs.ONLINE_AMOUNT);
            if (OnlineAmount != null) {
                Gson gson = new Gson();
                Type type = new TypeToken<ArrayList<PostOnlineCollectionModel>>() {
                }.getType();
                saveList = gson.fromJson(OnlineAmount, type);
                if (saveList != null) {
                    for (int j = 0; j < saveList.size(); j++) {
                        if (saveList.get(j).getPaymentFrom().equalsIgnoreCase("mPos")) {
                            mposAmount = mposAmount + saveList.get(j).getMposAmt();
                             mposModellist.add(new PostOnlineCollectionModel(saveList.get(j).getOrderid(), 0.0, saveList.get(j).getMposAmt(), 0, saveList.get(j).getOrderid(), saveList.get(j).getMposReferenceNo(), "", saveList.get(j).getPaymentFrom()));
                        }
                        if (!saveList.get(j).getPaymentFrom().equalsIgnoreCase("mPos") && !saveList.get(j).getPaymentFrom().equalsIgnoreCase("Cheque") && !saveList.get(j).getPaymentFrom().equalsIgnoreCase("Cash")) {
                            onlineAmount = onlineAmount + saveList.get(j).getPaymentGetwayAmt();
                            onlineModellist.add(new PostOnlineCollectionModel(saveList.get(j).getOrderid(), saveList.get(j).getPaymentGetwayAmt(), 0.0, 0, saveList.get(j).getOrderid(), "", saveList.get(j).getPaymentReferenceNO(), saveList.get(j).getPaymentFrom()));
                        }
                    }


                } else {
                    setData(selectedAssignmentId + "", DeliveryIssuanceDcs);
                    for (int j = 0; j < deliveryIssuanceDetailsList.size(); j++) {
                        for (int i = 0; i < deliveryIssuanceDetailsList.get(j).getPaymentDetails().size(); i++) {
                            if (deliveryIssuanceDetailsList.get(j).getPaymentDetails().get(i).getPaymentFrom().equalsIgnoreCase("mPos")) {
                                mposAmount = mposAmount + deliveryIssuanceDetailsList.get(j).getPaymentDetails().get(i).getAmount();
                                double mposAmount = deliveryIssuanceDetailsList.get(j).getPaymentDetails().get(i).getAmount();
                                TransRefNo = deliveryIssuanceDetailsList.get(j).getPaymentDetails().get(i).getTransRefNo();
                                mposModellist.add(new PostOnlineCollectionModel(deliveryIssuanceDetailsList.get(j).getOrderId(), 0.0, mposAmount, 0, deliveryIssuanceDetailsList.get(j).getOrderId(), TransRefNo, "", deliveryIssuanceDetailsList.get(j).getPaymentDetails().get(i).getPaymentFrom()));
                            }
                            if (!deliveryIssuanceDetailsList.get(j).getPaymentDetails().get(i).getPaymentFrom().equalsIgnoreCase("mPos") && !deliveryIssuanceDetailsList.get(j).getPaymentDetails().get(i).getPaymentFrom().equalsIgnoreCase("Cheque") && !deliveryIssuanceDetailsList.get(j).getPaymentDetails().get(i).getPaymentFrom().equalsIgnoreCase("Cash")) {
                                onlineAmount = onlineAmount + deliveryIssuanceDetailsList.get(j).getPaymentDetails().get(i).getAmount();
                                double onlineAmount = deliveryIssuanceDetailsList.get(j).getPaymentDetails().get(i).getAmount();
                                TransRefNo = deliveryIssuanceDetailsList.get(j).getPaymentDetails().get(i).getTransRefNo();
                                onlineModellist.add(new PostOnlineCollectionModel(deliveryIssuanceDetailsList.get(j).getOrderId(), onlineAmount, 0.0, 0, deliveryIssuanceDetailsList.get(j).getOrderId(), "", TransRefNo, deliveryIssuanceDetailsList.get(j).getPaymentDetails().get(i).getPaymentFrom()));
                            }
                        }

                    }

                }
            } else {
                setData(selectedAssignmentId + "", DeliveryIssuanceDcs);
                for (int j = 0; j < deliveryIssuanceDetailsList.size(); j++) {
                    for (int i = 0; i < deliveryIssuanceDetailsList.get(j).getPaymentDetails().size(); i++) {
                        if (deliveryIssuanceDetailsList.get(j).getPaymentDetails().get(i).getPaymentFrom().equalsIgnoreCase("mPos")) {
                            mposAmount = mposAmount + deliveryIssuanceDetailsList.get(j).getPaymentDetails().get(i).getAmount();
                            double mposAmount = deliveryIssuanceDetailsList.get(j).getPaymentDetails().get(i).getAmount();
                            TransRefNo = deliveryIssuanceDetailsList.get(j).getPaymentDetails().get(i).getTransRefNo();
                            mposModellist.add(new PostOnlineCollectionModel(deliveryIssuanceDetailsList.get(j).getOrderId(), 0.0, mposAmount, 0, deliveryIssuanceDetailsList.get(j).getOrderId(), TransRefNo, "", deliveryIssuanceDetailsList.get(j).getPaymentDetails().get(i).getPaymentFrom()));
                        }
                        if (!deliveryIssuanceDetailsList.get(j).getPaymentDetails().get(i).getPaymentFrom().equalsIgnoreCase("mPos") && !deliveryIssuanceDetailsList.get(j).getPaymentDetails().get(i).getPaymentFrom().equalsIgnoreCase("Cheque") && !deliveryIssuanceDetailsList.get(j).getPaymentDetails().get(i).getPaymentFrom().equalsIgnoreCase("Cash")) {
                            onlineAmount = onlineAmount + deliveryIssuanceDetailsList.get(j).getPaymentDetails().get(i).getAmount();
                            double onlineAmount = deliveryIssuanceDetailsList.get(j).getPaymentDetails().get(i).getAmount();
                            TransRefNo = deliveryIssuanceDetailsList.get(j).getPaymentDetails().get(i).getTransRefNo();
                            onlineModellist.add(new PostOnlineCollectionModel(deliveryIssuanceDetailsList.get(j).getOrderId(), onlineAmount, 0.0, 0, deliveryIssuanceDetailsList.get(j).getOrderId(), "", TransRefNo, deliveryIssuanceDetailsList.get(j).getPaymentDetails().get(i).getPaymentFrom()));
                        }
                    }

                }

            }


        } else {
            mBinding.onlinePaidamnt.setText(String.format("₹ %s", String.valueOf(AllAmount)));
            historyModel = SharePrefs.getCashmanagmentSharedPreferences(activity, SharePrefs.HISTORY_MODEL);
            setHistoryData(historyModel);
        }


        mBinding.onlinePaidaamnt.setText(String.format("₹ %s", String.valueOf(onlineAmount)));
        mBinding.mposPaidaamnt.setText(String.format("₹ %s", String.valueOf(mposAmount)));

        if (onlineModellist.size() > 0) {
            adapter = new OnlinepaymentAdapter(activity, onlineModellist);
            mBinding.recyclerOnline.setAdapter(adapter);

        }
        if (mposModellist.size() > 0) {
            mposPaymentAdapter = new MposPaymentAdapter(activity, mposModellist);
            mBinding.recyclerMpos.setAdapter(mposPaymentAdapter);
        }
    }

    private void setHistoryData(String historyModel) {
        try {
            String OnlineAmount = SharePrefs.getCashmanagmentSharedPreferences(activity, SharePrefs.ONLINE_AMOUNT);
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<PostOnlineCollectionModel>>() {
            }.getType();
            if (OnlineAmount != null)
            {
                saveList = gson.fromJson(OnlineAmount, type);
                if (saveList != null) {
                    for (int j = 0; j < saveList.size(); j++) {
                        if (saveList.get(j).getMposAmt()!= 0) {
                            mposAmount = mposAmount + saveList.get(j).getMposAmt();
                            mposModellist.add(new PostOnlineCollectionModel(saveList.get(j).getOrderid(), 0.0, saveList.get(j).getMposAmt(), 0, saveList.get(j).getOrderid(), saveList.get(j).getMposReferenceNo(), "", saveList.get(j).getPaymentFrom()));
                        }
                        else
                            {
                            onlineAmount = onlineAmount + saveList.get(j).getPaymentGetwayAmt();
                            onlineModellist.add(new PostOnlineCollectionModel(saveList.get(j).getOrderid(), saveList.get(j).getPaymentGetwayAmt(), 0.0, 0, saveList.get(j).getOrderid(), "", saveList.get(j).getPaymentReferenceNO(), saveList.get(j).getPaymentFrom()));

                        }

                    }


                }
                else{
                    JSONObject obj = new JSONObject(historyModel);
                    ArrayList<PostOnlineCollectionModel> CollectionArrayList = gson.fromJson(obj.get("OnlineCollections").toString(), type);
                    for (int i = 0; i < CollectionArrayList.size(); i++) {
                        if (CollectionArrayList.get(i).getMposAmt() != 0) {
                            mposAmount = mposAmount + CollectionArrayList.get(i).getMposAmt();
                            mposModellist.add(new PostOnlineCollectionModel(CollectionArrayList.get(i).getOrderid(), 0.0, CollectionArrayList.get(i).getMposAmt(), 0, CollectionArrayList.get(i).getOrderid(), CollectionArrayList.get(i).getMposReferenceNo(), "", CollectionArrayList.get(i).getPaymentFrom()));
                        } else {
                            onlineAmount = onlineAmount + CollectionArrayList.get(i).getPaymentGetwayAmt();
                            onlineModellist.add(new PostOnlineCollectionModel(CollectionArrayList.get(i).getOrderid(), CollectionArrayList.get(i).getPaymentGetwayAmt(), 0.0, 0, CollectionArrayList.get(i).getOrderid(), "", CollectionArrayList.get(i).getPaymentReferenceNO(), CollectionArrayList.get(i).getPaymentFrom()));
                        }

                    }
                }
            }
            else{
                JSONObject obj = new JSONObject(historyModel);
                ArrayList<PostOnlineCollectionModel> CollectionArrayList = gson.fromJson(obj.get("OnlineCollections").toString(), type);
                for (int i = 0; i < CollectionArrayList.size(); i++) {
                    if (CollectionArrayList.get(i).getMposAmt() != 0) {
                        mposAmount = mposAmount + CollectionArrayList.get(i).getMposAmt();
                        mposModellist.add(new PostOnlineCollectionModel(CollectionArrayList.get(i).getOrderid(), 0.0, CollectionArrayList.get(i).getMposAmt(), 0, CollectionArrayList.get(i).getOrderid(), CollectionArrayList.get(i).getMposReferenceNo(), "", CollectionArrayList.get(i).getPaymentFrom()));
                    } else {
                        onlineAmount = onlineAmount + CollectionArrayList.get(i).getPaymentGetwayAmt();
                        onlineModellist.add(new PostOnlineCollectionModel(CollectionArrayList.get(i).getOrderid(), CollectionArrayList.get(i).getPaymentGetwayAmt(), 0.0, 0, CollectionArrayList.get(i).getOrderid(), "", CollectionArrayList.get(i).getPaymentReferenceNO(), CollectionArrayList.get(i).getPaymentFrom()));
                    }

                }
            }




        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setData(String assignmentID, ArrayList<AssignmentID> deliveryIssuanceDcs) {
        deliveryIssuanceDetailsList.clear();
        try {
            for (int i = 0; i < deliveryIssuanceDcs.size(); i++) {
                String AssignmentID = String.valueOf(deliveryIssuanceDcs.get(i).getDeliveryIssuanceId());
                if (AssignmentID.equalsIgnoreCase(assignmentID)) {
                    deliveryIssuanceDetailsList = deliveryIssuanceDcs.get(i).getDeliveryIssuanceDetailDcs();


                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        mBinding.setOnlinePaymentListener(new OnlinePaymentListener() {
            boolean isCheckDetilsFlag;
            boolean isMposDetilsFlag;

            @Override
            public void onlineLayHideUnHideClicked() {

                if (isCheckDetilsFlag) {
                    mBinding.recyclerOnline.setVisibility(View.VISIBLE);
                    isCheckDetilsFlag = false;
                } else {
                    mBinding.recyclerOnline.setVisibility(View.GONE);
                    isCheckDetilsFlag = true;
                }
            }

            @Override
            public void mposLayHideUnHideClicked() {
                if (isMposDetilsFlag) {
                    mBinding.recyclerMpos.setVisibility(View.VISIBLE);
                    isMposDetilsFlag = false;
                } else {
                    mBinding.recyclerMpos.setVisibility(View.GONE);
                    isMposDetilsFlag = true;
                }
            }


        });
        mBinding.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SubmitPopup();
            }
        });
        mBinding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onBackPressed();
            }
        });
    }

    public void SubmitPopup() {

        LayoutInflater inflater = getLayoutInflater();
        if (activity != null && inflater != null) {
            final View mView = inflater.inflate(R.layout.confirmation_popup, null);
            customDialog = new Dialog(activity, R.style.CustomDialog);
            customDialog.setContentView(mView);
            TextView okBtn = mView.findViewById(R.id.btn_yes);
            TextView cancelBtn = mView.findViewById(R.id.btn_no);
            TextView textMesaage = mView.findViewById(R.id.tv_txt);
            textMesaage.setText(getContext().getResources().getString(R.string.online_summary));
            okBtn.setOnClickListener(v -> {
                ArrayList<PostOnlineCollectionModel> list = new ArrayList<>();
                ArrayList<PostOnlineCollectionModel> mposlist = new ArrayList<>();

                if (onlineModellist.size() > 0 && onlineModellist != null) {
                    if (adapter.getList() != null) {
                        list = adapter.getList();
                    }
                }

                if (mposModellist.size() > 0 && mposModellist != null) {
                    if (mposPaymentAdapter.getList().size() > 0) {
                        mposlist = mposPaymentAdapter.getList();

                    }
                }
                for (int i = 0; i < mposlist.size(); i++) {
                    if (TextUtils.isNullOrEmpty(mposlist.get(i).getMposReferenceNo())) {
                        isFlag = false;
                        break;
                    } else {
                        isFlag = true;
                    }
                }
                if (isFlag) {
                    mposlist.addAll(list);
                    Gson gson = new Gson();
                    SharePrefs.setCashmanagmentSharedPreference(activity, SharePrefs.ONLINE_AMOUNT, gson.toJson(mposlist));
                    System.out.println("json" + SharePrefs.getCashmanagmentSharedPreferences(activity, SharePrefs.ONLINE_AMOUNT));
                    activity.onBackPressed();
                    customDialog.dismiss();
                } else {
                    Toast.makeText(activity, R.string.enter_reference_id, Toast.LENGTH_SHORT).show();

                }
            });
            cancelBtn.setOnClickListener(v -> customDialog.dismiss());
            customDialog.show();
        }
    }


}
