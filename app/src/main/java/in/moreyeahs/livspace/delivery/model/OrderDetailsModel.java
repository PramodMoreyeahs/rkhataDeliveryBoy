package in.moreyeahs.livspace.delivery.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public  class OrderDetailsModel {

    @SerializedName("status")
    private boolean status;

    @SerializedName("Message")
    private String Message;

    @SerializedName("OrderDispatchedObj")
    private  OrderDispatchedObj OrderDispatchedObj;

    @SerializedName("Payments")
    private  ArrayList<Payments>payment;

    public ArrayList<Payments> getPayment() {
        return payment;
    }

    public void setPayment(ArrayList<Payments> payment) {
        this.payment = payment;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public in.moreyeahs.livspace.delivery.model.OrderDispatchedObj getOrderDispatchedObj() {
        return OrderDispatchedObj;
    }

    public void setOrderDispatchedObj(in.moreyeahs.livspace.delivery.model.OrderDispatchedObj orderDispatchedObj) {
        OrderDispatchedObj = orderDispatchedObj;
    }
}
