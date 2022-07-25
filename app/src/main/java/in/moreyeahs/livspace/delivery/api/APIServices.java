package in.moreyeahs.livspace.delivery.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import in.moreyeahs.livspace.delivery.model.AcceptModel;
import in.moreyeahs.livspace.delivery.model.AcceptRejectDC;
import in.moreyeahs.livspace.delivery.model.AssginmentSettleResponseModel;
import in.moreyeahs.livspace.delivery.model.AssignmentDetailsModel;
import in.moreyeahs.livspace.delivery.model.AssignmentListResponse;
import in.moreyeahs.livspace.delivery.model.CurrencyPostDataModel;
import in.moreyeahs.livspace.delivery.model.CurrentEndTimeModel;
import in.moreyeahs.livspace.delivery.model.DeliveryIssuanceM;
import in.moreyeahs.livspace.delivery.model.LatLongModel;
import in.moreyeahs.livspace.delivery.model.LoginModel;
import in.moreyeahs.livspace.delivery.model.OTPModel;
import in.moreyeahs.livspace.delivery.model.OrderPlacedModel;
import in.moreyeahs.livspace.delivery.model.PaymentReq;
import in.moreyeahs.livspace.delivery.model.PoListResponse;
import in.moreyeahs.livspace.delivery.model.SortOrderModel;
import in.moreyeahs.livspace.delivery.model.TimerDetailsModel;
import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by User on 03-11-2018.
 */

public interface APIServices {

    @POST("/api/DBSignup/V2")
    Observable<JsonElement> doLogin(@Body LoginModel loginModel);

    @GET("/api/appVersion/DeliveryApp")
    Observable<JsonArray> doSplashVersion();

    @GET("api/DeliveryTask/V3")
    Observable<JsonObject> changeOrderStatus(@Query("OrderId") int orderId, @Query("Status") String status, @Query("IsCredit") Boolean credit_status);

    @GET("api/PoAssignment/PendingAssignment")
    Observable<AssignmentListResponse> getAssignmentList(@Query("id") int peopleId);

    @GET("api/PoAssignment/GetEwayBill")
    Observable<String> getEWayBill(@Query("purchaseOrderId") int poId);


    @GET("/api/DeliveryTask/AssignmentOrder/V1")
    Observable<JsonElement> getMyTaskData(@Query("DeliveryIssuanceId") int DeliveryIssuanceId, @Query("mob") String mobile);

    @GET("/api/DeliveryTask/OrderDetail/V2")
    Observable<JsonElement> getOrderDetails(@Query("OrderId") int OrderId);

    @GET("api/DeliveryIssuance/PendingAssignment/V1")
    Observable<JsonElement> getPepoleMyTaskData(@Query("id") String mobile);

    @GET("api/PoAssignment/GetPurchaseorder")
    Observable<AssignmentDetailsModel> getAssignmentDetails(@Query("PurchaseOrderId") int PurchaseOrderId);

    @GET("api/PoAssignment/GetPOList")
    Observable<List<PoListResponse>> getPoList(@Query("POAssignmentId") int POAssignmentId);

    @POST("/api/DeliveryTask/PostOrder/V1")
    Observable<JsonElement> postOrderPostData(@Body OrderPlacedModel orderPlacedModel);

    @POST("/api/PoAssignment/GRN")
    Observable<Boolean> postAssignmentGrn(@Body AssignmentDetailsModel grnModel);

    @POST("/api/DeliveryTask/PostDeclineOrder")
    Observable<JsonElement> postDeclineOrderPostData(@Body OrderPlacedModel orderPlacedModel);

    @GET("/api/DeliveryTask/V1")
    Observable<JsonObject> getPepoleMyTaskData(@Query("mob") String mob, @Query("start") String start, @Query("end") String end, @Query("dboyId") String dboyId);

    @PUT("/api/DeliveryIssuance/V2")
    Observable<JsonElement> acceptMyPendingTask(@Body AcceptModel acceptModel);

    @PUT("api/PoAssignment/AcceptRejectPoAssignment")
    Observable<JsonObject> acceptRejectAssignment(@Body AcceptRejectDC model);

    @GET("api/PoAssignment/SubmittedAss")
    Observable<Boolean> submitAssignmentApi(@Query("POAssignmentId") int poid);

