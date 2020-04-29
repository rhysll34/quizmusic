package com.example.quizmusic

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.element_question.*
import kotlinx.android.synthetic.main.element_question.view.*

class QuizQuestionAdapter(private val questions: List<Question>, private val clipPlayer: ClipPlayer) :
    RecyclerView.Adapter<QuizQuestionAdapter.ViewHolder>()
{
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        LayoutContainer {
        val questionButton : Button = itemView.eq_button
        val answerText : TextView = itemView.eq_answer
        override val containerView: View?
            get() = itemView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val questionLayout = LayoutInflater.from(parent.context).inflate(
            R.layout.element_question, parent, false)
        return ViewHolder(questionLayout)
    }

    override fun getItemCount(): Int {
        return questions.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.answerText.text =
            holder.answerText.context.resources.getString(questions[position].answerStringResourceID)

        holder.questionButton.setOnClickListener {
            clipPlayer.playClip(questions[holder.adapterPosition].audioResourceID,
                holder.adapterPosition,
                holder.questionButton.id, holder.answerText.id) }

        val questionNumber = position + 1
        holder.questionButton.text = "Question $questionNumber"
    }

    interface ClipPlayer {
        fun playClip(audioResourceID: Int, position: Int, questionViewID: Int, answerViewID: Int)
    }
}
