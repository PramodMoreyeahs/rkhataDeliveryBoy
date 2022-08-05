package in.moreyeahs.livspace.delivery.model;

import java.io.Serializable;

public  class SortedOrdersModel implements Serializable {

    private double Distance;
    private int OrderId;
    private double Lat;
    private double Lng;

    public SortedOrdersModel(double distance, int orderId, double lat, double lng) {
        Distance = distance;
        OrderId = orderId;
        Lat = lat;
        Lng = lng;
    }
    SortedOrdersModel(){}

    public double getDistance() {
        return Distance;
    }

    public void setDistance(double distance) {
        Distance = distance;
    }

    public int getOrderId() {
        return OrderId;
    }

    public void setOrderId(int orderId) {
        OrderId = orderId;
    }

    public double getLat() {
        return Lat;
    }

    public void setLat(double lat) {
        Lat = lat;
    }

    public double getLng() {
        return Lng;
    }

    public void setLng(double lng) {
        Lng = lng;
    }


}
