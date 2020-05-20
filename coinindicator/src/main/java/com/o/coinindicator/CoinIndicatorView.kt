package com.o.coinindicator

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.util.Log
import android.view.View


class CoinIndicatorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    View(context, attrs, defStyleAttr) {

    companion object {
        private const val DEFAULT_START_COLOR = Color.WHITE
        private const val DEFAULT_END_COLOR = Color.BLACK
        private const val DEFAULT_TEXT_COLOR = Color.WHITE
        private const val DEFAULT_PLUS_COLOR = Color.WHITE
        private const val DEFAULT_PLUS_BACKGROUND_COLOR = Color.BLACK
        private const val DEFAULT_PLUS_THIKNESS = 4
        private const val DEFAULT_HAS_ICON = false
        private var DEFAULT_ICON_RESID = R.drawable.ic_coin_24dp
        private const val DEFAULT_PLUS_PERCENT_PADDING = 20
        private const val DEFAULT_TEXT = ""
        private const val DEFAULT_TEXT_SIZE = 12
        private const val DEFAULT_ASPECT_RATIO = 5
    }

    private var startColor: Int =
        DEFAULT_START_COLOR
    private var endColor: Int =
        DEFAULT_END_COLOR
    private var plusColor: Int =
        DEFAULT_PLUS_COLOR
    private var plusBackGroundColor: Int =
        DEFAULT_PLUS_BACKGROUND_COLOR
    private var plusThickness: Int =
        DEFAULT_PLUS_THIKNESS
    private var iconHasIcon: Boolean =
        DEFAULT_HAS_ICON
    private var iconIconResId: Int =
        DEFAULT_ICON_RESID
    private var plusPaddingPercent: Float = DEFAULT_PLUS_PERCENT_PADDING * 0.01f
    private var text: String? =
        DEFAULT_TEXT
    private var textSize: Int =
        DEFAULT_TEXT_SIZE
    private var textColor: Int =
        DEFAULT_TEXT_COLOR
    private var viewAspectRatio: Int =
        DEFAULT_ASPECT_RATIO

    private val paintCommonRect = Paint(Paint.ANTI_ALIAS_FLAG) //паинт для общего прямоугольника
    private val paintRightRect = Paint(Paint.ANTI_ALIAS_FLAG) //паинт для правого прямоугольника
    private val paintPlus = Paint(Paint.ANTI_ALIAS_FLAG) //паинт для символа плюс
    private val paintIcon = Paint(Paint.ANTI_ALIAS_FLAG) //паинт для иконки
    private val paintText = Paint(Paint.ANTI_ALIAS_FLAG) //паинт для текста
    private var textRect: Rect = Rect() //прямоугольник между иконкой и плюсом для вывода текста

    private lateinit var bitmapSource: Bitmap
    private lateinit var linearGradient: LinearGradient

