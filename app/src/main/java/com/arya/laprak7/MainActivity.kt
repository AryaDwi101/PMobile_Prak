package com.arya.laprak7

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.arya.laprak7.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: BookAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        layoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
        binding.rvBooks.layoutManager = GridLayoutManager(this, 2)
        fetchBooks()
    }

    private fun fetchBooks(){
        val retrofit = Retrofit.Builder()
            .baseUrl("https://potterapi-fedeperin.vercel.app/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(ApiService::class.java)

        api.getBooks().enqueue(object : Callback<List<Book>> {
            override fun onResponse(
                call: Call<List<Book>>,
                response: Response<List<Book>>
            ) {
                if (response.isSuccessful) {
                    val books = response.body() ?: emptyList()
                    adapter = BookAdapter(books) { book, imageUrl ->
                        val intent = Intent(this@MainActivity, DetailActivity::class.java).apply {
                            putExtra("title", book.title)
                            putExtra("description", book.description)
                            putExtra("pages", book.pages.toString())
                            putExtra("releaseDate", book.releaseDate)

                            putExtra("imageUrl", imageUrl)
                        }
                        startActivity(intent)
                    }
                    binding.rvBooks.adapter = adapter
                }
            }

            override fun onFailure(call: Call<List<Book>>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}