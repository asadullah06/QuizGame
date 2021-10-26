package com.app.androidcodingchellange.ui

import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.text.HtmlCompat
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_LEGACY
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.app.androidcodingchellange.R
import com.app.androidcodingchellange.data.models.Answers


class AnswersListingAdapter(
    val isAnswersTypeMultiSelect: Boolean,
    var answerOptionsList: List<Answers>,
) :
    RecyclerView.Adapter<AnswersListingAdapter.ViewHolder>() {


    private var correctAnswer: String = ""
        get() = field
        set(value) {
            field = value
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
    }

    override fun getItemCount(): Int {
        return answerOptionsList.size
    }


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val checkBox: CheckBox = view.findViewById(R.id.check_box_answer)
        val textView: TextView = view.findViewById(R.id.text_view_answer)
        val parentLayout: CardView = view.findViewById(R.id.parent_layout)
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
}