package eu.siacs.conversations.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import eu.siacs.conversations.R
import eu.siacs.conversations.http.model.wallet.Currencies

class ChainAdapter(
    private val context: Context,
    private val chainID: String = "0x1",
    private val showOnDialog: Boolean = false,
    private val chainList: List<Currencies>
) :
    RecyclerView.Adapter<ChainAdapter.ViewHolder>() {
    var onItemClick: ((Currencies) -> Unit)? = null

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.chain_row_item, parent, false)



        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currency = chainList[position]

        // sets the image to the imageview from our itemHolder class
//        holder.imageView.setImageResource(get)

        // sets the text to the textview from our itemHolder class
        holder.textView.text = currency.fullName
        if (showOnDialog) {
            holder.arrowBtn.visibility = View.GONE
            if (chainID == currency.chainHexId) {
                holder.arrowBtn.visibility = View.VISIBLE
                holder.arrowBtn.setImageResource(R.drawable.ic_baseline_check_24)
            }
            holder.imageView.visibility = View.GONE
            holder.rootLayout.background = null
        } else {

            Glide.with(context).load(currency.imgURL ?: "").into(
                holder.imageView
            )
        }

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return chainList.size
    }

    // Holds the views for adding it to image and text
    inner class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageView: ImageView = itemView.findViewById(R.id.chain_ic)
        val textView: TextView = itemView.findViewById(R.id.chain_name)
        val arrowBtn: ImageView = itemView.findViewById(R.id.chainDetailsBtn)
        val rootLayout: RelativeLayout = itemView.findViewById(R.id.rootLayout)

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(chainList[adapterPosition])
            }
        }

    }
}
