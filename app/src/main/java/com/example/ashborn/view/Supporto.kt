import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.ashborn.ui.theme.AshbornTheme
import com.example.ashborn.ui.theme.MediumPadding
import com.example.ashborn.viewModel.AshbornViewModel
import androidx.compose.ui.res.stringResource
import com.example.ashborn.R

@Composable
fun ErroreGenerico(navController: NavHostController, viewModel: AshbornViewModel) {
    val context = LocalContext.current
    Column (
        modifier = Modifier.padding(MediumPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {

        Text(text = stringResource(id = R.string.app_name), fontWeight =FontWeight.Bold, fontStyle = FontStyle.Italic) //TODO: da sistemare
        Text(text = stringResource(id = R.string.assist))
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "800 xxx xxxx ", fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(16.dp))

Text(text = stringResource(id = R.string.assist1))
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "+39 011 xxx xxxx", fontWeight = FontWeight.Bold)


        Text(text = stringResource(id = R.string.assist2))
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "07.00 - 00.00 ", fontWeight = FontWeight.Bold)

        Text(text = stringResource(id = R.string.assist3))
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "09.00 - 19.00 ", fontWeight = FontWeight.Bold)
    }
}


@Preview(showBackground = true) @Composable
fun Preview() {
    val viewModel = AshbornViewModel()
    val navController = rememberNavController()
    AshbornTheme {

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
           ErroreGenerico (navController = navController, viewModel =viewModel )
        }
    }
}