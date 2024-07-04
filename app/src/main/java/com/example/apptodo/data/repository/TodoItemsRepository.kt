package com.example.apptodo.data.repository

import com.example.apptodo.data.dao.TodoDao
import com.example.apptodo.data.entity.TodoItem
import com.example.apptodo.domain.ITodoItemsRepository
import com.example.apptodo.data.entity.Relevance
import com.example.apptodo.data.network.TodoBackend
import com.example.apptodo.data.network.exception.NetworkException
import com.example.apptodo.data.network.mapper.CloudTodoItemToEntityMapper
import com.example.apptodo.data.network.model.GenericToDoResponse
import com.example.apptodo.data.network.utils.NetworkChecker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class TodoItemsRepository(
    private val todoDao: TodoDao,
    private val todoBackend: TodoBackend,
    private val networkChecker: NetworkChecker,
    private val cloudTodoItemToEntityMapper: CloudTodoItemToEntityMapper,
    private val lastKnownRevisionRepository: LastKnownRevisionRepository
) : ITodoItemsRepository{


    override suspend fun addItem(todoItem: TodoItem) {
        todoDao.addItem(todoItem)
    }

    override suspend fun deleteItemById(id: String) {
        todoDao.deleteItemById(id)
    }

    override suspend fun getItems(): List<TodoItem> {
        return todoDao.getItems()
    }


    override suspend fun getItem(id: String): TodoItem? {
        return todoDao.getItem(id)
    }

    override suspend fun checkItem(item: TodoItem, checked: Boolean) {
        val id = item.id
        todoDao.checkItem(id, checked)
    }

    override suspend fun countChecked(): Int {
        return todoDao.countChecked()
    }

    override suspend fun updateItem(updatedItem: TodoItem) {
        todoDao.updateItem(updatedItem)
    }

    private suspend fun <T : GenericToDoResponse> handle(block: suspend () -> Response<T>): T {
        val response = block.invoke()
        val body = response.body()
        if (response.isSuccessful && body != null) {
            lastKnownRevisionRepository.updateRevision(body.revision)
            return body
        } else {
            throw NetworkException(response.errorBody()?.string())
        }
    }

}