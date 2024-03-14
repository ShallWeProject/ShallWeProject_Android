import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ExperienceDetailRes(
    val data: GiftDTO,
    val transaction_time: String,
    val status: String,
    val description: String?,
    val statusCode: Int
) : Parcelable

@Parcelize
data class GiftDTO(
    val giftImgUrl: List<String>,
    val title: String,
    val subtitle: String,
    val price: Int,
    val explanation: List<Explanation>,
    val description: String,
    val location: String,
    val experienceGiftId: Int,
    val note: String?
) : Parcelable

@Parcelize
data class Explanation(
    val stage: String,
    val description: String,
    val explanationUrl: String
) : Parcelable
