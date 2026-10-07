package com.pemob.resepnindya.ui.screens

import android.content.Intent
import androidx.core.net.toUri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.pemob.resepnindya.data.model.Meal
import com.pemob.resepnindya.ui.viewmodel.RecipeViewModel
import com.pemob.resepnindya.ui.viewmodel.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    idMeal: String,
    viewModel: RecipeViewModel,
    navigateBack: () -> Unit
) {
    val detailState by viewModel.detailState.collectAsState()

    LaunchedEffect(idMeal) {
        viewModel.getRecipeDetails(idMeal)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail Resep", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary, // Lebih gelap (sesuai request)
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val state = detailState) {
                is UiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is UiState.Error -> {
                    Text(
                        text = state.message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is UiState.Success -> {
                    DetailContent(meal = state.data)
                }
            }
        }
    }
}

@Composable
fun DetailContent(meal: Meal) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            AsyncImage(
                model = meal.strMealThumb,
                contentDescription = meal.strMeal,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                contentScale = ContentScale.Crop
            )
            
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = meal.strMeal ?: "Unknown",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    SuggestionChip(
                        onClick = { },
                        label = { Text("Kategori: ${meal.strCategory}") }
                    )
                    SuggestionChip(
                        onClick = { },
                        label = { Text("Asal: ${meal.strArea}") }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = "Bahan-bahan",
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Bahan-bahan",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                
                meal.ingredientsWithMeasures.forEach { (ingredient, measure) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "• $ingredient", modifier = Modifier.weight(1f))
                        Text(text = measure, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.MenuBook,
                        contentDescription = "Instruksi",
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Instruksi Memasak",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                val instructionsList = meal.strInstructions?.split(Regex("\r?\n"))
                    ?.map { it.trim() }
                    ?.filter { it.isNotEmpty() } ?: emptyList()
                    
                if (instructionsList.isEmpty()) {
                    Text(
                        text = "Tidak ada instruksi.",
                        style = MaterialTheme.typography.bodyLarge
                    )
                } else {
                    instructionsList.forEachIndexed { index, step ->
                        val cleanStep = step
                            .replace(Regex("^(?:\\d+\\.|step\\s*\\d+|\\d+)\\s*", RegexOption.IGNORE_CASE), "")
                            .replace(Regex("^[-*•]+\\s*"), "")
                            .trim()
                        val displayStep = cleanStep.ifEmpty { step }

                        Row(modifier = Modifier.padding(bottom = 8.dp)) {
                            Text(
                                text = "${index + 1}.",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.width(28.dp)
                            )
                            Text(
                                text = displayStep,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
                
                if (!meal.strYoutube.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    val context = LocalContext.current
                    Button(
                        onClick = {
                            val intent = Intent(Intent.ACTION_VIEW, meal.strYoutube.toUri())
                            context.startActivity(intent)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE52D27)) // YouTube Red
                    ) {
                        Text("Tonton Tutorial di YouTube")
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
