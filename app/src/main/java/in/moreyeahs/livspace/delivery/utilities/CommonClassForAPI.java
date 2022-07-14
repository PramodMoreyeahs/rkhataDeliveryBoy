package in.moreyeahs.livspace.delivery.utilities;

import android.app.Activity;

import com.google.gson.JsonObject;

import java.util.List;

import in.moreyeahs.livspace.delivery.api.RestClient;
import in.moreyeahs.livspace.delivery.model.AcceptRejectDC;
import in.moreyeahs.livspace.delivery.model.AssignmentDetailsModel;
import in.moreyeahs.livspace.delivery.model.AssignmentListResponse;
import in.moreyeahs.livspace.delivery.model.PoListResponse;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;


public class CommonClassForAPI {
    private static Activity mActivity;
    private static CommonClassForAPI CommonClassForAPI;

    public static CommonClassForAPI getInstance(Activity activity) {
        if (CommonClassForAPI == null) {
            CommonClassForAPI = new CommonClassForAPI();
        }
        mActivity = activity;
        return CommonClassForAPI;

    }

    public void fetchPoListByAssignmentId(final DisposableObserver fetchLoginDes, int assignmentId) {
        RestClient.getInstance().getService().getPoList(assignmentId)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> Utils.showProgressDialog(mActivity))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<PoListResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(List<PoListResponse> userModel) {
                        //  customDialog.dismiss();
                        fetchLoginDes.onNext(userModel);
                    }

                    @Override
                    public void onError(Throwable e) {
                        // customDialog.dismiss();
                        fetchLoginDes.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        //customDialog.dismiss();
                        fetchLoginDes.onComplete();
                    }
                });
    }

    public void fetchAssignmentList(final DisposableObserver fetchLoginDes, int peopleId) {
        RestClient.getInstance().getService().getAssignmentList(peopleId)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> Utils.showProgressDialog(mActivity))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AssignmentListResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(AssignmentListResponse userModel) {
                        //  customDialog.dismiss();
                        fetchLoginDes.onNext(userModel);
                    }

                    @Override
                    public void onError(Throwable e) {
                        // customDialog.dismiss();
                        fetchLoginDes.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        //customDialog.dismiss();
                        fetchLoginDes.onComplete();
                    }
                });
    }

    public void fetchEwayBill(final DisposableObserver fetchLoginDes, int poId) {
        RestClient.getInstance().getService().getEWayBill(poId)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> Utils.showProgressDialog(mActivity))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(String userModel) {
                        //  customDialog.dismiss();
                        fetchLoginDes.onNext(userModel);
                    }

                    @Override
                    public void onError(Throwable e) {
                        // customDialog.dismiss();
                        fetchLoginDes.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        //customDialog.dismiss();
                        fetchLoginDes.onComplete();
                    }
                });
    }

    public void fetchAssignmentDetails(final DisposableObserver fetchLoginDes, int purchaseOrderId) {
        RestClient.getInstance().getService().getAssignmentDetails(purchaseOrderId)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> Utils.showProgressDialog(mActivity))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AssignmentDetailsModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(AssignmentDetailsModel userModel) {
                        //  customDialog.dismiss();
                        fetchLoginDes.onNext(userModel);
                    }

                    @Override
                    public void onError(Throwable e) {
                        // customDialog.dismiss();
                        fetchLoginDes.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        //customDialog.dismiss();
                        fetchLoginDes.onComplete();
                    }
                });
    }

    public void postGrnToServer(final DisposableObserver fetchLoginDes, AssignmentDetailsModel model) {
        RestClient.getInstance().getService().postAssignmentGrn(model)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> Utils.showProgressDialog(mActivity))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(Boolean userModel) {
                        //  customDialog.dismiss();
                        fetchLoginDes.onNext(userModel);
                    }

                    @Override
                    public void onError(Throwable e) {
                        // customDialog.dismiss();
                        fetchLoginDes.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        //customDialog.dismiss();
                        fetchLoginDes.onComplete();
                    }
                });
    }

    public void acceptRejectOrder(final DisposableObserver fetchLoginDes, AcceptRejectDC model) {
        RestClient.getInstance().getService().acceptRejectAssignment(model)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> Utils.showProgressDialog(mActivity))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JsonObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(JsonObject userModel) {
                        //  customDialog.dismiss();
                        fetchLoginDes.onNext(userModel);
                    }

                    @Override
                    public void onError(Throwable e) {
                        // customDialog.dismiss();
                        fetchLoginDes.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        //customDialog.dismiss();
                        fetchLoginDes.onComplete();
                    }
                });
    }

    public void submitCompletedAssignment(final DisposableObserver fetchLoginDes, int poId) {
        RestClient.getInstance().getService().submitAssignmentApi(poId)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> Utils.showProgressDialog(mActivity))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(Boolean userModel) {
                        //  customDialog.dismiss();
                        fetchLoginDes.onNext(userModel);
                    }

                    @Override
                    public void onError(Throwable e) {
                        // customDialog.dismiss();
                        fetchLoginDes.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        //customDialog.dismiss();
                        fetchLoginDes.onComplete();
                    }
                });
    }

    public void changeOrderStatus(final DisposableObserver fetchLoginDes, int orderId, String orderStatus,Boolean credit_status) {
        RestClient.getInstance().getService().changeOrderStatus(orderId, orderStatus,credit_status)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> Utils.showProgressDialog(mActivity))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JsonObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(JsonObject userModel) {
                        //  customDialog.dismiss();
                        fetchLoginDes.onNext(userModel);
                    }

                    @Override
                    public void onError(Throwable e) {
                        // customDialog.dismiss();
                        fetchLoginDes.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        //customDialog.dismiss();
                        fetchLoginDes.onComplete();
                    }
                });
    }


   /* public void fetchMyTaskData(final DisposableObserver myTaskDes) {
        RestClient.getInstance().getService().getMyTaskData()
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> Utils.showProgressDialog(mActivity))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArrayList<MyTaskModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(ArrayList<MyTaskModel> myTaskList) {
                        myTaskDes.onNext(myTaskList);
                    }

                    @Override
                    public void onError(Throwable e) {
                        myTaskDes.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        myTaskDes.onComplete();
                    }
                });
    }*/



}