package com.test.gd8_d_0944

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.*
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.test.gd8_d_0944.api.MahasiswaApi
import com.test.gd8_d_0944.models.Mahasiswa
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class AddEditActivity : AppCompatActivity() {
    companion object{
        private val FAKULTAS_LIST = arrayOf("FTI","FT","FTB","FBE","FISIP","FH")
        private val PRODI_LIST = arrayOf(
            "Informatika",
            "Arsitektur",
            "Biologi",
            "Manajemen",
            "Ilmu Komunikasi",
            "Ilmu Hukum"
        )
    }
    private var etNama: EditText? = null
    private var etNPM: EditText? = null
    private var edFakultas: AutoCompleteTextView? = null
    private var edProdi: AutoCompleteTextView? = null
    private var layoutLoading: LinearLayout? = null
    private var queue: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit)

        queue = Volley.newRequestQueue(this)
        etNama = findViewById(R.id.et_nama)
        etNPM = findViewById(R.id.et_npm)
        edFakultas = findViewById(R.id.ed_fakultas)
        edProdi = findViewById(R.id.ed_prodi)
        layoutLoading = findViewById(R.id.layout_loading)

        setExposedDropDownMenu()

        val btnCancel = findViewById<Button>(R.id.btn_cancel)
        btnCancel.setOnClickListener{finish()}
        val btnSave = findViewById<Button>(R.id.btn_save)
        val tvTitle = findViewById<TextView>(R.id.tv_title)
        val id = intent.getLongExtra("id",-1)
        if (id == -1L){
            tvTitle.setText("Tambah Mahasiswa")
            btnSave.setOnClickListener { createMahasiswa() }
        }else{
            tvTitle.setText("Edit Mahasiswa")
            getMahasiswaById(id)
            btnSave.setOnClickListener { updateMahasiswa(id) }
        }
    }
    fun setExposedDropDownMenu(){
        val adapterFakultas: ArrayAdapter<String> = ArrayAdapter<String>(this,
        R.layout.item_list, FAKULTAS_LIST)
        edFakultas!!.setAdapter(adapterFakultas)

        val adapterProdi: ArrayAdapter<String> = ArrayAdapter<String>(this,
        R.layout.item_list, PRODI_LIST)
        edProdi!!.setAdapter(adapterProdi)
    }
    private fun getMahasiswaById(id: Long){
        setLoading(true)
        val stringRequest: StringRequest = object :
            StringRequest(Method.GET,MahasiswaApi.GET_BY_ID_URL+id,Response.Listener { response ->
                setLoading(false)
                val gson=Gson()
                var mahasiswa = gson.fromJson(response,Mahasiswa::class.java)
                if (mahasiswa != null)
                    Toast.makeText(this@MainActivity, "Data Berhasil Dihapus", Toast.LENGTH_SHORT).show()
                allMahasiswa()
            },Response.ErrorListener { error ->
                setLoading(false)
                try {
                    val responseBody = String(error.networkResponse.data,StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this@MainActivity, "message", Toast.LENGTH_SHORT).show()
                }catch (e:java.lang.Exception){
                    Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            }){
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers=java.util.HashMap<String,String>()
                headers["Accept"]="application/json"
                return headers
            }
            }
        queue!!.add(stringRequest)
    }
    override
}