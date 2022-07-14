package in.moreyeahs.livspace.delivery.views.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.moreyeahs.livspace.delivery.R;
import in.moreyeahs.livspace.delivery.databinding.GrnItemsListBinding;
import in.moreyeahs.livspace.delivery.model.AssignmentDetailsModel;
import in.moreyeahs.livspace.delivery.utilities.Utils;


public class AssignmentGrnAdapter extends RecyclerView.Adapter<AssignmentGrnAdapter.ViewHolder> {
    private LayoutInflater layoutInflater;
    Context context;
    private List<AssignmentDetailsModel.PurdetailsEntity> assignmentList;
    AssignmentListInterface interfacee;


    public AssignmentGrnAdapter(Context context, List<AssignmentDetailsModel.PurdetailsEntity> assignmentList, AssignmentListInterface interfacee) {
        this.assignmentList = assignmentList;
        this.context = context;
        this.interfacee = interfacee;
    }

    @NonNull
    @Override
    public AssignmentGrnAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(viewGroup.getContext());
        }
        GrnItemsListBinding mBinding = DataBindingUtil.inflate(layoutInflater, R.layout.grn_items_list, viewGroup, false);
        return new ViewHolder(mBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull AssignmentGrnAdapter.ViewHolder viewHolder, int i) {
        try {
            AssignmentDetailsModel.PurdetailsEntity obj = assignmentList.get(i);

            viewHolder.mBinding.tvSrno.setText(String.valueOf(i + 1));

            viewHolder.mBinding.tvItemName.setText(obj.getItemname());
            viewHolder.mBinding.tvQuantity.setText(String.valueOf(obj.getSupplierAcceptQty()));
//            viewHolder.mBinding.etQty.setText(String.valueOf(obj.getTotalquantity()));

            viewHolder.mBinding.etQty.addTextChangedListener(new TextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {
                    // TODO Auto-generated method stub
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    // TODO Auto-generated method stub
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    viewHolder.mBinding.etComment.setVisibility(View.GONE);

                    if (s.toString().isEmpty()) {
                        obj.setQtyrecived1(0);
                    } else if (Integer.parseInt(s.toString()) > obj.getSupplierAcceptQty()) {
                        obj.setQtyrecived1(0);
                        viewHolder.mBinding.etQty.setText("");
                        Utils.setToast(context, "Received quantity cannot be greater then required quantity");
                    } else {
                        if (Integer.parseInt(s.toString()) < obj.getSupplierAcceptQty()) {
                            viewHolder.mBinding.etComment.setVisibility(View.VISIBLE);
                        }
                        obj.setQtyrecived1(Integer.parseInt(s.toString()));
                    }
                    interfacee.syncValues(assignmentList);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface AssignmentListInterface {
        void syncValues(List<AssignmentDetailsModel.PurdetailsEntity> list);
    }

    @Override
    public int getItemCount() {
        return assignmentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        GrnItemsListBinding mBinding;

        public ViewHolder(GrnItemsListBinding mBinding) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
        }
    }
}