package se.accepted.krib

import kotlin.reflect.KClass

/**
 * IKrib
 * @author Bob Dahlberg
 */
interface IKrib {
    
    /**
     * Add an implementation type to be handled by this container.
     * If the alias is already mapped against a type it will throw a VerifyError.
     * The implementation type will be stored uninitialized until it is first required.
     * The same instance will then be returned every time the alias is required.
     */
    fun <I:Any,IM:I> add(clazz:KClass<IM>, alias:KClass<I>)
    
    /**
     * Add an implementation type to be handled by this container.
     * If the alias is already mapped against a type it will throw a VerifyError.
     * The implementation type will be stored uninitialized.
     * A new instance will then be created every time the alias is required.
     */
    fun <I:Any,IM:I> addCreateOnRequire(clazz:KClass<IM>, alias:KClass<I>)
    
    /**
     * Add an instantiated implementation to be provided by this container.
     * If the alias is already mapped against a type it will throw a VerifyError.
     * The same instance will be returned every time the alias is required.
     */
    fun <I:Any,IM:I> addInstance( instance:IM, alias:KClass<I>)
    
    /**
     * Require an instance mapped against the specific alias.
     * Since the instance implements (or extends) the alias, the type returned will be the alias.
     */
    fun <T:Any> require( alias:KClass<T>):T
}
