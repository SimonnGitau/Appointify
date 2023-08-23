package com.devsimone.appointify.LecturerDash.Adapter

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bumptech.glide.Glide
import com.devsimone.appointify.Models.Appointment
import com.devsimone.appointify.databinding.HomeAppBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LecHomeAdapter(
    private var appList: MutableList<Appointment>,
    private val context: Context

) : RecyclerView.Adapter<LecHomeAdapter.LecHomeViewHolder>() {

    class LecHomeViewHolder(homeApp: HomeAppBinding) :
        RecyclerView.ViewHolder(homeApp.root) {
        val binding = homeApp
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = LecHomeViewHolder(
        HomeAppBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun getItemCount() = appList.size

    override fun onBindViewHolder(holder: LecHomeViewHolder, position: Int) {

        val app = appList[position]

        holder.binding.apply {

            val originalChildName = app.appOwnId
            val childName = encodeChildName(originalChildName)

            homeAppTime.text = "${app.appTime} ${app.appDate}"
            homeAppTopic.text = app.appTopic

            FirebaseDatabase.getInstance().reference.child("Login")
                .child(childName)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val loginUsername =
                            dataSnapshot.child("login_username").getValue(String::class.java)

                        Glide.with(context).load(dataSnapshot.child("userDp").value.toString())
                            .into(homeAppDp)
                        homeAppName.text = loginUsername

                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        // Handle database error
                        Log.e("Firebase", "Database Error: ${databaseError.message}")
                    }
                })

            root.setOnClickListener {
                val sweetAlertDialog1 =
                    SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                sweetAlertDialog1.contentText =
                    "Join the meeting now? The student should join within the first 10 min."
                sweetAlertDialog1.setCancelable(true)
                sweetAlertDialog1.show()
                sweetAlertDialog1.setConfirmClickListener {
                    try {
                        val clipboardManager =
                            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val textToCopy = app.appLink
                        val clipData = ClipData.newPlainText("Meet code: ", textToCopy)
                        clipboardManager.setPrimaryClip(clipData)
                        sweetAlertDialog1.dismissWithAnimation()
                        Toast.makeText(context, "Meeting code has been copied!", Toast.LENGTH_LONG)
                            .show()
                    } catch (e: Exception) {
                        Toast.makeText(
                            context,
                            "Error copying link!" + e.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }

        }
    }

    private fun encodeChildName(childName: String): String {
        return childName.replace("/", "-")
    }

}