    init {
        isSaveEnabled = true

        if (attrs != null) {
            val ta = context.obtainStyledAttributes(attrs, R.styleable.CoinIndicatorView)
            startColor =
                ta.getColor(R.styleable.CoinIndicatorView_ci_startColor,
                    DEFAULT_START_COLOR
                )
            endColor = ta.getColor(R.styleable.CoinIndicatorView_ci_endColor,
                DEFAULT_END_COLOR
            )
            plusColor = ta.getColor(R.styleable.CoinIndicatorView_ci_plusColor,
                DEFAULT_PLUS_COLOR
            )
            plusBackGroundColor = ta.getColor(
                R.styleable.CoinIndicatorView_ci_plusBackgroundColor,
                DEFAULT_PLUS_BACKGROUND_COLOR
            )
            plusThickness =
                ta.getDimensionPixelSize(
                    R.styleable.CoinIndicatorView_ci_plusThickness,
                    DEFAULT_PLUS_THIKNESS
                )
            plusPaddingPercent = ta.getInteger(
                R.styleable.CoinIndicatorView_ci_plusPaddingPercent,
                DEFAULT_PLUS_PERCENT_PADDING
            ) * 0.01f

            iconHasIcon = ta.getBoolean(R.styleable.CoinIndicatorView_ci_hasIcon, false)
            iconIconResId =
                ta.getResourceId(R.styleable.CoinIndicatorView_ci_icon,
                    DEFAULT_ICON_RESID
                )

            textSize = ta.getDimensionPixelSize(
                R.styleable.CoinIndicatorView_ci_textSize,
                DEFAULT_TEXT_SIZE
            )

            text = ta.getString(R.styleable.CoinIndicatorView_ci_text) ?: ""

            textColor = ta.getColor(R.styleable.CoinIndicatorView_ci_textColor,
                DEFAULT_TEXT_COLOR
            )

            viewAspectRatio =
                ta.getInteger(R.styleable.CoinIndicatorView_ci_aspectRatio,
                    DEFAULT_ASPECT_RATIO
                )



            ta.recycle()
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        this.layoutParams.width = this.layoutParams.height * viewAspectRatio

        val endXGrad = width - height
        linearGradient = LinearGradient(
            0f,
            0f,
            endXGrad.toFloat(),
            0f,
            startColor,
            endColor,
            Shader.TileMode.CLAMP
        )

        @Suppress("DEPRECATION") val d = resources.getDrawable(iconIconResId)
        bitmapSource = Bitmap.createScaledBitmap(
            drawableToBitmap(d),
            (height * 0.9f).toInt(),
            (height * 0.9f).toInt(),
            true
        )

        val left = if (iconHasIcon) height else 0
        textRect = Rect(left, 0, width - height, height)
    }

    private fun drawableToBitmap(drawable: Drawable): Bitmap {
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    fun setText(text: String) {
        this.text = text
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        //рисуем общий прямоугольник
        paintCommonRect.shader = linearGradient
        paintCommonRect.style = Paint.Style.FILL
        canvas.drawPaint(paintCommonRect)

        //рисуем правый прямоугольник
        with(paintRightRect)
        {
            color = plusBackGroundColor
            style = Paint.Style.FILL
        }

        val paintRightRectLeft = (width - height).toFloat()
        val paintRightRectTop = 0f
        val paintRightRectRight = width.toFloat()
        val paintRightRectBottom = height.toFloat()

        canvas.drawRect(
            paintRightRectLeft,
            paintRightRectTop,
            paintRightRectRight,
            paintRightRectBottom,
            paintRightRect
        )

        //рисуем плюс
        val plusVertLeft = paintRightRectLeft + ((paintRightRectRight - paintRightRectLeft) / 2)
        val plusVertTop = (height - height * plusPaddingPercent)
        val plusVertBottom = (height * plusPaddingPercent)
        val plusVertRight = plusVertLeft

        with(paintPlus)
        {
            color = plusColor
            strokeWidth = plusThickness.toFloat()
        }
        canvas.drawLine(plusVertLeft, plusVertTop, plusVertRight, plusVertBottom, paintPlus)

        val plusHorLeft =
            paintRightRectLeft + (paintRightRectRight - paintRightRectLeft) * plusPaddingPercent
        val plusHorRight =
            paintRightRectRight - (paintRightRectRight - paintRightRectLeft) * plusPaddingPercent
        val plusHorTop = (paintRightRectBottom - paintRightRectTop) / 2
        val plusHorBottom = plusHorTop

        canvas.drawLine(plusHorLeft, plusHorTop, plusHorRight, plusHorBottom, paintPlus)

        //рисуем иконку
        if (iconHasIcon) {
            val paddingXY = height * 0.05f
            canvas.drawBitmap(bitmapSource, paddingXY, paddingXY, paintIcon)
        }

        //рисуем текст
        if (!text.isNullOrBlank()) {
            with(paintText)
            {
                color = textColor
                textSize = this@CoinIndicatorView.textSize.toFloat()
                style = Paint.Style.STROKE
                textAlign = Paint.Align.CENTER
            }

            val offsetY = (paintText.descent() + paintText.ascent()) / 2
            canvas.drawText(
                text ?: DEFAULT_TEXT,
                textRect.exactCenterX(),
                textRect.exactCenterY() - offsetY,
                paintText
            )
        }
    }

    override fun onSaveInstanceState(): Parcelable? {
        val savedState =
            SavedState(super.onSaveInstanceState())
        savedState.textValue = text
        return savedState
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        Log.d("TAg", "onRestoreInstanceState $id")
        if (state is SavedState) {
            super.onRestoreInstanceState(state.superState)
            text = state.textValue
        } else {
            super.onRestoreInstanceState(state)
        }
    }

    private class SavedState : BaseSavedState, Parcelable {
        var textValue: String? = ""

        constructor(parcel: Parcel) : super(parcel) {
            textValue = parcel.readString()
        }

        constructor(superState: Parcelable?) : super(superState)

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeString(textValue)
        }

        override fun describeContents(): Int = 0

        companion object CREATOR : Parcelable.Creator<SavedState> {
            override fun createFromParcel(source: Parcel) =
                SavedState(parcel = source)
            override fun newArray(size: Int): Array<SavedState?> = arrayOfNulls(size)
        }

    }
}


