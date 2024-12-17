package com.example.listapp
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.ads.mediationtestsuite.adapters.ItemsListRecyclerViewAdapter.OnItemClickListener

class ItemAdapter(private val groceryList: MutableList<Item>, private val onItemClick: (Item) -> Unit) :
    RecyclerView.Adapter<ItemViewHolder>() {


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        private val dateTextView: TextView = itemView.findViewById(R.id.buyBeforeDateTextView)
        private val quantityTextView: TextView = itemView.findViewById(R.id.quantityTextView)
        private val priceTextView: TextView = itemView.findViewById(R.id.priceTextView)

        fun bind(item: Item) {
            // Set click listener on the entire item view
            itemView.setOnClickListener {
                onItemClick(item) // Invoke the click callback
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_item, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val groceryItem = groceryList[position]
        holder.bind(groceryItem)
        holder.itemView.setOnClickListener(){
            onItemClick(groceryItem)
        }
    }

    override fun getItemCount(): Int = groceryList.size

    // Add an item to the list
    fun addItem(item: Item) {
        groceryList.add(item)
        notifyItemInserted(groceryList.size - 1)
    }

    // Remove an item by position
    fun removeItem(position: Int) {
        if (position in groceryList.indices) {
            groceryList.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    // Update an item
    fun updateItem(position: Int, newItem: Item) {
        if (position in groceryList.indices) {
            groceryList[position] = newItem
            notifyItemChanged(position)
        }
    }

    // Clear the entire list
    fun clearItems() {
        groceryList.clear()
        notifyDataSetChanged()
    }

    companion object

}