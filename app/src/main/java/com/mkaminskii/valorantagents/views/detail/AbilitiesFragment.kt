package com.mkaminskii.valorantagents.views.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.google.android.material.color.MaterialColors
import com.mkaminskii.valorantagents.R
import com.mkaminskii.valorantagents.data.mock.MockAgents
import com.mkaminskii.valorantagents.databinding.FragmentAbilitiesBinding
import com.mkaminskii.valorantagents.databinding.ItemAbilityBinding
import com.mkaminskii.valorantagents.model.Ability

/**
 * Fragment da seção "Habilidades" da tela de detalhe.
 * Cada habilidade é um componente reutilizável (item_ability.xml) inflado em tempo de execução;
 * ao tocar em uma delas, o card de descrição é atualizado.
 */
class AbilitiesFragment : Fragment() {

    // O binding só existe entre onCreateView e onDestroyView, evitando vazar a hierarquia de Views.
    private var _binding: FragmentAbilitiesBinding? = null
    private val binding get() = _binding!!

    private val itemBindings = mutableListOf<ItemAbilityBinding>()
    private var abilities: List<Ability> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAbilitiesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val agentId = requireArguments().getString(ARG_AGENT_ID)
        abilities = MockAgents.findById(agentId)?.orderedAbilities.orEmpty()

        // Sem habilidades, a seção fica oculta.
        binding.root.isVisible = abilities.isNotEmpty()
        if (abilities.isEmpty()) return

        itemBindings.clear()
        abilities.forEachIndexed { index, ability ->
            val item = ItemAbilityBinding.inflate(layoutInflater, binding.abilitiesRow, false)
            item.abilityKey.text = ability.slot.key
            item.abilityName.text = ability.name
            item.root.setOnClickListener { selectAbility(index) }
            binding.abilitiesRow.addView(item.root)
            itemBindings += item
        }

        selectAbility(0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        itemBindings.clear()
        _binding = null
    }

    /** Interação que atualiza a interface: destaca a habilidade tocada e mostra sua descrição. */
    private fun selectAbility(index: Int) {
        val ability = abilities[index]
        binding.selectedAbilitySlot.text = getString(R.string.ability_slot_key, ability.slot.key)
        binding.selectedAbilityName.text = ability.name
        binding.selectedAbilityDescription.text = ability.description

        val selectedStroke = MaterialColors.getColor(binding.root, androidx.appcompat.R.attr.colorPrimary)
        val defaultStroke = MaterialColors.getColor(
            binding.root,
            com.google.android.material.R.attr.colorOutline,
        )
        val density = resources.displayMetrics.density
        itemBindings.forEachIndexed { i, item ->
            val isSelected = i == index
            item.root.strokeColor = if (isSelected) selectedStroke else defaultStroke
            item.root.strokeWidth = ((if (isSelected) 2 else 1) * density).toInt()
        }
    }

    companion object {
        private const val ARG_AGENT_ID = "arg_agent_id"

        fun newInstance(agentId: String) = AbilitiesFragment().apply {
            arguments = bundleOf(ARG_AGENT_ID to agentId)
        }
    }
}
