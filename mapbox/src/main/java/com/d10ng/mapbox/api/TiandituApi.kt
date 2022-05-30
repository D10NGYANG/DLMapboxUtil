package com.d10ng.mapbox.api

import com.d10ng.http.Api
import io.ktor.client.request.*
import io.ktor.client.statement.*

object TiandituApi {

    suspend fun getNormalSearch(): HttpResponse? = Api.handlerResponse {
        it.get("http://api.tianditu.gov.cn/v2/search?postStr={\"keyWord\":\"北京大学\",\"level\":12,\"mapBound\":\"116.02524,39.83833,116.65592,39.99185\",\"queryType\":1,\"start\":0,\"count\":10}&type=query&tk=f7e6119545c218a681fdfda020e990b5") {
            header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/15.5 Safari/605.1.15")
        }
    }
}