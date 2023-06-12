package com.bishal.lazyreader.screens.details

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bishal.lazyreader.data.Resource
import com.bishal.lazyreader.model.Item
import com.bishal.lazyreader.repository.BookRepository
import com.bishal.lazyreader.utils.PaletteGenerator.convertImageUrlToBitmap
import com.bishal.lazyreader.utils.PaletteGenerator.extractColorsFromBitmap
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class ReaderDetailViewModel @Inject constructor(
    private val repository: BookRepository,
    private val context: Context
    )
    : ViewModel(){

    suspend fun getBookInfo(bookId: String): Resource<Item> {
        return repository.getBookInfo(bookId)
    }

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent: SharedFlow<UiEvent> = _uiEvent.asSharedFlow()

    private val _colorPalette: MutableState<Map<String, String>> = mutableStateOf(mapOf())
    val colorPalette: State<Map<String, String>> = _colorPalette

//    fun calcDominantColor( drawable: Drawable, onFinish: (Color) -> Unit) {
//        val bmp = (drawable as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888, true)
//
//        Palette.from(bmp).generate { palette ->
//            palette?.dominantSwatch?.rgb?.let { colorValue ->
//                onFinish(Color(colorValue))
//
//            }
//        }
//    }

    fun setColorPalette(colors: Map<String, String>) {
        _colorPalette.value = colors
    }

    fun onBookInfoLoaded(bookInfo: Resource<Item>) {
        if (_colorPalette.value.isEmpty() && bookInfo is Resource.Success) {
            viewModelScope.launch {
                val bitmap = bookInfo.data?.volumeInfo?.imageLinks?.let {
                    convertImageUrlToBitmap(
                        imageUrl = it.thumbnail,
                        context = context
                    )
                }
                if (bitmap != null) {
                    setColorPalette(
                        colors = extractColorsFromBitmap(
                            bitmap = bitmap
                        )
                    )
                }
            }
        }
    }

}
sealed class UiEvent {
    object GenerateColorPalette : UiEvent()
}