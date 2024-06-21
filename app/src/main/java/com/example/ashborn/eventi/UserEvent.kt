package com.example.ashborn.eventi

sealed interface UserEvent {
    object SaveUser:UserEvent
    data class setUserName(val userName:String)
}