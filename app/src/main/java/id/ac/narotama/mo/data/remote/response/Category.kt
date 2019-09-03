package id.ac.narotama.mo.data.remote.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import javax.annotation.Generated

@Generated("com.robohorse.robopojogenerator")
data class Category(

    @field:SerializedName("kategori_pelanggaran")
    val nama: String? = null,

    @field:SerializedName("id_kategori")
    val id: String? = null

) : Serializable