package com.nuzharukiya.gitm.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.nuzharukiya.gitm.R
import com.nuzharukiya.gitm.models.PullRequestModel
import com.nuzharukiya.gitm.utils.inflate

/**
 * Created by Nuzha Rukiya on 19/07/20.
 */

class PullReqAdapter(private val pullRequests: ArrayList<PullRequestModel>) : RecyclerView.Adapter<PullReqAdapter.ViewHolder>() {

    private var mContext: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        mContext = parent.context

        return ViewHolder(parent.inflate(R.layout.rv_pull_request))
    }

    override fun getItemCount(): Int {
        return pullRequests.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val prModel = pullRequests[position]

        holder.onBind(prModel)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)
        private val tvDescription = itemView.findViewById<TextView>(R.id.tvDescription)

        fun onBind(prModel: PullRequestModel) {
            tvTitle.text = prModel.title
            tvDescription.text = ("""#${prModel.number} opened on ${prModel.created_at} by ${prModel.user.login}""") //TODO: format time
        }
    }
}