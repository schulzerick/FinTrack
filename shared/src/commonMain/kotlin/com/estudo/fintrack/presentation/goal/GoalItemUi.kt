package com.estudo.fintrack.presentation

data class GoalItemUi(val id: Long, val name: String, val saved: String,
    val target: String, val percent: Int, val progress: Float, val complete: Boolean)