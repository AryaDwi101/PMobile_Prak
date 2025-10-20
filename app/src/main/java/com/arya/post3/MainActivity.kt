package com.arya.post3

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.arya.post3.ResultActivity.Companion.DATA_AGE
import com.arya.post3.ResultActivity.Companion.DATA_EMAIL
import com.arya.post3.ResultActivity.Companion.DATA_JK
import com.arya.post3.ResultActivity.Companion.DATA_NAMA
import com.arya.post3.ResultActivity.Companion.DATA_PASS
import com.arya.post3.ResultActivity.Companion.DATA_USER
import com.arya.post3.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var jk = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        binding.apply {
            btnSubmit.setOnClickListener {
                val nama = name.text.toString()
                val user = username.text.toString()
                val age = age.text.toString()
                val email = email.text.toString()

                if (rdb1.isChecked) {
                    jk = "Laki-laki"
                } else if (rdb2.isChecked){
                    jk = "Perempuan"
                }

                val pass = password.text.toString()
                val confirm = repassword.text.toString()

                if (pass == confirm) {
                    repassword.error = null
                    val intent = Intent(this@MainActivity, ResultActivity::class.java)
                        .putExtra(DATA_NAMA, nama)
                        .putExtra(DATA_USER, user)
                        .putExtra(DATA_AGE, age)
                        .putExtra(DATA_EMAIL, email)
                        .putExtra(DATA_JK, jk)
                        .putExtra(DATA_PASS, pass)
                    startActivity(intent)
                } else {
                    repassword.error = "Password tidak cocok!"
                }

            }
        }
    }
}