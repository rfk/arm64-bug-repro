This is a reduced test-case for an apparent bug in JNA on arm64,
when passing many small structures as function arguments.

In `./src/main/c/broken.c` we define a simple struct `WrappedLong` which
holds a single `int64_t` value, and a function `return_the_ninth_argument`
which takes nine such structs and returns the value from the ninth one.

Calling this function via JNA seems to trigger a bug on arm64 devices.
The ninth argument has to be passed on the stack rather than in registers,
and the code for this in libffi seems to be broken.

To test it, you can build an Android app `.apk` using:

* `./gradlew assemble`

Install and run the resulting app. It should display "Expected 9, got 9" but
on an arm64 device, it shows the native library call returning a large number
(which is actually a pointer).
