package `in`.moreyeahs.livspace.delivery.views.adapter

import `in`.moreyeahs.livspace.delivery.R
import `in`.moreyeahs.livspace.delivery.databinding.ItemReturnOrderDetailBinding
import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class ReturnOrderDetailAdapter : RecyclerView.Adapter<`in`.moreyeahs.livspace.delivery.views.adapter.ReturnOrderDetailAdapter.ViewHolder> {
    private var activity: Activity? = null
    private var list: ArrayList<`in`.moreyeahs.livspace.delivery.model.ReturnOrderItemModel>? = null


    constructor(activity: Activity?, list: ArrayList<`in`.moreyeahs.livspace.delivery.model.ReturnOrderItemModel>?) {
        this.activity = activity
        this.list = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): `in`.moreyeahs.livspace.delivery.views.adapter.ReturnOrderDetailAdapter.ViewHolder {
        return `in`.moreyeahs.livspace.delivery.views.adapter.ReturnOrderDetailAdapter.ViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context),
                R.layout.item_return_order_detail, parent, false))
    }

    override fun onBindViewHolder(holder: `in`.moreyeahs.livspace.delivery.views.adapter.ReturnOrderDetailAdapter.ViewHolder, position: Int) {
        val model: `in`.moreyeahs.livspace.delivery.model.ReturnOrderItemModel = list!![position]
        holder.mBinding.tvSerialNo.text = "" + position + 1
        holder.mBinding.tvName.text = model.itemName
        holder.mBinding.tvPrice.text = "" + model.price
        if (model.requestType == 0) {
            holder.mBinding.tvQty.text = "" + model.qty
        } else {
            holder.mBinding.tvQty.text = "" + model.qty
        }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return list!!.size
    }

    class ViewHolder(var mBinding: ItemReturnOrderDetailBinding) : RecyclerView.ViewHolder(mBinding.root)
}