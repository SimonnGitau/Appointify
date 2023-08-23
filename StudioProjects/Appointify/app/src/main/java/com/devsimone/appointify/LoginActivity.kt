package com.devsimone.appointify

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog
import com.devsimone.appointify.LecturerDash.LecActivity
import com.devsimone.appointify.StudentDash.StudentActivity
import com.devsimone.appointify.databinding.ActivityLoginBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import io.paperdb.Paper
import java.security.MessageDigest

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Paper.init(this)

        binding.apply {

            forgotPass.setOnClickListener {
                Toast.makeText(this@LoginActivity, "Lol! me too", Toast.LENGTH_LONG).show()
            }

            btnLogIn.setOnClickListener {

                if (edtUsernameLogIn.text.toString().trim() != "" &&
                    edtPasswordLogIn.text.toString().trim() != ""
                ) {

                    val sweetAlertDialog =
                        SweetAlertDialog(this@LoginActivity, SweetAlertDialog.PROGRESS_TYPE)
                    sweetAlertDialog.contentText = "Logging into your account"
                    sweetAlertDialog.setCancelable(false)
                    sweetAlertDialog.show()

                    val originalChildName = edtUsernameLogIn.text.toString()
                    val childName = encodeChildName(originalChildName)

                    val database = FirebaseDatabase.getInstance().reference
                    database.child("Login")
                        .child(childName)
                        .addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val value =
                                    snapshot.child("login_password").getValue(String::class.java)
                                val pass = hashString("SHA-512", edtPasswordLogIn.text.toString())
                                if (value != null && value == pass) {

                                    Paper.book()
                                        .write("userAdmin", edtUsernameLogIn.text.toString())

                                    val isLoginRank =
                                        snapshot.child("login_rank").getValue(Boolean::class.java)
                                            ?: false
                                    if (isLoginRank) lecDAsh() else stuDash()

                                } else {
                                    sweetAlertDialog.dismissWithAnimation()
                                    val sweetAlertDialog =
                                        SweetAlertDialog(
                                            this@LoginActivity,
                                            SweetAlertDialog.ERROR_TYPE
                                        )
                                    sweetAlertDialog.contentText =
                                        "Failed to log you in. Please check your credentials and try again."
                                    sweetAlertDialog.setCancelable(true)
                                    sweetAlertDialog.show()
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                sweetAlertDialog.dismissWithAnimation()
                                val sweetAlertDialog =
                                    SweetAlertDialog(
                                        this@LoginActivity,
                                        SweetAlertDialog.ERROR_TYPE
                                    )
                                sweetAlertDialog.contentText = error.message
                                sweetAlertDialog.setCancelable(true)
                                sweetAlertDialog.show()
                            }
                        })

                } else {
                    val sweetAlertDialog =
                        SweetAlertDialog(this@LoginActivity, SweetAlertDialog.ERROR_TYPE)
                    sweetAlertDialog.contentText = "Please enter all the required fields!"
                    sweetAlertDialog.setCancelable(true)
                    sweetAlertDialog.show()
                }


            }

            btnToSignUp.setOnClickListener {
                startActivity(Intent(this@LoginActivity, SignupActivity::class.java))
                finish()
            }

        }

    }

    private fun stuDash() {
        startActivity(Intent(this@LoginActivity, StudentActivity::class.java))
        finish()
    }

    private fun lecDAsh() {
        startActivity(Intent(this@LoginActivity, LecActivity::class.java))
        finish()
    }

    private fun hashString(type: String, input: String): String {
        val HEX_CHARS = "0123456789ABCDEF"
        val bytes = MessageDigest
            .getInstance(type)
            .digest(input.toByteArray())
        val result = StringBuilder(bytes.size * 2)

        bytes.forEach {
            val i = it.toInt()
            result.append(HEX_CHARS[i shr 4 and 0x0f])
            result.append(HEX_CHARS[i and 0x0f])
        }

        return result.toString()
    }

    private fun encodeChildName(childName: String): String {
        return childName.replace("/", "-")
    }
}