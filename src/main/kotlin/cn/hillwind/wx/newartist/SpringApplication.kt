package cn.hillwind.wx.newartist

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpringApplication {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            runApplication<SpringApplication>(*args)
        }
    }

}
