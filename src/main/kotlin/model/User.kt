package model
@kotlinx.serialization.Serializable
data class User(val value:String,val sites: List<Site>)

@kotlinx.serialization.Serializable
data class Site(val siteName:String,val userName:String,val password:String)