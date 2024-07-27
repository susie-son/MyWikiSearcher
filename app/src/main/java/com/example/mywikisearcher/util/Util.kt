package com.example.mywikisearcher.util

import com.example.mywikisearcher.util.Constants.WIKIPEDIA_BASE_URL

fun Double.format(decimalPlaces: Int) = "%.${decimalPlaces}f".format(this)

fun getWikiPageUrl(articleTitle: String): String {
    return "${WIKIPEDIA_BASE_URL}${articleTitle}"
}
