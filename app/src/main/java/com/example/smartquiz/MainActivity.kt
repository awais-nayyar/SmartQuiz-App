package com.example.smartquiz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class MainActivity : AppCompatActivity() {

    lateinit var btnStart: Button
    lateinit var tilName: TextInputLayout
    lateinit var etName: TextInputEditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnStart = findViewById(R.id.btnStart)
        tilName = findViewById(R.id.tilName)
        etName = findViewById(R.id.etName)

        btnStart.setOnClickListener(View.OnClickListener {
            val name = etName.text.toString()
            tilName.isErrorEnabled = false
            if(name.isEmpty()){
                tilName.isErrorEnabled = true
                tilName.error = "Name is required"
                etName.requestFocus()
            } else {

               val intent = Intent(this, QuestionActivity::class.java)
                intent.putExtra("name", name)
                finish()
                startActivity(intent)

            }
        })
    }
}