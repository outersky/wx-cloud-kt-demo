package cn.hillwind.wx.newartist.util

import cn.hillwind.wx.cloud.PageParam
import cn.hillwind.wx.cloud.PageReq
import cn.hillwind.wx.cloud.SearchParam
import cn.hillwind.wx.cloud.SortParam
import javax.servlet.http.HttpServletRequest

fun PageReq.Companion.parse(request: HttpServletRequest): PageReq {
    val searchParams = SearchParam.parse(request)
    val sortParam = SortParam.parse(request) ?: SortParam("")
    val pageParam = PageParam.parse(request) ?: PageParam(10, 0)
    return PageReq(pageParam, searchParams, sortParam)
}

fun SearchParam.Companion.parse(request: HttpServletRequest): Collection<SearchParam> {
    return request.parameterMap.filter { it.key.startsWith("search_") }.map { (k, v) ->
        //key: search_CONTAIN_name_STRING or search_CONTAIN_name
        val arr = k.split('_')
        if (arr.size >= 3) {
            val oper = arr[1]
            val property = arr[2]
            var kind = "String"
            if (arr.size >= 4) {
                kind = arr[3]
            }
            SearchParam(property, oper, v[0], kind).also { it.raw = k }
        } else {
            null
        }
    }.filterNotNull()
}

fun SortParam.Companion.parse(request: HttpServletRequest): SortParam? {
    var name = request.getParameter("page_sort") ?: return null
    name = name.trim()
    if (name.isEmpty()) return null
    val direction = request.getParameter("page_sort_dir")
    return if (direction != null && direction.isNotBlank()) {
        SortParam(name, direction.trim())
    } else {
        SortParam(name)
    }
}

/**
 * 获取参数或者cookie: page_size (因为页面大小是全局的，需要用cookie记住)
 * 获取参数：page_index , 页面传过来的参数需要-1
 */
fun PageParam.Companion.parse(request: HttpServletRequest): PageParam? {
    val pageSize = request.getParameter("page_size")?.toInt()
            ?: request.cookies?.find { it.name == "page_size" }?.value?.toInt() ?: 10
    val pageIndex = request.getParameter("page_index")?.toInt() ?: 1
    return PageParam(pageSize, pageIndex - 1)
}
