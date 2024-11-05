package com.jeong.mapmo.ui.view

import android.content.Context
import android.content.res.ColorStateList
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import com.jeong.mapmo.R
import com.jeong.mapmo.databinding.FragmentMemoAddBinding
import com.jeong.mapmo.util.BaseFragment

class MemoAddFragment : BaseFragment<FragmentMemoAddBinding>(FragmentMemoAddBinding::inflate) {
    private lateinit var mainContext: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainContext = context
    }


    override fun initView() {
       setSpinner()



    }


    private fun setSpinner() {
        val datas = resources.getStringArray(R.array.spinner_list)
        val spinner = binding.spinnerAdd
        val spinnerAdapter = ArrayAdapter<String>(mainContext, android.R.layout.simple_dropdown_item_1line, datas)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = spinnerAdapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when(position){
                    0 -> binding.ivAddSpinnercolor.backgroundTintList =
                        ColorStateList.valueOf(ContextCompat.getColor(mainContext, R.color.red))
                    1 -> binding.ivAddSpinnercolor.backgroundTintList =
                        ColorStateList.valueOf(ContextCompat.getColor(mainContext, R.color.yellow))
                    2 ->  binding.ivAddSpinnercolor.backgroundTintList =
                        ColorStateList.valueOf(ContextCompat.getColor(mainContext, R.color.green))
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

        }

    }

}
