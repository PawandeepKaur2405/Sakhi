package com.example.sakhi.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.size
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sakhi.Adapters.GuardianAdapter
import com.example.sakhi.Models.User
import com.example.sakhi.R
import com.example.sakhi.Repository.UserRepo
import com.example.sakhi.databinding.FragmentUserDetailsBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class UserDetails : Fragment() {

    private var _binding: FragmentUserDetailsBinding?=null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserDetailsBinding.inflate(inflater,container,false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()

        val userRepo: UserRepo = UserRepo()
        var _guards : User

        GlobalScope.launch {
            userRepo.getUserById(auth.currentUser!!.uid).addOnSuccessListener {
                _guards = it.toObject(User::class.java)!!
                val guards: List<String> = _guards.guardians

                binding.recyclerview.adapter = GuardianAdapter(guards)
                binding.recyclerview.layoutManager = LinearLayoutManager(view?.context)
            }
        }

        binding.addGuardianButton.setOnClickListener {
            findNavController().navigate(R.id.action_userDetails_to_guardianDetailsFragment)
        }

        binding.ContinueButton.setOnClickListener {
            val name = binding.UserNameEditText.text.toString()
            val phone = binding.UserPhoneEditText.text.toString()
            val guardians = binding.recyclerview.size.toString()

            if(name.isNotEmpty() && phone.isNotEmpty() && guardians.isNotEmpty()) {
                updateUI(name, phone)
            }
            else {
                Toast.makeText(view?.context, "Enter all details and atleast 1 guardian", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateUI(name: String, phone: String) {

        val userRepo: UserRepo = UserRepo()
        val user = auth.currentUser?.let { User(uid = it.uid, name = name, phone = phone, uniqueId = "G-"+phone+"-sakhi") }
        userRepo.updateName(user)
        userRepo.updatePhone(user)

        findNavController().navigate(R.id.action_userDetails_to_homeFragment)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}