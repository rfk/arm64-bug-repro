
package org.mozilla.appservices.broken

import com.sun.jna.Library
import com.sun.jna.NativeLibrary
import com.sun.jna.Native
import com.sun.jna.Structure
import com.sun.jna.Pointer


// A struct containing a single `int64_t` field.
//
@Structure.FieldOrder("inner")
open class WrappedLong : Structure() {
    @JvmField var inner: Long = 0

    class ByValue : WrappedLong(), Structure.ByValue

    companion object {
      //
      // A little helper to construct and set the inner field.
      // (I was using this mess around with different permutations of
      // padding fields in order to better understand the bug).
      //
      fun containing(v: Long) : WrappedLong.ByValue {
        val w = WrappedLong.ByValue()
        w.inner = v
        return w
      }
    }
}


// A library exposing a single function, which takes nine `WrappedLong` structs
// and returns the value contained in the ninth one.
//
internal interface LIBRARY: Library {
  companion object {
    internal var INSTANCE: LIBRARY = Native.load<LIBRARY>("broken", LIBRARY::class.java)
  }

  fun return_the_ninth_argument(
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
  fun check() {
    //
    // The native function returns the wrapped value in its ninth argument.
    // So this call should always return `9`, right?
    //
    // Well, on android arm64 it returns a pointer to the value `9` instead.
    //
    // This seems to be due to a bug in the version of libffi shipped with JNA,
    // specifically when passing a small struct argument on the stack. This line of code:
    //
    //    https://github.com/java-native-access/jna/blob/0a0e44d4e8c616eb948fae7b87f882db32db5e60/native/libffi/src/aarch64/ffi.c#L733
    //
    // Incorrectly `memcpy`s from `ecif->avalue + i` instead of from `ecif->avalue[i]`,
    // failing to resolve one level of pointer indirection.
    // 
    val ninth_arg = LIBRARY.INSTANCE.return_the_ninth_argument(
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

    if (ninth_arg != 9L) {
      throw RuntimeException("Expected call to return 9, but instead got $ninth_arg")
    }
  }
}
