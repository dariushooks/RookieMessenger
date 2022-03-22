package com.rookieandroid.rookiemessenger

data class MessageThread(val date: String = "", val time : String = "", val content: String = "",
                         val senderId : String = "", val receiverId : String = "", val type: String = "")
