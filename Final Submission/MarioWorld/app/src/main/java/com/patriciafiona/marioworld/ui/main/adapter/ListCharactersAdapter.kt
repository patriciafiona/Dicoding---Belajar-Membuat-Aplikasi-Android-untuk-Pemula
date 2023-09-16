package com.patriciafiona.marioworld.ui.main.adapter

import android.animation.ValueAnimator
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.patriciafiona.marioworld.R
import com.patriciafiona.marioworld.data.entities.Character
import com.patriciafiona.marioworld.utils.Utils.fadeVisibility


class ListCharactersAdapter(private val listCharacters: ArrayList<Character>) : RecyclerView.Adapter<ListCharactersAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_character, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val character = listCharacters[position]
        var isExpand: Boolean = false
        var prevHeight: Int = 0

        holder.tvName.text = character.name
        holder.tvDescription.text = character.description
        holder.ivPhoto.setImageResource(if (isExpand) character.imageOpen else character.imageClose)
        holder.container.setCardBackgroundColor(
            Color.rgb(character.backgroundColor[0],character.backgroundColor[1], character.backgroundColor[2])
        )

        holder.btnExpand.setOnClickListener {
            isExpand = !isExpand

            holder.ivPhoto.setImageResource(if (isExpand) character.imageOpen else character.imageClose)
            holder.btnExpand.setImageResource(if (isExpand) R.drawable.ic_arrow_up else R.drawable.ic_arrow_down)

            if(prevHeight == 0){ prevHeight =  holder.container.measuredHeight }

            val anim = ValueAnimator.ofInt(holder.container.measuredHeight, if (isExpand) prevHeight + 200 else prevHeight)
            anim.addUpdateListener { valueAnimator ->
                val `val` = valueAnimator.animatedValue as Int
                val layoutParams: ViewGroup.LayoutParams = holder.container.layoutParams
                layoutParams.height = `val`
                holder.container.layoutParams = layoutParams
            }
            anim.duration = 700
            anim.start()

            if (isExpand){
                holder.btnSeeDetails.fadeVisibility(View.VISIBLE, 700)
            }else{
                holder.btnSeeDetails.fadeVisibility(View.GONE, 2000)
            }
        }

        holder.btnSeeDetails.setOnClickListener { onItemClickCallback.onItemClicked(character) }
    }

    override fun getItemCount(): Int = listCharacters.size

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivPhoto: ImageView = itemView.findViewById(R.id.iv_photo)
        val tvName: TextView = itemView.findViewById(R.id.tv_name)
        val tvDescription: TextView = itemView.findViewById(R.id.tv_desc)
        val btnExpand: ImageButton = itemView.findViewById(R.id.btn_expand)
        val btnSeeDetails: AppCompatButton = itemView.findViewById(R.id.btn_see_detail)
        val container: CardView = itemView.findViewById(R.id.card_container)
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Character)
    }
}