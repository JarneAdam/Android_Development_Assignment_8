package be.vives.jarne.assignment_8.data

import be.vives.jarne.assignment_8.network.BoredApiService
import javax.inject.Inject

interface BoredRepository {
    suspend fun getRandomActivity(): Result<BoredActivityDto>
}

class NetworkBoredRepository @Inject constructor(
    private val apiService: BoredApiService,
) : BoredRepository {
    override suspend fun getRandomActivity(): Result<BoredActivityDto> {
        return runCatching {
            apiService.getRandomActivity()
        }
    }
}