    @POST("/api/MobileDelivery/DBoyCashCollection")
    Observable<JsonElement> postCurrencyData(@Body CurrencyPostDataModel currencyPostDataModel);

    @POST("/api/DBSignup/DeliveryEclipseTime")
    Observable<JsonElement> postCurentTime(@Body CurrentEndTimeModel CurrentEndTimeModel);

    @POST("/api/DBSignup/DeliveryEclipseTime")
    Observable<JsonElement> getCurentTime(@Body TimerDetailsModel CurrentEndTimeModel);

    @GET("/api/Customers/Forgrt/V2")
    Observable<JsonElement> postforgetPassword(@Query("Mobile") String Mobile);

    @GET("api/DeliveryIssuance/getAssignid")
    Observable<JsonElement> searchAssignmentIDResponse(@Query("AssignmentId") String AssignmentId, @Query("DBoyId") int DBoyId);

    @GET("api/DeliveryIssuance/getorderid")
    Observable<JsonElement> searchOrderIDResponse(@Query("orderId") String orderId, @Query("DBoyId") int DBoyId);

    @POST("/api/DBSignup/GenotpForDeliveryBoyApp")
    Observable<JsonElement> genotpForDeliveryBoy(@Body OTPModel otpModel);

    @GET("/api/DeliveryIssuance/SubmittedAssignment/V1")
    Observable<JsonElement> DeliveryIssuance(@Query("id") String id);

    @GET("api/DeliveryTask/PaymentAssignment/V1")
    Observable<JsonElement> AssignmentID(@Query("DeliveryIssuanceId") String id);

    @PUT("api/DeliveryTask/PaymentSubmittedAssignment/V1")
    Observable<JsonElement> sendAssignmentData(@Query("id") String Dboyid, @Query("DeliveryIssuanceId") int id, @Query("FileName") String FileName);

    @PUT("api/DeliveryOrderAssignmentChange/Rejected")
    Observable<JsonElement> rejectAssignment(@Body DeliveryIssuanceM deliveryIssuanceM);

    @POST("api/SalesAppCounter")
    Observable<JsonObject> postlatlong(@Body LatLongModel model);

    @GET("/api/MobileDelivery/PaymentSubmittedAssignment/V1?")
        // @GET("/api/MobileDelivery/FreezedAssignment/V1?")
    Observable<JsonObject> getAssignmentIDDeatil(@Query("peopleId") int peopleId, @Query("warehouseid") int warehouseid);

    @GET("/api/MobileDelivery/CurrencyDenomination")
    Observable<JsonObject> getCurrency();

    @GET("/api/MobileDelivery/GetLastTwoDayCurrencyCollection")
    Observable<JsonObject> getHistory(@Query("peopleId") int peopleId, @Query("warehouseid") int warehouseid);

    @GET("/api/MobileDelivery/GetCurrencyCollectionById")
    Observable<JsonObject> getHistoryByid(@Query("currencyCollectionId") int currencyCollectionId);

    @GET("/api/DeliveryIssuance/AcceptedInprogress/V1")
    Observable<JsonObject> getAcceptedAssignment(@Query("id") int id);

    @GET("/api/MobileDelivery/GetBankName")
    Observable<JsonElement> getBankName();

    @FormUrlEncoded
    @POST("/token")
    Observable<JsonObject> getToken(@Field("grant_type") String grant_type, @Field("username") String username, @Field("password") String password);

    @POST("/api/LatLng/SortOrdersInAssignment")
    Observable<JsonElement> getShortPath(@Body SortOrderModel sortOrderModel);

    @GET("/api/Companys/GetCompanyDetails")
    Observable<JsonElement> getCompanyInfo();

    @GET("/api/HDFCPayment/GetRSA")
    Observable<String> getRSAKey(@Query("hdfcOrderId") String hdfcOrderId, @Query("amount") String amount);

    @GET("/api/Customers/CheckCustomerCriticalInfo")
    Observable<String> getcustInfo(@Query("customerId") int customerId);

    @POST("/api/OrderMastersAPI/InsertOnlineTransactionV2")
    Observable<Boolean> getInsertOnlineTransaction(@Body PaymentReq paymentReq);

    @GET("/api/DeliveryTask/OTPExistsForOrder")
    Observable<JsonElement> checkOrderOtpExist(@Query("OrderId") int orderId, @Query("Status") String status, @Query("comments") String comments);

