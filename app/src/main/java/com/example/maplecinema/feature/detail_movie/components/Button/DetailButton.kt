package com.example.maplecinema.feature.movie_detail.components.Button

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import  androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.maplecinema.R


@Composable
fun DetailButton(
    text: String,
    textColor: Color,
    icon: Int,
    backgroundColor: Color,
    clickArgument: String, // Tham số chuỗi sẽ được truyền vào onClick
    onClick: (String) -> Unit // onClick nhận một chuỗi
) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(8f),
        shape = RoundedCornerShape(5.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor
        ),
        onClick = { onClick(clickArgument) } // Truyền clickArgument vào onClick khi bấm
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(textColor)
            )

            Spacer(Modifier.width(5.dp))
            Text(text, color = textColor, fontWeight = FontWeight.Bold)
        }
    }
}


@Composable
fun ActionButton(
    icon: Int? = null,
    iconVector: ImageVector? = null,
    title: String? = null,
    onClick: () -> Unit
) {
    Button(
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        onClick = { onClick() }) {
        Column(horizontalAlignment = Alignment.CenterHorizontally,verticalArrangement = Arrangement.Center) {
            if (icon != null) {
                Image(
                    painter = painterResource(icon), contentDescription = null,
                    modifier = Modifier.size(25.dp), colorFilter = ColorFilter.tint(Color.White)
                )
                Spacer(Modifier.height(10.dp))
            } else {
                if (iconVector != null) {
                    Image(
                        imageVector = iconVector, contentDescription = null,
                        modifier = Modifier.size(30.dp), colorFilter = ColorFilter.tint(Color.White)
                    )
                    Spacer(Modifier.height(5.dp))
                }
            }


            if (title != null) {
                Text(title, textAlign = TextAlign.Center, style = TextStyle(fontSize = 12.sp))
            }
        }

    }
}
