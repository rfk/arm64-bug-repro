This is a reduced test-case for an apparent bug in JNA on arm64,
when passing many small structures as function arguments.

Build a `.aar` using:

* `cd ./android`
* `./gradlew publishToMavenLocal`

Then, in a consuming application, import `org.mozilla.appservices.broken.BrokenThing`
and call `BrokenThing().getval()`. It is supposed to return `9`, but on (at least)
Android arm64 it seems to be reading and returngin garbage data.

I believe this to be a bug somewhere in JNA, because it reproduces against
functionality implemented in Rust (in the `./src` directory) and the same
functionality implemented in C (in the `./csrc` directory.
