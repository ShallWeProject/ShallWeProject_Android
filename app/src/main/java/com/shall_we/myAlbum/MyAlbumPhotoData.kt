package com.shall_we.myAlbum

data class MyAlbumPhotoData(
    var myPhoto: MutableList<PhotoInfo>
) data class PhotoInfo(
    val isUploader: Boolean,
    val memoryPhotoImgUrl: String
)
