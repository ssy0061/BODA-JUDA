package com.aeye.nextlabel.model.network.api

import com.aeye.nextlabel.model.dto.Project
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ProjectApi {
    @GET("/admin/project/list")
    suspend fun getProjectByPage(@Query("page") page: Int): Response<List<Project>>
}