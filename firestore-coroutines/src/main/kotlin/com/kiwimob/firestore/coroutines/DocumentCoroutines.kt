package com.kiwimob.firestore.coroutines

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.SetOptions
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

suspend fun <T : Any> DocumentReference.await(clazz: Class<T>): T {
    return await { documentSnapshot -> documentSnapshot.toObject(clazz)!! }
}

suspend fun <T : Any> DocumentReference.await(parser: (documentSnapshot: DocumentSnapshot) -> T): T {
    return suspendCoroutine { continuation ->
        get().addOnCompleteListener {

            if (it.isSuccessful && it.result != null) {
                continuation.resume(parser(it.result!!))
            } else if (it.exception != null){
                continuation.resumeWithException(it.exception!!)
            } else {
                continuation.resumeWithException(EmptyStackException())
            }
        }
    }
}

suspend fun DocumentReference.await(): DocumentSnapshot {
    return suspendCoroutine { continuation ->
        get().addOnCompleteListener {
            if (it.isSuccessful && it.result != null) {
                continuation.resume(it.result!!)
            } else if (it.exception != null){
                continuation.resumeWithException(it.exception!!)
            } else {
                continuation.resumeWithException(EmptyStackException())
            }
        }
    }
}

suspend fun DocumentReference.deleteAwait() {
    return suspendCoroutine { continuation ->
        delete().addOnCompleteListener {
            if (it.isSuccessful) {
                continuation.resume(Unit)
            } else {
                continuation.resumeWithException(it.exception!!)
            }
        }
    }
}

suspend fun DocumentReference.updateAwait(var1: Map<String, Any>) {
    return suspendCoroutine { continuation ->
        update(var1).addOnCompleteListener {
            if (it.isSuccessful) {
                continuation.resume(Unit)
            } else {
                continuation.resumeWithException(it.exception!!)
            }
        }
    }
}

suspend fun DocumentReference.updateAwait(var1: FieldPath, var2: Any, var3: List<Any>) {
    return suspendCoroutine { continuation ->
        update(var1, var2, var3).addOnCompleteListener {
            if (it.isSuccessful) {
                continuation.resume(Unit)
            } else {
                continuation.resumeWithException(it.exception!!)
            }
        }
    }
}

suspend fun DocumentReference.updateAwait(var1: String, var2 : Any, var3: List<Any>) {
    return suspendCoroutine { continuation ->
        update(var1, var2, var3).addOnCompleteListener {
            if (it.isSuccessful) {
                continuation.resume(Unit)
            } else {
                continuation.resumeWithException(it.exception!!)
            }
        }
    }
}

suspend fun DocumentReference.setAwait(var1: Any) {
    return suspendCoroutine { continuation ->
        set(var1).addOnCompleteListener {
            if (it.isSuccessful) {
                continuation.resume(Unit)
            } else {
                continuation.resumeWithException(it.exception!!)
            }
        }
    }
}

suspend fun DocumentReference.setAwait(var1: Any, var2 : SetOptions) {
    return suspendCoroutine { continuation ->
        set(var1, var2).addOnCompleteListener {
            if (it.isSuccessful) {
                continuation.resume(Unit)
            } else {
                continuation.resumeWithException(it.exception!!)
            }
        }
    }
}