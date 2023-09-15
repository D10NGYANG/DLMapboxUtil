package com.d10ng.mapbox.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.AppText
import com.d10ng.compose.view.Input

@Composable
fun InputItem(
    value: String,
    onValueChange: (String) -> Unit = {},
    enabled: Boolean = true,
    onClick: () -> Unit = {},
    title: String,
    placeholder: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(top = 4.dp)
            .background(AppColor.Neutral.card)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, style = AppText.Normal.Title.v14)
        if (enabled) {
            Input(
                value = value,
                onValueChange = onValueChange,
                placeholder = placeholder,
                textStyle = AppText.Normal.Body.v14,
                placeholderStyle = AppText.Normal.Hint.v14,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(start = 8.dp),
                singleLine = true
            )
        } else {
            Text(
                text = value.ifEmpty { placeholder },
                style = if (value.isEmpty()) AppText.Normal.Hint.v14 else AppText.Normal.Body.v14,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(start = 8.dp)
                    .clickable { onClick() },
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}