package com.example.smartquiz

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import org.w3c.dom.Text

class ResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        var ivReward: ImageView = findViewById(R.id.ivReward)
        val tvScore: TextView = findViewById(R.id.tvScore)
        val tvAppreciation: TextView = findViewById(R.id.tvAppreciation)
        val btnRetake: Button = findViewById(R.id.btnRetake)
        val btnClose: Button = findViewById(R.id.btnClose)
        var tvName: TextView = findViewById(R.id.tvName)

        val name: String = intent.getStringExtra("name").toString()
        val score: Int = intent.getIntExtra("score", 0)
        val listSize: Int = intent.getIntExtra("listsize", 0)

        tvName.text = name

        if (score <= listSize/2){

            tvAppreciation.text = "OOh! Best of luck for the next time..."
            tvScore.text = "Your score is $score out of $listSize"
            ivReward.background = ContextCompat.getDrawable(this, R.drawable.sad_face_emoji)
        }else {

            tvAppreciation.text = "Hey Congratulations!"
            tvScore.text = "Your score is $score out of $listSize"
            ivReward.background = ContextCompat.getDrawable(this, R.drawable.winning_cup)
        }

        btnRetake.setOnClickListener(View.OnClickListener {

            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        })

        btnClose.setOnClickListener(View.OnClickListener {

            val alert = AlertDialog.Builder(this)
            alert.setTitle("Warning!")
            alert.setMessage("Are you sure to want to close the app ?")
            alert.setPositiveButton("YES", DialogInterface.OnClickListener { dialogInterface, i ->

                finish()
            })

            alert.setNegativeButton("CANCEL", null)

            alert.show()
        })


    }
}