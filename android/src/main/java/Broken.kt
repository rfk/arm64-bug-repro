
package org.mozilla.appservices.broken

import com.sun.jna.Library
import com.sun.jna.NativeLibrary
import com.sun.jna.Native
import com.sun.jna.Structure
import com.sun.jna.Pointer


@Structure.FieldOrder("inner")
open class WrappedLong : Structure() {
    @JvmField var inner: Long = 0

    class ByValue : WrappedLong(), Structure.ByValue

    companion object {
      fun containing(v: Long) : WrappedLong.ByValue {
        val w = WrappedLong.ByValue()
        w.inner = v
        return w
      }
    }
}


internal interface LIBRARY: Library {
  companion object {
    internal var INSTANCE: LIBRARY = Native.load<LIBRARY>("cbroken", LIBRARY::class.java)
  }

  fun this_seems_broken(
    arg1: WrappedLong.ByValue,
    arg2: WrappedLong.ByValue,
    arg3: WrappedLong.ByValue,
    arg4: WrappedLong.ByValue,
    arg5: WrappedLong.ByValue,
    arg6: WrappedLong.ByValue,
    arg7: WrappedLong.ByValue,
    arg8: WrappedLong.ByValue,
    arg9: WrappedLong.ByValue
  ): Long
}


public class BrokenThing() {
  fun getval(): Long {
    // The native function returns the wrapped value in its ninth argument.
    // So this call should always return `9`, right?
    // Well, on android arm64 it seems to be reading garbage from the stack instead.
    // I have a working theory that JNA or libffi is not passing the 9th argument
    // correctly, because it works as expected when the native function returns any
    // of its first 8 arguments. This may be related to the arm64 calling convention
    // passing up to 8 integer arguments in registers but additional arguments on
    // the stack, but that's the limit of my understanding so far.
    return LIBRARY.INSTANCE.this_seems_broken(
      WrappedLong.containing(1),
      WrappedLong.containing(2),
      WrappedLong.containing(3),
      WrappedLong.containing(4),
      WrappedLong.containing(5),
      WrappedLong.containing(6),
      WrappedLong.containing(7),
      WrappedLong.containing(8),
      WrappedLong.containing(9)
    )
  }
}
