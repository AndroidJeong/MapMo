package com.jeong.mapmo.ui.view

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.jeong.mapmo.data.PriorityColor
import com.jeong.mapmo.data.dto.Memo
import com.jeong.mapmo.databinding.FragmentMemoBinding
import com.jeong.mapmo.ui.adapter.MemoAdapter
import com.jeong.mapmo.ui.adapter.SwipeHelper
import com.jeong.mapmo.util.BaseFragment

class MemoFragment : BaseFragment<FragmentMemoBinding>(FragmentMemoBinding::inflate) {
    private var _memoAdaper: MemoAdapter? = null
    val memoAdapter get() = requireNotNull(_memoAdaper)


    override fun initView() {

        val swipeHelper = SwipeHelper()
        val itemTouchHelper = ItemTouchHelper(swipeHelper)
        itemTouchHelper.attachToRecyclerView(binding.rvMemo)

        _memoAdaper = MemoAdapter()
        binding.rvMemo.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = memoAdapter
        }

        val a = listOf<Memo>(
            Memo("1","제목11111111111111111111111111111111111111111111111111111111111111111111", 1.1,2.2,"디테일1", PriorityColor.RED),
            Memo("2","제목2222222222222222222", 1.1,2.2,"디테일2222222222222222222222222222222222222222222222222", PriorityColor.YELLOW ),
            Memo("3","제목3333333333", 1.1,2.2,"디테일3", PriorityColor.BLUE ),
            Memo("4","제목444444", 1.1,2.2,"디테일4", PriorityColor.RED),
            Memo("5","제목5555", 1.1,2.2,"디테일5", PriorityColor.RED ),
            Memo("6","제목66", 1.1,2.2,"디테일6", PriorityColor.YELLOW ),
            Memo("7","제목7", 1.1,2.2,"디테일7", PriorityColor.YELLOW),
            Memo("8","제목8", 1.1,2.2,"디테일8", PriorityColor.BLUE),
            Memo("9","제목9", 1.1,2.2,"디테일9", PriorityColor.BLUE),
            Memo("10","제목10", 1.1,2.2,"디테일10", PriorityColor.BLUE),
            Memo("11","제목11", 1.1,2.2,"디테일11", PriorityColor.RED),
            Memo("12","제목12", 1.1,2.2,"디테일12", PriorityColor.YELLOW),
        )
        memoAdapter.submitList(a)
    }









    override fun onDestroyView() {
        super.onDestroyView()
        _memoAdaper = null
    }
}
