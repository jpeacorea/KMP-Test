package ni.gob.minsa.myapplication

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform