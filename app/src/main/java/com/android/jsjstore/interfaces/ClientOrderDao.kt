package com.android.jsjstore.interfaces

import androidx.lifecycle.LiveData
import androidx.room.*
import com.android.jsjstore.model.ClientOrder

@Dao
interface ClientOrderDao {
    @Query("SELECT * FROM client_orders")
    fun getAll(): List<ClientOrder>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(clientOrder: ClientOrder)

    @Delete
    fun delete(clientOrder: ClientOrder)

    @Query("DELETE FROM client_orders")
    fun deleteAll()
}
