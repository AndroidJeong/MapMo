package com.jeong.mapmo.ui.view

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.jeong.mapmo.R
import com.jeong.mapmo.data.common.PriorityColor
import com.jeong.mapmo.data.dto.Memo
import com.jeong.mapmo.databinding.FragmentMemoBinding
import com.jeong.mapmo.ui.adapter.MemoAdapter
import com.jeong.mapmo.ui.adapter.SwipeHelper
import com.jeong.mapmo.ui.viewModel.MemoViewModel
import com.jeong.mapmo.util.BaseFragment

class MemoFragment : BaseFragment<FragmentMemoBinding>(FragmentMemoBinding::inflate) {
    private var _memoAdaper: MemoAdapter? = null
    val memoAdapter get() = requireNotNull(_memoAdaper)
    private val memoViewModel by viewModels<MemoViewModel>()
    var eraseLater = 0
    val list = mutableListOf<Memo>()

    override fun initView() {

        val swipeHelper = SwipeHelper()
        val itemTouchHelper = ItemTouchHelper(swipeHelper)
        itemTouchHelper.attachToRecyclerView(binding.rvMemo)

        _memoAdaper = MemoAdapter()
        binding.rvMemo.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = memoAdapter
        }

        binding.ivMemoToolplus.setOnClickListener {
            findNavController().navigate(R.id.action_memoFragment_to_memoMapFragment)
        }

        val a = listOf<Memo>(
            Memo(
                title = "제목11111111111111111111111111111111111111111111111111111111111111111111",
                detail = "디테일1",
                priority = PriorityColor.RED
            ),
            Memo(title = "제목22222222222", detail = "디테일2", priority = PriorityColor.YELLOW),
            Memo(title = "제목333333333333333", detail = "디테일3", priority = PriorityColor.Green),

            )

        memoAdapter.submitList(a)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _memoAdaper = null
    }
}
