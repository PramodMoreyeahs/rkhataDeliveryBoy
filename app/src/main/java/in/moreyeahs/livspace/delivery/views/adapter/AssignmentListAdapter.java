package in.moreyeahs.livspace.delivery.views.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import in.moreyeahs.livspace.delivery.R;
import in.moreyeahs.livspace.delivery.databinding.AssignmentListAdapterBinding;
import in.moreyeahs.livspace.delivery.model.AssignmentListResponse;
import in.moreyeahs.livspace.delivery.utilities.Utils;
import in.moreyeahs.livspace.delivery.views.main.PoListByAssignmentActivity;


public class AssignmentListAdapter extends RecyclerView.Adapter<AssignmentListAdapter.ViewHolder> {
    private LayoutInflater layoutInflater;
    Context context;
    private List<AssignmentListResponse.AssignmentList> assignmentList = new ArrayList<>();
    AssignmentListInterface interfacee;


    public AssignmentListAdapter(Context context, AssignmentListInterface interfacee) {
        this.context = context;
        this.interfacee = interfacee;
    }

    public void setAssignmentList(List<AssignmentListResponse.AssignmentList> assignmentList) {
        this.assignmentList = assignmentList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AssignmentListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(viewGroup.getContext());
        }
        AssignmentListAdapterBinding mBinding = DataBindingUtil.inflate(layoutInflater, R.layout.assignment_list_adapter, viewGroup, false);
        return new ViewHolder(mBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull AssignmentListAdapter.ViewHolder viewHolder, int i) {
        try {
            AssignmentListResponse.AssignmentList obj = assignmentList.get(i);
            viewHolder.mBinding.tvAssignmentId.setText("Assignment Id: " + obj.getPoassignmentid());
            viewHolder.mBinding.createdDate.setText(Utils.getDateFormat(obj.getCreateddate()));
            viewHolder.mBinding.tvPrice.setText("₹" + new DecimalFormat("#.00").format(obj.getTotalassignmentamount()));
            viewHolder.mBinding.noOfItems.setText("Total items: " + obj.getDetails().size());
            viewHolder.mBinding.tvAssignmentStatus.setText(obj.getStatus());

            if (obj.getSubmitted()) {
                viewHolder.mBinding.accRejHolder.setVisibility(View.GONE);
                viewHolder.mBinding.viewDetailHolder.setVisibility(View.VISIBLE);
                viewHolder.mBinding.btnView.setText("Submit Assignment");
                viewHolder.mBinding.btnView.setOnClickListener(V -> {
                    interfacee.submitAssignment(obj.getPoassignmentid());
                });
            } else {
                viewHolder.mBinding.btnView.setText("Details");
                if (obj.getStatus().equalsIgnoreCase("Assigned")) {
                    viewHolder.mBinding.accRejHolder.setVisibility(View.VISIBLE);
                    viewHolder.mBinding.viewDetailHolder.setVisibility(View.GONE);

                    viewHolder.mBinding.accept.setOnClickListener(V -> {
                        interfacee.onButtonClick(obj.getPoassignmentid(), true);
                    });
                    viewHolder.mBinding.cancel.setOnClickListener(V -> {
                        interfacee.onButtonClick(obj.getPoassignmentid(), false);
                    });
                } else if (obj.getStatus().equalsIgnoreCase("Accepted")) {
                    viewHolder.mBinding.accRejHolder.setVisibility(View.GONE);
                    viewHolder.mBinding.viewDetailHolder.setVisibility(View.VISIBLE);

                    viewHolder.mBinding.btnView.setOnClickListener(V -> {
                        context.startActivity(new Intent(context, PoListByAssignmentActivity.class).putExtra("ASSIGNMENT_ID", obj.getPoassignmentid()));
                    });
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface AssignmentListInterface {
        void onButtonClick(int poId, Boolean isAccepted);
        void submitAssignment(int poId);
    }

    @Override
    public int getItemCount() {
        return assignmentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        AssignmentListAdapterBinding mBinding;

        public ViewHolder(AssignmentListAdapterBinding mBinding) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
        }
    }
}