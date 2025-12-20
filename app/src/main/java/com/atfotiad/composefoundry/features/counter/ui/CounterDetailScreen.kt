package com.atfotiad.composefoundry.features.counter.ui


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.atfotiad.composefoundry.annotations.Destination

@Destination(route = "counter_detail/{count}")
@Composable
fun CounterDetailScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "This is the Details Screen! 🎉")
    }
}
