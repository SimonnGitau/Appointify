package com.devsimone.appointify

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bumptech.glide.Glide
import com.devsimone.appointify.LecturerDash.LecActivity
import com.devsimone.appointify.Models.User
import com.devsimone.appointify.StudentDash.StudentActivity
import com.devsimone.appointify.databinding.ActivitySignupBinding
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import io.paperdb.Paper
import java.security.MessageDigest
import java.util.HashMap
import java.util.UUID

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var image_uri: Uri
    private lateinit var pickSingleMediaLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Paper.init(this)

        binding.apply {

            val user = User(
                UUID.randomUUID().toString(),
                "",
                edtUsername.text.toString(),
                edtMail.text.toString(),
                edtPhone.text.toString(),
                " ",
                edtAdmn.text.toString(),
                binding.checkAdmin.isChecked
            )

            pickSingleMediaLauncher =
                registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                    if (it.resultCode != Activity.RESULT_OK) {
                        Toast.makeText(
                            this@SignupActivity,
                            "Failed picking media.",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    } else {
                        image_uri = it.data?.data!!
                        Glide.with(this@SignupActivity).load(image_uri).into(userDp)
                    }
                }


            userDp.setOnClickListener {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                    pickSingleMediaLauncher.launch(
                        Intent(MediaStore.ACTION_PICK_IMAGES)
                            .apply {
                                type = "image/*"
                            }
                    )
                else {
                    val intent = Intent(Intent.ACTION_GET_CONTENT)
                    intent.type = "image/*"
                    pickSingleMediaLauncher.launch(intent)
                }
            }

            btnSignUp.setOnClickListener {
                if (userDp.drawable != null &&
                    edtUsername.text.toString().trim() != "" &&
                    edtAdmn.text.toString().trim() != "" &&
                    edtMail.text.toString().trim() != "" &&
                    edtPhone.text.toString().trim() != "" &&
                    edtPassword.text.toString().trim() != "" &&
                    edtPasswordConfirm.text.toString()
                        .trim() != "" && edtPasswordConfirm.text.toString()
                        .trim() == edtPassword.text.toString().trim()
                ) {

                    updateUser(binding, user)

                } else {
                    val sweetAlertDialog =
                        SweetAlertDialog(this@SignupActivity, SweetAlertDialog.ERROR_TYPE)
                    sweetAlertDialog.contentText = "Please fill in all the fields!"
                    sweetAlertDialog.setCancelable(true)
                    sweetAlertDialog.show()
                }


            }


            btnToLogIn.setOnClickListener {
                startActivity(Intent(this@SignupActivity, LoginActivity::class.java))
                finish()
            }

        }

    }

    private fun updateUser(binding: ActivitySignupBinding, user: User) {
        val sweetAlertDialog =
            SweetAlertDialog(this@SignupActivity, SweetAlertDialog.PROGRESS_TYPE)
        sweetAlertDialog.contentText = "Creating your account"
        sweetAlertDialog.setCancelable(false)
        sweetAlertDialog.show()

        val login: HashMap<String, Any> = HashMap()
        login["login_id"] = user.userId
        login["login_username"] = binding.edtUsername.text.toString()
        login["login_admin"] = binding.edtAdmn.text.toString()
        login["login_phone"] = binding.edtPhone.text.toString()
        login["login_email"] = binding.edtMail.text.toString()
        login["login_password"] =
            hashString("SHA-512", binding.edtPassword.text.toString())
        login["login_rank"] = this.binding.checkAdmin.isChecked

        val originalChildName = binding.edtAdmn.text.toString()
        val encodeChildName = encodeChildName(originalChildName)

        FirebaseDatabase.getInstance().reference.child("Login")
            .child(encodeChildName).setValue(login).addOnSuccessListener {
                Paper.book().write("userAdmin", binding.edtAdmn.text.toString())
                Toast.makeText(
                    this@SignupActivity, "Account created", Toast.LENGTH_SHORT
                ).show()

                setDp(encodeChildName, sweetAlertDialog)
            }
    }


    private fun setDp(encodeChildName: String, sweetAlertDialog: SweetAlertDialog) {
        Log.d("CREATE_UPDATES: ", "photo started to upload")

        val storageReference: StorageReference =
            FirebaseStorage.getInstance().reference.child("Images")
                .child(encodeChildName)
                .child("pfp")
        try {
            storageReference.putFile(image_uri).addOnSuccessListener {
                storageReference.downloadUrl.addOnSuccessListener { uri ->

                    FirebaseDatabase.getInstance().reference.child("Login")
                        .child(encodeChildName)
                        .child("userDp")
                        .setValue(uri.toString())
                        .addOnSuccessListener {
                            sweetAlertDialog.dismissWithAnimation()
                            val activityClass =
                                if (binding.checkAdmin.isChecked) LecActivity::class.java else StudentActivity::class.java
                            startActivity(Intent(this@SignupActivity, activityClass))
                            finish()
                        }

                }
            }
        } catch (e: Exception) {
            Log.d("CREATE_UPDATES: ", "failed to push file to firebase because " + e.message)
            sweetAlertDialog.dismiss()
            val sweetAlertDialog =
                SweetAlertDialog(this@SignupActivity, SweetAlertDialog.ERROR_TYPE)
            sweetAlertDialog.contentText = "Failed:" + e.message
            sweetAlertDialog.setCancelable(true)
            sweetAlertDialog.show()
        }

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

    private fun decodeChildName(encodedChildName: String): String {
        return encodedChildName.replace("-", "/")
    }

}