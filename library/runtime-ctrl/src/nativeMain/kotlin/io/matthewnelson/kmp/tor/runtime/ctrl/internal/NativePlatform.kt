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
@file:Suppress("UnnecessaryOptInAnnotation")

package io.matthewnelson.kmp.tor.runtime.ctrl.internal

import io.matthewnelson.kmp.file.IOException
import io.matthewnelson.kmp.file.errnoToIOException
import io.matthewnelson.kmp.tor.runtime.core.net.IPAddress
import io.matthewnelson.kmp.tor.runtime.core.net.IPSocketAddress
import io.matthewnelson.kmp.tor.runtime.ctrl.TorCtrl
import kotlinx.cinterop.*
import kotlinx.coroutines.CloseableCoroutineDispatcher
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.newFixedThreadPoolContext
import platform.posix.*

@OptIn(DelicateCoroutinesApi::class, ExperimentalCoroutinesApi::class)
internal actual fun TorCtrl.Factory.newTorCtrlDispatcher(): CloseableCoroutineDispatcher {
    return newFixedThreadPoolContext(2, "TorCtrl")
}

@Throws(Throwable::class)
@OptIn(ExperimentalForeignApi::class, UnsafeNumber::class)
internal actual fun IPSocketAddress.connect(): CtrlConnection = memScoped {
    val (family, len) = when (address) {
        is IPAddress.V4 -> AF_INET to sizeOf<sockaddr_in>()
        is IPAddress.V6 -> AF_INET6 to sizeOf<sockaddr_in6>()
    }

    val hint: CValue<addrinfo> = cValue {
        ai_family = family
        ai_socktype = SOCK_STREAM
        ai_flags = AI_NUMERICHOST
        ai_protocol = 0
    }
    val result = alloc<CPointerVar<addrinfo>>()

    if (getaddrinfo(address.value, port.toString(), hint, result.ptr) != 0) {
        throw errnoToIOException(errno)
    }

    defer { freeaddrinfo(result.value) }

    var info: addrinfo? = result.pointed
        ?: throw IOException("Failed to resolve addrinfo for ${this@connect}")

    var descriptor: Int? = null
    var lastErrno: Int? = null
    while (info != null && descriptor == null) {
        val sockaddr = info.ai_addr?.pointed

        if (sockaddr != null) {
            val socket = socket(family, SOCK_STREAM, 0)

            if (socket < 0) {
                lastErrno = errno
            } else {
                // connect man7 NOTES:
                // If connect() fails, consider the state of the socket
                // as unspecified. Portable applications should close
                // the socket and create a new one for reconnecting.
                if (connect(socket, sockaddr.ptr, len.convert()) != 0) {
                    lastErrno = errno
                    close(socket)
                } else {
                    descriptor = socket
                }
            }
        }

        info = info.ai_next?.pointed
    }

    if (descriptor == null) {
        throw lastErrno?.let { errnoToIOException(it) }
            ?: IOException("Failed to connect to ${this@connect}")
    }

    NativeCtrlConnection(descriptor)
}
