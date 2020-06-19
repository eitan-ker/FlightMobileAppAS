package com.example.flightmobileapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.flightmobileapp.databinding.ListItemBinding
import com.example.flightmobileapp.db.Url
import com.example.flightmobileapp.generated.callback.OnClickListener

class MyRecyclerViewAdapter(private val urlList : List<Url>,
                            private val clickListener :(Url) ->
                            Unit):RecyclerView.Adapter<MyViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflator = LayoutInflater.from(parent.context)
        val binding : ListItemBinding =
            DataBindingUtil.inflate(layoutInflator, R.layout.list_item, parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return urlList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(urlList[position], clickListener)
    }

}

class MyViewHolder(val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(url : Url, clickListener :(Url) -> Unit) {
        binding.urlTextView.text = url.url
        binding.listItemLayout.setOnClickListener {
            clickListener(url)
        }

    }
}