package in.moreyeahs.livspace.delivery.listener;

import in.moreyeahs.livspace.delivery.model.AssginmentSettleResponseModel;

import java.util.ArrayList;

public interface AssginmentSettleViewDetailInterface {
    void viewDetailClick(int deliveryIssuanceId);

    void saveCommentClick(ArrayList<AssginmentSettleResponseModel.DBoyAssignmentDeposits> deliveryIssuanceId);


}
