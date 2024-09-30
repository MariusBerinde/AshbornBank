package com.example.ashborn.repository

import com.example.ashborn.dao.AshbornDao
import com.example.ashborn.data.Avviso
import com.example.ashborn.data.StatoAvviso
import java.time.LocalDateTime

class AvvisiRepository(private val ashbornDao: AshbornDao) {

     fun getAvvisi(codCliente:String) = ashbornDao.getAvvisi(codCliente)
     fun aggiornaStatoAvviso(id:Long,nuovoStato:StatoAvviso) = ashbornDao.aggiornaStatoAvviso(id,nuovoStato)
     fun aggiungiAvviso(avviso:Avviso) = ashbornDao.aggiungiAvviso(avviso)
     suspend fun rimuoviAvviso(avviso: Avviso) = ashbornDao.rimuoviAvviso(avviso)
}