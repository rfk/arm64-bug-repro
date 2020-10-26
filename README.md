This is a reduced test-case for an apparent bug in JNA on arm64,
when passing many small structures as function arguments.

In `./src/main/c/broken.c` we define a simple struct `WrappedLong` which
holds a single `int64_t` value, and a function `return_the_ninth_argument`
which takes nine such structs and returns the value from the ninth one.

Calling this function via JNA seems to trigger a bug on arm64 devices.
The ninth argument has to be passed on the stack rather than in registers,
and the code for this in libffi seems to be broken.

To test it, you can build a `.aar` using:

* `./gradlew publishToMavenLocal`

Then, in a consuming Android application, import `org.mozilla.appservices.broken.BrokenThing`
and call `BrokenThing().check()`. It will throw a `RuntimeException` if the underlying
C library function does not return the expected result.

For me, this works fine when running on an x86_64-based emulator, but errors
when running on an arm64 device.

