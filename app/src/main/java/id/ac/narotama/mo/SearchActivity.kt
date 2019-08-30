package id.ac.narotama.mo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import id.ac.narotama.mo.data.remote.APIEndpoint
import id.ac.narotama.mo.data.remote.response.Student
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.coroutines.launch

class SearchActivity : AppActivity() {

    companion object {

        fun launch(activity: Activity, requestCode: Int = 22) {
            val launchIntent = Intent(activity, SearchActivity::class.java)
            activity.startActivityForResult(launchIntent, requestCode)
        }

    }

    override val layoutId: Int = R.layout.activity_search

    private val mStudentAdapter by lazy {
        ArrayAdapter<String>(
            this,
            android.R.layout.simple_list_item_1,
            android.R.id.text1,
            mutableListOf()
        )
    }

    private lateinit var mStudents: List<Student>

    private var mIsFetching: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupView()
        setupListView()
    }

    private fun setupView() {
        et_search_input.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                fetchStudent(et_search_input.text.toString())
                true
            } else false
        }

        btn_search_submit.setOnClickListener {
            fetchStudent(et_search_input.text.toString())
        }

        clp_search.hide()
    }

    private fun setupListView() {
        lv_search_student.adapter = mStudentAdapter
        lv_search_student.setOnItemClickListener { _, _, position, _ ->
            val data = Intent().apply {
                putExtra("NIM", mStudents[position].nim)
                putExtra("NAME", mStudents[position].nama)
                putExtra("GROUP", mStudents[position].kelompok)
            }

            setResult(Activity.RESULT_OK, data)
            finish()
        }
    }

    private fun showLoading() {
        et_search_input.isEnabled = false
        clp_search.show()
        lv_search_student.visibility = View.INVISIBLE
        mIsFetching = true
    }

    private fun hideLoading() {
        mIsFetching = false
        lv_search_student.visibility = View.VISIBLE
        clp_search.hide()
        et_search_input.isEnabled = true
    }

    private fun fetchStudent(search: String) {
        launch {
            showLoading()
            mStudents =
                APIEndpoint.Fallback(this@SearchActivity) { networkApi.getStudentByName(search) }
                    ?: listOf()
            hideLoading()

            mStudentAdapter.clear()
            mStudentAdapter.addAll(mStudents.map { "(${it.nim}) ${it.nama}" })
            mStudentAdapter.notifyDataSetChanged()
        }
    }

}