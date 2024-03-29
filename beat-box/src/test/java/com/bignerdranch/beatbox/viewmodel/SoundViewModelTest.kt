package com.bignerdranch.beatbox.viewmodel

import com.bignerdranch.beatbox.model.BeatBox
import com.bignerdranch.beatbox.model.Sound

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

    private fun createSoundViewModel(beatBox: BeatBox) =
        SoundViewModel(beatBox)

    private fun createSound(assetPath: String, soundId: Int? = null) =
        Sound(assetPath, soundId)

    @Before
    fun setUp() {
        beatBox = mock(BeatBox::class.java)
        sound   = createSound("assetPath")
        subject = createSoundViewModel(beatBox)
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