package in.moreyeahs.livspace.delivery.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import in.moreyeahs.livspace.delivery.api.RestClient;
import in.moreyeahs.livspace.delivery.utilities.ApiResponseList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class SplashViewModel extends ViewModel {
    private MutableLiveData<ApiResponseList> responseLiveData;
    private final CompositeDisposable disposables = new CompositeDisposable();

    public LiveData<ApiResponseList> versionResponse() {
        if (responseLiveData == null) {
            responseLiveData = new MutableLiveData<>();
        }
        return responseLiveData;
    }

    // Version API calling
    public void hitVersionApi() {
        disposables.add(RestClient.getInstance().getService().doSplashVersion()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> responseLiveData.setValue(ApiResponseList.success(result)),
                        (Throwable throwable) -> {
                            responseLiveData.setValue(ApiResponseList.error(throwable));
                            System.out.println("Error" + throwable);
                        }
                ));
    }

    @Override
    protected void onCleared() {
        disposables.clear();
    }
}