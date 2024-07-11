package com.evaza.spotify

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import coil.compose.AsyncImage
import com.evaza.spotify.database.DatabaseDao
import com.evaza.spotify.datamodel.Album
import com.evaza.spotify.datamodel.Section
import com.evaza.spotify.network.NetworkApi
import com.evaza.spotify.network.NetworkModule
import com.evaza.spotify.player.PlayerBar
import com.evaza.spotify.player.PlayerViewModel
import com.evaza.spotify.ui.theme.SpotifyTheme
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import javax.inject.Inject

// customized extend AppCompatActivity
// AndroidEntryPoint helps on field inject on Android component
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var api: NetworkApi
    // val networkApi: NetworkApi = ...

    @Inject
    lateinit var databaseDao: DatabaseDao

    private val playerViewModel: PlayerViewModel by viewModels()

    private val TAG = "lifecycle"

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        // Log.d(TAG, "we are at onCreate")
////        setContent {    // react component return <JSX>
////            SpotifyTheme {
////                // A surface container using the 'background' color from the theme
////                Surface(
////                    modifier = Modifier.fillMaxSize(),
////                    color = MaterialTheme.colors.background
////                ) {
//////                    // Greeting("Eva's Android")
//////                    // mutableListOf()
//////                    // by: getter and setter
//////                    // remember
//////                    // summary: "state" needs to write "by remember"
//////                    var name: String by remember { mutableStateOf("") }
//////                    HelloContent(name) {
//////                        name = it
//////                        // name = "Hello, $it"
//////                    }
////                    AlbumCover()
////                }
////            }
////        }
//        // @layout/
//        setContentView(R.layout.activity_main)
//
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "We are at onCreate()")
        setContentView(R.layout.activity_main)

        val navView = findViewById<BottomNavigationView>(R.id.nav_view)     // use resource: R.id.xxx

        val navHostFragment =supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController       // like the "search-box" in google
        // navController.navigate(R.id.homeFragment)
        navController.setGraph(R.navigation.nav_graph)

        NavigationUI.setupWithNavController(navView, navController)

        // https://stackoverflow.com/questions/70703505/navigationui-not-working-correctly-with-bottom-navigation-view-implementation
        navView.setOnItemSelectedListener{
            NavigationUI.onNavDestinationSelected(it, navController)
            navController.popBackStack(it.itemId, inclusive = false)
            true
        }

        val playerBar = findViewById<ComposeView>(R.id.player_bar)
        playerBar.apply {
            setContent {
                MaterialTheme(colors = darkColors()) {
                    PlayerBar(
                        playerViewModel
                    )
                }
            }
        }

//        // Restful API call
//        GlobalScope.launch(Dispatchers.IO) {
////            val retrofit = NetworkModule.provideRetrofit()
////            val networkApi:NetworkApi = retrofit.create(NetworkApi::class.java)
//            val responseCall = networkApi.getHomeFeed()
//            // Call: 任务， 数学作业： Call<数学作业>   // 异步
//            val response : List<Section>? = responseCall.execute().body()
//
//            Log.d("Network", response.toString())
//        }
        // Test retrofit
        GlobalScope.launch(Dispatchers.IO) {
            //val api = NetworkModule.provideRetrofit().create(NetworkApi::class.java)
            val response = api.getHomeFeed().execute().body()
            Log.d("Network", response.toString())
        }
//        // remember it runs everytime I start the app
//        GlobalScope.launch {
//            withContext(Dispatchers.IO) {
//                val album = Album(
//                    id = 1,
//                    name =  "Hexagonal",
//                    year = "2008",
//                    cover = "https://upload.wikimedia.org/wikipedia/en/6/6d/Leessang-Hexagonal_%28cover%29.jpg",
//                    artists = "Lesssang",
//                    description = "Leessang (Korean: 리쌍) was a South Korean hip hop duo, composed of Kang Hee-gun (Gary or Garie) and Gil Seong-joon (Gil)"
//                )
//                databaseDao.favoriteAlbum(album)
//            }
//        }
    }



//    override fun onStart() {
//        super.onStart()
//        Log.d(TAG, "we are at onStart")
//    }
//
//    override fun onResume() {
//        super.onResume()
//        Log.d(TAG, "we are at on onResume")
//    }
//
//    override fun onPause() {
//        super.onPause()
//        Log.d(TAG, "we are at on onPause")
//    }
//
//    override fun onStop() {
//        super.onStop()
//        Log.d(TAG, "we are at on onStop")
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        Log.d(TAG, "we are at on onDestroy")
//    }
}

@Composable
private fun AlbumCover() {
    Column {
        Box(modifier = Modifier.size(160.dp)) {
            AsyncImage(  // 异步把图片下载
                model = "https://upload.wikimedia.org/wikipedia/en/d/d1/Stillfantasy.jpg",
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds
            )
            Text(
                text = "Still Fantasy",
                modifier = Modifier
                    .padding(start = 2.dp, bottom = 3.dp)
                    .align(Alignment.BottomStart),
                color = Color.White
            )
        }
        Text(
            text = "Jay Chou",
            modifier = Modifier.padding(top = 4.dp),
            style = MaterialTheme.typography.body2.copy(fontWeight = FontWeight.Bold),
            color = Color.LightGray
        )
    }
}

@Composable
private fun HelloContent(name: String, onNameChange: (String) -> Unit) {

    Column (modifier = Modifier.padding(16.dp)) {
        if (name.isNotEmpty()) {
            Text(
                text = "Hello!",
                modifier = Modifier.padding(bottom = 8.dp),
                style = MaterialTheme.typography.body2
            )
        }
        OutlinedTextField(
            value = name,
            onValueChange = {
                onNameChange(it)
            },
            label = { Text(text = "Name")}
        )
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Composable
fun ArtistCardBox() {
    Box {
        Text(text = "Show Box")
        Text(text = "3 minute ago")
    }
}

@Composable
fun ArtistCardRow() {
    Row {
        Text(text = "Show Row -- ")
        Text(text = "3 minute ago")
    }
}

@Composable
fun ArtistCardCol() {
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(Color.DarkGray)
            .padding(24.dp)
    ) {
        Text(text = "Show Col -- ")
        Text(text = "3 minute ago")
        Row {
            Text(text = "Show Row -- ")
            Text(text = "3 minute ago")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SpotifyTheme {
        Surface {
            Greeting("Eva's Android")
        }
    }
}

@Preview
@Composable
fun PreviewArtistCardBox() {
    SpotifyTheme {
        Surface {
            ArtistCardBox()
        }
    }
}

@Preview
@Composable
fun PreviewArtistCardRow() {
    SpotifyTheme {
        Surface {
            ArtistCardRow()
        }
    }
}

@Preview
@Composable
fun PreviewArtistCardCol() {
    SpotifyTheme {
        Surface {
            ArtistCardCol()
        }
    }
}