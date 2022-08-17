package com.vsharkovski.easyrideshare.domain

sealed interface PostResult

data class PostCreated(val post: Post) : PostResult

object PostDatabaseSavingFail : PostResult

object PostMissingUserFail : PostResult

object PostInvalidLocationFail : PostResult

object PostInvalidParametersFail : PostResult