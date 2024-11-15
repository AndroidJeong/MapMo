package com.jeong.mapmo.ui.view

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.jeong.mapmo.R
import com.jeong.mapmo.data.common.LocationService
import com.jeong.mapmo.databinding.ActivityMainBinding
import com.jeong.mapmo.ui.view.onboarding.OnboardingActivity

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        window.statusBarColor = ContextCompat.getColor(this, R.color.yellow)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }
       // goToOnboarding()
        initNavHost()
        setBottomNavi()
        executeService()

    }

    private fun initNavHost() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(binding.fcvMain.id) as NavHostFragment
        navController = navHostFragment.navController
    }

    private fun setBottomNavi() {
        binding.bnvMain.setupWithNavController(navController)
    }

    private fun goToOnboarding() {
        val intent = Intent(this, OnboardingActivity::class.java)
        startActivity(intent)
    }

    private lateinit var serviceIntent: Intent
    private fun executeService() {
        if (!this::serviceIntent.isInitialized) {
            serviceIntent = Intent(this@MainActivity, LocationService::class.java)
        }
            with(serviceIntent) {
                putExtra("메모한 위치", "위치")
                //Android Oreo 부터는 다음과 같이 호출 한다
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(this)
                } else {
                    startService(this)
                }
            }
    }




}
