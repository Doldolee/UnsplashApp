package com.seook.api_example.utils

enum class SEARCH_TYPE {
    PHOTO,
    USER
}

enum class RESPONSE_STATE{
    OKAY,
    FAIL,
    NO_CONTENT
}

object API {
    const val BASE_URL :String = "https://api.unsplash.com/"

    const val CLIENT_ID :String="hXD12iEjSWE3wtK0qGUwJvan4k3NXWSBklCdxplRvvk"

    const val SEARCH_PHOTO :String = "search/photos"
    const val SEARCH_USERS :String = "search/users"
}