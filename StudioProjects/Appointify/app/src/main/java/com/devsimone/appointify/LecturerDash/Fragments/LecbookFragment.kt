package com.devsimone.appointify.LecturerDash.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.devsimone.appointify.LecturerDash.Adapter.BooksAdapter
import com.devsimone.appointify.Models.Appointment
import com.devsimone.appointify.databinding.FragmentLecbookBinding
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import io.paperdb.Paper

class LecbookFragment : Fragment() {

    private lateinit var binding: FragmentLecbookBinding
    private lateinit var bookList: MutableList<Appointment>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLecbookBinding.inflate(layoutInflater, container, false)

        binding.apply {

            updateList()

            bookRef.setOnRefreshListener {
                updateList()
                bookList.clear()
                bookRef.isRefreshing = false
            }

        }

        return binding.root
    }

    private fun updateList() {
        bookList = mutableListOf()

        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                if (snapshot.exists()) {

                    val childEventListener1 = object : ChildEventListener {
                        override fun onChildAdded(
                            snapshot1: DataSnapshot,
                            previousChildName: String?
                        ) {
                            if (snapshot1.exists()) {

                                val app = Appointment()
                                app.appId = snapshot1.child("appId").value.toString()
                                app.appOwnId = snapshot1.child("appOwnId").value.toString()
                                app.appLecId = snapshot1.child("appLecId").value.toString()
                                app.appTopic = snapshot1.child("appTopic").value.toString()
                                app.appDate = snapshot1.child("appDate").value.toString()
                                app.appTime = snapshot1.child("appTime").value.toString()
                                app.appDes = snapshot1.child("appDes").value.toString()
                                app.appLink = snapshot1.child("appLink").value.toString()
                                app.appStatus = false

                                if (app.appLecId == Paper.book().read<String>("userAdmin")) {
                                    bookList.add(app)
                                    binding.textView3.visibility = View.GONE
                                    binding.animationView.visibility = View.GONE
                                    binding.bookRef.visibility = View.VISIBLE
                                    binding.rcBook.visibility = View.VISIBLE
                                    binding.rcBook.layoutManager = LinearLayoutManager(context)
                                    binding.rcBook.adapter =
                                        context?.let { BooksAdapter(bookList, context = it) }
                                }

                            }

                        }

                        override fun onChildChanged(
                            snapshot: DataSnapshot,
                            previousChildName: String?
                        ) {
                        }

                        override fun onChildRemoved(snapshot: DataSnapshot) {
                        }

                        override fun onChildMoved(
                            snapshot: DataSnapshot,
                            previousChildName: String?
                        ) {
                        }

                        override fun onCancelled(error: DatabaseError) {
                        }
                    }

                    val originalChildName = Paper.book().read<String>("userAdmin")
                    val childName = encodeChildName(originalChildName.toString())

                    FirebaseDatabase.getInstance().reference.child("Login")
                        .child(childName)
                        .child("Appointments")
                        .orderByChild("appStatus")
                        .equalTo(false.toString())
                        .addChildEventListener(childEventListener1)

                } else {
                    // Data does not exist
                    Toast.makeText(context, "No data found!", Toast.LENGTH_LONG)
                        .show()
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onCancelled(error: DatabaseError) {
            }

        }
        FirebaseDatabase.getInstance().reference.child("Login")
            .orderByChild("login_rank")
            .equalTo(true)
            .addChildEventListener(childEventListener)

    }

    private fun encodeChildName(childName: String): String {
        return childName.replace("/", "-")
    }

}