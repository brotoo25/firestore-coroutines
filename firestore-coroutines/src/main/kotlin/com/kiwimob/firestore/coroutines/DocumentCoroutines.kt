package com.kiwimob.firestore.coroutines

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

suspend fun <T : Any> DocumentReference.await(clazz: Class<T>): T {
    return await { documentSnapshot -> documentSnapshot.toObject(clazz)!! }
}

suspend fun <T : Any> DocumentReference.await(parser: (documentSnapshot: DocumentSnapshot) -> T): T {
    return suspendCancellableCoroutine { continuation ->
        get().addOnCompleteListener {

            if (it.isSuccessful && it.result != null) {
                continuation.resume(parser(it.result!!))
            } else {
                continuation.resumeWithException(it.exception ?: IllegalStateException())
            }
        }
    }
}

suspend fun DocumentReference.await(): DocumentSnapshot {
    return suspendCancellableCoroutine { continuation ->
        get().addOnCompleteListener {
            if (it.isSuccessful && it.result != null) {
                continuation.resume(it.result!!)
            } else {
                continuation.resumeWithException(it.exception ?: IllegalStateException())
            }
        }
    }
}

suspend fun DocumentReference.deleteAwait() {
    return suspendCancellableCoroutine { continuation ->
        delete().addOnCompleteListener {
            if (it.isSuccessful) {
                continuation.resume(Unit)
            } else {
                continuation.resumeWithException(it.exception ?: IllegalStateException())
            }
        }
    }
}

suspend fun DocumentReference.updateAwait(var1: Map<String, Any>) {
    return suspendCancellableCoroutine { continuation ->
        update(var1).addOnCompleteListener {
            if (it.isSuccessful) {
                continuation.resume(Unit)
            } else {
                continuation.resumeWithException(it.exception ?: IllegalStateException())
            }
        }
    }
}

suspend fun DocumentReference.updateAwait(var1: FieldPath, var2: Any, var3: List<Any>) {
    return suspendCancellableCoroutine { continuation ->
        update(var1, var2, var3).addOnCompleteListener {
            if (it.isSuccessful) {
                continuation.resume(Unit)
            } else {
                continuation.resumeWithException(it.exception ?: IllegalStateException())
            }
        }
    }
}

suspend fun DocumentReference.updateAwait(var1: String, var2 : Any, var3: List<Any>) {
    return suspendCancellableCoroutine { continuation ->
        update(var1, var2, var3).addOnCompleteListener {
            if (it.isSuccessful) {
                continuation.resume(Unit)
            } else {
                continuation.resumeWithException(it.exception ?: IllegalStateException())
            }
        }
    }
}

suspend fun DocumentReference.setAwait(var1: Any) {
    return suspendCancellableCoroutine { continuation ->
        set(var1).addOnCompleteListener {
            if (it.isSuccessful) {
                continuation.resume(Unit)
            } else {
                continuation.resumeWithException(it.exception ?: IllegalStateException())
            }
        }
    }
}

suspend fun DocumentReference.setAwait(var1: Any, var2 : SetOptions) {
    return suspendCancellableCoroutine { continuation ->
        set(var1, var2).addOnCompleteListener {
            if (it.isSuccessful) {
                continuation.resume(Unit)
            } else {
                continuation.resumeWithException(it.exception ?: IllegalStateException())
            }
        }
    }
}

suspend fun DocumentReference.setAwait(data: Map<String, Any>) {
    return suspendCancellableCoroutine { continuation ->
        set(data).addOnCompleteListener {
            if (it.isSuccessful) {
                continuation.resume(Unit)
            } else {
                continuation.resumeWithException(it.exception!!)
            }
        }
    }
}

suspend fun DocumentReference.setAwait(data: Map<String, Any>, options: SetOptions) {
    return suspendCancellableCoroutine { continuation ->
        set(data, options).addOnCompleteListener {
            if (it.isSuccessful) {
                continuation.resume(Unit)
            } else {
                continuation.resumeWithException(it.exception!!)
            }
        }
    }
}