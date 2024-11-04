package com.jeong.mapmo.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.jeong.mapmo.data.dto.Memo

class MemoDiffUtilInfo: DiffUtil.ItemCallback<Memo>() {
    override fun areItemsTheSame(
        oldItem: Memo,
        newItem: Memo
    ): Boolean {
        return (oldItem == newItem)
    }

    override fun areContentsTheSame(
        oldItem: Memo,
        newItem: Memo
    ): Boolean {
        return (oldItem == newItem)
    }
}