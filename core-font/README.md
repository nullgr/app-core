Core Font
============

This module contains set of extensions and classes to work with spans and fonts.

FontsExtensions
---------------
Quick access to typeface by full path to specified font.
Sample usage:
```kotlin
val typeface = context.getTypeface("font/RobotoRegular.otf")
```
Extension to easy set title with custom typeface for ```Toolbar``` and ```ActionBar```
Sample usage:
```kotlin
toolbar.setSpannableTitle(context, "Some title", "font/RobotoRegular.otf")
```
SpanExtensions
--------------
* Contains set of extension functions to easy apply common span to string. 

Sample usage:
```kotlin
val spannableString = "Some String".applyForegroundColor(Color.RED, 0,5)
```
* Contains set of factory functions to create common spans.

Sample usage:
```kotlin
val foregroundColorSpan = foregroundColor(Color.RED)
val absoluteSizeSpan = absSize(50)
```
* Contains extension function to apply ```SpanSet``` to ```CharSequence```
Sample usage see in [SpanSet section](#spanset)

SpanSet
--------------
Class that provides ability to build and apply complicated span decor to text in easy way.
Can be used as a method call chain (builder style) or in infix style. 
Looks even better with factory functions listed in [SpanExtensions section](#spanextensions)

Sample usage (Infix style):
 ```kotlin
 val spannedText = ("Lorem ipsum dolor sit amet".applySpanSet()
                     add TypefaceSpan(context.getTypeface("Some.otf")) from 0 to 5
                     and RelativeSizeSpan(1.4f) from 7 to 9).build()
 ```
 Or like this (Builder style):
 ```kotlin
 val spannedText = SpanSet.applySpanSet("Lorem ipsum dolor sit amet")
                    .add(TypefaceSpan(context.getTypeface("Some.otf"))).from(0).to(5)
                    .and(RelativeSizeSpan(1.4f)).from(7).to(9).build()
 ```

SpannableStringDslExtensions
----------------------------
This set of an extensions provides DSL designed API to build and apply complicated span decor, 
working directly with ```SpannableString```. 
So this functionality is easy to extend, even without providing PR.

Sample usage:
```kotlin
val spannableString = "SomeText".withSpan {

                   typeface {
                       typeface = getTypeface("Roboto-Bold.ttf")
                       toText = "Some"
                   }

                   typeface {
                       typeface = getTypeface("Roboto-Italic.ttf")
                       from = 3
                       to = 7
                   }

                   relativeSize {
                       size = 1.8f
                       toText = "Text"
                   }  
                }
```

TypeFaceSpan
------------
Span class to set custom ```Typeface``` to string

For full list of functions, please look to the source code,[example project](../app/src/main/java/com/nullgr/androidcore/fonts)
or view documentation