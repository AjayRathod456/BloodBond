package uk.ac.tees.mad.bloodbond.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface MainDao {

    @Upsert()
    suspend fun insertDonor(jobs: List<MainEntity>)

    @Query("SELECT * FROM mainDb WHERE bloodGroup = :bloodGroup")
    suspend fun getDonorsByBloodGroup(bloodGroup: String): List<MainEntity>

    @Query("SELECT * FROM mainDb")
    suspend fun getAllDonors(): List<MainEntity>

}