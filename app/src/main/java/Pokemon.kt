import android.graphics.drawable.Drawable

data class Pokemon(
    var id: Int,
    var imageSource: Drawable?,
    var name: String?,
    var type1: String,
    var type2: String
) {
}