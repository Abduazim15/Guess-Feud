package com.skipissue.guess.adapter

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.skipissue.guess.R
import com.skipissue.guess.model.QuestionEntity
import com.skipissue.guess.model.VariantResponse


class VariantAdapter(val query: QuestionEntity) :
    ListAdapter<VariantResponse, VariantAdapter.HistoryViewHolder>(CharacterComparator) {
    private var onClickListener: ((Int) -> Unit)? = null
    fun setOnClickClickListener(clickListener: (Int) -> Unit) {
        onClickListener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.variant_item, parent, false)
        return HistoryViewHolder(view, onClickListener)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val character = getItem(position)
        character?.let { holder.bind(it) }
    }

    object CharacterComparator : DiffUtil.ItemCallback<VariantResponse>() {
        override fun areItemsTheSame(oldItem: VariantResponse, newItem: VariantResponse): Boolean {
            return oldItem.phrase == newItem.phrase
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: VariantResponse,
            newItem: VariantResponse
        ): Boolean {
            return oldItem == newItem
        }
    }

    inner class HistoryViewHolder(view: View, val onItemClickListener: ((Int) -> Unit)?) :
        RecyclerView.ViewHolder(view) {
        private val layout: CardView = view.findViewById(R.id.item)
        private val cancer: LinearLayout = view.findViewById(R.id.cancer)
        private val questionT: TextView = view.findViewById(R.id.body)
        private val variantT: TextView = view.findViewById(R.id.variant)
        private var isFinished = false
        var anim = ObjectAnimator()

        fun bind(question: VariantResponse) {
            anim = ObjectAnimator.ofInt(
                cancer,
                "backgroundColor",
                if (cancer.background is ColorDrawable) (cancer.background as ColorDrawable).color else Color.TRANSPARENT,
                Color.TRANSPARENT
            )
            anim.setEvaluator(ArgbEvaluator())
            anim.duration = 1000
            val variant = question.phrase.substring(query.question.length)
            questionT.setText(query.question)
            variantT.setText(variant)
            if (bindingAdapterPosition == itemCount -1)
                startAnim()
            layout.setOnClickListener {
                onItemClickListener?.invoke(bindingAdapterPosition)
            }
        }

        fun startAnim() {
            if (!isFinished) {
                anim.start()
                isFinished = true
            }
        }
    }
}
