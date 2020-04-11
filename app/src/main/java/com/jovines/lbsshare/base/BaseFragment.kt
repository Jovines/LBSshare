package com.jovines.lbsshare.base

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.jovines.lbsshare.event.LoginStateChangeEvent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * Created By jay68 on 2018/8/9.
 */
open class BaseFragment : Fragment() {

    /**
     * 这里可以开启生命周期的Log，你可以重写这个值并给值为true，
     * 也可以直接赋值为true（赋值的话请在init{}里面赋值或者在super.onCreate(savedInstanceState)调用之前赋值）
     */
    open protected var isOpenLifeCycleLog = false

    //当然，你要定义自己的TAG方便在Log里面找也可以重写这个
    open protected var TAG: String = this::class.java.simpleName

    private var isStarted = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        EventBus.getDefault().register(this)
    }

    override fun onPause() {
        super.onPause()
        lifeCycleLog("onPause")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        lifeCycleLog("onDestroyView")
        EventBus.getDefault().unregister(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifeCycleLog("onCreate")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        lifeCycleLog("onAttach")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        lifeCycleLog("onCreateView")
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        lifeCycleLog("onActivityCreated")
    }

    override fun onStart() {
        super.onStart()
        lifeCycleLog("onStart")
    }

    override fun onResume() {
        super.onResume()
        lifeCycleLog("onResume")
    }

    override fun onStop() {
        super.onStop()
        lifeCycleLog("onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        lifeCycleLog("onDestroy")
    }

    override fun onDetach() {
        super.onDetach()
        lifeCycleLog("onDetach")
    }

    fun lifeCycleLog(message: String) {
        if (isOpenLifeCycleLog) {
            Log.d(TAG, "${this::class.java.simpleName}\$\$${message}")
        }
    }

    /**
     * 登陆状态发生改变回调
     * @param event true 发生改变，false
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    open fun onLoginStateChangeEvent(event: LoginStateChangeEvent) {
    }
}