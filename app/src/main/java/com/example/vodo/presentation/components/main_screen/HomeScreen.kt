package com.example.vodo.presentation.components.main_screen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleOwner
import com.example.vodo.core.contants.FONT_FAMILY
import com.example.vodo.data.local.entity.TaskEntity
import com.example.vodo.presentation.viewmodel.TaskViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.LocalDate

@OptIn(DelicateCoroutinesApi::class)
@SuppressLint("CoroutineCreationDuringComposition", "NewApi")
@Composable
fun HomeScreen(lifecycleOwner: LifecycleOwner, viewModel: TaskViewModel) {


    var showAddTaskDialog by remember { mutableStateOf(false) }
    var deleteEditTaskDialog by remember { mutableStateOf(false) }
    var itemSearched by remember { mutableStateOf(false) }


    val allTasksLiveData = viewModel.getAllTasks()
    var searchedItem by remember {
        mutableStateOf("")
    }
    val searchedTasksLiveData = viewModel.getSearchedTasks(searchedItem)
    var allTasks by remember { mutableStateOf(listOf<TaskEntity>()) }


    if (itemSearched && searchedItem != "") {
        searchedTasksLiveData.observe(lifecycleOwner) {
            GlobalScope.launch(Dispatchers.Main) {
                allTasks = it
            }
        }
    } else {
        allTasksLiveData.observe(lifecycleOwner) {
            GlobalScope.launch(Dispatchers.Main) {
                allTasks = it
            }
        }
    }


    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddTaskDialog = true }) {
                Icon(
                    imageVector = Icons.Default.AddCircle,
                    tint = Color.Black,
                    contentDescription = "add"
                )
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x9593C4EB))
        ) {

            TopBar(
                it,
                onQueryChange = { searchedItem = it;itemSearched = true },
                onActiveChange = { itemSearched = false })
            Spacer(modifier = Modifier.height(12.dp))
            DateShow()

            var task by remember {
                mutableStateOf(TaskEntity(null, "", "", false, "00:00"))
            }

            TodoList(
                allTasks,
                viewModel,
                onDeleteEdit = {
                    deleteEditTaskDialog = true
                    task = it
                }
            )

            AddTaskDialog(
                showAddTaskDialog = showAddTaskDialog,
                onDismissRequest = { showAddTaskDialog = false },
                onConfirm = { viewModel.addTask(it) })


            if (deleteEditTaskDialog) {
                DeleteEditTaskDialog(
                    viewModel = viewModel,
                    task = task,
                    onDismissRequest = { deleteEditTaskDialog = false }
                )
            }

        }
    }


}

@Composable
fun TodoList(
    tasks: List<TaskEntity>,
    viewModel: TaskViewModel,
    onDeleteEdit: (task: TaskEntity) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .padding(top = 24.dp, bottom = 8.dp)
                .clip(RoundedCornerShape(90.dp))
                .background(Color.Black), contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Your Tasks",
                color = Color.White,
                fontSize = 24.sp,
                fontFamily = FONT_FAMILY,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(start = 12.dp, end = 12.dp, top = 4.dp, bottom = 4.dp)
            )
        }
        Spacer(
            modifier = Modifier
                .padding(top = 8.dp, bottom = 8.dp, start = 34.dp, end = 34.dp)
                .height(1.dp)
                .fillMaxWidth()
                .background(Color.Black)
        )

        LazyColumn {

            items(tasks) { item ->
                TodoItem(item, onDeleteEdit = onDeleteEdit, onTaskCompleted = {
                    viewModel.addTask(
                        TaskEntity(
                            item.id,
                            item.title,
                            item.description,
                            !item.completed,
                            item.time
                        )
                    )
                })
            }
        }


    }
}

