package com.estudo.fintrack.presentation

import kotlin.test.Test
import kotlin.test.assertEquals

class FormatCentsTest {


    @Test
    fun format_euros_and_cents(){
        assertEquals("104,70 €", formatCents(10470))
    }

    @Test
    fun format_cents(){
        assertEquals("0,05 €", formatCents(5))
    }

    @Test
    fun format_zero(){
        assertEquals("0,00 €", formatCents(0))
    }

    @Test
    fun format_negative(){
        assertEquals("-12,50 €", formatCents(-1250))
    }



}