package com.jeong.mapmo.ui.view.onboarding

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jeong.mapmo.R
import com.jeong.mapmo.domain.repository.ResourceRepository
import com.jeong.mapmo.ui.view.onboarding.component.OnboardingItem


class OnboardingViewModel(private val resourceRepository: ResourceRepository) : ViewModel() {
    val onboardingItemIndex = MutableLiveData<Int>().apply { value = 0 }
    val onboardingItems by lazy {
        listOf(
            OnboardingItem(
                title = "권한 허용 및 최적화 필요",
                image = R.drawable.ic_launcher_background,
                description = getString(R.string.onboarding_01),
                buttonText = getString(R.string.onboarding_next),
                isSkip = true
            ),
            OnboardingItem(
                title = "권한 필요2",
                image = R.drawable.ic_launcher_background,
                description = getString(R.string.onboarding_01),
                buttonText = getString(R.string.onboarding_next),
                isSkip = true
            ),
            OnboardingItem(
                title = "권한 필요3",
                image = R.drawable.ic_launcher_background,
                description = getString(R.string.onboarding_01),
                buttonText = getString(R.string.onboarding_next),
                isSkip = true
            ),
            OnboardingItem(
                title = "권한 필요4",
                image = R.drawable.ic_launcher_background,
                description = getString(R.string.onboarding_01),
                buttonText = getString(R.string.onboarding_next),
                isSkip = true
            )
        )
    }

    private fun getString(resourceId: Int): String {
        return resourceRepository.getString(resourceId)
    }
}