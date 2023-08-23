package com.devsimone.appointify.StudentDash.Activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bumptech.glide.Glide
import com.devsimone.appointify.Models.Appointment
import com.devsimone.appointify.StudentDash.StudentActivity
import com.devsimone.appointify.databinding.ActivityAddAppointmentBinding
import com.google.firebase.database.FirebaseDatabase
import io.paperdb.Paper
import java.util.UUID

class AddAppointmentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddAppointmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddAppointmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = Paper.book().read<String>("userAdmin")

        val receivedBundle = intent.extras
        if (receivedBundle != null) {
            val lecAdmin = receivedBundle.getString("lecAdmin")
            val lecDp = receivedBundle.getString("lecDp")
            val lecName = receivedBundle.getString("lecName")

            Glide.with(this@AddAppointmentActivity).load(lecDp).into(binding.addAppDp)
            binding.addAppName.text = lecName

            binding.apply {

                btnBackAdd.setOnClickListener { onBackPressedDispatcher.onBackPressed() }


                btnAddAppointment.setOnClickListener {
                    val sweetAlertDialog =
                        SweetAlertDialog(
                            this@AddAppointmentActivity,
                            SweetAlertDialog.PROGRESS_TYPE
                        )
                    sweetAlertDialog.contentText = "Please wait"
                    sweetAlertDialog.show()

                    val appId = UUID.randomUUID().toString()

                    val addAppointment = Appointment(
                        appId,
                        id.toString(),
                        lecAdmin.toString(),
                        edtEventName.text.toString(),
                        edtEventDate.text.toString(),
                        edtTimeStart.text.toString(),
                        edtEventVenue.text.toString(),
                        edtEventDescription.text.toString(),
                        "",
                        false,
                        false
                    )

                    val appPosting: HashMap<String, String> = HashMap()
                    appPosting["appId"] = addAppointment.appId
                    appPosting["appOwnId"] = addAppointment.appOwnId
                    appPosting["appLecId"] = addAppointment.appLecId
                    appPosting["appTopic"] = addAppointment.appTopic
                    appPosting["appDate"] = addAppointment.appDate
                    appPosting["appTime"] = addAppointment.appTime
                    appPosting["appTimeEnd"] = addAppointment.appTimeEnd
                    appPosting["appDes"] = addAppointment.appDes
                    appPosting["appLink"] = addAppointment.appLink
                    appPosting["appStatus"] = false.toString()

                    val originalChildName = lecAdmin.toString()
                    val encodeChildName = encodeChildName(originalChildName)

                    FirebaseDatabase.getInstance().reference.child("Login")
                        .child(encodeChildName).child("Appointments").child(appId)
                        .setValue(appPosting).addOnSuccessListener {
                            sweetAlertDialog.dismissWithAnimation()
                            val sweetAlertDialog1 =
                                SweetAlertDialog(
                                    this@AddAppointmentActivity,
                                    SweetAlertDialog.SUCCESS_TYPE
                                )
                            sweetAlertDialog1.contentText = "Your booking has been added!"
                            sweetAlertDialog1.setCancelable(false)
                            sweetAlertDialog1.show()
                            sweetAlertDialog1.setConfirmClickListener {
                                startActivity(
                                    Intent(
                                        this@AddAppointmentActivity,
                                        StudentActivity::class.java
                                    )
                                )
                                finish()
                            }

                        }.addOnFailureListener {
                            sweetAlertDialog.dismissWithAnimation()
                            val sweetAlertDialog1 =
                                SweetAlertDialog(
                                    this@AddAppointmentActivity,
                                    SweetAlertDialog.ERROR_TYPE
                                )
                            sweetAlertDialog1.contentText = "Booking failed:$it"
                            sweetAlertDialog1.setCancelable(true)
                            sweetAlertDialog1.show()
                        }

                }

            }

        }
    }

    private fun encodeChildName(childName: String): String {
        return childName.replace("/", "-")
    }

}