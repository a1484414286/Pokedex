import android.graphics.drawable.Drawable

data class Pokemon(
    var id: Int,
    var imageSource: Drawable?,
    var name: String?,
    var type1: String,
    var type2: String
) {
    constructor(id : Int, imageSource: Drawable, name: String, type1: String) : this(
        id,
        imageSource,
        name,
        type1,
        ""
    )
}