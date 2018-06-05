Core Collections
============
A set of extensions to work with collections

CollectionsExtensions
---------------------
Sample usage:
```kotlin
val someCollection = mutableListOf<String>()
val someOtherCollection = listOf("String", "String2")

if(someCollection.isNotNullOrEmpty()){
    someCollection.replace(someOtherCollection)
}
```
Sample usage of working with ```Predicate```:
```kotlin
 val favoritesPredicates = arrayListOf<Predicate<UserContact>>(
            Predicate { it.isStarred },
            Predicate { !it.isStarred }
    )
 val result: SparseArray<List<UserContact>> = originalCollection.split(favoritesPredicates)
```

For full list of functions, please look to the source code, or view documentation