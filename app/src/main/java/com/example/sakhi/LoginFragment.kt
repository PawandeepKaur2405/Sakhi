package com.example.sakhi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.sakhi.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()

        binding.loginButton.setOnClickListener {
            val email = binding.EmailEditText.text.toString()
            val pass = binding.PasswordEditText.text.toString()

            if(email.isNotEmpty() && pass.isNotEmpty()) {
                auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                    if(it.isSuccessful) {
                        Toast.makeText(view?.context, "Sign in successful!", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        Toast.makeText(view?.context, "Sign in Failed!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else {
                Toast.makeText(view?.context, "Please fill all details", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()

        if(auth.currentUser!=null) {
            Toast.makeText(view?.context, "Already a user!", Toast.LENGTH_SHORT).show()
        }
    }
}