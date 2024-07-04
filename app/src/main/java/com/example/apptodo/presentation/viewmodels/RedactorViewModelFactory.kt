package com.example.apptodo.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.apptodo.data.repository.TodoItemsRepository
import com.example.apptodo.domain.ITodoItemsRepository

class RedactorViewModelFactory(
    private val todoItemsRepository : ITodoItemsRepository
) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RedactorViewModel(
            todoItemsRepository
        ) as T
    }

}