package se.accepted.krib

import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

/**
 * Krib
 *
 * This class is a container for inversion of control.
 * By mapping an implementation to an alias (interface) you can later retrieve an instance of the
 * implementation with that alias as key.
 *
 * This is the simplest version of Krib where it can manage any type and it will only store a single instance
 * of any given implementation. It will not create implementations never required.
 *
 * @author Bob Dahlberg
 */
class Krib :IKrib {
    
    private val aliasMap = mutableMapOf<KClass<out Any>, KClass<out Any>>()
    private val aliasNewMap = mutableMapOf<KClass<out Any>, KClass<out Any>>()
    private val instanceMap = mutableMapOf<KClass<out Any>,Any>()
    
    /**
     * Add an implementation type to be handled by this container.
     * If the alias is already mapped against a type it will throw a VerifyError.
     * The implementation type will be stored uninitialized until it is first required.
     * The same instance will then be returned every time the alias is required.
     */
    override fun <I:Any,IM:I> add(clazz:KClass<IM>, alias:KClass<I>) {
        if( aliasMap.contains( alias ) ) {
            throw VerifyError( "Can't add several implementation types mapped to the same alias: $alias" )
        }
        aliasMap[alias] = clazz
    }
    
    /**
     * Add an implementation type to be handled by this container.
     * If the alias is already mapped against a type it will throw a VerifyError.
     * The implementation type will be stored uninitialized.
     * A new instance will then be created every time the alias is required.
     */
    override fun <I:Any,IM:I> addCreateOnRequire(clazz:KClass<IM>, alias:KClass<I>) {
        if( aliasMap.contains( alias ) ) {
            throw VerifyError( "Can't add several implementation types mapped to the same alias: $alias" )
        }
        aliasNewMap[alias] = clazz
        aliasMap[alias] = clazz
    }
    
    /**
     * Add an instantiated implementation to be provided by this container.
     * If the alias is already mapped against a type it will throw a VerifyError.
     * The same instance will be returned every time the alias is required.
     */
    override fun <I:Any,IM:I> addInstance( instance:IM, alias:KClass<I>) {
        add( instance::class, alias )
        instanceMap[instance::class] = instance
    }
    
    /**
     * Require an instance mapped against the specific alias.
     * Since the instance implements (or extends) the alias, the type returned will be the alias.
     */
    override fun <T:Any> require( alias:KClass<T>):T {
        aliasMap[alias]?.let {
            aliasNewMap[alias]?.let {
                return it.createInstance() as T
            }
            return instanceMap.getOrPut( it, it::createInstance ) as T
        }
        throw Error( "Could not find implementation for alias $alias" )
    }
}

