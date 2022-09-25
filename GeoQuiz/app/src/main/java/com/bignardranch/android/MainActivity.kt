package com.bignardranch.android

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

/**
 * Объект `ViewGroup` - подвид `View`, который содержит и упорядочивает другие виджеты. При этом
 * сам `ViewGroup` контент не отображает. Он распоряжается тем, где отображается содержимое других
 * представлений. Объекты `ViewGroup` ещё часто называют __макетами__.
 *
 * В Макете `activity` по умолчанию `ConstraintLayout` - это `ViewGroup`, отвечающий за
 * расположение своего единственного дочернего элемента, виджета `TextView`.
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.
        setContentView(R.layout.activity_main)

        // Every widget (for example TextView or Button) inherited by View class
        //var button = Button(Context())
    }
}