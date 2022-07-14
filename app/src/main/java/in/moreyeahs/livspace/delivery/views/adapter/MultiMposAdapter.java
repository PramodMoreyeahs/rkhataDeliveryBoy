package in.moreyeahs.livspace.delivery.views.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import in.moreyeahs.livspace.delivery.R;
import in.moreyeahs.livspace.delivery.databinding.MultiMposadapterBinding;
import in.moreyeahs.livspace.delivery.listener.MultiMposListener;
import in.moreyeahs.livspace.delivery.model.DeliveryPayments;

import java.util.ArrayList;

public class MultiMposAdapter extends RecyclerView.Adapter<MultiMposAdapter.ViewHolder> {
    private Context context;
    private ArrayList<DeliveryPayments> payment;
    private MultiMposListener listener;
    private int orderId;

    public MultiMposAdapter(Context context, ArrayList<DeliveryPayments> mpospayment, int orderId, MultiMposListener listener) {
        this.context = context;
        this.payment = mpospayment;
        this.listener = listener;
        this.orderId = orderId;
    }

    @NonNull
    @Override
    public MultiMposAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MultiMposAdapter.ViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.multi_mposadapter, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MultiMposAdapter.ViewHolder holder, int position) {
        holder.UPIReference.setText(payment.get(position).getTransId());
        holder.UPIAmount.setText("" + payment.get(position).getAmount());
        payment.get(position).setPaymentFrom("mPos");
        payment.get(position).setOrderId(orderId);
    }

    @Override
    public int getItemCount() {
        return payment.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        MultiMposadapterBinding mBinding;
        EditText UPIAmount, UPIReference;

        public ViewHolder(@NonNull MultiMposadapterBinding mBinding) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
            UPIAmount = mBinding.UPIAmount;
            UPIReference = mBinding.UPIReference;
            mBinding.ivCross.setOnClickListener(v -> {
                payment.get(getAdapterPosition()).setAmount(0);
                listener.onMposAmountChange(payment);
                payment.remove(getAdapterPosition());
                notifyItemRemoved(getAdapterPosition());
            });
            UPIReference.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() > 0) {
                        payment.get(getAdapterPosition()).setTransId(s.toString());
                    } else {
                        payment.get(getAdapterPosition()).setTransId("");
                    }

                }
            });
            UPIAmount.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() > 0) {
                        payment.get(getAdapterPosition()).setAmount(Integer.valueOf(s.toString()));
                    } else {
                        payment.get(getAdapterPosition()).setAmount(0);
                    }
                    listener.onMposAmountChange(payment);
                }
            });
        }
    }

    public ArrayList<DeliveryPayments> getListFromAdapter() {
        return payment;
    }
}
