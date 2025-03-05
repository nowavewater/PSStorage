package com.waldemartech.psstorage.data.api

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.POST
import retrofit2.http.QueryMap
import retrofit2.http.Url

interface ApiDataSource {

    @GET
    suspend fun downloadFile(
        @Url url: String,
        @QueryMap params: Map<String, String>? = null,
        @HeaderMap headers: Map<String, String>? = null,
    ): ResponseBody

    @POST
    suspend fun downloadFileByPost(
        @Url url: String,
        @HeaderMap headers: Map<String, String>? = null
    ): ResponseBody

}