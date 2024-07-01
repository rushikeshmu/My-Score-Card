package com.example.myscorecardtask

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myscorecardtask.ui.theme.LightBlue
import com.example.myscorecardtask.ui.theme.MyScoreCardTaskTheme
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyScoreCardTaskTheme {
                MyScorecardScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("PrivateResource")
@Composable
fun MyScorecardScreen(viewModel: ScoreCardViewModel = viewModel()) {
    val selectedFilter = viewModel.selectedFilter
    val filters = listOf("Company", "Area", "Region", "District", "State", "Country")
    val searchText = viewModel.searchText.collectAsState().value
    val bottomSheetStateValue = viewModel.bottomSheetState.collectAsState().value
    val scope = rememberCoroutineScope()
    val bottomSheetState = remember {
        SheetState(

            initialValue = SheetValue.Hidden,
            skipPartiallyExpanded = true
        )
    }
    val scaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = bottomSheetState)

    LaunchedEffect(bottomSheetStateValue) {
        if (bottomSheetStateValue == SheetValue.Hidden) {
            bottomSheetState.hide()
        } else {
            bottomSheetState.expand()
        }
    }

    Spacer(modifier = Modifier.width(8.dp))

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetContent = {
            BottomSheetContent()
        },
        sheetPeekHeight = 0.dp,
    ) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = { /*TODO: handle back navigation*/ }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "My Scorecard", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.weight(1f))
            TextButton(onClick = { /*TODO: handle reset action*/ }) {
                Text(text = "Reset")
            }
        }

        Spacer(modifier = Modifier.height(1.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(1.dp)
        ) {
            DayButtons { show ->
                if (show) {
                    scope.launch {
                        viewModel.showBottomSheet()
                    }
                } else {
                    scope.launch {
                        viewModel.hideBottomSheet()
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .background(Color.White)
                .padding(horizontal = 6.dp, vertical = 1.dp)
                .border(1.dp, Color.Gray)
                .height(32.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                contentAlignment = Alignment.CenterStart
            ) {
                if (searchText.isEmpty()) {
                    Text(
                        text = "Search",
                        style = TextStyle(
                            color = Color.Gray,
                            fontStyle = FontStyle.Normal,
                            fontWeight = FontWeight.SemiBold
                        ),
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(start = 8.dp)
                    )
                }

                BasicTextField(
                    value = searchText,
                    onValueChange = { viewModel.onSearchTextChanged(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterStart)
                        .padding(8.dp),
                    singleLine = true,
                    textStyle = TextStyle(color = Color.Black),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Search
                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = { /* handle search action */ }
                    )
                )
            }

            IconButton(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .weight(0.1f)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.searchimg),
                    tint = Color.Black,
                    contentDescription = stringResource(id = androidx.compose.material3.R.string.search_bar_search),
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(1.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(filters) { filter ->
                FilterButton(
                    text = filter,
                    isSelected = selectedFilter == filter,
                    onClick = { viewModel.onFilterChanged(filter) }
                )
            }
        }
        }
    }
 }

@Composable
fun BottomSheetContent() {
    val bottomSheetItems = listOf("Item 1", "Item 2", "Item 3", "Item 4")

    Column(modifier = Modifier.padding(16.dp)) {
        bottomSheetItems.forEach { item ->
            Text(text = item, modifier = Modifier.padding(vertical = 8.dp))
        }
    }
}

@Composable
fun DayButtons(onDayButtonClick: (Boolean) -> Unit) {
    val selectedButton = remember { mutableStateOf<String?>(null) }

    DayButton(text = "Today", isSelected = selectedButton.value == "Today") {
        selectedButton.value = if (selectedButton.value == "Today") null else "Today"
        onDayButtonClick(selectedButton.value == "Today")
    }

    val today = LocalDate.now().format(DateTimeFormatter.ofPattern("MM-dd-yyyy"))
    DateButton(text = today, isSelected = selectedButton.value ==today ) {
        selectedButton.value = if (selectedButton.value ==today) null else today
        onDayButtonClick(false)
    }
}

@Composable
fun DayButton(text: String, isSelected: Boolean, onClick: () -> Unit) {
    val borderColor = if (isSelected) LightBlue else Color.Transparent

    Button(
        onClick = onClick,
        modifier = Modifier.padding(4.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) Color.Transparent else Color.LightGray,
            contentColor = Color.Black,
        ),
        contentPadding = PaddingValues(start = 12.dp, end = 16.dp, top = 4.dp, bottom = 4.dp),
        border = BorderStroke(2.dp, borderColor)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text, color = if (isSelected) LightBlue else Color.Black)
            Spacer(modifier = Modifier.width(1.dp))
            if (isSelected) {
                Icon(
                    painter = painterResource(id = R.drawable.down_arrow),
                    contentDescription = "Arrow Down",
                    modifier = Modifier
                        .size(8.dp)
                        .clickable { onClick() },
                    tint = LightBlue
                )
            }
        }
    }
}



@Composable
fun DateButton(text: String, isSelected: Boolean, onClick: () -> Unit) {
    val borderColor = if (isSelected) LightBlue else Color.Transparent

    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) Color.White else Color.LightGray,
            contentColor = Color.Black,
        ),
        border = BorderStroke(1.dp, borderColor)
    ) {
        Row {
            Text(text, color = if (isSelected) LightBlue else Color.Black)
            Spacer(modifier = Modifier.width(4.dp))

        }
    }
}



@Composable
fun FilterButton(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.padding(4.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) LightBlue else Color.LightGray,
            contentColor = if (isSelected) Color.White else Color.Black,
        ),
        contentPadding = PaddingValues(start = 12.dp, end = 12.dp, top = 4.dp, bottom = 4.dp) // Remove default padding

    ) {
        Row(verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = text,
            )
            Spacer(modifier = Modifier.weight(1f))
            if(isSelected)
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint =  Color.White ,
                    modifier = Modifier.padding(start = 4.dp) // Add padding to space out the icon
                )
        }
    }
}



@Preview(showBackground = true)
@Composable
fun MyScoreCardScreenPreview() {
    MyScoreCardTaskTheme {
        MyScorecardScreen()

    }
}