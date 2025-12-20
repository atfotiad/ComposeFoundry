package com.atfotiad.composefoundry.features.counter.repository

import com.atfotiad.composefoundry.designsystem.network.NetworkResult
import com.atfotiad.composefoundry.designsystem.network.safeCall
import com.atfotiad.composefoundry.features.counter.api.CounterApi
import com.atfotiad.composefoundry.features.counter.data.local.CounterDao
import com.atfotiad.composefoundry.features.counter.data.local.CounterEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CounterRepository @Inject constructor(
    private val api: CounterApi,
    private val dao: CounterDao
) {
    // Hardcoded ID for this sample since we only have one counter
    private val counterId = "main_counter"

    /**
     * Reactive Stream: The UI observes this.
     * We map Entity? -> Int so the ViewModel doesn't care about DB objects.
     */
    fun getCounterStream(): Flow<Int> {
        return dao.getCounterFlow(counterId).map { entity ->
            entity?.count ?: 0 // Default to 0 if DB is empty
        }
    }

    /**
     * Network Sync: Fetches from API and updates DB.
     * The UI will update automatically via getCounterStream().
     */
    suspend fun syncCounterFromNetwork(): NetworkResult<Unit> {
        return when (val result = api::getRandomUuid.safeCall()) {
            is NetworkResult.Success -> {
                // Logic: Use UUID length as the new count (just for demo)
                val newCount = result.data.uuid.length

                // SAVE TO DB -> This triggers the Flow -> UI updates
                dao.saveCounter(CounterEntity(counterId, newCount))

                NetworkResult.Success(Unit)
            }
            is NetworkResult.Failure -> {
                // Pass the error up, but don't crash.
                // The UI will still show the OLD data from DB (Offline support!)
                result
            }
        }
    }

    /**
     * Local Update
     */
    suspend fun incrementLocally() {
        val current = dao.getCounterSnapshot(counterId)?.count ?: 0
        dao.saveCounter(CounterEntity(counterId, current + 1))
    }
}