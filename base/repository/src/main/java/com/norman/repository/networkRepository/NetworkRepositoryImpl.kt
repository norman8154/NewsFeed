package com.norman.repository.networkRepository

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

class NetworkRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : NetworkRepository {

    override val isOnline: Flow<Boolean> = callbackFlow {
        val connectivityManager = context.getSystemService(ConnectivityManager::class.java)

        if (connectivityManager == null) {
            trySend(false)
            channel.close()

            return@callbackFlow
        }

        var currentNetwork = connectivityManager.activeNetwork

        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                currentNetwork = network

                val capabilities = connectivityManager.getNetworkCapabilities(network)
                trySend(capabilities?.hasValidatedInternet() == true)
            }

            override fun onCapabilitiesChanged(
                network: Network,
                networkCapabilities: NetworkCapabilities,
            ) {
                if (network == currentNetwork) {
                    trySend(networkCapabilities.hasValidatedInternet())
                }
            }

            override fun onLost(network: Network) {
                if (network == currentNetwork) {
                    currentNetwork = null
                    trySend(false)
                }
            }
        }

        connectivityManager.registerDefaultNetworkCallback(callback)
        trySend(connectivityManager.isCurrentlyOnline())

        awaitClose {
            connectivityManager.unregisterNetworkCallback(callback)
        }
    }
        .distinctUntilChanged()

    private fun ConnectivityManager.isCurrentlyOnline(): Boolean {
        val capabilities = getNetworkCapabilities(activeNetwork) ?: return false

        return capabilities.hasValidatedInternet()
    }

    private fun NetworkCapabilities.hasValidatedInternet(): Boolean {
        return hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }
}
