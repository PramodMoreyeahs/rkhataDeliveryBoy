package in.moreyeahs.livspace.delivery.views.adapter;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import in.moreyeahs.livspace.delivery.R;
import in.moreyeahs.livspace.delivery.databinding.CurrencyAdapterBinding;
import in.moreyeahs.livspace.delivery.databinding.CurrencyHeaderBinding;
import in.moreyeahs.livspace.delivery.listener.CurrencyIncreaseInterface;
import in.moreyeahs.livspace.delivery.model.CurrencyDenomination;
import in.moreyeahs.livspace.delivery.model.CurrencyListModel;
import in.moreyeahs.livspace.delivery.utilities.TextUtils;
import in.moreyeahs.livspace.delivery.views.main.MainActivity;

import java.util.ArrayList;


public class CurrencyAdapter extends RecyclerView.Adapter {

    private Context context;
    MainActivity activity1;
    ArrayList<CurrencyDenomination> currencyListModel;
    final Handler handler = new Handler();
    private LayoutInflater layoutInflater;
    CurrencyIncreaseInterface currencyIncreaseInterface;
    boolean activate;
    boolean add = false;
    TextView totalAmnt, noteQty, coinQty;
    ArrayList<CurrencyListModel> HistoryDataList;

    public CurrencyAdapter(Context context, ArrayList<CurrencyDenomination> currencyListModel, CurrencyIncreaseInterface currencyIncreaseInterface) {
        this.context = context;
        this.currencyListModel = currencyListModel;
        this.currencyIncreaseInterface = currencyIncreaseInterface;
    }

