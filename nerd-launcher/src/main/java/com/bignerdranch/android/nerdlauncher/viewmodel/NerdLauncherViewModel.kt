package com.bignerdranch.android.nerdlauncher.viewmodel

import android.content.pm.ResolveInfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NerdLauncherViewModel(activities: MutableList<ResolveInfo>) : ViewModel() {

    private val _activitiesLiveData: LiveData<MutableList<ResolveInfo>> = MutableLiveData(activities)
    val activitiesLiveData: LiveData<MutableList<ResolveInfo>> = _activitiesLiveData

    fun deleteActivity(position: Int) = _activitiesLiveData.value!!.removeAt(position)
}