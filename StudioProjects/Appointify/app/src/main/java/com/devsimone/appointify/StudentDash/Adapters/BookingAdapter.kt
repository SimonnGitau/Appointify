package com.devsimone.appointify.StudentDash.Adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bumptech.glide.Glide
import com.devsimone.appointify.Models.Appointment
import com.devsimone.appointify.databinding.BookAppBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class BookingAdapter(

    private var bookList: MutableList<Appointment>,
    private val context: Context

) : RecyclerView.Adapter<BookingAdapter.BookViewHolder>() {

    class BookViewHolder(bookApp: BookAppBinding) :
        RecyclerView.ViewHolder(bookApp.root) {
        val binding = bookApp
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = BookViewHolder(
        BookAppBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun getItemCount() = bookList.size

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val app = bookList[position]

        holder.binding.apply {

            val originalChildName = app.appLecId
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

            }

            root.setOnLongClickListener {

                val sweetAlertDialog1 =
                    SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                sweetAlertDialog1.contentText =
                    "Are you sure you want to delete this appointment? This action is irreversible!"
                sweetAlertDialog1.setCancelable(true)
                sweetAlertDialog1.show()
                sweetAlertDialog1.setConfirmClickListener {
                    sweetAlertDialog1.dismissWithAnimation()
                    deleteBooking(app.appId, childName)
                }

                true
            }
        }
    }

    private fun deleteBooking(appId: String, childName: String) {
        Toast.makeText(context, appId, Toast.LENGTH_LONG).show()

        val database = FirebaseDatabase.getInstance()
        val loginRef = database.reference.child("Login")
        val appointmentsRef = loginRef.child(childName).child("Appointments")
        val appointment1Ref = appointmentsRef.child(appId)

        appointment1Ref.removeValue()
            .addOnSuccessListener {
                // Deletion successful
                val sweetAlertDialog1 =
                    SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                sweetAlertDialog1.contentText =
                    "Your booking has been deleted successfully! You can make another appointment at anytime."
                sweetAlertDialog1.setCancelable(true)
                sweetAlertDialog1.show()
                sweetAlertDialog1.setConfirmClickListener {
                    sweetAlertDialog1.dismissWithAnimation()
                }

            }
            .addOnFailureListener { exception ->
                val sweetAlertDialog1 =
                    SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                sweetAlertDialog1.contentText = "Oops!: ${exception.message}"
                sweetAlertDialog1.setCancelable(true)
                sweetAlertDialog1.show()
                sweetAlertDialog1.setConfirmClickListener {
                    sweetAlertDialog1.dismissWithAnimation()
                }
            }

    }

    private fun encodeChildName(childName: String): String {
        return childName.replace("/", "-")
    }

}