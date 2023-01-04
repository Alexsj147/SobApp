package com.alex.sobapp.apiService.domain

data class WendaAnswer(
    val code: Int,
    val `data`: List<Data>,
    val message: String,
    val success: Boolean
){
    data class Data(
        val _id: String,
        val avatar: String,
        val bestAs: String,
        val content: String,
        val nickname: String,
        val publishTime: String,
        val publishTimeText: String,
        val subCommentCount: Int,
        val thumbUp: Int,
        val thumbUps: List<String>,
        val uid: String,
        val vip: Boolean,
        val wendaId: String,
        val wendaSubComments: List<WendaSubComment>
    ){
        data class WendaSubComment(
            val _id: String,
            val beNickname: String,
            val beUid: String,
            val content: String,
            val parentId: String,
            val publishTime: String,
            val vip: Boolean,
            val wendaId: String,
            val yourAvatar: String,
            val yourNickname: String,
            val yourRole: Any,
            val yourUid: String
        )
    }
}



