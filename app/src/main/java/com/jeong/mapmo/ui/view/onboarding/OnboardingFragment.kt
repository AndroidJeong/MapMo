package com.jeong.mapmo.ui.view.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.jeong.mapmo.databinding.FragmentOnboardingBinding
import com.jeong.mapmo.domain.repository.ResourceRepository
import com.jeong.mapmo.ui.view.onboarding.component.OnboardingAdapter
import kotlinx.coroutines.launch

class OnboardingFragment : Fragment() {
    private var _binding: FragmentOnboardingBinding? = null
    private val binding get() = _binding!!
    private val viewModel: OnboardingViewModel by activityViewModels {
        OnboardingViewModelFactory(ResourceRepository(requireContext()))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnboardingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = OnboardingAdapter(this, viewModel.onboardingItems.value)
        binding.vpContainer.adapter = adapter
        viewModel.totalItemCount = adapter.itemCount

        binding.vpContainer.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                viewModel.updatePage(position)
            }
        })

        TabLayoutMediator(binding.tabIndicator, binding.vpContainer) { tab, position ->
        }.attach()

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.currentPage.collect { page ->
                binding.vpContainer.currentItem = page
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isSkipVisible.collect { isVisible ->
                binding.btnSkip.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isDoneVisible.collect { isVisible ->
                binding.btnDone.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isNextVisible.collect { isVisible ->
                binding.btnNext.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
            }
        }

        binding.btnNext.setOnClickListener {
            viewModel.goToNextPage()
        }

        binding.btnSkip.setOnClickListener {
            viewModel.updatePage(viewModel.totalItemCount - 1)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}