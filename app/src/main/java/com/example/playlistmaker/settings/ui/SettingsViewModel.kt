package com.example.playlistmaker.settings.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.mapTheme.domain.GetMapThemeUseCase
import com.example.playlistmaker.mapTheme.domain.SetMapThemeUseCase
import com.example.playlistmaker.mapTheme.domain.models.MapTheme

class SettingsViewModel(
    private val getMapThemeUseCase: GetMapThemeUseCase,
    private val setMapThemeUseCase: SetMapThemeUseCase
) : ViewModel() {


    private val viewState = MutableLiveData(ViewState(switchTheme = false, action = null))

    fun observeViewState(): LiveData<ViewState> = viewState

    init {
        gepMapTheme()
    }

    private fun gepMapTheme() {
        viewState.value = viewState.value?.copy(switchTheme = getMapThemeUseCase().darkTheme, action = null)
    }

    fun sendEmail(){
        viewState.value = viewState.value?.copy(action = ActionView.SEND_EMAIL)
    }

    fun openPracticumLink(){
        viewState.value = viewState.value?.copy(action = ActionView.OPEN_PRACTICUM_LINK)
    }

    fun openOfferLink(){
        viewState.value = viewState.value?.copy(action = ActionView.OPEN_OFFER_LINK)
    }

    fun switchTheme(checked : Boolean){
        val mapTheme = MapTheme(checked)
        setMapThemeUseCase.invoke(mapTheme)
        gepMapTheme()
    }
}

data class ViewState(
    val switchTheme: Boolean,
    val action: ActionView?
)

enum class ActionView{
    SEND_EMAIL,
    OPEN_PRACTICUM_LINK,
    OPEN_OFFER_LINK
}
