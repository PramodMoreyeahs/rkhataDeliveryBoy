package in.moreyeahs.livspace.delivery.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonElement;
import in.moreyeahs.livspace.delivery.api.RestClient;
import in.moreyeahs.livspace.delivery.model.OTPModel;
import in.moreyeahs.livspace.delivery.utilities.ApiResponse;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class OTPViewModel extends ViewModel {
    private MutableLiveData<ApiResponse> responseLiveData;
    private MutableLiveData<ApiResponse> tokenLiveData;

    private final CompositeDisposable disposables = new CompositeDisposable();



    public LiveData<ApiResponse> OtpResponse() {
        if (responseLiveData == null) {
            responseLiveData = new MutableLiveData<>();
        }
        return responseLiveData;
    }
    public LiveData<ApiResponse> tokenResponse() {
        if (tokenLiveData == null) {
            tokenLiveData = new MutableLiveData<>();
        }
        return tokenLiveData;
    }
    /*
     * login API calling
     * */
    public void hitOTPApi(OTPModel otpModel) {
        disposables.add(RestClient.getInstance().getService().genotpForDeliveryBoy(otpModel)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> responseLiveData.setValue(ApiResponse.loading()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Consumer<JsonElement>() {
                            @Override
                            public void accept(JsonElement result) throws Exception {
                                responseLiveData.setValue(ApiResponse.success(result));
                            }
                        },
                        (Throwable throwable) -> {
                            responseLiveData.setValue(ApiResponse.error(throwable));
                        }
                ));
    }
    public void hitTokenAPI(String password, String username, String Password) {
        disposables.add(RestClient.getInstance().getService().getToken(password, username, Password)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        tokenLiveData.setValue(ApiResponse.loading());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Consumer<JsonElement>() {
                            @Override
                            public void accept(JsonElement result) throws Exception {
                                tokenLiveData.setValue(ApiResponse.success(result));
                            }
                        },
                        (Throwable throwable) -> {
                            tokenLiveData.setValue(ApiResponse.error(throwable));
                        }
                ));
    }

    @Override
    protected void onCleared() {
        disposables.clear();
    }
}
