package com.example.playlistmaker.domain.settings.impl

import com.example.playlistmaker.domain.settings.api.SharingInteractor
import com.example.playlistmaker.domain.settings.api.SharingRepository

class SharingInteractorImpl(private val sharingRepository: SharingRepository): SharingInteractor {
    override fun shareButton() {
        sharingRepository.shareButton()
    }

    override fun supportWrite() {
        sharingRepository.supportWrite()
    }

    override fun agreementUser() {
        sharingRepository.agreementUser()
    }

}