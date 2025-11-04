package com.arya.post5

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.arya.post5.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(),
    AddPostDialog.OnPostAddedListener,
    AddPostDialog.OnPostEditedListener,
    PostAdapter.OnPostOptionsClickListener {

    private lateinit var binding: ActivityMainBinding

    private lateinit var postAdapter: PostAdapter
    private val postList = mutableListOf<Post>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupStoryRecyclerView()
        setupPostRecyclerView()

        binding.fabAddPost.setOnClickListener {
            val addPostDialog = AddPostDialog()
            addPostDialog.setOnPostAddedListener(this)
            addPostDialog.show(supportFragmentManager, AddPostDialog.TAG)
        }
    }

    private fun setupStoryRecyclerView() {
        val storyList = listOf(
            Story("intan_dwi", R.drawable.profile_intan),
            Story("minda_04", R.drawable.profile_minda),
            Story("rubi_comm", R.drawable.profile_rubi),
            Story("rizka", R.drawable.profile_rizka),
            Story("riza", R.drawable.profile_riza),
            Story("joko", R.drawable.profile_joko)
        )
        val storyAdapter = StoryAdapter(storyList)
        binding.rvStories.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = storyAdapter
        }
    }

    private fun setupPostRecyclerView() {
        postList.clear()
        postList.addAll(listOf(
            Post("intan_dwi", R.drawable.profile_intan, R.drawable.post_image_1, "Liburan ke pantai"),
            Post("minda_04", R.drawable.profile_minda, R.drawable.post_image_2, "Hari yang menyenangkan!"),
            Post("riza", R.drawable.profile_riza, R.drawable.post_image_4, "Sedang Menjomblo!"),
            Post("joko", R.drawable.profile_joko, R.drawable.post_image_3, "Pemandangan yang menakjubkan️")
        ))

        postAdapter = PostAdapter(postList)
        postAdapter.setOnPostOptionsClickListener(this)
        binding.rvPosts.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = postAdapter
        }
    }

    override fun onEditClick(post: Post, position: Int) {
        val addPostDialog = AddPostDialog.newInstanceForEdit(post, position)
        addPostDialog.setOnPostEditedListener(this)
        addPostDialog.show(supportFragmentManager, AddPostDialog.TAG)
    }

    override fun onPostAdded(username: String, caption: String, image: Any) {
        val newPost = Post(
            username = username,
            profileImageResId = R.drawable.profile_joko,
            postImage = image,
            caption = caption
        )

        postList.add(0, newPost)
        postAdapter.notifyItemInserted(0)
        binding.rvPosts.scrollToPosition(0)
    }

    override fun onPostEdited(position: Int, newUsername: String, newCaption: String) {
        val oldPost = postList[position]
        val updatedPost = oldPost.copy(
            username = newUsername,
            caption = newCaption
        )
        postList[position] = updatedPost
        postAdapter.notifyItemChanged(position)
    }
}