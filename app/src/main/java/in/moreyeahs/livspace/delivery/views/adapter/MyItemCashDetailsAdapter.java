package in.moreyeahs.livspace.delivery.views.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.moreyeahs.livspace.delivery.R;
import in.moreyeahs.livspace.delivery.databinding.ItemDetailsAdapterBinding;
import in.moreyeahs.livspace.delivery.model.CashCollectionResponse;
import in.moreyeahs.livspace.delivery.model.OrderItemDetails;
import in.moreyeahs.livspace.delivery.viewmodels.ItemDetailsInfo;


public class MyItemCashDetailsAdapter extends RecyclerView.Adapter<MyItemCashDetailsAdapter.ViewHolder> {

    private Context context;
    private ArrayList<CashCollectionResponse.ListData.OrderDetails> itemList;
    private LayoutInflater layoutInflater;
    public MyItemCashDetailsAdapter(Context context, ArrayList<CashCollectionResponse.ListData.OrderDetails> itemList) {
        this.context = context;
        this.itemList = itemList;
    }
    public void setItemDetailsList(ArrayList<CashCollectionResponse.ListData.OrderDetails> itemList) {
        this.itemList = itemList;
        notifyDataSetChanged();
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        ItemDetailsAdapterBinding mBinding = DataBindingUtil.inflate(layoutInflater, R.layout.item_details_adapter, parent, false);
        return new ViewHolder(mBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyItemCashDetailsAdapter.ViewHolder viewHolder, int i) {
        try {
            /**
             * set value
             * ***/
            CashCollectionResponse.ListData.OrderDetails orderItemDetails = itemList.get(i);
            ItemDetailsInfo info = new ItemDetailsInfo();
           // String sPRICE = new DecimalFormat("##.##").format(orderItemDetails.getPrice());
            info.setItemName(orderItemDetails.getItemname());
            info.setPrice(String.valueOf(orderItemDetails.getPrice()));
            info.setQty(String.valueOf(orderItemDetails.getQty()));
            info.setSrNo(String.valueOf(i+1));
            viewHolder.bind(info);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return itemList == null ? 0 : itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemDetailsAdapterBinding mBinding;

        public ViewHolder(ItemDetailsAdapterBinding mBinding)
        {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
        }

        public void bind(ItemDetailsInfo item)
        {
            mBinding.setItemDetailsInfo(item);
            mBinding.executePendingBindings();
        }
    }
}

