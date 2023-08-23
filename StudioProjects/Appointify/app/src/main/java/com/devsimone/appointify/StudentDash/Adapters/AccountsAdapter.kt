package com.devsimone.appointify.StudentDash.Adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.devsimone.appointify.Models.Notifications
import com.devsimone.appointify.databinding.AccNotBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AccountsAdapter(

    private var notList: MutableList<Notifications>,
    private val context: Context

) : RecyclerView.Adapter<AccountsAdapter.AccViewHolder>() {

    class AccViewHolder(accNot: AccNotBinding) :
        RecyclerView.ViewHolder(accNot.root) {
        val binding = accNot
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = AccViewHolder(
        AccNotBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun getItemCount() = notList.size

    override fun onBindViewHolder(holder: AccViewHolder, position: Int) {

        val app = notList[position]

        holder.binding.apply {

            val originalChildName = app.notID
            val childName = encodeChildName(originalChildName)

            text.text = app.notText

            FirebaseDatabase.getInstance().reference.child("Login")
                .child(childName)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        Glide.with(context).load(dataSnapshot.child("userDp").value.toString())
                            .into(notDp)
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        // Handle database error
                        Log.e("Firebase", "Database Error: ${databaseError.message}")
                    }
                })

        }

    }

    private fun encodeChildName(childName: String): String {
        return childName.replace("/", "-")
    }

}