package com.jeong.mapmo.ui.view.onboarding.component

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OnboardingItem(
    val title: String,
    val image: Int,
    val description: String,
    val buttonText: String,
    val isSkip: Boolean
) : Parcelable