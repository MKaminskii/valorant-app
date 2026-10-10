package com.mkaminskii.valorantagents.views.common

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.View
import androidx.core.graphics.ColorUtils
import androidx.core.view.isVisible
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.color.MaterialColors
import com.mkaminskii.valorantagents.databinding.ViewAgentAvatarBinding
import com.mkaminskii.valorantagents.model.Agent

fun ViewAgentAvatarBinding.bind(agent: Agent) {
    val background = agent.accentColor(root)
    root.backgroundTintList = ColorStateList.valueOf(background)

    val iconRes = agent.iconRes
    avatarImage.isVisible = iconRes != null
    avatarInitial.isVisible = iconRes == null
    if (iconRes != null) {
        avatarImage.setImageResource(iconRes)
    } else {
        avatarInitial.text = agent.initial
        val textColor = if (ColorUtils.calculateLuminance(background) > 0.4) Color.BLACK else Color.WHITE
        avatarInitial.setTextColor(textColor)
    }
}

fun Agent.accentColor(view: View): Int =
    parseColorOrNull(colorHex) ?: MaterialColors.getColor(view, androidx.appcompat.R.attr.colorPrimary)

private fun parseColorOrNull(hex: String?): Int? {
    if (hex == null) return null
    return try {
        Color.parseColor(hex)
    } catch (_: IllegalArgumentException) {
        null
    }
}

fun View.applySystemBarsPadding() {
    ViewCompat.setOnApplyWindowInsetsListener(this) { view, insets ->
        val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
        view.setPadding(bars.left, bars.top, bars.right, bars.bottom)
        insets
    }
}
