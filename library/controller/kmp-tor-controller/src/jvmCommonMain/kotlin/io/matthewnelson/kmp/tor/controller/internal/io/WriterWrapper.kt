/*
 * Copyright (c) 2022 Matthew Nelson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 **/
package io.matthewnelson.kmp.tor.controller.internal.io

import io.matthewnelson.kmp.tor.controller.common.exceptions.TorControllerException
import java.io.IOException
import java.io.OutputStreamWriter
import java.net.Socket
import java.net.SocketException
import kotlin.jvm.JvmSynthetic

@JvmInline
internal actual value class WriterWrapper private actual constructor(private val value: Any) {

    private inline val asWriter: OutputStreamWriter get() = value as OutputStreamWriter

    @JvmSynthetic
    @Throws(TorControllerException::class)
    actual fun write(string: String) {
        try {
            asWriter.write(string)
        } catch (e: IOException) {
            throw TorControllerException(e)
        }
    }

    @JvmSynthetic
    @Throws(TorControllerException::class)
    actual fun flush() {
        try {
            asWriter.flush()
        } catch (e: IOException) {
            throw TorControllerException(e)
        }
    }

    companion object {
        @JvmSynthetic
        @Throws(IOException::class, SocketException::class)
        internal fun from(socket: Socket): WriterWrapper =
            WriterWrapper(socket.getOutputStream().writer())
    }
}
