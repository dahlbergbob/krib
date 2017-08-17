package se.accepted.krib

/**
 * Extension functions for the library
 * @author Bob Dahlberg
 */
object App{
    val krib = Krib()
    init {
        // Bootstrap
        // krib.add()
    }
}
// Use if you have global static access to your krib instance
inline fun <reified T:Any> inject() = lazy { App.krib.require( T::class ) }

// Use if you need to inject the proper krib instance to require from
inline fun <reified T:Any> inject( from:Krib ) = lazy { from.require( T::class ) }