package com.jeong.mapmo.ui.view

import android.util.Log
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.jeong.mapmo.R
import com.jeong.mapmo.data.common.MemoResult
import com.jeong.mapmo.data.common.toastCommon
import com.jeong.mapmo.databinding.FragmentMemoBinding
import com.jeong.mapmo.ui.adapter.MemoAdapter
import com.jeong.mapmo.ui.adapter.SwipeHelper
import com.jeong.mapmo.ui.viewModel.MemoViewModel
import com.jeong.mapmo.util.BaseFragment
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MemoFragment : BaseFragment<FragmentMemoBinding>(FragmentMemoBinding::inflate) {
    //질문 프레그먼트가 사라져도 어뎁터 객체가 남아있어서 이런식으로 처리하는지
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
                        is MemoResult.NoConstructor -> {}
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

        _memoAdaper = MemoAdapter(
            deleteFromRoom = { title ->
                memoViewModel.deleteMemo(title)
            },
            naviToEdit =  { memo ->
                findNavController().navigate(
                R.id.action_memoFragment_to_memoMapFragment, bundleOf("editData" to memo)
                )
            },
            updateMemo = { checked, title ->
                memoViewModel.updateMemo(checked, title)
            }
        )
        binding.rvMemo.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = memoAdapter
        }

    }

    //질문 체크박스 누를때마다 룸 업데이트?
    override fun onPause() {
        super.onPause()
       // memoViewModel.updateMemo()
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _memoAdaper = null
    }
}
