package com.estudo.fintrack.presentation

import kotlin.math.absoluteValue


fun formatCents(cents: Long): String {

        val absolute = kotlin.math.abs(cents)

        val euro = absolute.absoluteValue / 100
        val remainder = absolute % 100

        val centsFormat = remainder.toString().padStart(2, '0')

        val sign = if (cents < 0) "-" else ""

        return "$sign$euro,$centsFormat €"

    }
