package id.ac.narotama.mo.data.remote.response

import com.google.gson.annotations.SerializedName
import javax.annotation.Generated

@Generated("com.robohorse.robopojogenerator")
data class DataResponse<T>(

    @field:SerializedName("code")
    val code: Int? = null,

    @field:SerializedName("data")
    val data: T? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: String? = null
)