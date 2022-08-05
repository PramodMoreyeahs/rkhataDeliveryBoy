package in.moreyeahs.livspace.delivery.model;

public class LatLongModel {
    public double lat;
    public double Long;
    public int SalesPersonId;
    public String State;
    public String City;
    public String Pincode;
    public String Address;


    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getPincode() {
        return Pincode;
    }

    public void setPincode(String pincode) {
        Pincode = pincode;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLong() {
        return Long;
    }

    public void setLong(double aLong) {
        Long = aLong;
    }

    public int getSalesPersonId() {
        return SalesPersonId;
    }

    public void setSalesPersonId(int salesPersonId) {
        SalesPersonId = salesPersonId;
    }

    public LatLongModel(double lat, double aLong, int salesPersonId, String state, String city, String pincode, String address) {
        this.lat = lat;
        Long = aLong;
        SalesPersonId = salesPersonId;
        State = state;
        City = city;
        Pincode = pincode;
        Address = address;
    }
}
