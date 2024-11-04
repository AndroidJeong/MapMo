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
                title = "정확한 위치",
                image = R.drawable.ic_launcher_background,
                description = getString(R.string.onboarding_01),
                buttonText = getString(R.string.onboarding_next),
                isSkip = true
            ),
            OnboardingItem(
                title = "위치 액세스 권한\n항상 허용",
                image = R.drawable.ic_launcher_background,
                description = getString(R.string.onboarding_01),
                buttonText = getString(R.string.onboarding_next),
                isSkip = true
            ),
            OnboardingItem(
                title = "내 신체 활동 정보",
                image = R.drawable.ic_launcher_background,
                description = getString(R.string.onboarding_01),
                buttonText = getString(R.string.onboarding_next),
                isSkip = true
            ),
            OnboardingItem(
                title = "알람 및 리마인더\n권한 허용",
                image = R.drawable.ic_launcher_background,
                description = getString(R.string.onboarding_01),
                buttonText = getString(R.string.onboarding_next),
                isSkip = true
            ),
            OnboardingItem(
                title = "배터리 사용량 최적화 중지\n(추천)",
                image = R.drawable.ic_launcher_background,
                description = getString(R.string.onboarding_01),
                buttonText = getString(R.string.onboarding_next),
                isSkip = true
            ),
            OnboardingItem(
                title = "권한 설정 완료!",
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