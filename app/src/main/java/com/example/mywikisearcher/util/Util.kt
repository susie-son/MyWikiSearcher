package com.example.mywikisearcher.util

import com.example.mywikisearcher.util.Constants.WIKIPEDIA_BASE_URL
import java.net.URLEncoder

fun Double.format(digit: Int) = "%.${digit}f".format(this)

fun getWikiPageUrl(articleTitle: String): String {
    val encodedTitle = URLEncoder.encode(articleTitle, "UTF-8")
    return "${WIKIPEDIA_BASE_URL}${encodedTitle}"
}
