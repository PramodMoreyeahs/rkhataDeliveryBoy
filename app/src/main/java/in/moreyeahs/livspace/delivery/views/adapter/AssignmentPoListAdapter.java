package in.moreyeahs.livspace.delivery.views.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import in.moreyeahs.livspace.delivery.R;
import in.moreyeahs.livspace.delivery.databinding.AssignmentPoAdapterBinding;
import in.moreyeahs.livspace.delivery.model.AssignmentDetailsModel;
import in.moreyeahs.livspace.delivery.model.PoListResponse;
import in.moreyeahs.livspace.delivery.utilities.Utils;
import in.moreyeahs.livspace.delivery.views.main.AssignmentGrnActivity;


public class AssignmentPoListAdapter extends RecyclerView.Adapter<AssignmentPoListAdapter.ViewHolder> {
    private LayoutInflater layoutInflater;
    Context context;
    private List<PoListResponse> assignmentList = new ArrayList<>();
    PoListInterface poInterface;


    public AssignmentPoListAdapter(Context context, PoListInterface poInterface) {
        this.context = context;
        this.poInterface = poInterface;
    }

    public void setResponseList(List<PoListResponse> assignmentList) {
        this.assignmentList = assignmentList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AssignmentPoListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(viewGroup.getContext());
        }
        AssignmentPoAdapterBinding mBinding = DataBindingUtil.inflate(layoutInflater, R.layout.assignment_po_adapter, viewGroup, false);
        return new ViewHolder(mBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull AssignmentPoListAdapter.ViewHolder viewHolder, int i) {
        try {
            PoListResponse obj = assignmentList.get(i);
            viewHolder.mBinding.tvPoId.setText("Po Id: " + obj.getPurchaseorderid());
            viewHolder.mBinding.orderStatuTxt.setText(obj.getStatus());
            viewHolder.mBinding.price.setText("₹" + new DecimalFormat("#.00").format(obj.getEtotalamount()));
            viewHolder.mBinding.createdDate.setText(Utils.getDateFormat(obj.getCreationdate()));
            viewHolder.mBinding.tvSupplierName.setText(obj.getSuppliername());
            if (obj.getSupplierAddress() != null && !obj.getSupplierAddress().isEmpty()) {
                viewHolder.mBinding.tvAddress.setVisibility(View.VISIBLE);
                viewHolder.mBinding.tvAddress.setText("Address: " + obj.getSupplierAddress());
            } else {
                viewHolder.mBinding.tvAddress.setVisibility(View.GONE);
            }

            viewHolder.mBinding.tvAdvAmt.setText("Adv amt: ₹" + new DecimalFormat("#.00").format(obj.getAdvanceAmt()));
            viewHolder.mBinding.tvRemAmt.setText("Rem Amt: ₹" + new DecimalFormat("#.00").format(obj.getEtotalamount() - obj.getAdvanceAmt()));

            if (obj.getStatus().equalsIgnoreCase("shipped")) {
                viewHolder.mBinding.viewDetails.setText("View details");
                viewHolder.mBinding.viewDetails.setOnClickListener(V -> {
                    context.startActivity(new Intent(context, AssignmentGrnActivity.class).putExtra("PO_ID", obj.getPurchaseorderid()));
                });
            } else if (obj.getStatus().equalsIgnoreCase("Partial Received") || obj.getStatus().equalsIgnoreCase("Received")) {
                viewHolder.mBinding.viewDetails.setText("Get E-Way bill");
                viewHolder.mBinding.viewDetails.setOnClickListener(V -> {
                    poInterface.getEWayBill(obj.getPurchaseorderid());
                });
            }

            if (obj.getSupplierMobileMo() > 0) {
                viewHolder.mBinding.callSupplier.setVisibility(View.VISIBLE);
                viewHolder.mBinding.callSupplier.setOnClickListener(V -> {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "+91" + obj.getSupplierMobileMo()));
                    context.startActivity(intent);
                });
            } else {
                viewHolder.mBinding.callSupplier.setVisibility(View.GONE);
            }

            viewHolder.mBinding.btnDirection.setOnClickListener(V -> {
                if (!Utils.checkInternetConnection(context)) {
                    Utils.setToast(context, context.getResources().getString(R.string.network_error));
                } else {
                    LatLng latlong = new LatLng(obj.getLatitude(), obj.getLongitude());
                    if (obj.getLatitude() != 0.0 && obj.getLongitude() != 0.0) {
                        Intent navigation = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=" + obj.getLatitude() + "," + obj.getLongitude()));
                        context.startActivity(navigation);
                    } else {
                        Intent navigation = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=" + latlong.latitude + "," + latlong.longitude));
                        context.startActivity(navigation);
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface PoListInterface {
        void getEWayBill(int poNo);
    }

    @Override
    public int getItemCount() {
        return assignmentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        AssignmentPoAdapterBinding mBinding;

        public ViewHolder(AssignmentPoAdapterBinding mBinding) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
        }
    }
}