package com.alex.sobapp.apiService

import com.alex.sobapp.apiService.domain.*
import okhttp3.MultipartBody
import retrofit2.http.*

interface Api {
    //登录
    @POST("/uc/user/login/{captcha}")
    suspend fun login(
        @Header("l_c_i") l_c_i: String,
        @Path("captcha") verifyCode: String,
        @Body user: User
    ): LoginResponse

    //token解析
    @GET("/uc/user/checkToken")
    suspend fun analysisToken(): TokenInfo

    //获取注册的手机验证码（注册）
    @POST("/uc/ut/join/send-sms")
    suspend fun getPhoneVerifyCode(@Body sendSmsVo: SendSmsVo): BaseResponseResult

    //获取找回密码的手机验证码（找回密码）
    @POST("/uc/ut/forget/send-sms")
    suspend fun getForgetVerifyCode(@Body sendSmsVo: SendSmsVo): BaseResponseResult

    //检查手机验证码是否正确
    @GET("/uc/ut/check-sms-code/{phoneNumber}/{smsCode}")
    suspend fun checkSmsCode(
        @Path("phoneNumber") phoneNumber: String,
        @Path("smsCode") smsCode: String
    ): BaseResponseResult

    //注册账号
    @POST("/uc/user/register/{smsCode}")
    suspend fun registerAccount(
        @Path("smsCode") smsCode: String,
        @Body user: RegisterUser
    ): BaseResponseResult

    //找回密码（通过短信找回）
    @PUT("/uc/user/forget/{smsCode}")
    suspend fun forgetPasswordBySms(
        @Path("smsCode") smsCode: String,
        @Body userVo: User
    ): BaseResponseResult

    //获取分类列表
    @GET("/ct/category/list")
    suspend fun getCategoryList(): CategoryList

    //获取推荐内容
    @GET("/ct/content/home/recommend/{page}")
    suspend fun getRecommendInfo(@Path("page") page: Int): DataClass<RecommendList>

    //根据分类获取内容
    @GET("/ct/content/home/recommend/{categoryId}/{page}")
    suspend fun getClassifiedContent(
        @Path("categoryId") categoryId: String,
        @Path("page") page: Int
    ): DataClass<RecommendList>

    //获取轮播图
    @GET("/ast/home/loop/list")
    suspend fun getHomeBanner(): HomeBannerList

    //todo:获取摸鱼界面的导航
    @GET("/ct/moyu/topic/index")
    suspend fun getMoYuCategory(): MoYuCategory

    //获取动态列表
    @GET("/ct/moyu/list/{topicId}/{page}")
    suspend fun getMoYuList(
        @Path("topicId") topicId: String,
        @Path("page") page: Int
    ): DataClass<MoYuInfoList>

    //获取动态评论
    @GET("/ct/moyu/comment/{momentId}/{page}?sort=1")
    suspend fun getDynamicComments(
        @Path("momentId") momentId: String, @Path("page") page: Int
    ): DataClass<DynamicComments>

    //获取话题列表
    @GET("/ct/moyu/topic")
    suspend fun getTopicList(): TopicList

    //上传动态图片
    @Multipart
    @POST("/ct/image/mo_yu")
    suspend fun uploadImage(@Part() file: MultipartBody.Part): UploadImageResult

    //发布动态
    @POST("/ct/moyu")
    suspend fun releaseDynamic(@Body dynamicBody: DynamicBody): ReleaseDynamicResult

    //发表评论(评论动态)
    @POST("/ct/moyu/comment")
    suspend fun replyComment(@Body momentComment: MomentComment): BaseResponseResult

    //回复评论
    @POST("/ct/moyu/sub-comment")
    suspend fun replySubComment(@Body subComment: SubComment): BaseResponseResult

    //动态点赞
    @PUT("/ct/moyu/thumb-up/{momentId}")
    suspend fun thumbUp(@Path("momentId") momentId: String): ThumbUpResult

    //获取用户信息
    @GET("/uc/user-info/{userId}")
    suspend fun getUserInfo(@Path("userId") userId: String): DataClass<UserInfo>

    //获取关注列表
    @GET("/uc/follow/list/{userId}/{page}")
    suspend fun getFocusList(
        @Path("userId") userId: String,
        @Path("page") page: Int
    ): DataClass<FocusAndFansListInfo>

    //获取粉丝列表
    @GET("/uc/fans/list/{userId}/{page}")
    suspend fun getFansList(
        @Path("userId") userId: String,
        @Path("page") page: Int
    ): DataClass<FocusAndFansListInfo>

