package com.kiwimob.firestore.coroutines

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.experimental.NonCancellable
import kotlinx.coroutines.experimental.suspendCancellableCoroutine

suspend fun <T : Any> Query.await(clazz: Class<T>): List<T> {
    return await { documentSnapshot -> documentSnapshot.toObject(clazz) }
}

suspend fun <T : Any> Query.awaitSingle(clazz: Class<T>): T {
    return awaitSingle { documentSnapshot -> documentSnapshot.toObject(clazz) }
}

suspend fun Query.await(): QuerySnapshot {
    return suspendCancellableCoroutine { continuation ->
        get().addOnCompleteListener {
            if (it.isSuccessful) {
                continuation.resume(it.result)
            } else {
                continuation.resumeWithException(it.exception!!)
            }
        }

        continuation.invokeOnCompletion {
            if (continuation.isCancelled)
                try {
                    NonCancellable.cancel()
                } catch (ex: Throwable) {
                    //Ignore cancel exception
                }
        }
    }
}

suspend fun <T : Any> Query.await(parser: (documentSnapshot: DocumentSnapshot) -> T): List<T> {
    return suspendCancellableCoroutine { continuation ->
        get().addOnCompleteListener {
            if (it.isSuccessful) {
                val list = arrayListOf<T>()
                it.result.forEach { list.add(parser.invoke(it)) }

                continuation.resume(list)
            } else {
                continuation.resumeWithException(it.exception!!)
            }
        }

        continuation.invokeOnCompletion {
            if (continuation.isCancelled)
                try {
                    NonCancellable.cancel()
                } catch (ex: Throwable) {
                    //Ignore cancel exception
                }
        }
    }
}

suspend fun <T : Any> Query.awaitSingle(parser: (documentSnapshot: DocumentSnapshot) -> T): T {
    return suspendCancellableCoroutine { continuation ->
        get().addOnCompleteListener {
            if (it.isSuccessful) {
                continuation.resume(parser.invoke(it.result.documents[0]))
            } else {
                continuation.resumeWithException(it.exception!!)
            }
        }

        continuation.invokeOnCompletion {
            if (continuation.isCancelled)
                try {
                    NonCancellable.cancel()
                } catch (ex: Throwable) {
                    //Ignore cancel exception
                }
        }
    }
}