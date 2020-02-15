package com.kiwimob.firestore.coroutines

import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.callbackFlow

@ExperimentalCoroutinesApi
fun Query.snapshotAsFlow() = callbackFlow<QuerySnapshot> {
    val registration = addSnapshotListener { querySnapshot, exception ->
        exception?.let {
            close(it)
        }
        if (querySnapshot == null) {
            close()
        }
        querySnapshot?.let {
            sendBlocking(it)
        }
    }

    awaitClose {
        registration.remove()
    }
}