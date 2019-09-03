package id.ac.narotama.mo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import id.ac.narotama.mo.data.remote.APIEndpoint
import kotlinx.android.synthetic.main.activity_other.*
import kotlinx.coroutines.launch

class OtherActivity : AppActivity() {

    override val layoutId: Int = R.layout.activity_other

    companion object {

        fun launch(ctx: Context) {
            ctx.startActivity(Intent(ctx, OtherActivity::class.java))
        }

    }

    private val mNimTextWatcher by lazy {
        object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                val nim = p0.toString()

                // 04316034 - if length equal valid nim / 6
                if (nim.length == 8) {
                    fetchStudentByNim(nim)
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        btn_other_send.setOnClickListener {
            submitViolation()
        }
        et_other_nim.addTextChangedListener(mNimTextWatcher)
    }

    override fun onDestroy() {
        super.onDestroy()
        et_other_nim.removeTextChangedListener(mNimTextWatcher)
    }

    private fun showLoading() {
        et_other_nim.isEnabled = false
        et_other_violation.isEnabled = false
        et_other_punishment.isEnabled = false
    }

    private fun hideLoading() {
        et_other_nim.isEnabled = true
        et_other_violation.isEnabled = true
        et_other_punishment.isEnabled = true
    }

    private fun fetchStudentByNim(nim: String) {
        launch {
            showLoading()

            val results = APIEndpoint.Fallback(this@OtherActivity) {
                networkApi.getStudentList(nim).data
            }

            hideLoading()

            results?.let { data ->
                tv_other_name.text = data.nama
                tv_other_team.text = data.kelompok
            } ?: showMessage("Mahasiswa dgn NIM '$nim' tidak ditemukan")
        }
    }

    private fun submitViolation() {
        val nim = et_other_nim.text.toString()
        val violation = et_other_violation.text.toString()
        val punishment = et_other_punishment.text.toString()

        if (nim.isEmpty() || violation.isEmpty() || punishment.isEmpty()) {
            toast("Form harus diisi lengkap")
            return
        }

        launch {
            showLoading()
            val results = networkApi.postOtherRecord(nim, violation, punishment)
            hideLoading()

            if (results.code == 200) {
                toast(results.message ?: "Success")
                CategoryActivity.launchClearTask(this@OtherActivity)
            } else {
                toast(results.message ?: "Not success")
            }
        }
    }

}