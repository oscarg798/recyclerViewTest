package com.oscarg798.recyclerviewtest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rv = findViewById<RecyclerView>(R.id.rv)
        rv.layoutManager = LinearLayoutManager(this)
        val data = listOf(
            Data(0, "oscar", "gallon", "rosero"),
            Data(1, "stephany", "berrio", "alzate"),
            Data(2, "nicolas", "gallon", "berrio"),
            Data(3, "matias", "gallon", "berrio")
        )
        rv.adapter = MyAdapter(data.toMutableList()) {
            (rv.adapter as MyAdapter).updateItem(it.copy(expanded = !it.expanded))
        }
    }
}

data class Data(
    val id: Int,
    val title: String,
    val subtitle: String,
    val description: String,
    val expanded: Boolean = false
)

class MyAdapter(
    private val data: MutableList<Data>,
    private val onClickListener: (Data) -> Unit
) : RecyclerView.Adapter<CommonViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommonViewHolder {
       return  when (viewType) {
            0 -> ExpandedViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_expanded, parent, false)
            )
            else -> CollapsedViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_collapsed, parent, false)
            )

        }
    }

    override fun onBindViewHolder(holder: CommonViewHolder, position: Int) {
        holder.bind(data = data[position], onClickListener = onClickListener)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun updateItem(item: Data) {
        val itemIndex = data.indexOfFirst { it.id == item.id }
        data.removeAt(itemIndex)
        data.add(itemIndex, item)
        notifyItemChanged(itemIndex)
    }

    override fun getItemViewType(position: Int): Int {
        return if (data[position].expanded) {
            0
        } else {
            1
        }
    }
}

abstract class CommonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    abstract fun bind(data: Data, onClickListener: (Data) -> Unit)
}

class CollapsedViewHolder(itemView: View) : CommonViewHolder(itemView) {

    override fun bind(data: Data, onClickListener: (Data) -> Unit) {
        itemView.findViewById<TextView>(R.id.tvTitle).text = data.title
        itemView.setOnClickListener { onClickListener(data) }
    }
}

class ExpandedViewHolder(itemView: View) : CommonViewHolder(itemView) {

    override fun bind(data: Data, onClickListener: (Data) -> Unit) {
        itemView.findViewById<TextView>(R.id.tvTitle).text = data.title
        itemView.findViewById<TextView>(R.id.tvSubtitle).text = data.subtitle
        itemView.findViewById<TextView>(R.id.tvDescription).text = data.description
        itemView.setOnClickListener { onClickListener(data) }
    }
}