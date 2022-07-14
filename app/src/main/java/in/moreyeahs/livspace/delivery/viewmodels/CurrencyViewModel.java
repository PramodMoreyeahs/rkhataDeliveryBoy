package in.moreyeahs.livspace.delivery.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonElement;
import in.moreyeahs.livspace.delivery.api.RestClient;
import in.moreyeahs.livspace.delivery.R;
import in.moreyeahs.livspace.delivery.model.CurrencyModel;
import in.moreyeahs.livspace.delivery.utilities.ApiResponse;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class CurrencyViewModel extends ViewModel {
    private final CompositeDisposable disposables = new CompositeDisposable();

    private MutableLiveData<ApiResponse> myCurrencyMutableLiveDat;
    private MutableLiveData<ApiResponse> CurrencyDataList;

    private ArrayList<CurrencyModel> currencyList;

    public LiveData<ApiResponse> getCurrencyResponseData()
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
     public LiveData<ApiResponse> getCurrencyData()
     {
        if (CurrencyDataList == null)
        {
            CurrencyDataList = new MutableLiveData<>();
        }
        if (CurrencyDataList.getValue() != null && CurrencyDataList.getValue().data != null)
        {
            CurrencyDataList.setValue(null);
        }
        return CurrencyDataList;
    }

    public ArrayList<CurrencyModel> getCurrencyList()
    {
        currencyList = new ArrayList<>();
        currencyList.add(new CurrencyModel(1, 1, 1, R.drawable.one_rs));
        currencyList.add(new CurrencyModel(2, 2, 2, R.drawable.two_rs));
        currencyList.add(new CurrencyModel(3, 5, 5, R.drawable.five_rs));
        currencyList.add(new CurrencyModel(4, 5, 5, R.drawable.five_note_rs));
        currencyList.add(new CurrencyModel(5, 10, 10, R.drawable.ten_rs));
        currencyList.add(new CurrencyModel(6, 10, 10, R.drawable.ten_note_rs));
        currencyList.add(new CurrencyModel(7, 20, 20, R.drawable.twenty_note_rs));
        currencyList.add(new CurrencyModel(8, 50, 50, R.drawable.fifty_note_rs));
        currencyList.add(new CurrencyModel(9, 100, 100, R.drawable.one_note_hundred_rs));
        currencyList.add(new CurrencyModel(10, 200, 200, R.drawable.two_hundred));
        currencyList.add(new CurrencyModel(11, 500, 500, R.drawable.five_hundred_note_rs));
        currencyList.add(new CurrencyModel(12, 2000, 2000, R.drawable.two_thousand_note_rs));
        return currencyList;
    }



    public void hitgetCurrencyData() {
        DisposableObserver<JsonElement> observerDes = RestClient.getInstance().getService().getCurrency()
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {

                        CurrencyDataList.setValue(ApiResponse.loading());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<JsonElement>() {
                    @Override
                    public void onNext(JsonElement result) {
                        CurrencyDataList.setValue(ApiResponse.success(result));
                        disposables.clear();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        CurrencyDataList.setValue(ApiResponse.error(throwable));
                        disposables.clear();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
               disposables.add(observerDes);


    }


    @Override
    protected void onCleared() {
        disposables.dispose();
        disposables.clear();
    }

}