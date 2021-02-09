package com.tech.bazaar.template.di

import com.tech.bazaar.template.helper.storage.BazaarUserRepository
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@EntryPoint
@InstallIn(ApplicationComponent::class)
interface CustomEntryPoint {
    fun getBazaarUserRepository(): BazaarUserRepository
}