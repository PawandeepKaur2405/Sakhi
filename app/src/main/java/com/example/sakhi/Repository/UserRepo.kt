package com.example.sakhi.Repository

import android.util.Log
import androidx.navigation.fragment.findNavController
import com.example.sakhi.Models.User
import com.example.sakhi.R
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*
import com.google.firebase.firestore.model.Document
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.handleCoroutineException
import kotlinx.coroutines.launch

class UserRepo {
    val db = FirebaseFirestore.getInstance()
    val userCollection = db.collection("student")

    fun addUser(user: User?) {
        user?.let {
            GlobalScope.launch(Dispatchers.IO) {
                userCollection.document(user.uid).set(it)
            }
        }
    }

    fun updateName(user: User?) {
        user?.let {
            GlobalScope.launch(Dispatchers.IO) {
                userCollection.document(user.uid).update(mapOf("name" to user.name))
            }
        }
    }

    fun updatePhone(user: User?) {
        user?.let {
            GlobalScope.launch(Dispatchers.IO) {
                userCollection.document(user.uid).update(mapOf("phone" to user.phone))
            }
        }
    }

    fun updateGuardian(userId: String, guardId: String) {
        userId.let {
            GlobalScope.launch(Dispatchers.IO) {
                userCollection.document(userId).update("guardians",FieldValue.arrayUnion(guardId))
            }
        }
    }

    fun updateSuperGuardian(userId: String, sGuardian: String) {
        userId.let {
            GlobalScope.launch(Dispatchers.IO) {
                userCollection.document(userId).update(mapOf("superGuardian" to sGuardian))
            }
        }
    }

    fun getUserById(uid: String): Task<DocumentSnapshot> {
        return userCollection.document(uid).get()
    }
}
