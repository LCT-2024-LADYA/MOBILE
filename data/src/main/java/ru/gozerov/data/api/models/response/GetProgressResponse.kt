package ru.gozerov.data.api.models.response

import ru.gozerov.domain.models.ProgressCard

data class GetProgressResponse(
    val is_more: Boolean,
    val objects: List<ProgressCard>
)
