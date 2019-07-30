package id.ac.narotama.mo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_other.*
import kotlinx.coroutines.launch

class OtherActivity : AppActivity() {

    override val layoutId: Int = R.layout.activity_other

    companion object {

        fun launch(ctx: Context) {
            ctx.startActivity(Intent(ctx, OtherActivity::class.java))
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        btn_other_send.setOnClickListener {
            submitViolation()
        }
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

            results.firstOrNull()?.let {
                if (it.notice == "success") {
                    toast(it.message ?: "Success")
                    CategoryActivity.launchClearTask(this@OtherActivity)
                } else {
                    toast(it.message ?: "Not success")
                }
            }
        }
    }

}