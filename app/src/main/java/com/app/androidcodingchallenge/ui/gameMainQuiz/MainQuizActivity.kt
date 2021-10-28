package com.app.androidcodingchallenge.ui.gameMainQuiz

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.androidcodingchallenge.BaseActivity
import com.app.androidcodingchallenge.R
import com.app.androidcodingchallenge.data.models.Answers
import com.app.androidcodingchallenge.data.models.Question
import com.app.androidcodingchallenge.databinding.MainQuizActivityBinding
import com.app.androidcodingchallenge.utils.ANSWER_TYPE_MULTI_CHOICE
import com.app.androidcodingchallenge.utils.CommonMethods
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect


@AndroidEntryPoint
class MainQuizActivity : BaseActivity(), View.OnClickListener {
    private val TAG = "MainQuizActivity"
    private lateinit var binding: MainQuizActivityBinding
    private lateinit var answersListingAdapter: AnswersListingAdapter

    private val viewModel: MainQuizViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainQuizActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.getQuestions()
        collectResponseEvents()
        binding.btnCheck.setOnClickListener(this)
        collectQTimerEvents()
        collectATimerEvents()
    }

    private fun createQuestionVew(
        questToPopIndex: Int,
        totalQCount: Int,
        questionObject: Question
    ) {
        binding.textViewQuestion.text = questionObject.question

        if (questionObject.questionImageUrl.isNullOrEmpty().not()) {
            binding.imageVewQuestion.isVisible = true
            Picasso.get().load(questionObject.questionImageUrl).into(binding.imageVewQuestion)
        } else {
            binding.imageVewQuestion.isVisible = false
        }
        if (questToPopIndex == 0)
            setTotalQuestionsCount(totalQCount)
        else
            updateQuestionsProgress(questToPopIndex + 1, totalQCount)

        val answers = questionObject.answers
        val answersList: ArrayList<Answers> = ArrayList()
        answers.forEach {
            val item = Answers(it.value, it.key)
            answersList.add(item)
        }
        val isAnswersTypeMultiSelect = (questionObject.type == ANSWER_TYPE_MULTI_CHOICE) ||
                (questionObject.correctAnswer.split(",").size > 1)
        answersListingAdapter = AnswersListingAdapter(isAnswersTypeMultiSelect, answersList)
        binding.answersRecyclerview.adapter = answersListingAdapter
        binding.answersRecyclerview.layoutManager = LinearLayoutManager(this)
    }

    private fun setTotalQuestionsCount(totalQCount: Int) {
        binding.textViewQuestionNo.text = createQuestionsCountString(totalQCount = totalQCount)
        binding.progressBar.max = totalQCount
        binding.progressBar.progress = 1
    }

    private fun updateQuestionsProgress(populatedQCount: Int, totalQCount: Int) {
        binding.textViewQuestionNo.text = createQuestionsCountString(populatedQCount, totalQCount)
        binding.progressBar.max = totalQCount
        binding.progressBar.progress = populatedQCount
    }

    private fun createQuestionsCountString(populatedQCount: Int = 1, totalQCount: Int): String {
        return StringBuilder().append("Question ").append(populatedQCount).append(" of ")
            .append(totalQCount).toString()
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btn_check -> {
                if (binding.btnCheck.text == getString(R.string.check)) {
                    var canCheckAnswer = false
                    run loop@{
                        answersListingAdapter.answerOptionsList.forEach {
                            if (it.isOptionSelected) {
                                canCheckAnswer = true
                                return@loop
                            }
                        }
                    }
                    if (canCheckAnswer) {
                        viewModel.checkIsAnswerCorrect()
                        binding.btnCheck.text = getString(R.string.next)
                    } else {
                        Snackbar.make(
                            binding.root,
                            "Must select an option to check",
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                } else
                    viewModel.loadNextQuestion()
            }
        }
    }

    private fun collectResponseEvents() {
        lifecycleScope.launchWhenStarted {
            viewModel.quizSchema.collect { events ->
                when (events) {
                    is MainQuizViewModel.QuizSchemaEvents.Success -> {
                        binding.pbLoading.isVisible = false
                        Log.i(TAG, events.questionObject.toString())
                        createQuestionVew(
                            events.questionToPopulateIndex,
                            events.totalQuestions,
                            events.questionObject
                        )
                        binding.btnCheck.text = getString(R.string.check)

                        // Reset the answer result text
                        binding.textViewAnswerResult.text = ""

                        //Below methods are called to stop answer timer if on and start question time
                        viewModel.stopAnswerCountDown()
                        viewModel.startQuestionCountDown()
                    }
                    is MainQuizViewModel.QuizSchemaEvents.CheckIsAnswerCorrect -> {
                        Log.i(TAG, "is answer correct checking")
                        answersListingAdapter.setCorrectAnswer(events.questionObject.correctAnswer)
                        answersListingAdapter.clearItemsClick()

                        // After updating in the adapter about answer correctness below method will update the message as well
                        checkIsAnswersAreCorrect(events.questionObject.score)

                        //Below methods are called to stop question and start answer time
                        viewModel.stopQuestionCountDown()
                        viewModel.startAnswerCountDown()
                    }
                    is MainQuizViewModel.QuizSchemaEvents.Failure -> {
                        binding.pbLoading.isVisible = false
                        Snackbar.make(binding.root, events.errorText, Snackbar.LENGTH_LONG).show()
                        Log.e(TAG, events.errorText)
                    }
                    is MainQuizViewModel.QuizSchemaEvents.Loading -> {
                        binding.pbLoading.isVisible = true
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun collectQTimerEvents() {
        lifecycleScope.launchWhenStarted {
            viewModel.questionTimerStateFlow.collect { events ->
                when (events) {
                    is MainQuizViewModel.TimerEvents.OnStarted -> {
                        Log.i(
                            TAG,
                            "Question count down started ${CommonMethods.formatToDigitalClock(events.totalTime)}"
                        )
                        binding.textViewTimer.text =
                            CommonMethods.formatToDigitalClock(events.totalTime)
                    }
                    is MainQuizViewModel.TimerEvents.OnTick -> {
                        Log.i(
                            TAG,
                            "Question count down ${CommonMethods.formatToDigitalClock(events.long)}"
                        )
                        binding.textViewTimer.text = CommonMethods.formatToDigitalClock(events.long)
                    }
                    is MainQuizViewModel.TimerEvents.OnFinished -> {
                        Log.i(TAG, "QuestionTimerFinished")
                        viewModel.checkIsAnswerCorrect()
                        viewModel.startAnswerCountDown()
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun collectATimerEvents() {
        lifecycleScope.launchWhenStarted {
            viewModel.answerTimerStateFlow.collect { events ->
                when (events) {
                    is MainQuizViewModel.TimerEvents.OnStarted -> {
                        Log.i(
                            TAG,
                            "answer count down started ${CommonMethods.formatToDigitalClock(events.totalTime)}"
                        )
                        binding.textViewTimer.text =
                            CommonMethods.formatToDigitalClock(events.totalTime)
                    }
                    is MainQuizViewModel.TimerEvents.OnTick -> {
                        Log.i(
                            TAG,
                            "answer count down ${CommonMethods.formatToDigitalClock(events.long)}"
                        )
                        binding.textViewTimer.text = CommonMethods.formatToDigitalClock(events.long)
                    }
                    is MainQuizViewModel.TimerEvents.OnFinished -> {
                        Log.i(TAG, "AnswerTimerFinished")
                        viewModel.loadNextQuestion()
                    }
                    else -> Unit
                }
            }
        }
    }

    /**
     * Method  will check the that all the selected options are correct or not  and after update the message accordingly.
     */
    private fun checkIsAnswersAreCorrect(questionScore: Int) {
        var answerIsCorrect = true
        run loop@{
            answersListingAdapter.answerOptionsList.forEach {
                if (it.isOptionSelected.not() && it.isCorrectOption) {
                    answerIsCorrect = false
                    return@loop
                }
            }
        }

        if (answerIsCorrect) {
            updateAnswerResults("Your answer is Correct!", 1)

            //below method will update the current total score as answer of the question is correct.
            viewModel.updateCurrentGameScore(questionScore)
        } else {
            updateAnswerResults("Your answer is Incorrect!", 2)
        }
    }

    /**
     * @param message the message that is going to be shown to user
     * @param resultType shows that is the result is correct or incorrect and
     *  1 = correct
     *  2 = incorrect
     */
    private fun updateAnswerResults(message: String, resultType: Int) {
        binding.textViewAnswerResult.text = message
        if (resultType == 1) {
            binding.textViewAnswerResult.setTextColor(Color.GREEN)
        } else if (resultType == 2) {
            binding.textViewAnswerResult.setTextColor(Color.RED)
        }

    }
}