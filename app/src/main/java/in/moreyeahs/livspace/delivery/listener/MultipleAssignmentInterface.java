package in.moreyeahs.livspace.delivery.listener;

import in.moreyeahs.livspace.delivery.model.SortOrderModel;

public interface MultipleAssignmentInterface {

    public void StartTimer(int deliveryIssuanceId);
    public void Details(int deliveryIssuanceId, boolean isTimerstart,int i);
    public void shortPathData(SortOrderModel sortOrderModel, int postion);


}
