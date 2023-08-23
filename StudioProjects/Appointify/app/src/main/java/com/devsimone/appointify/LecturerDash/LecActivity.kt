package com.devsimone.appointify.LecturerDash

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.devsimone.appointify.LecturerDash.Fragments.LecbookFragment
import com.devsimone.appointify.LecturerDash.Fragments.LechomeFragment
import com.devsimone.appointify.databinding.ActivityLecBinding
import io.paperdb.Paper

class LecActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLecBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLecBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Paper.book().read<String>("userAdmin")

        binding.apply {

            pager.adapter = PagerAdapter(this@LecActivity)
            pager.offscreenPageLimit = 3

            bottomNavigationView.setupWithViewPager2(pager)
            bottomNavigationView.isSelected = true
        }

    }

    private class PagerAdapter(fragmentActivity: FragmentActivity) :
        FragmentStateAdapter(fragmentActivity) {

        override fun getItemCount() = 2

        override fun createFragment(position: Int) =
            when (position) {
                0 -> LechomeFragment()
                1 -> LecbookFragment()
                else -> LechomeFragment()

            }

    }
}