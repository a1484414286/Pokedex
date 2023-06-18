import android.graphics.Paint
import android.graphics.Typeface
import android.text.TextPaint
import android.text.style.MetricAffectingSpan

class CustomFontSpan(private val typeface: Typeface) : MetricAffectingSpan() {

    override fun updateDrawState(ds: TextPaint) {
        applyTypeface(ds)
    }

    override fun updateMeasureState(paint: TextPaint) {
        applyTypeface(paint)
    }

    private fun applyTypeface(paint: Paint) {
        val oldTypeface = paint.typeface
        val newTypeface = Typeface.create(typeface, oldTypeface?.style ?: Typeface.NORMAL)
        paint.typeface = newTypeface
    }
}