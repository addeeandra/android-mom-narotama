package id.ac.narotama.mo.data.remote

import id.ac.narotama.mo.data.remote.response.Category
import id.ac.narotama.mo.data.remote.response.Violation
import id.ac.narotama.mo.data.remote.response.RecordResult
import id.ac.narotama.mo.data.remote.response.Student
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