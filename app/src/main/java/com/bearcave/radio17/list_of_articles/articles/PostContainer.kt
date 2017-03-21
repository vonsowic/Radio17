package com.bearcave.radio17.list_of_articles.articles

import java.io.Serializable

/**
 * Created by miwas on 21.03.17.
 */

class PostContainer(
        val url: String,
        val title: String,
        val imageUrl: String,
        val text: String
) : Serializable
