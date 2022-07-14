package in.moreyeahs.livspace.delivery.views.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import in.moreyeahs.livspace.delivery.R;
import in.moreyeahs.livspace.delivery.databinding.ItemCashCollectionBinding;
import in.moreyeahs.livspace.delivery.databinding.ItemPeddingListBinding;
import in.moreyeahs.livspace.delivery.listener.AcceptClickInterface;
import in.moreyeahs.livspace.delivery.model.CashCollectionResponse;
import in.moreyeahs.livspace.delivery.model.OrderItemDetails;
import in.moreyeahs.livspace.delivery.model.PendingModel;
import in.moreyeahs.livspace.delivery.utilities.CommonMethods;
import in.moreyeahs.livspace.delivery.utilities.DateUtils;
import in.moreyeahs.livspace.delivery.utilities.Logger;
import in.moreyeahs.livspace.delivery.utilities.Utils;
import in.moreyeahs.livspace.delivery.viewmodels.PendingTaskModel;

public class CashCollectionAdapter extends RecyclerView.Adapter<CashCollectionAdapter.ViewHolder> {
    private Context context;
    private List<CashCollectionResponse.ListData> pendingDetailsList;
    private LayoutInflater layoutInflater;
    private Boolean onClickValue = true;
    private Boolean isShortItemClick = true;
    private AcceptClickInterface acceptInterface;
    private ArrayList<OrderItemDetails> orderList;
    private RecyclerView recyclerView;


    public CashCollectionAdapter(Context context, List<CashCollectionResponse.ListData> pendingDetailsList, AcceptClickInterface acceptInterface) {
        this.context = context;
        this.pendingDetailsList = pendingDetailsList;
        this.acceptInterface = acceptInterface;
    }

    @NonNull
    @Override
    public CashCollectionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(viewGroup.getContext());
        }
        return new ViewHolder(DataBindingUtil.inflate(layoutInflater, R.layout.item_cash_collection, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CashCollectionAdapter.ViewHolder viewHolder, int i) {
        try {
            CashCollectionResponse.ListData pendingDetailsModel = pendingDetailsList.get(i);
        //    viewHolder.bind(pendingDetailsModel);
            if(pendingDetailsModel.getOrderDetails().isEmpty() || pendingDetailsModel.getOrderDetails()==null)
            {
                viewHolder.mBinding.tableLayout.setVisibility(View.GONE);
            }
            else
            {
                viewHolder.mBinding.tableLayout.setVisibility(View.VISIBLE);
            }
            recyclerView=viewHolder.itemView.findViewById(R.id.rv_item);
            if(pendingDetailsModel.getPaymentStatus().equals("Pending"))
            {
            viewHolder.mBinding.tvAmountStatus.setTextColor(Color.parseColor("#FF6347"));
            }
            else
            {
                viewHolder.mBinding.tvAmountStatus.setTextColor(Color.parseColor("#38a561"));
            }
            viewHolder.mBinding.tvDob.setText(""+ Utils.getDateFormat(pendingDetailsModel.getPaymentDate()));
            viewHolder.mBinding.tvName.setText(pendingDetailsModel.getCustomerName());
         //   viewHolder.mBinding.tvAmount.setText("₹ "+pendingDetailsModel.getAmount());
            viewHolder.mBinding.tvShopname.setText(pendingDetailsModel.getShopName());
            viewHolder.mBinding.tvOrderId.setText(""+pendingDetailsModel.getOrderId());
            viewHolder.mBinding.status.setText(""+pendingDetailsModel.getStatus());
            viewHolder.mBinding.tvAmountStatus.setText(""+pendingDetailsModel.getPaymentStatus());
            sortItemShow(viewHolder,pendingDetailsModel);
          viewHolder.mBinding.totalPrice.setText(""+pendingDetailsModel.getAmount());


        } catch (Exception e) {
            Logger.e(CommonMethods.getTag(this), e.toString());
        }
    }

    private void sortItemShow(ViewHolder viewHolder,CashCollectionResponse.ListData pendingDetailsModel ) {
      //  viewHolder.mBinding.tvSortItem.setText("Hide");
        viewHolder.orderListRV = viewHolder.mBinding.rvItem;
        viewHolder.quantity=viewHolder.mBinding.totalQ;
         int qty=0;
        viewHolder.orderListRV.setVisibility(View.VISIBLE);
      //  viewHolder.mBinding.llSerialNo.setVisibility(View.VISIBLE);
        viewHolder.orderListRV.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        viewHolder.orderListRV.setHasFixedSize(true);
        if (pendingDetailsModel.getOrderDetails().size() > 0) {
            MyItemCashDetailsAdapter myItemDetailsAdapter = new MyItemCashDetailsAdapter(context, pendingDetailsModel.getOrderDetails());
            viewHolder.orderListRV.setAdapter(myItemDetailsAdapter);
        }
        for(int j=0;j<pendingDetailsModel.getOrderDetails().size();j++)
        {
            qty = qty+ pendingDetailsModel.getOrderDetails().get(j).getQty();
        }
        viewHolder.quantity.setText(""+qty);
    }

   /* private void sortItemShow(ViewHolder viewHolder, CashCollectionResponse.Data.ListData pendingDetailsModel) {
        viewHolder.mBinding.tvSortItem.setText("Hide");
        viewHolder.orderListRV = viewHolder.mBinding.rvOrderList;
        viewHolder.orderListRV.setVisibility(View.VISIBLE);
        viewHolder.mBinding.llSerialNo.setVisibility(View.VISIBLE);
        viewHolder.orderListRV.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        viewHolder.orderListRV.setHasFixedSize(true);
        if (pendingDetailsModel.getOrderDetails().size() > 0) {
            ShortIssuanceAdapter shortIssuanceAdapter = new ShortIssuanceAdapter(context, pendingDetailsModel.getOrderDetails());
            viewHolder.orderListRV.setAdapter(shortIssuanceAdapter);
        }
    }*/

   /* private void sortItemGone(ViewHolder viewHolder) {
        try {
            viewHolder.orderListRV.setVisibility(View.GONE);
            viewHolder.mBinding.llSerialNo.setVisibility(View.GONE);
            viewHolder.mBinding.tvSortItem.setText("Short item");
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }*/

  /*  private void oderListShowView(ViewHolder viewHolder,  CashCollectionResponse.Data.ListData pendingDetailsModel) {
        viewHolder.mBinding.btOrderList.setText(context.getString(R.string.hide));
        viewHolder.orderListRV = viewHolder.mBinding.rvOrderList;
        viewHolder.orderListRV.setVisibility(View.VISIBLE);
        viewHolder.mBinding.llSerialNo.setVisibility(View.VISIBLE);
        viewHolder.orderListRV.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        viewHolder.orderListRV.setHasFixedSize(true);
        OrderListAdapter orderListAdapter = new OrderListAdapter(context, pendingDetailsModel.getOrderDetails());
        viewHolder.orderListRV.setAdapter(orderListAdapter);
    }*/

    @Override
    public int getItemCount() {
        if (pendingDetailsList != null)
            return pendingDetailsList.size();
        else
            return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemCashCollectionBinding mBinding;
        RecyclerView orderListRV;
        TextView quantity;

        public ViewHolder(ItemCashCollectionBinding mBinding) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
        }


    }

   /* private void orderListViewGone(ViewHolder viewHolder) {
        try {
            viewHolder.orderListRV.setVisibility(View.GONE);
            viewHolder.mBinding.llSerialNo.setVisibility(View.GONE);
            viewHolder.mBinding.btOrderList.setText("Order List");
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }*/
}