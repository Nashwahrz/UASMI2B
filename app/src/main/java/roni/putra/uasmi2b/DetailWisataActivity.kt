package roni.putra.uasmi2b

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.squareup.picasso.Picasso

class DetailWisataActivity : AppCompatActivity() {
    private lateinit var imgWisata : ImageView
    private lateinit var tvNama : TextView
    private lateinit var tvNoTelp : TextView
    private lateinit var tvAlamat : TextView
    private lateinit var tvInformasi : TextView
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detail_wisata)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        imgWisata = findViewById(R.id.imgWisata)
        tvNama = findViewById(R.id.tvNama)
        tvNoTelp = findViewById(R.id.tvNoTelp)
        tvAlamat = findViewById(R.id.tvAlamat)
        tvInformasi = findViewById(R.id.tvDetailWisata)

        Picasso.get().load(intent.getStringExtra("gambar")).into(imgWisata)
        tvNama.text = intent.getStringExtra("nama_wisata")
        tvNoTelp.text = intent.getStringExtra("notlpn")
        tvAlamat.text = intent.getStringExtra("alamat")
        tvInformasi.text = intent.getStringExtra("deskripsi_wisata")

    }
}