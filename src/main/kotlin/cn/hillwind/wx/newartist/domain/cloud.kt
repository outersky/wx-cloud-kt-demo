package cn.hillwind.wx.newartist.domain

import cn.hillwind.wx.cloud.CloudBase
import cn.hillwind.wx.cloud.CloudDb
import cn.hillwind.wx.cloud.CloudStatic
import cn.hillwind.wx.cloud.DbComment
import com.github.binarywang.wxpay.service.WxPayService
import me.chanjar.weixin.common.error.WxErrorException

/**
﻿交易时间,公众账号ID,商户号,子商户号,设备号,微信订单号,商户订单号,用户标识,交易类型,交易状态,付款银行,货币种类,总金额,企业红包金额,微信退款单号,商户退款单号,退款金额,企业红包退款金额,退款类型,退款状态,商品名称,商户数据包,手续费,费率
`2020-01-04 17:43:52,`wx0e25d797adcdc399,`1314217101,`0,`,`4200000480202001043149128488,`aas,`od-im5MNGQTdvvJWDqhPM2aarDJY,`JSAPI,`SUCCESS,`BOSH_CREDIT,`CNY,`0.12,`0.00,`0,`0,`0.00,`0.00,`,`,`商品简单描述,`,`0.00000,`1.00%
`2020-01-04 16:45:31,`wx0e25d797adcdc399,`1314217101,`0,`,`4200000475202001040513553255,`111,`od-im5MNGQTdvvJWDqhPM2aarDJY,`JSAPI,`SUCCESS,`BOSH_CREDIT,`CNY,`0.01,`0.00,`0,`0,`0.00,`0.00,`,`,`商品简单描述,`,`0.00000,`1.00%
总交易单数,总交易额,总退款金额,总企业红包退款金额,手续费总金额
`2,`0.13,`0.00,`0.00,`0.00000
 */
@DbComment("对账单日期")
@CloudDb("billdates")
class BillDate : CloudBase() {

    @DbComment("对账单日期")
    var date: String = ""

    @DbComment("状态")
    var status: String = ""

    @DbComment("系统备注")
    var remarks: String? = ""

    @DbComment("总交易单数")
    var totalRecord: String? = null

    @DbComment("应结订单总金额")
    var totalFee: String? = null

    @DbComment("退款总金额")
    var totalRefundFee: String? = null

    @DbComment("充值券退款总金额")
    var totalCouponFee: String? = null

    @DbComment("手续费总金额")
    var totalPoundageFee: String? = null

    @DbComment("订单总金额")
    var totalAmount: String? = null

    @DbComment("申请退款总金额")
    var totalAppliedRefundFee: String? = null

    var bills: List<Bill>? = null

    override fun innerDataJson(): String {
        return """
            { 
                date: "$date",
                status: "$status",
                remarks: "${remarks ?: ""}",
                totalRecord: "${totalRecord ?: ""}",
                totalFee: "${totalFee ?: ""}",
                totalRefundFee: "${totalRefundFee ?: ""}",
                totalCouponFee: "${totalCouponFee ?: ""}",
                totalPoundageFee: "${totalPoundageFee ?: ""}",
                totalAmount: "${totalAmount ?: ""}",
                totalAppliedRefundFee: "${totalAppliedRefundFee ?: ""}"
            }
        """.trimIndent()
    }

    companion object : CloudStatic<BillDate>(BillDate::class.java) {
        @Throws(WxErrorException::class)
        fun findByDate(date: String): BillDate? {
            val list = BillDate.findList(""".where({date:"$date"}).limit(1)""")
            if (list.isNullOrEmpty()) return null
            return list[0]
        }

        fun download(payService: WxPayService, downloadBillDate: String): BillDate {
            val billResult = payService.downloadBill(downloadBillDate, "ALL", null, null)
            val billDate = BillDate()
            billDate.date = downloadBillDate
            billDate.totalAmount = billResult.totalAmount
            billDate.totalAppliedRefundFee = billResult.totalAppliedRefundFee
            billDate.totalCouponFee = billResult.totalCouponFee
            billDate.totalFee = billResult.totalFee
            billDate.totalPoundageFee = billResult.totalPoundageFee
            billDate.totalRecord = billResult.totalRecord
            billDate.totalRefundFee = billResult.totalRefundFee

            billDate.add()

            val bills = billResult.billInfoList.map { info ->
                Bill().apply {
                    date = downloadBillDate
                    tradeTime = info.tradeTime
                    appId = info.appId
                    mchId = info.mchId
                    subMchId = info.subMchId
                    deviceInfo = info.deviceInfo
                    transactionId = info.transactionId
                    outTradeNo = info.outTradeNo
                    openId = info.openId
                    tradeType = info.tradeType
                    tradeState = info.tradeState
                    feeType = info.feeType
                    totalFee = info.totalFee
                    couponFee = info.couponFee
                    refundId = info.refundId
                    outRefundNo = info.outRefundNo
                    settlementRefundFee = info.settlementRefundFee
                    couponRefundFee = info.couponRefundFee
                    refundChannel = info.refundChannel
                    refundState = info.refundState
                    body = info.body
                    attach = info.attach
                    poundage = info.poundage
                    poundageRate = info.poundageRate
                    totalAmount = info.totalAmount
                    appliedRefundAmount = info.appliedRefundAmount
                    feeRemark = info.feeRemark
                    refundTime = info.refundTime
                    refundSuccessTime = info.refundSuccessTime
                }
            }
            billDate.bills = bills
            return billDate
        }
    }

}

