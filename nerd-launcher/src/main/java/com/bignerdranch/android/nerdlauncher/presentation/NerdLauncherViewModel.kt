package com.bignerdranch.android.nerdlauncher.presentation

import android.app.Activity
import android.content.pm.ResolveInfo
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bignerdranch.android.nerdlauncher.NotNullableMutableLiveData
import com.bignerdranch.android.nerdlauncher.presentation.NerdLauncherSingleLiveEvent.ShowActivity
import com.bignerdranch.android.nerdlauncher.presentation.NerdLauncherSingleLiveEvent.ShowDeleteDialog
import com.bignerdranch.android.nerdlauncher.presentation.NerdLauncherSingleLiveEvent.ShowErrorDeletingApp
import com.bignerdranch.android.nerdlauncher.presentation.NerdLauncherSingleLiveEvent.ShowSuccessDeletingApp
import com.bignerdranch.android.nerdlauncher.utils.SingleLiveEvent

class NerdLauncherViewModel(activities: List<ResolveInfo>) : ViewModel() {

    private val _state: NotNullableMutableLiveData<List<ResolveInfo>> = NotNullableMutableLiveData(activities)

    val state: LiveData<List<ResolveInfo>> = _state
    val events = SingleLiveEvent<NerdLauncherSingleLiveEvent>()

    private var itemToDelete: ResolveInfo? = null

    fun handleActivityUninstallActionResult(resultCode: Int) {
        if (resultCode == Activity.RESULT_OK) {
            val newList = _state.value.toMutableList().apply { remove(itemToDelete) }
            events.postValue(ShowSuccessDeletingApp)
            _state.postValue(newList)
        } else {
            events.postValue(ShowErrorDeletingApp(_state.value.indexOf(itemToDelete)))
        }
    }

    fun onItemDelete(item: ResolveInfo) {
        itemToDelete = item
        events.postValue(ShowDeleteDialog(item))
    }

    fun onItemClick(resolveInfo: ResolveInfo) {
        events.postValue(ShowActivity(resolveInfo))
    }
}
