package ru.gozerov.presentation.screens.trainer.diary.create_service.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import ru.gozerov.domain.models.UserCard
import ru.gozerov.presentation.R
import ru.gozerov.presentation.shared.views.SearchTextField
import ru.gozerov.presentation.shared.views.SimpleChatUserCard
import ru.gozerov.presentation.ui.theme.FitLadyaTheme

@Composable
fun AttachUserToCreating(
    searchState: MutableState<String>,
    onTextChange: (value: String) -> Unit,
    data: LazyPagingItems<UserCard>,
    onProfileClick: (client: UserCard) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(520.dp)
            .background(
                FitLadyaTheme.colors.primaryBackground,
                RoundedCornerShape(topEnd = 16.dp, topStart = 16.dp)
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .size(32.dp, 4.dp)
                .background(FitLadyaTheme.colors.text, RoundedCornerShape(100))
        )
        Spacer(modifier = Modifier.height(24.dp))
        SearchTextField(
            textState = searchState,
            onValueChange = onTextChange,
            placeholderText = stringResource(id = R.string.search_user),
            containerColor = FitLadyaTheme.colors.primaryBackground
        )
        Spacer(modifier = Modifier.height(12.dp))
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(data.itemCount) { index ->
                val client = data[index]
                client?.let {
                    SimpleChatUserCard(
                        user = client,
                        onProfileClick = {
                            onProfileClick(client)
                        }
                    )
                }
            }
        }
    }
}