package com.example.appweather.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.appweather.R
import com.example.appweather.ui.theme.AppWeatherTheme

@Composable
internal fun SearchAndGpsBar(
    modifier: Modifier = Modifier,
    searchQuery: String,
    isSearchVisible: Boolean,
    onSearchQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onUseGps: () -> Unit,
    onToggleSearchVisibility: () -> Unit,
) {
    val transition = updateTransition(targetState = isSearchVisible)

    val searchWidth by transition.animateDp(
        transitionSpec = { tween(durationMillis = 400, easing = FastOutSlowInEasing) }
    ) { visible ->
        if (visible) 220.dp else 0.dp
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Box(
            modifier = Modifier.width(searchWidth)
        ) {
            if (isSearchVisible) {
                CompactSearchField(
                    query = searchQuery,
                    onQueryChange = onSearchQueryChange,
                    onSearch = onSearch,
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(44.dp)
                )
            }
        }

        IconButton(onClick = { onToggleSearchVisibility() }) {
            Icon(
                imageVector = if (isSearchVisible) Icons.Filled.Search else Icons.Outlined.Search,
                contentDescription = stringResource(R.string.search_button),
                tint = if (isSearchVisible) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(24.dp)
            )
        }

        IconButton(
            onClick = onUseGps
        ) {
            Icon(
                imageVector = Icons.Outlined.LocationOn,
                contentDescription = "Use GPS",
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(24.dp)
            )
        }

        IconButton(
            onClick = onUseGps
        ) {
            Icon(
                imageVector = Icons.Filled.Notifications,
                contentDescription = "Notifications",
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
private fun CompactSearchField(
    modifier: Modifier = Modifier,
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .height(40.dp)
            .background(
                color = MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 12.dp)
            .fillMaxWidth()
    ) {
        BasicTextField(
            value = query,
            onValueChange = { if (it.length <= 19) onQueryChange(it) },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = { onSearch() }
            ),
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 4.dp, vertical = 0.dp)
        ) { innerTextField ->
            Box(
                modifier = Modifier.fillMaxHeight(),
                contentAlignment = Alignment.CenterStart
            ) {
                if (query.isEmpty()) {
                    Text(
                        text = stringResource(R.string.search_button_hint),
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                    )
                }
                innerTextField()
            }
        }

        if (query.isNotEmpty()) {
            Icon(
                imageVector = Icons.Default.Close,
                tint = MaterialTheme.colorScheme.secondary,
                contentDescription = "Clear",
                modifier = Modifier
                    .size(20.dp)
                    .offset((-20).dp)
                    .clickable { onQueryChange("") }
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun PreviewSearchAndGpsBar() {
    AppWeatherTheme {
        Surface {
            val searchQuery = remember { mutableStateOf("") }
            val isSearchVisible = remember { mutableStateOf(true) }

            SearchAndGpsBar(
                searchQuery = searchQuery.value,
                isSearchVisible = isSearchVisible.value,
                onSearchQueryChange = { searchQuery.value = it },
                onSearch = {},
                onUseGps = {},
                onToggleSearchVisibility = { isSearchVisible.value = !isSearchVisible.value }
            )
        }
    }
}