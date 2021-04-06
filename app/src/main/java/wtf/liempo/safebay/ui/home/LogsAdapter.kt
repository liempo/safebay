package wtf.liempo.safebay.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import wtf.liempo.safebay.R
import wtf.liempo.safebay.models.LogUnwrapped
import java.text.SimpleDateFormat
import java.util.*

class LogsAdapter : RecyclerView.Adapter<LogsAdapter.ViewHolder>() {

    var items = listOf<LogUnwrapped>()

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.image_profile)
        val name: TextView = view.findViewById(R.id.text_name)
        val address: TextView = view.findViewById(R.id.text_address)
        val date: TextView = view.findViewById(R.id.text_date)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_log, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        Glide.with(holder.image)
            .load(item.profile.imageUri)
            .into(holder.image)

        holder.name.text = item.profile.name
        holder.address.text = item.profile.address.toString()

        // Format date
        val format = SimpleDateFormat(
            "MMM dd, yyyy KK:mm a",
            Locale.getDefault())

        holder.date.text = format.format(item.date)
    }

    override fun getItemCount(): Int = items.size
}
