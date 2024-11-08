package com.jeong.mapmo.ui.view.onboarding.component

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.jeong.mapmo.ui.view.onboarding.OnboardingContentsFragment
import com.jeong.mapmo.util.Constants.ONBOARDING_OBJECT

class OnboardingAdapter(
    fragment: Fragment,
    private val onboardingItems: List<OnboardingItem>
) : FragmentStateAdapter(fragment) {

    override fun getItemCount() = onboardingItems.size

    override fun createFragment(position: Int): Fragment {
        val fragment = OnboardingContentsFragment()
        fragment.arguments = Bundle().apply {
            putParcelable(ONBOARDING_OBJECT, onboardingItems[position])
        }
        return fragment
    }
}