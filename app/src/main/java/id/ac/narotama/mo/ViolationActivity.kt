package id.ac.narotama.mo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import id.ac.narotama.mo.data.remote.APIEndpoint
import id.ac.narotama.mo.data.remote.response.Category
import id.ac.narotama.mo.data.remote.response.Violation
import kotlinx.android.synthetic.main.activity_violations.*
import kotlinx.coroutines.launch

class ViolationActivity : AppActivity() {

    companion object {

        private const val KEY_DATA = "data_category"

        fun launch(ctx: Context, category: Category) {
            ctx.startActivity(
                Intent(ctx, ViolationActivity::class.java).apply {
                    putExtra(KEY_DATA, category)
                }
            )
        }

    }

    override val layoutId: Int = R.layout.activity_violations

    private val mViolationAdapter by lazy {
        ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, mutableListOf())
    }

    private val mCategory by lazy {
        intent?.getSerializableExtra(KEY_DATA) as Category
    }

    private lateinit var mViolations: List<Violation>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupListView()
        fetchViolationList(mCategory.id ?: "")
    }

    private fun setupListView() {
        lv_violations.adapter = mViolationAdapter
        lv_violations.setOnItemClickListener { _, _, position, _ ->
            ResultActivity.launch(this@ViolationActivity, mViolations[position])
        }
    }

    private fun fetchViolationList(categoryId: String) {
        launch {
            mViolations = APIEndpoint.Fallback(this@ViolationActivity) {
                networkApi.getViolationList(categoryId).data
            } ?: listOf()

            mViolationAdapter.clear()
            mViolationAdapter.addAll(mViolations.map { it.pelanggaran })
            mViolationAdapter.notifyDataSetChanged()
        }
    }
}
