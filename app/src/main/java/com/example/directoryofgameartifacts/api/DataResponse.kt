package com.example.directoryofgameartifacts.api


data class marked_artifacts(
    val id: Int,
    val title: String,
    val text: String,
    val bookmarked: String
)

data class LoginResponse(
    val profile_id: Int,
    val message: String,
    val marked_artifacts: List<marked_artifacts>
)

data class Sign_inResponse(
    val profile_id: Int,
    val message: String
)

data class SaveResponse(
    val message: String
)



data class ArtifactsResponse(
    val artifact: List<marked_artifacts>
)