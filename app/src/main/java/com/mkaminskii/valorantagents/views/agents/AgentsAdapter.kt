package com.mkaminskii.valorantagents.views.agents

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mkaminskii.valorantagents.R
import com.mkaminskii.valorantagents.databinding.ItemAgentBinding
import com.mkaminskii.valorantagents.model.Agent
import com.mkaminskii.valorantagents.views.common.bind

class AgentsAdapter(
    private val onAgentClick: (Agent) -> Unit,
) : ListAdapter<Agent, AgentsAdapter.ViewHolder>(DiffCallback) {

    class ViewHolder(val binding: ItemAgentBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAgentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val agent = getItem(position)
        val context = holder.binding.root.context
        val count = agent.abilities.size

        with(holder.binding) {
            avatar.bind(agent)
            agentName.text = agent.name
            roleBadge.root.text = agent.role?.name ?: context.getString(R.string.no_role)
            abilitiesCount.text = context.resources.getQuantityString(
                R.plurals.abilities_count,
                count,
                count,
            )
            root.setOnClickListener { onAgentClick(agent) }
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<Agent>() {
        override fun areItemsTheSame(oldItem: Agent, newItem: Agent) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Agent, newItem: Agent) = oldItem == newItem
    }
}
