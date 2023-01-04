package com.alex.sobapp

import org.json.JSONObject
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    @Test
    fun addition_isCorrect() {



    }
}

class Req(var msgId: String, var cmd: String, var msgTxt: MsgTxt)
class MsgTxt(var source: String, var target: String, var roomId: String, var status: String)