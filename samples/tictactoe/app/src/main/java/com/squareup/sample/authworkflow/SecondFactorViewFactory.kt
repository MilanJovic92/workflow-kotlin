package com.squareup.sample.authworkflow

import com.squareup.sample.tictactoe.databinding.SecondFactorLayoutBinding
import com.squareup.workflow1.ui.ScreenViewFactory
import com.squareup.workflow1.ui.ScreenViewRunner.Companion.bind
import com.squareup.workflow1.ui.WorkflowUiExperimentalApi
import com.squareup.workflow1.ui.backPressedHandler

@OptIn(WorkflowUiExperimentalApi::class)
internal val SecondFactorViewFactory: ScreenViewFactory<SecondFactorScreen> =
  bind(SecondFactorLayoutBinding::inflate) { rendering, _ ->
    root.backPressedHandler = { rendering.onCancel() }
    secondFactorToolbar.setNavigationOnClickListener { rendering.onCancel() }

    secondFactorErrorMessage.text = rendering.errorMessage

    secondFactorSubmitButton.setOnClickListener {
      rendering.onSubmit(secondFactor.text.toString())
    }
  }
