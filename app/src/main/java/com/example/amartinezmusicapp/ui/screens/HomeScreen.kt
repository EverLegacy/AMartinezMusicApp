package com.example.amartinezmusicapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
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
import com.example.amartinezmusicapp.viewmodel.HomeViewModel
import com.example.amartinezmusicapp.viewmodel.UiState

@Composable
fun HomeScreen(
    onAlbumClick: (String) -> Unit,
    vm: HomeViewModel = viewModel()
) {
    val state by vm.albums.collectAsState()

    Box(Modifier.fillMaxSize()) {
        LazyColumn(contentPadding = PaddingValues(bottom = 100.dp)) {
            item { HeaderGreeting(name = "Juan Frausto") }
            item { SectionTitle("Albums") }

            // Albums horizontal carousel
            item {
                when (state) {
                    UiState.Idle, UiState.Loading -> LoadingRow()
                    is UiState.Error -> ErrorRow(
                        (state as UiState.Error).message,
                        onRetry = { vm.loadAlbums() }
                    )
                    is UiState.Success -> {
                        val albums = (state as UiState.Success<List<Album>>).data
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(items = albums) { album ->
                                AlbumCard(album = album) { onAlbumClick(album.safeId) }
                            }

                        }
                        }
                    }
                }


            // Recently played section
            item { SectionTitle("Recently Played") }
            if (state is UiState.Success) {
                val albums = (state as UiState.Success<List<Album>>).data
                items(items = albums) { album ->
                    RecentlyPlayedCard(album = album) { onAlbumClick(album.safeId) }
                }

            }
        }

        MiniPlayer(
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
fun HeaderGreeting(name: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .background(
                brush = Brush.verticalGradient(
                    listOf(Color(0xFFB993D6), Color(0xFF8CA6DB))
                ),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Top row with icons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu",
                    tint = Color.White,
                    modifier = Modifier.size(26.dp)
                )

                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color.White,
                    modifier = Modifier.size(26.dp)
                )
            }

            // Texts below icons
            Column {
                Text(
                    text = "Good Morning!",
                    style = MaterialTheme.typography.labelSmall.copy(color = Color.White)
                )
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}




@Composable
fun SectionTitle(title: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )
        Text(
            text = "See more",
            style = MaterialTheme.typography.bodySmall.copy(
                color = Color(0xFF6C63FF),
                fontWeight = FontWeight.SemiBold
            )
        )
    }
}


@Composable
private fun AlbumCard(album: Album, onClick: () -> Unit) {
    Card(
        modifier = Modifier.size(width = 260.dp, height = 200.dp),
        onClick = onClick,
        shape = MaterialTheme.shapes.extraLarge
    ) {
        Box {
            AsyncImage(model = album.image, contentDescription = album.title, modifier = Modifier.fillMaxSize())
            Column(Modifier.align(Alignment.BottomStart).padding(16.dp)) {
                Text(album.title, color = Color.White, fontWeight = FontWeight.Bold)
                Text(album.artist, color = Color.White.copy(alpha = 0.9f))
            }
        }
    }
}

@Composable
private fun RecentlyPlayedCard(album: Album, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.large
    ) {
        Row(
            Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = album.image,
                contentDescription = null,
                modifier = Modifier
                    .size(54.dp)
                    .clip(MaterialTheme.shapes.medium)
            )
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(album.title, fontWeight = FontWeight.Bold)
                Text(album.artist, color = Color.Gray)
            }
            IconButton(onClick = {}) {
                Icon(Icons.Default.MoreVert, contentDescription = null)
            }
        }
    }
}

@Composable
private fun LoadingRow() {
    Box(Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorRow(message: String, onRetry: () -> Unit) {
    Column(
        Modifier.fillMaxWidth().height(200.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Error: $message")
        Spacer(Modifier.height(8.dp))
        Button(onClick = onRetry) { Text("Retry") }
    }
}

@Composable
fun MiniPlayer(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2A1543)),
        shape = MaterialTheme.shapes.extraLarge
    ) {
        Row(
            Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = "https://i.imgur.com/lJwW1Vb.jpeg",
                contentDescription = null,
                modifier = Modifier.size(48.dp).clip(MaterialTheme.shapes.medium)
            )
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text("Tales of Ithiria", color = Color.White, fontWeight = FontWeight.Bold)
                Text("Haggard", color = Color.White.copy(alpha = 0.8f))
            }
            FilledIconButton(onClick = {}) {
                Icon(Icons.Default.PlayArrow, contentDescription = null)
            }
        }
    }
}
