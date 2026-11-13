package com.estudo.fintrack.presentation


import kotlin.test.Test
import kotlin.test.assertEquals

class ParseCentsTest {

    @Test
    fun parse_cents_decimal_coma(){
        assertEquals(1250, parseCents("12,50"))
    }

    @Test
    fun parse_cents_decimal_dot(){
        assertEquals(1250, parseCents("12.50"))
    }

    @Test
    fun parse_cents_no_decimal(){
        assertEquals(1200, parseCents("12"))
    }

    @Test
    fun parse_cents_one_decimal(){
        assertEquals(1250, parseCents("12,5"))
    }

    @Test
    fun parse_cents_with_spaces(){
        assertEquals(705, parseCents("  7,05 "))
    }

    @Test
    fun parse_cents_less_than_one_euro(){
        assertEquals(99, parseCents("0,99"))
    }

    @Test
    fun parse_cents_empty(){
        assertEquals(null, parseCents(""))
    }

    @Test
    fun parse_cents_not_a_number(){
        assertEquals(null, parseCents("abc"))
    }

    @Test
    fun parse_cents_to_mutch_decimals(){
        assertEquals(null, parseCents("12,505"))
    }

    @Test
    fun parse_cents_negative(){
        assertEquals(null, parseCents("-5"))
    }

    @Test
    fun parse_cents_zero(){
        assertEquals(null, parseCents("0"))
    }



}