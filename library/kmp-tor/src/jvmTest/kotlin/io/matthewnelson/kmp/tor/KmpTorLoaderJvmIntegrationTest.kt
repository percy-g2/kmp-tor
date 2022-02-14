/*
 * Copyright (c) 2021 Matthew Nelson
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
package io.matthewnelson.kmp.tor

import io.matthewnelson.kmp.tor.common.address.Port
import io.matthewnelson.kmp.tor.controller.common.config.TorConfig
import io.matthewnelson.kmp.tor.controller.common.config.TorConfig.Setting.*
import io.matthewnelson.kmp.tor.controller.common.config.TorConfig.Option.*
import io.matthewnelson.kmp.tor.helper.TorTestHelper
import kotlinx.coroutines.*
import org.junit.*

class KmpTorLoaderJvmIntegrationTest: TorTestHelper() {

    override fun testConfig(testProvider: TorConfigProviderJvm): TorConfig {
        return TorConfig.Builder {
            val control = Ports.Control()
            put(control.set(AorDorPort.Value(Port(9150))))
            put(control.set(AorDorPort.Value(Port(9151))))
            put(control.set(AorDorPort.Value(Port(9152))))
            put(control.set(AorDorPort.Value(Port(9153))))

            val dns = Ports.Dns()
            put(dns.set(AorDorPort.Value(Port(9154))))
            put(dns.set(AorDorPort.Value(Port(9155))))
            put(dns.set(AorDorPort.Value(Port(9156))))
            put(dns.set(AorDorPort.Value(Port(9157))))

            val socks = Ports.Socks()
            put(socks.set(AorDorPort.Value(Port(9158))))
            put(socks.set(AorDorPort.Value(Port(9159))))
            put(socks.set(AorDorPort.Value(Port(9160))))
            put(socks.set(AorDorPort.Value(Port(9161))))

            val http = Ports.HttpTunnel()
            put(http.set(AorDorPort.Value(Port(9162))))
            put(http.set(AorDorPort.Value(Port(9163))))
            put(http.set(AorDorPort.Value(Port(9164))))
            put(http.set(AorDorPort.Value(Port(9165))))

            val trans = Ports.Trans()
            put(trans.set(AorDorPort.Value(Port(9166))))
            put(trans.set(AorDorPort.Value(Port(9167))))
            put(trans.set(AorDorPort.Value(Port(9168))))
            put(trans.set(AorDorPort.Value(Port(9169))))

            put(ClientOnionAuthDir().set(FileSystemDir(
                testProvider.workDir.builder { addSegment(ClientOnionAuthDir.DEFAULT_NAME) }
            )))

            put(DormantClientTimeout().set(Time.Minutes(10)))
            put(DormantCanceledByStartup().set(TorF.True))
        }.build()
    }

    @Test
    fun testRestart() = runBlocking {
        println("==== JAVA - TEST: RESTART 1")
        val result1 = manager.restart()
        result1.onFailure { t ->
            throw t
        }

        delay(5_000L)
        println("==== JAVA - TEST: RESTART 2")
        val result2 = manager.restart()
        result2.onFailure { t ->
            throw t
        }

        Unit
    }

    @Test
    fun testRestart2() = runBlocking {
        println("==== JAVA - TEST: RESTART 3")
        val result3 = manager.restart()
        result3.onFailure { t ->
            throw t
        }

        delay(5_000L)
        println("==== JAVA - TEST: RESTART 4")
        val result4 = manager.restart()
        result4.onFailure { t ->
            throw t
        }

        delay(5_000)
        val result5 = manager.restart()
        result5.onFailure { t ->
            throw t
        }

        Unit
    }
}
