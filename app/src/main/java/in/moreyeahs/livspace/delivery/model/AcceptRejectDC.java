package in.moreyeahs.livspace.delivery.model;

import com.google.gson.annotations.SerializedName;

public class AcceptRejectDC {
    @SerializedName("POAssignmentId")
    public int POAssignmentId;
    @SerializedName("Acceptance")
    public Boolean Acceptanc;
    @SerializedName("Reason")
    public String Reason;

    public AcceptRejectDC(int POAssignmentId, Boolean acceptanc, String reason) {
        this.POAssignmentId = POAssignmentId;
        Acceptanc = acceptanc;
        Reason = reason;
    }

    public int getPOAssignmentId() {
        return POAssignmentId;
    }

    public void setPOAssignmentId(int POAssignmentId) {
        this.POAssignmentId = POAssignmentId;
    }

    public Boolean getAcceptanc() {
        return Acceptanc;
    }

    public void setAcceptanc(Boolean acceptanc) {
        Acceptanc = acceptanc;
    }

    public String getReason() {
        return Reason;
    }

    public void setReason(String reason) {
        Reason = reason;
    }
}