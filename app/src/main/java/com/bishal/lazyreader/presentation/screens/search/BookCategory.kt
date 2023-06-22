package com.bishal.lazyreader.presentation.screens.search

enum class BookCategory (val value: String) {
    ANDROID("Android"),
    ROMANCE("Romance"),
    HISTORY("History"),
    MYTHOLOGY("Mythology"),
    SCIFI("Scifi"),
    BIOLOGY("Biology")
}

fun getAllBookCategories(): List<BookCategory>{
    return listOf(
        BookCategory.ANDROID,
        BookCategory.ROMANCE,
        BookCategory.HISTORY,
        BookCategory.MYTHOLOGY,
        BookCategory.SCIFI,
        BookCategory.BIOLOGY
    )
}

fun getBookCategory(value: String): BookCategory?{
    val map = BookCategory.values().associateBy(BookCategory::value)
    return map[value]
}