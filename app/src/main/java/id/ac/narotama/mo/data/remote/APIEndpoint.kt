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

    @GET("mahasiswa/by-nim/{nim}")
    suspend fun getStudentList(@Path("nim") nim: String): DataResponse<List<Student>>

    @GET("kategori")
    suspend fun getCategoryList(): DataResponse<List<Category>>

    @GET("pelanggaran/{id}")
    suspend fun getViolationList(@Path("id") categoryId: String): DataResponse<List<Violation>>

    @GET("mahasiswa/by-nama/{nama}")
    suspend fun getStudentByName(@Path("nama") name: String): DataResponse<List<Student>>

    @FormUrlEncoded
    @POST("masuk")
    suspend fun postLogin(@Field("nim") nim: String, @Field("password") password: String): DataResponse<List<Login>>

    @FormUrlEncoded
    @POST("punishment")
    suspend fun postRecord(
        @Field("nim") nim: String,
        @Field("pelanggaran") punishmentId: String
    ): DataResponse<List<RecordResult>>

    @FormUrlEncoded
    @POST("punishment-lainnya")
    suspend fun postOtherRecord(
        @Field("nim") nim: String,
        @Field("pelanggaran") violation: String,
        @Field("rewards") punishment: String
    ): DataResponse<List<RecordResult>>

    interface Noticable {

        fun showMessage(message: String)

    }

}