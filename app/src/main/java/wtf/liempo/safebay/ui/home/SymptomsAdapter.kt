package wtf.liempo.safebay.ui.home

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.TextView
import wtf.liempo.safebay.R
import wtf.liempo.safebay.models.Symptom

class SymptomsAdapter : RecyclerView.Adapter<SymptomsAdapter.ViewHolder>() {

    var items = listOf<Symptom>()
    private var onCompleteListener: ((List<Symptom>) -> Unit)? = null

    fun setOnCompleteListener(l: ((List<Symptom>) -> Unit)) {
        onCompleteListener = l
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_symptom,
                parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val item = items[position]
        holder.text.text = item.text

        holder.group.setOnCheckedChangeListener { _, checkedId ->
            item.present = when (checkedId) {
                R.id.button_yes -> true
                R.id.button_no -> false
                else -> false
            }

            // Check if all are answered
            if (items.all { it.present != null })
                onCompleteListener?.invoke(items)
        }
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val text: TextView = view.findViewById(R.id.text_symptom)
        val group: RadioGroup = view.findViewById(R.id.group_answers)
    }
}
