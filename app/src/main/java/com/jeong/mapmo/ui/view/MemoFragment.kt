package com.jeong.mapmo.ui.view

import android.content.Intent
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
import com.jeong.mapmo.data.common.LocationService
import com.jeong.mapmo.data.common.MemoResult
import com.jeong.mapmo.databinding.FragmentMemoBinding
import com.jeong.mapmo.ui.adapter.MemoAdapter
import com.jeong.mapmo.ui.adapter.SwipeHelper
import com.jeong.mapmo.ui.viewModel.MemoViewModel
import com.jeong.mapmo.util.BaseFragment
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MemoFragment : BaseFragment<FragmentMemoBinding>(FragmentMemoBinding::inflate) {
    //프레그먼트가 사라져도 어뎁터 객체가 남아있을 수 있음 아래 링크 참고
    private var _memoAdaper: MemoAdapter? = null
    val memoAdapter get() = requireNotNull(_memoAdaper)
    private val memoViewModel by viewModels<MemoViewModel>()

    override fun initView() {
        initRecyclerView()
        initMemo()
        binding.ivMemoToolplus.setOnClickListener {
            findNavController().navigate(R.id.action_memoFragment_to_mapFragment)
        }

        binding.ivMemoSetting.setOnClickListener {
            val serviceIntent = Intent(requireActivity(), LocationService::class.java)
            requireActivity().stopService(serviceIntent)
            toastCommon("알림을 종료합니다. 알림을 다시 시작하려면 앱을 다시 실행해주세요")
            Log.d("location2", "서비스 종료")
        }

    }

    private fun initMemo() {
        memoViewModel.getMemo()
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                memoViewModel.memoList.collectLatest {
                    when (it) {
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
            naviToEdit = { memo ->
                findNavController().navigate(
                    R.id.action_memoFragment_to_mapFragment, bundleOf("editData" to memo)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _memoAdaper = null
    }
}
/*
https://velog.io/@lijunhyeong/%EC%95%88%EB%93%9C%EB%A1%9C%EC%9D%B4%EB%93%9C-%EB%A9%94%EB%AA%A8%EB%A6%AC-%EB%A6%ADmemory-leak
https://stackoverflow.com/questions/60969041/memory-leaks-in-recyclerview-android

 */