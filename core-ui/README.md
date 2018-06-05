Core UI
============
This module contains different extensions and utilities to improve working with UI. 

Animation
---------
* AnimatorExtensions (originally based on [Ktx Animator](https://github.com/android/android-ktx/blob/master/src/main/java/androidx/core/animation/Animator.kt))
* ViewAnimationsExtensions, provides extension functions to create easy animations.
 ```kotlin
 imageView reavealTo otherImageView
```
* ViewPropertyAnimator, the same as ```AnimatorExtensions``` but for ```ViewPropertyAnimator```.
 ```kotlin
 imageView.animate().alpha(1f).doOnEnd{}
```

Decor
-----
* DividerItemDecoration

DividerItemDecoration is a ```RecyclerView.ItemDecoration``` that can be used as a divider
between items of a ```android.support.v7.widget.LinearLayoutManager```
It supports ```android.support.v7.widget.LinearLayoutManager.VERTICAL``` orientation.
Can be used with custom divider drawable. 
Also provides ability to notify if decoration for last item in list is needed.
[See sample usage](../app/src/main/java/com/nullgr/androidcore/adapter)

Extensions
----------
* EditTextExtensions, provides functions to work with different ```InputFilters``` in easy way
```kotlin
editText.applyFilterOnlyDigits()
```

* KeyboardExtensions, provide functions to show and hide keyboard
```kotlin
editText.showKeyboard()
//or
activity.showKeyboard()
```

* ScreenExtensions, provides a set of functions to convert ```px``` to ```dp``` or ```sp``` in both way.
[See sample usage](../app/src/main/java/com/nullgr/androidcore/resources/ResourcesExampleActivity.kt)

* ViewExtensions, provides set of extensions for ```View```.
```kotlin
someView.hide()
someView.show()
someView.toggleView(true)

```
* WindowExtensions, provides extensions to set ```fitsSystemWindows``` for window content view,
and set status bar color.
```kotlin
window.setStatusBarColor(Color.RED)
```

Fragments
---------
FragmentManagerExtensions provides set of extensions to quick and easy adding, replacing fragments.

```kotlin
fragmentManager.addScreen(SomeFragment.newInstance(),R.id.fragmentContainer)
```

Toast
-----
QuickToastExtensions provides few methods to quick show any string as toast.
```kotlin
"Some toast message".showToast(context)
```

Widgets
-------
Contains custom widgets:```TextView```,```EditText```, ```Button```, and ```Switch``` 
that can work with custom typeface.


For full list of functions and classes, please look to the source code, or view documentation.
