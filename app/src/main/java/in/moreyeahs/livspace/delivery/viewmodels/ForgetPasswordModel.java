package in.moreyeahs.livspace.delivery.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonElement;
import in.moreyeahs.livspace.delivery.api.RestClient;
import in.moreyeahs.livspace.delivery.utilities.ApiResponse;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ForgetPasswordModel extends ViewModel {

    private final CompositeDisposable disposables = new CompositeDisposable();
    private MutableLiveData<ApiResponse> myTaskModelMutableLiveDat;
    public LiveData<ApiResponse> getpassworddata() {
        if (myTaskModelMutableLiveDat == null) {
            myTaskModelMutableLiveDat = new MutableLiveData<>();
        }
        if (myTaskModelMutableLiveDat.getValue() != null && myTaskModelMutableLiveDat.getValue().data != null) {
            myTaskModelMutableLiveDat.setValue(null);
        }

        return myTaskModelMutableLiveDat;
    }
    /*public void ForgetpasswordAPI(String Mobile) {
        DisposableObserver<ForgetpassresponseModel> observerDes = RestClient.getInstance().getService().postforgetPassword(Mobile)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<ForgetpassresponseModel>() {
                    @Override
                    public void onNext(ForgetpassresponseModel result) {
                        myTaskModelMutableLiveDat.setValue(ApiResponse.loading());

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        myTaskModelMutableLiveDat.setValue(ApiResponse.error(throwable));
                        throwable.printStackTrace();
                        disposables.clear();
                    }

                    @Override
                    public void onComplete() {

                    }
                });

        disposables.add(observerDes);
    }*/
    public void ForgetpasswordAPI(String Mobile) {
        disposables.add(RestClient.getInstance().getService().postforgetPassword(Mobile)
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
    @Override
    protected void onCleared() {
        // myOrderDetailsMutableLiveData.postValue(null);
        disposables.clear();
    }
}
