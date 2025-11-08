import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter

@Composable
fun ImagePreview(selectedImageUri: Uri?) {
    if (selectedImageUri != null) {
        Image(
            painter = rememberAsyncImagePainter(selectedImageUri),
            contentDescription = "Selected Image",
            modifier = Modifier
                .size(200.dp), // adjust size as needed
            contentScale = ContentScale.Crop
        )
    }
}