    //添加关注
    @POST("/uc/fans/{userId}")
    suspend fun addFocus(@Path("userId") userId: String): BaseResponseResult

    //取消关注
    @DELETE("/uc/fans/{userId}")
    suspend fun deleteFocus(@Path("userId") userId: String): BaseResponseResult

    //获取未读信息
    @GET("/ct/msg/count")
    suspend fun getUnreadInfo(): DataClass<UnreadInfo>


    //用户中心
    //获取文章评论列表
    @GET("/ct/ucenter/message/article/{page}")
    suspend fun getMineArticleComment(@Path("page") page: Int): DataClass<MineArticleComment>

    //获取动态评论
    @GET("/ct/ucenter/message/moment/{page}")
    suspend fun getDynamicComment(@Path("page") page: Int): DataClass<MineDynamicComment>

    //获取回答列表
    @GET("/ct/ucenter/message/wenda/{page}")
    suspend fun getMineWendaList(@Path("page") page: Int): DataClass<MineWendaList>

    //回复我的
    @GET("/ct/ucenter/message/at/{page}")
    suspend fun getAtMeInfo(@Path("page") page: Int): DataClass<MineAtMeInfo>

    //获取点赞列表
    @GET("/ct/ucenter/message/thumb/{page}")
    suspend fun getThumbUpList(@Path("page") page: Int): DataClass<MineThumbUpList>

    //获取系统消息
    @GET("/ct/ucenter/message/system/{page}")
    suspend fun getSystemInfo(@Path("page") page: Int): DataClass<SystemInfo>

    //更新摸鱼动态消息的状态
    @PUT("/ct/ucenter/message/moment/state/{msgId}/1")
    suspend fun updateDynamicState(@Path("msgId") msgId: String): UpdateStateResult

    //一键已读所有消息
    @GET("/ct/msg/read")
    suspend fun oneStepRead(): BaseResponseResult

    //VIP会员
    //判断是否有领取津贴
    @GET("/ast/vip-allowance")
    suspend fun checkVipAllowance(): BaseResponseResult

    //VIP领取津贴（每月）
    @PUT("/ast/vip-allowance")
    suspend fun getVipAllowance(): BaseResponseResult

    //首页
    //获取文章详情
    @GET("/ct/article/detail/{articleId}")
    suspend fun getArticle(@Path("articleId") articleId: String): DataClass<ArticleContent>

    //获取文章详情的评论
    @GET("/ct/article/comment/{articleId}/{page}")
    suspend fun getArticleComments(
        @Path("articleId") articleId: String,
        @Path("page") page: Int
    ): DataClass<ArticleComments>

    //获取相关文章列表
    @GET("/ct/article/recommend/{articleId}/{size}")
    suspend fun getRecommendArticle(
        @Path("articleId") articleId: String,
        @Path("size") size: Int
    ): RecommendArticle

    //问答模块
    //获取问答列表
    @GET("/ct/wenda/list")
    suspend fun getWendaList(
        @Query("page") page: Int,
        @Query("state") state: String,
        @Query("category") category: String
    ): DataClass<WendaList>

    //获取问答详情
    @GET("/ct/wenda/{wendaId}")
    suspend fun getWendaDetail(@Path("wendaId") wendaId: String): DataClass<WendaDetail>

    //获取答案列表
    @GET("/ct/wenda/comment/list/{wendaId}/{page}")
    suspend fun getWendaAnswerList(
        @Path("wendaId") wendaId: String,
        @Path("page") page: Int
    ): WendaAnswer

    //获取相关的问题
    @GET("/ct/wenda/relative/{wendaId}/{size}")
    suspend fun getRelatedQuestion(
        @Path("wendaId") wendaId: String,
        @Path("size") size: Int
    ): DataClass<RelatedQuestionList>

    //学院
    //获取课程列表
    @GET("/ct/edu/course/list/{page}")
    suspend fun getCourseList(@Path("page") page: Int): DataClass<CourseList>

    //课程详情
    @GET("/ct/edu/course/{courseId}")
    suspend fun getCourseDetail(@Path("courseId") courseId: String): DataClass<CourseDetail>

    //课程章节内容
    @GET("/ct/edu/course/chapter/{courseId}")
    suspend fun getCourseChapter(@Path("courseId") courseId: String): DataClass<CourseChapter>

    //创建播放凭证
    @GET("/ct/video/certification/{videoId}")
    suspend fun getVideo(@Path("videoId") videoId: String): DataClass<VideoResult>
}