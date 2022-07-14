package `in`.moreyeahs.livspace.delivery.views.returnorder

import `in`.moreyeahs.livspace.delivery.R
import `in`.moreyeahs.livspace.delivery.databinding.ActivityReturnOrderListBinding
import `in`.moreyeahs.livspace.delivery.views.fragment.PickFromCustomerFragment
import `in`.moreyeahs.livspace.delivery.views.fragment.PickFromWarehouseFragment
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.livspace.trade.adapter.ViewPagerAdapter
import kotlinx.android.synthetic.main.activity_return_order_list.*

class ReturnOrderListActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityReturnOrderListBinding
    private lateinit var viewModel: `in`.moreyeahs.livspace.delivery.viewmodels.ReturnOrderListViewModel
//    private var list: ArrayList<ReturnOrderListModel>? = null
//    private var adapter: ReturnOrderListAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_return_order_list)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

//        viewModel = ViewModelProviders.of(this).get(ReturnOrderListViewModel::class.java)
//        mBinding.returnOrderViewModel = viewModel
//        mBinding.lifecycleOwner = this

//        mBinding.recyclerReturnOrder.layoutManager = LinearLayoutManager(applicationContext)
//        viewModel.returnOrderListResponse().observe(this, Observer { apiResponse: ApiResponse -> consumeReturnOrderResponse(apiResponse) })
//        viewModel.hitReturnOrderListAPI(SharePrefs.getInstance(applicationContext).getInt(SharePrefs.PEOPLE_ID))

        setupViewPager()
        tabs.setupWithViewPager(viewPagerReturn)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        `in`.moreyeahs.livspace.delivery.utilities.Utils.rightTransaction(this)
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == 9 && resultCode == Activity.RESULT_OK) {
//            list!!.clear()
//            adapter!!.notifyDataSetChanged()
//            viewModel.hitReturnOrderListAPI(SharePrefs.getInstance(applicationContext).getInt(SharePrefs.PEOPLE_ID))
//        }
//    }

    private fun setupViewPager() {
        val list = ArrayList<Fragment>()
        list.add(PickFromCustomerFragment())
        list.add(PickFromWarehouseFragment())

        val titleList = ArrayList<String>()
        titleList.add("Pick From Customer")
        titleList.add("Pick From Warehouse")
        val adapter = ViewPagerAdapter(supportFragmentManager, list, titleList = titleList)
        viewPagerReturn.adapter = adapter
    }

//    private fun consumeReturnOrderResponse(apiResponse: ApiResponse) {
//        when (apiResponse.status) {
//            Status.LOADING -> Utils.showProgressDialog(this)
//            Status.SUCCESS -> {
//                Utils.hideProgressDialog(this)
//                renderSuccessResponse(apiResponse.data)
//            }
//            Status.ERROR -> {
//                Utils.hideProgressDialog(this)
//                mBinding.tvEmpty.visibility = View.VISIBLE
//                Utils.setToast(this, resources.getString(R.string.errorString))
//            }
//            else -> {
//            }
//        }
//    }
//
//    private fun renderSuccessResponse(element: JsonElement?) {
//        mBinding.tvEmpty.visibility = View.INVISIBLE
//        try {
//            if (element != null) {
//                val array = JSONArray(element.toString())
//                if (array.length() > 0) {
//                    list = Gson().fromJson(array.toString(), object : TypeToken<ArrayList<ReturnOrderListModel?>?>() {}.type)
//                    adapter = ReturnOrderListAdapter(this, list)
//                    mBinding.recyclerReturnOrder.adapter = adapter
//                } else {
//                    mBinding.tvEmpty.visibility = View.VISIBLE
//                }
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//            mBinding.tvEmpty.visibility = View.VISIBLE
//        }
//    }
}