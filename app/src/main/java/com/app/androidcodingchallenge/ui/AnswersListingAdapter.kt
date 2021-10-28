package com.app.androidcodingchellange.ui

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.RadioGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.app.androidcodingchellange.R
import com.app.androidcodingchellange.data.models.Answers


class AnswersListingAdapter(
    private val isAnswersTypeMultiSelect: Boolean,
    var answerOptionsList: List<Answers>,
) :
    RecyclerView.Adapter<AnswersListingAdapter.ViewHolder>() {
    val TAG = "AnswersListingAdapter"
    var checkAnswerFlag = false
    private var clearItemsClicksOnFlag = false

    fun clearItemsClick() {
        clearItemsClicksOnFlag = true
        notifyDataSetChanged()
    }

    fun setCorrectAnswer(correctAnswer: String) {
        val correctAnswersArray = correctAnswer.split(",")
        if (correctAnswersArray.size > 1) {
            answerOptionsList.forEach {
                if (correctAnswersArray.contains(it.optionKey)) {
                    it.isCorrectOption = true
                }
            }
        } else {
            answerOptionsList.forEach {
                if (correctAnswer == it.optionKey) {
                    it.isCorrectOption = true
                }
            }
        }
        checkAnswerFlag = true
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.multi_list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val answer = answerOptionsList[position]
        holder.checkBox.isVisible = isAnswersTypeMultiSelect
        holder.checkBox.isChecked = answer.isOptionSelected
        holder.textView.text = answer.option
        holder.checkBox.isEnabled =false

        if (isAnswersTypeMultiSelect.not()) {
            if (answer.isOptionSelected) {
                holder.parentLayout.setBackgroundColor(Color.GRAY)
                holder.textView.setTextColor(Color.GREEN)
            } else {
                holder.parentLayout.setBackgroundColor(Color.WHITE)
                holder.textView.setTextColor(Color.BLACK)
            }
        }
        if (checkAnswerFlag) {
            when (getBackgroundColorCode(answer)) {
                1 -> {
                    holder.parentLayout.setBackgroundColor(Color.GREEN)
                    holder.textView.setTextColor(Color.WHITE)
                }
                2 -> {
                    holder.parentLayout.setBackgroundColor(Color.RED)
                    holder.textView.setTextColor(Color.WHITE)
                }
                3 -> {
                    holder.parentLayout.setBackgroundColor(Color.WHITE)
                    holder.textView.setTextColor(Color.BLACK)
                }
            }
        }
        if (clearItemsClicksOnFlag) {
            holder.parentLayout.setOnClickListener(null)
        } else {
            holder.parentLayout.setOnClickListener {
                if (!isAnswersTypeMultiSelect) {
                    for (i in answerOptionsList.indices) {
                        if (answerOptionsList[i].isOptionSelected) {
                            answerOptionsList[i].isOptionSelected = false
                            notifyItemChanged(i)
                        }
                    }
                    Log.i(TAG, "Options selections are reset")
                }
                answer.isOptionSelected = true
                notifyItemChanged(position)
            }
        }
    }

    override fun getItemCount(): Int {
        return answerOptionsList.size
    }


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val checkBox: CheckBox = view.findViewById(R.id.check_box_answer)
        val textView: TextView = view.findViewById(R.id.text_view_answer)
        val parentLayout: ConstraintLayout = view.findViewById(R.id.parent_layout)

    }

    private fun getBackgroundColorCode(item: Answers): Int {
        return if ((item.isOptionSelected && item.isCorrectOption) || item.isCorrectOption) {  // green
            1
        } else if (item.isOptionSelected) {  // red
            2
        } else { // white
            3
        }
    }

    private fun setItemViewColors() {

    }
}