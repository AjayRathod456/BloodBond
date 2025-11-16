package uk.ac.tees.mad.bloodbond.utils

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.GoTrue
import io.github.jan.supabase.storage.Storage

object SupabaseClientProvider {
    val client =
        createSupabaseClient(
            supabaseUrl = "https://cjhupvrexxbptvubwbdm.supabase.co",
            supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImNqaHVwdnJleHhicHR2dWJ3YmRtIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTc5NDM0MTEsImV4cCI6MjA3MzUxOTQxMX0.KQqRiJlSHrrpuzObBtojvA7Su-IC2l00D7QCEmlzk1U"
        ) {

            install(GoTrue)

            install(Storage)
        }

}