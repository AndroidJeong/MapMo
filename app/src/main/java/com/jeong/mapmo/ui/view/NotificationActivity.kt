package com.jeong.mapmo.ui.view

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.jeong.mapmo.R
import com.jeong.mapmo.data.dto.Memo
import com.jeong.mapmo.databinding.ActivityNotificationBinding


class NotificationActivity : AppCompatActivity() {

    private val binding: ActivityNotificationBinding by lazy {
        ActivityNotificationBinding.inflate(layoutInflater)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val bundle = intent.getBundleExtra("memoBundle")
        //binding.textView.text =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                Log.d("location2", "bundle: ${bundle?.getParcelable("memo", Memo::class.java)}")
            } else {
                Log.d("location2", "bundle: ${bundle?.getParcelable("memo") as? Memo}")
            }

    }
}