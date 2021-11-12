package com.example.musichub.ui.previewproviders

import androidx.compose.ui.tooling.preview.PreviewParameterProvider

class SampleClickListenerProvider : PreviewParameterProvider<() -> Unit> {
    override val values = sequenceOf({}, {})
    override val count: Int = values.count()
}