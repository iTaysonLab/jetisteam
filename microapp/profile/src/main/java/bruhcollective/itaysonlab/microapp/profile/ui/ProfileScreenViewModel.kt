package bruhcollective.itaysonlab.microapp.profile.ui

import bruhcollective.itaysonlab.jetisteam.uikit.vm.PageViewModel
import bruhcollective.itaysonlab.jetisteam.usecases.GetProfileData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class ProfileScreenViewModel @Inject constructor(
    private val getProfileData: GetProfileData
): PageViewModel<GetProfileData.ProfileData>() {
    init { reload() }
    override suspend fun load() = getProfileData()
}