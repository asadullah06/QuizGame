package com.app.androidcodingchellange.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.androidcodingchellange.BaseActivity
import com.app.androidcodingchellange.data.models.Answers
import com.app.androidcodingchellange.data.models.QuizSchemaResponse
import com.app.androidcodingchellange.databinding.MainQuizActivityBinding
import com.app.androidcodingchellange.utils.ANSWER_TYPE_MULTI_CHOICE
import com.google.android.material.snackbar.Snackbar
import com.google.gson.JsonObject
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import org.json.JSONException
import org.json.JSONObject


@AndroidEntryPoint
class MainQuizActivity : BaseActivity() {
    private val TAG = "MainQuizActivity"
    private lateinit var binding: MainQuizActivityBinding
    private lateinit var answersListingAdapter: AnswersListingAdapter

    private val viewModel: MainQuizViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainQuizActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getQuestions()

        lifecycleScope.launchWhenStarted {
            viewModel.quizSchema.collect { events ->
                when (events) {
                    is MainQuizViewModel.QuizSchemaEvents.Success -> {
                        binding.pbLoading.isVisible = false
                        Log.i(TAG, events.result.toString())
                        createQuestionVew(questions = events.result)
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

    private fun createQuestionVew(questToPopIndex: Int = 0, questions: QuizSchemaResponse) {
        val questionObject = questions.questions[questToPopIndex]
        binding.textViewQuestion.text = questionObject.question

        if (questionObject.questionImageUrl != null) {
            binding.imageVewQuestion.isVisible = true
            Picasso.get().load(questionObject.questionImageUrl).into(binding.imageVewQuestion)
        } else {
            binding.imageVewQuestion.isVisible = false
        }
        if (questToPopIndex == 0)
            setTotalQuestionsCount(questions.questions.size)
        else
            updateQuestionsProgress(questToPopIndex + 1, questions.questions.size)

        val answers = questionObject.answers
        val answersList: ArrayList<Answers> = ArrayList()
        answers.forEach {
            val item = Answers(it.value, it.key)
            answersList.add(item)
        }
        val isAnswersTypeMultiSelect = questionObject.type == ANSWER_TYPE_MULTI_CHOICE
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
}