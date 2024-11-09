package com.jeong.mapmo.ui.view

import android.content.res.ColorStateList
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.jeong.mapmo.R
import com.jeong.mapmo.data.common.PriorityColor
import com.jeong.mapmo.data.dto.Memo
import com.jeong.mapmo.databinding.FragmentMemoAddBinding
import com.jeong.mapmo.ui.viewModel.MemoAddViewModel
import com.jeong.mapmo.util.BaseFragment

class MemoAddFragment : BaseFragment<FragmentMemoAddBinding>(FragmentMemoAddBinding::inflate) {

    private val memoAddviewModel by viewModels<MemoAddViewModel>()
    private val args: MemoAddFragmentArgs by navArgs()

    override fun initView() {
        setSpinner()
        setAddButton()
    }

    fun setAddButton() {
        binding.btAddSave.setOnClickListener {
            with(binding) {
                val memo = Memo(
                    title = etAddTitle.text.toString(),
                    longitude = args.location.longitude,
                    latitude = args.location.latitude,
                    detail = etAddDetail.text.toString(),
                    priority =
                    when (spinnerAdd.selectedItemPosition) {
                        0 -> PriorityColor.RED
                        1 -> PriorityColor.YELLOW
                        2 -> PriorityColor.Green
                        else -> throw IllegalArgumentException("스피너 색상 지정에 문제발생")
                    }
                )

                memoAddviewModel.saveMemo(memo)
            }

        }
    }

    private fun setSpinner() {
        val datas = resources.getStringArray(R.array.spinner_list)
        val spinner = binding.spinnerAdd
        val spinnerAdapter =
            ArrayAdapter<String>(requireActivity(), android.R.layout.simple_dropdown_item_1line, datas)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = spinnerAdapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                when (position) {
                    0 -> binding.ivAddSpinnercolor.backgroundTintList =
                        ColorStateList.valueOf(
                            ContextCompat.getColor(
                                requireActivity(),
                                PriorityColor.RED.color
                            )
                        )

                    1 -> binding.ivAddSpinnercolor.backgroundTintList =
                        ColorStateList.valueOf(
                            ContextCompat.getColor(
                                requireActivity(),
                                PriorityColor.YELLOW.color
                            )
                        )

                    2 -> binding.ivAddSpinnercolor.backgroundTintList =
                        ColorStateList.valueOf(
                            ContextCompat.getColor(
                                requireActivity(),
                                PriorityColor.Green.color
                            )
                        )
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

        }

    }

}
