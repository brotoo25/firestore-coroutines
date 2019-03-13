package com.kiwimob.firestore.coroutines

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.sendBlocking
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

suspend fun <T : Any> Query.await(clazz: Class<T>): List<T> {
    return await { documentSnapshot -> documentSnapshot.toObject(clazz) as T }
}

suspend fun <T : Any> Query.awaitSingle(clazz: Class<T>): T {
    return awaitSingle { documentSnapshot -> documentSnapshot.toObject(clazz) as T }
}

suspend fun Query.await(): QuerySnapshot {
    return suspendCoroutine { continuation ->
        get().addOnCompleteListener {
            if (it.isSuccessful && it.result != null) {
                continuation.resume(it.result!!)
            } else {
                continuation.resumeWithException(it.exception ?: IllegalStateException())
            }
        }
    }
}

suspend fun <T : Any> Query.await(parser: (documentSnapshot: DocumentSnapshot) -> T): List<T> {
    return suspendCoroutine { continuation ->
        get().addOnCompleteListener {
            if (it.isSuccessful && it.result != null) {
                val list = it.result!!.map(parser)
                continuation.resume(list)
            } else {
                continuation.resumeWithException(it.exception ?: IllegalStateException())
            }
        }
    }
}

suspend fun <T : Any> Query.awaitSingle(parser: (documentSnapshot: DocumentSnapshot) -> T): T {
    return suspendCoroutine { continuation ->
        get().addOnCompleteListener {
            if (it.isSuccessful && it.result != null) {
                continuation.resume(parser(((it.result) as QuerySnapshot).documents[0]))
            } else {
                continuation.resumeWithException(it.exception ?: IllegalStateException())
            }
        }
    }
}

fun <T : Any> Query.observe(clazz: Class<T>): ReceiveChannel<List<T>> = observe { documentSnapshot ->
    documentSnapshot.toObject(clazz) as T
}

fun <T : Any> Query.observe(parser: (documentSnapshot: DocumentSnapshot) -> T): ReceiveChannel<List<T>> {
    val channel = Channel<List<T>>()

    addSnapshotListener { querySnapshot, exception ->
        exception?.let {
            channel.close(it)
            return@addSnapshotListener
        }
        if (querySnapshot == null) {
            channel.close()
            return@addSnapshotListener
        }

        val list = querySnapshot.map(parser)
        channel.sendBlocking(list)
    }

    return channel
}