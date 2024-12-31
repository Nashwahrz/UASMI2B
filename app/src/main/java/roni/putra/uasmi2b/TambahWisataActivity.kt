package roni.putra.uasmi2b

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.github.dhaval2404.imagepicker.ImagePicker
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import roni.putra.uasmi2b.api.ApiClient
import roni.putra.uasmi2b.model.TambahWisataResponse
import java.io.File

class TambahWisataActivity : AppCompatActivity() {
    private lateinit var etNama: EditText
    private lateinit var etNoTelp: EditText
    private lateinit var etAlamat: EditText
    private lateinit var etDetailWisata: EditText
    private lateinit var btnGambar: Button
    private lateinit var btnTambah: Button
    private lateinit var imgGambar: ImageView
    private lateinit var progressBar: ProgressBar
    private var imageFile: File? = null


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tambah_wisata)

        btnGambar = findViewById(R.id.btnGambar)
        imgGambar = findViewById(R.id.imgGambar)
        etNama = findViewById(R.id.etNama)
        btnTambah = findViewById(R.id.btnTambah)
        etDetailWisata = findViewById(R.id.etDetailWisata)
        etNoTelp = findViewById(R.id.etNoTelp)
        etAlamat = findViewById(R.id.etAlamat)

        progressBar = findViewById(R.id.progressBar)

        btnGambar.setOnClickListener {
            ImagePicker.with(this)
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start()
        }

        btnTambah.setOnClickListener {
            imageFile?.let {
                    file ->
                tambahWisata(etNama.text.toString(), etNoTelp.text.toString(), etAlamat.text.toString(), etDetailWisata.text.toString(), file)
            }
        }


    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            val uri = data.data!!
            imageFile = File(uri.path!!)
            imgGambar.visibility = View.VISIBLE
            imgGambar.setImageURI(uri)

        }
    }

    private fun tambahWisata(nama_wisata: String, notlpn : String, alamat: String, deskripsi_wisata: String, gambar : File ){
        progressBar.visibility = View.VISIBLE
        val requestBody = gambar.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val partFileGambar =
            MultipartBody.Part.createFormData("gambar", gambar.name, requestBody)

        val title = nama_wisata.toRequestBody("text/plain".toMediaTypeOrNull())
        val telepon = notlpn.toRequestBody("text/plain".toMediaTypeOrNull())
        val alamat = alamat.toRequestBody("text/plain".toMediaTypeOrNull())
        val deskripsiWisata = deskripsi_wisata.toRequestBody("text/plain".toMediaTypeOrNull())

        ApiClient.apiService.addWisata(title,telepon,alamat, deskripsiWisata, partFileGambar)
            .enqueue(object : Callback<TambahWisataResponse> {
                override fun onResponse(
                    call: Call<TambahWisataResponse>,
                    response: Response<TambahWisataResponse>
                ) {
                    if (response.isSuccessful) {
                        if (response.body()!!.success) {
                            startActivity(
                                Intent(
                                    this@TambahWisataActivity,
                                    MainActivity::class.java
                                )
                            )

                        } else {
                            Toast.makeText(
                                this@TambahWisataActivity,
                                response.body()!!.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }


                    } else {
                        Toast.makeText(
                            this@TambahWisataActivity,
                            response.body()!!.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    progressBar.visibility = View.GONE
                }

                override fun onFailure(call: Call<TambahWisataResponse>, t: Throwable) {
                    Toast.makeText(
                        this@TambahWisataActivity,
                        t.message,
                        Toast.LENGTH_SHORT
                    ).show()
                    progressBar.visibility = View.GONE

                }

            })
    }

}