package com.d10ng.mapbox.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.AppShape
import com.d10ng.compose.ui.AppText

@Composable
fun SureButton(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(32.dp, 8.dp),
    text: String = "确定",
    onClick: () -> Unit
) {
    Button(
        shape = AppShape.RC.Cycle,
        colors = ButtonDefaults.buttonColors(
            containerColor = AppColor.Main.primary
        ),
        contentPadding = contentPadding,
        modifier = modifier,
        onClick = onClick,
        elevation = null
    ) {
        Text(
            text = text,
            style = AppText.Bold.White.v14
        )
    }
}

@Preview
@Composable
private fun SureButton_Test() {
    Column {
        SureButton() {}
    }
}