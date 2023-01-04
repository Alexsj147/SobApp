package com.alex.sobapp.apiService.domain

class CourseChapter : ArrayList<CourseChapter.CourseChapterItem>() {
    data class CourseChapterItem(
        val children: List<Children>,
        val courseId: String,
        val description: String,
        val id: String,
        val sort: Double,
        val title: String
    ) {
        data class Children(
            val chapterId: String,
            val courseId: String,
            val duration: Double,
            val id: String,
            val isFree: Boolean,
            val playCount: Double,
            val sort: Double,
            val title: String
        )
    }

}


