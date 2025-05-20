package com.yhsh.flowstudy.kit

/**
 * 密封类示例代码
 */
sealed class SealedClass {
    object People : SealedClass()
    data class Student(val age: Int) : SealedClass()
    data class Animal(val type: String) : SealedClass()
}

fun handlerResult(s: SealedClass) {
    when (s) {
        is SealedClass.Animal -> println("执行动物了${s.type}")
        is SealedClass.Student -> println("执行student了${s.age}")
        is SealedClass.People -> println("执行people了")
    }
}

fun main() {
    handlerResult(SealedClass.People)
    handlerResult(SealedClass.Student(19))
    handlerResult(SealedClass.Animal("加菲猫"))
}
