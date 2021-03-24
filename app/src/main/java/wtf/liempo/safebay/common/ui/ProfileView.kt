package wtf.liempo.safebay.common.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import wtf.liempo.safebay.databinding.ViewProfileBinding


class ProfileView : ConstraintLayout {

    private var _binding: ViewProfileBinding? = null
    private val binding get() = _binding!!

    constructor(
        context: Context,
        attrs: AttributeSet,
        defStyle: Int) : super(
        context, attrs, defStyle
    ) {
        inflate()
    }

    constructor(
        context: Context,
        attrs: AttributeSet
    ) : super(context, attrs) {
        inflate()
    }

    constructor(context: Context) : super(context) {
        inflate()
    }

    private fun inflate() {
        _binding = ViewProfileBinding.inflate(
            LayoutInflater.from(context),
            this, true)
    }

}
