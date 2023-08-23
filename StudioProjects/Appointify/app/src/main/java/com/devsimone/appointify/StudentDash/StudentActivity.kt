package com.devsimone.appointify.StudentDash

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.devsimone.appointify.StudentDash.Fragments.AccountFragment
import com.devsimone.appointify.StudentDash.Fragments.BookingsFragment
import com.devsimone.appointify.StudentDash.Fragments.HomeFragment
import com.devsimone.appointify.databinding.ActivityStudentBinding

class StudentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStudentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {

            pager.adapter = PagerAdapter(this@StudentActivity)
            pager.offscreenPageLimit = 3

            bottomNavigationView.setupWithViewPager2(pager)
            bottomNavigationView.isSelected = true
        }

    }

    private class PagerAdapter(fragmentActivity: FragmentActivity) :
        FragmentStateAdapter(fragmentActivity) {

        override fun getItemCount() = 3

        override fun createFragment(position: Int) =
            when (position) {
                0 -> HomeFragment()
                1 -> BookingsFragment()
                2 -> AccountFragment()
                else -> HomeFragment()

            }

    }
}