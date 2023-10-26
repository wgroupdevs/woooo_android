package eu.siacs.conversations.ui.adapter

import android.content.Context
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import eu.siacs.conversations.R
import eu.siacs.conversations.http.model.wallet.Transaction
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date


class WalletTransactionAdapter(
    private val context: Context,
    private val transaction: List<Transaction>
) :
    RecyclerView.Adapter<WalletTransactionAdapter.ViewHolder>() {

    private val TAG = "TransactionAdapter_TAG"

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.wallet_transaction_row, parent, false)



        return ViewHolder(view)
    }

    // binds the list items to a view
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val payment = transaction[position]
        holder.trDate.text = payment.date?.let {
            formatDate(it)
        }
        holder.trCurrency.text = payment.currency ?: ""
        holder.trType.text = payment.type ?: ""
        holder.trAmount.text = payment.amount?.let { it.toString() }
        holder.trStatus.text = payment.confirms?.let {
            when (it) {
                0 -> "Pending"
                1 -> "Completed"
                else -> {
                    ""
                }
            }
        }

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return transaction.size
    }


    private fun formatDate(dateString: String): String {

        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        var date: Date?
        var formattedDate = ""
        try {
            date = dateFormat.parse(dateString)
            val simpleFormatter = SimpleDateFormat("M/d/yy")
            formattedDate = simpleFormatter.format(date)
            Log.d(TAG, "CURRENT_DATE_TIME :${formattedDate}")
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return formattedDate
    }

    // Holds the views for adding it to image and text
    inner class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val trDate: TextView = itemView.findViewById(R.id.tr_date)
        val trCurrency: TextView = itemView.findViewById(R.id.tr_currency)
        val trType: TextView = itemView.findViewById(R.id.tr_type)
        val trAmount: TextView = itemView.findViewById(R.id.tr_amount)
        val trStatus: TextView = itemView.findViewById(R.id.tr_status)

    }
}
