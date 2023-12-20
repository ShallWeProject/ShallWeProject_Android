package com.shall_we.myAlbum

data class MyAlbumData(
    val date: String,
    val time: String,
    val experienceGiftTitle: String,
    val experienceGiftSubTitle: String,
    var memoryPhotoImgs: MutableList<Photos>,
) {
    data class Photos(
        val isUploader: Boolean,
        val memoryPhotoImgUrl: String
    )}