/*
 * Copyright 2019 Square Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.squareup.workflow.testing

import com.squareup.workflow.WorkflowAction

/**
 * Result of a [RenderTester.render] call that can be used to verify that a [WorkflowAction] was
 * processed and perform assertions on that action.
 *
 * @see verifyAction
 * @see verifyActionResult
 */
interface RenderTestResult<StateT, OutputT> {

  /**
   * Asserts that the render pass handled either a workflow/worker output or a rendering event, and
   * passes the resulting [WorkflowAction] to [block] for asserting.
   *
   * If the workflow didn't process any actions, [block] will be passed [WorkflowAction.noAction].
   *
   * This is useful if your actions are a sealed class or enum. If you need to test an anonymous
   * action, use [verifyActionResult].
   */
  fun verifyAction(block: (WorkflowAction<StateT, OutputT>) -> Unit)

  /**
   * If the render pass handled either a workflow/worker output or a rendering event, "executes" the
   * action with the state passed to [renderTester], then invokes [block] with the resulting state
   * value.
   *
   * If the workflow didn't process any actions, `newState` will be the initial state.
   *
   * Note that by using this method, you're also testing the implementation of your action. This can
   * be useful if your actions are anonymous. If they are a sealed class or enum, use [verifyAction]
   * instead and write separate unit tests for your action implementations.
   */
  fun verifyActionState(block: (newState: StateT) -> Unit): RenderTestResult<StateT, OutputT>

  /**
   * If the render pass handled either a workflow/worker output or a rendering event, "executes" the
   * action with the state passed to [renderTester], verifies that the action set an output, then
   * invokes [block] with the resulting output value.
   *
   * If the workflow didn't process any actions, or no output was set, an [AssertionError] will be
   * thrown.
   *
   * Note that by using this method, you're also testing the implementation of your action. This can
   * be useful if your actions are anonymous. If they are a sealed class or enum, use [verifyAction]
   * instead and write separate unit tests for your action implementations.
   */
  fun verifyActionOutput(block: (output: OutputT) -> Unit): RenderTestResult<StateT, OutputT>

  /**
   * If the render pass handled either a workflow/worker output or a rendering event, "executes" the
   * action with the state passed to [renderTester], and then verifies that the action did not set
   * any output.
   *
   * If the workflow didn't process any actions, this method will do nothing.
   *
   * Note that by using this method, you're also testing the implementation of your action. This can
   * be useful if your actions are anonymous. If they are a sealed class or enum, use [verifyAction]
   * instead and write separate unit tests for your action implementations.
   */
  fun verifyNoActionOutput(): RenderTestResult<StateT, OutputT>

  /**
   * Asserts that the render pass handled either a workflow/worker output or a rendering event,
   * "executes" the action with the state passed to [renderTester], then invokes [block] with the
   * resulting state and output values.
   *
   * If the workflow didn't process any actions, `newState` will be the initial state and `output`
   * will be null.
   *
   * Note that by using this method, you're also testing the implementation of your action. This can
   * be useful if your actions are anonymous. If they are a sealed class or enum, use [verifyAction]
   * instead and write separate unit tests for your action implementations.
   *
   * Note that if [OutputT] is nullable, this method does not distinguish between an no output and
   * null output. Use [RenderTestResult.verifyActionOutput] and
   * [RenderTestResult.verifyNoActionOutput] instead.
   */
  @Deprecated("Use verifyActionState and verify(No)ActionOutput")
  fun verifyActionResult(block: (newState: StateT, output: OutputT?) -> Unit) {
    var state: StateT? = null
    var output: OutputT? = null
    verifyActionState { state = it }
    try {
      verifyActionOutput { output = it }
    } catch (e: AssertionError) {
      // No output was set, leave as null.
    }

    @Suppress("UNCHECKED_CAST")
    block(state as StateT, output)
  }
}
