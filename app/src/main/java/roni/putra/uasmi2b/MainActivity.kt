package roni.putra.uasmi2b

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import roni.putra.uasmi2b.adapter.WisataAdapter
import roni.putra.uasmi2b.api.ApiClient
import roni.putra.uasmi2b.model.WisataResponse

class MainActivity : AppCompatActivity() {
    private lateinit var svNama: SearchView
    private lateinit var progressBar: ProgressBar
    private lateinit var rvSekolah: RecyclerView
    private lateinit var floatbtnTambah: FloatingActionButton
    private lateinit var wisataAdapter: WisataAdapter
    private lateinit var imgNotFound: ImageView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        svNama = findViewById(R.id.svNama)
        progressBar = findViewById(R.id.progressBar)
        rvSekolah = findViewById(R.id.rvSekolah)
        floatbtnTambah = findViewById(R.id.floatBtnTambah)
        imgNotFound = findViewById(R.id.imgNotFound)

        getWisata("")


        svNama.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(pO: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(pencarian: String?): Boolean {
                getWisata(pencarian.toString())
                return true
            }
        })

    }

    private fun getWisata(nama_wisata: String){
        progressBar.visibility = View.VISIBLE
        ApiClient.apiService.getListWisata(nama_wisata).enqueue(object: Callback<WisataResponse>{
            override fun onResponse(
                call: Call<WisataResponse>,
                response: Response<WisataResponse>
            ) {
                if(response.isSuccessful){
                    if(response.body()!!.success){
                        //set data
                        wisataAdapter= WisataAdapter(arrayListOf())
                        rvSekolah.adapter = wisataAdapter
                        wisataAdapter.setData(response.body()!!.data)

                        // Sembunyikan gambar error
                        imgNotFound.visibility = View.GONE
                    }else{
                        //jika data tidak ditemukan
                        wisataAdapter= WisataAdapter(arrayListOf())
                        rvSekolah.adapter = wisataAdapter
                        imgNotFound.visibility= View.VISIBLE

                    }
                }
                progressBar.visibility= View.GONE
            }

            override fun onFailure(call: Call<WisataResponse>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error : ${t.message}", Toast.LENGTH_LONG)
                progressBar.visibility = View.GONE
            }

        })

        floatbtnTambah.setOnClickListener {
            startActivity(Intent(this@MainActivity,TambahWisataActivity::class.java))
        }
    }
}