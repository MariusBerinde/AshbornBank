package com.example.ashborn.repository

import com.example.ashborn.dao.AshbornDao
import com.example.ashborn.data.Operation
import java.time.LocalDateTime

class OperationRepository(private val ashbornDao: AshbornDao) {

    suspend fun insertOperation(operation: Operation) = ashbornDao.insertOperation(operation)

    suspend fun insertAllOperations(listOperation: List<Operation>) = ashbornDao.insertAllOperations(listOperation)

    suspend fun deleteOperation(operation: Operation) = ashbornDao.deleteOperation(operation)

    fun getOperations(clientCode: String, from: LocalDateTime, upTo: LocalDateTime, offset: Int, limit: Int) = ashbornDao.getOperations(clientCode, from, upTo, offset, limit)
}