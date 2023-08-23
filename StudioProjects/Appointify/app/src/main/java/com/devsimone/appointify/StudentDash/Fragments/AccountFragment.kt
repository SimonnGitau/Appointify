package com.devsimone.appointify.StudentDash.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.devsimone.appointify.Models.Notifications
import com.devsimone.appointify.StudentDash.Adapters.AccountsAdapter
import com.devsimone.appointify.databinding.FragmentAccountBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import io.paperdb.Paper

class AccountFragment : Fragment() {

    private lateinit var binding: FragmentAccountBinding
    private lateinit var notificationsList: MutableList<Notifications>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountBinding.inflate(layoutInflater, container, false)

        binding.apply {


            val originalChildName1 = Paper.book().read<String>("userAdmin")
            val childName1 = originalChildName1?.let { encodeChildName(it) }

            if (childName1 != null) {
                FirebaseDatabase.getInstance().reference.child("Login")
                    .child(childName1)
                    .addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {

                            Glide.with(this@AccountFragment)
                                .load(dataSnapshot.child("userDp").value.toString()).into(accDp)

                        }

                        override fun onCancelled(error: DatabaseError) {
                            // Handle the error if any
                        }
                    })
            }

            updateList()

            homeRef.setOnRefreshListener {
                updateList()
                notificationsList.clear()
                homeRef.isRefreshing = false
            }

            return binding.root
        }
    }

    private fun updateList() {

        notificationsList = mutableListOf()

        val originalChildName = Paper.book().read<String>("userAdmin")
        val childName = originalChildName?.let { encodeChildName(it) }

        val database = FirebaseDatabase.getInstance()

        childName?.let { database.reference.child("Login").child(it).child("Notifications") }
            ?.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        val app = Notifications()

                        app.notText =
                            dataSnapshot.child("notText").getValue(String::class.java).toString()
                        app.notID =
                            dataSnapshot.child("notID").getValue(String::class.java).toString()

                        notificationsList.add(app)
                        binding.textView3.visibility = View.GONE
                        binding.animationView.visibility = View.GONE
                        binding.homeRef.visibility = View.VISIBLE
                        binding.rcAcc.visibility = View.VISIBLE
                        binding.rcAcc.layoutManager = LinearLayoutManager(context)
                        binding.rcAcc.adapter =
                            context?.let { AccountsAdapter(notificationsList, context = it) }

                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle the error
                }
            })


    }

}

private fun encodeChildName(childName: String): String {
    return childName.replace("/", "-")
}


