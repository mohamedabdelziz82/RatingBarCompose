@file:OptIn(ExperimentalComposeUiApi::class)
@file:Suppress("OPT_IN_IS_NOT_ENABLED")

package com.mohamedabdelaziz.rating_bar

import android.view.MotionEvent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
fun RatingBar(
    modifier: Modifier = Modifier,
    rating: MutableState<Int> = remember { mutableStateOf(0) },
    isRatingEditable: Boolean = true,
    starSize: Dp = 40.dp,
    starsPadding: Dp = 0.dp,
    isViewAnimated: Boolean = true,
    numberOfStars: Int = 5,
    starIcon: ImageVector = Icons.Rounded.Star,
    ratedStarsColor: Color = Color(0xFFF9B848),
    unRatedStarsColor: Color = Color(0xFFF2F4F8),
    onValueChanged: (Int) -> Unit = { _: Int -> }
) {

    var selected by remember {
        mutableStateOf(false)
    }
    val size by animateDpAsState(
        targetValue = if (selected) (starSize + 6.dp) else starSize,
        spring(Spring.DampingRatioLowBouncy),
    )

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        for (star in 1..numberOfStars) {
            Icon(
                imageVector = starIcon,
                contentDescription = "rating",
                modifier = Modifier
                    .size(if (isViewAnimated) size else starSize)
                    .padding(end = starsPadding)
                    .pointerInteropFilter {
                        when (isRatingEditable) {
                            true -> {
                                when (it.action) {
                                    MotionEvent.ACTION_DOWN -> {
                                        selected = true
                                        rating.value = star
                                        onValueChanged(rating.value)
                                    }
                                    MotionEvent.ACTION_UP -> {
                                        selected = false
                                    }
                                    MotionEvent.ACTION_CANCEL -> {
                                        selected = false
                                    }
                                }
                            }
                            false -> {}
                        }
                        isRatingEditable
                    },

                tint = if (star <= rating.value) ratedStarsColor else unRatedStarsColor
            )
        }
    }
}