package com.jeong.mapmo.ui.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jeong.mapmo.R
import com.jeong.mapmo.data.common.PriorityColor
import com.jeong.mapmo.data.dto.Memo
import com.jeong.mapmo.databinding.ItemMemoBinding

class MemoAdapter : ListAdapter<Memo, MemoAdapter.ViewHolder>(MemoDiffUtilInfo()) {

    inner class ViewHolder(val binding: ItemMemoBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            with(binding) {
                root.setOnClickListener {
                    currentList[adapterPosition].expand = !currentList[adapterPosition].expand

                    tvMemoitemDetail.visibility =
                        if (currentList[adapterPosition].expand) View.VISIBLE else View.GONE

                    tvMemoitemTitle.maxLines =
                        if (currentList[adapterPosition].expand) Int.MAX_VALUE else 1

                    tvMemoitemLocationtext.maxLines =
                        if (currentList[adapterPosition].expand) Int.MAX_VALUE else 1

                }

                cbMemoitemCheckbox.setOnClickListener {
                    tvMemoitemTitle.paintFlags =
                        if(cbMemoitemCheckbox.isChecked) Paint.STRIKE_THRU_TEXT_FLAG else 0

                    tvMemoitemLocationtext.paintFlags =
                        if(cbMemoitemCheckbox.isChecked) Paint.STRIKE_THRU_TEXT_FLAG else 0
                }

            }
        }

        fun bind(item: Memo) {

            // 뷰홀더 재사용 과정에서 isClamped 값에 맞지 않는 스와이프 상태가 보일 수 있으므로 아래와 같이 명시적으로 isClamped 값에 따라 스와이프 상태 지정
            if(item.isClamped) binding.clMemoitemSwipearea.translationX = binding.root.width * -1f / 10 * 3
            else binding.clMemoitemSwipearea.translationX = 0f

            with(binding) {

                ivMemoitemErase.setOnClickListener {
                    if(item.isClamped) removeItem(adapterPosition)
                }

                ivMemoitemBackground.apply {
                    when (item.priority) {
                        PriorityColor.RED -> setBackgroundResource(R.color.red)
                        PriorityColor.YELLOW -> setImageResource(R.color.yellow)
                        PriorityColor.Green -> setImageResource(R.color.green)
                    }
                }
                tvMemoitemTitle.text = item.title
                tvMemoitemLocationtext.text = "장소가 들어갈 자리"
                tvMemoitemDetail.text = item.detail

            }
        }

        fun setClamped(isClamped: Boolean){
            getItem(adapterPosition).isClamped = isClamped
        }

        fun getClamped(): Boolean{
            return getItem(adapterPosition).isClamped
        }


    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoAdapter.ViewHolder {
        val binding: ItemMemoBinding =
            ItemMemoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MemoAdapter.ViewHolder, position: Int) {
        with(holder) {
            bind(currentList[position])
        }
    }


    fun removeItem(position: Int){  // currentList에서 바로 아이템 지우면 에러 발생
        val newList = currentList.toMutableList()
        newList.removeAt(position)

        newList.forEach {
            it.isClamped = false
        } // 한 아이템 삭제 시 다른 아이템들 모두 스와이프x 상태 처리하기 위함

        submitList(newList.toList())
    }





}
