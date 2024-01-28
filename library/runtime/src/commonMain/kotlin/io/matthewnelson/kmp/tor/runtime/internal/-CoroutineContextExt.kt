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
@file:Suppress("KotlinRedundantDiagnosticSuppress")

package io.matthewnelson.kmp.tor.runtime.internal

import kotlinx.coroutines.*
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.coroutines.CoroutineContext

@Suppress("NOTHING_TO_INLINE")
internal inline val CoroutineContext.isTorRuntime: Boolean get() {
    // TODO: Check dispatcher name/type
    val name = this[CoroutineName]?.name ?: ""
    return name.startsWith("TorRuntime")
}

@Suppress("NOTHING_TO_INLINE")
internal inline val CoroutineContext.jobOrNull: Job? get() = get(Job)

@OptIn(ExperimentalContracts::class)
internal inline fun <T: Throwable> CoroutineContext?.cancellationExceptionOr(
    block: () -> T,
): Throwable {
    contract {
        callsInPlace(block, InvocationKind.AT_MOST_ONCE)
    }

    if (this == null) return block()

    return if (jobOrNull?.isCancelled == true) {
        CancellationException("Job was cancelled")
    } else {
        block()
    }
}
