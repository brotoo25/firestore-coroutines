package com.kiwimob.firestore.coroutines.sample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.kiwimob.firestore.coroutine.await
import com.kiwimob.firestorecoroutines.R
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        launch(UI) {
            val result = FirebaseFirestore
                    .getInstance()
                    .collection("users")
                    .await(User::class.java)

            for (document in result) {
                Log.d("MainActivity", document.name + " => " + document.email)
            }

            val users = FirebaseFirestore.getInstance().collection("users").await({
                async { parseUser(it) }
            })
        }
    }

    private suspend fun parseUser(documentSnapshot: DocumentSnapshot) : User {
        FirebaseFirestore.getInstance().document("").await(User::class.java)
        return User(name = documentSnapshot.getString("name"), email = documentSnapshot.getString("email"))
    }
}