# Krib
A utility micro-library for inversion of control, lazy initialization and centralizin resources. 

This class stores and serves your modules for you. 
After you've provided mapping for witch implementation or instance to return when an alias is requested
it will store that instance in memory and serve it all following requests of the same alias. 

## Bootstrapping
```kotlin
// Instantiate
val krib = Krib()

// Put an implementation with empty constructor mapped to an alias
krib.add( Clock::class, IClock::class )

// Put an instance of an implementation mapped to an alias when you need to inject contructor paramters.
krib.addInstance( Clock( 0, 0, 0 ), IClock::class )

// Put an implementation that will create a new instance each time requested
krib.addCreateOnRequire( Clock::class, ICurrentTime::class )
```

The first example uses lazy initialization, which means that if an alias is never requested, 
the corresponding implementation is never instantiated. 
To be able to do this, the implementations need to have empty constructors.

The second example is for those implementations that don't have an empty constructor, 
then you provide an instance of the implementation to serve when a specific alias is requested.

The third example is for when you need a new instance of an implementation each time the alias is requested.

## Requesting implementation
```kotlin
val clock:IClock = krib.require( IClock::class )
```
This is all you have to do to get the implementation mapped to the IClock::class alias. 
The returned implementation type is not shown here, the alias is what you should be using.
If you're casting the returned instance to the `Clock` type your missing the 
separation of the actual implementation and the interface (or subclass).

## Dependency injection-ish
Using this micro library together with kotlins delegate properties makes it in many 
ways as useful as dependency injection.
```kotlin
class Schedule() {
  val clock by inject<IClock>()
  /* rest of the schedule implementation */
}
```
I've provided an extension-file showing how I got this solution to work.
For the exact example above, I use the following solution.
```kotlin
object App() {
  val krib = Krib()
  init {
    // Bootstrap
    krib.add( Clock::class, IClock::class )
  }
}
inline fun <reified T:Any> inject() = lazy { App.krib.require( T::class ) }
```


## Restrictions
1. Classes added with `add` or `addCreateOnRequire` must have an empty constructor.
2. You can only mapp an alias once.
3. The alias mapped must be an interface implemented or base class that is extended from the implementation.
4. Not thread safe
5. Kotlin 1.1

## Future plans
* Build even more dependency injection like features for when an instance is required. 
* Look into having other ways of constructing instances, not only via empty constructors.
* Having typed Kribs. 

## License
Licensed under the [MIT license](http://www.opensource.org/licenses/mit-license.php).
> MIT License
> 
> Copyright (c) 2017 Bob Dahlberg
> 
> Permission is hereby granted, free of charge, to any person obtaining a copy
> of this software and associated documentation files (the "Software"), to deal
> in the Software without restriction, including without limitation the rights
> to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
> copies of the Software, and to permit persons to whom the Software is
> furnished to do so, subject to the following conditions:
> 
> The above copyright notice and this permission notice shall be included in all
> copies or substantial portions of the Software.
> 
> THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
> IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
> FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
> AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
> LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
> OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
> SOFTWARE.
