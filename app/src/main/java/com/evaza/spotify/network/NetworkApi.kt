package com.evaza.spotify.network

import android.provider.MediaStore.Audio.Playlists
import com.evaza.spotify.datamodel.Playlist
import com.evaza.spotify.datamodel.Section
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface NetworkApi {
    // Restful Get Api call, "feed"
    @GET("feed")
    fun getHomeFeed(): Call<List<Section>>

//    @GET("playlists")
//    fun getHomeFeed(): Call<List<Playlist>>
    @GET("playlist/{id}")
    fun getPlaylist(@Path("id") id: Int): Call<Playlist>
}