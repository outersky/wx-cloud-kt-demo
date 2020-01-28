package cn.hillwind.wx.newartist.util

import org.springframework.context.ApplicationContext
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import java.io.File
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

object Global {

    lateinit var uploadRootDir: File

    lateinit var ctx: String

    lateinit var applicationContext: ApplicationContext

    //获取HttpServletRequest
    @JvmStatic
    fun request(): HttpServletRequest? {
        val attr = RequestContextHolder.getRequestAttributes() as ServletRequestAttributes?
        return attr?.request
    }

    //获取HttpServletRequest
    @JvmStatic
    fun response(): HttpServletResponse? {
        val attr = RequestContextHolder.getRequestAttributes() as ServletRequestAttributes?
        return attr?.response
    }

    fun <T> getBean(cls: Class<T>): T {
        return applicationContext.getBean(cls)
    }

}