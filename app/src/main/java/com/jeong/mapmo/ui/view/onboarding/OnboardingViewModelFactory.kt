package com.jeong.mapmo.ui.view.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jeong.mapmo.domain.repository.ResourceRepository

class OnboardingViewModelFactory(private val resourceRepository: ResourceRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OnboardingViewModel::class.java)) {
            return OnboardingViewModel(resourceRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}