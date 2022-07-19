package in.moreyeahs.livspace.delivery.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import in.moreyeahs.livspace.delivery.api.RestClient;
import in.moreyeahs.livspace.delivery.model.AcceptModel;
import in.moreyeahs.livspace.delivery.utilities.ApiResponse;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class PendingTaskViewModel  extends ViewModel {

    private final CompositeDisposable disposables = new CompositeDisposable();
    private MutableLiveData<String> startTimerMutableLiveData;
    private MutableLiveData<String> stopTimerMutableLiveData;
    private MutableLiveData<ApiResponse> myTaskModelMutableLiveDat;
    private MutableLiveData<Boolean> acceptMyTaskMutableLiveDat;
    private MutableLiveData<ApiResponse> cashCollectionMutableLiveDat;


    public LiveData<ApiResponse> getPendingTaskData() {
        if (myTaskModelMutableLiveDat == null) {
            myTaskModelMutableLiveDat = new MutableLiveData<>();
        }
        return myTaskModelMutableLiveDat;
    }
    public LiveData<ApiResponse> getCashData() {
        if (cashCollectionMutableLiveDat == null) {
            cashCollectionMutableLiveDat = new MutableLiveData<>();
        }
        return cashCollectionMutableLiveDat;
    }

    public LiveData<Boolean> getAcceptMyTaskData() {
        if (acceptMyTaskMutableLiveDat == null) {
            acceptMyTaskMutableLiveDat = new MutableLiveData<>();
        }
        return acceptMyTaskMutableLiveDat;
    }

    /*
     * My task API call
     * */
    public void pendingMyTaskAdi(int peopleID) {
        disposables.add(RestClient.getInstance().getService().getPepoleMyTaskData(String.valueOf(peopleID))
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        myTaskModelMutableLiveDat.setValue(ApiResponse.loading());
                    }
                })
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

    public void cashCollection(String mobile,int page,int limit ) {
        disposables.add(RestClient.getInstance().getService().getcashCollection(mobile,page,limit)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        cashCollectionMutableLiveDat.setValue(ApiResponse.loading());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Consumer<JsonElement>() {
                            @Override
                            public void accept(JsonElement result) throws Exception {
                                cashCollectionMutableLiveDat.setValue(ApiResponse.success(result));
                            }
                        },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                cashCollectionMutableLiveDat.setValue(ApiResponse.error(throwable));
                            }
                        }
                ));
    }
    public void creditCollection(String mobile ,int page,int limit) {
        disposables.add(RestClient.getInstance().getService().getcreditCollection(mobile,page,limit)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        cashCollectionMutableLiveDat.setValue(ApiResponse.loading());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Consumer<JsonElement>() {
                            @Override
                            public void accept(JsonElement result) throws Exception {
                                cashCollectionMutableLiveDat.setValue(ApiResponse.success(result));
                            }
                        },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                cashCollectionMutableLiveDat.setValue(ApiResponse.error(throwable));
                            }
                        }
                ));
    }
    /*
     * Accept API call
     * */
    public void AcceptPendingMyTaskAdi(AcceptModel acceptModel) {
        disposables.add(RestClient.getInstance().getService().acceptMyPendingTask(acceptModel)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                     //   acceptMyTaskMutableLiveDat.setValue(false);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Consumer<JsonElement>() {
                            @Override
                            public void accept(JsonElement result) throws Exception {
                                acceptMyTaskMutableLiveDat.setValue(true);
                            }
                        },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                acceptMyTaskMutableLiveDat.setValue(false);
                            }
                        }
                ));
    }

}
