package com.bignardranch.beatbox.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.rules.RuleChain

fun viewModelTestingRules() = RuleChain.outerRule(InstantTaskExecutorRule())
