package com.example.ashborn

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.ashborn.ui.theme.AshbornTheme
import com.example.ashborn.view.AskPIN
import com.example.ashborn.view.Welcome
import com.example.ashborn.viewModel.AshbornViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AshbornTheme {
                val viewModel: AshbornViewModel by viewModels()
                val navController = rememberNavController()
           /*     NavHost(navController = navController, startDestination = "login") {
                    composable("login") { Welcome(navController, viewModel) }

                }*/
                AppNavigazione(viewModel = viewModel, name ="" , modifier =Modifier )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AshbornPreview() {
    AshbornTheme {
        val viewModel: AshbornViewModel = AshbornViewModel()
        val navController = rememberNavController()
        AppNavigazione(viewModel = viewModel, name = "", modifier = Modifier)
    }
}

@Composable
fun AppNavigazione(viewModel: AshbornViewModel,name:String,modifier: Modifier){
    val navController = rememberNavController()
    val startDest="init";
    NavHost(navController=navController, startDestination = startDest){
        navigation(startDestination = "welcome",route="init"){
            composable("welcome"){
                Welcome(navController = navController, viewModel = viewModel)
            }
            composable("login"){
                AskPIN(navController = navController, viewModel = viewModel)
            }
            /*
            composable("primoLogin"){
                PrimoLogin(navController = navController, viewModel = viewModel)
            }*/
        }
        /*navigation(startDestination = "generico",route="problemi"){
           composable("generico"){
               Supporto()
           }
        }*/
    }
}

