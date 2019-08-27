package id.ac.narotama.mo.data.remote.response

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class Login(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("nim")
	val nim: String? = null,

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("divisi")
	val divisi: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)