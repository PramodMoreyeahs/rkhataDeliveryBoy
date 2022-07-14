package in.moreyeahs.livspace.delivery.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonElement;
import in.moreyeahs.livspace.delivery.api.RestClient;
import in.moreyeahs.livspace.delivery.model.LoginModel;
import in.moreyeahs.livspace.delivery.model.User;
import in.moreyeahs.livspace.delivery.utilities.ApiResponse;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class LoginViewModel extends ViewModel {
    private User user;
    private MutableLiveData<ApiResponse> responseLiveData;
    private MutableLiveData<ApiResponse> tokenLiveData;
    private final CompositeDisposable disposables = new CompositeDisposable();

    public LoginViewModel() {
        user = new User("", "");
    }

    public LiveData<ApiResponse> loginResponse() {
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
    public void hitLoginApi(LoginModel loginModel) {
        disposables.add(RestClient.getInstance().getService().doLogin(loginModel)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> responseLiveData.setValue(ApiResponse.loading()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> responseLiveData.setValue(ApiResponse.success(result)),
                        (Throwable throwable) -> {
                            responseLiveData.setValue(ApiResponse.error(throwable));
                            System.out.println("Error" + throwable);
                        }
                ));
    }

    public void hitTokenAPI(String password, String username, String Password) {
        disposables.add(RestClient.getInstance().getService().getToken(password, username, Password)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> tokenLiveData.setValue(ApiResponse.loading()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((Consumer<JsonElement>) result -> tokenLiveData.setValue(ApiResponse.success(result)),
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