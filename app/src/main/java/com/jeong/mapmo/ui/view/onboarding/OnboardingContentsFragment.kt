package com.jeong.mapmo.ui.view.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.jeong.mapmo.databinding.FragmentOnboardingContentsBinding
import com.jeong.mapmo.domain.repository.ResourceRepository

class OnboardingContentsFragment : Fragment() {
    private var _binding: FragmentOnboardingContentsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: OnboardingViewModel by activityViewModels {
        OnboardingViewModelFactory(ResourceRepository(requireContext()))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnboardingContentsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        viewModel.onboardingItemIndex.observe(viewLifecycleOwner) {
            updateUI(it)
        }
        // moveToNextItem()
    }

    private fun updateUI(currentIndex: Int) {
        val currentItem = viewModel.onboardingItems[currentIndex]
        binding.tvTitle.text = currentItem.title
        binding.ivOnboarding.setImageResource(currentItem.image)
        binding.tvDescription.text = currentItem.description
        // binding.btnOnboarding.text = currentItem.buttonText
    }

//    private fun moveToNextItem() {
//        binding.btnOnboarding.setOnClickListener {
//            val nextItem = viewModel.onboardingItemIndex.value?.plus(1)
//            if (nextItem != null && nextItem < viewModel.onboardingItems.size) {
//                viewModel.onboardingItemIndex.value = nextItem
//            } else {
//                activity?.finish()
//            }
//        }
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}