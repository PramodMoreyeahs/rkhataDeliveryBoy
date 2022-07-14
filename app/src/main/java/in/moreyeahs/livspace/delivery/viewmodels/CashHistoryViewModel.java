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
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class CashHistoryViewModel extends ViewModel {


    private final CompositeDisposable disposables = new CompositeDisposable();
    private MutableLiveData<ApiResponse> myCurrencyMutableLiveDat;
    private MutableLiveData<ApiResponse> getCashHistorylivedata;
    public LiveData<ApiResponse> getcashHistory()
    {
        if (myCurrencyMutableLiveDat == null)
        {
            myCurrencyMutableLiveDat = new MutableLiveData<>();
        }
        if (myCurrencyMutableLiveDat.getValue() != null && myCurrencyMutableLiveDat.getValue().data != null)
        {
            myCurrencyMutableLiveDat.setValue(null);
        }
        return myCurrencyMutableLiveDat;
    }
    public LiveData<ApiResponse> getHistorydata()
    {
        if (getCashHistorylivedata == null)
        {
            getCashHistorylivedata = new MutableLiveData<>();
        }
        if (getCashHistorylivedata.getValue() != null && getCashHistorylivedata.getValue().data != null)
        {
            getCashHistorylivedata.setValue(null);
        }
        return getCashHistorylivedata;
    }

    public void hitCashHistorydata(int peopleId,int warehouseid) {
        DisposableObserver<JsonElement> observerDes = RestClient.getInstance().getService().getHistory(peopleId,warehouseid)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {

                        myCurrencyMutableLiveDat.setValue(ApiResponse.loading());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<JsonElement>() {
                    @Override
                    public void onNext(JsonElement result) {
                        myCurrencyMutableLiveDat.setValue(ApiResponse.success(result));
                        disposables.clear();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        myCurrencyMutableLiveDat.setValue(ApiResponse.error(throwable));
                        disposables.clear();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        disposables.add(observerDes);


    }
    public void hitgetHistorydata(int currencyCollectionId) {
        DisposableObserver<JsonElement> observerDes = RestClient.getInstance().getService().getHistoryByid(currencyCollectionId)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {

                        getCashHistorylivedata.setValue(ApiResponse.loading());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<JsonElement>() {
                    @Override
                    public void onNext(JsonElement result) {
                        getCashHistorylivedata.setValue(ApiResponse.success(result));
                        disposables.clear();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        getCashHistorylivedata.setValue(ApiResponse.error(throwable));
                        disposables.clear();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        disposables.add(observerDes);


    }
}
