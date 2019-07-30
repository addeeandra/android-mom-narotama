package id.ac.narotama.mo.data.remote.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import javax.annotation.Generated

@Generated("com.robohorse.robopojogenerator")
data class Category(

    @field:SerializedName("tgl_update")
    val tglUpdate: String? = null,

    @field:SerializedName("nama")
    val nama: String? = null,

    @field:SerializedName("id")
    val id: String? = null

) : Serializable