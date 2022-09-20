package com.example.smartquiz

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.makeramen.roundedimageview.RoundedImageView
import com.squareup.picasso.Picasso
import org.json.JSONObject
import org.w3c.dom.Text
import java.lang.Exception

class QuestionActivity : AppCompatActivity(), View.OnClickListener {

    private var currentPosition = 1
    private var wrongOption = 0
    private var selectedOption: Int = 0
    private var totalScore: Int = 0
    private lateinit var name: String

    private var tvQuestion: TextView? = null
    private var ivQuestion: RoundedImageView? = null
    private var progressBar: ProgressBar? = null
    private var tvProgress: TextView? = null

    private var tvOptionOne: TextView? = null
    private var tvOptionTwo: TextView? = null
    private var tvOptionThree: TextView? = null
    private var tvOptionFour: TextView? = null
    private var btnSubmit: Button? = null

    private var questionsList: ArrayList<Question>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question)

        tvQuestion = findViewById(R.id.tvQuestion)
        ivQuestion = findViewById(R.id.ivQuestion)
        progressBar = findViewById(R.id.progressBar)
        tvProgress = findViewById(R.id.tvProgress)
        tvOptionFour = findViewById(R.id.tvOptionFour)
        tvOptionOne = findViewById(R.id.tvOptionOne)
        tvOptionThree = findViewById(R.id.tvOptionThree)
        tvOptionTwo = findViewById(R.id.tvOptionTwo)
        btnSubmit = findViewById(R.id.btnSubmit)

        questionsList = ArrayList()

        //to make options clickable
        tvOptionOne?.setOnClickListener(this)
        tvOptionTwo?.setOnClickListener(this)
        tvOptionThree?.setOnClickListener(this)
        tvOptionFour?.setOnClickListener(this)
        btnSubmit?.setOnClickListener(this)

        getQuestionsFromServer()

        name = intent.getStringExtra("name").toString()

    }

    private fun getQuestionsFromServer() {

        val fQuestionsList = ArrayList<Question>()

        val url = "http://smashdevelopers.tk/smartquiz/question.php"

        val request: StringRequest =
            StringRequest(Request.Method.GET, url, Response.Listener { response ->

                try {

                    val jsonResponse = JSONObject(response)
                    var status = jsonResponse.getInt("status")

                    if (status == 1) {

                        var jsonArray = jsonResponse.getJSONArray("question")
                        for (i in 0..jsonArray.length() - 1) {

                            val jsonObject = jsonArray.getJSONObject(i)
                            val id = jsonObject.getInt("id")
                            val question = jsonObject.getString("question")
                            val image = jsonObject.getString("image")
                            val optionOne = jsonObject.getString("optionOne")
                            val optionTwo = jsonObject.getString("optionTwo")
                            val optionThree = jsonObject.getString("optionThree")
                            val optionFour = jsonObject.getString("optionFour")
                            val correctOption = jsonObject.getInt("correctOption")

                            fQuestionsList.add(
                                Question(
                                    id, question, image, optionOne,
                                    optionTwo, optionThree, optionFour, correctOption
                                )
                            )

                        }
                        //function which sets the all questions defined below
                        /*totalScore = fQuestionsList!!.size
                        Log.i("mytag", totalScore.toString())*/
                        setQuestionsToDisplay(fQuestionsList)

                    } else {
                        Toast.makeText(this, "JSON is empty", Toast.LENGTH_SHORT).show()
                    }


                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(this, "Error while parsing data from server", Toast.LENGTH_SHORT)
                        .show()
                }

            }, Response.ErrorListener { volleyError ->

                volleyError.printStackTrace()
                Toast.makeText(
                    this,
                    "Unable to get response, Please check your internet connection and try again.",
                    Toast.LENGTH_SHORT
                )
                    .show()
            })
        //asynchronous call || this request runs in another background thread
        Volley.newRequestQueue(this).add(request)
    }

    private fun setQuestionsToDisplay(list: ArrayList<Question>) {

        defaultOptionView()
        questionsList = list

        val questionObject: Question = questionsList!!.get(currentPosition - 1)

        tvQuestion?.text = questionObject.question
        tvOptionOne?.text = questionObject.optionOne
        tvOptionTwo?.text = questionObject.optionTwo
        tvOptionThree?.text = questionObject.optionThree
        tvOptionFour?.text = questionObject.optionFour

        progressBar?.progress = currentPosition
        tvProgress?.text = "${currentPosition}/${progressBar?.max}"
        Picasso.get().load(questionObject.image).placeholder(R.drawable.placeholder)
            .into(ivQuestion)

        if (currentPosition == questionsList!!.size) {
            btnSubmit?.text = "FINISH"
        }

    }

    private fun defaultOptionView() {

        val options = ArrayList<TextView>()

        tvOptionOne?.let {
            options.add(0, it)
        }
        tvOptionTwo?.let {
            options.add(1, it)
        }
        tvOptionThree?.let {
            options.add(2, it)
        }
        tvOptionFour?.let {
            options.add(3, it)
        }

        for (op in options) {
            op.setTextColor(Color.parseColor("#8F8F8F"))
            op.background = ContextCompat.getDrawable(this, R.drawable.default_option_bg)
            op.typeface = Typeface.DEFAULT
        }
        btnSubmit?.text = "SUBMIT"
    }

    //is ka kam hy selected option ka color change karna
    private fun selectedOptionView(optionSelected: TextView) {

        //selectedOption = selectedPosition

        optionSelected.setTextColor(Color.parseColor("#434343"))
        optionSelected.background = ContextCompat.getDrawable(this, R.drawable.selected_option_bg)
        optionSelected.typeface = Typeface.DEFAULT_BOLD
    }

    //is ka kam hy answer jo pass hua hy us ka color set karna red or green jo pass hua hy
    private fun answerView(
        answer: Int,
        drawbleView: Int,
        color: String,
        typeface: Typeface,
        errorControl: String
    ) {


        val clr = color
        when (answer) {

            1 -> {
                tvOptionOne?.background = ContextCompat.getDrawable(this, drawbleView)
                tvOptionOne?.setTextColor(Color.parseColor(clr))
                tvOptionOne?.setTypeface(typeface)
                if (errorControl == "error") {
                    tvOptionOne?.error = "Error"
                }
            }
            2 -> {
                tvOptionTwo?.background = ContextCompat.getDrawable(this, drawbleView)
                tvOptionTwo?.setTextColor(Color.parseColor(clr))
                tvOptionTwo?.setTypeface(typeface)
                if (errorControl == "error") {
                    tvOptionTwo?.error = "error"
                }
            }
            3 -> {
                tvOptionThree?.background = ContextCompat.getDrawable(this, drawbleView)
                tvOptionThree?.setTextColor(Color.parseColor(clr))
                tvOptionThree?.setTypeface(typeface)
                if (errorControl == "error") {
                    tvOptionThree?.error = "error"
                }
            }
            4 -> {
                tvOptionFour?.background = ContextCompat.getDrawable(this, drawbleView)
                tvOptionFour?.setTextColor(Color.parseColor(clr))
                tvOptionFour?.setTypeface(typeface)
                if (errorControl == "error") {
                    tvOptionFour?.error = "error"
                }
            }
        }

    }

    //this function made by implementing methods after adding onclick listner to main class
    override fun onClick(view: View?) {

        defaultOptionView()
        when (view?.id) {

            R.id.tvOptionOne -> {
                tvOptionOne?.let {
                    selectedOptionView(it)
                    selectedOption = 1

                    tvOptionOne?.error = null
                    tvOptionTwo?.error = null
                    tvOptionThree?.error = null
                    tvOptionFour?.error = null
                }
            }
            R.id.tvOptionTwo -> {
                tvOptionTwo?.let {
                    selectedOptionView(it)
                    selectedOption = 2
                    tvOptionOne?.error = null
                    tvOptionTwo?.error = null
                    tvOptionThree?.error = null
                    tvOptionFour?.error = null
                }
            }
            R.id.tvOptionThree -> {
                tvOptionThree?.let {
                    selectedOptionView(it)
                    selectedOption = 3

                    //for error remove purpose
                    tvOptionOne?.error = null
                    tvOptionTwo?.error = null
                    tvOptionThree?.error = null
                    tvOptionFour?.error = null
                }
            }
            R.id.tvOptionFour -> {
                tvOptionFour?.let {
                    selectedOptionView(it)
                    selectedOption = 4

                    tvOptionOne?.error = null
                    tvOptionTwo?.error = null
                    tvOptionThree?.error = null
                    tvOptionFour?.error = null
                }
            }
            R.id.btnSubmit -> {

                //yeh jesy zero hoga tu hamara next list ka object load hoga
                if (selectedOption == 0) {
                    currentPosition++
                    if (currentPosition <= questionsList!!.size) {

                        setQuestionsToDisplay(questionsList!!)
                        tvOptionOne?.error = null
                        tvOptionTwo ?. error = null
                        tvOptionThree?.error = null
                        tvOptionFour?.error = null
                    } else {
                        val listSize: Int = questionsList!!.size
                        val intent = Intent(this, ResultActivity::class.java)
                        intent.putExtra("name", name)
                        intent.putExtra("score", totalScore)
                        intent.putExtra("listsize", listSize)
                        finish()
                        startActivity(intent)
                    }
                } else {
                    val question =
                        questionsList!!.get(currentPosition - 1)
                    if (question.correctOption != selectedOption) {
                        answerView(
                            selectedOption,
                            R.drawable.wrong_option_bg,
                            "#FF0000",
                            Typeface.DEFAULT_BOLD,
                            "error"
                        )
                    } else {

                        totalScore++
                    }
                    answerView(
                        question.correctOption,
                        R.drawable.correct_option_bg,
                        "#FFFFFF",
                        Typeface.DEFAULT_BOLD,
                        "correct"
                    )

                    if (currentPosition == questionsList!!.size) {

                        btnSubmit?.text = "FINISH"

                    } else {

                        btnSubmit?.text = "GO TO NEXT QUESTION"

                    }

                    selectedOption = 0
                }
            }
        }
    }
}