package com.example.ashborn

import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime

class Validatore {
    /**
     * Controlla il formato di dataNascita , il formato è considerato valido se rispetta il pattern giorno/mese/anno o giorno-mese-anno
     * @param dataNascita : la data di nascita del cliente
     * @return true se data di nascita ha un formato valido
     */
    fun formatoDataNascitaValida(dataNascita: String): Boolean{
        var ris = false
        if (dataNascita.isEmpty()) return ris
        val pattern= "\\d{1,2}-\\d{1,2}-\\d{4} || \\d{1,2}/\\d{1,2}/\\d{4}"
        if (Regex(pattern).matches(dataNascita)){
            val campiData: List<String> = dataNascita.replace("-","/").split("/")
            val day = campiData[0].toInt()
            val month = campiData[1].toInt()
            val year = campiData[2].toInt()
            val dataEsistente: Boolean  = when(month){
                1,3,5,7,8,10,12 -> if(day in 1..31) true
                else false

                4,6,9,11 -> if(day in 1..30) true
                else false

                2 -> if( (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)){
                    if(day in 1..29) true
                    else false
                } else {
                    if(day in 1..28) true
                    else false
                }

                else -> false
            }

            if(dataEsistente) {
                val oggi = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
                val maggiorenne = LocalDate(year, month, day) <= oggi.minus(18, DateTimeUnit.YEAR)
                if (maggiorenne)
                    ris = true
            }
        }
        return ris
    }

    fun formatoCodiceCliente(codCliente: String): Boolean {
        var ris = false
        val codClienteL = codCliente.replace(" ", "")
        if( codClienteL.length == 9){
            val regex = Regex("[^a-z0-9]")
            ris = !regex.containsMatchIn(codClienteL)
        }
        return ris
    }

    fun formatoNomeValido(nome: String): Boolean {
        var ris = false
        val nome1 = nome.trimEnd().trimStart().replace("  ", " ")
        val regex = Regex("[^a-zA-Z0-9\\s]")
        val caratteri_speciali = regex.containsMatchIn(nome1)
        if (nome.length in 2..20 && !caratteri_speciali){ ris = true }
        return ris
    }
    fun formatoCognomeValido(cognome: String): Boolean {
        return formatoNomeValido(cognome)
    }

    /**@param beneficiario il nome e il cognome del beneficiario
     * @param iban il codice iban del beneficiario
     * @param importo l'importo del bonifico in formato stringa
     * @param causale la motivazione del bonifico
     * @param dataAccredito la data in cui verrà effettuato il bonifico
     * @return true se il bonifico ha dati validi per effettuarlo false altrimenti
     */
    fun formatoBonificoValido(beneficiario: String, iban: String, importo: String, causale: String, dataAccredito: String): Boolean {
        val beneficiarioOK: Boolean = formatoBeneficiarioValido(beneficiario)
        val ibanOK: Boolean = formatoIbanValido(iban)
        val importoOK: Boolean = formatoImportoValido(importo)
        val causaleOK: Boolean = formatoCausaleValida(causale)
        val dataAccreditoOK: Boolean = formatoDataAccreditoValida(dataAccredito)
        return beneficiarioOK && ibanOK && importoOK && causaleOK && dataAccreditoOK
    }

    /**
     * @param beneficiario il beneficiario del bonifico deve contenere nome e cognome del beneficiario o il nome di un'azienda
     * @return true se il beneficiario è valido false altrimenti
     */
    fun formatoBeneficiarioValido(beneficiario: String): Boolean {
        var ris = false
        //val regex = Regex("[^a-zA-Z]")
        val regex = Regex("[^a-zA-Z\\s]")

        val caratteri_speciali = regex.containsMatchIn(beneficiario)
        if ( beneficiario.length > 2 && beneficiario.length < 100 ){
            if ( !caratteri_speciali){
                ris = true
            }
        }
        return ris
    }

    /**
     * @param iban l'iban da controllare
     * @return true se l'iban è valido false altrimenti
     */
    fun formatoIbanValido(iban: String): Boolean {
        // Rimuove gli spazi e converte l'IBAN in maiuscolo
        val cleanedIban = iban.replace(" ", "").uppercase()

        // Verifica la lunghezza minima e massima dell'IBAN
        if (cleanedIban.length < 15 || cleanedIban.length > 34) {
            return false
        }

        // Verifica che l'IBAN contenga solo lettere e cifre
        if (!cleanedIban.all { it.isLetterOrDigit() }) {
            return false
        }

        // Algoritmo di controllo (mod 97)
        val rearrangedIban = cleanedIban.drop(4) + cleanedIban.take(4)
        val numericIban = rearrangedIban.map {
            if (it.isDigit()) it.toString()
            else (it.code - 'A'.code + 10).toString()
        }.joinToString("")

        val ibanAsBigInteger = numericIban.toBigInteger()
        return ibanAsBigInteger.mod(97.toBigInteger()) == 1.toBigInteger()
    }

    /**
     * @param causale la causale del bonifico da effettuare
     * @return true se la causale è valida false altrimenti
     */
    fun formatoCausaleValida(causale: String): Boolean {
        return causale.isNotEmpty() && causale.length < 300
    }

    /**
     * @param dataAccredito la data in cui verrà effettuato il bonifico
     * @return true se la data è valida false altrimenti
     */
    fun formatoDataAccreditoValida(dataAccredito: String): Boolean {
        var ris:Boolean = false
        val pattern= "\\d{1,2}-\\d{1,2}-\\d{4}||\\d{1,2}/\\d{1,2}/\\d{4}"
        if (Regex(pattern).matches(dataAccredito)){
            val campiData: List<String> = dataAccredito.replace("-","/").split("/")
            val day = campiData[0].toInt()
            val month = campiData[1].toInt()
            val year = campiData[2].toInt()
            val dataEsistente: Boolean
            dataEsistente = when(month){
                1,3,5,7,8,10,12 -> if(day > 0 && day <= 31) true
                else false

                4,6,9,11 -> if(day > 0 && day <= 30) true
                else false

                2 -> if( (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)){
                    if(day > 0 && day <= 29) true
                    else false
                } else {
                    if(day > 0 && day <= 28) true
                    else false
                }

                else -> false
            }

            if(dataEsistente) {
                val dataAccreditoData = LocalDate(year, month, day)
                val oggi = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
                if(dataAccreditoData >= oggi && dataAccreditoData <= oggi.plus(6, DateTimeUnit.MONTH)){
                    ris = true
                }
            }
        }
        return ris
    }

    /**
     * @param importo l'importo del bonifico da effettuare
     * @return true se l'importo è valido false altrimenti
     */
    fun formatoImportoValido(importo: String): Boolean {
        var ris:Boolean = false
        val pattern= "\\d{1,5}\\.\\d{0,2}"

        if (Regex(pattern).matches(importo)){
            ris = (importo.toDouble() < 15000.0)
        }
        return ris
    }
}