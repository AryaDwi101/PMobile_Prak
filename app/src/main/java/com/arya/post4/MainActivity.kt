package com.arya.post4

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.arya.post4.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var dbWarga: DatabaseWarga
    private lateinit var wargaDao: WargaDao
    private lateinit var appExecutors: AppExecutor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        appExecutors = AppExecutor()
        dbWarga = DatabaseWarga.getDatabase(applicationContext)
        wargaDao = dbWarga.wargaDao()

        setupListeners()

        loadInitialData()
    }

    private fun setupListeners() {
        binding.btnSubmit.setOnClickListener {
            simpanDataWarga()
        }
        binding.btnReset.setOnClickListener {
            hapusSemuaData()
            resetForm()
        }
    }

    private fun loadInitialData() {
        appExecutors.diskIO.execute {
            val initialData = wargaDao.getAllWarga()

            appExecutors.mainThread.execute {
                updateWargaUI(initialData)
            }
        }
    }

    private fun simpanDataWarga() {
        binding.apply {
            val nama = etNamaLengkap.text.toString().trim()
            val nik = etNik.text.toString().trim()

            if (nama.isEmpty() || nik.isEmpty()) {
                Toast.makeText(this@MainActivity, "Nama dan NIK tidak boleh kosong!", Toast.LENGTH_SHORT).show()
                return
            }

            val selectedJenisKelaminId = radiogrup.checkedRadioButtonId
            val selectedRadioButton = findViewById<RadioButton>(selectedJenisKelaminId)
            val jenisKelamin = selectedRadioButton.text.toString()
            val statusPernikahan = spinnerStatusPernikahan.selectedItem.toString()

            val newWarga = Warga(
                namaLengkap = nama,
                nik = nik,
                kabupaten = etKabupaten.text.toString().trim(),
                kecamatan = etKecamatan.text.toString().trim(),
                desa = etDesa.text.toString().trim(),
                rt = etRT.text.toString().trim(),
                rw = etRW.text.toString().trim(),
                jenisKelamin = jenisKelamin,
                statusPernikahan = statusPernikahan
            )

            appExecutors.diskIO.execute {
                wargaDao.insert(newWarga)

                val dataTerbaru = wargaDao.getAllWarga()

                appExecutors.mainThread.execute {
                    Toast.makeText(this@MainActivity, "Data berhasil disimpan!", Toast.LENGTH_SHORT).show()
                    resetForm()

                    updateWargaUI(dataTerbaru)
                }
            }
        }
    }

    private fun resetForm() {
        binding.apply {
            etNamaLengkap.text.clear()
            etNik.text.clear()
            etKabupaten.text.clear()
            etKecamatan.text.clear()
            etDesa.text.clear()
            etRT.text.clear()
            etRW.text.clear()
            rdb1.isChecked = true
            spinnerStatusPernikahan.setSelection(0)
            etNamaLengkap.requestFocus()
        }
    }

    private fun updateWargaUI(listWarga: List<Warga>) {
        binding.containerDataWarga.removeAllViews()
        for (warga in listWarga) {
            val itemContainer = LinearLayout(this@MainActivity)
            itemContainer.orientation = LinearLayout.VERTICAL

            val itemLayoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin = 24
            }
            itemContainer.layoutParams = itemLayoutParams
            itemContainer.setPadding(32, 32, 32, 32)
            itemContainer.setBackgroundColor(Color.parseColor("#F0F0F0"))

            // B. Buat TextView untuk Nama
            val tvNama = TextView(this@MainActivity).apply {
                text = warga.namaLengkap
                textSize = 18f
                setTypeface(null, Typeface.BOLD)
                setTextColor(Color.BLACK)
            }

            val tvNik = TextView(this@MainActivity).apply {
                text = "NIK: ${warga.nik}"
                textSize = 14f
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply { topMargin = 8 }
            }

            val tvAlamat = TextView(this@MainActivity).apply {
                text = "Alamat: ${warga.desa}, ${warga.kecamatan}, ${warga.kabupaten}"
                textSize = 14f
            }

            val tvInfo = TextView(this@MainActivity).apply {
                text = "Info: ${warga.jenisKelamin}, ${warga.statusPernikahan}"
                textSize = 14f
            }

            itemContainer.addView(tvNama)
            itemContainer.addView(tvNik)
            itemContainer.addView(tvAlamat)
            itemContainer.addView(tvInfo)
            binding.containerDataWarga.addView(itemContainer)
        }
    }

    private fun hapusSemuaData() {
        appExecutors.diskIO.execute {
            wargaDao.deleteAllWarga()
            appExecutors.mainThread.execute {
                updateWargaUI(emptyList())
                Toast.makeText(this@MainActivity, "Semua data berhasil direset!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}