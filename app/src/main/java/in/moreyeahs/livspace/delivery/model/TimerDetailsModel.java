package in.moreyeahs.livspace.delivery.model;

import com.google.gson.annotations.SerializedName;

public class TimerDetailsModel {

    @SerializedName("DeliveryIssuanceId")
    public int DeliveryIssuanceId  ;

    public TimerDetailsModel(int deliveryIssuanceId)
    {
        this.DeliveryIssuanceId = deliveryIssuanceId;

    }
}

