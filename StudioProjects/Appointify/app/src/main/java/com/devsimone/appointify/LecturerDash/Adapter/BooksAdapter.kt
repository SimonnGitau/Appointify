package com.devsimone.appointify.LecturerDash.Adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.devsimone.appointify.LecturerDash.Activity.ViewActivity
import com.devsimone.appointify.Models.Appointment
import com.devsimone.appointify.databinding.BookAppBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class BooksAdapter(
    private var bookListLec: MutableList<Appointment>,
    private val context: Context
): RecyclerView.Adapter<BooksAdapter.BooksViewHolder>() {

    class BooksViewHolder(bookApp: BookAppBinding) :
        RecyclerView.ViewHolder(bookApp.root) {
        val binding = bookApp
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = BooksViewHolder(
        BookAppBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun getItemCount() = bookListLec.size

    override fun onBindViewHolder(holder: BooksViewHolder, position: Int) {
        val app1 = bookListLec[position]

        holder.binding.apply {

            val originalChildName = app1.appOwnId
            val childName = encodeChildName(originalChildName)

            homeAppTime.text = "${app1.appTime} ${app1.appDate}"
            homeAppTopic.text = app1.appTopic

            var appUsername: String? = null
            var appUserdp: String? = null


            FirebaseDatabase.getInstance().reference.child("Login")
                .child(childName)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        appUsername = dataSnapshot.child("login_username").getValue(String::class.java)
                        appUserdp = dataSnapshot.child("userDp").value.toString()

                        Glide.with(context).load(appUserdp)
                            .into(homeAppDp)
                        homeAppName.text = appUsername

                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        // Handle database error
                        Log.e("Firebase", "Database Error: ${databaseError.message}")
                    }
                })

            root.setOnClickListener {

                val intent = Intent(context, ViewActivity::class.java)
                val bundle = Bundle()
                bundle.putString("appId", app1.appId)
                bundle.putString("appUsername", appUsername)
                bundle.putString("appUserdp", appUserdp)
                intent.putExtras(bundle)
                root.context.startActivity(intent)

                true
            }
        }
    }

    private fun encodeChildName(childName: String): String {
        return childName.replace("/", "-")
    }
}