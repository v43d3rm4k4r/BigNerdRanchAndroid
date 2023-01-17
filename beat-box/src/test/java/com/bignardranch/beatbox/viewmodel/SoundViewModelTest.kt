package com.bignardranch.beatbox.viewmodel

import com.bignardranch.beatbox.model.BeatBox
import com.bignardranch.beatbox.model.Sound

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`

import org.junit.Before
import org.junit.Rule
import org.junit.Test

import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

internal class SoundViewModelTest {

    @get:Rule
    val viewModelRule = viewModelTestingRules()

    private lateinit var beatBox: BeatBox
    private lateinit var sound: Sound
    private lateinit var subject: SoundViewModel

    @Before
    fun setUp() {
        beatBox = mock(BeatBox::class.java)
        sound = Sound("assetPath")
        subject = SoundViewModel(beatBox)
        //subject.sound = sound
    }

//    @Test
//    fun exposesSoundNameAsTitle() =
//        assertThat(subject.title.value, `is`(sound.name))

    @Test
    fun `calls beatBoxPlayOnButtonClicked() method`() {
        subject.onSoundClicked(sound)

        verify(beatBox).play(sound)
    }
}