    public void setCurrencyList(ArrayList<CurrencyDenomination> currencyListModel, TextView totalAmnt, TextView noteQty, TextView coinQty) {
        this.currencyListModel = currencyListModel;
        this.totalAmnt = totalAmnt;
        this.noteQty = noteQty;
        this.coinQty = coinQty;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(viewGroup.getContext());
        }
        switch (viewType) {
            case CurrencyDenomination.HEADER:
                return new HeaderViewHolder(DataBindingUtil.inflate(layoutInflater, R.layout.currency_header, viewGroup, false));

            case CurrencyDenomination.VIEW:
                return new ViewHolder(DataBindingUtil.inflate(layoutInflater, R.layout.currency_adapter, viewGroup, false));

            default:
                return new ViewHolder(DataBindingUtil.inflate(layoutInflater, R.layout.currency_adapter, viewGroup, false));
        }

    }

    @Override
    public int getItemViewType(int position) {

        if (currencyListModel.get(position).isSectionHeader()) {
            return CurrencyDenomination.HEADER;

        } else {
/*
            return CurrencyDenomination.VIEW;

*/
            return position;
        }


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        try {
            CurrencyDenomination currencyDenomination = currencyListModel.get(i);
            //  CurrencyModel currencyModel = currencyList.get(i);

            if (viewHolder.getItemViewType() == CurrencyDenomination.HEADER) {
                ((HeaderViewHolder) viewHolder).mBinding.sectionHeader.setText(currencyDenomination.getCurrencyType());

            } else {
                if (!TextUtils.isNullOrEmpty(currencyListModel.get(i).getTitle())) {
                    ((ViewHolder) viewHolder).mBinding.title.setText(currencyListModel.get(viewHolder.getAdapterPosition()).getTitle());
                }
                // viewHolder.rsIcon.setImageResource(currencyListModel.get(i).getCurrencyImage());
                // viewHolder.mBinding.totalCoins.setText("" + 0);
                boolean isItemFound = false;
                ((ViewHolder) viewHolder).mBinding.totalCoins.setText(String.valueOf(currencyDenomination.getCurrencyCountByDBoy()));

                setTotalValue(1, currencyDenomination, ((ViewHolder) viewHolder));
                saveValues((ViewHolder) viewHolder);
                /*((ViewHolder) viewHolder).mBinding.setCurrencyIncreaseListener(new CurrencyIncreaseListener() {
                    @Override
                    public void cPlusBtnClicked() {
                        add = true;
                        setTotalValue(2, currencyDenomination, ((ViewHolder) viewHolder));
                    }

                    @Override
                    public void cMinusBtnClicked() {
                        add = false;
                        setTotalValue(3, currencyDenomination, ((ViewHolder) viewHolder));
                    }
                });*/
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setTotalValue(int wBtnClicked, CurrencyDenomination currencyDenomination, ViewHolder viewHolder) {
        String etQtyValue = viewHolder.mBinding.totalCoins.getText().toString();

        if (etQtyValue != "" && etQtyValue.length() > 0) {
            /***
             * Plus btn clicked
             * ***/
            /*if(wBtnClicked == 1){
                int itemQuantity = Integer.parseInt(etQtyValue);
                viewHolder.mBinding.totalCoins.setText("" + itemQuantity);
            }
           else*/
            /*if (wBtnClicked == 2) {
                int itemQuantity = Integer.parseInt(etQtyValue);
                itemQuantity += 1;
                viewHolder.mBinding.totalCoins.setText("" + itemQuantity);
                *//***
             * minus btn clicked
             * ***//*
            } else if (wBtnClicked == 3) {
                int itemQuantity = Integer.parseInt(etQtyValue);
                if (itemQuantity > 0) {
                    itemQuantity -= 1;
                }
                viewHolder.mBinding.totalCoins.setText("" + itemQuantity);
            }
*/
            viewHolder.mBinding.totalRs.setText(addNumbers(currencyDenomination, viewHolder.mBinding.totalCoins.getText().toString()));

            viewHolder.mBinding.totalCoins.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (viewHolder.mBinding.totalCoins.getText().toString().length() == 1 && viewHolder.mBinding.totalCoins.getText().toString().startsWith("0")) {
                        viewHolder.mBinding.totalCoins.setText("");
                    }
                    InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(viewHolder.mBinding.totalCoins, InputMethodManager.SHOW_IMPLICIT);
                }
            });
            viewHolder.mBinding.totalCoins.addTextChangedListener(new TextWatcher() {

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    // viewHolder.mBinding.totalRs.setText(addNumbers(currencyDenomination.getQty(), viewHolder.mBinding.totalCoins.getText().toString()));

                }

                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    //viewHolder.mBinding.totalRs.setText(addNumbers(currencyDenomination, viewHolder.mBinding.totalCoins.getText().toString()));

                  /*  if (s.length() > 0) {
                        currencyListModel.get(viewHolder.getAdapterPosition()).setCurrencyCountByDBoy(Integer.parseInt(s.toString()));
                        viewHolder.mBinding.totalRs.setText(addNumbers(currencyDenomination, viewHolder.mBinding.totalCoins.getText().toString()));

                    }
                    else{
                        currencyListModel.get(viewHolder.getAdapterPosition()).setCurrencyCountByDBoy(0);
                        viewHolder.mBinding.totalRs.setText(addNumbers(currencyDenomination, viewHolder.mBinding.totalCoins.getText().toString()));

                    }*/
                }

                public void afterTextChanged(Editable s) {

                    if (s.length() > 0) {
                        saveValues(viewHolder);
                        viewHolder.mBinding.totalRs.setText(addNumbers(currencyDenomination, viewHolder.mBinding.totalCoins.getText().toString()));

                    }
                    else{
                        saveValues(viewHolder);
                        viewHolder.mBinding.totalRs.setText(addNumbers(currencyDenomination, viewHolder.mBinding.totalCoins.getText().toString()));

                    }
                }
            });
        } else {
            viewHolder.mBinding.totalCoins.setText("0");
            viewHolder.mBinding.totalRs.setText(addNumbers(currencyDenomination, viewHolder.mBinding.totalCoins.getText().toString()));

            viewHolder.mBinding.totalCoins.addTextChangedListener(new TextWatcher() {

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    // viewHolder.mBinding.totalRs.setText(addNumbers(currencyDenomination.getQty(), viewHolder.mBinding.totalCoins.getText().toString()));

                }

                public void onTextChanged(CharSequence s, int start, int before, int count) {

                   /* if (s.length() > 0) {
                        currencyListModel.get(viewHolder.getAdapterPosition()).setCurrencyCountByDBoy(Integer.parseInt(s.toString()));
                        viewHolder.mBinding.totalRs.setText(addNumbers(currencyDenomination, viewHolder.mBinding.totalCoins.getText().toString()));

                    }*/
                }

                public void afterTextChanged(Editable s) {

                    if (s.length() > 0) {
                        saveValues(viewHolder);
                        viewHolder.mBinding.totalRs.setText(addNumbers(currencyDenomination, viewHolder.mBinding.totalCoins.getText().toString()));

                    }

                }
            });
        }

    }

    private void saveValues(ViewHolder viewHolder) {
        String etQty = viewHolder.mBinding.totalCoins.getText().toString();
        int number2;
        if (etQty != "" && etQty.length() > 0) {
            number2 = Integer.parseInt(etQty);
        } else {
            number2 = 0;
        }
        currencyListModel.get(viewHolder.getAdapterPosition()).setCurrencyCountByDBoy(number2);
        currencyListModel.get(viewHolder.getAdapterPosition()).setTotalAmt(Integer.parseInt(currencyListModel.get(viewHolder.getAdapterPosition()).getValue()) * number2);
    }

    private String addNumbers(CurrencyDenomination currencyDenomination, String etQty) {
        int number2;
        if (etQty != "" && etQty.length() > 0) {
            number2 = Integer.parseInt(etQty);
        } else {
            number2 = 0;
        }
        /*currencyDenomination.setTotalAmt(Integer.parseInt(currencyDenomination.getValue()) * number2);
        currencyDenomination.setCurrencyCountByDBoy(number2);
        currencyDenomination.setValue(currencyDenomination.getValue());
        currencyDenomination.setCurrencyType(currencyDenomination.getCurrencyType());
        currencyDenomination.setTitle(currencyDenomination.getTitle());*/
        //currencyIncreaseInterface.totalCurrencyAmt();
        try {
            int totalAmt = 0;
            int totalAmt1 = 0;
            int totalQty = 0;
            int totalNoteAmnt = 0;
            int totalCoinAmnt = 0;
            int totalQty1 = 0;
            for (int i = 0; i < currencyListModel.size(); i++)
            {
                totalAmt = currencyListModel.get(i).getTotalAmt();
                totalQty = currencyListModel.get(i).getCurrencyCountByDBoy();
                totalAmt1 = totalAmt1 + totalAmt;
                totalQty1 = totalQty1 + totalQty;
                if (currencyListModel.get(i).getCurrencyType().equalsIgnoreCase("Notes"))
                {
                    totalNoteAmnt = totalNoteAmnt + currencyListModel.get(i).getTotalAmt();
                } else {
                    totalCoinAmnt = totalCoinAmnt + currencyListModel.get(i).getTotalAmt();
                }
            }
            totalAmnt.setText("" + totalAmt1);
            noteQty.setText("" + totalNoteAmnt);
            coinQty.setText("" + totalCoinAmnt);


        } catch (Exception e) {
            e.printStackTrace();
        }

        return Integer.toString(Integer.parseInt(currencyDenomination.getValue()) * number2);
    }

    public void activateButtons(boolean activate) {
        this.activate = activate;
        notifyDataSetChanged();
        //need to call it for the child views to be re-created with buttons.
    }

    @Override
    public int getItemCount()
    {
        return currencyListModel == null ? 0 : currencyListModel.size();
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder
    {
        CurrencyHeaderBinding mBinding;
        HeaderViewHolder(CurrencyHeaderBinding mBinding)
        {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CurrencyAdapterBinding mBinding;
        EditText totalCoins;
        TextView totalRs, title;

        public ViewHolder(CurrencyAdapterBinding mBinding) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
            totalCoins = mBinding.totalCoins;
            totalRs = mBinding.totalRs;
            title = mBinding.title;
        }

        /*public void bind(MyTaskInfo item) {
            mBinding.setMyTaskInfo(item);
            mBinding.executePendingBindings();
        }*/
    }


    /*@Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        context.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }*/


}