package roni.putra.uasmi2b.model

data class WisataResponse(

    val success : Boolean,
    val message : String,
    val data : ArrayList<ListItems>
) {
    data class ListItems(
        val id: String,
        val nama_wisata: String,
        val notlpn: String,
        val alamat : String,
        val deskripsi_wisata: String,
        val gambar : String,

        )
}