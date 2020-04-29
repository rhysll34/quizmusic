package com.example.quizmusic

import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.view.View.*
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_fullscreen.*

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class QuizMusicActivity : AppCompatActivity(), QuizQuestionAdapter.ClipPlayer {
    private var mediaPlayer: MediaPlayer? = null
    private var answerMode = true
    private var currentPlayingAudio = 0
    private var mVisible: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_fullscreen)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mVisible = true

        qmMainLayout.systemUiVisibility = SYSTEM_UI_FLAG_IMMERSIVE or SYSTEM_UI_FLAG_FULLSCREEN or
            SYSTEM_UI_FLAG_HIDE_NAVIGATION

        val questions: List<Question> = listOf(
            Question(R.raw.question_1, R.string.answer_1),
            Question(R.raw.question_2, R.string.answer_2)//,
//            Question(R.raw.question_3, R.string.answer_3),
//            Question(R.raw.question_4, R.string.answer_4),
//            Question(R.raw.question_5, R.string.answer_5),
//            Question(R.raw.question_6, R.string.answer_6),
//            Question(R.raw.question_7, R.string.answer_7),
//            Question(R.raw.question_8, R.string.answer_8),
//            Question(R.raw.question_9, R.string.answer_9),
//            Question(R.raw.question_10, R.string.answer_10)
        )

        val questionAdapter = QuizQuestionAdapter(questions, this)
        qmQuestionList.layoutManager = GridLayoutManager(this, 2)
        qmQuestionList.adapter = questionAdapter
    }

    override fun playClip(audioResourceID: Int, position: Int, questionViewID: Int, answerViewID: Int) {
        val holder = qmQuestionList.layoutManager?.findViewByPosition(position)
        val questionView = holder?.findViewById<Button>(questionViewID)
        val answerView = holder?.findViewById<TextView>(answerViewID)

        mediaPlayer?.let{
            if(it.isPlaying) {
                it.stop()
                if(currentPlayingAudio > -1) {
                    val oldHolder = qmQuestionList.layoutManager?.findViewByPosition(currentPlayingAudio)
                    val oldQuestionView = oldHolder?.findViewById<Button>(questionViewID)
                    oldQuestionView?.setBackgroundColor(Color.BLACK)
                }
            }
            it.release()
        }

        mediaPlayer = MediaPlayer.create(this, audioResourceID)
        mediaPlayer?.setOnPreparedListener {
            mediaPlayer -> mediaPlayer.start()
            currentPlayingAudio = position

            if(answerMode) {
                answerView?.visibility = View.VISIBLE
            }

            questionView?.setBackgroundColor(Color.YELLOW)
        }

        mediaPlayer?.setOnCompletionListener {
            questionView?.setBackgroundColor(Color.BLACK)
        }

        mediaPlayer?.setOnErrorListener { _: MediaPlayer, _: Int, _: Int ->
            questionView?.setBackgroundColor(Color.BLACK)
            true
        }
    }
}
