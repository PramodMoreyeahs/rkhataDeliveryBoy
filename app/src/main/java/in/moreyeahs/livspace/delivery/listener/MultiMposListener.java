package in.moreyeahs.livspace.delivery.listener;

import in.moreyeahs.livspace.delivery.model.DeliveryPayments;

import java.util.ArrayList;

public interface MultiMposListener {


    void onMposAmountChange(ArrayList<DeliveryPayments> payment);


}
