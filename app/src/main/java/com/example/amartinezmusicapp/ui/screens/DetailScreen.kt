package com.example.amartinezmusicapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.amartinezmusicapp.data.Album
import com.example.amartinezmusicapp.viewmodel.DetailViewModel
import com.example.amartinezmusicapp.viewmodel.UiState
@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)



@Composable
fun DetailScreen(
    albumId: String,
    onBack: () -> Unit,
    vm: DetailViewModel = viewModel()
) {
    val state by vm.album.collectAsState()

    LaunchedEffect(albumId) {
        vm.loadAlbum(albumId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )

        },
        bottomBar = {
            MiniPlayer(Modifier.fillMaxWidth())
        }
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            when (state) {
                UiState.Idle, UiState.Loading -> CenteredLoading()
                is UiState.Error -> CenteredError((state as UiState.Error).message) {
                    vm.loadAlbum(albumId)
                }
                is UiState.Success -> DetailContent((state as UiState.Success<Album>).data)
            }
        }
    }
}

@Composable
private fun DetailContent(album: Album) {
    LazyColumn(contentPadding = PaddingValues(bottom = 100.dp)) {
        // Header with image, scrim and actions
        item {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(280.dp)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clip(MaterialTheme.shapes.extraLarge)
            ) {
                AsyncImage(model = album.image, contentDescription = album.title, modifier = Modifier.fillMaxSize())
                Box(
                    Modifier.fillMaxSize().background(
                        Brush.verticalGradient(
                            listOf(Color.Transparent, Color(0xAA8E6BFF))
                        )
                    )
                )
                Column(
                    Modifier.align(Alignment.BottomStart).padding(16.dp)
                ) {
                    Text(
                        album.title,
                        color = Color.White,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(album.artist, color = Color.White, style = MaterialTheme.typography.titleMedium)
                    Row(Modifier.padding(top = 12.dp)) {
                        FilledTonalButton(onClick = {}, shape = MaterialTheme.shapes.large) {
                            Icon(Icons.Default.PlayArrow, contentDescription = null)
                            Spacer(Modifier.width(6.dp))
                            Text("Play")
                        }
                        Spacer(Modifier.width(12.dp))
                        OutlinedButton(onClick = {}, shape = MaterialTheme.shapes.large) {
                            Icon(Icons.Default.Refresh, contentDescription = null)

                            Spacer(Modifier.width(6.dp))
                            Text("Shuffle")
                        }
                    }
                }
            }
        }

        // About section
        item {
            ElevatedCard(
                Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                shape = MaterialTheme.shapes.extraLarge
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text("About this album", fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(6.dp))
                    Text(album.description ?: "No description available.")
                }
            }
        }

        // Artist chip
        item {
            AssistChip(
                onClick = {},
                label = { Text("Artist: ${album.artist}") },
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        // Fake tracks
        items((1..10).map { "${album.title} • Track $it" }) { track ->
            TrackRow(album.image, track, album.artist)
        }
    }
}

@Composable
private fun TrackRow(image: String, title: String, artist: String) {
    Card(
        Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = image,
                contentDescription = null,
                modifier = Modifier.size(54.dp).clip(MaterialTheme.shapes.medium)
            )
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.Bold)
                Text(artist, color = Color.Gray)
            }
            IconButton(onClick = {}) { Icon(Icons.Default.MoreVert, contentDescription = null) }
        }
    }
}

@Composable
fun CenteredLoading() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
fun CenteredError(msg: String, retry: () -> Unit) {
    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Error: $msg")
        Spacer(Modifier.height(8.dp))
        Button(onClick = retry) { Text("Retry") }
    }
}
