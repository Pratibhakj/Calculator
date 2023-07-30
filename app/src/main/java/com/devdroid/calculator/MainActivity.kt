package com.devdroid.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import com.devdroid.calculator.databinding.ActivityMainBinding
import net.objecthunter.exp4j.Expression
import net.objecthunter.exp4j.ExpressionBuilder

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    var lastNumeric = false
    var stateError = false
    var lastDot=false

    private  lateinit var expression: Expression
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun onEqualClick(view: View) {
        onEqual()
        binding.operand.text=binding.result.text.toString().drop(1)
    }
    fun onDigitClick(view: View) {
        if (stateError){
            binding.operand.text=(view as Button).text
            stateError=false
        }
        else{
            binding.operand.append((view as Button).text)
        }
        lastNumeric=true
        onEqual()

    }
    fun onAllClearClick(view: View) {
        binding.operand.text=""
        binding.result.text=""
        stateError=false
        lastDot=false
        lastNumeric=false
        binding.result.visibility=View.GONE

    }
    fun onOperatorClick(view: View) {
        if(!stateError && lastNumeric){
            binding.operand.append((view as Button).text)
            lastDot=false
            lastNumeric=false
            onEqual()
        }
    }
    fun onClearClick(view: View) {
        binding.operand.text=""
        lastNumeric=false
    }
    fun onBackClick(view: View) {
        binding.operand.text=binding.operand.text.toString().dropLast(1)
        try {
            val lastChar =binding.operand.text.toString().last()
            if(lastChar.isDigit()){
                onEqual()
            }
        }catch (e:Exception){
            binding.result.text=""
            binding.result.visibility=View.GONE
            Log.e("last char error",e.toString())
        }
    }
    fun onEqual(){
        if(lastNumeric && !stateError){
            val txt=binding.operand.text.toString()
            expression=ExpressionBuilder(txt).build()
            try {
                val result =expression.evaluate()
                binding.result.visibility=View.VISIBLE
                binding.result.text="="+result

            }catch (ex : java.lang.ArithmeticException){
                Log.e("Evaluate error",ex.toString())
                binding.result.text="Error"
                stateError=true
                lastNumeric=false
            }

        }

    }
}