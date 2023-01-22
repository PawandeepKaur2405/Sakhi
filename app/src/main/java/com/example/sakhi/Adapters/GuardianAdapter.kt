package com.example.sakhi.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sakhi.R
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

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
        }

        override fun getItemCount(): Int {
            return guardians.size
        }

        class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var guardName = itemView.findViewById<TextView>(R.id.GNameTextView)
            var guardPhone = itemView.findViewById<TextView>(R.id.GPhoneTextView)
        }

    }