package com.devsimone.appointify.LecturerDash.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.devsimone.appointify.databinding.ActivityProfileBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {


            val receivedBundle = intent.extras
            if (receivedBundle != null) {
                val id = receivedBundle.getString("profAdmin")

                val childName = encodeChildName(id.toString())

                    FirebaseDatabase.getInstance().reference.child("Login")
                        .child(childName)
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                profName.text = dataSnapshot.child("login_username").getValue(String::class.java)

                                Glide.with(this@ProfileActivity).load(dataSnapshot.child("userDp").value.toString()).into(profImg)
                                profMail.text = dataSnapshot.child("login_email").value.toString()
                                profPhone.text = dataSnapshot.child("login_phone").value.toString()
                                profAdmin.text = dataSnapshot.child("login_admin").value.toString()

                            }

                            override fun onCancelled(error: DatabaseError) {
                                Toast.makeText(this@ProfileActivity, "Error: " + error.message, Toast.LENGTH_LONG).show()
                            }
                        })
                }

        }

    }

    private fun encodeChildName(childName: String): String {
        return childName.replace("/", "-")
    }

}