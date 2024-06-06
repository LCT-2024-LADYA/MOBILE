package ru.gozerov.domain.usecases

import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.gozerov.domain.repositories.LoginRepository
import javax.inject.Inject

class UpdateClientPhotoUseCase @Inject constructor(
    private val loginRepository: LoginRepository
) {

    suspend operator fun invoke(uri: Uri) = withContext(Dispatchers.IO) {
        return@withContext loginRepository.updateClientPhoto(uri)
    }

}