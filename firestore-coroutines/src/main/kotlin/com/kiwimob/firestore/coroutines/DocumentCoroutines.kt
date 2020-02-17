package com.kiwimob.firestore.coroutines

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.callbackFlow

@ExperimentalCoroutinesApi
fun DocumentReference.snapshotAsFlow() = callbackFlow<DocumentSnapshot> {
    val registration = addSnapshotListener { documentSnapshot, exception ->
        exception?.let {
            close(it)
        }
        if (documentSnapshot == null) {
            close()
        }
        documentSnapshot?.let {
            sendBlocking(it)
        }
    }

    awaitClose {
        registration.remove()
    }
}