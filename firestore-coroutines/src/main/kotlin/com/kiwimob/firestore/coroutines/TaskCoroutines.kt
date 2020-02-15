package com.kiwimob.firestore.coroutines

import com.google.android.gms.tasks.Task
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

suspend inline fun <reified T : Any> Task<T>.await(): T {
    return suspendCancellableCoroutine { continuation ->
        addOnCompleteListener {
            if (it.isSuccessful) {
                if (it.result != null) {
                    continuation.resume(it.result!!)
                } else {
                    continuation.resumeWithException(EmptyStackException())
                }
            } else {
                continuation.resumeWithException(it.exception ?: IllegalStateException())
            }
        }
        addOnSuccessListener { continuation.resume(it) }
        addOnFailureListener { continuation.resumeWithException(it) }
        addOnCanceledListener { continuation.cancel() }
    }
}
