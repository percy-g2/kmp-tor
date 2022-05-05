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
package io.matthewnelson.kmp.tor.ext.callback.controller.common.control.usecase

import io.matthewnelson.kmp.tor.common.address.OnionAddressV3
import io.matthewnelson.kmp.tor.ext.callback.controller.common.Task
import io.matthewnelson.kmp.tor.ext.callback.controller.common.TorCallback
import io.matthewnelson.kmp.tor.ext.callback.controller.common.control.CallbackTorControlOnionClientAuth

/**
 * "ONION_CLIENT_AUTH_REMOVE" SP HSAddress
 *
 * KeyType = “x25519” is the only one supported right now
 *
 * https://torproject.gitlab.io/torspec/control-spec/#onion_client_auth_remove
 *
 * @see [CallbackTorControlOnionClientAuth]
 * */
interface CallbackTorControlOnionClientAuthRemove {

    fun onionClientAuthRemove(
        address: OnionAddressV3,
        failure: TorCallback<Throwable>?,
        success: TorCallback<Any?>,
    ): Task

}
