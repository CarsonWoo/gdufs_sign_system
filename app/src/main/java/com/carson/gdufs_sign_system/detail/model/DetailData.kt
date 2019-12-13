package com.carson.gdufs_sign_system.detail.model

data class DetailData(val url: String,
                      val title: String,
                      val info: String?,
                      val type: String,
                      val time: String,
                      val area: String,
                      val signedPeople: Long,
                      val totalPeople: Long,
                      val endTime: String)