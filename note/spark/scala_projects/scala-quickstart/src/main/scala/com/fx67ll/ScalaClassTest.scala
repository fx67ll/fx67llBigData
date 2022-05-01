package com.fx67ll

class ScalaClassTest(name: String, age: Int) {
  def printerInfo(): Unit = {
    println(this.name + this.age);
  }
}

// 引入伴生对象
object ScalaClassTest {
  val school: String = "fx67ll"

  def main(args: Array[String]): Unit = {
    val test1 = new ScalaClassTest("test1 - ", 20)
    val test2 = new ScalaClassTest("test2 - ", 22)

    test1.printerInfo()
    test2.printerInfo()

    println("${school}.com")
    val testNum = 2.3456
    // 表示输出变量
    println(s"The testNum is ${testNum}")
    // f"${变量}%2.2f" 2.2表示各位整个数长度2不够则补空格，小数位表示保留2位小数字
    println(f"The testNum is ${testNum}%2.2f")
    // raw 表示不使用格式化模板字符串原样输出
    println(raw"The testNum is ${testNum}%2.2f")

    // for循环
    for (i <- 1.to(2)) print(s"${i}/")
    for (i <- Range(11, 14)) print(s"${i}-")
    for (i <- 23 until 25) println(s"${i}")

    // 直接拿Array、List、Set
    for (i <- Array(1, 3, 5)) println(s"${i}")
    for (i <- List(11, 23, 35)) println(s"${i}")
    for (i <- Set(15, 43, 65)) println(s"${i}")

    // 嵌套循环
    for (i <- 1 to 3; j <- 1 to 5) {
      print("*");
      if (j == 5) {
        println()
      }
    }

    // 循环守卫
    for (i <- 1 to 10 if i % 3 == 0) println(i)

    // 循环步长
    for (i <- 1 to 10 by 2) println(i)

    // for推导式（返回值）
    val v = for (i <- 1 to 10) yield i * 10
  }
}
