package pl.ejdev.spotifyplugin.model

enum class ProductType(val type: String) {
    BASIC_DESKTOP("basic-desktop"),
    DAYPASS(("daypass")),
    FREE("free"),
    OPEN("open"),
    PREMIUM("premium");
}