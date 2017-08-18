package se.accepted.krib

import junit.framework.TestCase

class KribTest():TestCase() {
	
	private var crib:Krib = Krib()
    private val myLazy by inject<IMarker>( from = crib )

    override fun setUp() {
        super.setUp()
        crib = Krib()
    }
    
    fun testAdd() {
        crib.add( KribStubImpl::class, Kribbed::class )
        assertTrue( "Should get here without an exception", true )
    }
    
    fun testAddCreater() {
        crib.addCreateOnRequire( KribStubImpl::class, Kribbed::class )
        assertTrue( "Should get here without an exception", true )
    }
    
    fun testAddMultiple() {
        crib.add( KribStubImpl::class, Kribbed::class )
        crib.add( KribStubImpl::class, IMarker::class )
        assertTrue( "Should get here without an exception", true )
    }
    
    fun testAddInstance() {
        crib.addInstance(KribStubImpl(), Kribbed::class )
        assertTrue( "Should get here without an exception", true )
    }
    
    fun testRequireUninitialized() {
        crib.add( KribStubImpl::class, Kribbed::class )
        val c = crib.require( Kribbed::class )
        assertTrue( "The return value should be the class put in.", c is KribStubImpl)
    }
    
    fun testRequireInitialized() {
        val c1 = KribStubImpl()
        crib.addInstance( c1, Kribbed::class )
        val c2 = crib.require( Kribbed::class )
        assertTrue( "The return value should be the same as we added.", c1 === c2 )
    }
    
    fun testRequireNewInstance() {
        
        crib.addCreateOnRequire( KribStubImpl::class, Kribbed::class )
        val c1 = crib.require( Kribbed::class )
        val c2 = crib.require( Kribbed::class )
        assertNotSame( "The return value should be not the same.", c1, c2 )
    }
    
    fun testRequireMultipleUninitialized() {
        crib.add( KribStubImpl::class, Kribbed::class )
        crib.add( KribStubImpl::class, IMarker::class )
    
        val c1 = crib.require( Kribbed::class )
        val c2 = crib.require( IMarker::class )
        assertSame( "Should be the same implementation even on different interfaces", c1, c2 )
    }
    
    fun testAddDoubletsClass() {
        try {
            crib.add(KribStubImpl::class, Kribbed::class)
            crib.add(KribStubImpl::class, Kribbed::class)
        }
        catch ( e:VerifyError ) {
            assertTrue( "We caught the error", true )
            return
        }
        fail( "Should not end up here" )
    }
    
    fun testAddDoubletsInstance() {
        
        try {
            crib.addInstance(KribStubImpl(), Kribbed::class )
            crib.addInstance(KribStubImpl(), Kribbed::class )
        }
        catch ( e:VerifyError ) {
            assertTrue( "We caught the error", true )
            return
        }
        fail( "Should not end up here" )
    }
    
    fun testAddDoubletsClassNew() {
        try {
            crib.addCreateOnRequire( KribStubImpl::class, Kribbed::class )
            crib.addCreateOnRequire( KribStubImpl::class, Kribbed::class )
        }
        catch ( e:VerifyError ) {
            assertTrue( "We caught the error", true )
            return
        }
        fail( "Should not end up here" )
    }
    
    fun testAddDoubletsCI() {
        try {
            crib.add( KribStubImpl::class, Kribbed::class )
            crib.addInstance(KribStubImpl(), Kribbed::class )
        }
        catch ( e:VerifyError ) {
            assertTrue( "We caught the error", true )
            return
        }
        fail( "Should not end up here" )
    }
    
    fun testAddDoubletsNI() {
        try {
            crib.addCreateOnRequire( KribStubImpl::class, Kribbed::class )
            crib.addInstance(KribStubImpl(), Kribbed::class )
        }
        catch ( e:VerifyError ) {
            assertTrue( "We caught the error", true )
            return
        }
        fail( "Should not end up here" )
    }
    
    fun testAddDoubletsNC() {
        try {
            crib.addCreateOnRequire( KribStubImpl::class, Kribbed::class )
            crib.add( KribStubImpl::class, Kribbed::class )
        }
        catch ( e:VerifyError ) {
            assertTrue( "We caught the error", true )
            return
        }
        fail( "Should not end up here" )
    }
    
    fun testAddDoubletsReordered() {
        try {
            crib.addInstance(KribStubImpl(), Kribbed::class)
            crib.add(KribStubImpl::class, Kribbed::class)
        }
        catch ( e:VerifyError ) {
            assertTrue( "We caught the error", true )
            return
        }
        fail( "Should not end up here" )
    }
}
