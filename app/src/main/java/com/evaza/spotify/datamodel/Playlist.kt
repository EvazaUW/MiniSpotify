package com.evaza.spotify.datamodel

import com.google.gson.annotations.SerializedName

class Playlist (
    @SerializedName("id")
    val albumId: String,
    val songs: List<Song>
)