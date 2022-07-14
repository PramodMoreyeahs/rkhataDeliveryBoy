package in.moreyeahs.livspace.delivery.listener;

import android.widget.ImageView;

import in.moreyeahs.livspace.delivery.model.DeliveryPayments;

import java.util.ArrayList;

public interface OrderchequeimageListener {
    void onImageClick(ImageView imageView,int position);
    void onAmountChange(ArrayList<DeliveryPayments> payment);



}
