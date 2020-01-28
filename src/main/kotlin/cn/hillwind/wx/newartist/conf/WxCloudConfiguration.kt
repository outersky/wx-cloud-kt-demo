package cn.hillwind.wx.newartist.conf

import cn.hillwind.wx.cloud.WxCloudDbService
import cn.hillwind.wx.cloud.WxCloudProperties
import cn.hillwind.wx.cloud.WxCloudService
import cn.hillwind.wx.cloud.WxCloudStorageService
import cn.hillwind.wx.cloud.impl.WxCloudServiceDefaultImpl
import cn.hillwind.wx.cloud.impl.WxCloudServiceImpl
import me.chanjar.weixin.mp.api.WxMpService
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * 云开发的有关配置
 */
@Configuration
@EnableConfigurationProperties(WxCloudSpringProperties::class)
class WxCloudConfiguration {

    @Bean
    fun wxCloudService(wxMpService: WxMpService, cloudProperties: WxCloudProperties): WxCloudService {
        return WxCloudServiceImpl(wxMpService, cloudProperties)
    }

    @Bean
    fun wxCloudDbService(wxMpService: WxMpService, cloudProperties: WxCloudProperties): WxCloudDbService {
        return WxCloudServiceDefaultImpl(wxMpService, cloudProperties)
    }

    @Bean
    fun wxCloudStorageService(wxMpService: WxMpService, cloudProperties: WxCloudProperties): WxCloudStorageService {
        return WxCloudServiceDefaultImpl(wxMpService, cloudProperties)
    }

}

/**
 * wechat cloud properties
 *
 */
@ConfigurationProperties(prefix = "wx.cloud")
class WxCloudSpringProperties : WxCloudProperties()