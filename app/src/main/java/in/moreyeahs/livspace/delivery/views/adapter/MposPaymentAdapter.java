package in.moreyeahs.livspace.delivery.views.adapter;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import in.moreyeahs.livspace.delivery.R;
import in.moreyeahs.livspace.delivery.databinding.MposAdapterBinding;
import in.moreyeahs.livspace.delivery.listener.OnlinePaymentListener;
import in.moreyeahs.livspace.delivery.model.PostOnlineCollectionModel;

import java.util.ArrayList;

public class MposPaymentAdapter extends RecyclerView.Adapter<MposPaymentAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater layoutInflater;
    ArrayList<PostOnlineCollectionModel> mposList;
    ArrayList<String> datelist;
    OnlinePaymentListener onlinePaymentListener;
    boolean save = true;


    public MposPaymentAdapter(Context context, ArrayList<PostOnlineCollectionModel> mposList) {
        this.context = context;
        this.mposList = mposList;

    }


    @NonNull
    @Override
    public MposPaymentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(viewGroup.getContext());
        }

        MposAdapterBinding mBinding = DataBindingUtil.inflate(layoutInflater, R.layout.mpos_adapter, viewGroup, false);
        return new MposPaymentAdapter.ViewHolder(mBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MposPaymentAdapter.ViewHolder vh, int i) {
        PostOnlineCollectionModel model = mposList.get(i);
        vh.orderId.setText("OrderID: " + model.getOrderid());
        vh.refNo.setText(model.getMposReferenceNo());
        vh.paymentName.setText(model.getPaymentFrom());

        vh.paidAmount.setText(String.format("₹ %s", String.valueOf(model.getMposAmt())));

        vh.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                vh.edit.setVisibility(View.GONE);
                vh.refNo.setVisibility(View.GONE);
                vh.etLayout.setVisibility(View.VISIBLE);
                vh.save.setVisibility(View.VISIBLE);
                vh.etrefNO.setText(model.getMposReferenceNo());
            }
        });

        vh.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vh.mBinding.etRefNo.getText().toString().equalsIgnoreCase(""))
                {
                    Toast.makeText(context, R.string.enter_reference_id, Toast.LENGTH_SHORT).show();

                }
                else if (vh.mBinding.etRefNo.getText().length()<8)
                {
                    Toast.makeText(context, R.string.mpos_id_8, Toast.LENGTH_SHORT).show();

                }
                else
                    {
                    vh.edit.setVisibility(View.VISIBLE);
                    vh.refNo.setVisibility(View.VISIBLE);
                    vh.etLayout.setVisibility(View.GONE);
                    vh.save.setVisibility(View.GONE);
                    vh.refNo.setText(vh.etrefNO.getText().toString());
                    mposList.get(vh.getAdapterPosition()).setMposReferenceNo(vh.etrefNO.getText().toString());
                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return mposList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        MposAdapterBinding mBinding;
        TextView orderId, paidAmount, deliveryDate, refNo,paymentName;
        Button edit, save;
        LinearLayout etLayout;
        EditText etrefNO;

        public ViewHolder(MposAdapterBinding mBinding) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
            orderId = mBinding.tvOrderId;
            paidAmount = mBinding.paidAmnt;
            paymentName = mBinding.paymentName;
            refNo = mBinding.tvRefNo;
            edit = mBinding.btnEdit;
            save = mBinding.btnSave;
            etLayout = mBinding.etRefNoLayout;
            etrefNO = mBinding.etRefNo;

        }
    }


    public ArrayList<PostOnlineCollectionModel> getList() {
        return mposList.size() > 0 ? mposList : null;
    }
}

