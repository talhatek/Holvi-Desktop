package util

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.abs
import kotlin.math.max
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Job

val AppBarHeight = 56.dp
val AppBarHorizontalPadding = 4.dp
val TitleIconModifier = Modifier.fillMaxHeight()
var iconWidth = 72.dp - AppBarHorizontalPadding
var withoutIconWidth = 16.dp - AppBarHorizontalPadding

@Composable
fun CenterTopAppBar(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    backgroundColor: Color = PrimaryColor,
    contentColor: Color = contentColorFor(backgroundColor),
    elevation: Dp = AppBarDefaults.TopAppBarElevation
) {
    val defLeftSectionWidth = if (navigationIcon == null) withoutIconWidth else iconWidth
    var leftSectionWidth by remember { mutableStateOf(defLeftSectionWidth) }
    var rightSectionWidth by remember { mutableStateOf(-1f) }
    var rightSectionPadding by remember { mutableStateOf(0f) }

    AppBar(
        backgroundColor,
        contentColor,
        elevation,
        AppBarDefaults.ContentPadding,
        RectangleShape,
        modifier
    ) {
        if (navigationIcon == null) {
            Spacer(Modifier.width(leftSectionWidth))
        } else {
            Row(
                TitleIconModifier.width(leftSectionWidth),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CompositionLocalProvider(
                    LocalContentAlpha provides ContentAlpha.high,
                    content = navigationIcon
                )
            }
        }

        Row(
            Modifier
                .fillMaxHeight()
                .weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (leftSectionWidth != defLeftSectionWidth
                || rightSectionPadding != 0f
            ) {
                ProvideTextStyle(value = MaterialTheme.typography.h6) {
                    CompositionLocalProvider(
                        LocalContentAlpha provides ContentAlpha.high,
                        content = title
                    )
                }
            }
        }

        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            with(LocalDensity.current) {
                Row(
                    Modifier
                        .fillMaxHeight()
                        .padding(start = rightSectionPadding.toDp())
                        .onGloballyPositioned {
                            rightSectionWidth = it.size.width.toFloat()
                            if (leftSectionWidth == defLeftSectionWidth
                                && rightSectionWidth != -1f
                                && rightSectionPadding == 0f
                            ) {

                                val maxWidth = max(leftSectionWidth.value, rightSectionWidth)
                                leftSectionWidth = maxWidth.toDp()
                                rightSectionPadding = abs(rightSectionWidth - maxWidth)
                            }
                        },
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically,
                    content = actions
                )
            }
        }
    }
}

@Composable
fun AppBar(
    backgroundColor: Color,
    contentColor: Color,
    elevation: Dp,
    contentPadding: PaddingValues,
    shape: Shape,
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {
    Surface(
        color = backgroundColor,
        contentColor = contentColor,
        elevation = elevation,
        shape = shape,
        modifier = modifier
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(contentPadding)
                .height(AppBarHeight),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            content = content
        )
    }
}

@Composable
fun CircleTextButton(text: String, percentage: Int, onClicked: () -> Unit) {
    BoxWithConstraints {
        Button(
            onClick = {
                onClicked.invoke()
            },
            modifier = Modifier.size((this.maxWidth / 100) * percentage),
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(backgroundColor = PrimaryColor),
            elevation = ButtonDefaults.elevation(defaultElevation = 8.dp, pressedElevation = 12.dp)
        ) {
            BoxWithConstraints {
                Text(
                    text = text.toUpperCase(Locale.current),
                    fontSize = ((this.maxWidth / 100) * 50).value.sp,
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                )

            }

        }
    }
}

@Composable
fun CircleIconButton(resPath: String, percentage: Int, onClicked: () -> Unit) {
    val degree = remember { mutableStateOf(60f) }
    val angle: Float by animateFloatAsState(
        targetValue = degree.value,
        animationSpec = tween(
            durationMillis = 400, // duration
            easing = FastOutSlowInEasing
        ),
    )
    BoxWithConstraints {
        Button(
            onClick = {
                degree.value = degree.value + 180f
                onClicked.invoke()

            },
            modifier = Modifier.size((this.maxWidth / 100) * percentage),
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary),
            elevation = ButtonDefaults.elevation(defaultElevation = 8.dp, pressedElevation = 12.dp)
        ) {
            Icon(
                modifier = Modifier.rotate(angle),
                painter = painterResource(resPath),
                contentDescription = "Renew"
            )

        }
    }
}


@Composable
fun TopAppBarOnlyIcon(resPath: String, onIconClicked: () -> Unit) {
    CenterTopAppBar(
        title = {
            Box(modifier = Modifier.fillMaxSize()) {
                IconButton(
                    onClick = { onIconClicked.invoke() },
                    enabled = true,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        painter = painterResource(resPath),
                        contentDescription = "Back",
                        tint = WhiteColor
                    )
                }
            }
        }
    )

}

@Composable
fun TopAppBarBackWithLogo(onBackClicked: () -> Unit) {
    CenterTopAppBar(
        title = {
            Text(
                text = "Holvi",
                textDecoration = TextDecoration.Underline,
                style = TextStyle(
                    color = Color.Black,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium
                )
            )
        },
        navigationIcon = {
            IconButton(
                onClick = { onBackClicked.invoke() },
                enabled = true,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    painter = painterResource("ic_back.png"),
                    contentDescription = "Back"
                )
            }
        }
    )

}

@Composable
fun BottomButton(s: String, function: () -> Job) {
        Button(
            modifier = Modifier.fillMaxWidth().fillMaxHeight(.2f), onClick = {
                function.invoke()
            },
            colors = ButtonDefaults.buttonColors(PrimaryColor), shape = RectangleShape
        ) {
            Text(
                text = s,
                style = TextStyle(
                    fontWeight = FontWeight.Light,
                    fontSize = 24.sp
                )
            )

        }

}