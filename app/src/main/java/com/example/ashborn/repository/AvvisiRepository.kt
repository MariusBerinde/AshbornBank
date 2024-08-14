package com.example.ashborn.repository

import com.example.ashborn.dao.AshbornDao
import com.example.ashborn.data.StatoAvviso

class AvvisiRepository(private val ashbornDao: AshbornDao) {

     fun getAvvisi(codCliente:String) = ashbornDao.getAvvisi(codCliente)
     fun aggiornaStatoAvviso(id:Long,nuovoStato:StatoAvviso) = ashbornDao.aggiornaStatoAvviso(id,nuovoStato)
}