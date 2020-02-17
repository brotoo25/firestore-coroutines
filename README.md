# Firestore Coroutines

## What's this?

Set of Kotlin extension functions that consume Firestore Collections and Documents asynchronously making use of Coroutines.

## Download

Gradle:

```groovy
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}

dependencies {

    implementation 'com.github.brotoo25:firestore-coroutines:1.0.0'
}
```

## How to use

```java

GlobalScope.launch() {
    val users =
        FirebaseFirestore
            .getInstance()
            .collection("users")
            .get()
            .await()
            .toObjects(User::class.java)

    for (document in users) {
        Log.d("MainActivity", document.name + " => " + document.email)
    }
}
```

## Observing changes

The new Flow Api is used to get realtime updates from Firestore Collections/Documents:

```java

GlobalScope.launch() {
    FirebaseFirestore
        .getInstance()
        .collection("users")
        .snapshotAsFlow()
        .collect {
            val result = it.toObjects(User::class.java)
            Log.d("MainActivity", "Current users: $result.size")
        }
}
```

## Next steps

 * Create sample app
 * Documentation
