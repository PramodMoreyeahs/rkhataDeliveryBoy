package in.moreyeahs.livspace.delivery.listener;

import android.widget.ImageView;

import in.moreyeahs.livspace.delivery.model.PostChequeCollectionModel;

import java.util.ArrayList;

public interface ChequeImageListener {
    void onImageClick(ImageView imageView, int position);
    void onButtonClick(ArrayList<PostChequeCollectionModel> list, boolean save);
}
