package com.liike.liikegomi.my_purchases.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.liike.liikegomi.base.ui.BaseActivity
import com.liike.liikegomi.databinding.ActivityMyPurchasesBinding
import com.liike.liikegomi.my_purchases.ItemPurchaseAdapter
import com.liike.liikegomi.my_purchases.view_model.MyPurchasesViewModel
import com.liike.liikegomi.my_purchases.view_model.MyPurchasesViewModelFactory

class MyPurchasesActivity: BaseActivity<ActivityMyPurchasesBinding, MyPurchasesViewModel>() {

    private lateinit var mPurchasesAdapter: ItemPurchaseAdapter

    companion object {
        fun launch(appCompatActivity: AppCompatActivity) {
            val intent = Intent(appCompatActivity, MyPurchasesActivity::class.java)
            appCompatActivity.startActivity(intent)
        }
    }

    override val mViewModel: MyPurchasesViewModel
        get() = getViewModelFactory(this, MyPurchasesViewModelFactory())

    override fun inflate(): ActivityMyPurchasesBinding {
        return ActivityMyPurchasesBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mPurchasesAdapter = ItemPurchaseAdapter()
        mBinding.recyclerPurchases.adapter = mPurchasesAdapter

        mViewModel.mPurchasesGrouped.observe(this) {
//            Log.d("count", it.eachCount().toString())
//            val map = it.fold(0) { accumulator, element ->
//                Log.d("fold", "($accumulator - ${element.fechaCompra.toDate()})")
//                accumulator
//            }
//            println(map)
//            it.reduce { key, a, element ->
//                Log.d("reduce", "$key ($a - $element)")
////                mList.add(key to element)
//                Ventas()
//            }
            mPurchasesAdapter.setData(it)
        }

        mViewModel.getMyPurchases()
    }

}