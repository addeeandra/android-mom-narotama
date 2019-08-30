package id.ac.narotama.mo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import id.ac.narotama.mo.data.remote.APIEndpoint
import id.ac.narotama.mo.data.remote.response.Category
import kotlinx.android.synthetic.main.activity_categories.*
import kotlinx.coroutines.launch

class CategoryActivity : AppActivity() {

    companion object {

        fun launchClearTask(ctx: Context) {
            ctx.startActivity(Intent(ctx, CategoryActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            })
        }

    }

    override val layoutId: Int = R.layout.activity_categories

    private val mSharedPreferencse by lazy {
        getSharedPreferences(
            "narotama-mom",
            Context.MODE_PRIVATE
        )
    }

    private val mCategoryAdapter by lazy {
        ArrayAdapter<String>(
            this,
            android.R.layout.simple_list_item_1,
            android.R.id.text1,
            mutableListOf()
        )
    }

    private lateinit var mCategories: List<Category>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        btn_categories_add_other.setOnClickListener {
            OtherActivity.launch(this@CategoryActivity)
        }

        toolbar.setOnMenuItemClickListener { menu ->
            if (menu.itemId == R.id.menu_logout) {
                showLogoutConfirmationDialog()
                true
            } else false
        }

        setupListView()
        fetchCategories()
    }

    private fun setupListView() {
        lv_categories.adapter = mCategoryAdapter
        lv_categories.setOnItemClickListener { _, _, position, _ ->
            ViolationActivity.launch(this@CategoryActivity, mCategories[position])
        }
    }

    private fun fetchCategories() {
        launch {
            mCategories =
                APIEndpoint.Fallback(this@CategoryActivity) { networkApi.getCategoryList() }
                    ?: listOf()

            mCategoryAdapter.clear()
            mCategoryAdapter.addAll(mCategories.map { it.nama })
            mCategoryAdapter.notifyDataSetChanged()
        }
    }

    private fun showLogoutConfirmationDialog() {
        AlertDialog
            .Builder(this)
            .setTitle("Keluar")
            .setMessage("Keluar dari akun saat ini?")
            .setNegativeButton("Batal") { dialog, _ -> dialog.dismiss() }
            .setPositiveButton("Ya, logout") { _, _ ->
                mSharedPreferencse.edit().remove(LoginActivity.SP_LOGIN_KEY).apply()
                startActivity(
                    Intent(this@CategoryActivity, LoginActivity::class.java).addFlags(
                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    )
                )
            }
            .show()
    }

}
