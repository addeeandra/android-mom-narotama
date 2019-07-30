package id.ac.narotama.mo

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import id.ac.narotama.mo.data.NetworkUtil
import id.ac.narotama.mo.data.remote.APIEndpoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

abstract class AppActivity : AppCompatActivity(), APIEndpoint.Noticable, CoroutineScope {

    override val coroutineContext: CoroutineContext = Dispatchers.Main

    protected val networkApi by lazy { NetworkUtil.getApi() }

    protected abstract val layoutId: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId)

        setToolbarNavigation()
    }

    private fun setToolbarNavigation() {
        findViewById<Toolbar>(R.id.toolbar)?.let { toolbar ->
            if (toolbar.navigationIcon != null) {
                toolbar.setNavigationOnClickListener { finish() }
            }
        }
    }

    protected fun toast(message: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(this, message, duration).show()
    }

    override fun showMessage(message: String) {
        toast(message)
    }

}