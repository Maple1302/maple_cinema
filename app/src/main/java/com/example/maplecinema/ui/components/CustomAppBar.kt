package com.example.maplecinema.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.maplecinema.R

@Composable
fun AppBar(
    modifier: Modifier = Modifier,
    isTextHeader: Boolean = false,
    textHeader: String = "",
    iconFirst: Int? = R.drawable.cast,
    iconHead: Int? = R.drawable.logo,
    iconSizeHead: Dp = 40.dp,
    iconSecond: Int? = R.drawable.icon_dowload,
    iconThird: ImageVector? = Icons.Default.Search,
    onIconHeadClick: () -> Unit = {},
    onIconSecondClick: () -> Unit = {},
    onIconThirdClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 40.dp, bottom = 5.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (!isTextHeader) {

            Box(
                Modifier.clickable(
                    role = Role.Button,
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }) { onIconHeadClick() }) {
                iconHead?.let { painterResource(id = it) }?.let {
                    Image(
                        alignment = Alignment.CenterStart,
                        painter = it,
                        contentDescription = "Logo App",
                        modifier = Modifier
                            .size(iconSizeHead)
                            .padding(start = 12.dp),
                        colorFilter = if (iconHead != R.drawable.logo) ColorFilter.tint(color = Color.White) else null
                    )
                }
            }
        } else {
            Text(textHeader)
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(15.dp),
            modifier = Modifier.padding(horizontal = 10.dp)
        ) {
            iconFirst?.let { painterResource(id = it) }?.let {
                Image(
                    modifier = Modifier.size(25.dp),
                    painter = it,
                    contentDescription = "Add",
                    colorFilter = ColorFilter.tint(color = Color.White)
                )
            }
            iconSecond?.let { painterResource(id = it) }?.let {
                Image(
                    modifier = Modifier.size(25.dp).clickable { onIconSecondClick() },
                    painter = it,
                    contentDescription = "download ",
                    colorFilter = ColorFilter.tint(color = Color.White)
                )
            }
            if (iconThird != null) {
                Icon(
                    iconThird,
                    contentDescription = "Search",
                    tint = Color.White,
                    modifier = Modifier
                        .size(25.dp)
                        .clickable { onIconThirdClick() },
                )
            }


        }
    }
}