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

/**
 * Tela 1: lista de agentes com filtro por função.
 * Ao tocar em um agente, abre [AgentDetailActivity] por Intent explícita passando o id.
 */
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

    /** Infla um chip reutilizável (view_filter_chip.xml) para "Todos" e para cada função. */
    private fun setupRoleFilter() {
        val options: List<AgentRole?> = listOf(null) + MockAgents.roles
        options.forEach { role ->
            val chip = ViewFilterChipBinding.inflate(layoutInflater, binding.roleChips, false).root
            chip.id = View.generateViewId()
            // A lista sempre começa em "Todos"; o chip não guarda estado próprio para não divergir dela.
            chip.isSaveEnabled = false
            chip.text = role?.name ?: getString(R.string.filter_all)
            chip.isChecked = role == null
            chip.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) showAgents(roleId = role?.id)
            }
            binding.roleChips.addView(chip)
        }
    }

    /** Interação que atualiza a interface: troca o conteúdo da lista conforme o filtro. */
    private fun showAgents(roleId: String?) {
        val agents = MockAgents.agents.filter { roleId == null || it.role?.id == roleId }
        adapter.submitList(agents)
    }
}
