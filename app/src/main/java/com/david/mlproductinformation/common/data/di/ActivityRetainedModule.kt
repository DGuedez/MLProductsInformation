package com.david.mlproductinformation.common.data.di

import com.david.mlproductinformation.common.data.ProductInfoRepository
import com.david.mlproductinformation.common.data.preferences.Preferences
import com.david.mlproductinformation.common.data.preferences.ProductInfoPreferences
import com.david.mlproductinformation.common.domain.repositories.ProductRepository
import com.david.mlproductinformation.common.utils.CoroutineDispatcherProvider
import com.david.mlproductinformation.common.utils.DispatchersProvider
import dagger.Binds
import dagger.Module
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