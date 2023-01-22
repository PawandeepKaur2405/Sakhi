package com.example.sakhi.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.sakhi.Models.User
import com.example.sakhi.R
import com.example.sakhi.Repository.UserRepo
import com.example.sakhi.databinding.FragmentGuardianDetailsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class GuardianDetailsFragment : Fragment() {

    private var _binding: FragmentGuardianDetailsBinding?=null
    private val binding get()= _binding!!
    private val TAG = "DEBUG"
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGuardianDetailsBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        binding.ContinueButton.setOnClickListener {
            val guardID = binding.IDEditText.text.toString()
            val isSuperGuard = binding.SuperGuardianRButton.isChecked

            if(guardID.isNotEmpty()) {
                updateUI(guardID, isSuperGuard)
            }
            else {
                Toast.makeText(view?.context, "Enter Guardian ID", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateUI(guardID: String, isSuperGuard: Boolean) {

        //checking if guardId is correct
        val db = FirebaseFirestore.getInstance()
        val userCollection = db.collection("student")

        val guardianQuery: Query = userCollection.whereEqualTo("uniqueId", guardID)
        guardianQuery.get().addOnCompleteListener {
            Log.d(TAG, "Checking if guardian exists")

            if(it.isSuccessful) {
                for(document in it.result) {
                    if(document.exists()) {
                        updateFirestore(guardID, isSuperGuard)
                        findNavController().navigate(R.id.action_guardianDetailsFragment_to_userDetails)
                        break;
                    }
                }
            }
        }
    }

    private fun updateFirestore(guardID: String, isSuperGuard: Boolean) {
        val userRepo: UserRepo = UserRepo()
        val user: User
        if(isSuperGuard) {
            auth.currentUser?.let { userRepo.updateSuperGuardian(it.uid, guardID) }
        }
        auth.currentUser?.let { userRepo.updateGuardian(it.uid, guardID) }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}