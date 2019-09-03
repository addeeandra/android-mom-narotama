package id.ac.narotama.mo

import android.content.Context
import android.os.Bundle
import id.ac.narotama.mo.data.remote.APIEndpoint
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.launch

class LoginActivity : AppActivity(), APIEndpoint.Noticable {

    companion object {

        const val SP_LOGIN_KEY = "is_logged_in"

    }

    override val layoutId: Int = R.layout.activity_login

    private val mSharedPreferencse by lazy {
        getSharedPreferences(
            "narotama-mom",
            Context.MODE_PRIVATE
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (hasLoggedIn()) CategoryActivity.launchClearTask(this)

        btn_login_submit.setOnClickListener {
            postLogin()
        }
    }

    private fun showLoading() {
        et_login_nim.isEnabled = false
        et_login_password.isEnabled = false
        btn_login_submit.isEnabled = false
    }

    private fun hideLoading() {
        et_login_nim.isEnabled = true
        et_login_password.isEnabled = true
        btn_login_submit.isEnabled = true
    }

    private fun hasLoggedIn(): Boolean {
        return mSharedPreferencse.getBoolean(SP_LOGIN_KEY, false)
    }

    private fun setLoggedIn() {
        mSharedPreferencse.edit().putBoolean(SP_LOGIN_KEY, true).apply()
    }

    private fun postLogin() {
        val username = et_login_nim.text.toString()
        val password = et_login_password.text.toString()

        if (username.isEmpty() || password.isEmpty()) {
            showMessage("Semua input harus diisi")
            return
        }

        showLoading()

        launch {
            val results = APIEndpoint.Fallback(this@LoginActivity) {
                networkApi.postLogin(username, password).data
            }

            hideLoading()

            results?.firstOrNull()?.let {
                if (it.code == 200) {
                    setLoggedIn()
                    CategoryActivity.launchClearTask(this@LoginActivity)
                } else showMessage("Login gagal")
            } ?: showMessage("Login gagal")
        }
    }

}