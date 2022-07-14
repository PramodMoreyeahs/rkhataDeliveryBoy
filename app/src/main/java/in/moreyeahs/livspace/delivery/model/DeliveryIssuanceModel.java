package in.moreyeahs.livspace.delivery.model;

public class DeliveryIssuanceModel {

    private String DeliveryIssuanceId;

    public DeliveryIssuanceModel(String deliveryIssuanceId) {
        DeliveryIssuanceId = deliveryIssuanceId;
    }

    public String getDeliveryIssuanceId() {
        return DeliveryIssuanceId;
    }

    public void setDeliveryIssuanceId(String deliveryIssuanceId) {
        DeliveryIssuanceId = deliveryIssuanceId;
    }
}
