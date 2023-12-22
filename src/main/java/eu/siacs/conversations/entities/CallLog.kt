package eu.siacs.conversations.entities


class CallLog(private val messages: List<Message>) {

    fun getItemCount(): Int {
        return messages.size
    }

    fun getLastMessage(): Message? {
        return messages.firstOrNull()
    }

    fun getTimestamp(): Long {
        return getLastMessage()?.getTimeSent() ?: 0
    }


}
