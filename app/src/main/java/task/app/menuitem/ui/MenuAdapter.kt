package task.app.menuitem.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import task.app.menuitem.databinding.MenuItemLayoutBinding
import task.app.menuitem.databinding.MenuHeaderLayoutBinding
import task.app.menuitem.model.MenuData

/**
 * MenuAdapter for displaying a categorized menu in a RecyclerView.
 * The data is represented as a list of Pair<Boolean, Any>,
 * where the Boolean indicates whether the item is a category (true) or a menu item (false).
 */
class MenuAdapter(val listener: OnItemClickListener) : RecyclerView.Adapter<ViewHolder>() {
    private val menuItems = mutableListOf<Pair<Boolean, Any>>()

    /**
     * Creates ViewHolder depending on the item type (category or item).
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when(viewType) {
            TYPE_CATEGORY -> {
                val binding = MenuHeaderLayoutBinding.inflate(LayoutInflater.from(parent.context))
                CategoryViewHolder(binding)
            }
            else -> {
                val binding = MenuItemLayoutBinding.inflate(LayoutInflater.from(parent.context))
                MenuItemViewHolder(binding)
            }
        }
    }

    override fun getItemCount(): Int = menuItems.size

    /**
     * Binds the data to the ViewHolder depending on item type.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pair = menuItems[position]
        when(holder) {
            is CategoryViewHolder -> {
                holder.bindHeader(pair.second as String)
            }
            is MenuItemViewHolder -> {
                holder.bindItem((pair.second as MenuData))
            }
        }
    }

    /**
     * Returns the view type based on whether the item is a category or menu item.
     */
    override fun getItemViewType(position: Int): Int =
        if (menuItems[position].first) TYPE_CATEGORY else TYPE_ITEM

    companion object {
        private const val TYPE_CATEGORY = 0
        private const val TYPE_ITEM = 1
    }

    /**
     * ViewHolder for category.
     */
    inner class CategoryViewHolder(private val binding: MenuHeaderLayoutBinding) : ViewHolder(binding.root) {
        fun bindHeader(category: String) {
            binding.tvCategory.text = category.replaceFirstChar { it.uppercase() }
        }
    }

    /**
     * ViewHolder for menu items.
     */
    inner class MenuItemViewHolder(private val binding: MenuItemLayoutBinding) : ViewHolder(binding.root) {
        fun bindItem(menuData: MenuData) {
            binding.tvName.text = menuData.name
            binding.tvPrice.text = "£%.2f".format(menuData.price)
            binding.root.setOnClickListener {listener.onItemClicked(menuData.id)}
        }
    }

    /**
     * Interface for handling menu item click events.
     */
    interface OnItemClickListener {
        fun onItemClicked(id: Int)
    }

    fun submitGrouped(grouped: Map<String, List<MenuData>>) {
        menuItems.clear()
        for ((category, items) in grouped) {
            menuItems.add(true to category) // Add category
            for (it in items) {
                menuItems.add(false to it) // Add each item under the category
            }
        }
        notifyDataSetChanged() // Notify RecyclerView that data has changed
    }
}