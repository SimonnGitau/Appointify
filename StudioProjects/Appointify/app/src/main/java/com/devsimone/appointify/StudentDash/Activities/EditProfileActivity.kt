package com.devsimone.appointify.StudentDash.Activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bumptech.glide.Glide
import com.devsimone.appointify.Models.User
import com.devsimone.appointify.databinding.ActivityEditProfileBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import io.paperdb.Paper

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var image_uri: Uri
    private lateinit var pickSingleMediaLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {

            pickSingleMediaLauncher =
                registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                    if (it.resultCode != Activity.RESULT_OK) {
                        Toast.makeText(
                            this@EditProfileActivity,
                            "Failed picking media.",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    } else {
                        image_uri = it.data?.data!!
                        Glide.with(this@EditProfileActivity).load(image_uri).into(addAppDp)
                    }
                }

            addAppDp.setOnClickListener {
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

            val originalChildName1 = Paper.book().read<String>("userAdmin")
            val childName1 = originalChildName1?.let { encodeChildName(it) }

            if (childName1 != null) {

                FirebaseDatabase.getInstance().reference.child("Login")
                    .child(childName1)
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            // Handle the data snapshot here
                            if (snapshot.exists()) {
                                for (childSnapshot in snapshot.children) {

                                    val user = User()
                                    user.userName =
                                        snapshot.child("login_username").value.toString()
                                    user.userDp = snapshot.child("userDp").value.toString()
                                    user.userAdmn = snapshot.child("login_admin").value.toString()
                                    user.userMail = snapshot.child("login_email").value.toString()
                                    user.userPhone = snapshot.child("login_phone").value.toString()

                                    Glide.with(this@EditProfileActivity).load(user.userDp)
                                        .into(addAppDp)
                                    addAppName.hint = user.userName
                                    edtEventName.hint = user.userMail
                                    edtEventDescription.hint = user.userPhone

                                }
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(
                                this@EditProfileActivity,
                                "Error: " + error.message,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    })

            }

            btnSave.setOnClickListener {
                val sweetAlertDialog =
                    SweetAlertDialog(this@EditProfileActivity, SweetAlertDialog.WARNING_TYPE)
                sweetAlertDialog.contentText = "Save changes?"
                sweetAlertDialog.setCancelable(true)
                sweetAlertDialog.show()
                sweetAlertDialog.setConfirmClickListener {

                    updatePfp(childName1)

                }

            }
        }

    }

    private fun updatePfp(childName1: String?) {

        Log.d("CREATE_UPDATES: ", "photo started to upload")

        val storageReference: StorageReference? =
            childName1?.let {
                FirebaseStorage.getInstance().reference.child("Images")
                    .child(it)
                    .child("pfp")
            }
        try {
            storageReference?.putFile(image_uri)?.addOnSuccessListener {
                storageReference.downloadUrl.addOnSuccessListener { uri ->

                    val ref = childName1.let { it1 ->
                        FirebaseDatabase.getInstance().reference.child("Login").child(
                            it1
                        )
                    }

                    val updates = HashMap<String, Any>()
                    updates["login_username"] = binding.addAppName.text.toString().trim()
                    updates["login_email"] = binding.edtEventName.text.toString().trim()
                    updates["login_phone"] = binding.edtEventDescription.text.toString().trim()
                    updates["userDp"] = uri.toString()

                    ref.updateChildren(updates).addOnSuccessListener {

                        val sweetAlertDialog =
                            SweetAlertDialog(
                                this@EditProfileActivity,
                                SweetAlertDialog.SUCCESS_TYPE
                            )
                        sweetAlertDialog.contentText = "Profile updated successfully!"
                        sweetAlertDialog.setCancelable(true)
                        sweetAlertDialog.show()

                    }.addOnFailureListener {

                        val sweetAlertDialog =
                            SweetAlertDialog(
                                this@EditProfileActivity,
                                SweetAlertDialog.SUCCESS_TYPE
                            )
                        sweetAlertDialog.contentText = "Error making changes: " + it.message
                        sweetAlertDialog.setCancelable(true)
                        sweetAlertDialog.show()

                    }

                }
            }
        } catch (e: Exception) {
            Log.d("CREATE_UPDATES: ", "failed to push file to firebase because " + e.message)

            val sweetAlertDialog =
                SweetAlertDialog(this@EditProfileActivity, SweetAlertDialog.ERROR_TYPE)
            sweetAlertDialog.contentText = "Failed:" + e.message
            sweetAlertDialog.setCancelable(true)
            sweetAlertDialog.show()
        }

    }

    private fun encodeChildName(childName: String): String {
        return childName.replace("/", "-")
    }

}