package eu.siacs.conversations.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import eu.siacs.conversations.R

class DailyRewardAdapter(private val reward: List<Int>) :
    RecyclerView.Adapter<DailyRewardAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.daily_reward_box, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return reward.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        val item = reward[position]
        holder.rewardTitle.setText("Day ${position + 1}")


    }


    inner class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val rewardBoxIc: ImageView = itemView.findViewById(R.id.reward_box_ic)
        val rewardTitle: TextView = itemView.findViewById(R.id.reward_title)
        val rewardStatus: TextView = itemView.findViewById(R.id.reward_status)

//        init {
//            itemView.setOnClickListener {
//                onItemClick?.invoke(chainList[adapterPosition])
//            }
//        }

    }
}