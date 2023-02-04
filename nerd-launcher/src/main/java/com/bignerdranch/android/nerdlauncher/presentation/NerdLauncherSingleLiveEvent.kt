package com.bignerdranch.android.nerdlauncher.presentation

import android.content.pm.ResolveInfo

sealed class NerdLauncherSingleLiveEvent {

    class ShowDeleteDialog(val resolveInfo: ResolveInfo) : NerdLauncherSingleLiveEvent()

    object ShowDeletingAppSuccess : NerdLauncherSingleLiveEvent()

    class ShowDeletingAppError(val itemIdx: Int) : NerdLauncherSingleLiveEvent()

    class ShowActivity(val resolveInfo: ResolveInfo) : NerdLauncherSingleLiveEvent()
}