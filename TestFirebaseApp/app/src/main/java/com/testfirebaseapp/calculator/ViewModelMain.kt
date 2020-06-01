package com.testfirebaseapp.calculator

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.testfirebaseapp.R
import kotlinx.android.synthetic.main.calculator_view.view.*
import kotlin.math.sqrt

class ViewModelMain internal constructor(private val mainRepository: MainRepository) : ViewModel() {


    fun click(context: Context, v: View) {
        v.btn_clear.setOnLongClickListener {
            clean(v)
            true
        }


        val clickListener = View.OnClickListener { view ->

            when (view.getId()) {
                R.id.btn_0 -> adder("0", v)
                R.id.btn_1 -> adder("1", v)
                R.id.btn_2 -> adder("2", v)
                R.id.btn_3 -> adder("3", v)
                R.id.btn_4 -> adder("4", v)
                R.id.btn_5 -> adder("5", v)
                R.id.btn_6 -> adder("6", v)
                R.id.btn_7 -> adder("7", v)
                R.id.btn_8 -> adder("8", v)
                R.id.btn_9 -> adder("9", v)
                R.id.btn_decimal -> adder(".", v)
                R.id.btn_divide -> adder(" / ", v)
                R.id.btn_multiply -> adder(" * ", v)
                R.id.btn_plus -> adder(" + ", v)
                R.id.btn_minus -> adder(" - ", v)
                R.id.btn_root -> isroot(v)
                R.id.btn_equals -> ishas(v)
                R.id.btn_pl_mn ->plus_(v)
                R.id.btn_clear -> clear(v)
                R.id.button -> blackandwhite(v, context)
            }
        }
        v.button.setOnClickListener(clickListener)
        v.btn_0.setOnClickListener(clickListener)
        v.btn_1.setOnClickListener(clickListener)
        v.btn_2.setOnClickListener(clickListener)
        v.btn_3.setOnClickListener(clickListener)
        v.btn_4.setOnClickListener(clickListener)
        v.btn_5.setOnClickListener(clickListener)
        v.btn_6.setOnClickListener(clickListener)
        v.btn_7.setOnClickListener(clickListener)
        v.btn_8.setOnClickListener(clickListener)
        v.btn_9.setOnClickListener(clickListener)
        v.btn_decimal.setOnClickListener(clickListener)
        v.btn_divide.setOnClickListener(clickListener)
        v.btn_multiply.setOnClickListener(clickListener)
        v.btn_plus.setOnClickListener(clickListener)
        v.btn_minus.setOnClickListener(clickListener)
        v.btn_equals.setOnClickListener(clickListener)
        v.btn_root.setOnClickListener(clickListener)
        v.btn_clear.setOnClickListener(clickListener)
        v.btn_pl_mn.setOnClickListener(clickListener)
    }


    @SuppressLint("SetTextI18n")
    fun adder(str: String, view: View) {
        if (view.text_sum.text.toString() == "0" && str.toInt() < 10 && str.toInt() >= 0 && view.text_sum.text.toString().length < 2)
            view.text_sum.text = str
        else
            view.text_sum.text = view.text_sum.text.toString() + str
    }

    fun clean(v: View) {
        v.text_formula.text = ""
        v.text_sum.text = "0"
    }
    fun clear(v:View) {
        v.text_sum.text="0"
    }

    fun ishas(v: View) {

        var strr = eval(v.text_sum.text.toString()).toString()
        v.text_formula.text = v.text_sum.text.toString()

        if (strr[strr.length - 2] == '.' && strr[strr.length - 1] == '0')
            v.text_sum.text = eval(v.text_sum.text.toString()).toInt().toString()
        else
            v.text_sum.text = eval(v.text_sum.text.toString()).toString()

    }

    fun isroot(v: View) {
        val strr = sqrt(eval(v.text_sum.text.toString())).toString()
        v.text_formula.text = v.text_sum.text.toString()

        v.text_sum.text = strr

    }
    fun plus_(v:View){
        var strr = eval(v.text_sum.text.toString()).toString()
        v.text_formula.text = v.text_sum.text.toString()

        if (strr[strr.length - 2] == '.' && strr[strr.length - 1] == '0')
            v.text_sum.text=
                (eval(v.text_sum.text.toString()).toInt()*-1).toString()
        else
            v.text_sum.text=
                (eval(v.text_sum.text.toString()) *-1).toString()



    }

    @SuppressLint("ResourceType")
    fun blackandwhite(v: View, cxt: Context) {

        val white = ContextCompat.getColor(cxt, R.color.colorWhite)
        val black = ContextCompat.getColor(cxt, R.color.colorBlack)

        if (v.button.text.toString() == "white") {
            v.setBackgroundColor(black)
            v.btn_clear.setTextColor(white)
            v.btn_pl_mn.setTextColor(white)
            v.btn_percent.setTextColor(white)
            v.btn_root.setTextColor(white)
            v.text_formula.setTextColor(white)
            v.text_sum.setTextColor(white)
            v.btn_decimal.setTextColor(white)
            v.button.setTextColor(white)
            v.button.setText("black")
        } else {
            v.setBackgroundColor(white)
            v.btn_clear.setTextColor(black)
            v.btn_pl_mn.setTextColor(black)
            v.btn_percent.setTextColor(black)
            v.btn_root.setTextColor(black)
            v.text_formula.setTextColor(black)
            v.text_sum.setTextColor(black)
            v.btn_decimal.setTextColor(black)
            v.button.setTextColor(black)
            v.button.setText("white")
        }

    }


    class Factory(private val mainRepository: MainRepository) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>) = ViewModelMain(mainRepository) as T
    }

}