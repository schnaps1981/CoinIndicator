# CoinIndicator
Custom View следующего вида 


![Альтернативный текст](https://github.com/schnaps1981/CoinIndicator/blob/master/ScreenShot.png)

подключение


**implementation 'com.o.coinindicator:coinindicator:1.0.0'**


использование в layout

```xml
<com.o.coinindicator.CoinIndicatorView
    android:id="@+id/ci_view"
    android:layout_width="0dp"
    android:layout_height="24dp"
    app:ci_aspectRatio="5"
    app:ci_plusPaddingPercent="20"
    app:ci_hasIcon="true"
    app:ci_icon="@drawable/inventory_currency_gold"
    app:ci_plusThickness="2dp"
    app:ci_textColor="#FAC62A"
    app:ci_startColor="#A57624"
    app:ci_endColor="#583B0F"
    app:ci_plusColor="#7A664B"
    app:ci_plusBackgroundColor="#4E320B"
    app:ci_textSize="16sp"
    app:ci_text="101020.30"/>
```

* ширина, указанная в атрибутах игнорируется, расчитывается через высоту и ci_aspectRatio: 
width = height * ci_aspectRatio
* ci_plusPaddingPercent отступ линий "плюса" от края. Задается в процентах высоты
* ci_hasIcon - флаг отрисовки иконки справа
* ci_icon - drawable иконки
* ci_plusThickness - толщина линий "плюса"
* ci_textColor - цвет текста
* ci_startColor - начальный цвет градиента фона поля с иконокой и текстом
* ci_endColor - конечный цвет градиента фона поля с иконокой и текстом
* ci_plusColor - цвет линий плюса
* ci_plusBackgroundColor - цвет фона поля с плюсом
* ci_textSize - размер текста
* ci_text - текст



