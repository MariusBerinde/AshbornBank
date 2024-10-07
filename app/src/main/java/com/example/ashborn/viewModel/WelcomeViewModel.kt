package com.example.ashborn.viewModel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ashborn.model.DataStoreManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
/**
 * WelcomeViewModel is a ViewModel responsible for managing user data and permissions
 * related to the welcome screen of the app.
 *
 * This ViewModel uses `DataStoreManager` to retrieve and update user-related preferences,
 * such as whether the user has requested permissions. It interacts with the
 * Android application's lifecycle and manages state using Jetpack Compose's `mutableStateOf`.
 *
 * @property application The application instance used to access the context and DataStore.
 */
class  WelcomeViewModel(application: Application): AndroidViewModel(application){
    /**
     * Instance of `DataStoreManager` used for reading and writing user preferences.
     */
    var dsm= DataStoreManager.getInstance(application)
    /**
     * Tracks whether the user has requested permission.
     * This state is mutable and backed by Jetpack Compose's `mutableStateOf`.
     * The initial value is retrieved from the DataStore in a blocking manner using
     * the IO dispatcher.
     *
     * The setter is private to restrict direct modification outside of the ViewModel.
     */
    var hasRequestedPermission by mutableStateOf(
        runBlocking(Dispatchers.IO) {
            dsm.hasRequestedPermissionFlow.first()
        }
    )
        private set
    /**
     * Stores the user's name.
     * The value is initialized to an empty string and can be retrieved using [getUsername].
     */
    var userName = ""
    /**
     * Retrieves the username stored in the DataStore.
     *
     * This method executes a blocking call to fetch the username from the `DataStoreManager`
     * on the IO dispatcher.
     *
     * @return The username stored in the DataStore.
     */
    fun getUsername():String{
        var ris: String
        runBlocking(Dispatchers.IO) { ris = dsm.usernameFlow.first() }
        return ris
    }
    /**
     * Updates the state indicating whether the user has requested permissions.
     *
     * This method updates the `hasRequestedPermission` state and asynchronously writes
     * the new value to the DataStore using a coroutine in the IO dispatcher.
     *
     * @param hasRequestedPermission The new value indicating if the user has requested permissions.
     */
    fun setHasRequestedPermissionX(hasRequestedPermission : Boolean) {
        this.hasRequestedPermission = hasRequestedPermission
        viewModelScope.launch (Dispatchers.IO){
            dsm.writeHasRequestedPermission(hasRequestedPermission)
        }
    }

}
/**
 * Factory class for creating an instance of `WelcomeViewModel`.
 *
 * This factory is used when the `ViewModel` requires an `Application` instance
 * in its constructor. It is necessary to instantiate `WelcomeViewModel` within
 * the `ViewModelProvider` context.
 *
 * @property application The application instance passed to the ViewModel.
 */
@Suppress("UNCHECKED_CAST")
class WelcomeViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WelcomeViewModel::class.java)) {
            return WelcomeViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}