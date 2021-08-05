package com.oscarg798.recyclerviewtest

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val data = listOf(
        Data(0, "oscar", "gallon", "rosero"),
        Data(1, "stephany", "berrio", "alzate"),
        Data(2, "nicolas", "gallon", "berrio"),
        Data(3, "matias", "gallon", "berrio")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val dataSet = remember { mutableStateOf(data) }

            val onDataClicked: (Data) -> Unit = { dataItem ->
                val mutableData = dataSet.value.toMutableList()
                val itemIndex = mutableData.indexOfFirst { it.id == dataItem.id }
                mutableData.removeAt(itemIndex)
                mutableData.add(itemIndex, dataItem.copy(expanded = !dataItem.expanded))
                dataSet.value = mutableData
            }

            LazyColumn(Modifier.fillMaxSize()) {
                items(dataSet.value) { item ->
                    if (item.expanded) {
                        ExpandedView(item, onDataClicked)
                    } else {
                        CollapsedView(item, onDataClicked)
                    }
                }
            }
        }
    }
}

@Composable
fun ViewContainer(
    data: Data,
    onClickListener: (Data) -> Unit,
    content: @Composable () -> Unit,
) {
    Column(
        Modifier
            .padding(16.dp)
            .clickable { onClickListener(data) }) {
        content()
    }
}

@Composable
fun ExpandedView(data: Data, onClickListener: (Data) -> Unit) {
    ViewContainer(data = data, onClickListener = onClickListener) {
        Text(data.title)
        Text(data.subtitle)
        Text(data.description)
    }
}

@Composable
fun CollapsedView(data: Data, onClickListener: (Data) -> Unit) {
    ViewContainer(data = data, onClickListener = onClickListener) {
        Text(data.title)
    }
}

data class Data(
    val id: Int,
    val title: String,
    val subtitle: String,
    val description: String,
    val expanded: Boolean = false
)