package in.moreyeahs.livspace.delivery.model;

import com.google.gson.annotations.SerializedName;

public class PaymentReq {

    @SerializedName("EncString")
    private String EncString;

    public PaymentReq(String encString) {
        EncString = encString;
    }
}