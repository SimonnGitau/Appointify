package com.devsimone.appointify

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.devsimone.appointify.databinding.ActivityMainBinding
import io.paperdb.Paper

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({

            Paper.init(this@MainActivity)
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                finish()

        }, 2000)
    }
}