# Firestore Coroutines

## What's this?

Set of Kotlin extension functions that consume Firestore Collections and Documents asynchronously making use of Coroutines.

## Download

Gradle:

```groovy

dependencies {

    implementation 'com.kiwimob.firestore.coroutines:firestore-coroutines:0.0.1'
}
```

## How to use

```java

launch(UI) {
    val result = FirebaseFirestore.getInstance().collection("users").await(User::class.java)

    for (document in result) {
        Log.d("MainActivity", document.name + " => " + document.email)
    }
}
```

## Parsing results

 When passing the return class type to the await function the default **DocumentSnapshot.toObject()** is called in order to parse the final result.
 <br><br>
 In case of the default document parser not working for your use case there is also the option to pass a parser function as an argument to handle the mapping behaviour.

 ```java
launch(UI) {
    val users = FirebaseFirestore.getInstance().collection("users").await({parseUser(it)})
}

private fun parseUser(documentSnapshot: DocumentSnapshot) : User {
    return User(name = documentSnapshot.getString("name"), email = documentSnapshot.getString("email"))
}
 ```

##### It can also be useful when parsing References from other Documents as Firestore does not fetch them automatically.

## Collection Functions

Function | Return Type
------------ | -------------
await (clazz: Class\<T>) | List\<T>
await (parser: (documentSnapshot: DocumentSnapshot) -> T) | List\<T>
addAwait (value: Any) | DocumentReference
addAwait (value: Map<String, Any>) | DocumentReference

## Document Functions

Function | Return Type
------------ | -------------
await (clazz: Class\<T>) | T
await (parser: (documentSnapshot: DocumentSnapshot) -> T) | T
deleteAwait() | -
updateAwait (var1: Map<String, Any>) | -
updateAwait (var1: FieldPath, var2: Any, var3: List<Any>) | -
updateAwait (var1: String, var2 : Any, var3: List<Any>) | -
setAwait (var1: Any) | -
setAwait (var1: Map<String, Any>) | -
setAwait (var1: Any, var2 : SetOptions) | -
setAwait(var1: Map<String, Any>, var2: SetOptions) | -

## Query Functions
Function | Return Type
------------ | -------------
await (clazz: Class\<T>) | List\<T>
await (parser: (documentSnapshot: DocumentSnapshot) -> T) | List\<T>
awaitSingle (clazz: Class\<T>) | T
awaitSingle (parser: (documentSnapshot: DocumentSnapshot) -> T) | T


## Downsides

Coroutines were not built to provide streams of events, therefor we cannot take advantage of Firestore's realtime database updates. For that matter I recommend checking my [Firestore LiveData](https://github.com/brotoo25/Firestore-LiveData) project that makes use of Android Architecture Components to provide a lifecycle aware stream of data coming from your collections or documents.
## Next steps

 * Create sample app
 * Documentation
