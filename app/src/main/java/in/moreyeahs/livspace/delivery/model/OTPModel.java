package in.moreyeahs.livspace.delivery.model;

import com.google.gson.annotations.SerializedName;

public class OTPModel {

    @SerializedName("Mobile")
    private String Mobile;

    @SerializedName("DeviceId")
    private String DeviceId ;

    @SerializedName("FcmId")
    private String FcmId ;

    @SerializedName("CurrentAPKversion")
    private String CurrentAPKversion ;

    @SerializedName("PhoneOSversion")
    private String PhoneOSversion ;

    @SerializedName("UserDeviceName")
    private String UserDeviceName ;

    @SerializedName("IMEI")
    private String IMEI;
    public OTPModel(String mobile, String deviceId, String fcmId, String currentAPKversion, String phoneOSversion, String userDeviceName, String IMEI) {
        this. Mobile = mobile;
        this. DeviceId = deviceId;
        this. FcmId = fcmId;
        this.CurrentAPKversion = currentAPKversion;
        this.PhoneOSversion = phoneOSversion;
        this.UserDeviceName = userDeviceName;
        this.IMEI = IMEI;
    }
}
