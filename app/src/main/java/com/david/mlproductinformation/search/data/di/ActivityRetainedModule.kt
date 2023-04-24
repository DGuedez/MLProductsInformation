package com.david.mlproductinformation.search.data.di

import android.app.Application
import android.content.Context
import com.david.mlproductinformation.common.utils.CoroutineDispatcherProvider
import com.david.mlproductinformation.common.utils.DispatchersProvider
import com.david.mlproductinformation.search.data.ProductInfoRepository
import com.david.mlproductinformation.search.data.preferences.Preferences
import com.david.mlproductinformation.search.data.preferences.ProductInfoPreferences
import com.david.mlproductinformation.search.domain.repositories.ProductRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class ActivityRetainedModule {

    @Binds
    @ActivityRetainedScoped
    abstract fun bindProductRepository(repository: ProductInfoRepository): ProductRepository

    @Binds
    @ActivityRetainedScoped
    abstract fun bindPreferences(preferences: ProductInfoPreferences): Preferences


    @Binds
    abstract fun bindDispatchersProvider(dispatchersProvider: CoroutineDispatcherProvider):
            DispatchersProvider
}