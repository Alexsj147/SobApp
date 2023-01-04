package com.alex.sobapp.utils

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.ArrayMap
import androidx.annotation.NonNull

/**
 * Activity的管理
 * finish所有Activity，然后start一个新的Activity
 * 想某些Activity不被finish，其它的Activity都被销毁
 * 判断当前app是否处于前台
 * 获取当前栈顶Activity
 */
object ActivityManager : Application.ActivityLifecycleCallbacks {

    private lateinit var mApplication: Application
    private val mActivityTaskMap = ArrayMap<String, Activity>()

    //最后可见Activity对象的TAG
    private var mLastVisibleTag: String? = null

    //最后不可见的Activity对象的TAG
    private var mLastInVisibleTag: String? = null

    /**
     * 初始化，仅第一次有效
     */
    fun init(application: Application) {
        if (::mApplication.isInitialized.not()) {
            mApplication = application
            application.registerActivityLifecycleCallbacks(this)
        }
    }

    /**
     * 获取Application对象
     */
    fun getApplication() = mApplication

    /**
     * 最后一个可见的Activity和最后一个不可见的Activity不是同一个
     * 换言之：是同一个Activity，app则未处于前台
     * 或栈顶Activity不为null，则app处于前台
     */
    fun applsForeground() =
        ((mLastVisibleTag == mLastInVisibleTag).not() || getStackTopActivity() != null)

    /**
     * 获取栈顶的Activity
     */
    fun getStackTopActivity() = mActivityTaskMap[mLastVisibleTag]

    /**
     * 销毁所有的Activity
     */
    fun finishAllActivity() {
        finishAllActivities(null)
    }

    /**
     * 销毁除了参数列表中以外所有的Activity
     */
    fun finishAllActivities(vararg clazzArray: Class<out Activity>?) {
        val keys: Array<String> = mActivityTaskMap.keys.toTypedArray()
        for (key in keys) {
            val activity: Activity? = mActivityTaskMap[key]
            activity ?: continue
            if (activity.isFinishing.not()) {
                var isWhiteListActivity = false
                for (clazz in clazzArray) {
                    if (activity.javaClass == clazz) {
                        isWhiteListActivity = true
                    }
                }
                //如果不是白名单上面的Activity就销毁
                if (!isWhiteListActivity) {
                    activity.finish()
                    mActivityTaskMap.remove(key)
                }
            }
        }
    }

    override fun onActivityPaused(activity: Activity) {
        setLastInvisibleTag(activity)
    }

    override fun onActivityStarted(activity: Activity) {
        setLastVisibleTag(activity)
    }

    override fun onActivityDestroyed(activity: Activity) {
        val objectTag = getObjectTag(activity)
        mActivityTaskMap.remove(objectTag)
        if (objectTag == mLastVisibleTag) {
            //清除当前标记
            mLastVisibleTag = null
        }
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

    }

    override fun onActivityStopped(activity: Activity) {
        setLastInvisibleTag(activity)
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        setLastVisibleTag(activity)
        mActivityTaskMap[mLastVisibleTag] = activity
    }

    override fun onActivityResumed(activity: Activity) {
        setLastVisibleTag(activity)
    }


    private fun setLastInvisibleTag(activity: Activity) {
        mLastInVisibleTag = getObjectTag(activity)
    }

    private fun setLastVisibleTag(activity: Activity) {
        mLastVisibleTag = getObjectTag(activity)
    }

    private fun getObjectTag(@NonNull any: Any): String =
        any::class.java.name + Integer.toHexString(any.hashCode())


}