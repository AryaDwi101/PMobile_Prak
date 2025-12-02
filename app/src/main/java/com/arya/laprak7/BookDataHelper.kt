package com.arya.laprak7

object BookDataHelper {
    fun getCoverUrl(index: Int): String {
        return when (index) {
            0 -> "https://upload.wikimedia.org/wikipedia/en/6/6b/Harry_Potter_and_the_Philosopher%27s_Stone_Book_Cover.jpg"
            1 -> "https://upload.wikimedia.org/wikipedia/en/5/5c/Harry_Potter_and_the_Chamber_of_Secrets.jpg"
            2 -> "https://upload.wikimedia.org/wikipedia/en/a/a0/Harry_Potter_and_the_Prisoner_of_Azkaban.jpg"
            3 -> "https://upload.wikimedia.org/wikipedia/en/b/b6/Harry_Potter_and_the_Goblet_of_Fire_cover.png"
            4 -> "https://upload.wikimedia.org/wikipedia/en/7/70/Harry_Potter_and_the_Order_of_the_Phoenix.jpg"
            5 -> "https://upload.wikimedia.org/wikipedia/en/a/a9/Harry_Potter_and_the_Half-Blood_Prince.jpg"
            6 -> "https://upload.wikimedia.org/wikipedia/en/a/a9/Harry_Potter_and_the_Deathly_Hallows.jpg"
            7 -> "https://upload.wikimedia.org/wikipedia/en/thumb/6/6e/Harry_Potter_and_the_Cursed_Child_Special_Rehearsal_Edition_Book_Cover.jpg/220px-Harry_Potter_and_the_Cursed_Child_Special_Rehearsal_Edition_Book_Cover.jpg"
            else -> "https://via.placeholder.com/150"
        }
    }
}