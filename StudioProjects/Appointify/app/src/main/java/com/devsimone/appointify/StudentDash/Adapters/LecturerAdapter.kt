package com.devsimone.appointify.StudentDash.Adapters

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.devsimone.appointify.LecturerDash.Activity.ProfileActivity
import com.devsimone.appointify.Models.User
import com.devsimone.appointify.StudentDash.Activities.AddAppointmentActivity
import com.devsimone.appointify.databinding.AvailabeLecBinding

class LecturerAdapter(
    private var userList: MutableList<User>,
    private val context: Context
) :
    RecyclerView.Adapter<LecturerAdapter.LecViewHolder>() {

    class LecViewHolder(availableLecBinding: AvailabeLecBinding) :
        RecyclerView.ViewHolder(availableLecBinding.root) {
        val binding = availableLecBinding
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = LecViewHolder(
        AvailabeLecBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun getItemCount() = userList.size

    override fun onBindViewHolder(holder: LecViewHolder, position: Int) {

        val user = userList[position]

        holder.binding.apply {

            lecName.text = user.userName
            Glide.with(context).load(user.userDp).into(lecPfp)

            root.setOnClickListener {

                val intent = Intent(context, AddAppointmentActivity::class.java)
                val bundle = Bundle()
                bundle.putString("lecAdmin", user.userAdmn)
                bundle.putString("lecDp", user.userDp)
                bundle.putString("lecName", user.userName)
                intent.putExtras(bundle)
                root.context.startActivity(intent)

            }

            root.setOnLongClickListener {

                val intent = Intent(context, ProfileActivity::class.java)
                val bundle = Bundle()
                bundle.putString("profAdmin", user.userAdmn)
                intent.putExtras(bundle)
                root.context.startActivity(intent)

                true


            }

        }
    }
}