    @GET("/api/MobileDelivery/GetDeliveryAppVersion")
    Observable<JsonElement> getCurrentVersion();

    @GET("/api/DeliveryTask/GenerateOTPForOrder")
    Observable<JsonElement> getOtpForOrder(@Query("OrderId") int orderId, @Query("Status") String status, @Query("lat") double lat, @Query("lg") double lg);

    @GET("/api/DeliveryTask/ValidateOTPForOrder")
    Observable<JsonElement> validateOrderOtp(@Query("OrderId") int orderId, @Query("Status") String status, @Query("otp") String otp);

    @GET("/api/Customers/IsChequeAccepted")
    Observable<JsonElement> getChequepermission(@Query("CustomerId") int CustomerId);

    @GET("/api/KKReturnReplace/GetReturnReplaceOrderForDBoy")
    Observable<JsonElement> getReturnOrderList(@Query("DboyId") int dBoyId);

    @GET("/api/KKReturnReplace/GetWarehouseRejectOrderForDBoy")
    Observable<JsonElement> getWarehouseRejectOrderList(@Query("DboyId") int dBoyId);

    @GET("/api/DeliveryTask/VerifyDeliveryCashAmount")
    Observable<Double> checkPayment(@Query("OrderId") int OrderId);

    @GET("/api/DeliveryTask/GetDeclineAssignmentOrder")
    Observable<JsonElement> GetDeclineAssignment(@Query("DeliveryIssuanceId") int DeliveryIssuanceId, @Query("mob") String Mobile);

    @GET("api/DeliveryTask/GetRejectedAssignment")
    Observable<JsonObject> GetRejectedAssignment(@Query("id") int id);

    @GET("api/DeliveryTask/DeclineOrderDetail")
    Observable<JsonObject> GetDeclineOrderDetail(@Query("OrderId") int OrderId);

    @GET("api/KKReturnReplaceHistory/GetReturnReplaceItemList")
    Observable<JsonElement> getReturnReplaceItemList(@Query("KKRequestId") int KKRequestId);

    @GET("/api/DBoyAssignmentDeposit/GetDBoySattlementAssignment")
    Observable<JsonElement> GetAssginmentToSettle(@Query("BoyId") int BoyId);

    @GET("/api/DBoyAssignmentDeposit/getSignedDepositSlip")
    Observable<JsonElement> GetAssginmentHistoryToSettle(@Query("SlipId") int SlipId, @Query("AssignmentId") int AssignmentId, @Query("DboyId") int DboyId,
                                                         @Query("StartDate") String StartDate, @Query("EndDate") String EndDate, @Query("PageNumber") int PageNumber, @Query("PageSize") int PageSize);

    @POST("/api/DBoyAssignmentDeposit/UpdateAssignment")
    Observable<JsonObject> PostAssginmentToSettle(@Body AssginmentSettleResponseModel model);

    @Multipart
    @POST("/api/MobileDelivery/CurrencyUploadChequeImageForMobile")
    Observable<String> UploadCheque(@Part MultipartBody.Part image);

    @Multipart
    @POST("/api/AssignmentCopyUpload")
    Observable<JsonObject> AssignmentCopyUpload(@Part MultipartBody.Part image);

    @Multipart
    @POST("/api/DBoyAssignmentDeposit/UploadDboySignature")
    Observable<String> UploadDboySignature(@Part MultipartBody.Part image);

    @Multipart
    @POST("/api/itemimageupload/UploadKKReturnReplaceImages")
    Observable<String> uploadKKReturnImage(@Part MultipartBody.Part image);

    @GET("api/KKReturnReplace/ChangeStatus")
    Observable<JsonElement> updateReturnRequestStatus(@Query("KKReturnReplaceId") int KKRequestId, @Query("Status") String status, @Query("dboyId") int dBoyId, @Query("picker_comment") String pickerComment, @Query("ReturnReplaceImage") String returnReplaceImage);

    @GET("/api/DeliveryTask/cashcollectionbydboy")
    Observable<JsonElement> getcashCollection(@Query("mob") String mobile,@Query("page") int page,@Query("list") int limit);

    @GET("/api/DeliveryTask/creditcallection")
    Observable<JsonElement> getcreditCollection(@Query("mob") String mobile,@Query("page") int page,@Query("list") int limit);


}