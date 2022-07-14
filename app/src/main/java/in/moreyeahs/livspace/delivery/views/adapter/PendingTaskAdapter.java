package in.moreyeahs.livspace.delivery.views.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import in.moreyeahs.livspace.delivery.R;
import in.moreyeahs.livspace.delivery.databinding.ItemPeddingListBinding;
import in.moreyeahs.livspace.delivery.listener.AcceptClickInterface;
import in.moreyeahs.livspace.delivery.model.PendingModel;
import in.moreyeahs.livspace.delivery.utilities.CommonMethods;
import in.moreyeahs.livspace.delivery.utilities.DateUtils;
import in.moreyeahs.livspace.delivery.utilities.Logger;
import in.moreyeahs.livspace.delivery.viewmodels.PendingTaskModel;

import java.util.List;

public class PendingTaskAdapter extends RecyclerView.Adapter<PendingTaskAdapter.ViewHolder> {
    private Context context;
    private List<PendingModel> pendingDetailsList;
    private LayoutInflater layoutInflater;
    private Boolean onClickValue = true;
    private Boolean isShortItemClick = true;
    private AcceptClickInterface acceptInterface;

    public PendingTaskAdapter(Context context, List<PendingModel> pendingDetailsList, AcceptClickInterface acceptInterface) {
        this.context = context;
        this.pendingDetailsList = pendingDetailsList;
        this.acceptInterface = acceptInterface;
    }

    @NonNull
    @Override
    public PendingTaskAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(viewGroup.getContext());
        }
        return new ViewHolder(DataBindingUtil.inflate(layoutInflater, R.layout.item_pedding_list, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PendingTaskAdapter.ViewHolder viewHolder, int i) {
        try {
            PendingModel pendingDetailsModel = pendingDetailsList.get(i);
            PendingTaskModel pendingTaskModel = new PendingTaskModel();
            String str = pendingDetailsModel.getOrderIds();
            String[] arrOfStr = str.split(",");
            pendingTaskModel.setAssignmentID(context.getString(R.string.assignment_id) + pendingDetailsModel.getDeliveryIssuanceId());
            pendingTaskModel.setOrderNo(context.getString(R.string.oreder_no) + arrOfStr.length);
            pendingTaskModel.setDateTime(pendingDetailsModel.getCreatedDate());
            if (pendingDetailsModel.getCreatedDate() != null && !pendingDetailsModel.getCreatedDate().isEmpty()) {
                pendingTaskModel.setDateTime(DateUtils.getDateFormat(pendingDetailsModel.getCreatedDate()));
            }
            viewHolder.bind(pendingTaskModel);

            if (pendingDetailsModel.getShortIssuanceModelArrayList() == null) {
                viewHolder.mBinding.llShortItem.setVisibility(View.GONE);
            }

            viewHolder.mBinding.llOrderList.setOnClickListener(v -> {
                try {
                    if (onClickValue) {
                        if (!isShortItemClick) {
                            sortItemGone(viewHolder);
                            isShortItemClick = true;
                        } else {
                            oderListShowView(viewHolder, pendingDetailsModel);
                            onClickValue = false;
                        }
                    } else {
                        orderListViewGone(viewHolder);
                        onClickValue = true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            viewHolder.mBinding.llShortItem.setOnClickListener(v -> {
                if (isShortItemClick) {
                    if (!onClickValue) {
                        orderListViewGone(viewHolder);
                        onClickValue = true;
                    } else {
                        isShortItemClick = false;
                        sortItemShow(viewHolder, pendingDetailsModel);
                    }
                } else {
                    sortItemGone(viewHolder);
                    isShortItemClick = true;
                }
            });
            viewHolder.mBinding.btAccept.setOnClickListener(v -> acceptInterface.acceptClicked(pendingDetailsModel.getDeliveryIssuanceId(), "true"));

            viewHolder.mBinding.llReject.setOnClickListener(v -> acceptInterface.rejectClicked(pendingDetailsModel.getDeliveryIssuanceId(), "false"));
        } catch (Exception e) {
            Logger.e(CommonMethods.getTag(this), e.toString());
        }
    }

    private void sortItemShow(ViewHolder viewHolder, PendingModel pendingDetailsModel) {
        viewHolder.mBinding.tvSortItem.setText("Hide");
        viewHolder.orderListRV = viewHolder.mBinding.rvOrderList;
        viewHolder.orderListRV.setVisibility(View.VISIBLE);
        viewHolder.mBinding.llSerialNo.setVisibility(View.VISIBLE);
        viewHolder.orderListRV.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        viewHolder.orderListRV.setHasFixedSize(true);
        if (pendingDetailsModel.getShortIssuanceModelArrayList().size() > 0) {
            ShortIssuanceAdapter shortIssuanceAdapter = new ShortIssuanceAdapter(context, pendingDetailsModel.getShortIssuanceModelArrayList());
            viewHolder.orderListRV.setAdapter(shortIssuanceAdapter);
        }
    }

    private void sortItemGone(ViewHolder viewHolder) {
        try {
            viewHolder.orderListRV.setVisibility(View.GONE);
            viewHolder.mBinding.llSerialNo.setVisibility(View.GONE);
            viewHolder.mBinding.tvSortItem.setText("Short item");
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void oderListShowView(ViewHolder viewHolder, PendingModel pendingDetailsModel) {
        viewHolder.mBinding.btOrderList.setText(context.getString(R.string.hide));
        viewHolder.orderListRV = viewHolder.mBinding.rvOrderList;
        viewHolder.orderListRV.setVisibility(View.VISIBLE);
        viewHolder.mBinding.llSerialNo.setVisibility(View.VISIBLE);
        viewHolder.orderListRV.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        viewHolder.orderListRV.setHasFixedSize(true);
        OrderListAdapter orderListAdapter = new OrderListAdapter(context, pendingDetailsModel.getPendingDetailsModels());
        viewHolder.orderListRV.setAdapter(orderListAdapter);
    }

    @Override
    public int getItemCount() {
        if (pendingDetailsList != null)
            return pendingDetailsList.size();
        else
            return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemPeddingListBinding mBinding;
        RecyclerView orderListRV;

        public ViewHolder(ItemPeddingListBinding mBinding) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
        }

        public void bind(PendingTaskModel item) {
            mBinding.setPendingTaskModel(item);
            mBinding.executePendingBindings();
        }
    }

    private void orderListViewGone(ViewHolder viewHolder) {
        try {
            viewHolder.orderListRV.setVisibility(View.GONE);
            viewHolder.mBinding.llSerialNo.setVisibility(View.GONE);
            viewHolder.mBinding.btOrderList.setText("Order List");
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}