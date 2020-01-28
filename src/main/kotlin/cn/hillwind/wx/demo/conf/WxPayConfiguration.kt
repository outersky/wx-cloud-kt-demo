package cn.hillwind.wx.demo.conf

import com.github.binarywang.wxpay.config.WxPayConfig
import com.github.binarywang.wxpay.service.WxPayService
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl
import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.builder.ToStringBuilder
import org.apache.commons.lang3.builder.ToStringStyle
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * @author Binary Wang
 */
@Configuration
@ConditionalOnClass(WxPayService::class)
@EnableConfigurationProperties(WxPayProperties::class)
class WxPayConfiguration @Autowired constructor(private val properties: WxPayProperties) {
    @Bean
    @ConditionalOnMissingBean
    fun wxService(): WxPayService {
        val payConfig = WxPayConfig()
        payConfig.appId = StringUtils.trimToNull(properties.appId)
        payConfig.mchId = StringUtils.trimToNull(properties.mchId)
        payConfig.mchKey = StringUtils.trimToNull(properties.mchKey)
        payConfig.subAppId = StringUtils.trimToNull(properties.subAppId)
        payConfig.subMchId = StringUtils.trimToNull(properties.subMchId)
        payConfig.keyPath = StringUtils.trimToNull(properties.keyPath)
        // 可以指定是否使用沙箱环境
        payConfig.isUseSandboxEnv = false
        val wxPayService: WxPayService = WxPayServiceImpl()
        wxPayService.config = payConfig
        return wxPayService
    }

}

/**
 * wxpay pay properties
 *
 * @author Binary Wang
 */
@ConfigurationProperties(prefix = "wx.pay")
class WxPayProperties {
    /**
     * 设置微信公众号或者小程序等的appid
     */
    var appId: String = ""
    /**
     * 微信支付商户号
     */
    var mchId: String? = null
    /**
     * 微信支付商户密钥
     */
    var mchKey: String? = null
    /**
     * 服务商模式下的子商户公众账号ID，普通模式请不要配置，请在配置文件中将对应项删除
     */
    var subAppId: String? = null
    /**
     * 服务商模式下的子商户号，普通模式请不要配置，最好是请在配置文件中将对应项删除
     */
    var subMchId: String? = null
    /**
     * apiclient_cert.p12文件的绝对路径，或者如果放在项目中，请以classpath:开头指定
     */
    var keyPath: String? = null

    override fun toString(): String {
        return ToStringBuilder.reflectionToString(this,
                ToStringStyle.MULTI_LINE_STYLE)
    }
}