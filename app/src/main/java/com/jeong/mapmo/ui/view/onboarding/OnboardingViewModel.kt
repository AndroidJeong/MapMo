package com.jeong.mapmo.ui.view.onboarding

import androidx.lifecycle.ViewModel
import com.jeong.mapmo.R
import com.jeong.mapmo.domain.repository.ResourceRepository
import com.jeong.mapmo.ui.view.onboarding.component.OnboardingItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class OnboardingViewModel(private val resourceRepository: ResourceRepository) : ViewModel() {

    private val _onboardingItems = MutableStateFlow<List<OnboardingItem>>(emptyList())
    val onboardingItems: StateFlow<List<OnboardingItem>> get() = _onboardingItems

    private val _currentPage = MutableStateFlow(0)
    val currentPage: StateFlow<Int> get() = _currentPage

    private val _isSkipVisible = MutableStateFlow(true)
    val isSkipVisible: StateFlow<Boolean> get() = _isSkipVisible

    private val _isDoneVisible = MutableStateFlow(false)
    val isDoneVisible: StateFlow<Boolean> get() = _isDoneVisible

    private val _isNextVisible = MutableStateFlow(true)
    val isNextVisible: StateFlow<Boolean> get() = _isNextVisible

    var totalItemCount = 0

    init {
        loadDummyData()
    }

    private fun loadDummyData() {
        val dummyData = listOf(
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
        _onboardingItems.value = dummyData
        totalItemCount = dummyData.size
    }

    fun updatePage(position: Int) {
        _currentPage.value = position
        updateButtonVisibility(position)
    }

    private fun updateButtonVisibility(position: Int) {
        if (position == totalItemCount - 1) {
            _isSkipVisible.value = false
            _isDoneVisible.value = true
            _isNextVisible.value = false
        } else {
            _isSkipVisible.value = true
            _isDoneVisible.value = false
            _isNextVisible.value = true
        }
    }

    fun goToNextPage() {
        val nextPage = (_currentPage.value) + 1
        if (nextPage < totalItemCount) {
            _currentPage.value = nextPage
        }
    }

    private fun getString(resourceId: Int): String {
        return resourceRepository.getString(resourceId)
    }
}