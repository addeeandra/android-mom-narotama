package id.ac.narotama.mo.data.remote.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import javax.annotation.Generated

@Generated("com.robohorse.robopojogenerator")
data class Violation(

    @field:SerializedName("pelanggaran")
    val pelanggaran: String? = null,

    @field:SerializedName("tgl_update")
    val tglUpdate: String? = null,

    @field:SerializedName("id_category")
    val idCategory: String? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("punisment")
    val punisment: String? = null

) : Serializable