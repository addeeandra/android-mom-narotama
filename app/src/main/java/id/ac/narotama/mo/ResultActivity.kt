package id.ac.narotama.mo

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AlertDialog
import com.google.zxing.BarcodeFormat
import com.google.zxing.integration.android.IntentIntegrator
import id.ac.narotama.mo.data.model.QRData
import id.ac.narotama.mo.data.remote.APIEndpoint
import id.ac.narotama.mo.data.remote.response.Violation
import id.ac.narotama.mo.data.remote.response.Student
import kotlinx.android.synthetic.main.activity_result.*
import kotlinx.coroutines.launch
import java.util.*

class ResultActivity : AppActivity() {

    companion object {

        private const val KEY_DATA = "data_punishment"

        fun launch(ctx: Context, violation: Violation) {
            ctx.startActivity(Intent(ctx, ResultActivity::class.java).apply {
                putExtra(KEY_DATA, violation)
            })
        }

    }

    override val layoutId: Int = R.layout.activity_result

    private var qrdata: QRData? = null
    private var shouldCheckNim: Boolean = false
    private var mStudent = Student()

    private val mPunishment by lazy {
        intent.getSerializableExtra(KEY_DATA) as Violation
    }

    private val mNimTextWatcher by lazy {
        object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (shouldCheckNim) {
                    val nim = p0.toString()

                    // 04316034 - if length equal valid nim / 6
                    if (nim.length == 8) {
                        fetchStudentByNim(nim)
                    }
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()
        showChooserDialog()
    }

    override fun onDestroy() {
        super.onDestroy()
        et_result_nim.removeTextChangedListener(mNimTextWatcher)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_OK) finish()

        IntentIntegrator.parseActivityResult(requestCode, resultCode, data)?.let { intent ->
            intent.contents?.let { rawData ->
                qrdata = QRData.parse(rawData)
                qrdata?.let {
                    mStudent = Student(nim = it.nim, nama = it.name, kelompok = it.group)
                    showData(it)
                }
            }
        }
    }

    private fun setupView() {
        et_result_nim.addTextChangedListener(mNimTextWatcher)
        btn_result_send.setOnClickListener {
            sendReport(mStudent, mPunishment)
        }
    }

    private fun showData(data: QRData) {
        if (!shouldCheckNim) et_result_nim.setText(data.nim)

        et_result_nim.isEnabled = false
        et_result_nim.isClickable = false
        et_result_nim.isFocusable = false

        tv_result_name.text = data.name
        tv_result_violation.text = mPunishment.pelanggaran
        tv_result_reward.text = mPunishment.punisment

    }

    private fun showLoading() {
        et_result_nim.isEnabled = false
        btn_result_send.isEnabled = false
    }

    private fun hideLoading() {
        et_result_nim.isEnabled = true
        btn_result_send.isEnabled = true
    }

    private fun showChooserDialog() {
        AlertDialog
            .Builder(this)
            .setTitle("Pilih cara input")
            .setMessage(
                String.format(
                    Locale.getDefault(),
                    "Mahasiswa melakukan pelanggaran '%s'. Pilih input dengan Scan QR atau manual.",
                    mPunishment.pelanggaran
                )
            )
            .setNeutralButton("Batalkan") { _, _ -> finish() }
            .setNegativeButton("QRCode") { _, _ ->
                shouldCheckNim = false
                IntentIntegrator(this)
                    .setBeepEnabled(false)
                    .setBarcodeImageEnabled(false)
                    .setDesiredBarcodeFormats(BarcodeFormat.QR_CODE.name)
                    .setOrientationLocked(true)
                    .initiateScan()
            }
            .setPositiveButton("Manual") { dialog, _ ->
                shouldCheckNim = true
                dialog.dismiss()
            }
            .setCancelable(false)
            .show()
    }

    private fun fetchStudentByNim(nim: String) {
        launch {
            showLoading()

            val results = APIEndpoint.Fallback(this@ResultActivity) {
                networkApi.getStudentList(nim)
            }

            hideLoading()

            results?.firstOrNull()?.let { data ->
                qrdata = QRData(nim, data.nama ?: "-", data.kelompok ?: "-")
                qrdata?.let {
                    mStudent = data
                    showData(it)
                }
            } ?: showMessage("Mahasiswa dgn NIM '$nim' tidak ditemukan")
        }
    }

    private fun sendReport(student: Student, violation: Violation) {
        launch {
            showLoading()

            val results = APIEndpoint.Fallback(this@ResultActivity) {
                networkApi.postRecord(
                    student.nim ?: "",
                    violation.id ?: ""
                )
            }

            hideLoading()

            results?.firstOrNull()?.let {
                if (it.notice == "success") {
                    toast(it.message ?: "Success")
                    CategoryActivity.launchClearTask(this@ResultActivity)
                } else {
                    toast(it.message ?: "Not success")
                }
            }
        }
    }

}