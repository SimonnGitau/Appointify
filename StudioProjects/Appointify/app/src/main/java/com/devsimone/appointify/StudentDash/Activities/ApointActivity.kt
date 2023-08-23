package com.devsimone.appointify.StudentDash.Activities

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.devsimone.appointify.Models.User
import com.devsimone.appointify.StudentDash.Adapters.LecturerAdapter
import com.devsimone.appointify.databinding.ActivityApointBinding
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase

class ApointActivity : AppCompatActivity() {

    private lateinit var binding: ActivityApointBinding
    private lateinit var userList: MutableList<User>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityApointBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {

            updateList()

            lecRef.setOnRefreshListener {
                updateList()
                userList.clear()
                lecRef.isRefreshing = false
            }

        }
    }

    private fun updateList() {
        userList = mutableListOf()

        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                if (snapshot.exists()) {
                    val user = User()
                    user.userName = snapshot.child("login_username").value.toString()
                    user.userDp = snapshot.child("userDp").value.toString()
                    user.userAdmn = snapshot.child("login_admin").value.toString()

                    userList.add(user)
                    binding.textView3.visibility = View.GONE
                    binding.rcLec.layoutManager = GridLayoutManager(this@ApointActivity, 2)
                    binding.rcLec.adapter =
                        LecturerAdapter(userList, context = this@ApointActivity)
                } else {
                    // Data does not exist
                    Toast.makeText(this@ApointActivity, "No data found!", Toast.LENGTH_LONG)
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

        binding.btnBackLec.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
    }

}