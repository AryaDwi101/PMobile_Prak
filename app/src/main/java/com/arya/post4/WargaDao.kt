package com.arya.post4

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface WargaDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(warga: Warga)

    @Update
    fun update(warga: Warga)

    @Delete
    fun delete(warga: Warga)

    @Query("SELECT * from warga ORDER BY id DESC")
    fun getAllWarga(): List<Warga>

    @Query("SELECT * FROM warga WHERE id = :wargaId")
    fun getWargaById(wargaId: Int): Warga

    @Query("DELETE FROM warga")
    fun deleteAllWarga()
}