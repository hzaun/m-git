package com.nuzharukiya.gitm.adapters

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import com.nuzharukiya.gitm.R
import com.nuzharukiya.gitm.models.PullRequestModel
import com.nuzharukiya.gitm.utils.TimeUtils
import com.nuzharukiya.gitm.utils.inflate
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject


/**
 * Created by Nuzha Rukiya on 19/07/20.
 */

class PullReqAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mContext: Context? = null
    private val pullRequests = ArrayList<PullRequestModel>()

    // Flags for footer ProgressBar
    private var bIsLoadingAdded = false
    private val ITEM = 0
    private val LOADING = 1

    // To check onClick
    private val clickSubject = PublishSubject.create<PullRequestModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        mContext = parent.context

        return when (viewType) {
            ITEM -> ItemViewHolder(parent.inflate(R.layout.rv_pull_request))
            else -> LoaderViewHolder(parent.inflate(R.layout.rv_loader))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == pullRequests.size - 1 && bIsLoadingAdded) LOADING else ITEM
    }

    override fun getItemCount(): Int {
        return pullRequests.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val prModel = pullRequests[position]

        when (getItemViewType(position)) {
            ITEM -> {
                holder as ItemViewHolder
                holder.onBind(prModel)
                holder.vDivider.visibility = if (position == pullRequests.size - 1) View.GONE else View.VISIBLE

                holder.clParent.setOnClickListener {
                    clickSubject.onNext(pullRequests[position])
                }
            }
            LOADING -> {
                holder as LoaderViewHolder
            }
        }

    }

    val clickEvent: Observable<PullRequestModel> = clickSubject

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val clParent = itemView.findViewById<ConstraintLayout>(R.id.clParent)
        private val tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)
        private val tvDescription = itemView.findViewById<TextView>(R.id.tvDescription)
        internal val vDivider = itemView.findViewById<View>(R.id.vDivider)

        init {
            itemView.setOnClickListener {
                clickSubject.onNext(pullRequests[layoutPosition])
            }
        }

        fun onBind(prModel: PullRequestModel) {
            tvTitle.text = prModel.title
            tvDescription.text = ("#${prModel.number} " +
                    "opened on ${TimeUtils.getInstance().convertDateTimeToSimpleFormat(prModel.created_at)} " +
                    "by ${prModel.user.login}")
        }
    }

    inner class LoaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val pbPagination = itemView.findViewById<ProgressBar>(R.id.pbPagination)
    }

    // Helper methods

    fun addLoadingFooter() {
        bIsLoadingAdded = true
        add(PullRequestModel(bLoader = true))
    }

    private fun add(prModel: PullRequestModel) {
        pullRequests.add(prModel)
        notifyItemInserted(pullRequests.size - 1)
    }

    fun addAll(prList: List<PullRequestModel>) {
        pullRequests.addAll(prList)
        notifyDataSetChanged()
    }

    fun clear() {
        bIsLoadingAdded = false
        pullRequests.clear()
    }

    fun isEmpty(): Boolean {
        return pullRequests.isEmpty()
    }

    fun removeLoadingFooter() {
        bIsLoadingAdded = false

        val position = pullRequests.size - 1
        val item = getItem(position)
        if (item != null) {
            pullRequests.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    private fun getItem(position: Int): PullRequestModel? {
        return pullRequests[position]
    }
}