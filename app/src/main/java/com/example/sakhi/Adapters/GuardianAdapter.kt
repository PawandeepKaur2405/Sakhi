package com.example.sakhi.Adapters

import android.app.PendingIntent.getActivity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sakhi.Fragments.UserDetails
import com.example.sakhi.R
import com.example.sakhi.Repository.UserRepo
import com.google.android.gms.tasks.Task
import com.google.android.material.internal.ContextUtils.getActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.auth.User
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class GuardianAdapter(val guardians: List<String>) : RecyclerView.Adapter<GuardianAdapter.MyViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val inflator: LayoutInflater = LayoutInflater.from(parent.context)
            val view = inflator.inflate(R.layout.guardian_item, parent, false)
            return MyViewHolder(view)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            val db = FirebaseFirestore.getInstance()
            val userCollection = db.collection("student")

            val guardianQuery: Query = userCollection.whereEqualTo("uniqueId", guardians[position])
            guardianQuery.get().addOnCompleteListener {

                if(it.isSuccessful) {
                    for(document in it.result) {
                        if(document.exists()) {
                            holder.guardName.text = document.get("name").toString()
                            holder.guardPhone.text = document.get("phone").toString()
                        }
                    }
                }
            }

            holder.deleteGuard.setOnClickListener {
                var guard = guardians[position]
                val auth = FirebaseAuth.getInstance()
                val userRepo: UserRepo = UserRepo()
                userRepo.deleteGuardian(auth.currentUser!!.uid, guard)

                it.findNavController().navigate(R.id.action_userDetails_self)
            }
        }

        override fun getItemCount(): Int {
            return guardians.size
        }

        class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var guardName = itemView.findViewById<TextView>(R.id.GNameTextView)
            var guardPhone = itemView.findViewById<TextView>(R.id.GPhoneTextView)
            var deleteGuard = itemView.findViewById<ImageView>(R.id.gRemoveButton)
        }

    }
