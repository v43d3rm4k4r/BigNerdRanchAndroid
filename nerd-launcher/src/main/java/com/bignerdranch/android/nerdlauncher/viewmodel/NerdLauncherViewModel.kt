package com.bignerdranch.android.nerdlauncher.viewmodel

import android.app.Activity
import android.content.pm.ResolveInfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bignerdranch.android.nerdlauncher.utils.showToast

class NerdLauncherViewModel(activities: List<ResolveInfo>) : ViewModel() {

    private val _activitiesLiveData: MutableLiveData<List<ResolveInfo>> = MutableLiveData(activities)
    val activitiesLiveData: LiveData<List<ResolveInfo>> = _activitiesLiveData
    val activitiesLiveDataValue = activitiesLiveData.value!!

    var appIndexToDelete = -1

    fun handleActivityUninstallActionResult(resultCode: Int): Boolean {
        val deleted = if (resultCode == Activity.RESULT_OK) {
            deleteActivity(appIndexToDelete)
            true
        } else {
            false
        }
        return deleted
    }

    private fun deleteActivity(position: Int) {
        _activitiesLiveData.value = activitiesLiveData.value!!.filterIndexed { index, _ ->
            position != index
        }
    }
}