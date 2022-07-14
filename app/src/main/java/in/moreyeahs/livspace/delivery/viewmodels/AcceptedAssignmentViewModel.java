package in.moreyeahs.livspace.delivery.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import in.moreyeahs.livspace.delivery.api.RestClient;
import in.moreyeahs.livspace.delivery.model.CurrentEndTimeModel;
import in.moreyeahs.livspace.delivery.model.SortOrderModel;
import in.moreyeahs.livspace.delivery.utilities.ApiResponse;
import in.moreyeahs.livspace.delivery.utilities.ApiResponseObj;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class AcceptedAssignmentViewModel extends ViewModel {
    private final CompositeDisposable disposablesa = new CompositeDisposable();

    private MutableLiveData<ApiResponseObj> myacceptedAssginmenmtLiveData;
    private MutableLiveData<ApiResponse> PoststartTimeData;
    private MutableLiveData<ApiResponse> tokenLiveData;
    private MutableLiveData<ApiResponse> shortPathLiveData;
    private MutableLiveData<ApiResponseObj>RejectedAssignmentLiveData;

    public LiveData<ApiResponseObj> getacceptedAssignment() {
        if (myacceptedAssginmenmtLiveData == null) {
            myacceptedAssginmenmtLiveData = new MutableLiveData<>();
        }
        if (myacceptedAssginmenmtLiveData.getValue() != null && myacceptedAssginmenmtLiveData.getValue().data != null) {
            myacceptedAssginmenmtLiveData.setValue(null);
        }

        return myacceptedAssginmenmtLiveData;
    }

    public LiveData<ApiResponse> getpostStartTime() {
        if (PoststartTimeData == null) {
            PoststartTimeData = new MutableLiveData<>();
        }
        if (PoststartTimeData.getValue() != null && PoststartTimeData.getValue().data != null) {
            PoststartTimeData.setValue(null);
        }

        return PoststartTimeData;
    }
    public LiveData<ApiResponseObj> getrejectedAssignment() {
        if (RejectedAssignmentLiveData == null) {
            RejectedAssignmentLiveData = new MutableLiveData<>();
        }
        if (RejectedAssignmentLiveData.getValue() != null && RejectedAssignmentLiveData.getValue().data != null) {
            RejectedAssignmentLiveData.setValue(null);
        }

        return RejectedAssignmentLiveData;
    }


    public LiveData<ApiResponse> getshortPath() {
        if (shortPathLiveData == null) {
            shortPathLiveData = new MutableLiveData<>();
        }
        if (shortPathLiveData.getValue() != null && shortPathLiveData.getValue().data != null) {
            shortPathLiveData.setValue(null);
        }

        return shortPathLiveData;
    }

    public LiveData<ApiResponse> tokenResponse() {
        if (tokenLiveData == null) {
            tokenLiveData = new MutableLiveData<>();
        }
        if (tokenLiveData.getValue() != null && tokenLiveData.getValue().data != null) {
            tokenLiveData.setValue(null);
        }
        return tokenLiveData;
    }


    public void hitShortPathApi(SortOrderModel sortOrderModel) {
        DisposableObserver<JsonElement> observerDes = RestClient.getInstance().getService().getShortPath(sortOrderModel)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        shortPathLiveData.setValue(ApiResponse.loading());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<JsonElement>() {
                    @Override
                    public void onNext(JsonElement result) {
                        shortPathLiveData.setValue(ApiResponse.success(result));

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        shortPathLiveData.setValue(ApiResponse.error(throwable));
                        throwable.printStackTrace();

                    }

                    @Override
                    public void onComplete() {

                    }
                });

        disposablesa.add(observerDes);
    }


    public void hitAcceptedAssignmentApi(int dbid) {
        DisposableObserver<JsonObject> observerDes = RestClient.getInstance().getService().getAcceptedAssignment(dbid)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> myacceptedAssginmenmtLiveData.setValue(ApiResponseObj.loading()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<JsonObject>() {
                    @Override
                    public void onNext(JsonObject result) {
                        myacceptedAssginmenmtLiveData.setValue(ApiResponseObj.success(result));

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        myacceptedAssginmenmtLiveData.setValue(ApiResponseObj.error(throwable));

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        disposablesa.add(observerDes);
    }

    public void hitPostTimerApi(CurrentEndTimeModel CurrentEndTimeModel) {
        DisposableObserver<JsonElement> observerDes = RestClient.getInstance().getService().postCurentTime(CurrentEndTimeModel)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        PoststartTimeData.setValue(ApiResponse.loading());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<JsonElement>() {
                    @Override
                    public void onNext(JsonElement result) {
                        PoststartTimeData.setValue(ApiResponse.success(result));
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        PoststartTimeData.setValue(ApiResponse.error(throwable));
                        throwable.printStackTrace();

                    }

                    @Override
                    public void onComplete() {

                    }
                });

        disposablesa.add(observerDes);
    }

    public void hitGetRejectedAssignment(int Dboyid) {
        DisposableObserver<JsonObject> observerDes = RestClient.getInstance().getService().GetRejectedAssignment(Dboyid)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        RejectedAssignmentLiveData.setValue(ApiResponseObj.loading());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<JsonObject>() {
                    @Override
                    public void onNext(JsonObject result) {
                        RejectedAssignmentLiveData.setValue(ApiResponseObj.success(result));
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        RejectedAssignmentLiveData.setValue(ApiResponseObj.error(throwable));
                        throwable.printStackTrace();

                    }

                    @Override
                    public void onComplete() {

                    }
                });

        disposablesa.add(observerDes);
    }

    public void hitTokenAPI(String password, String username, String Password) {
        DisposableObserver<JsonObject> observerDes = RestClient.getInstance().getService().getToken(password, username, Password)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> tokenLiveData.setValue(ApiResponse.loading()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<JsonObject>() {
                    @Override
                    public void onNext(JsonObject result) {
                        tokenLiveData.setValue(ApiResponse.success(result));
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        tokenLiveData.setValue(ApiResponse.error(throwable));
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        disposablesa.add(observerDes);
    }

    @Override
    protected void onCleared() {
        disposablesa.clear();
    }
}