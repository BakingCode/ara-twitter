Description
=================

ara-twitter is a library for consume twitter Rest API 1.1. Actually the library does not implement all the methods, only the most used but is accepting pull requests for complete all the api specification.


Using the library
================

via gradle. Add this to your build.gradle:

    dependencies {  
        repositories {
            mavenCentral()
            maven {
                url 'https://oss.sonatype.org/content/groups/staging'
            }
        }
        compile 'com.bakingcode.io.twitter:ara-twitter:1.0.2-SNAPSHOT@aar'
    }


Applications using ara-twitter
================

* [Ararauna Twitter tracker](https://play.google.com/store/apps/details?id=bakingcode.tweedb) by BakingCode

Want to be here? Send us a [twitter](https://twitter.com/bakingcode) or [email](mailto:info@bakingcode.com) with your playstore link.


License
=======

    Copyright 2014 BakingCode

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
