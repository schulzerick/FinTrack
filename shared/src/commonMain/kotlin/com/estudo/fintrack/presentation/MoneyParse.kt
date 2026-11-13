package com.estudo.fintrack.presentation

    fun parseCents(text: String): Long?{

        val clean = text.trim().replace(',', '.')

        val parts = clean.split(".")

        if (parts.size > 2){
            return null
        }

        val wholeText = parts[0]

        if(!wholeText.all { it.isDigit() }){
            return null
        }

        val whole  = wholeText.toLongOrNull() ?: return null

        val decimalText = parts.getOrElse(1) {""}

        if(decimalText.length> 2){
            return null
        }

        if(!decimalText.all { it.isDigit() }){
            return null
        }

        val decimal = decimalText.padEnd(2, '0').toLong()

        val cents = whole * 100 + decimal

        if(cents <= 0 ){
            return null
        }

        return cents
    }


