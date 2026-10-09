package com.mkaminskii.valorantagents.views.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.commit
import com.mkaminskii.valorantagents.R
import com.mkaminskii.valorantagents.data.mock.MockAgents
import com.mkaminskii.valorantagents.databinding.ActivityAgentDetailBinding
import com.mkaminskii.valorantagents.databinding.ViewBadgeBinding
import com.mkaminskii.valorantagents.model.Agent
import com.mkaminskii.valorantagents.views.common.applySystemBarsPadding
import com.mkaminskii.valorantagents.views.common.bind

/**
 * Tela 2: detalhes de um agente.
 * Recebe o id do agente pela Intent ([EXTRA_AGENT_ID]) e exibe as habilidades
 * dentro de um [AbilitiesFragment], que faz parte desta mesma tela.
 */
class AgentDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAgentDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAgentDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.root.applySystemBarsPadding()
        binding.toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        // O extra é opcional: se não vier ou não corresponder a um agente, avisamos e fechamos a tela.
        val agent = MockAgents.findById(intent.getStringExtra(EXTRA_AGENT_ID))
        if (agent == null) {
            Toast.makeText(this, R.string.agent_not_found, Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        bindAgent(agent)

        // Na recriação da Activity o FragmentManager já restaura o fragment;
        // adicioná-lo de novo criaria uma cópia duplicada.
        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                replace(binding.abilitiesContainer.id, AbilitiesFragment.newInstance(agent.id))
            }
        }
    }

    private fun bindAgent(agent: Agent) {
        binding.avatar.bind(agent)
        binding.agentName.text = agent.name
        binding.description.text = agent.description

        val role = agent.role
        binding.roleBadge.root.text = role?.name ?: getString(R.string.no_role)
        binding.roleCard.isVisible = role != null
        if (role != null) {
            binding.roleTitle.text = getString(R.string.role_title, role.name)
            binding.roleDescription.text = role.description
        }

        val developerName = agent.developerName
        binding.developerName.isVisible = developerName != null
        if (developerName != null) {
            binding.developerName.text = getString(R.string.developer_name, developerName)
        }

        // Tags são opcionais: lista nula ou vazia esconde a linha.
        val tags = agent.tags.orEmpty()
        binding.tagsScroll.isVisible = tags.isNotEmpty()
        val spacing = resources.getDimensionPixelSize(R.dimen.spacing_small)
        tags.forEach { tag ->
            val badge = ViewBadgeBinding.inflate(layoutInflater, binding.tagsRow, false)
            badge.root.text = tag
            badge.root.updateLayoutParams<LinearLayout.LayoutParams> { marginEnd = spacing }
            binding.tagsRow.addView(badge.root)
        }
    }

    companion object {
        const val EXTRA_AGENT_ID = "extra_agent_id"

        fun newIntent(context: Context, agentId: String): Intent =
            Intent(context, AgentDetailActivity::class.java)
                .putExtra(EXTRA_AGENT_ID, agentId)
    }
}
