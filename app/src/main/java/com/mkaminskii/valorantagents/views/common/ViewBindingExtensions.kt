package com.mkaminskii.valorantagents.views.common

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.View
import androidx.core.graphics.ColorUtils
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.color.MaterialColors
import com.mkaminskii.valorantagents.databinding.ViewAgentAvatarBinding
import com.mkaminskii.valorantagents.model.Agent

/**
 * Preenche o componente reutilizável de avatar (view_agent_avatar.xml).
 * A cor do agente é opcional: se for nula ou inválida, usa a cor primária do tema.
 */
fun ViewAgentAvatarBinding.bind(agent: Agent) {
    val background = parseColorOrNull(agent.colorHex)
        ?: MaterialColors.getColor(root, androidx.appcompat.R.attr.colorPrimary)
    root.backgroundTintList = ColorStateList.valueOf(background)
    avatarInitial.text = agent.initial
    // Letra escura em cores claras e letra branca em cores escuras.
    val textColor = if (ColorUtils.calculateLuminance(background) > 0.4) Color.BLACK else Color.WHITE
    avatarInitial.setTextColor(textColor)
}

private fun parseColorOrNull(hex: String?): Int? {
    if (hex == null) return null
    return try {
        Color.parseColor(hex)
    } catch (_: IllegalArgumentException) {
        null
    }
}

/** Aplica o padding das barras do sistema (edge-to-edge) na view raiz da tela. */
fun View.applySystemBarsPadding() {
    ViewCompat.setOnApplyWindowInsetsListener(this) { view, insets ->
        val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
        view.setPadding(bars.left, bars.top, bars.right, bars.bottom)
        insets
    }
}
