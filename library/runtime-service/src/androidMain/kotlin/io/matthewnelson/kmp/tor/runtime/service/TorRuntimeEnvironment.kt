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
@file:JvmName("TorRuntimeEnvironment")

package io.matthewnelson.kmp.tor.runtime.service

import android.content.Context
import android.content.res.Resources
import io.matthewnelson.kmp.file.File
import io.matthewnelson.kmp.tor.core.api.ResourceInstaller
import io.matthewnelson.kmp.tor.core.api.ResourceInstaller.Paths
import io.matthewnelson.kmp.tor.core.api.annotation.ExperimentalKmpTorApi
import io.matthewnelson.kmp.tor.runtime.TorRuntime
import io.matthewnelson.kmp.tor.runtime.core.ThisBlock
import io.matthewnelson.kmp.tor.runtime.core.apply

/**
 * Android extension which utilizes [Context.getDir] and [Context.getCacheDir]
 * to acquire default tor directory locations within the application's data
 * directory to create [TorRuntime.Environment].
 *
 * **NOTE:** [TorRuntime.Environment.Builder.serviceFactoryLoader] is set
 * automatically and tor will run inside an [android.app.Service].
 *
 * - workDirectory: app_torservice
 * - cacheDirectory: cache/torservice
 *
 * @throws [Resources.NotFoundException] if configured to run as a Foreground
 *   Service and [TorServiceConfig.getMetaData] throws exception.
 * */
@JvmName("Builder")
public fun Context.createTorRuntimeEnvironment(
    installer: (installationDirectory: File) -> ResourceInstaller<Paths.Tor>,
): TorRuntime.Environment = createTorRuntimeEnvironment("torservice", installer)

/**
 * Android extension which utilizes [Context.getDir] and [Context.getCacheDir]
 * to acquire default tor directory locations within the application's data
 * directory to create [TorRuntime.Environment].
 *
 * **NOTE:** [TorRuntime.Environment.Builder.serviceFactoryLoader] is set
 * automatically and tor will run inside an [android.app.Service].
 *
 * - workDirectory: app_torservice
 * - cacheDirectory: cache/torservice
 *
 * @throws [Resources.NotFoundException] if configured to run as a Foreground
 *   Service and [TorServiceConfig.getMetaData] throws exception.
 * */
@JvmName("Builder")
public fun Context.createTorRuntimeEnvironment(
    installer: (installationDirectory: File) -> ResourceInstaller<Paths.Tor>,
    block: ThisBlock<TorRuntime.Environment.Builder>,
): TorRuntime.Environment = createTorRuntimeEnvironment("torservice", installer, block)

/**
 * Android extension which utilizes [Context.getDir] and [Context.getCacheDir]
 * to acquire tor directory locations within the application's data directory
 * for specified [dirName] to create [TorRuntime.Environment] with.
 *
 * **NOTE:** [TorRuntime.Environment.Builder.serviceFactoryLoader] is set
 * automatically and tor will run inside an [android.app.Service].
 *
 * - workDirectory: app_[dirName]
 * - cacheDirectory: cache/[dirName]
 *
 * @throws [Resources.NotFoundException] if configured to run as a Foreground
 *   Service and [TorServiceConfig.getMetaData] throws exception.
 * */
@JvmName("Builder")
public fun Context.createTorRuntimeEnvironment(
    dirName: String,
    installer: (installationDirectory: File) -> ResourceInstaller<Paths.Tor>,
): TorRuntime.Environment = createTorRuntimeEnvironment(dirName, installer) {}

/**
 * Android extension which utilizes [Context.getDir] and [Context.getCacheDir]
 * to acquire tor directory locations within the application's data directory
 * for specified [dirName] to create [TorRuntime.Environment] with.
 *
 * **NOTE:** [TorRuntime.Environment.Builder.serviceFactoryLoader] is set
 * automatically and tor will run inside an [android.app.Service].
 *
 * - workDirectory: app_[dirName]
 * - cacheDirectory: cache/[dirName]
 *
 * @throws [Resources.NotFoundException] if configured to run as a Foreground
 *   Service and [TorServiceConfig.getMetaData] throws exception.
 * */
@JvmName("Builder")
public fun Context.createTorRuntimeEnvironment(
    dirName: String,
    installer: (installationDirectory: File) -> ResourceInstaller<Paths.Tor>,
    block: ThisBlock<TorRuntime.Environment.Builder>,
): TorRuntime.Environment = TorRuntime.Environment.Builder(
    workDirectory = getDir(dirName.ifBlank { "torservice" }, Context.MODE_PRIVATE),
    cacheDirectory = cacheDir.resolve(dirName.ifBlank { "torservice" }),
    installer = installer,
) {
    this.apply(block)

    // Will not be null b/c TorService.Initializer
    // should be there if consumer is utilizing this
    // function which has Context available.
    @OptIn(ExperimentalKmpTorApi::class)
    serviceFactoryLoader = TorService.loaderOrNull()
}
