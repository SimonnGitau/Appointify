package com.devsimone.appointify.StudentDash.Activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog
import com.devsimone.appointify.LoginActivity
import com.devsimone.appointify.MainActivity
import com.devsimone.appointify.databinding.ActivitySettingsBinding
import com.google.firebase.database.FirebaseDatabase
import io.paperdb.Paper

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {

            btnBackAdd.setOnClickListener { onBackPressedDispatcher.onBackPressed() }

            userProfEdt.setOnClickListener {
                startActivity(Intent(this@SettingsActivity, EditProfileActivity::class.java))
            }

            userLogOut.setOnClickListener {
                val sweetAlertDialog1 =
                    SweetAlertDialog(this@SettingsActivity, SweetAlertDialog.WARNING_TYPE)
                sweetAlertDialog1.contentText = "End session?"
                sweetAlertDialog1.setCancelable(true)
                sweetAlertDialog1.show()
                sweetAlertDialog1.setConfirmClickListener {
                    sweetAlertDialog1.dismissWithAnimation()
                    startActivity(Intent(this@SettingsActivity, LoginActivity::class.java))
                    finish()
                }
            }

            userDel.setOnClickListener {
                val sweetAlertDialog1 =
                    SweetAlertDialog(this@SettingsActivity, SweetAlertDialog.WARNING_TYPE)
                sweetAlertDialog1.contentText =
                    "Are you sure you want to delete your account? This action is irreversible!"
                sweetAlertDialog1.setCancelable(true)
                sweetAlertDialog1.show()
                sweetAlertDialog1.setConfirmClickListener {
                    deleteBooking()
                    sweetAlertDialog1.dismissWithAnimation()
                }

            }


        }

    }

    private fun deleteBooking() {

        val id = Paper.book().read<String>("userAdmin")
        val childName = encodeChildName(id.toString())

        val database = FirebaseDatabase.getInstance()
        val loginRef = database.reference.child("Login").child(childName)

        loginRef.removeValue().addOnSuccessListener {
            startActivity(Intent(this@SettingsActivity, MainActivity::class.java))
            finish()

        }.addOnFailureListener { exception ->
            val sweetAlertDialog1 =
                SweetAlertDialog(this@SettingsActivity, SweetAlertDialog.ERROR_TYPE)
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