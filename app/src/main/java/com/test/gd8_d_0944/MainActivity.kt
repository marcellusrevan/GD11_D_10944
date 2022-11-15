package com.test.gd8_d_0944

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.test.gd8_d_0944.adapters.MahasiswaAdapter
import com.test.gd8_d_0944.api.MahasiswaApi
import com.test.gd8_d_0944.models.Mahasiswa

class MainActivity : AppCompatActivity() {
    private var srMahasiswa : SwipeRefreshLayout? = null
    private var adapter : MahasiswaAdapter? = null
    private var svMahasiswa : SearchView? = null
    private var layoutLoading : LinearLayout? = null
    private var queue : RequestQueue? = null

    companion object{
        const val LAUNCH_ADD_ACTIVITY = 123
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        queue=Volley.newRequestQueue(this)
        layoutLoading=findViewById(R.id.layout_loading)
        srMahasiswa=findViewById(R.id.sr_mahasiswa)
        svMahasiswa=findViewById(R.id.sv_mahasiswa)

        srMahasiswa?.setOnRefreshListener (SwipeRefreshLayout.OnRefreshListener{allMahasiswa()})
        svMahasiswa?.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(s: String): Boolean {
                return false
            }

            override fun onQueryTextChange(s: String?): Boolean {
                adapter!!.filter.filter(s)
                return false
            }
        })
        val fabAdd = findViewById<FloatingActionButton>(R.id.fab_add)
        fabAdd.setOnClickListener{
            val i = Intent(this@MainActivity,AddEditActivity::class.java)
            startActivityForResult(i, LAUNCH_ADD_ACTIVITY)
        }
        val rvProduk= findViewById<RecyclerView>(R.id.rv_mahasiswa)
        adapter= MahasiswaAdapter(ArrayList(),this)
        rvProduk.adapter = adapter
        allMahasiswa()
    }
    private fun allMahasiswa(){
        srMahasiswa!!.isRefreshing = true
        val stringRequest: StringRequest= object :
        StringRequest(Method.GET,MahasiswaApi.GET_ALL_URL,Response.Listener { response ->
            val gson = Gson()
            var mahasiswa : Array<Mahasiswa> = gson.fromJson(response,Array<Mahasiswa>::class.java)
            adapter!!.setMahasiswaList(mahasiswa)
            adapter!!.filter.filter(svMahasiswa!!.query)
            srMahasiswa!!.isRefreshing=false
            if (!mahasiswa.isEmpty())
                Toast.makeText(this@MainActivity, "Data Berhasil Diambil", Toast.LENGTH_SHORT).show()
        })
    }
}