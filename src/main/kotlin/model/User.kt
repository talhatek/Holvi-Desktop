package model
@kotlinx.serialization.Serializable
data class User(val value:String,val sites: List<Site>)

@kotlinx.serialization.Serializable
data class Site(var siteName:String, var userName:String, var password:String)

@kotlinx.serialization.Serializable
data class Key(val secretKey:String)
