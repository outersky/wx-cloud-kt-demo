package cn.hillwind.wx.newartist.wxcloud

import cn.hillwind.wx.cloud.*
import cn.hillwind.wx.newartist.domain.Bill
import cn.hillwind.wx.newartist.domain.BillDate
import com.github.binarywang.wxpay.service.WxPayService
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import java.io.File

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WxCloudIntegrationTest {

    @LocalServerPort
    private var port: Int = 0

    @DisplayName("测试云函数")
    @Test
    fun invokeFunction() {
        val invokeResult = cloudService.functionService().invoke("login")
        println("invokeResult.result:")
        println(invokeResult.toJson())
    }

    @DisplayName("测试下载指定日期的对账单功能，并保存Bill 和 BillDate 实体")
    @Test
    fun downloadBill() {
        val billDate = BillDate.download(payService, "20200107")
        billDate.add() // 保存到云数据库
        Bill.addAll(* billDate.bills!!.toTypedArray()) // 批量保存到云数据库
    }

    @DisplayName("测试下载指定文件")
    @Test
    fun downloadFile() {
        val downloadResult = cloudService.storageService().getDownloadUrls(300, arrayListOf(
                "cloud://newartist-dev.6e65-newartist-dev-1301059729/works/20200114/150030_388.jpg",
                "cloud://newartist-dev.6e65-newartist-dev-1301059729/works/20200114/150322_342.jpg"
        ))
        println("downloadResult.result:")
        println(downloadResult.toJson())
        downloadResult.fileList?.forEach {
            val f = File("d:/tmp", it.fileId!!.substringAfterLast("/"))
            it.download(f.outputStream())
        }
    }

    @DisplayName("测试上传文件")
    @Test
    fun uploadFile() {
        val uploadResult = cloudService.storageService().getUploadUrl(
                "test/abc.jpg"
        )
        println("uploadResult.result:")
        println(uploadResult.toJson())
        uploadResult.upload(File("d:/tmp", "a.jpg"))
    }

    @DisplayName("测试删除文件")
    @Test
    fun deleteFile() {
        val deleteResult = cloudService.storageService().delete(
                arrayListOf("cloud://newartist-dev.6e65-newartist-dev-1301059729/my-image.png")
        )
        println("deleteResult.result:")
        println(deleteResult.toJson())
    }

    @DisplayName("测试数据库查询")
    @Test
    fun testQuery() {
        println("testQuery...")
        val stmt = DbStatement(Bill)
        stmt.apply(PageParam(3, 0)) // 只查询第一页的3条记录
        stmt.conditions.add(DbCondition("tradeState", "\"SUCCESS\"")) // 只看SUCCESS状态的
        val pagedList = stmt.query()
        println("total: ${pagedList.totalElements}")
        pagedList.content.forEach {
            println(it.toJson())
        }
    }

    //    @WxCloudAround("cloudService")
    @DisplayName("测试实体类的数据库查询简便方法")
    @Test
    fun testCloudWorks() {
        println("load BillDate list...")
        BillDate.findList().forEach {
            println(JsonHelper.toJson(it))
        }

        println("load single BillDate ...")
        val billDate = BillDate.find("59f543215e15c464004e687c49dbe35f")
        billDate?.also { println(it.toJson()) }
    }

    @Autowired
    lateinit var cloudService: WxCloudService

    @Autowired
    lateinit var payService: WxPayService

}