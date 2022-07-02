package com.example.tibberapp.domain.usecase

import com.example.tibberapp.domain.repository.PowerUpsRepository
import javax.inject.Inject

class GetPowerUpsUseCase @Inject constructor(
    private val powerUpsRepository: PowerUpsRepository
) {
    suspend operator fun invoke() = powerUpsRepository.getPowerUps()
}