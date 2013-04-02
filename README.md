AndroidImageCache
=================
Android library which simplifies displaying, caching and managing a lifecycle of images fetch from the web. It provides asynchronous downloading, tasks cancelling and two level cache.  
### Purposes ####
Almost every Android application has `ListView` which contains row with images showed via `ImageView`. Making smooth application is what we want to have and processing bitmap (or worse jpg) each time a row is built decreases smoothness. 

### Sample ###
You can play with this library using sample app. Checkout code under [`/sample`](https://github.com/Polidea/AndroidImageCache/tree/master/sample) or play with the [APK](https://github.com/Polidea/AndroidImageCache/blob/master/sample/android-image-cache-sample-2.0.apk) on your device.

Releases
========
1. \[LATEST\] 2013-04-01 - [v2.0](https://github.com/Polidea/AndroidImageCache/tree/v2.0) - _Downloading tasks improvements, introducing Android NBS, migrating all tests to RoboSpock_

Obtaining
=========
###Maven###
```xml
<dependency>
      <groupId>pl.polidea</groupId>
      <artifactId>android-image-cache-library</artifactId>
      <version>2.0</version>
  </dependency>
```

###Gradle###
`compile 'pl.polidea:android-image-cache-library:2.0'`

###JAR###
[Download](http://repo1.maven.org/maven2/pl/polidea/android-image-cache-library/2.0/android-image-cache-library-2.0.jar) from public Maven repository

How to build
============

### Android New Build System ###
Not only this project is a handy android library. It's also a real-world working example of [Android New Build System](http://tools.android.com/tech-docs/new-build-system). Checkout how dependencies between modules are configured among `build.gradle` files.

### RoboSpock ###
Quality of this library is guarded by tests build using our [Robospock](https://github.com/Polidea/RoboSpock) library. You can find out how real Android library can be tested without on-device deployment using [Spock](https://code.google.com/p/spock/) and [Robolectric](http://pivotal.github.com/robolectric/)

### Dependencies ###
The only tool you need is Gradle v1.3 or v1.4 (as of 02-04-2013, gradle 1.5 is not supported by NBS)

### Build ###
Just run in project root: `gradle clean build`

Developed by
============
* Przemysław Jakubczyk <przemyslaw.jakubczyk@polidea.pl>
* Mateusz Grzechociński <mateusz.grzechocinski@polidea.pl>

License
=======
        Copyright 2013 Polidea sp. z o.o.
      
        Licensed under the Apache License, Version 2.0 (the "License");
        you may not use this file except in compliance with the License.
        You may obtain a copy of the License at
        
        http://www.apache.org/licenses/LICENSE-2.0
      
        Unless required by applicable law or agreed to in writing, software
        distributed under the License is distributed on an "AS IS" BASIS,
        WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
        See the License for the specific language governing permissions and
        limitations under the License.