@Composable
fun TodoItem(
    task: TaskEntity,
    onDeleteEdit: (task: TaskEntity) -> Unit,
    onTaskCompleted: (task: TaskEntity) -> Unit
) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(12.dp)
        .clickable { onDeleteEdit(task) }) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(if (task.completed) Color(0xC38A66E0) else Color.Black)
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Text(text = task.time, fontFamily = FONT_FAMILY, color = Color.White)
            Spacer(modifier = Modifier.width(12.dp))
            Column(
                modifier = Modifier.fillMaxWidth(0.8f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = task.title,
                    fontFamily = FONT_FAMILY,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.ExtraBold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = task.description,
                    fontFamily = FONT_FAMILY,
                    textAlign = TextAlign.Center,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.width(12.dp))
            Icon(
                imageVector = if (task.completed) Icons.Filled.CheckCircle else Icons.Outlined.CheckCircle,
                contentDescription = "search",
                tint = Color.White,
                modifier = Modifier.clickable { onTaskCompleted(task) }
            )
        }
    }
}

@Composable
fun DateShow() {

    val date by remember {
        mutableStateOf(LocalDate.now())
    }

    val days = listOf("Sun", "Mon", "Tue", "Wed", "Thur", "Fri", "Sat")

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Card(
            modifier = Modifier
                .width(90.dp)
                .height(150.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
//                date.dayOfWeek.toString().slice(0..2)
                Text(
                    text = (days[date.dayOfWeek.value.dec()]),
                    fontFamily = FONT_FAMILY,
                    color = Color.White,
                    modifier = Modifier.padding(12.dp)
                )
                Text(
                    text = "${date.dayOfMonth.dec()}",
                    fontFamily = FONT_FAMILY,
                    color = Color.White,
                    modifier = Modifier.padding(12.dp)
                )
                Text(
                    text = "${date.year}",
                    color = Color.White,
                    fontFamily = FONT_FAMILY,
                    modifier = Modifier.padding(12.dp)
                )
            }

        }

        Card(
            modifier = Modifier
                .width(100.dp)
                .height(180.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xC38A66E0)),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = days[date.dayOfWeek.value],
                    fontFamily = FONT_FAMILY,
                    color = Color.White,
                    modifier = Modifier.padding(12.dp)
                )
                Text(
                    text = "${date.dayOfMonth}",
                    fontFamily = FONT_FAMILY,
                    color = Color.White,
                    modifier = Modifier.padding(12.dp)
                )
                Text(
                    text = "${date.year}",
                    color = Color.White,
                    fontFamily = FONT_FAMILY,
                    modifier = Modifier.padding(12.dp)
                )
            }

        }

        Card(
            modifier = Modifier
                .width(90.dp)
                .height(150.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = days[date.dayOfWeek.value.inc()],
                    fontFamily = FONT_FAMILY,
                    color = Color.White,
                    modifier = Modifier.padding(12.dp)
                )
                Text(
                    text = "${date.dayOfMonth.inc()}",
                    fontFamily = FONT_FAMILY,
                    color = Color.White,
                    modifier = Modifier.padding(12.dp)
                )
                Text(
                    text = "${date.year}",
                    fontFamily = FONT_FAMILY,
                    color = Color.White,
                    modifier = Modifier.padding(12.dp)
                )
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    paddingValues: PaddingValues,
    onQueryChange: (String) -> Unit,
    onActiveChange: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 8.dp,
                bottom = 8.dp,
                end = 8.dp,
                top = paddingValues.calculateTopPadding() + 22.dp
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .padding(8.dp)
                .clip(RoundedCornerShape(90.dp))
                .background(Color.Black), contentAlignment = Alignment.Center
        ) {
            Text(
                text = "VoDo List",
                fontFamily = FONT_FAMILY,
                fontSize = 24.sp,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(start = 12.dp, end = 12.dp, top = 4.dp, bottom = 4.dp)
            )
        }

        SearchBar(
            query = searchQuery,
            onQueryChange = { searchQuery = it; onQueryChange("%$searchQuery%") },
            onSearch = { onQueryChange(searchQuery) },
            active = false,
            colors = SearchBarDefaults.colors(
                containerColor = Color(0xC38A66E0)
            ),
            onActiveChange = { onActiveChange();onQueryChange("") },
            placeholder = {
                Text(
                    text = "Search tasks",
                    fontFamily = FONT_FAMILY,
                    color = Color.White
                )
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    tint = Color.White,
                    contentDescription = "search"
                )
            }
        ) {

        }

    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskDialog(
    showAddTaskDialog: Boolean,
    onDismissRequest: () -> Unit,
    onConfirm: (task: TaskEntity) -> Unit
) {

    var newtitle by remember { mutableStateOf("") }
    var newdescription by remember { mutableStateOf("") }
    var newtime by remember { mutableStateOf(TimePickerState(0, 0, false)) }

    if (showAddTaskDialog) {

        AlertDialog(
            onDismissRequest = { onDismissRequest() },
            text = {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Add Your Task", fontSize = 24.sp, fontFamily = FONT_FAMILY)
                    TextField(
                        value = newtitle,
                        placeholder = { Text(text = "Task Title", fontFamily = FONT_FAMILY) },
                        onValueChange = { newtitle = it })
                    TextField(
                        value = newdescription,
                        placeholder = { Text(text = "Task Description", fontFamily = FONT_FAMILY) },
                        onValueChange = { newdescription = it })
                    Spacer(modifier = Modifier.height(12.dp))
                    TimePicker(state = newtime)
                }
            },
            confirmButton = {
                Button(onClick = {
                    val task = TaskEntity(
                        id = null,
                        title = newtitle,
                        description = newdescription,
                        time = newtime.hour.toString() + ":" + newtime.minute.toString(),
                        completed = false
                    )
                    onConfirm(task)
                    newtitle = ""
                    newdescription = ""
                    newtime = TimePickerState(0, 0, false)
                    onDismissRequest()
                }) {
                    Text(text = "Add Task", fontFamily = FONT_FAMILY)
                }

            }
        )

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteEditTaskDialog(
    viewModel: TaskViewModel,
    task: TaskEntity,
    onDismissRequest: () -> Unit,
) {
    val hour = task.time.split(":")[0].toInt()
    val min = task.time.split(":")[1].toInt()

    var title by remember { mutableStateOf(task.title) }
    var description by remember { mutableStateOf(task.description) }
    var time by remember { mutableStateOf(TimePickerState(hour, min, false)) }

        AlertDialog(
            onDismissRequest = { onDismissRequest() },
            text = {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Update Your Task", fontSize = 24.sp, fontFamily = FONT_FAMILY)
                    TextField(
                        value = title,
                        placeholder = { Text(text = "Update Title", fontFamily = FONT_FAMILY) },
                        onValueChange = { title = it })
                    TextField(
                        value = description,
                        placeholder = {
                            Text(
                                text = "Update Description",
                                fontFamily = FONT_FAMILY
                            )
                        },
                        onValueChange = { description = it })
                    Spacer(modifier = Modifier.height(12.dp))
                    TimePicker(state = time)

                }
            },
            confirmButton = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Button(onClick = {
                        viewModel.deleteTask(task)
                        title = ""
                        description = ""
                        time = TimePickerState(0, 0, false)
                        onDismissRequest()
                    }) {
                        Text(text = "Delete", fontFamily = FONT_FAMILY)
                    }
                    Button(onClick = {
                        viewModel.addTask(
                            TaskEntity(
                                task.id,
                                title,
                                description,
                                task.completed,
                                time.hour.toString() + ":" + time.minute.toString()
                            )
                        )
                        title = ""
                        description = ""
                        time = TimePickerState(0, 0, false)
                        onDismissRequest()
                    }) {
                        Text(text = "Update", fontFamily = FONT_FAMILY)
                    }
                }

            }
        )

    }



