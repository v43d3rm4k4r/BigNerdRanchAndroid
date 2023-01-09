package com.bignardranch.beatbox.viewmodel

import com.bignardranch.beatbox.model.BeatBox
import com.bignardranch.beatbox.model.Sound
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock

internal class SoundViewModelTest {

    private lateinit var beatBox: BeatBox
    private lateinit var sound: Sound
    private lateinit var subject: SoundViewModel

    @Before
    fun setUp() {
        beatBox = mock(BeatBox::class.java)
        sound = Sound("assetPath")
        subject = SoundViewModel()
        subject.sound = sound /// FIXME
    }

    @Test
    fun exposesSoundNameAsTitle() =
        assertThat(subject.title.value, `is`(sound.name))
}