package id.ac.narotama.mo.data.remote

import id.ac.narotama.mo.data.remote.response.*
import retrofit2.http.*

interface APIEndpoint {

    companion object {

        suspend fun <T> Fallback(noticable: Noticable, exec: suspend () -> T?): T? {
            return try {
                exec()
            } catch (e: Exception) {
                noticable.showMessage("Terjadi kesalahan! Silakan cek internet Anda.")
                null
            }
        }

    }

    @GET("get_mahasiswa.php")
    suspend fun getStudentList(@Query("nim") nim: String): List<Student>

    @GET("get_category.php")
    suspend fun getCategoryList(): List<Category>

    @GET("get_pelanggaran.php")
    suspend fun getViolationList(@Query("id") categoryId: String): List<Violation>

    @GET("get_where_nama.php")
    suspend fun getStudentByName(@Query("nama") name: String): List<Student>

    @FormUrlEncoded
    @POST("post_login.php")
    suspend fun postLogin(@Field("nim") nim: String, @Field("password") password: String): List<Login>

    @FormUrlEncoded
    @POST("post_pencatatan.php")
    suspend fun postRecord(
        @Field("nim") nim: String,
        @Field("pelanggaran") punishmentId: String
    ): List<RecordResult>

    @FormUrlEncoded
    @POST("post_pencatatan_lainnya.php")
    suspend fun postOtherRecord(
        @Field("nim") nim: String,
        @Field("pelanggaran") violation: String,
        @Field("rewards") punishment: String
    ): List<RecordResult>

    interface Noticable {

        fun showMessage(message: String)

    }

}