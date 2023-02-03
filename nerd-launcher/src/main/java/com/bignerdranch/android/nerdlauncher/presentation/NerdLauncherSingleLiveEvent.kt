package com.bignerdranch.android.nerdlauncher.presentation

import android.content.pm.ResolveInfo

sealed class NerdLauncherSingleLiveEvent {

    class ShowDeleteDialog(val resolveInfo: ResolveInfo) : NerdLauncherSingleLiveEvent()

    object ShowSuccessDeletingApp : NerdLauncherSingleLiveEvent()

    class ShowErrorDeletingApp(val itemIdx: Int) : NerdLauncherSingleLiveEvent()

    class ShowActivity(val resolveInfo: ResolveInfo): NerdLauncherSingleLiveEvent()
}
