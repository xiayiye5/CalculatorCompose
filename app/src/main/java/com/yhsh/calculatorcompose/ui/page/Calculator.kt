package com.yhsh.calculatorcompose.ui.page

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yhsh.calculatorcompose.ui.theme.ClBg
import com.yhsh.calculatorcompose.ui.theme.DarkGray
import com.yhsh.calculatorcompose.ui.theme.LightGray
import com.yhsh.calculatorcompose.ui.theme.Orange

/**
 * 主要改动说明：
1. 添加了三个状态变量：
- operator : 存储当前的运算符
- firstNumber : 存储第一个输入的数字
- isNewNumber : 标记是否需要开始新的数字输入
2. 实现了数字按钮的输入逻辑，包括小数点的处理
3. 实现了运算符的处理逻辑
4. 实现了等号按钮的计算逻辑
5. 实现了清除(AC)、正负号转换(+/-)和百分比(%)功能

现在这个计算器可以：
- 进行基本的加减乘除运算
- 处理小数点输入
- 转换正负号
- 计算百分比
- 清除输入
- 连续计算
你可以继续使用这个计算器，如果发现任何问题或需要添加新功能，请告诉我。
 */
val data = arrayOf(
    arrayOf("AC" to LightGray, "+/-" to LightGray, "%" to LightGray, "/" to Orange),
    arrayOf("7" to DarkGray, "8" to DarkGray, "9" to DarkGray, "X" to Orange),
    arrayOf("4" to DarkGray, "5" to DarkGray, "6" to DarkGray, "—" to Orange),
    arrayOf("1" to DarkGray, "2" to DarkGray, "3" to DarkGray, "+" to Orange),
    arrayOf("0" to DarkGray, "." to DarkGray, "=" to DarkGray)
)
val dataPair = arrayOf(
    arrayOf(
        Pair("AC", DarkGray), Pair("+/-", DarkGray), Pair("%", DarkGray), Pair("/", Orange)
    ), arrayOf(
        Pair("7", DarkGray), Pair("8", DarkGray), Pair("9", DarkGray), Pair("x", Orange)
    ), arrayOf(
        Pair("4", DarkGray), Pair("5", DarkGray), Pair("6", DarkGray), Pair("—", Orange)
    ), arrayOf(
        Pair("1", DarkGray), Pair("2", DarkGray), Pair("3", DarkGray), Pair("+", Orange)
    ), arrayOf(Pair("0", DarkGray), Pair(".", DarkGray), Pair("=", Orange))
)

@Composable
fun Calculator() {
    val context = LocalContext.current
    val input = remember { mutableStateOf("0") }
    val operator = remember { mutableStateOf("") }
    val firstNumber = remember { mutableStateOf("") }
    val isNewNumber = remember { mutableStateOf(true) }
    Column(
        modifier = Modifier
            .background(color = ClBg)
            .padding(horizontal = 15.dp)
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.3f), contentAlignment = Alignment.BottomEnd
        ) {
            Text(
                text = input.value,
                fontSize = 42.sp,
                color = Color.White,
//                style = TextStyle(lineHeight = 42.sp),
                //设置文本行高，2种方法都可以
                lineHeight = 42.sp
            )
        }
        Column(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            data.forEach {
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    it.forEach { item ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(if (item.first == "0") 2f else 1f)
                                .clip(CircleShape)
                                .background(color = item.second)
                                .aspectRatio(if (item.first == "0") 2f else 1f)
                                .clickable {
                                    when (item.first) {
                                        "AC" -> {
                                            input.value = "0"
                                            operator.value = ""
                                            firstNumber.value = ""
                                            isNewNumber.value = true
                                        }

                                        in "0123456789." -> {
                                            if (isNewNumber.value) {
                                                input.value = item.first
                                                isNewNumber.value = false
                                            } else {
                                                if (item.first == "." && input.value.contains(".")) {
                                                    return@clickable
                                                }
                                                input.value += item.first
                                            }
                                        }

                                        in arrayOf("+", "—", "X", "/") -> {
                                            operator.value = item.first
                                            firstNumber.value = input.value
                                            isNewNumber.value = true
                                        }

                                        "=" -> {
                                            if (operator.value.isNotEmpty() && firstNumber.value.isNotEmpty()) {
                                                val num1 = firstNumber.value.toDouble()
                                                val num2 = input.value.toDouble()
                                                val result = when (operator.value) {
                                                    "+" -> num1 + num2
                                                    "—" -> num1 - num2
                                                    "X" -> num1 * num2
                                                    "/" -> if (num2 != 0.0) num1 / num2 else Double.POSITIVE_INFINITY
                                                    else -> num2
                                                }
                                                input.value = if (result % 1.0 == 0.0) {
                                                    result
                                                        .toInt()
                                                        .toString()
                                                } else {
                                                    result.toString()
                                                }
                                                operator.value = ""
                                                firstNumber.value = ""
                                                isNewNumber.value = true
                                            }
                                        }

                                        "+/-" -> {
                                            if (input.value != "0") {
                                                input.value = if (input.value.startsWith("-")) {
                                                    input.value.substring(1)
                                                } else {
                                                    "-${input.value}"
                                                }
                                            }
                                        }

                                        "%" -> {
                                            val value = input.value.toDouble()
                                            input.value = (value / 100).toString()
                                        }
                                    }
                                }, contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = item.first, color = Color.White, fontSize = 26.sp
                            )
                        }
                    }
                }
            }
        }
    }
}