data class Pokemon(
    var imageSource: Int,
    var name: String,
    var type1: String,
    var type2: String
) {
    constructor(imageSource: Int, name: String, type1: String) : this(
        imageSource,
        name,
        type1,
        ""
    )
}