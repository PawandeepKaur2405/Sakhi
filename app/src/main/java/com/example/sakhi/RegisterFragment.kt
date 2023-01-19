package com.example.sakhi

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.sakhi.databinding.FragmentLoginBinding
import com.example.sakhi.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val TAG: String = "Message"
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()

        binding.SignInTextView.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        binding.RegisterButton.setOnClickListener {
            val email = binding.ResgiterEmailEditText.text.toString()
            val pass = binding.RegisterPasswordEditText.text.toString()
            val confirmPass = binding.RePasswordEditText.text.toString()

            if(email.isNotEmpty() && pass.isNotEmpty() && confirmPass.isNotEmpty()) {
                if(pass == confirmPass) {
                    auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener{
                        if(it.isSuccessful) {
                            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                        }
                        else {
                            Toast.makeText(view?.context, it.exception.toString(), Toast.LENGTH_SHORT).show()
                            Log.w(TAG, "signInWithCredential:failure", it.exception)
                        }
                    }
                }
                else {
                    Toast.makeText(view?.context, "Password is not matching", Toast.LENGTH_SHORT).show()
//                    Log.w(TAG, "signInWithCredential:failure", Exception)
                }
            }
            else {
                Toast.makeText(view?.context,"Please enter all details",Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
}