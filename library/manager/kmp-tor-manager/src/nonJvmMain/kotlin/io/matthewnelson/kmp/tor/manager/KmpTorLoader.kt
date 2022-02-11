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
package io.matthewnelson.kmp.tor.manager

import io.matthewnelson.kmp.tor.controller.TorController
import io.matthewnelson.kmp.tor.controller.common.config.TorConfig
import io.matthewnelson.kmp.tor.manager.common.event.TorManagerEvent
import io.matthewnelson.kmp.tor.manager.common.exceptions.TorManagerException
import io.matthewnelson.kmp.tor.manager.internal.TorStateMachine
import kotlinx.atomicfu.locks.ReentrantLock
import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.cancellation.CancellationException

@Suppress("CanBePrimaryConstructorProperty")
actual abstract class KmpTorLoader(provider: TorConfigProvider) {

    private val provider = provider
    protected actual open val excludeSettings: Set<TorConfig.Setting<*>> = emptySet()
    internal actual open suspend fun load(
        managerScope: CoroutineScope,
        stateMachine: TorStateMachine,
        notify: (TorManagerEvent) -> Unit,
    ): Result<TorController> {
        TODO("Not yet implemented")
    }

    internal actual open fun close() { /* no-op */ }

    internal actual open fun cancelTorJob() { /* no-op */ }

    @Throws(TorManagerException::class, CancellationException::class)
    protected actual abstract suspend fun startTor(
        configLines: List<String>,
        notify: (TorManagerEvent.Log) -> Unit,
    )
}