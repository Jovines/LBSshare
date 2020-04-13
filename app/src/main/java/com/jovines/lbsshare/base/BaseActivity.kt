package com.jovines.lbsshare.base

//import com.jude.swipbackhelper.SwipeBackHelper
import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.jovines.lbs_server.entity.UserBean
import com.jovines.lbsshare.APP
import com.jovines.lbsshare.config.PASSWORD
import com.jovines.lbsshare.config.USER_NAME
import com.jovines.lbsshare.event.LoginEvent
import com.jovines.lbsshare.event.LoginStateChangeEvent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.defaultSharedPreferences
import org.jetbrains.anko.startActivity


abstract class BaseActivity : AppCompatActivity() {

    /**
     * 这里可以开启生命周期的Log，你可以重写这个值并给值为true，
     * 也可以直接赋值为true（赋值的话请在init{}里面赋值或者在onCreate的super.onCreate(savedInstanceState)调用之前赋值）
     */
    protected open var isOpenLifeCycleLog = false

    //当然，你要定义自己的TAG方便在Log里面找也可以重写这个
    protected open var TAG: String = this::class.java.simpleName


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
        lifeCycleLog("onCreate")
        when {
            Build.VERSION.SDK_INT >= 23 -> {
                window.decorView.systemUiVisibility =
                        //亮色模式状态栏
//                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or
                        //设置decorView的布局设置为全屏
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                            //维持布局稳定，不会因为statusBar和虚拟按键的消失而移动view位置
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            }
        }
    }

    inline fun <reified T : Activity> startActivity(
        finish: Boolean = false,
        vararg params: Pair<String, Any?>
    ) {
        if (finish) finish()
        startActivity<T>(*params)
    }

    fun signOut() {
        APP.user = UserBean(0, "")
        APP.context.defaultSharedPreferences.edit {
            putLong(USER_NAME, 0)
            putString(PASSWORD, "")
        }
        EventBus.getDefault().post(LoginStateChangeEvent(false))
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    open fun onLoginEvent(event: LoginEvent) {
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    open fun onLoginStateChangeEvent(event: LoginStateChangeEvent) {
    }

    override fun onRestart() {
        super.onRestart()
        lifeCycleLog("onRestart")
    }


    override fun onStart() {
        super.onStart()
        lifeCycleLog("onStart")
    }


    override fun onResume() {
        super.onResume()
        lifeCycleLog("onResume")
    }

    override fun onPause() {
        super.onPause()
        lifeCycleLog("onPause")
    }

    override fun onStop() {
        super.onStop()
        lifeCycleLog("onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
        lifeCycleLog("onDestroy")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        lifeCycleLog("onSaveInstanceState")
    }

    override fun onRestoreInstanceState(
        savedInstanceState: Bundle?,
        persistentState: PersistableBundle?
    ) {
        super.onRestoreInstanceState(savedInstanceState, persistentState)
        lifeCycleLog("onRestoreInstanceState")
    }

    fun lifeCycleLog(message: String) {
        if (isOpenLifeCycleLog) {
            Log.d(TAG, "${this::class.java.simpleName}\$\$${message}")
        }
    }

}
