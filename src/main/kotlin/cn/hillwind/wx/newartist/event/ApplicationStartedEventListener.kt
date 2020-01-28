package cn.hillwind.wx.newartist.event

import cn.hillwind.wx.cloud.WxCloudContext
import cn.hillwind.wx.cloud.WxCloudService
import cn.hillwind.wx.cloud.WxCloudServiceHolder
import cn.hillwind.wx.newartist.util.Global
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.event.ApplicationStartedEvent
import org.springframework.context.ApplicationContext
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import java.io.File

@Component
class ApplicationStartedEventListener {

    @Value("\${uploadRootDir}")
    lateinit var uploadRootDir: String

    @Value("\${server.servlet.context-path}")
    lateinit var ctx: String

    @EventListener
    fun onApplicationEvent(ev: ApplicationStartedEvent) {
        Global.applicationContext = applicationContext
        Global.uploadRootDir = File(uploadRootDir)
        Global.ctx = ctx

        WxCloudContext.register(DefaultWxCloudServiceHolder())
    }

    @Autowired
    lateinit var applicationContext: ApplicationContext
}

class DefaultWxCloudServiceHolder : WxCloudServiceHolder {

    override fun getWxCloudService(): WxCloudService {
        var instance = wxCloudServiceHolder.get()
        if (instance == null) {
            instance = Global.getBean(WxCloudService::class.java)
            set(instance)
        }
        return instance
    }

    private val wxCloudServiceHolder = ThreadLocal<WxCloudService>()

    override fun set(service: WxCloudService) {
        wxCloudServiceHolder.set(service)
    }

    override fun remove() {
        wxCloudServiceHolder.remove()
    }

}