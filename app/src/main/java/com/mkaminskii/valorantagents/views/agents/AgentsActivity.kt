package com.mkaminskii.valorantagents.views.agents

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.mkaminskii.valorantagents.R
import com.mkaminskii.valorantagents.data.mock.MockAgents
import com.mkaminskii.valorantagents.databinding.ActivityAgentsBinding
import com.mkaminskii.valorantagents.databinding.ViewFilterChipBinding
import com.mkaminskii.valorantagents.model.AgentRole
import com.mkaminskii.valorantagents.views.common.applySystemBarsPadding
import com.mkaminskii.valorantagents.views.detail.AgentDetailActivity

class AgentsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAgentsBinding

    private val adapter = AgentsAdapter { agent ->
        startActivity(AgentDetailActivity.newIntent(this, agent.id))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAgentsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.root.applySystemBarsPadding()

        binding.agentsList.layoutManager = LinearLayoutManager(this)
        binding.agentsList.adapter = adapter

        setupRoleFilter()
        showAgents(roleId = null)
    }

    private fun setupRoleFilter() {
        val options: List<AgentRole?> = listOf(null) + MockAgents.roles
        options.forEach { role ->
            val chip = ViewFilterChipBinding.inflate(layoutInflater, binding.roleChips, false).root
            chip.id = View.generateViewId()
            chip.isSaveEnabled = false
            chip.text = role?.name ?: getString(R.string.filter_all)
            chip.isChecked = role == null
            chip.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) showAgents(roleId = role?.id)
            }
            binding.roleChips.addView(chip)
        }
    }

    private fun showAgents(roleId: String?) {
        val agents = MockAgents.agents.filter { roleId == null || it.role?.id == roleId }
        adapter.submitList(agents)
    }
}
