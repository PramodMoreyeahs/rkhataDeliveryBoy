package in.moreyeahs.livspace.delivery.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonElement;
import in.moreyeahs.livspace.delivery.api.RestClient;
import in.moreyeahs.livspace.delivery.model.DeliveryIssuanceM;
import in.moreyeahs.livspace.delivery.utilities.ApiResponse;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class AssignmentBillingViewModel  extends ViewModel {

    private final CompositeDisposable disposables = new CompositeDisposable();

    private MutableLiveData<ApiResponse> myTaskModelMutableLiveDat;
    private MutableLiveData<ApiResponse> MutableLiveDat;
    private MutableLiveData<ApiResponse> MutableLiveDatSendAllData;
    private MutableLiveData<ApiResponse> MutableLiveDatSendReject;


    public LiveData<ApiResponse> getAssignmentBilling() {
        if (myTaskModelMutableLiveDat == null) {
            myTaskModelMutableLiveDat = new MutableLiveData<>();
        }
        return myTaskModelMutableLiveDat;
    }

    public LiveData<ApiResponse> getAssignmentAllData() {
        if (MutableLiveDat == null) {
            MutableLiveDat = new MutableLiveData<>();
        }
        return MutableLiveDat;
    }

    public LiveData<ApiResponse> getAssignmentSendAllData() {
        if (MutableLiveDatSendAllData == null) {
            MutableLiveDatSendAllData = new MutableLiveData<>();
        }
        return MutableLiveDatSendAllData;
    }

    public LiveData<ApiResponse> getRejectAssignment() {
        if (MutableLiveDatSendReject == null) {
            MutableLiveDatSendReject = new MutableLiveData<>();
        }
        return MutableLiveDatSendReject;
    }

    /*
     * Assignment ID API call
     * */
    public void AssignmentBillingID(int deleveryBoyID) {
        disposables.add(RestClient.getInstance().getService().DeliveryIssuance(String.valueOf(deleveryBoyID))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Consumer<JsonElement>() {
                            @Override
                            public void accept(JsonElement result) throws Exception {
                                myTaskModelMutableLiveDat.setValue(ApiResponse.success(result));
                            }
                        },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                myTaskModelMutableLiveDat.setValue(ApiResponse.error(throwable));
                            }
                        }
                ));
    }

    public void AssignmentAllData(String deleveryBoyID) {
        disposables.add(RestClient.getInstance().getService().AssignmentID(deleveryBoyID)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        MutableLiveDat.setValue(ApiResponse.loading());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Consumer<JsonElement>() {
                            @Override
                            public void accept(JsonElement result) throws Exception {
                                MutableLiveDat.setValue(ApiResponse.success(result));
                            }
                        },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                MutableLiveDat.setValue(ApiResponse.error(throwable));
                            }
                        }
                ));
    }

    public void sendAllAssignmentData(int Dboyid, int AlignmentId,String FileName) {
        disposables.add(RestClient.getInstance().getService().sendAssignmentData(String.valueOf(Dboyid),AlignmentId,FileName)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        MutableLiveDatSendAllData.setValue(ApiResponse.loading());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Consumer<JsonElement>() {
                            @Override
                            public void accept(JsonElement result) throws Exception {
                                MutableLiveDatSendAllData.setValue(ApiResponse.success(result));
                            }
                        },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                MutableLiveDatSendAllData.setValue(ApiResponse.error(throwable));
                            }
                        }
                ));
    }

    public void rejectAssignmentData(DeliveryIssuanceM deliveryIssuanceM) {
        disposables.add(RestClient.getInstance().getService().rejectAssignment(deliveryIssuanceM)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        MutableLiveDatSendReject.setValue(ApiResponse.loading());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Consumer<JsonElement>() {
                            @Override
                            public void accept(JsonElement result) throws Exception {
                                MutableLiveDatSendReject.setValue(ApiResponse.success(result));
                            }
                        },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                MutableLiveDatSendReject.setValue(ApiResponse.error(throwable));
                            }
                        }
                ));
    }
}
