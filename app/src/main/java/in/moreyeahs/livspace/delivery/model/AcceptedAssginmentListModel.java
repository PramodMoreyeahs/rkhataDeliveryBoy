package in.moreyeahs.livspace.delivery.model;

import com.google.gson.annotations.SerializedName;

public  class AcceptedAssginmentListModel {

    @SerializedName("AssignmentDate")
    private String AssignmentDate;

    @SerializedName("End")
    private boolean End;

    @SerializedName("Start")
    private boolean Start;

    @SerializedName("StartDateTime")
    private String StartDateTime;

    @SerializedName("DeliveryIssuanceId")
    private int DeliveryIssuanceId;

    public String getAssignmentDate() {
        return AssignmentDate;
    }

    public void setAssignmentDate(String AssignmentDate) {
        this.AssignmentDate = AssignmentDate;
    }

    public boolean getEnd() {
        return End;
    }

    public void setEnd(boolean End) {
        this.End = End;
    }

    public boolean getStart() {
        return Start;
    }

    public void setStart(boolean Start) {
        this.Start = Start;
    }

    public String getStartDateTime() {
        return StartDateTime;
    }

    public void setStartDateTime(String StartDateTime) {
        this.StartDateTime = StartDateTime;
    }

    public int getDeliveryIssuanceId() {
        return DeliveryIssuanceId;
    }

    public void setDeliveryIssuanceId(int DeliveryIssuanceId) {
        this.DeliveryIssuanceId = DeliveryIssuanceId;
    }
}