@DbComment("对账单")
@CloudDb("bills")
class Bill : CloudBase() {

    @DbComment("对账单日期")
    var date: String = ""

    @DbComment("状态")
    var status: String = ""

    @DbComment("系统备注")
    var remarks: String? = ""

    @DbComment("交易时间")
    var tradeTime: String? = null

    @DbComment("公众账号ID")
    var appId: String? = null

    @DbComment("商户号")
    var mchId: String? = null

    @DbComment("特约商户号")
    var subMchId: String? = null

    @DbComment("设备号")
    var deviceInfo: String? = null

    @DbComment("微信订单号")
    var transactionId: String? = null

    @DbComment("商户订单号")
    var outTradeNo: String? = null

    @DbComment("用户标识")
    var openId: String? = null

    @DbComment("交易类型")
    var tradeType: String? = null

    @DbComment("交易状态")
    var tradeState: String? = null

    @DbComment("付款银行")
    var bankType: String? = null

    @DbComment("货币种类")
    var feeType: String? = null

    @DbComment("应结订单金额")
    var totalFee: String? = null

    @DbComment("代金券金额")
    var couponFee: String? = null

    @DbComment("微信退款单号")
    var refundId: String? = null

    @DbComment("商户退款单号")
    var outRefundNo: String? = null

    @DbComment("退款金额")
    var settlementRefundFee: String? = null

    @DbComment("充值券退款金额")
    var couponRefundFee: String? = null

    @DbComment("退款类型")
    var refundChannel: String? = null

    @DbComment("退款状态")
    var refundState: String? = null

    @DbComment("商品名称")
    var body: String? = null

    @DbComment("商户数据包")
    var attach: String? = null

    @DbComment("手续费")
    var poundage: String? = null

    @DbComment("费率")
    var poundageRate: String? = null

    @DbComment("订单金额")
    var totalAmount: String? = null

    @DbComment("申请退款金额")
    var appliedRefundAmount: String? = null

    @DbComment("费率备注")
    var feeRemark: String? = null

    @DbComment("退款申请时间")
    var refundTime: String? = null

    @DbComment("退款成功时间")
    var refundSuccessTime: String? = null

    override fun innerDataJson(): String {
        return """
            { 
                date: "$date",
                status: "$status",
                remarks: "${remarks ?: ""}",
                tradeTime: "${tradeTime ?: ""}",
                appId: "${appId ?: ""}",
                mchId: "${mchId ?: ""}",
                subMchId: "${subMchId ?: ""}",
                deviceInfo: "${deviceInfo ?: ""}",
                transactionId: "${transactionId ?: ""}",
                outTradeNo: "${outTradeNo ?: ""}",
                openId: "${openId ?: ""}",
                tradeType: "${tradeType ?: ""}",
                tradeState: "${tradeState ?: ""}",
                bankType: "${bankType ?: ""}",
                feeType: "${feeType ?: ""}",
                totalFee: "${totalFee ?: ""}",
                couponFee: "${couponFee ?: ""}",
                refundId: "${refundId ?: ""}",
                outRefundNo: "${outRefundNo ?: ""}",
                settlementRefundFee: "${settlementRefundFee ?: ""}",
                couponRefundFee: "${couponRefundFee ?: ""}",
                refundChannel: "${refundChannel ?: ""}",
                refundState: "${refundState ?: ""}",
                body: "${body ?: ""}",
                attach: "${attach ?: ""}",
                poundage: "${poundage ?: ""}",
                poundageRate: "${poundageRate ?: ""}",
                totalAmount: "${totalAmount ?: ""}",
                appliedRefundAmount: "${appliedRefundAmount ?: ""}",
                feeRemark: "${feeRemark ?: ""}",
                refundTime: "${refundTime ?: ""}",
                refundSuccessTime: "${refundSuccessTime ?: ""}"
            }
        """.trimIndent()
    }

    companion object : CloudStatic<Bill>(Bill::class.java) {

    }

}