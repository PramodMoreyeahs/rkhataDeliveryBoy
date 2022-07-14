package in.moreyeahs.livspace.delivery.model;

import com.google.gson.annotations.SerializedName;

public class DeliveryPayments {

    @SerializedName("OrderId")
    private int OrderId;

    @SerializedName("TransId")
    private String TransId;

    @SerializedName("amount")
    private int amount;

    @SerializedName("PaymentFrom")
    private String PaymentFrom;

    @SerializedName("ChequeImageUrl")
    private String ChequeImageUrl;

    @SerializedName("ChequeBankName")
    private String ChequeBankName;


    @SerializedName("PaymentDate")
    private String PaymentDate;

   /* public DeliveryPayments(String paymentFrom) {
        PaymentFrom = paymentFrom;
    }*/

    public DeliveryPayments(int orderId, String transId, int amount, String paymentFrom, String chequeImageUrl, String chequeBankName, String paymentDate) {
        this. OrderId = orderId;
        this. TransId = transId;
        this.amount = amount;
        this. PaymentFrom = paymentFrom;
        this. ChequeImageUrl = chequeImageUrl;
        this.ChequeBankName = chequeBankName;
        this. PaymentDate = paymentDate;
    }
    public int getOrderId() {
        return OrderId;
    }

    public void setOrderId(int orderId) {
        OrderId = orderId;
    }

    public String getTransId() {
        return TransId;
    }

    public void setTransId(String transId) {
        TransId = transId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getPaymentFrom() {
        return PaymentFrom;
    }

    public void setPaymentFrom(String paymentFrom) {
        PaymentFrom = paymentFrom;
    }

    public String getChequeImageUrl() {
        return ChequeImageUrl;
    }

    public void setChequeImageUrl(String chequeImageUrl) {
        ChequeImageUrl = chequeImageUrl;
    }

    public String getChequeBankName() {
        return ChequeBankName;
    }

    public void setChequeBankName(String chequeBankName) {
        this. ChequeBankName = chequeBankName;
    }

    public String getPaymentDate() {
        return PaymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        PaymentDate = paymentDate;
    }
}
