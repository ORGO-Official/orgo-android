package com.orgo.feature.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.orgo.core.designsystem.R
import com.orgo.core.designsystem.theme.Pretendard
import com.orgo.core.ui.component.PretendardText
import com.orgo.core.ui.component.RoundedButton

@Composable
fun SettingsDialog(
    text: String,
    subText: String,
    onDismissRequest: () -> Unit,
    onSubmit: () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false,
        )
    ) {
        Surface(
            modifier = Modifier
                .width(400.dp)
                .height(200.dp)
                .padding(dimensionResource(id = R.dimen.setting_dialog_padding))
                .clip(RoundedCornerShape(dimensionResource(id = R.dimen.setting_dialog_roundedCorner)))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(dimensionResource(id = R.dimen.horizontal_margin)),
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    PretendardText(
                        text = text,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    PretendardText(
                        text = subText,
                        color = colorResource(id = R.color.gray),
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center,
                        lineHeight = 13.sp
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(dimensionResource(id = R.dimen.setting_dialog_button_height)),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RoundedButton(
                        modifier = Modifier
                            .weight(.5f)
                            .fillMaxHeight(),
                        basicBackgroundColor = colorResource(id = R.color.light_gray),
                        pressedBackgroundColor = colorResource(id = R.color.ios_blue),
                        onClick = onSubmit
                    ) {
                        PretendardText(
                            text = "예",
                            modifier = Modifier
                                .padding(horizontal = 16.dp, vertical = 1.dp),
                            fontSize = 12.sp,
                            color = colorResource(id = R.color.black),
                        )
                    }
                    Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.horizontal_margin)))
                    RoundedButton(
                        modifier = Modifier
                            .weight(.5f)
                            .fillMaxHeight(),
                        basicBackgroundColor = colorResource(id = R.color.light_gray),
                        pressedBackgroundColor = colorResource(id = R.color.ios_blue),
                        onClick = onDismissRequest
                    ) {
                        PretendardText(
                            text = "아니오",
                            modifier = Modifier
                                .padding(horizontal = 16.dp, vertical = 1.dp),
                            fontSize = 12.sp,
                            color = colorResource(id = R.color.black),
                        )
                    }

                }
            }

        }
    }
}