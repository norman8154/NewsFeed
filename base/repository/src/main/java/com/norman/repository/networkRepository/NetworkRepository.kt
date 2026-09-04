package com.norman.repository.networkRepository

import kotlinx.coroutines.flow.Flow

interface NetworkRepository {

    val isOnline: Flow<Boolean>
}
