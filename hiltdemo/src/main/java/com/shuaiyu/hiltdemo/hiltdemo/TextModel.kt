package com.shuaiyu.hiltdemo.hiltdemo

import com.shuaiyu.hiltdemo.hiltdemo.annotation.BindCai
import com.shuaiyu.hiltdemo.hiltdemo.annotation.BindHaha
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton
@Module
@InstallIn(ApplicationComponent::class)
abstract  class  TextModel {
    @Binds
    @BindCai
    @Singleton
    abstract fun showTextCai(showTextCai: ShowTextCai): IProcessor

    @Binds
    @BindHaha
    @Singleton
    abstract fun showTextHaha(showTextHaha: ShowTextHaha): IProcessor
}