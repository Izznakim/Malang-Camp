package com.firmansyah.malangcamp.other

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.firmansyah.malangcamp.R
import com.firmansyah.malangcamp.other.ConstVariable.Companion.EXTRA_IMAGE
import com.firmansyah.malangcamp.theme.MalangCampTheme

class ZoomImageActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val image = intent.getStringExtra(EXTRA_IMAGE)

        setContent {
            MalangCampTheme {
                var scale by remember { mutableStateOf(1f) }
                var rotationState by remember { mutableStateOf(1f) }
                var offsetX by remember { mutableStateOf(0f) }
                var offsetY by remember { mutableStateOf(0f) }
                var size by remember { mutableStateOf(IntSize.Zero) }
                Box(
                    modifier = Modifier
                        .clip(RectangleShape)
                        .fillMaxSize()
                        .background(Color.Gray)
                        .onSizeChanged { size = it }
                        .pointerInput(Unit) {
                            detectTransformGestures { _, pan, zoom, rotation ->
                                scale = maxOf(.5f, minOf(scale * zoom, 5f))
                                rotationState += rotation

                                val maxX = (size.width * scale)
                                val minX = -maxX
                                offsetX = maxOf(minX, minOf(maxX, offsetX + pan.x))
                                val maxY = (size.height * scale)
                                val minY = -maxY
                                offsetY = maxOf(minY, minOf(maxY, offsetY + pan.y))
                            }
                        }
                        .padding(
                            top = 8.dp
                        )
                ) {
                    AsyncImage(
                        model = image,
                        contentDescription = stringResource(id = R.string.foto_bukti_pembayaran),
                        modifier = Modifier
                            .align(Alignment.Center)
                            .graphicsLayer(
                                scaleX = scale,
                                scaleY = scale,
                                rotationZ = rotationState,
                                translationX = offsetX,
                                translationY = offsetY
                            ),
                        contentScale = ContentScale.Inside,
                        placeholder = painterResource(
                            id = R.drawable.ic_photo
                        )
                    )
                }
            }
        }
    }
}