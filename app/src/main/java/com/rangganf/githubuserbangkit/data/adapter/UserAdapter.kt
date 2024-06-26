package com.rangganf.githubuserbangkit.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.rangganf.githubuserbangkit.model.UserResponse
import com.rangganf.githubuserbangkit.databinding.ItemUserBinding
//Ini adalah user adapter yang berfungsi sebagai alamat untuk pemanggilan data ke sebuah ui pada user.
class UserAdapter(
    private val data: MutableList<UserResponse.Item> = mutableListOf(),
    private val listener: (UserResponse.Item) -> Unit
) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    fun setData(data: MutableList<UserResponse.Item>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    class UserViewHolder(private val v: ItemUserBinding) : RecyclerView.ViewHolder(v.root) {
        fun bind(item: UserResponse.Item) {
            v.imgAvatar.load(item.avatar_url) {
                transformations(CircleCropTransformation())
            }

            v.tvUsername.text = item.login
            v.idName.text = item.id.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder =
        UserViewHolder(ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            listener(item)
        }
    }

    override fun getItemCount(): Int = data.size
}
