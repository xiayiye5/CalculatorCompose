package com.yhsh.flamingocompose.page

import android.util.Log
import android.widget.Toast
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
    val input = remember {
        mutableStateOf("0")
    }
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
                    it.forEach {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(if (it.first == "0") 2f else 1f)
                                .clip(CircleShape)
                                .background(color = it.second)
                                .aspectRatio(if (it.first == "0") 2f else 1f)
                                .clickable {
                                    //输入数字
                                    Log.d("开始输入了", "输入了${it.first}")
                                    input.value += it.first
                                    Toast
                                        .makeText(
                                            context, "点击了${input.value}", Toast.LENGTH_LONG
                                        )
                                        .show()
                                }, contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = it.first, color = Color.White, fontSize = 26.sp
                            )
                        }
                    }
                }
            }

        }
    }
}