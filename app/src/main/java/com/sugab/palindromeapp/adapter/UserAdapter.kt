package com.sugab.palindromeapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sugab.palindromeapp.R
import com.sugab.palindromeapp.model.user.User

class UserAdapter(private val onUserClicked: (User) -> Unit) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private val users = mutableListOf<User>()

    fun setUsers(users: List<User>) {
        this.users.clear()
        this.users.addAll(users)
        notifyDataSetChanged()
    }

    fun addUsers(users: List<User>) {
        val start = this.users.size
        this.users.addAll(users)
        notifyItemRangeInserted(start, users.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = users[position]
        holder.bind(user, onUserClicked)
    }

    override fun getItemCount(): Int = users.size

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val avatar: ImageView = itemView.findViewById(R.id.ivAvatar)
        private val name: TextView = itemView.findViewById(R.id.tvName)
        private val email: TextView = itemView.findViewById(R.id.tvEmail)

        fun bind(user: User, onUserClicked: (User) -> Unit) {
            name.text = "${user.firstName} ${user.lastName}"
            email.text = user.email
            Glide.with(itemView.context).load(user.avatar).into(avatar)

            itemView.setOnClickListener {
                onUserClicked(user)
            }
        }
    }
}
