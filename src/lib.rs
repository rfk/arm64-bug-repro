// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

#![feature(asm)]

#[repr(C)]
pub struct WrappedLong {
    inner: i64,
}

#[no_mangle]
pub extern "C" fn this_seems_broken(
  arg1: WrappedLong,
  arg2: WrappedLong,
  arg3: WrappedLong,
  arg4: WrappedLong,
  arg5: WrappedLong,
  arg6: WrappedLong,
  arg7: WrappedLong,
  arg8: WrappedLong,
  arg9: WrappedLong,
) -> i64 {
  arg9.inner
}
