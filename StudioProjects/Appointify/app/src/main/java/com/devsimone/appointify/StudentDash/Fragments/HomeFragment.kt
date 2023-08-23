package com.devsimone.appointify.StudentDash.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.devsimone.appointify.Models.Appointment
import com.devsimone.appointify.StudentDash.Activities.ApointActivity
import com.devsimone.appointify.StudentDash.Activities.SettingsActivity
import com.devsimone.appointify.StudentDash.Adapters.HomeAdapter
import com.devsimone.appointify.databinding.FragmentHomeBinding
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import io.paperdb.Paper

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var appList: MutableList<Appointment>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)

        binding.apply {

            val originalChildName1 = Paper.book().read<String>("userAdmin")
            val childName1 = originalChildName1?.let { encodeChildName(it) }

            if (childName1 != null) {
                FirebaseDatabase.getInstance().reference.child("Login")
                    .child(childName1)
                    .addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {

                            textView.text =
                                "Welcome,  ${dataSnapshot.child("login_username").value}"
                            Glide.with(this@HomeFragment)
                                .load(dataSnapshot.child("userDp").value.toString()).into(homeDp)

                        }

                        override fun onCancelled(error: DatabaseError) {
                            // Handle the error if any
                        }
                    })
            }

            toSet.setOnClickListener {
                startActivity(Intent(context, SettingsActivity::class.java))
            }

            btnAddEvent.setOnClickListener {
                startActivity(Intent(context, ApointActivity::class.java))
            }

            updateList()

            homeRef.setOnRefreshListener {
                updateList()
                appList.clear()
                homeRef.isRefreshing = false
            }

        }
        return binding.root
    }

    private fun updateList() {

        appList = mutableListOf()

        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                if (snapshot.exists()) {

                    val childEventListener1 = object : ChildEventListener {
                        override fun onChildAdded(
                            snapshot1: DataSnapshot,
                            previousChildName: String?
                        ) {
                            if (snapshot1.exists()) {

                                val app = Appointment()
                                app.appId = snapshot1.child("appId").value.toString()
                                app.appOwnId = snapshot1.child("appOwnId").value.toString()
                                app.appLecId = snapshot1.child("appLecId").value.toString()
                                app.appTopic = snapshot1.child("appTopic").value.toString()
                                app.appDate = snapshot1.child("appDate").value.toString()
                                app.appTime = snapshot1.child("appTime").value.toString()
                                app.appDes = snapshot1.child("appDes").value.toString()
                                app.appLink = snapshot1.child("appLink").value.toString()

                                if (app.appOwnId == Paper.book().read<String>("userAdmin")) {
                                    appList.add(app)
                                    binding.textView3.visibility = View.GONE
                                    binding.animationView.visibility = View.GONE
                                    binding.rcApp.visibility = View.VISIBLE
                                    binding.rcApp.layoutManager = LinearLayoutManager(context)
                                    binding.rcApp.adapter =
                                        context?.let { HomeAdapter(appList, context = it) }
                                } else {
                                    //Hello
                                    binding.textView3.visibility = View.VISIBLE
                                    binding.animationView.visibility = View.VISIBLE
                                    binding.rcApp.visibility = View.GONE
                                }

                            }

                        }

                        override fun onChildChanged(
                            snapshot: DataSnapshot,
                            previousChildName: String?
                        ) {
                        }

                        override fun onChildRemoved(snapshot: DataSnapshot) {
                        }

                        override fun onChildMoved(
                            snapshot: DataSnapshot,
                            previousChildName: String?
                        ) {
                        }

                        override fun onCancelled(error: DatabaseError) {
                        }
                    }

                    val originalChildName = snapshot.child("login_admin").value.toString()
                    val childName = encodeChildName(originalChildName)

                    FirebaseDatabase.getInstance().reference.child("Login")
                        .child(childName)
                        .child("Appointments")
                        .orderByChild("appStatus")
                        .equalTo(true.toString())
                        .addChildEventListener(childEventListener1)

                } else {
                    // Data does not exist
                    Toast.makeText(context, "No data found!", Toast.LENGTH_LONG)
                        .show()
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onCancelled(error: DatabaseError) {
            }

        }
        FirebaseDatabase.getInstance().reference.child("Login")
            .orderByChild("login_rank")
            .equalTo(true)
            .addChildEventListener(childEventListener)

    }

    private fun encodeChildName(childName: String): String {
        return childName.replace("/", "-")
    }

}