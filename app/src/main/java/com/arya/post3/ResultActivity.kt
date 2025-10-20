package com.arya.post3

import android.graphics.Color
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.arya.post3.databinding.ActivityMainBinding
import com.arya.post3.databinding.ActivityResultBinding


class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val nama = intent.getStringExtra(DATA_NAMA)
        val user = intent.getStringExtra(DATA_USER)
        val age = intent.getStringExtra(DATA_AGE)
        val email = intent.getStringExtra(DATA_EMAIL)
        val gender = intent.getStringExtra(DATA_JK)


        binding.apply {
            viewname.text = nama
            viewuser.text = user
            viewage.text = age
            viewemail.text = email
            viewgender.text = gender

            if (gender == "Laki-laki") {
                viewgender.setBackgroundColor(Color.parseColor("#ADD8E6")) // Light Blue
            } else if (gender == "Perempuan") {
                viewgender.setBackgroundColor(Color.parseColor("#FFB6C1")) // Light Pink
            }
        }
    }

    companion object {
        const val DATA_NAMA = "data_nama"
        const val DATA_USER = "data_user"
        const val DATA_AGE = "data_age"
        const val DATA_EMAIL = "data_email"
        const val DATA_JK = "data_jk"
        const val DATA_PASS = "data_pass"
    }
}