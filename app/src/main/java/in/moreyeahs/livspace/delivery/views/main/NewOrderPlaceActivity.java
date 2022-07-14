package in.moreyeahs.livspace.delivery.views.main;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import id.zelory.compressor.Compressor;
import in.moreyeahs.livspace.delivery.R;
import in.moreyeahs.livspace.delivery.api.RestClient;
import in.moreyeahs.livspace.delivery.databinding.OrderPlaceFragBinding;
import in.moreyeahs.livspace.delivery.databinding.SignaturePopupBinding;
import in.moreyeahs.livspace.delivery.listener.MultiMposListener;
import in.moreyeahs.livspace.delivery.listener.OrderPlacedListener;
import in.moreyeahs.livspace.delivery.listener.OrderchequeimageListener;
import in.moreyeahs.livspace.delivery.model.BankNameModel;
import in.moreyeahs.livspace.delivery.model.ChequePermissionModel;
import in.moreyeahs.livspace.delivery.model.DeliveryPayments;
import in.moreyeahs.livspace.delivery.model.OrderDetailsModel;
import in.moreyeahs.livspace.delivery.model.OrderItemDetails;
import in.moreyeahs.livspace.delivery.model.OrderPlacedModel;
import in.moreyeahs.livspace.delivery.model.OrderResponsesModel;
import in.moreyeahs.livspace.delivery.utilities.ApiResponse;
import in.moreyeahs.livspace.delivery.utilities.CommonClassForAPI;
import in.moreyeahs.livspace.delivery.utilities.CommonMethods;
import in.moreyeahs.livspace.delivery.utilities.CustomRunnable;
import in.moreyeahs.livspace.delivery.utilities.GPSTracker;
import in.moreyeahs.livspace.delivery.utilities.Logger;
import in.moreyeahs.livspace.delivery.utilities.SharePrefs;
import in.moreyeahs.livspace.delivery.utilities.TextUtils;
import in.moreyeahs.livspace.delivery.utilities.Utils;
import in.moreyeahs.livspace.delivery.viewmodels.OrderPlaceViewModel;
import in.moreyeahs.livspace.delivery.views.adapter.MultiMposAdapter;
import in.moreyeahs.livspace.delivery.views.adapter.MultipleChequeAdapter;
import in.moreyeahs.livspace.delivery.views.adapter.MyItemDetailsAdapter;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class NewOrderPlaceActivity extends AppCompatActivity implements CropImageView.OnSetImageUriCompleteListener, OrderchequeimageListener, MultiMposListener {
    private final int HDFC_REQUEST = 999;
    private Dialog customDialog;
    private OrderPlaceFragBinding mBinding;
    private OrderPlaceViewModel orderPlaceViewModel;
    private MyItemDetailsAdapter myItemDetailsAdapter;
    private ArrayList<OrderItemDetails> itemDetailsList = new ArrayList<>();
    private Spinner spStatus, spComment,spCash;
    private int orderId, givenTotalAmount;
    private EditText etCaseAmount, etChequeAmount, etUPIAmount;
    private TextView tvReceivedTotalAmt;
    private Button btnPayment, btnUpdate;
    private int redispatch, deliveryIssuanceId, orderDispatchedMasterId, warehouseId, customerId;
    private String trupay = "false";
    private String sCaseAmount = "";
    private String sChequeAmount = "";
    private String sUPIAmount = "";
    private File compressedImage;
    private double sServiceText;
    private String fCheckName, dboyMobileNo, dboyName, uploadFilePath;
    private boolean isCheckDetilsFlag = true, isPreapaidkDetilsFlag = true;
    private LinearLayout checkDetails;
    private SignaturePad mSignaturePad;
    // Message type for the handler
    private boolean paymentBtnFlag = false, isOtpVerified;
    private TextView tvTrupay;
    private byte[] byteArraySing;
    private LinearLayout mPosViewLayout;
    private String paymentThrough = "";
    private String ChequeimgUrl = "";
    private Button saveSignBtn;
    private double lat, lg;
    private ArrayList<String> BankNameList = new ArrayList<>();
    private String time;
    private CustomRunnable customRunnable;
    private Handler handler;
    private double dueAmount = 0;
    private int Customerid;
    private String custname, custaddress, custmobno, colorCode, msg, type;
    private Uri mCropImageUri;
    private BottomSheetDialog dialog;
    private ImageView chequeimage;
    private ProgressDialog progressDialog;
    ArrayList<DeliveryPayments> chequePaymentList = new ArrayList<>();
    ArrayList<DeliveryPayments> mposPaymentList = new ArrayList<>();
    MultipleChequeAdapter adapter;
    MultiMposAdapter mposadapter;
    int chequeAmount, mposAmount;
    int position, chequeLimit;
    boolean ischeque = false;
    ArrayList<DeliveryPayments> postPaymentList;
    Matrix matrix;
    double amount = 0.0, hdfcamount = 0.0, epayamount = 0.0, grossAmt, cashAmt = 0.0, gullakamount = 0.0, cheque_Amount = 0.0, mpos_Amount = 0.0;
    Utils utils;
    CommonClassForAPI commonClassForAPI;
    boolean credit_status=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.order_place_frag);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("submitting");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        if (getIntent().getExtras() != null) {
            lat = getIntent().getDoubleExtra("lat", 0);
            lg = getIntent().getDoubleExtra("lg", 0);
            orderId = getIntent().getIntExtra("ORDER_ID", 0);
            deliveryIssuanceId = getIntent().getIntExtra("assignmentID", 0);
            int position = getIntent().getIntExtra("position", 0);
            String skcode = getIntent().getStringExtra("SkCode");
            time = getIntent().getStringExtra("time");
            custname = getIntent().getStringExtra("CUSTOMER_NAME");
            custaddress = getIntent().getStringExtra("SHIPPING_ADDRESS");
            custmobno = getIntent().getStringExtra("MOBILE_NUMBER");
            Customerid = getIntent().getIntExtra("Customerid", 0);
            colorCode = getIntent().getStringExtra("colorCode");
            type = getIntent().getStringExtra("type");
        }

        intView();
        onCropImageClick();
        customDialog = new Dialog(this, R.style.CustomDialog);
        orderPlaceViewModel = ViewModelProviders.of(this).get(OrderPlaceViewModel.class);
        mBinding.setOrderPlaceViewModel(orderPlaceViewModel);
        setActionBarConfiguration();
        /***
         * listener
         * **/
        mBinding.setOrderPlacedListener(new OrderPlacedListener() {
            /***
             * check lay hide and un-hide
             * **/
            @Override
            public void checkLayHideUnHideClicked() {
                if (ischeque) {
                    if (isCheckDetilsFlag) {
                        checkDetails.setVisibility(View.VISIBLE);
                        mBinding.UTRDetail.setVisibility(View.GONE);
                        isCheckDetilsFlag = false;
                    } else {
                        checkDetails.setVisibility(View.GONE);
                        isCheckDetilsFlag = true;
                    }
                } else {
                    if (msg != null && !(msg.equalsIgnoreCase(""))) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(NewOrderPlaceActivity.this)
                                .setCancelable(true)
                                .setMessage(msg);
                        builder.show();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(NewOrderPlaceActivity.this)
                                .setCancelable(true)
                                .setMessage("This Customer is Not Eligible for Cheque Payment");
                        builder.show();
                    }


                }


            }

            @Override
            public void checkUriHideUnHideClicked() {
                if (isPreapaidkDetilsFlag) {
                    mBinding.UTRDetail.setVisibility(View.VISIBLE);
                    checkDetails.setVisibility(View.GONE);
                    isPreapaidkDetilsFlag = false;
                } else {
                    mBinding.UTRDetail.setVisibility(View.GONE);
                    isPreapaidkDetilsFlag = true;
                }
            }

            @Override
            public void updateBtnClicked() {
                if (mBinding.spStatus.getSelectedItemPosition() == 0) {
                    Utils.setToast(NewOrderPlaceActivity.this, getString(R.string.choose_status_and_comment));
                } else if (mBinding.spComment.getSelectedItemPosition() == 0) {
                    Utils.setToast(NewOrderPlaceActivity.this, getString(R.string.choose_status_and_comment));
                } else {
                    if (trupay.equalsIgnoreCase("true")) {
                        resetValue("prepaidAmt");
                        WSCallforOrderPlace(true);
                    } else {
                        // generate OTP for Order
                        btnUpdate.setClickable(false);
                        resetValue("orderCancel");
                        isOtpVerified=true;
                        UpdateAPICall(false);
                    }
                }
            }


            /***
             * offline order placed btn
             * **/
            @Override
            public void paymentBtnClicked() {

//                if (!Utils.checkInternetConnection(NewOrderPlaceActivity.this)) {
//                    Utils.setToast(NewOrderPlaceActivity.this, getResources().getString(R.string.network_error));
//                } else {
//                    // getting et value
//                    postPaymentList = new ArrayList<>();
//                    sCaseAmount = etCaseAmount.getText().toString();
//                    if (adapter != null && adapter.getListFromAdapter() != null) {
//                        chequePaymentList = adapter.getListFromAdapter();
//                        mposPaymentList = mposadapter.getListFromAdapter();
//                    }
//
//                    if (sCaseAmount.length() > 0) {
//                        if (Integer.valueOf(sCaseAmount) > 200000) {
//                            Utils.setToast(NewOrderPlaceActivity.this, "Cash Amount should not Be Greater than 2 lakh");
//                        } else {
//                            double TotalenteredAmount = Double.parseDouble(mBinding.tvReceivedTotalAmt.getText().toString());
//                            if (spStatus.getSelectedItem().toString().equalsIgnoreCase("Delivered")) {
//                                if (amount == grossAmt) {
//                                    if (sChequeAmount != "" && sChequeAmount.length() > 0 && !(sChequeAmount.equalsIgnoreCase("0"))) {
//                                        boolean isboolean = false;
//                                        if (Integer.valueOf(sChequeAmount) > chequeLimit) {
//                                            Utils.setToast(NewOrderPlaceActivity.this, "Cheque Amount should not be greater than " + chequeLimit);
//                                        } else {
//                                            if (sUPIAmount != "" && sUPIAmount.length() > 0 && !(sUPIAmount.equalsIgnoreCase("0"))) {
//
//                                                if (chequePaymentList.size() > 0) {
//
//                                                    PaymentCheckValidation(true, true);
//
//                                                }
//
//                                            } else {
//                                                if (sChequeAmount != "" && sChequeAmount.length() > 0 && !(sChequeAmount.equalsIgnoreCase("0"))) {
//                                                    PaymentCheckValidation(true, false);
//                                                } else {
//                                                    orderPlacedPopup();
//                                                }
//                                            }
//                                        }
//
//
//                                    } else {
//                                        if (sUPIAmount != "" && sUPIAmount.length() > 0 && !(sUPIAmount.equalsIgnoreCase("0"))) {
//                                            PaymentCheckValidation(false, true);
//                                        } else {
//                                            orderPlacedPopup();
//                                        }
//
//                                    }
//
//
//                                } else {
//                                    Utils.setToast(NewOrderPlaceActivity.this, getString(R.string.enter_total_amt));
//                                }
//                            } else if (spStatus.getSelectedItem().toString().equalsIgnoreCase("select")) {
//                                Utils.setToast(NewOrderPlaceActivity.this, getString(R.string.choose_status_and_comment));
//                            } else {
//                                // Status deliver cancel
//                                resetValue("orderCancel");
//                                WSCallforOrderPlace(false);
//                            }
//                        }
//                    } else {
//                        double TotalenteredAmount = Double.parseDouble(mBinding.tvReceivedTotalAmt.getText().toString());
//                        if (spStatus.getSelectedItem().toString().equalsIgnoreCase("Delivered")) {
//                            if (amount == grossAmt) {
//                                if (sChequeAmount != "" && sChequeAmount.length() > 0 && !(sChequeAmount.equalsIgnoreCase("0"))) {
//
//                                    if (Integer.valueOf(sChequeAmount) > chequeLimit) {
//                                        Utils.setToast(NewOrderPlaceActivity.this, "Cheque Amount should not be greater than " + chequeLimit);
//                                    } else {
//                                        if (sUPIAmount != "" && sUPIAmount.length() > 0 && !(sUPIAmount.equalsIgnoreCase("0"))) {
//                                            if (chequePaymentList.size() > 0) {
//                                                PaymentCheckValidation(true, true);
//
//                                            }
//                                        } else {
//                                            if (sChequeAmount != "" && sChequeAmount.length() > 0 && !(sChequeAmount.equalsIgnoreCase("0"))) {
//
//                                                if (chequePaymentList.size() > 0) {
//                                                    PaymentCheckValidation(true, false);
//                                                }
//
//                                            }
//                                        }
//
//                                    }
//
//                                } else {
//                                    if (sUPIAmount != "" && sUPIAmount.length() > 0 && !(sUPIAmount.equalsIgnoreCase("0"))) {
//                                        PaymentCheckValidation(false, true);
//                                    }
//
//                                }
//
//                            } else {
//                                Utils.setToast(NewOrderPlaceActivity.this, getString(R.string.enter_total_amt));
//                            }
//                        } else if (spStatus.getSelectedItem().toString().equalsIgnoreCase("select")) {
//                            Utils.setToast(NewOrderPlaceActivity.this, getString(R.string.choose_status_and_comment));
//                        } else {
//                            // Status deliver cancel
//                            resetValue("orderCancel");
//                            WSCallforOrderPlace(false);
//                        }
//                    }
//
//                }

                if (utils.isNetworkAvailable()) {
                    if (spStatus.getSelectedItem().toString().equalsIgnoreCase("select")) {
                        Utils.setToast(NewOrderPlaceActivity.this, getString(R.string.choose_status_and_comment));
                    } else if (spComment.getSelectedItem().toString().equalsIgnoreCase("select")) {
                        Utils.setToast(NewOrderPlaceActivity.this, getString(R.string.choose_status_and_comment));
                    } else {
                        if (commonClassForAPI != null) {

                            commonClassForAPI.changeOrderStatus(deliveredDes, orderId, spStatus.getSelectedItem().toString(),credit_status);
                        }
                    }
                } else {
                    utils.setToast(NewOrderPlaceActivity.this, getString(R.string.internet_connection));
                }

            }

            DisposableObserver<JsonObject> deliveredDes = new DisposableObserver<JsonObject>() {
                @Override
                public void onNext(JsonObject response) {
                    try {
                        if (response.get("Message").getAsString().equalsIgnoreCase("Success")) {
                            startActivity(new Intent(NewOrderPlaceActivity.this, MainActivity.class));
                            Toast.makeText(NewOrderPlaceActivity.this, "Assignment delivered", Toast.LENGTH_SHORT).show();
                            finish();
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

            /***
             * Online order btn
             * **/
            @Override
            public void onlinePaymentBtnClicked() {
                if (spStatus.getSelectedItem().toString().equalsIgnoreCase("select")) {
                    Utils.setToast(NewOrderPlaceActivity.this, getString(R.string.choose_status_and_comment));
                } else {
                    if (lat != 0 && lg != 0) {
                        GPSTracker gpsTracker = new GPSTracker(NewOrderPlaceActivity.this);
                        double latitude = gpsTracker.getLatitude();
                        double longitutde = gpsTracker.getLongitude();
                        //double dist = distance(lat, lg, latitude, longitutde);

                        Location locationA = new Location("point A");

                        locationA.setLatitude(lat);
                        locationA.setLongitude(lg);

                        Location locationB = new Location("point B");

                        locationB.setLatitude(latitude);
                        locationB.setLongitude(longitutde);

                        float distance = locationA.distanceTo(locationB);
                        if (distance <= 100) {
                            resetValue("online");

                        } else {
                            Intent i = new Intent(NewOrderPlaceActivity.this, ScanCodeActivity.class);
                            i.putExtra("orderID", orderId);
                            i.putExtra("type", "online");
                            startActivityForResult(i, 3);

                        }

                    } else {
                        Intent i = new Intent(NewOrderPlaceActivity.this, ScanCodeActivity.class);
                        i.putExtra("orderID", orderId);
                        i.putExtra("type", "online");
                        startActivityForResult(i, 3);

                    }
                }
            }
        });


        /***
         * Getting response
         * **/
        orderPlaceViewModel.getOrderDetailsData().observe(this, apiResponse -> {
            if (apiResponse != null) {
                consumeResponse(apiResponse);
            }
        });


        FetchCustinfo(getCustInfo, Customerid);

        /***
         * Order Placed response Getting
         * **/
        orderPlaceViewModel.getOrderPlacedResponseData().observe(this, apiResponse -> {
            if (apiResponse != null) {
                consumeResponseForOrderPlace(apiResponse);
            }
        });

        orderPlaceViewModel.getchequepermission().observe(this, apiResponse -> {
            if (apiResponse != null) {
                consumeResponseForchequePermission(apiResponse);
            }
        });


        orderPlaceViewModel.getBankname().observe(this, apiResponse -> {
            if (apiResponse != null) {
                consumeResponseForBankname(apiResponse);
            }
        });
        orderPlaceViewModel.hitBankName();

        orderPlaceViewModel.checkOrderOTPExist().observe(this, apiResponse -> {
            if (apiResponse != null) {
                consumeCheckOrderOtpResponse(apiResponse);
            }
        });
        orderPlaceViewModel.generateOrderOTP().observe(this, apiResponse -> {
            if (apiResponse != null) {
                consumeOrderOtpGenerateResponse(apiResponse);
            }
        });
        orderPlaceViewModel.validateOrderOTP().observe(this, apiResponse -> {
            if (apiResponse != null) {
                consumeVerifyOrderOtpResponse(apiResponse);
            }
        });

/***
 * Order details API call
 * **/

        CallAPI();
    }

    private void PaymentCheckValidation(boolean ischeque, boolean isMpos) {
        ArrayList<String> newList = new ArrayList<String>();
        boolean isboolean = false;
        if (ischeque && isMpos) {
            for (int i = 0; i < chequePaymentList.size(); i++) {
                if (!newList.contains(chequePaymentList.get(i).getTransId())) {
                    newList.add(chequePaymentList.get(i).getTransId());
                } else {
                    isboolean = false;
                    Utils.setToast(NewOrderPlaceActivity.this, "Cheque Number should not be same");
                    break;
                }
                if (chequePaymentList.get(i).getAmount() == 0) {
                    Utils.setToast(NewOrderPlaceActivity.this, "Enter Cheque Amount");
                    isboolean = false;
                    break;
                } else if (chequePaymentList.get(i).getChequeBankName() == null || chequePaymentList.get(i).getChequeBankName().equalsIgnoreCase("") || chequePaymentList.get(i).getChequeBankName().equalsIgnoreCase("Select Bank Name")) {
                    isboolean = false;
                    Utils.setToast(NewOrderPlaceActivity.this, getString(R.string.plz_select_Bank_name));
                    break;
                } else if (chequePaymentList.get(i).getChequeImageUrl() == null || chequePaymentList.get(i).getChequeImageUrl().equalsIgnoreCase("")) {
                    isboolean = false;
                    Utils.setToast(NewOrderPlaceActivity.this, getString(R.string.plz_upload_check_images));
                    break;

                } else if (chequePaymentList.get(i).getPaymentDate() == null || chequePaymentList.get(i).getPaymentDate().equalsIgnoreCase("")) {
                    isboolean = false;
                    Utils.setToast(NewOrderPlaceActivity.this, getString(R.string.enter_check_date));
                    break;
                } else if (chequePaymentList.get(i).getTransId() == null || chequePaymentList.get(i).getTransId().equalsIgnoreCase("")) {
                    isboolean = false;
                    Utils.setToast(NewOrderPlaceActivity.this, getString(R.string.plz_enter_chequeno));
                    break;
                } else {
                    isboolean = true;
                }
            }
            if (isboolean) {
                ArrayList<String> newlist = new ArrayList<String>();
                boolean ismposboolean = false;
                for (int i = 0; i < mposPaymentList.size(); i++) {
                    if (!newlist.contains(mposPaymentList.get(i).getTransId())) {
                        newlist.add(mposPaymentList.get(i).getTransId());
                    } else {
                        ismposboolean = false;
                        Utils.setToast(NewOrderPlaceActivity.this, "Mpos Number should not be same");
                        break;
                    }
                    if (mposPaymentList.get(i).getTransId() == null || mposPaymentList.get(i).getTransId().equalsIgnoreCase("")) {
                        ismposboolean = false;
                        Utils.setToast(NewOrderPlaceActivity.this, getString(R.string.enter_reference_id));
                        break;
                    } else if (mposPaymentList.get(i).getTransId().length() < 8) {
                        ismposboolean = false;
                        Utils.setToast(NewOrderPlaceActivity.this, getString(R.string.enter_reference_mpos_id));
                        break;
                    } else if (mposPaymentList.get(i).getAmount() == 0) {
                        ismposboolean = false;
                        Utils.setToast(NewOrderPlaceActivity.this, "Please Enter Mpos Amount");
                        break;
                    } else {
                        ismposboolean = true;
                    }

                }
                if (ismposboolean) {
                    orderPlacedPopup();

                }

            }
        } else if (ischeque) {
            for (int i = 0; i < chequePaymentList.size(); i++) {
                if (!newList.contains(chequePaymentList.get(i).getTransId())) {
                    newList.add(chequePaymentList.get(i).getTransId());
                } else {
                    isboolean = false;
                    Utils.setToast(NewOrderPlaceActivity.this, "Cheque Number should not be same");
                    break;
                }
                if (chequePaymentList.get(i).getAmount() == 0) {
                    Utils.setToast(NewOrderPlaceActivity.this, "Enter Cheque Amount");
                    isboolean = false;
                    break;
                } else if (chequePaymentList.get(i).getChequeBankName() == null || chequePaymentList.get(i).getChequeBankName().equalsIgnoreCase("") || chequePaymentList.get(i).getChequeBankName().equalsIgnoreCase("Select Bank Name")) {
                    isboolean = false;
                    Utils.setToast(NewOrderPlaceActivity.this, getString(R.string.plz_select_Bank_name));
                    break;
                } else if (chequePaymentList.get(i).getChequeImageUrl() == null || chequePaymentList.get(i).getChequeImageUrl().equalsIgnoreCase("")) {
                    isboolean = false;
                    Utils.setToast(NewOrderPlaceActivity.this, getString(R.string.plz_upload_check_images));
                    break;

                } else if (chequePaymentList.get(i).getPaymentDate() == null || chequePaymentList.get(i).getPaymentDate().equalsIgnoreCase("")) {
                    isboolean = false;
                    Utils.setToast(NewOrderPlaceActivity.this, getString(R.string.enter_check_date));
                    break;
                } else if (chequePaymentList.get(i).getTransId() == null || chequePaymentList.get(i).getTransId().equalsIgnoreCase("")) {
                    isboolean = false;
                    Utils.setToast(NewOrderPlaceActivity.this, getString(R.string.plz_enter_chequeno));
                    break;
                } else {
                    isboolean = true;
                }


            }
            if (isboolean) {
                orderPlacedPopup();
            }
        } else if (isMpos) {
            ArrayList<String> newlist = new ArrayList<String>();
            boolean ismposboolean = false;
            for (int i = 0; i < mposPaymentList.size(); i++) {
                if (!newlist.contains(mposPaymentList.get(i).getTransId())) {
                    newlist.add(mposPaymentList.get(i).getTransId());
                } else {
                    ismposboolean = false;
                    Utils.setToast(NewOrderPlaceActivity.this, "Mpos Number should not be same");
                    break;
                }
                if (mposPaymentList.get(i).getTransId() == null || mposPaymentList.get(i).getTransId().equalsIgnoreCase("")) {
                    ismposboolean = false;
                    Utils.setToast(NewOrderPlaceActivity.this, getString(R.string.enter_reference_id));
                    break;
                } else if (mposPaymentList.get(i).getTransId().length() < 8) {
                    ismposboolean = false;
                    Utils.setToast(NewOrderPlaceActivity.this, getString(R.string.enter_reference_mpos_id));
                    break;
                } else if (mposPaymentList.get(i).getAmount() == 0) {
                    ismposboolean = false;
                    Utils.setToast(NewOrderPlaceActivity.this, "Please Enter Mpos Amount");
                    break;
                } else {
                    ismposboolean = true;
                }

            }
            if (ismposboolean) {
                orderPlacedPopup();
            }
        }

    }

    private void CallAPI() {
        if (!Utils.checkInternetConnection(this)) {
            Utils.setToast(this, getResources().getString(R.string.network_error));
        } else {
            orderPlaceViewModel.hitOrderDetailsApi(orderId);
        }


    }

    /**
     * Give capture permission
     **/
    private void requestWritePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        } else {
            CropImage.startPickImageActivity(this);
        }
    }

    private void consumeResponseForBankname(ApiResponse apiResponse) {
        switch (apiResponse.status) {
            case LOADING:
                mBinding.proRelatedItem.setVisibility(View.VISIBLE);
                break;
            case SUCCESS:
                mBinding.proRelatedItem.setVisibility(View.GONE);
                renderSuccessResponseForBankname(apiResponse.data);
                break;
            case ERROR:
                mBinding.proRelatedItem.setVisibility(View.GONE);
                Utils.setToast(this, getResources().getString(R.string.errorString));
                break;
            default:
                break;
        }
    }


    private void renderSuccessResponseForBankname(JsonElement response) {
        if (Utils.isJSONValid(response.toString())) {
            if (!response.isJsonNull()) {
                Logger.d(CommonMethods.getTag(this), response.toString());
                try {
                    JSONObject obj = new JSONObject(response.toString());
                    BankNameModel bankNameModel = new Gson().fromJson(obj.toString(), BankNameModel.class);
                    if (bankNameModel.isStatus()) {
                        BankNameList.add("Select Bank Name");
                        for (int i = 0; i < bankNameModel.getBankNameDc().size(); i++) {
                            String bankname = bankNameModel.getBankNameDc().get(i).getBankName();
                            BankNameList.add(bankname);
                        }
                        adapter = new MultipleChequeAdapter(NewOrderPlaceActivity.this, BankNameList, chequePaymentList, orderId, this);
                        mBinding.rvCheque.setAdapter(adapter);
                    } else {
                        Utils.setToast(this, obj.getString("Message"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(this, getResources().getString(R.string.errorString), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, response.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void resetValue(String resetType) {
        if (resetType.equalsIgnoreCase("prepaidAmt")) {
            sCaseAmount = "0";
            sUPIAmount = "0";
            sServiceText = 0;
            paymentThrough = "";
            return;
        } else if (resetType.equalsIgnoreCase("orderCancel")) {
            sCaseAmount = "0";
            sUPIAmount = "0";
            sServiceText = 0;
            return;
        }
    }

    private void setPaymentBtn() {
        // set listeners
        etCaseAmount.addTextChangedListener(mTextWatcher);
        // run once to disable if empty
        checkFieldsForEmptyValues();
    }

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {


        }


        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            // check Fields For Empty Values
            checkFieldsForEmptyValues();
        }
    };

    private void checkFieldsForEmptyValues() {
        try {
            double cashAmnt = 0;
            double chequeAmt = 0;
            double upiAmt = 0;
            sCaseAmount = etCaseAmount.getText().toString();
            if (sCaseAmount != "" && sCaseAmount.length() > 0) {
                cashAmnt = Double.parseDouble(sCaseAmount);

            } else {
                cashAmnt = 0;
            }
            if (sChequeAmount != "" && sChequeAmount.length() > 0) {
                chequeAmt = Double.parseDouble(sChequeAmount);
            } else {
                chequeAmt = 0;
            }

            if (sUPIAmount != "" && sUPIAmount.length() > 0) {
                upiAmt = Double.parseDouble(sUPIAmount);
            } else {
                upiAmt = 0;
            }

            cashAmnt = (grossAmt - (hdfcamount + epayamount + gullakamount + chequeAmt + upiAmt));
            if (cashAmnt > 0 || cashAmnt == 0) {
                etCaseAmount.removeTextChangedListener(mTextWatcher);
                etCaseAmount.setText("" + (int) cashAmnt);
                etCaseAmount.addTextChangedListener(mTextWatcher);
                double totalAmt = cashAmnt + chequeAmt + upiAmt;
                amount = hdfcamount + epayamount + gullakamount + totalAmt;
                // mBinding.tvPaidAmount.setText("" + hdfcamount + epayamount + gullakamount);
                tvReceivedTotalAmt.setText(String.valueOf(totalAmt));
                /*if (amount == grossAmt) {
                    dueAmount = 0.0;
                    mBinding.tvDueAmount.setText(getString(R.string.rs) + dueAmount);
                } else {
                    dueAmount = grossAmt - amount;
                    mBinding.tvDueAmount.setText(getString(R.string.rs) + dueAmount);
                }*/


                if (amount != 0) {
                    if (amount == grossAmt) {
                        btnPayment.setEnabled(true);
                        btnPayment.setBackgroundColor(Color.parseColor("#38a561"));
                        paymentBtnFlag = true;
                    } else {
                        paymentBtnFlag = false;
                        btnPayment.setEnabled(false);
                        btnPayment.setBackgroundColor(Color.parseColor("#828282"));
                    }
                } else {
                    paymentBtnFlag = false;
                    btnPayment.setEnabled(false);
                    btnPayment.setBackgroundColor(Color.parseColor("#828282"));
                }
            } else {
                paymentBtnFlag = false;
                btnPayment.setEnabled(false);
                btnPayment.setBackgroundColor(Color.parseColor("#828282"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void WSCallforOrderPlace(boolean paymentType) {
        if (spStatus.getSelectedItem().toString().equalsIgnoreCase("Delivered")) {

            if (isOtpVerified) {
                try {
                    FetchpaymentData(orderId, paymentType);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                orderPlaceViewModel.hitOrderOTPExist(orderId, spStatus.getSelectedItem().toString(), spComment.getSelectedItem().toString());
            }
        } else {
            if (isOtpVerified) {
                try {
                    SharePrefs.getInstance(this).putInt(SharePrefs.DELIVERY_ISSUANCE_ID, deliveryIssuanceId);
                    SignPaidPopup(paymentType);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                orderPlaceViewModel.hitOrderOTPExist(orderId, spStatus.getSelectedItem().toString(), spComment.getSelectedItem().toString());
            }
        }


    }

    private void intView() {
        utils = new Utils(this);
        commonClassForAPI = CommonClassForAPI.getInstance(this);

        try {
            /**
             * get mobile and  name
             * ***/
            dboyMobileNo = SharePrefs.getInstance(this).getString(SharePrefs.MOBILE);
            dboyName = SharePrefs.getInstance(this).getString(SharePrefs.PEAOPLE_FIRST_NAME);

            /**
             * initialize tv,et,sp and rv
             * **/
            mBinding.swipeContainer.setColorSchemeResources(R.color.colorAccent);
            tvReceivedTotalAmt = mBinding.tvReceivedTotalAmt;
            etCaseAmount = mBinding.etCaseAmount;
            spStatus = mBinding.spStatus;
            spComment = mBinding.spComment;
            spCash=mBinding.spCashCollection;

            btnPayment = mBinding.paymentBtn;
            btnUpdate = mBinding.updateBtn;
            tvTrupay = mBinding.tvTrupay;
            checkDetails = mBinding.checkDetails;
            RecyclerView rvItemDetails = mBinding.rvItem;
            mPosViewLayout = mBinding.llMpasoView;
            mposadapter = new MultiMposAdapter(this, mposPaymentList, orderId, this);
            mBinding.rvMpos.setAdapter(mposadapter);


            mBinding.btnAdd.setOnClickListener(v -> {
                chequePaymentList = adapter.getListFromAdapter();
                boolean isboolean = false;
                if (chequePaymentList.size() > 0) {
                    ArrayList<String> newList = new ArrayList<String>();
                    for (int i = 0; i < chequePaymentList.size(); i++) {
                        if (!newList.contains(chequePaymentList.get(i).getTransId())) {
                            newList.add(chequePaymentList.get(i).getTransId());
                        } else {
                            isboolean = false;
                            Utils.setToast(NewOrderPlaceActivity.this, "Cheque Number should not be same");
                            break;
                        }
                        if (chequePaymentList.get(i).getAmount() == 0) {
                            Utils.setToast(NewOrderPlaceActivity.this, "Enter Cheque Amount");
                            isboolean = false;
                            break;
                        } else if (chequePaymentList.get(i).getChequeBankName() == null || chequePaymentList.get(i).getChequeBankName().equalsIgnoreCase("") || chequePaymentList.get(i).getChequeBankName().equalsIgnoreCase("Select Bank Name")) {
                            isboolean = false;
                            Utils.setToast(NewOrderPlaceActivity.this, getString(R.string.plz_select_Bank_name));
                            break;
                        } else if (chequePaymentList.get(i).getChequeImageUrl() == null || chequePaymentList.get(i).getChequeImageUrl().equalsIgnoreCase("")) {
                            isboolean = false;
                            Utils.setToast(NewOrderPlaceActivity.this, getString(R.string.plz_upload_check_images));
                            break;

                        } else if (chequePaymentList.get(i).getPaymentDate() == null || chequePaymentList.get(i).getPaymentDate().equalsIgnoreCase("")) {
                            isboolean = false;
                            Utils.setToast(NewOrderPlaceActivity.this, getString(R.string.enter_check_date));
                            break;
                        } else if (chequePaymentList.get(i).getTransId() == null || chequePaymentList.get(i).getTransId().equalsIgnoreCase("")) {
                            isboolean = false;
                            Utils.setToast(NewOrderPlaceActivity.this, getString(R.string.plz_enter_chequeno));
                            break;

                        } else {
                            isboolean = true;
                        }
                    }
                    if (isboolean) {
                        chequePaymentList.add(new DeliveryPayments(0, "", 0, "Cheque", "", "", ""));
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    chequePaymentList.add(new DeliveryPayments(0, "", 0, "Cheque", "", "", ""));
                    adapter.notifyDataSetChanged();
                }

            });
            mBinding.btnMposadd.setOnClickListener(v -> {
                mposPaymentList = mposadapter.getListFromAdapter();
                boolean ismposboolean = false;
                if (mposPaymentList.size() > 0) {
                    ArrayList<String> newList = new ArrayList<String>();
                    for (int i = 0; i < mposPaymentList.size(); i++) {
                        if (!newList.contains(mposPaymentList.get(i).getTransId())) {
                            newList.add(mposPaymentList.get(i).getTransId());
                        } else {
                            ismposboolean = false;
                            Utils.setToast(NewOrderPlaceActivity.this, "Mpos Number should not be same");
                            break;
                        }
                        if (mposPaymentList.get(i).getTransId() == null || mposPaymentList.get(i).getTransId().equalsIgnoreCase("")) {
                            ismposboolean = false;
                            Utils.setToast(NewOrderPlaceActivity.this, getString(R.string.enter_reference_id));
                            break;
                        } else if (mposPaymentList.get(i).getTransId().length() < 8) {
                            ismposboolean = false;
                            Utils.setToast(NewOrderPlaceActivity.this, getString(R.string.enter_reference_mpos_id));
                            break;
                        } else if (mposPaymentList.get(i).getAmount() == 0) {
                            ismposboolean = false;
                            Utils.setToast(NewOrderPlaceActivity.this, "Please Enter Mpos Amount");
                            break;
                        } else {
                            ismposboolean = true;
                        }
                    }

                    if (ismposboolean) {
                        mposPaymentList.add(new DeliveryPayments(0, "",
                                0, "mPos", "", "", ""));
                        mposadapter.notifyDataSetChanged();
                    }

                } else {
                    mposPaymentList.add(new DeliveryPayments(0, "", 0, "mPos", "", "", ""));
                    mposadapter.notifyDataSetChanged();
                }
            });
            mBinding.rvCheque.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            mBinding.rvMpos.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            mBinding.swipeContainer.setOnRefreshListener(() -> {
                CallAPI();
            });

            if (colorCode != null && !colorCode.isEmpty()) {
                mBinding.tvCustName.setTextColor(Color.parseColor("" + colorCode));
            } else {
                mBinding.tvCustName.setTextColor(Color.BLACK);
            }
            // set sp value
            ArrayAdapter statusAdapter = ArrayAdapter.createFromResource(this, R.array.status, android.R.layout.simple_spinner_dropdown_item);
            spStatus.setAdapter(statusAdapter);
            ArrayAdapter commentAdapter = ArrayAdapter.createFromResource(this, R.array.comment, android.R.layout.simple_spinner_dropdown_item);
            spComment.setAdapter(commentAdapter);

            /**
             * set items value
             * **/
            rvItemDetails.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            rvItemDetails.setHasFixedSize(true);
            myItemDetailsAdapter = new MyItemDetailsAdapter(this, itemDetailsList);
            rvItemDetails.setAdapter(myItemDetailsAdapter);
            /**
             * Spinner click listener
             * **/
            spStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String sSpStatus = spStatus.getSelectedItem().toString();
                    if (sSpStatus.equalsIgnoreCase("Delivery Canceled") || sSpStatus.equalsIgnoreCase("Delivery Redispatch")) {
                        // spStatus.setBackgroundColor(ContextCompat.getColor(activity,R.color.green));
                        View v = spStatus.getSelectedView();
                        ((TextView) v).setTextColor(ContextCompat.getColor(NewOrderPlaceActivity.this, R.color.green));
                        hideShowView(true, sSpStatus);

                        return;
                    } else if (trupay.equalsIgnoreCase("true")) {

                        if (sSpStatus.equalsIgnoreCase("Delivered")) {
                            View v = spStatus.getSelectedView();

                            ((TextView) v).setTextColor(ContextCompat.getColor(NewOrderPlaceActivity.this, R.color.green));
                            ArrayAdapter commentAdapter = ArrayAdapter.createFromResource(NewOrderPlaceActivity.this, R.array.delivered_comment, android.R.layout.simple_spinner_dropdown_item);
                            spComment.setAdapter(commentAdapter);
                        } else {

                            ArrayAdapter commentAdapter = ArrayAdapter.createFromResource(NewOrderPlaceActivity.this, R.array.comment_delivery_redispatch, android.R.layout.simple_spinner_dropdown_item);
                            spComment.setAdapter(commentAdapter);
                        }
                        return;
                    } else if (sSpStatus.equalsIgnoreCase("Delivered")) {
                        View v = spStatus.getSelectedView();
                        ((TextView) v).setTextColor(ContextCompat.getColor(NewOrderPlaceActivity.this, R.color.green));
                        hideShowView(false, sSpStatus);
                        return;
                    }



                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


            ArrayAdapter cashAdaptor= ArrayAdapter.createFromResource(this,R.array.cash_collection, R.layout.spinner_text);
            spCash.setMinimumHeight(30);
            spCash.setAdapter(cashAdaptor);
            spCash.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    String spcash = spCash.getSelectedItem().toString();
                    if(spcash.equals("Credit"))
                    {
                        credit_status=true;
                    }
                    else
                    {
                        credit_status=false;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * hide layout
     **/
    private void hideShowView(boolean huValue, String sSpStatus) {
        if (huValue) {
            mBinding.paymentLl.setVisibility(View.GONE);
            btnPayment.setVisibility(View.GONE);
            btnUpdate.setVisibility(View.VISIBLE);

            if (sSpStatus.equalsIgnoreCase("Delivery Canceled")) {
                ArrayAdapter commentAdapter = ArrayAdapter.createFromResource(this, R.array.comment_delivery_cancel, android.R.layout.simple_spinner_dropdown_item);
                spComment.setAdapter(commentAdapter);
            } else {
                ArrayAdapter commentAdapter = ArrayAdapter.createFromResource(this, R.array.comment_delivery_redispatch, android.R.layout.simple_spinner_dropdown_item);
                spComment.setAdapter(commentAdapter);
            }
        } else {
            ArrayAdapter commentAdapter = ArrayAdapter.createFromResource(this, R.array.delivered_comment, android.R.layout.simple_spinner_dropdown_item);
            spComment.setAdapter(commentAdapter);
            mBinding.paymentLl.setVisibility(View.VISIBLE);
            btnPayment.setVisibility(View.VISIBLE);
            btnUpdate.setVisibility(View.GONE);
            if (paymentBtnFlag) {
                btnPayment.setEnabled(true);
                btnPayment.setBackgroundColor(Color.parseColor("#38a561"));
                paymentBtnFlag = true;
            } else {
                paymentBtnFlag = false;
                btnPayment.setEnabled(false);
                btnPayment.setBackgroundColor(Color.parseColor("#828282"));
            }
        }
        spComment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String sSpComment = spComment.getSelectedItem().toString();
                if (!sSpComment.equalsIgnoreCase("select")) {
                    View v = spComment.getSelectedView();
                    ((TextView) v).setTextColor(ContextCompat.getColor(NewOrderPlaceActivity.this, R.color.green));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
                Utils.setToast(this, getResources().getString(R.string.errorString));
                break;

            default:
                break;
        }
    }


    /*
     * method to handle response
     * */
    private void consumeResponseForOrderPlace(ApiResponse apiResponse) {
        switch (apiResponse.status) {
            case LOADING:
                progressDialog.show();
                break;
            case SUCCESS:
                progressDialog.dismiss();
                renderSuccessResponseForOrderPlaced(apiResponse.data);
                break;
            case ERROR:
                progressDialog.dismiss();
                Utils.setToast(this, getResources().getString(R.string.errorString));
                break;
            default:
                break;
        }
    }


    private void consumeResponseForchequePermission(ApiResponse apiResponse) {
        switch (apiResponse.status) {
            case LOADING:
                mBinding.proRelatedItem.setVisibility(View.VISIBLE);
                break;
            case SUCCESS:
                mBinding.proRelatedItem.setVisibility(View.GONE);
                renderSuccessResponseForchequePermission(apiResponse.data);
                break;
            case ERROR:
                mBinding.proRelatedItem.setVisibility(View.GONE);
                Utils.setToast(this, getResources().getString(R.string.errorString));
                break;
            default:
                break;
        }
    }


    private void renderSuccessResponseForchequePermission(JsonElement response) {
        mBinding.proRelatedItem.setVisibility(View.GONE);
        if (Utils.isJSONValid(response.toString())) {
            if (!response.isJsonNull()) {
                try {
                    JSONObject obj = new JSONObject(response.toString());
                    ChequePermissionModel model = new Gson().fromJson(obj.toString(), ChequePermissionModel.class);
                    ischeque = model.isChequeAccepted();
                    msg = model.getMsg();
                    chequeLimit = (int) model.getChequeLimit();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(this, getResources().getString(R.string.errorString), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, response.toString(), Toast.LENGTH_SHORT).show();
        }
    }


    /*
     * method to handle success response
     * */
    private void renderSuccessResponse(JsonElement response) {
        dueAmount = 0.0;
        hdfcamount = 0.0;
        cashAmt = 0.0;
        epayamount = 0.0;
        gullakamount = 0.0;
        chequePaymentList.clear();
        mposPaymentList.clear();
        if (Utils.isJSONValid(response.toString())) {
            mBinding.swipeContainer.setRefreshing(false);
            if (!response.isJsonNull()) {
                int totalItemQty = 0;
                Logger.e(CommonMethods.getTag(this), response.toString());
                try {
                    JSONObject obj = new JSONObject(response.toString());
                    OrderDetailsModel orderDetailsModel = new Gson().fromJson(obj.toString(), OrderDetailsModel.class);
                    if (orderDetailsModel.isStatus()) {
                        if (orderDetailsModel.getOrderDispatchedObj().getPaymentMode().equals("CCAvenue")) {
                            mBinding.tvCashOption.setText("Online Payment");
                            mBinding.spCashCollection.setVisibility(View.GONE);
                        } else if (orderDetailsModel.getOrderDispatchedObj().getPaymentMode().equals("ePaylater")) {
                            mBinding.tvCashOption.setText("Online Payment");
                            mBinding.spCashCollection.setVisibility(View.GONE);
                        } else
                        {
                            mBinding.spCashCollection.setVisibility(View.VISIBLE);
                            mBinding.tvCashOption.setText("Cash Payment");
                        }

                        mBinding.tvReceivedTotalAmt.setText("0.0");
                        redispatch = orderDetailsModel.getOrderDispatchedObj().getRedispatchcount();
                        deliveryIssuanceId = orderDetailsModel.getOrderDispatchedObj().getDeliveryissuanceid();
                        orderDispatchedMasterId = orderDetailsModel.getOrderDispatchedObj().getOrderdispatchedmasterid();
                        warehouseId = orderDetailsModel.getOrderDispatchedObj().getWarehouseid();
                        customerId = orderDetailsModel.getOrderDispatchedObj().getCustomerid();
                        orderPlaceViewModel.verifyChequePermission(customerId);
                        /**
                         *At Truepay condition time
                         ****/
                        // orderDetailsModel.getOrderDispatchedObj().setTrupay("true");

                        mBinding.tvCustName.setText(orderDetailsModel.getOrderDispatchedObj().getCustomername());
                        mBinding.tvCustAddress.setText(orderDetailsModel.getOrderDispatchedObj().getShippingaddress());
                        mBinding.totalPrice.setText("" + orderDetailsModel.getOrderDispatchedObj().getGrossamount());
                        mBinding.tvTotalamnt.setText("" + orderDetailsModel.getOrderDispatchedObj().getGrossamount());
                        double amount = 0.0;
                        grossAmt = orderDetailsModel.getOrderDispatchedObj().getGrossamount();

                        for (int i = 0; i < orderDetailsModel.getPayment().size(); i++) {
                            if (orderDetailsModel.getPayment().get(i).getPaymentFrom().equalsIgnoreCase("Cash")) {
                                mBinding.tvTotalPrice.setText(getString(R.string.rs) + orderDetailsModel.getPayment().get(i).getAmount());
                                dueAmount = dueAmount + orderDetailsModel.getPayment().get(i).getAmount();
                                etCaseAmount.setText("" + (int) orderDetailsModel.getPayment().get(i).getAmount());
                                cashAmt = orderDetailsModel.getPayment().get(i).getAmount();
                            } else if (orderDetailsModel.getPayment().get(i).getPaymentFrom().equalsIgnoreCase("hdfc")) {
                                hdfcamount = hdfcamount + orderDetailsModel.getPayment().get(i).getAmount();
                            } else if (orderDetailsModel.getPayment().get(i).getPaymentFrom().equalsIgnoreCase("Gullak")) {
                                gullakamount = gullakamount + orderDetailsModel.getPayment().get(i).getAmount();
                            } else if (orderDetailsModel.getPayment().get(i).getPaymentFrom().equalsIgnoreCase("ePaylater")) {
                                mBinding.etEpaylater.setText(orderDetailsModel.getPayment().get(i).getAmount() + "");
                                mBinding.epayRefno.setText(orderDetailsModel.getPayment().get(i).getGatewayTransId() + "");
                                epayamount = epayamount + orderDetailsModel.getPayment().get(i).getAmount();
                            } else if (orderDetailsModel.getPayment().get(i).getPaymentFrom().equalsIgnoreCase("Cheque")) {
                                cheque_Amount = cheque_Amount + orderDetailsModel.getPayment().get(i).getAmount();
                                chequePaymentList.add(new DeliveryPayments(orderDetailsModel.getPayment().get(i).getOrderId(), orderDetailsModel.getPayment().get(i).getGatewayTransId(), (int) orderDetailsModel.getPayment().get(i).getAmount(), "Cheque", orderDetailsModel.getPayment().get(i).getChequeImageUrl(), orderDetailsModel.getPayment().get(i).getChequeBankName(), ""));
                                dueAmount = dueAmount + orderDetailsModel.getPayment().get(i).getAmount();
                            } else if (orderDetailsModel.getPayment().get(i).getPaymentFrom().equalsIgnoreCase("mPos")) {
                                mposPaymentList.add(new DeliveryPayments(orderDetailsModel.getPayment().get(i).getOrderId(), orderDetailsModel.getPayment().get(i).getGatewayTransId(), (int) orderDetailsModel.getPayment().get(i).getAmount(), "mPos", "", "", ""));
                                mpos_Amount = mpos_Amount + orderDetailsModel.getPayment().get(i).getAmount();
                                dueAmount = dueAmount + orderDetailsModel.getPayment().get(i).getAmount();
                            }
                        }
                        mBinding.tvPaidAmount.setText("" + Math.round(hdfcamount + epayamount + gullakamount));
                        mBinding.tvDueAmount.setText(getString(R.string.rs) + dueAmount);

                        if (chequePaymentList.size() > 0) {
                            mBinding.checkDetails.setVisibility(View.VISIBLE);
                            mBinding.rvCheque.setAdapter(adapter);
                            adapter.notifyDataSetChanged();

                        } else {
                            mBinding.checkDetails.setVisibility(View.GONE);
                        }
                        if (mposPaymentList.size() > 0) {
                            mBinding.UTRDetail.setVisibility(View.VISIBLE);
                            mBinding.rvMpos.setAdapter(mposadapter);
                            mposadapter.notifyDataSetChanged();

                        } else {
                            mBinding.UTRDetail.setVisibility(View.GONE);
                        }

                        /* if (dueAmount == 0) {*/
                        if ((hdfcamount + epayamount + gullakamount) == grossAmt) {
                            trupay = "true";
                            tvTrupay.setVisibility(View.VISIBLE);
                            mPosViewLayout.setVisibility(View.GONE);
                            ArrayAdapter statusAdapter = ArrayAdapter.createFromResource(this, R.array.status_online, android.R.layout.simple_spinner_dropdown_item);
                            spStatus.setAdapter(statusAdapter);
                            hideShowView(true, "trupay");

                        } else if ((hdfcamount + epayamount) != grossAmt && orderDetailsModel.getOrderDispatchedObj().getRedispatchcount() == 5) {
                            ArrayAdapter statusAdapter = ArrayAdapter.createFromResource(this, R.array.Redispatch_status, android.R.layout.simple_spinner_dropdown_item);
                            spStatus.setAdapter(statusAdapter);
                        }

                        for (int i = 0; i < orderDetailsModel.getOrderDispatchedObj().getOrderDetails().size(); i++) {
                            totalItemQty += orderDetailsModel.getOrderDispatchedObj().getOrderDetails().get(i).getQty();
                        }
                        mBinding.totalQuantity.setText(String.valueOf(totalItemQty));
                        myItemDetailsAdapter.setItemDetailsList(orderDetailsModel.getOrderDispatchedObj().getOrderDetails());

                        /***
                         * set payment btn
                         * **/
                        setPaymentBtn();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(this, getResources().getString(R.string.errorString), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, response.toString(), Toast.LENGTH_SHORT).show();
        }
    }


    /*
     * method to handle success response
     * */
    private void renderSuccessResponseForOrderPlaced(JsonElement response) {
        if (Utils.isJSONValid(response.toString())) {
            if (!response.isJsonNull()) {
                mBinding.proRelatedItem.setVisibility(View.GONE);
                customDialog.dismiss();
                Logger.d(CommonMethods.getTag(this), response.toString());
                try {
                    OrderResponsesModel orderResponsesModel = new Gson().fromJson(response, OrderResponsesModel.class);
                    if (orderResponsesModel.isStatus()) {
                        //    UpdateMyTaskStatus();
                        Utils.setToast(this, getResources().getString(R.string.order_placed_done));
                        finish();
                    } else {
                        Utils.setToast(this, getResources().getString(R.string.errorString));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(this, getResources().getString(R.string.errorString), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, response.toString(), Toast.LENGTH_SHORT).show();
        }
    }


    // CustomerInfoAPI
    private void FetchCustinfo(DisposableObserver observer, int CustomerId) {
        RestClient.getInstance().getService().getcustInfo(CustomerId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String object) {
                        observer.onNext(object);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("TAG", "Error:" + e.toString());
                        observer.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        observer.onComplete();

                    }
                });
    }

    private void FetchpaymentData(int orderid, boolean paymentType) {
        RestClient.getInstance().getService().checkPayment(orderid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Double>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Double response) {
                        Utils.hideProgressDialog(NewOrderPlaceActivity.this);
                        if (!TextUtils.isNullOrEmpty(response.toString())) {
                            System.out.println("response" + response);
                            givenTotalAmount = Integer.valueOf(response.intValue());
                            double TotalenteredAmount = Double.parseDouble(mBinding.tvReceivedTotalAmt.getText().toString());
                            if (givenTotalAmount == (TotalenteredAmount - (cheque_Amount + mpos_Amount))) {
                                SharePrefs.getInstance(NewOrderPlaceActivity.this).putInt(SharePrefs.DELIVERY_ISSUANCE_ID, deliveryIssuanceId);
                                SignPaidPopup(paymentType);
                            } else {
                                mBinding.etCaseAmount.setText("");
                                sChequeAmount = "";
                                sUPIAmount = "";
                                CallAPI();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("TAG", "Error:" + e.toString());

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    /**
     * Order place popup
     ***/
    private void orderPlacedPopup() {
        if (isOtpVerified) {
            final View mView = getLayoutInflater().inflate(R.layout.placeorder_popup, null);
            Dialog customDialog = new Dialog(this, R.style.CustomDialog);
            customDialog.setContentView(mView);
            TextView okBtn = mView.findViewById(R.id.ok_btn);
            TextView cancelBtn = mView.findViewById(R.id.cancel_btn);
            okBtn.setOnClickListener(v -> {
                customDialog.dismiss();
                Intent i = new Intent(this, ScanCodeActivity.class);
                i.putExtra("orderID", orderId);
                i.putExtra("type", "payment");
                startActivityForResult(i, 2);
                overridePendingTransition(0, 0);
            });
            cancelBtn.setOnClickListener(v -> customDialog.dismiss());
            customDialog.show();
        } else {
            orderPlaceViewModel.hitOrderOTPExist(orderId, spStatus.getSelectedItem().toString(), spComment.getSelectedItem().toString());
        }
    }

    /**
     * Cust info Alert Popup
     * *
     *
     * @param response
     */
    private void CustInfoPopup(String response) {
        LayoutInflater inflater = getLayoutInflater();
        final View mView = inflater.inflate(R.layout.cust_info_popup, null);
        Dialog customDialog = new Dialog(this, R.style.CustomDialog);
        customDialog.setContentView(mView);
        TextView okBtn = mView.findViewById(R.id.tv_ok);
        TextView msg = mView.findViewById(R.id.tv_msg);
        msg.setText(response);
        okBtn.setOnClickListener(v -> customDialog.dismiss());
        customDialog.show();
    }


    //custinfo response
    private DisposableObserver<String> getCustInfo = new DisposableObserver<String>() {
        @Override
        public void onNext(String response) {
            try {
                Utils.hideProgressDialog(NewOrderPlaceActivity.this);
                if (!TextUtils.isNullOrEmpty(response)) {
                 //   CustInfoPopup(response);
                }
            } catch (Exception e) {
                e.printStackTrace();

            }
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
            Utils.hideProgressDialog(NewOrderPlaceActivity.this);
        }

        @Override
        public void onComplete() {
            Utils.hideProgressDialog(NewOrderPlaceActivity.this);
        }
    };


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("onResult-ActivityMain", String.valueOf(requestCode));
        String barcoderesult;
        if (requestCode == 2) {
            try {
                barcoderesult = data.getStringExtra("result");
                if (barcoderesult.equalsIgnoreCase(String.valueOf(orderId))) {
                    WSCallforOrderPlace(false);
                } else {
                    Toast.makeText(this, "Barcode Does not Match with Order ID", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == 200 && resultCode == Activity.RESULT_OK) {
            mBinding.CropView.setVisibility(View.VISIBLE);
            mBinding.rlOrder.setVisibility(View.GONE);
            Uri imageUri = CropImage.getPickImageResultUri(this, data);
            boolean requirePermissions = false;
            if (CropImage.isReadExternalStoragePermissionsRequired(this, imageUri)) {
                requirePermissions = true;
                mCropImageUri = imageUri;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
                }
            }
            if (!requirePermissions) {
                mBinding.CropImageView.setImageUriAsync(imageUri);
            }
        }
    }

    // timerMethods
    @Override
    public void onStart() {
        super.onStart();
        if (Log.isLoggable(TAG, Log.VERBOSE)) {
            Log.v(TAG, "Starting and binding service");
        }
    }

    /**
     * Signature popup
     * *
     */
    private void SignPaidPopup(boolean paymentType) {
        SignaturePopupBinding signaturePopupBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.signature_popup, null, false);
        Dialog customDialog = new Dialog(this, R.style.CustomDialog);
        customDialog.setContentView(signaturePopupBinding.getRoot());
        saveSignBtn = signaturePopupBinding.saveButton;
        Button clearSignBtn = signaturePopupBinding.clearButton;
        mSignaturePad = signaturePopupBinding.signaturePad;

        saveSignBtn.setEnabled(false);
        clearSignBtn.setEnabled(false);

        signaturePopupBinding.toolbar.skip.setEnabled(true);
        signaturePopupBinding.toolbar.skip.setClickable(true);
        mSignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {

            @Override
            public void onStartSigning() {

            }

            @Override
            public void onSigned() {
                saveSignBtn.setBackgroundResource(R.drawable.drawable_corner_white);
                clearSignBtn.setBackgroundResource(R.drawable.orangerectangle);
                saveSignBtn.setEnabled(true);
                clearSignBtn.setEnabled(true);
            }

            @Override
            public void onClear() {
                saveSignBtn.setBackgroundResource(R.drawable.drawable_corner_green);
                clearSignBtn.setBackgroundResource(R.drawable.diable_view);
                saveSignBtn.setEnabled(false);
                clearSignBtn.setEnabled(false);
            }
        });

        saveSignBtn.setOnClickListener(v -> {
            try {
                saveSignBtn.setClickable(false);
                saveSignBtn.setEnabled(false);

                Bitmap signatureBitmap = mSignaturePad.getSignatureBitmap();
                if (signatureBitmap != null) {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    signatureBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byteArraySing = stream.toByteArray();
                }
                String str = Base64.encodeToString(byteArraySing, Base64.DEFAULT);
                Log.e("strConvert:::", str);
                Log.e("Fragment::", "ByteArray::" + byteArraySing);
                UpdateAPICall(paymentType);

                Toast.makeText(this, "Signature saved into the Gallery", Toast.LENGTH_SHORT).show();
                customDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        signaturePopupBinding.toolbar.back.setOnClickListener(View -> customDialog.dismiss());
        signaturePopupBinding.toolbar.skip.setOnClickListener(View -> {
            //customDialog.dismiss();
            signaturePopupBinding.toolbar.skip.setEnabled(false);
            signaturePopupBinding.toolbar.skip.setClickable(false);
            UpdateAPICall(paymentType);
            customDialog.dismiss();

        });
        clearSignBtn.setOnClickListener(View -> {
            mSignaturePad.clear();
        });
        customDialog.show();
    }

    private void UpdateAPICall(boolean paymentType) {
        if (isOtpVerified) {
            if (!sCaseAmount.equalsIgnoreCase("")) {
                paymentThrough = "cash";
            }
            if (paymentType) {
                if (spStatus.getSelectedItem().toString().equalsIgnoreCase("Delivered")) {
                    postPaymentList = new ArrayList<>();
                    paymentThrough = "";

                }
            } else {
                if (spStatus.getSelectedItem().toString().equalsIgnoreCase("Delivered")) {
                    postPaymentList.addAll(chequePaymentList);
                    postPaymentList.addAll(mposPaymentList);
                } else {
                    postPaymentList = new ArrayList<>();
                }

            }
            OrderPlacedModel orderPlacedModel = new OrderPlacedModel(orderDispatchedMasterId, orderId,
                    spStatus.getSelectedItem().toString(), spComment.getSelectedItem().toString(),
                    deliveryIssuanceId, sCaseAmount, dueAmount, sServiceText, "", String.valueOf(dboyMobileNo),
                    dboyName, redispatch, warehouseId, paymentThrough, Utils.getLat(this), Utils.getLog(this), postPaymentList);
            Gson gson = new Gson();
            System.out.println("ChequeJson" + gson.toJson(orderPlacedModel));

            if (!Utils.checkInternetConnection(this)) {
                Utils.setToast(this, getResources().getString(R.string.network_error));
            } else {
                orderPlaceViewModel.hitOrderPlacedApi(orderPlacedModel);
            }
        } else {
            orderPlaceViewModel.hitOrderOTPExist(orderId, spStatus.getSelectedItem().toString(), spComment.getSelectedItem().toString());
        }
    }


    @Override
    public void onImageClick(ImageView imageView, int position) {
        this.chequeimage = imageView;
        this.position = position;
        fCheckName = orderId + "_" + System.currentTimeMillis() + "CheckNo.jpg";
        requestWritePermission();
    }


    @Override
    public void onAmountChange(ArrayList<DeliveryPayments> payment) {
        chequeAmount = 0;
        for (int i = 0; i < payment.size(); i++) {
            if (payment.get(i).getPaymentFrom().equalsIgnoreCase("Cheque")) {
                chequeAmount = chequeAmount + payment.get(i).getAmount();
            }
        }
        sChequeAmount = String.valueOf(chequeAmount);
        checkFieldsForEmptyValues();
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
            if (handler == null) {
                handler = new Handler();
            }
            if (customRunnable != null) {
                customRunnable = null;
            }
            customRunnable = new CustomRunnable(handler, timer, 10000);
            handler.removeCallbacks(customRunnable);
            customRunnable.holder = timer;
            customRunnable.millisUntilFinished = millse; //Current time - received time
            handler.postDelayed(customRunnable, 1000);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mBinding.CropImageView.setOnSetImageUriCompleteListener(null);
    }

    private void onCropImageClick() {
        mBinding.cropBtn.setOnClickListener(view -> {
            Bitmap cropped = mBinding.CropImageView.getCroppedImage();
            chequeimage.setImageBitmap(cropped);
            SavedImages(cropped);
            mBinding.CropView.setVisibility(View.GONE);
            mBinding.rlOrder.setVisibility(View.VISIBLE);
        });
    }

    private void SavedImages(Bitmap bm) {
        String root = Environment.getExternalStorageDirectory().toString();
        try {
            File myDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/ShopKirana");
            myDir.mkdirs();
            File file = new File(myDir, fCheckName);
            if (file.exists())
                file.delete();
            FileOutputStream out = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            uploadFilePath = root + "/ShopKirana/" + fCheckName;
            uploadMultipart();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Upload images
     **/
    public void uploadMultipart() {
        try {
            final File fileToUpload = new File(uploadFilePath);
            Disposable subscribe = new Compressor(this)
                    .compressToFileAsFlowable(fileToUpload)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(file -> {
                        compressedImage = file;
                        RequestBody fileReqBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                        MultipartBody.Part part = MultipartBody.Part.createFormData("file", fileToUpload.getName(), fileReqBody);
                        UploadChequeImage(getimageResponse, part);
                        // uploadImagePath();
                    }, throwable -> {
                        throwable.printStackTrace();
                        showError(throwable.getMessage());
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showError(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    private void UploadChequeImage(DisposableObserver observer, MultipartBody.Part image) {
        RestClient.getInstance().getService().UploadCheque(image)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String object) {
                        observer.onNext(object);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("TAG", "Error:" + e.toString());
                        observer.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        observer.onComplete();

                    }
                });
    }

    private DisposableObserver<String> getimageResponse = new DisposableObserver<String>() {
        @Override
        public void onNext(String response) {
            try {
                Utils.hideProgressDialog(NewOrderPlaceActivity.this);
                if (response != null) {

                    ChequeimgUrl = response.replace("\"", "");
                    adapter.paymentList.get(position).setChequeImageUrl(ChequeimgUrl);
                    adapter.notifyDataSetChanged();
                    Log.e("Success", ChequeimgUrl);

                } else {
                    Log.e("Failed", "Failed ####  " + response);
                    Picasso.get().load(R.drawable.ic_add_image_icon).placeholder(R.drawable.ic_add_image_icon).into(chequeimage);
                    Toast.makeText(NewOrderPlaceActivity.this, "Image Not Uploaded", Toast.LENGTH_SHORT).show();

                }

            } catch (Exception e) {
                e.printStackTrace();

            }
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
            Utils.hideProgressDialog(NewOrderPlaceActivity.this);
        }

        @Override
        public void onComplete() {
            Utils.hideProgressDialog(NewOrderPlaceActivity.this);
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        if (requestCode == 0) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                CropImage.startPickImageActivity(this);
            } else {

                Toast.makeText(this, "Access Denid", Toast.LENGTH_SHORT).show();
            }
            return;

        }
        if (mBinding.CropImageView != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            mBinding.CropImageView.setImageUriAsync(mCropImageUri);

        }
    }


    @Override
    public void onSetImageUriComplete(CropImageView view, Uri uri, Exception error) {
        if (error != null) {
            Log.e("Crop", "Failed to load image for cropping", error);
            Toast.makeText(this, "Something went wrong, try again", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            // customRunnable.stop();
            handler.removeCallbacks(customRunnable);
            customRunnable = null;
            handler = null;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void setActionBarConfiguration() {
        if (type == null) {
            mBinding.tvTimmer.setVisibility(View.VISIBLE);
            timer(time, mBinding.tvTimmer);

        } else {
            mBinding.tvTimmer.setVisibility(View.GONE);
        }
        mBinding.tvOrderno.setText(NewOrderPlaceActivity.this.getString(R.string.OrderID) + orderId);
        mBinding.tvAssitId.setText(this.getString(R.string.assignment_id) + deliveryIssuanceId);
        mBinding.ivBack.setOnClickListener(view -> onBackPressed());
    }

    private void consumeCheckOrderOtpResponse(ApiResponse apiResponse) {
        switch (apiResponse.status) {
            case LOADING:
                mBinding.proRelatedItem.setVisibility(View.VISIBLE);
                break;
            case SUCCESS:
                mBinding.proRelatedItem.setVisibility(View.GONE);
                showOtpDialog(apiResponse.data.getAsBoolean());
                if (!apiResponse.data.getAsBoolean()) {
                    orderPlaceViewModel.generateOrderOTP(orderId, spStatus.getSelectedItem().toString(), lat, lg);
                }
                break;
            case ERROR:
                mBinding.proRelatedItem.setVisibility(View.GONE);
                Utils.setToast(this, getResources().getString(R.string.errorString));
                break;
            default:
                break;
        }
    }

    private void consumeOrderOtpGenerateResponse(ApiResponse apiResponse) {
        switch (apiResponse.status) {
            case LOADING:
                mBinding.proRelatedItem.setVisibility(View.VISIBLE);
                break;
            case SUCCESS:
                mBinding.proRelatedItem.setVisibility(View.GONE);
                Utils.setToast(this, "OTP sent successfully!");
                break;
            case ERROR:
                mBinding.proRelatedItem.setVisibility(View.GONE);
                Utils.setToast(this, getResources().getString(R.string.errorString));
                break;
            default:
                break;
        }
    }

    private void consumeVerifyOrderOtpResponse(ApiResponse apiResponse) {
        switch (apiResponse.status) {
            case LOADING:
                mBinding.proRelatedItem.setVisibility(View.VISIBLE);
                break;
            case SUCCESS:
                mBinding.proRelatedItem.setVisibility(View.GONE);
                if (apiResponse.data.getAsBoolean()) {
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    isOtpVerified = true;
                    if (mBinding.updateBtn.getVisibility() == View.VISIBLE) {
                        mBinding.updateBtn.callOnClick();
                    }
                    if (mBinding.paymentBtn.getVisibility() == View.VISIBLE) {
                        orderPlacedPopup();
                    }
                } else {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    Utils.setToast(this, getResources().getString(R.string.enter_correct_otp));
                }
                break;
            case ERROR:
                mBinding.proRelatedItem.setVisibility(View.GONE);
                Utils.setToast(this, getResources().getString(R.string.errorString));
                break;
            default:
                break;
        }
    }

    private void showOtpDialog(boolean isOtpSent) {
        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_order_otp, null);
        dialog = new BottomSheetDialog(this);
        dialog.setContentView(view);
        AppCompatImageView ivClose = dialog.findViewById(R.id.iv_close);
        TextInputLayout tiOtp = dialog.findViewById(R.id.ti_otp);
        AppCompatEditText etOtp = dialog.findViewById(R.id.et_otp);
        AppCompatButton btnVerify = dialog.findViewById(R.id.btn_verify);
        AppCompatTextView tvTime = dialog.findViewById(R.id.tv_time);
        AppCompatButton btnResend = dialog.findViewById(R.id.btn_resend);
//        if (!isOtpSent) {
//            btnResend.setEnabled(false);
//        }

        CountDownTimer timer = new CountDownTimer(60 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                //Convert milliseconds into hour,minute and seconds
                String hms = String.format("%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                tvTime.setText("Resend OTP in " + hms);
            }

            @Override
            public void onFinish() {
                tvTime.setText(""); //On finish change timer text
                btnResend.setEnabled(true);
                cancel();
            }
        };

        btnVerify.setOnClickListener(v -> {
            String otp = etOtp.getText().toString();

            if (android.text.TextUtils.isEmpty(otp)) {
                tiOtp.setError(getResources().getString(R.string.enteotp));
            } else {
                orderPlaceViewModel.verifyOrderOTP(orderId, spStatus.getSelectedItem().toString(), otp);
                progressDialog = new ProgressDialog(this);
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Verifying");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();
            }
        });
        btnResend.setOnClickListener(v -> {
            orderPlaceViewModel.generateOrderOTP(orderId, spStatus.getSelectedItem().toString(), lat, lg);
            btnResend.setEnabled(false);
            timer.start();
        });
        ivClose.setOnClickListener(v -> {
            dialog.dismiss();
            btnPayment.setClickable(true);
            btnUpdate.setClickable(true);
            btnPayment.setEnabled(true);
            btnUpdate.setEnabled(true);
        });
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    public void onMposAmountChange(ArrayList<DeliveryPayments> payment) {
        mposAmount = 0;
        for (int i = 0; i < payment.size(); i++) {
            if (payment.get(i).getPaymentFrom().equalsIgnoreCase("mPos")) {
                mposAmount = mposAmount + payment.get(i).getAmount();
            }
        }
        sUPIAmount = String.valueOf(mposAmount);
        checkFieldsForEmptyValues();
    }


}