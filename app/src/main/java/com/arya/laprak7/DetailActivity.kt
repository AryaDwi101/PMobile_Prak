package com.arya.laprak7

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.arya.laprak7.databinding.ActivityDetailBinding
import com.bumptech.glide.Glide // Import Glide

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val title = intent.getStringExtra("title")
        val description = intent.getStringExtra("description")
        val pages = intent.getStringExtra("pages")
        val releaseDate = intent.getStringExtra("releaseDate")
        val imageUrl = intent.getStringExtra("imageUrl")

        // 1. Setup Toolbar (Tanpa tombol back di atas)
        setSupportActionBar(binding.toolbarDetail)
        supportActionBar?.title = title
        // HAPUS baris setDisplayHomeAsUpEnabled(true) karena kita tidak pakai panah atas lagi

        // 2. Setup Data Tampilan
        binding.tvDetailTitle.text = title
        binding.tvDetailDesc.text = description
        binding.tvDetailPages.text = "$pages Pages"
        binding.tvDetailRelease.text = releaseDate

        Glide.with(this).load(imageUrl).into(binding.imgDetailCover)

        // 3. Setup Tombol Home (Logika Back Baru)
        binding.btnHome.setOnClickListener {
            // Perintah untuk kembali ke activity sebelumnya (sama seperti tombol back)
            finish()
        }
    }
}