package com.example.sakhi.Fragments

import android.content.Intent
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
import com.example.sakhi.databinding.FragmentLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var userRepo: UserRepo

    private val RC_SIGN_IN: Int = 123
    private val TAG: String = "Message"

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
        userRepo = UserRepo()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this.requireContext(), gso)
        googleSignInClient.revokeAccess()

        binding.loginButton.setOnClickListener {
            loginUsingCredentials()
        }

        binding.GoogleSignInButton.setOnClickListener {
            loginUsingGoogle()
        }

        binding.createAccountTextView.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    private fun loginUsingGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithGoogle:success")
                    val user = auth.currentUser
                    val isNew: Boolean = task.getResult()?.additionalUserInfo?.isNewUser == true
                    if(isNew) {
                        val user = User(auth.currentUser!!.uid)
                        userRepo.addUser(user)
                    }
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithGoogle:failure", task.exception)
                    Toast.makeText(view?.context, "Sign in with Google failed!", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun loginUsingCredentials() {
        val email = binding.EmailEditText.text.toString()
        val pass = binding.PasswordEditText.text.toString()

        if(email.isNotEmpty() && pass.isNotEmpty()) {
            auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                if(it.isSuccessful) {
                    Toast.makeText(view?.context, "Sign in successful!", Toast.LENGTH_SHORT).show()
                    updateUI(auth.currentUser)
//                    findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
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

    override fun onStart() {
        super.onStart()

        val currUser = auth.currentUser
        if(currUser!=null) {
            updateUI(currUser)
        }
    }

    private fun updateUI(user: FirebaseUser?) {

            if(user != null) {
                userRepo.getUserById(user!!.uid).addOnCompleteListener {
                    val temp = it.result.getString("name")

                    if(temp!="" && temp!=null) {
                        findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                    }
                    else {
                        findNavController().navigate(R.id.action_loginFragment_to_userDetails)
                    }
                }
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}