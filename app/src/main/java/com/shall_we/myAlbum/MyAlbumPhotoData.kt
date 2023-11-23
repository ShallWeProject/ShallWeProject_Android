package com.shall_we.myAlbum

data class MyAlbumPhotoData(
    var imgUrl: MutableList<String>?
) {
    constructor() : this(
        imgUrl = null
    )
}
