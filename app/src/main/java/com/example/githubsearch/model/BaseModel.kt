package com.example.githubsearch.model

import com.google.gson.annotations.SerializedName
import java.security.acl.Owner


data class BaseModel (

    @SerializedName("total_count") val total_count : Int,
    @SerializedName("incomplete_results") val incomplete_results : Boolean,
    @SerializedName("items") val items : List<Items>
)