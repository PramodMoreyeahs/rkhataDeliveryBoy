package `in`.moreyeahs.livspace.delivery.views.fragment

import `in`.moreyeahs.livspace.delivery.R
import `in`.moreyeahs.livspace.delivery.views.adapter.ReturnOrderListAdapter
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_pick_from_customer.*
import org.json.JSONArray
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class PickFromCustomerFragment : Fragment(), `in`.moreyeahs.livspace.delivery.listener.ButtonClick {
    private lateinit var activity: Activity
    private lateinit var viewModel: `in`.moreyeahs.livspace.delivery.viewmodels.ReturnOrderListViewModel
    private var list: ArrayList<`in`.moreyeahs.livspace.delivery.model.ReturnOrderListModel>? = null
    private var adapter: ReturnOrderListAdapter? = null


    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as Activity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pick_from_customer, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(`in`.moreyeahs.livspace.delivery.viewmodels.ReturnOrderListViewModel::class.java)

        recyclerPickFromCustomer.layoutManager = LinearLayoutManager(activity)
//        recyclerPickFromCustomer.addOnItemTouchListener(RecyclerItemClickListener(activity, object : RecyclerItemClickListener.OnItemClickListener {
//            override fun onItemClick(view: View, position: Int) {}
//        }))

        viewModel.returnOrderListResponse().observe(this, Observer { apiResponse: `in`.moreyeahs.livspace.delivery.utilities.ApiResponse -> consumeReturnOrderResponse(apiResponse) })
        viewModel.hitReturnOrderListAPI(`in`.moreyeahs.livspace.delivery.utilities.SharePrefs.getInstance(activity).getInt(`in`.moreyeahs.livspace.delivery.utilities.SharePrefs.PEOPLE_ID))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 9 && resultCode == Activity.RESULT_OK) {
            list!!.clear()
            adapter!!.notifyDataSetChanged()
            viewModel.hitReturnOrderListAPI(`in`.moreyeahs.livspace.delivery.utilities.SharePrefs.getInstance(activity).getInt(`in`.moreyeahs.livspace.delivery.utilities.SharePrefs.PEOPLE_ID))
        }
    }

    override fun onButtonClick(pos: Int) {
        startActivityForResult(Intent(activity, `in`.moreyeahs.livspace.delivery.views.returnorder.ReturnOrderDetailActivity::class.java).putExtra("list", list!![pos]), 9)
        `in`.moreyeahs.livspace.delivery.utilities.Utils.leftTransaction(activity)
    }

    private fun consumeReturnOrderResponse(apiResponse: `in`.moreyeahs.livspace.delivery.utilities.ApiResponse) {
        when (apiResponse.status) {
            `in`.moreyeahs.livspace.delivery.utilities.Status.LOADING -> `in`.moreyeahs.livspace.delivery.utilities.Utils.showProgressDialog(activity)
            `in`.moreyeahs.livspace.delivery.utilities.Status.SUCCESS -> {
                `in`.moreyeahs.livspace.delivery.utilities.Utils.hideProgressDialog(activity)
                renderSuccessResponse(apiResponse.data)
            }
            `in`.moreyeahs.livspace.delivery.utilities.Status.ERROR -> {
                `in`.moreyeahs.livspace.delivery.utilities.Utils.hideProgressDialog(activity)
                tvEmpty.visibility = View.VISIBLE
                `in`.moreyeahs.livspace.delivery.utilities.Utils.setToast(activity, resources.getString(R.string.errorString))
            }
            else -> {
            }
        }
    }

    private fun renderSuccessResponse(element: JsonElement?) {
        tvEmpty.visibility = View.INVISIBLE
        try {
            if (element != null) {
                val array = JSONArray(element.toString())
                if (array.length() > 0) {
                    list = Gson().fromJson(array.toString(), object : TypeToken<ArrayList<`in`.moreyeahs.livspace.delivery.model.ReturnOrderListModel?>?>() {}.type)
                    adapter = ReturnOrderListAdapter(activity, list, this@PickFromCustomerFragment)
                    recyclerPickFromCustomer.adapter = adapter
                } else {
                    tvEmpty.visibility = View.VISIBLE
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            tvEmpty.visibility = View.VISIBLE
        }
    }
}