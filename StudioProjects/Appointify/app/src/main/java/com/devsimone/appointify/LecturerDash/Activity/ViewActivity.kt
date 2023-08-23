package com.devsimone.appointify.LecturerDash.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bumptech.glide.Glide
import com.devsimone.appointify.LecturerDash.LecActivity
import com.devsimone.appointify.Models.Notifications
import com.devsimone.appointify.databinding.ActivityViewBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import io.paperdb.Paper

class ViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {

            val receivedBundle = intent.extras
            if (receivedBundle != null) {

                val appId = receivedBundle.getString("appId")
                val appUsername = receivedBundle.getString("appUsername")
                val appUserdp = receivedBundle.getString("appUserdp")


                Toast.makeText(this@ViewActivity, appId, Toast.LENGTH_LONG).show()

                Glide.with(this@ViewActivity).load(appUserdp).into(binding.addAppDp)
                binding.addAppName.text = appUsername

                val originalChildName = Paper.book().read<String>("userAdmin")
                val childName = encodeChildName(originalChildName.toString())

                var stuID: String? = null
                var text: String? = null

                FirebaseDatabase.getInstance().reference.child("Login")
                    .child(childName)
                    .child("Appointments")
                    .child(appId.toString())
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {

                            edtEventName.text = dataSnapshot.child("appTopic").value.toString()
                            edtEventDate.text = dataSnapshot.child("appDate").value.toString()
                            edtEventVenue.text = dataSnapshot.child("appTimeEnd").value.toString()
                            edtEventDescription.text = dataSnapshot.child("appDes").value.toString()
                            edtStartDate.text = dataSnapshot.child("appTime").value.toString()

                            stuID = dataSnapshot.child("appOwnId").value.toString()
                            text =
                                "Your appointment on ${dataSnapshot.child("appTopic").value} has been declined. Please reschedule it at another time."

                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            // Handle database error
                            Log.e("Firebase", "Database Error: ${databaseError.message}")
                        }
                    })

                toStuProfile.setOnClickListener {

                    val intent = Intent(this@ViewActivity, ProfileActivity::class.java)
                    val bundle = Bundle()
                    bundle.putString("profAdmin", stuID)
                    intent.putExtras(bundle)
                    root.context.startActivity(intent)

                }


                btnDel.setOnClickListener {
                    val addNotif = text?.let { it1 ->
                        Paper.book().read<String>("userAdmin")
                            ?.let { it2 -> Notifications(it1, it2) }
                    }

                    val notPost: java.util.HashMap<String, String> = java.util.HashMap()
                    if (addNotif != null) {
                        notPost["notText"] = addNotif.notText
                        notPost["notID"] = addNotif.notID
                    }

                    val childName1 = stuID?.let { it1 -> encodeChildName(it1) }

                    val sweetAlertDialog1 =
                        SweetAlertDialog(this@ViewActivity, SweetAlertDialog.WARNING_TYPE)
                    sweetAlertDialog1.contentText = "Decline this appointment?"
                    sweetAlertDialog1.setCancelable(true)
                    sweetAlertDialog1.show()
                    sweetAlertDialog1.setConfirmClickListener {

                        sweetAlertDialog1.dismissWithAnimation()

                        if (childName1 != null) {
                            FirebaseDatabase.getInstance().reference.child("Login")
                                .child(childName1)
                                .child("Notifications")
                                .setValue(notPost)
                                .addOnSuccessListener {
                                    val sweetAlertDialog1 =
                                        SweetAlertDialog(
                                            this@ViewActivity,
                                            SweetAlertDialog.SUCCESS_TYPE
                                        )
                                    sweetAlertDialog1.contentText = "Loading..."
                                    sweetAlertDialog1.setCancelable(true)
                                    sweetAlertDialog1.show()
                                    sweetAlertDialog1.setConfirmClickListener {

                                        sweetAlertDialog1.dismissWithAnimation()

                                        val database = FirebaseDatabase.getInstance()
                                        val loginRef = database.reference.child("Login")
                                        val appointmentsRef =
                                            loginRef.child(childName).child("Appointments")
                                        val appointment1Ref = appId?.let { it1 ->
                                            appointmentsRef.child(
                                                it1
                                            )
                                        }

                                        appointment1Ref?.removeValue()?.addOnSuccessListener {
                                            // Deletion successful
                                            val sweetAlertDialog1 =
                                                SweetAlertDialog(
                                                    this@ViewActivity,
                                                    SweetAlertDialog.SUCCESS_TYPE
                                                )
                                            sweetAlertDialog1.contentText =
                                                "The student has been notified!"
                                            sweetAlertDialog1.setCancelable(true)
                                            sweetAlertDialog1.show()
                                            sweetAlertDialog1.setConfirmClickListener {
                                                sweetAlertDialog1.dismissWithAnimation()
                                            }

                                        }
                                    }

                                }


                        }
                    }
                }

                btnApp.setOnClickListener {
                    if (edtMeetLink.text.toString().trim() == "") {
                        val sweetAlertDialog1 =
                            SweetAlertDialog(this@ViewActivity, SweetAlertDialog.ERROR_TYPE)
                        sweetAlertDialog1.contentText = "Please enter meeting code!"
                        sweetAlertDialog1.setCancelable(true)
                        sweetAlertDialog1.show()
                    } else {

                        //updateMeeting()
                        val originalChildName = Paper.book().read<String>("userAdmin")
                        val childName = encodeChildName(originalChildName.toString())

                        val updates: MutableMap<String, Any> = HashMap()

                        updates["appLink"] = binding.edtMeetLink.text.toString()
                        updates["appStatus"] = true.toString()

                        FirebaseDatabase.getInstance().reference.child("Login")
                            .child(childName)
                            .child("Appointments")
                            .child(appId.toString())
                            .updateChildren(updates)
                            .addOnSuccessListener {
                                val sweetAlertDialog1 =
                                    SweetAlertDialog(
                                        this@ViewActivity,
                                        SweetAlertDialog.SUCCESS_TYPE
                                    )
                                sweetAlertDialog1.contentText = "Meeting approved!"
                                sweetAlertDialog1.setCancelable(false)
                                sweetAlertDialog1.show()
                                sweetAlertDialog1.setConfirmClickListener {
                                    startActivity(
                                        Intent(
                                            this@ViewActivity,
                                            LecActivity::class.java
                                        )
                                    )
                                    finish()
                                }
                            }.addOnFailureListener {
                                val sweetAlertDialog1 =
                                    SweetAlertDialog(
                                        this@ViewActivity,
                                        SweetAlertDialog.ERROR_TYPE
                                    )
                                sweetAlertDialog1.contentText = it.message
                                sweetAlertDialog1.setCancelable(true)
                                sweetAlertDialog1.show()
                            }
                    }
                }
            }
        }

    }
}

private fun encodeChildName(childName: String): String {
    return childName.replace("/", "-")
}

