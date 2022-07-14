package in.moreyeahs.livspace.delivery.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonElement;
import in.moreyeahs.livspace.delivery.api.RestClient;
import in.moreyeahs.livspace.delivery.model.OrderPlacedModel;
import in.moreyeahs.livspace.delivery.utilities.ApiResponse;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class OrderPlaceViewModel extends ViewModel {
    private final CompositeDisposable disposablesa = new CompositeDisposable();
    private final CompositeDisposable disposables = new CompositeDisposable();
    private MutableLiveData<ApiResponse> myOrderDetailsMutableLiveData;
    private MutableLiveData<ApiResponse> myOrderPlacedResponseMutableLiveData;
    private MutableLiveData<ApiResponse> postDeclineOrderMutableLiveData;
    private MutableLiveData<ApiResponse> bankNameResponseLiveData;
    private MutableLiveData<ApiResponse> hdfcinfoLivedata;
    private MutableLiveData<ApiResponse> RSAKeyLivedata;
    private MutableLiveData<ApiResponse> checkOrderOtpExistLiveData;
    private MutableLiveData<ApiResponse> generateOrderOtpLiveData;
    private MutableLiveData<ApiResponse> validateOrderOtpLiveData;
    private MutableLiveData<ApiResponse> validateChequepermissionLiveData;
    private MutableLiveData<ApiResponse> DeclineOrderDetailLiveData;

    public OrderPlaceViewModel() {

    }


    public LiveData<ApiResponse> getOrderDetailsData() {
        if (myOrderDetailsMutableLiveData == null) {
            myOrderDetailsMutableLiveData = new MutableLiveData<>();
        }
        if (myOrderDetailsMutableLiveData.getValue() != null && myOrderDetailsMutableLiveData.getValue().data != null) {
            myOrderDetailsMutableLiveData.setValue(null);
        }
        return myOrderDetailsMutableLiveData;
    }

    public LiveData<ApiResponse> getDeclineOrderDetail() {
        if (DeclineOrderDetailLiveData == null) {
            DeclineOrderDetailLiveData = new MutableLiveData<>();
        }
        if (DeclineOrderDetailLiveData.getValue() != null && DeclineOrderDetailLiveData.getValue().data != null) {
            DeclineOrderDetailLiveData.setValue(null);
        }
        return DeclineOrderDetailLiveData;
    }

    public LiveData<ApiResponse> getchequepermission() {
        if (validateChequepermissionLiveData == null) {
            validateChequepermissionLiveData = new MutableLiveData<>();
        }
        if (validateChequepermissionLiveData.getValue() != null && validateChequepermissionLiveData.getValue().data != null) {
            validateChequepermissionLiveData.setValue(null);
        }
        return validateChequepermissionLiveData;
    }


    public LiveData<ApiResponse> getBankname() {
        if (bankNameResponseLiveData == null) {
            bankNameResponseLiveData = new MutableLiveData<>();
        }
        if (bankNameResponseLiveData.getValue() != null && bankNameResponseLiveData.getValue().data != null) {
            bankNameResponseLiveData.setValue(null);
        }
        return bankNameResponseLiveData;
    }

    public LiveData<ApiResponse> getOrderPlacedResponseData() {
        if (myOrderPlacedResponseMutableLiveData == null) {
            myOrderPlacedResponseMutableLiveData = new MutableLiveData<>();
        }
        if (myOrderPlacedResponseMutableLiveData.getValue() != null && myOrderPlacedResponseMutableLiveData.getValue().data != null) {
            myOrderPlacedResponseMutableLiveData.setValue(null);
        }
        return myOrderPlacedResponseMutableLiveData;
    }public LiveData<ApiResponse> getDeclineOrderPlacedResponseData() {
        if (postDeclineOrderMutableLiveData == null) {
            postDeclineOrderMutableLiveData = new MutableLiveData<>();
        }
        if (postDeclineOrderMutableLiveData.getValue() != null && postDeclineOrderMutableLiveData.getValue().data != null) {
            postDeclineOrderMutableLiveData.setValue(null);
        }
        return postDeclineOrderMutableLiveData;
    }

    /*
     * Order details API call
     * */
    public void hitOrderDetailsApi(int OrderId) {
        DisposableObserver<JsonElement> observerDes = RestClient.getInstance().getService().getOrderDetails(OrderId)
                .subscribeOn(Schedulers.io())

                //.doOnSubscribe(disposable -> myOrderDetailsMutableLiveData.setValue(ApiResponse.loading()))
                /*.doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        myOrderDetailsMutableLiveData.setValue(ApiResponse.loading());
                    }
                })*/
                .doOnSubscribe(disposable -> {
                    // myOrderDetailsMutableLiveData.setValue(ApiResponse.loading());
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<JsonElement>() {
                    @Override
                    public void onNext(JsonElement result) {
                        myOrderDetailsMutableLiveData.setValue(ApiResponse.success(result));

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        myOrderDetailsMutableLiveData.setValue(ApiResponse.error(throwable));

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        disposablesa.add(observerDes);
    }

    public void hitDeclineOrderDetailAPI(int OrderId) {
        DisposableObserver<JsonElement> observerDes = RestClient.getInstance().getService().GetDeclineOrderDetail(OrderId)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
                    DeclineOrderDetailLiveData.setValue(ApiResponse.loading());
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<JsonElement>() {
                    @Override
                    public void onNext(JsonElement result) {
                        DeclineOrderDetailLiveData.setValue(ApiResponse.success(result));

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        DeclineOrderDetailLiveData.setValue(ApiResponse.error(throwable));

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        disposablesa.add(observerDes);
    }

    /*
     * Order Placed API call
     * */
    public void hitOrderPlacedApi(OrderPlacedModel orderPlacedModel) {
        DisposableObserver<JsonElement> observerDes = RestClient.getInstance().getService().postOrderPostData(orderPlacedModel)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> myOrderPlacedResponseMutableLiveData.setValue(ApiResponse.loading()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<JsonElement>() {
                    @Override
                    public void onNext(JsonElement result) {
                        myOrderPlacedResponseMutableLiveData.setValue(ApiResponse.success(result));

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        myOrderPlacedResponseMutableLiveData.setValue(ApiResponse.error(throwable));

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        disposables.add(observerDes);
    }

    public void hitDeclineOrderPlacedApi(OrderPlacedModel orderPlacedModel) {
        DisposableObserver<JsonElement> observerDes = RestClient.getInstance().getService().postDeclineOrderPostData(orderPlacedModel)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> postDeclineOrderMutableLiveData.setValue(ApiResponse.loading()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<JsonElement>() {
                    @Override
                    public void onNext(JsonElement result) {
                        postDeclineOrderMutableLiveData.setValue(ApiResponse.success(result));

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        postDeclineOrderMutableLiveData.setValue(ApiResponse.error(throwable));

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        disposables.add(observerDes);
    }

    public void hitBankName() {
        DisposableObserver<JsonElement> observerDes = RestClient.getInstance().getService().getBankName()
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> bankNameResponseLiveData.setValue(ApiResponse.loading()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<JsonElement>() {
                    @Override
                    public void onNext(JsonElement result) {
                        bankNameResponseLiveData.setValue(ApiResponse.success(result));

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        bankNameResponseLiveData.setValue(ApiResponse.error(throwable));

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        disposables.add(observerDes);
    }


    public void hitOrderOTPExist(int orderId, String status, String comment) {
        DisposableObserver<JsonElement> observerDes = RestClient.getInstance().getService().checkOrderOtpExist(orderId, status, comment)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> checkOrderOtpExistLiveData.setValue(ApiResponse.loading()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<JsonElement>() {
                    @Override
                    public void onNext(JsonElement result) {
                        checkOrderOtpExistLiveData.setValue(ApiResponse.success(result));
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        checkOrderOtpExistLiveData.setValue(ApiResponse.error(throwable));
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        disposables.add(observerDes);
    }

    public void generateOrderOTP(int orderId, String status, double lat, double lg) {
        DisposableObserver<JsonElement> observerDes = RestClient.getInstance().getService().getOtpForOrder(orderId, status, lat, lg)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> generateOrderOtpLiveData.setValue(ApiResponse.loading()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<JsonElement>() {
                    @Override
                    public void onNext(JsonElement result) {
                        generateOrderOtpLiveData.setValue(ApiResponse.success(result));
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        generateOrderOtpLiveData.setValue(ApiResponse.error(throwable));
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        disposables.add(observerDes);
    }

    public void verifyOrderOTP(int orderId, String status, String otp) {
        DisposableObserver<JsonElement> observerDes = RestClient.getInstance().getService().validateOrderOtp(orderId, status, otp)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> validateOrderOtpLiveData.setValue(ApiResponse.loading()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<JsonElement>() {
                    @Override
                    public void onNext(JsonElement result) {
                        validateOrderOtpLiveData.setValue(ApiResponse.success(result));
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        validateOrderOtpLiveData.setValue(ApiResponse.error(throwable));
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        disposables.add(observerDes);
    }

    public void verifyChequePermission(int custId) {
        DisposableObserver<JsonElement> observerDes = RestClient.getInstance().getService().getChequepermission(custId)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> validateChequepermissionLiveData.setValue(ApiResponse.loading()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<JsonElement>() {
                    @Override
                    public void onNext(JsonElement result) {
                        validateChequepermissionLiveData.setValue(ApiResponse.success(result));
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        validateChequepermissionLiveData.setValue(ApiResponse.error(throwable));
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        disposables.add(observerDes);
    }


    public LiveData<ApiResponse> checkOrderOTPExist() {
        if (checkOrderOtpExistLiveData == null) {
            checkOrderOtpExistLiveData = new MutableLiveData<>();
        }
        if (checkOrderOtpExistLiveData.getValue() != null && checkOrderOtpExistLiveData.getValue().data != null) {
            checkOrderOtpExistLiveData.setValue(null);
        }
        return checkOrderOtpExistLiveData;
    }

    public LiveData<ApiResponse> generateOrderOTP() {
        if (generateOrderOtpLiveData == null) {
            generateOrderOtpLiveData = new MutableLiveData<>();
        }
        if (generateOrderOtpLiveData.getValue() != null && generateOrderOtpLiveData.getValue().data != null) {
            generateOrderOtpLiveData.setValue(null);
        }
        return generateOrderOtpLiveData;
    }

    public LiveData<ApiResponse> validateOrderOTP() {
        if (validateOrderOtpLiveData == null) {
            validateOrderOtpLiveData = new MutableLiveData<>();
        }
        if (validateOrderOtpLiveData.getValue() != null && validateOrderOtpLiveData.getValue().data != null) {
            validateOrderOtpLiveData.setValue(null);
        }
        return validateOrderOtpLiveData;
    }

    @Override
    protected void onCleared() {
        disposablesa.clear();
        disposables.clear();
    }
}