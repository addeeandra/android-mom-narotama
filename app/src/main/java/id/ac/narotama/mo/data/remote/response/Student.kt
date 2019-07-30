package id.ac.narotama.mo.data.remote.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import javax.annotation.Generated

@Generated("com.robohorse.robopojogenerator")
data class Student(

    @field:SerializedName("tgl_update")
    val tglUpdate: String? = null,

    @field:SerializedName("kelompok")
    val kelompok: String? = null,

    @field:SerializedName("nim")
    val nim: String? = null,

    @field:SerializedName("nama")
    val nama: String? = null,

    @field:SerializedName("id")
    val id: String? = null

) : Serializable // NB : SERIALIZEABLE are not recommended for HEAVY OBJECT