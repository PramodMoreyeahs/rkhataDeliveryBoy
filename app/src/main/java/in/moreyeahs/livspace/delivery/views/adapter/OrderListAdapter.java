package in.moreyeahs.livspace.delivery.views.adapter;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import in.moreyeahs.livspace.delivery.R;
import in.moreyeahs.livspace.delivery.databinding.ItemOrderListBinding;
import in.moreyeahs.livspace.delivery.model.PendingModel;
import in.moreyeahs.livspace.delivery.viewmodels.OrderListModel;

import java.util.List;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.ViewHolder> {

    private Context context;
    private List<PendingModel.PendingDetailsModel> pendingDetailsList;
    private LayoutInflater layoutInflater;

    public OrderListAdapter(Context context, List<PendingModel.PendingDetailsModel> pendingDetailsList) {
        this.context = context;
        this.pendingDetailsList = pendingDetailsList;
    }

    @NonNull
    @Override
    public OrderListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(viewGroup.getContext());
        }
        ItemOrderListBinding mBinding = DataBindingUtil.inflate(layoutInflater, R.layout.item_order_list, viewGroup, false);
        return new OrderListAdapter.ViewHolder(mBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderListAdapter.ViewHolder viewHolder, int i) {
        PendingModel.PendingDetailsModel detailsModel = pendingDetailsList.get(i);
        OrderListModel orderListModel = new OrderListModel();
        orderListModel.setSerialNO(String.valueOf(i+1));
        orderListModel.setProductName(detailsModel.itemname);
        orderListModel.setQuantity(String.valueOf(detailsModel.qty));
        viewHolder.bind(orderListModel);


    }

    @Override
    public int getItemCount() {

        if (pendingDetailsList != null)
            return pendingDetailsList.size();
        else
            return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemOrderListBinding mBinding;

        public ViewHolder(ItemOrderListBinding mBinding) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
        }

        public void bind(OrderListModel item) {
            mBinding.setOrderListModel(item);
            mBinding.executePendingBindings();
        }
    }
}
