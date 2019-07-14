package com.example.githubsearch

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.githubsearch.model.Items
import kotlinx.android.synthetic.main.item_row.view.*

class ChapterAdapter(private val context: MainActivity, private var chaptersList: List<Items>) :
    RecyclerView.Adapter<ChapterAdapter.ViewHolder>() {
    lateinit var mainActivity: MainActivity
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_row, parent, false))
    }

    override fun getItemCount(): Int {
        return chaptersList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.repoName?.text = chaptersList[position].full_name
        /*todo callback should be returned to the view(activity) to be able to pass actions to domain layer
          //todo use interface callback or using high order functions
            //todo one of best advantages of modern languages is it internally host functional paradigms*/

        holder.itemView.setOnClickListener {
            /*     Toast.makeText(context, chaptersList[position], Toast.LENGTH_LONG).show()
            */
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val repoName = view.tv_item_row

    }

    fun updateItems(updatedItems: List<Items>) {
        chaptersList = updatedItems
        notifyDataSetChanged()
    }

}