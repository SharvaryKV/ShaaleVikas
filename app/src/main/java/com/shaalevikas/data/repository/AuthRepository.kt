package com.shaalevikas.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AuthRepository {

    private val auth = FirebaseAuth.getInstance()

    private val firestore =
        FirebaseFirestore.getInstance()

    fun login(
        email: String,
        password: String,
        onSuccess: (String) -> Unit,
        onFailure: (String) -> Unit
    ) {

        auth.signInWithEmailAndPassword(
            email,
            password
        ).addOnSuccessListener {

            val uid = auth.currentUser?.uid

            if (uid == null) {

                onFailure("User ID not found")

                return@addOnSuccessListener
            }

            firestore.collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener { document ->

                    val role =
                        document.getString("role")

                    if (role != null) {

                        onSuccess(role)

                    } else {

                        onFailure(
                            "User role not found"
                        )
                    }
                }
                .addOnFailureListener {

                    onFailure(
                        it.message
                            ?: "Failed to fetch role"
                    )
                }

        }.addOnFailureListener {

            onFailure(
                it.message
                    ?: "Login Failed"
            )
        }
    }
}