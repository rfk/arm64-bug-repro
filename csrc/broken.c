// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

#include<stdint.h>

typedef struct WrappedLong {
    int64_t inner;
} WrappedLong;

int64_t this_seems_broken(
  WrappedLong arg1,
  WrappedLong arg2,
  WrappedLong arg3,
  WrappedLong arg4,
  WrappedLong arg5,
  WrappedLong arg6,
  WrappedLong arg7,
  WrappedLong arg8,
  WrappedLong arg9
) {
  return arg9.inner;
}
