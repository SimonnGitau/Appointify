package com.devsimone.appointify.LecturerDash.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.devsimone.appointify.LecturerDash.Adapter.LecHomeAdapter
import com.devsimone.appointify.Models.Appointment
import com.devsimone.appointify.StudentDash.Activities.SettingsActivity
import com.devsimone.appointify.databinding.FragmentLechomeBinding
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import io.paperdb.Paper


class LechomeFragment : Fragment() {

    private lateinit var binding: FragmentLechomeBinding
    private lateinit var appListLec: MutableList<Appointment>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentLechomeBinding.inflate(layoutInflater, container, false)

        binding.apply {

            toSet.setOnClickListener {
                startActivity(Intent(context, SettingsActivity::class.java))
            }

            val originalChildName1 = Paper.book().read<String>("userAdmin")
            val childName1 = originalChildName1?.let { encodeChildName(it) }

            if (childName1 != null) {
                FirebaseDatabase.getInstance().reference.child("Login")
                    .child(childName1)
                    .addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {

                            textView.text =
                                "Welcome,  ${dataSnapshot.child("login_username").value}"
                            Glide.with(this@LechomeFragment)
                                .load(dataSnapshot.child("userDp").value.toString()).into(homeDp)

                        }

                        override fun onCancelled(error: DatabaseError) {
                            // Handle the error if any
                        }
                    })
            }



            updateList()

            homeRef.setOnRefreshListener {
                updateList()
                appListLec.clear()
                homeRef.isRefreshing = false
            }

        }

        return binding.root
    }

    private fun updateList() {
        appListLec = mutableListOf()

        val childEventListener1 = object : ChildEventListener {
            override fun onChildAdded(
                snapshot1: DataSnapshot,
                previousChildName: String?
            ) {
                if (snapshot1.exists()) {

                    val app = Appointment()
                    app.appLink = snapshot1.child("appLink").value.toString()
                    app.appOwnId = snapshot1.child("appOwnId").value.toString()
                    app.appId = snapshot1.child("appId").value.toString()
                    app.appLecId = snapshot1.child("appLecId").value.toString()
                    app.appTopic = snapshot1.child("appTopic").value.toString()
                    app.appDate = snapshot1.child("appDate").value.toString()
                    app.appTime = snapshot1.child("appTime").value.toString()
                    app.appDes = snapshot1.child("appDes").value.toString()

                    if (app.appLecId == Paper.book().read<String>("userAdmin")) {
                        appListLec.add(app)
                        binding.textView3.visibility = View.GONE
                        binding.animationView.visibility = View.GONE
                        binding.rcApp.visibility = View.VISIBLE
                        binding.rcApp.layoutManager = LinearLayoutManager(context)
                        binding.rcApp.adapter =
                            context?.let { LecHomeAdapter(appListLec, context = it) }
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

        val originalChildName = Paper.book().read<String>("userAdmin")
        val childName = encodeChildName(originalChildName.toString())

        FirebaseDatabase.getInstance().reference.child("Login")
            .child(childName)
            .child("Appointments")
            .orderByChild("appStatus")
            .equalTo(true.toString())
            .addChildEventListener(childEventListener1)

    }

    private fun encodeChildName(childName: String): String {
        return childName.replace("/", "-")
    }

}