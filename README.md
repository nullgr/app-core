[![Awesome](https://cdn.rawgit.com/sindresorhus/awesome/d7305f38d29fed78fa85652e3a63e154dd8e8829/media/badge.svg)](https://github.com/sindresorhus/awesome)

App Core
===========
A set of extensions for Kotlin and RX utilities for more comfortable development of applications for android. 
This library is designed to combine often used functions in development and provide 
a convenient interface for working with widespread, boilerplate tasks. 
Each project contains a utility package, so this project is called upon to replace it, 
as well as find a quick solution for everyday tasks. 
You can add the library completely or select only the modules you need.

----

Few samples:
===========
**Date extensions:**

```kotlin
var sampleDate:Date = System.currentTimeMillis().toDate()
var formattedDate = sampleDate
                        .toStringWithFormat(CommonFormats.FORMAT_SIMPLE_DATE_TIME)
```
**Fonts and Spans extensions:**
```kotlin
var spannableText = someStingText.applyFont(context, "Roboto-Bold.ttf")
```
```kotlin
var spannableText = (someStringText.applySpanSet()
                      add font(getTypeface("Roboto-Bold.ttf")) toText "Digital"
                      and font(getTypeface("Roboto-Italic.ttf")) from start2 to end2
                      and relativeSize(1.8f) toText "revolution"
                      and foregroundColor(Color.GREEN) from start3
                      and absSize(28f.spToPx(context).toInt()) toText "we do.").build()
```

**Intents:**
```kotlin
  callIntent("+### ### ## ##").launch(context)
```

```kotlin
 selectContactIntent()
        .launchForResult(context)
        .subscribe(
           { rxResult:RxActivityResult->
                   doSomeThingWithResult(rxResult)
           },
           { 
             it.message.toString.showToast(contxt)
           })
```
View***WIKI PAGE*** (will be added soon) for full list of provided utilities and documentation.
For more information about how to use - look into <a href="https://github.com/nullgr/app-core/tree/develop/app">sample project</a>.</b>

Getting Started
---------------

To add App Core to your project, add the following to your app module's `build.gradle`:

```groovy
repositories {
  //TODO
}

dependencies {
   //TODO
}
```

Project Status
--------------
Still under hard development

How to Contribute
-----------------
Soon...

License
=======
MIT License

Copyright (c) 2018 Nullgravity

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.