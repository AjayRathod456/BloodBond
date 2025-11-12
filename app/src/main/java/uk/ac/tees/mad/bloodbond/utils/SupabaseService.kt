package uk.ac.tees.mad.bloodbond.utils

import com.google.android.gms.auth.api.signin.internal.Storage
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.storage.storage

object SupabaseService {
    val client: SupabaseClient = createSupabaseClient(
        supabaseUrl = "https://ftnmtcfmdobyxenfqlsl.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImZ0bm10Y2ZtZG9ieXhlbmZxbHNsIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTc4NzUwMDgsImV4cCI6MjA3MzQ1MTAwOH0.i2fJT2RwVKWW7QsPFrkQ28J5Qm9ztdjU_ofazWVb2Z8"
    ) {
        install(io.github.jan.supabase.storage.Storage)

        // for file storage
    }

    val bucket = client.storage.from("doner_id_images")
}