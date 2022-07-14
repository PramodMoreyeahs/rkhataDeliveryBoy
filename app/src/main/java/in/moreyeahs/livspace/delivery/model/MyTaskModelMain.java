package in.moreyeahs.livspace.delivery.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MyTaskModelMain {
    @SerializedName("status")
    private boolean status;
    @SerializedName("Message")
    private String Message;
    @SerializedName("OrderDispatchedObj")
    public ArrayList<MyTaskModel> OrderDispatchedObj;

    public boolean getStatus() {
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

    public ArrayList<MyTaskModel> getOrderDispatchedObj() {
        return OrderDispatchedObj;
    }

    public void setOrderDispatchedObj(ArrayList<MyTaskModel> orderDispatchedObj) {
        OrderDispatchedObj = orderDispatchedObj;
    }
}
