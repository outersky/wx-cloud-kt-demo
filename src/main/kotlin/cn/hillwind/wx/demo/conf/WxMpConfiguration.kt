package cn.hillwind.wx.demo.conf

import cn.hillwind.wx.cloud.JsonHelper
import me.chanjar.weixin.mp.api.WxMpMessageRouter
import me.chanjar.weixin.mp.api.WxMpService
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl
import me.chanjar.weixin.mp.config.WxMpConfigStorage
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.stream.Collectors

/**
 * wechat mp configuration
 *
 * @author Binary Wang(https://github.com/binarywang)
 */
@Configuration
@EnableConfigurationProperties(WxMpProperties::class)
class WxMpConfiguration {

    @Autowired
    lateinit var properties: WxMpProperties

    @Bean
    fun wxMpService(): WxMpService { // 代码里 getConfigs()处报错的同学，请注意仔细阅读项目说明，你的IDE需要引入lombok插件！！！！
        val configs: List<WxMpProperties.MpConfig> = properties.configs
                ?: throw RuntimeException("大哥，拜托先看下项目首页的说明（readme文件），添加下相关配置，注意别配错了！")
        val service: WxMpService = WxMpServiceImpl()
        service.setMultiConfigStorages(configs
                .stream().map { a: WxMpProperties.MpConfig ->
                    val configStorage = WxMpDefaultConfigImpl()
                    configStorage.appId = a.appId
                    configStorage.secret = a.secret
                    configStorage.token = a.token
                    configStorage.aesKey = a.aesKey
                    configStorage
                }.collect(Collectors.toMap({ obj: WxMpDefaultConfigImpl -> obj.appId }, { a: WxMpDefaultConfigImpl? -> a }) { o: WxMpConfigStorage?, _: WxMpConfigStorage? -> o }))
        return service
    }

    @Bean
    fun messageRouter(wxMpService: WxMpService?): WxMpMessageRouter {
        val newRouter = WxMpMessageRouter(wxMpService)
        // 记录所有事件的日志 （异步执行）
/*
        newRouter.rule().handler(logHandler).next()
        // 接收客服会话管理事件
        newRouter.rule().async(false).msgType(XmlMsgType.EVENT).event(CustomerService.KF_CREATE_SESSION)
                .handler(kfSessionHandler).end()
        newRouter.rule().async(false).msgType(XmlMsgType.EVENT).event(CustomerService.KF_CLOSE_SESSION)
                .handler(kfSessionHandler).end()
        newRouter.rule().async(false).msgType(XmlMsgType.EVENT).event(CustomerService.KF_SWITCH_SESSION)
                .handler(kfSessionHandler).end()
        // 门店审核事件
        newRouter.rule().async(false).msgType(XmlMsgType.EVENT).event(WxMpEventConstants.POI_CHECK_NOTIFY).handler(storeCheckNotifyHandler).end()
        // 自定义菜单事件
        newRouter.rule().async(false).msgType(XmlMsgType.EVENT).event(MenuButtonType.CLICK).handler(menuHandler).end()
        // 点击菜单连接事件
        newRouter.rule().async(false).msgType(XmlMsgType.EVENT).event(MenuButtonType.VIEW).handler(nullHandler).end()
        // 关注事件
        newRouter.rule().async(false).msgType(XmlMsgType.EVENT).event(WxConsts.EventType.SUBSCRIBE).handler(subscribeHandler).end()
        // 取消关注事件
        newRouter.rule().async(false).msgType(XmlMsgType.EVENT).event(WxConsts.EventType.UNSUBSCRIBE).handler(unsubscribeHandler).end()
        // 上报地理位置事件
        newRouter.rule().async(false).msgType(XmlMsgType.EVENT).event(WxConsts.EventType.LOCATION).handler(locationHandler).end()
        // 接收地理位置消息
        newRouter.rule().async(false).msgType(XmlMsgType.LOCATION).handler(locationHandler).end()
        // 扫码事件
        newRouter.rule().async(false).msgType(XmlMsgType.EVENT).event(WxConsts.EventType.SCAN).handler(scanHandler).end()
        // 默认
        newRouter.rule().async(false).handler(msgHandler).end()
*/
        return newRouter
    }
}

/**
 * wechat mp properties
 *
 * @author Binary Wang(https://github.com/binarywang)
 */
@ConfigurationProperties(prefix = "wx.mp")
class WxMpProperties {

    var configs: List<MpConfig>? = null

    class MpConfig {
        /**
         * 设置微信公众号的appid
         */
        var appId: String? = null
        /**
         * 设置微信公众号的app secret
         */
        var secret: String? = null
        /**
         * 设置微信公众号的token
         */
        var token: String? = null
        /**
         * 设置微信公众号的EncodingAESKey
         */
        var aesKey: String? = null
    }

    override fun toString(): String {
        return JsonHelper.toJson(this)
    }
}