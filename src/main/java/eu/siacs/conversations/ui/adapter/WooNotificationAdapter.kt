package eu.siacs.conversations.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import eu.siacs.conversations.R


class WooNotificationAdapter(private val reward: List<Int>) :
    RecyclerView.Adapter<WooNotificationAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.notification_row_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return reward.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        val item = reward[position]
        holder.rewardTitle.setText("Notification ${position + 1}")


    }


    inner class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val rewardBoxIc: ImageView = itemView.findViewById(R.id.notification_icon)
        val rewardTitle: TextView = itemView.findViewById(R.id.title)
        val rewardStatus: TextView = itemView.findViewById(R.id.time)

//        init {
//            itemView.setOnClickListener {
//                onItemClick?.invoke(chainList[adapterPosition])
//            }
//        }

    }
}