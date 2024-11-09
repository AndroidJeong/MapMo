package com.jeong.mapmo.ui.view

import android.util.Log
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.jeong.mapmo.R
import com.jeong.mapmo.data.common.MemoResult
import com.jeong.mapmo.databinding.FragmentMemoBinding
import com.jeong.mapmo.ui.adapter.MemoAdapter
import com.jeong.mapmo.ui.adapter.SwipeHelper
import com.jeong.mapmo.ui.viewModel.MemoViewModel
import com.jeong.mapmo.util.BaseFragment
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MemoFragment : BaseFragment<FragmentMemoBinding>(FragmentMemoBinding::inflate) {
    private var _memoAdaper: MemoAdapter? = null
    val memoAdapter get() = requireNotNull(_memoAdaper)
    private val memoViewModel by viewModels<MemoViewModel>()

    override fun initView() {
        initRecyclerView()
        initMemo()
        binding.ivMemoToolplus.setOnClickListener {
            findNavController().navigate(R.id.action_memoFragment_to_memoMapFragment)
        }
    }

    private fun initMemo() {
        memoViewModel.getMemo()
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                memoViewModel.memoList.collectLatest {
                    when(it){
                        is MemoResult.Loading -> {}
                        is MemoResult.NoConstructor -> Unit
                        is MemoResult.RoomDBError -> Log.d("room error", it.toString())
                        is MemoResult.Success -> memoAdapter.submitList(it.resultData)
                    }
                }
            }
        }
    }

    private fun initRecyclerView() {
        val swipeHelper = SwipeHelper()
        val itemTouchHelper = ItemTouchHelper(swipeHelper)
        itemTouchHelper.attachToRecyclerView(binding.rvMemo)

        _memoAdaper = MemoAdapter()
        binding.rvMemo.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = memoAdapter
        }

    }



    override fun onDestroyView() {
        super.onDestroyView()
        _memoAdaper = null
    }
}
