package com.example.android.layouts

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.LayoutModifier
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.constrainHeight
import androidx.compose.ui.unit.constrainWidth
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.offset
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import coil.compose.rememberImagePainter
import com.example.android.layouts.ui.theme.LayoutsTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LayoutsTheme {
                SimpleList()
            }
        }
    }

    @Composable
    fun TwoTexts(modifier: Modifier = Modifier, text1: String, text2: String) {
        Row(modifier = modifier.height(IntrinsicSize.Min)) {
            Text(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 4.dp)
                    .wrapContentWidth(Alignment.Start),
                text = text1
            )

            Divider(
                color = Color.Black,
                modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp)
            )
            Text(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 4.dp)
                    .wrapContentWidth(Alignment.End),

                text = text2
            )
        }
    }

    @Preview
    @Composable
    fun TwoTextsPreview() {
        LayoutsTheme {
            Surface {
                TwoTexts(text1 = "Hi", text2 = "there")
            }
        }
    }

    /**ConstraintLayout*/

    @Composable
    fun LargeConstraintLayout() {
        ConstraintLayout {
            val text = createRef()

            val guideline = createGuidelineFromStart(fraction = 0.5f)
            Text(
                "This is a very very very very very very very long text",
                Modifier.constrainAs(text) {
                    linkTo(start = guideline, end = parent.end)
                    width = Dimension.preferredWrapContent
                }
            )
        }
    }

    @Preview
    @Composable
    fun LargeConstraintLayoutPreview() {
        LayoutsTheme {
            LargeConstraintLayout()
        }
    }

    @Composable
    fun DecoupledConstraintLayout() {
        BoxWithConstraints {
            val constraints = if (maxWidth < maxHeight) {
                decoupledConstraints(margin = 16.dp) // Portrait constraints
            } else {
                decoupledConstraints(margin = 32.dp) // Landscape constraints
            }

            ConstraintLayout(constraints) {
                Button(
                    onClick = { /* Do something */ },
                    modifier = Modifier.layoutId("button")
                ) {
                    Text("Button")
                }

                Text("Text", Modifier.layoutId("text"))
            }
        }
    }

    private fun decoupledConstraints(margin: Dp): ConstraintSet {
        return ConstraintSet {
            val button = createRefFor("button")
            val text = createRefFor("text")

            constrain(button) {
                top.linkTo(parent.top, margin = margin)
            }
            constrain(text) {
                top.linkTo(button.bottom, margin)
            }
        }
    }

    @Composable
    fun ConstraintLayoutContent() {
        ConstraintLayout {
            val (button1, button2, text) = createRefs()

            Button(
                onClick = { /* Do something */ },
                modifier = Modifier.constrainAs(button1) {
                    top.linkTo(parent.top, margin = 16.dp)
                }
            ) {
                Text("Button 1")
            }

            Button(
                onClick = { /* Do something */ },
                modifier = Modifier.constrainAs(button1) {
                    top.linkTo(button1.bottom, margin = 16.dp)
                    centerAround(button1.end)
                }
            ) {
                Text("Button 1")
            }

            // Assign reference "text" to the Text composable
            // and constrain it to the bottom of the Button composable
            Text(
                "Text",
                Modifier.constrainAs(text) {
                    top.linkTo(button1.bottom, margin = 16.dp)
                    centerHorizontallyTo(parent)
                }
            )

            val barrier = createEndBarrier(button1, text)
            Button(
                onClick = { /* Do something */ },
                modifier = Modifier.constrainAs(button2) {
                    top.linkTo(parent.top, margin = 16.dp)
                    start.linkTo(barrier)
                }
            ) {
                Text("Button 2")
            }
        }
    }

    @Preview
    @Composable
    fun ConstraintLayoutContentPreview() {
        LayoutsTheme {
            ConstraintLayoutContent()
        }
    }

    /**Custom Layout*/
    val topics = listOf(
        "Arts & Crafts", "Beauty", "Books", "Business", "Comics", "Culinary",
        "Design", "Fashion", "Film", "History", "Maths", "Music", "People", "Philosophy",
        "Religion", "Social sciences", "Technology", "TV", "Writing"
    )

    @Composable
    fun BodyContent(modifier: Modifier = Modifier) {
        Row(modifier = modifier.horizontalScroll(rememberScrollState())) {
            StaggeredGrid {
                for (topic in topics) {
                    Chip(modifier = Modifier.padding(8.dp), text = topic)
                }
            }
        }
    }

    @Composable
    fun StaggeredGrid(
        modifier: Modifier = Modifier,
        rows: Int = 5,
        content: @Composable () -> Unit,
    ) {
        Layout(
            modifier = modifier,
            content = content
        ) { measurables, constraints ->
            // Keep track of the width of each row
            val rowWidths = IntArray(rows) { 0 }

            // Keep track of the max height of each row
            val rowHeights = IntArray(rows) { 0 }

            // Don't constrain child views further, measure them with given constraints
            // List of measured children
            val placeables = measurables.mapIndexed { index, measurable ->

                // Measure each child
                val placeable = measurable.measure(constraints)

                // Track the width and max height of each row
                val row = index % rows
                rowWidths[row] += placeable.width
                rowHeights[row] = Math.max(rowHeights[row], placeable.height)

                placeable
            }

            // Grid's width is the widest row
            val width = rowWidths.maxOrNull()
                ?.coerceIn(constraints.minWidth.rangeTo(constraints.maxWidth)) ?: constraints.minWidth

            // Grid's height is the sum of the tallest element of each row
            // coerced to the height constraints
            val height = rowHeights.sumOf { it }
                .coerceIn(constraints.minHeight.rangeTo(constraints.maxHeight))

            // Y of each row, based on the height accumulation of previous rows
            val rowY = IntArray(rows) { 0 }
            for (i in 1 until rows) {
                rowY[i] = rowY[i - 1] + rowHeights[i - 1]
            }

            // Set the size of the parent layout
            layout(width, height) {
                // x cord we have placed up to, per row
                val rowX = IntArray(rows) { 0 }

                placeables.forEachIndexed { index, placeable ->
                    val row = index % rows
                    placeable.placeRelative(
                        x = rowX[row],
                        y = rowY[row]
                    )
                    rowX[row] += placeable.width
                }
            }
        }
    }

    @Composable
    fun Chip(modifier: Modifier = Modifier, text: String) {
        Card(
            modifier = modifier,
            border = BorderStroke(color = Color.Black, width = Dp.Hairline),
            shape = RoundedCornerShape(8.dp)
        ) {
            Row(
                modifier = Modifier.padding(start = 8.dp, top = 4.dp, end = 8.dp, bottom = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(16.dp, 16.dp)
                        .background(color = MaterialTheme.colors.secondary)
                )
                Spacer(Modifier.width(4.dp))
                Text(text = text)
            }
        }
    }

    //    @Preview
    @Composable
    fun ChipPreview() {
        LayoutsTheme {
            Chip(text = "Hi there")
        }
    }

    @Composable
    fun MyOwnColumn(
        modifier: Modifier = Modifier,
        content: @Composable () -> Unit,
    ) {
        Layout(
            modifier = modifier,
            content = content
        ) { measurables, constraints ->
            // Don't constrain child views further, measure them with given constraints
            // List of measured children
            val placeables = measurables.map { measurable ->
                // Measure each child
                measurable.measure(constraints)
            }

            // Track the y co-ord we have placed children up to
            var yPosition = 0

            // Set the size of the layout as big as it can
            layout(constraints.maxWidth, constraints.maxHeight) {
                // Place children in the parent layout
                placeables.forEach { placeable ->
                    // Position item on the screen
                    placeable.placeRelative(x = 0, y = yPosition)

                    // Record the y co-ord placed up to
                    yPosition += placeable.height
                }
            }
        }
    }

    /**Modifier*/
    //    @Preview
    @Composable
    fun TextWithPaddingToBaselinePreview() {
        LayoutsTheme {
            Text("Hi there!", Modifier.firstBaselineToTop(32.dp))
        }
    }

    //    @Preview
    @Composable
    fun TextWithNormalPaddingPreview() {
        LayoutsTheme {
            Text("Hi there!", Modifier.padding(top = 32.dp))
        }
    }

    fun Modifier.firstBaselineToTop(
        firstBaselineToTop: Dp,
    ) = this.then(
        layout { measurable, constraints ->
            val placeable = measurable.measure(constraints)

            // Check the composable has a first baseline
            check(placeable[FirstBaseline] != AlignmentLine.Unspecified)
            val firstBaseline = placeable[FirstBaseline]

            // Height of the composable with padding - first baseline
            val placeableY = firstBaselineToTop.roundToPx() - firstBaseline
            val height = placeable.height + placeableY
            layout(placeable.width, height) {
                placeable.placeRelative(0, placeableY)
            }
        }
    )

    /**List Items*/

    @Composable
    fun SimpleList() {
        val listSize = 100
        val scrollState = rememberLazyListState()
        val coroutineScope = rememberCoroutineScope()

        Column {
            Row {
                Button(onClick = {
                    coroutineScope.launch {
                        scrollState.animateScrollToItem(0)
                    }
                }) {
                    Text("Scroll to the top")
                }

                Button(onClick = {
                    coroutineScope.launch {
                        scrollState.animateScrollToItem(listSize - 1)
                    }
                }) {
                    Text("Scroll to the end")
                }
            }

            LazyColumn(state = scrollState) {
                items(100) {
                    ImageListItem(index = it)
                }
            }
        }
    }

    @Composable
    fun ImageListItem(index: Int) {
        Row(verticalAlignment = Alignment.CenterVertically) {

            Image(
                painter = rememberImagePainter(
                    data = "https://developer.android.com/images/brand/Android_Robot.png"
                ),
                contentDescription = "Android Logo",
                modifier = Modifier.size(50.dp)
            )
            Spacer(Modifier.width(10.dp))
            Text("Item #$index", style = MaterialTheme.typography.subtitle1)
        }
    }

    /**Scaffold*/
    @Composable
    fun LayoutsCodelab() {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text("LayoutsCodelab")
                    },
                    actions = {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(imageVector = Icons.Filled.Favorite, contentDescription = null)
                        }
                    }
                )
            }
        ) { innerPadding ->
            BodyContent(modifier = Modifier.padding(innerPadding))
        }
    }

    //    @Preview
    @Composable
    fun LayoutsCodelabPreview() {
        LayoutsTheme {
            LayoutsCodelab()
        }
    }

    @Composable
    fun PhotographerCard(modifier: Modifier = Modifier) {
        Row(
            modifier = modifier
                .padding(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(MaterialTheme.colors.surface)
                .clickable { }
                .padding(16.dp)
        ) {
            Surface(
                modifier = Modifier.size(50.dp),
                shape = CircleShape,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.2f)
            ) {
                // Image goes here
            }

            Column(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .align(Alignment.CenterVertically)
            ) {
                Text("Alfred Sisley", fontWeight = FontWeight.Bold)
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                    Text("3 minutes ago", style = MaterialTheme.typography.body2)
                }
            }
        }
    }

    //    @Preview
    @Composable
    fun PhotographerCardPreview() {
        LayoutsTheme {
            PhotographerCard()
        }
    }

    /**extra*/
    @Stable
    fun Modifier.padding(all: Dp) =
        this.then(
            PaddingModifier(start = all, top = all, end = all, bottom = all, rtlAware = true)
        )

    private class PaddingModifier(
        val start: Dp = 0.dp,
        val top: Dp = 0.dp,
        val end: Dp = 0.dp,
        val bottom: Dp = 0.dp,
        val rtlAware: Boolean,
    ) : LayoutModifier {

        override fun MeasureScope.measure(
            measurable: Measurable,
            constraints: Constraints,
        ): MeasureResult {

            val horizontal = start.roundToPx() + end.roundToPx()
            val vertical = top.roundToPx() + bottom.roundToPx()

            val placeable = measurable.measure(constraints.offset(-horizontal, -vertical))

            val width = constraints.constrainWidth(placeable.width + horizontal)
            val height = constraints.constrainHeight(placeable.height + vertical)
            return layout(width, height) {
                if (rtlAware) {
                    placeable.placeRelative(start.roundToPx(), top.roundToPx())
                } else {
                    placeable.place(start.roundToPx(), top.roundToPx())
                }
            }
        }
    }
}
