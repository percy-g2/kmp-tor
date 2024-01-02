/*
 * Copyright (c) 2024 Matthew Nelson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 **/
package io.matthewnelson.kmp.tor.runtime.api.internal

import io.matthewnelson.kmp.file.SysPathSep
import io.matthewnelson.kmp.tor.core.api.annotation.InternalKmpTorApi
import io.matthewnelson.kmp.tor.core.resource.OSHost
import io.matthewnelson.kmp.tor.core.resource.OSInfo
import kotlin.jvm.JvmSynthetic

@get:JvmSynthetic
@OptIn(InternalKmpTorApi::class)
internal actual val IsUnixLikeHost: Boolean get() {
    return when (OSInfo.INSTANCE.osHost) {
        is OSHost.FreeBSD,
        is OSHost.Linux,
        is OSHost.MacOS -> true
        is OSHost.Windows -> false
        else -> SysPathSep == '/'
    }
}

@get:JvmSynthetic
@OptIn(InternalKmpTorApi::class)
internal actual val IsAndroidHost: Boolean get() {
    // Could be Java running on Android via Termux if JVM
    return OSInfo.INSTANCE.osHost is OSHost.Linux.Android
}
