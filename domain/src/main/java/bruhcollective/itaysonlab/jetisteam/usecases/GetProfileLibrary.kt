package bruhcollective.itaysonlab.jetisteam.usecases

import bruhcollective.itaysonlab.jetisteam.controllers.SteamSessionController
import bruhcollective.itaysonlab.jetisteam.mappers.OwnedGame
import bruhcollective.itaysonlab.jetisteam.models.SteamID
import bruhcollective.itaysonlab.jetisteam.repository.ClientCommRepository
import bruhcollective.itaysonlab.jetisteam.repository.LibraryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import steam.clientcomm.CClientComm_GetAllClientLogonInfo_Response_Session
import javax.inject.Inject

class GetProfileLibrary @Inject constructor(
    private val libraryRepository: LibraryRepository,
    private val clientCommRepository: ClientCommRepository,
    private val sessionController: SteamSessionController
) {
    suspend operator fun invoke(steamId: SteamID): Library {
        return Library(
            games = libraryRepository.getLibrary(steamId).map(::OwnedGame),
            machines = if (sessionController.isMe(steamId)) {
                clientCommRepository.getActiveClients()
            } else emptyList()
        )
    }

    class Library(
        val games: List<OwnedGame>,
        val machines: List<CClientComm_GetAllClientLogonInfo_Response_Session>
    )
}