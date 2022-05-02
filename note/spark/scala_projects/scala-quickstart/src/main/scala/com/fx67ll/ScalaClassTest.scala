package com.fx67ll

import scala.annotation.tailrec

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

    // 可变参数，放最后
    def f1(str1: String, str2: String*): Unit = {
      println(str1, str2)
    }

    f1("aaa", "bbb", "ccc")

    // 有默认值的参数，放最后
    def f2(str1: String, str2: String = "fx67ll"): Unit = {
      println(str1, str2)
    }

    f2("ll76xf")

    // 带名参数就是传参时候带上参数名
    f2(str2 = "ll76xf-1", str1 = "ll76xf-2")

    // 高阶函数，作为值传递和作为参数传递
    def testHighLevel(arg1: Double => String, arg2: Double => Double, arg3: Double): String = {
      arg1(arg2(arg3)).toString
    }

    def def1(arg: Double): String = {
      arg.toString
    }

    def def2(arg: Double): Double = {
      arg + arg * 1000
    }

    val strHighLevel: String = testHighLevel(def1, def2, 10.23)
    println(strHighLevel)

    // 高阶函数，作为返回值传递
    def testHighLevelReturn(x: Int): Int => Int = {
      y: Int => x + y
    }

    val strHighLevelReturn: Int = testHighLevelReturn(10)(23)
    println(strHighLevelReturn)

    // 高阶函数应用，对集合数组中的元素进行处理
    val testArr: Array[Int] = Array(12, 45, 75, 98)

    def arrayOperation(arr: Array[Int], op: Int => Int): Array[Int] = {
      for (i <- arr) yield op(i)
    }

    def addOne(a: Int): Int = {
      a + 1
    }

    println(arrayOperation(testArr, addOne).mkString(","))


    // 柯里化和闭包
    def addCurry(a: Int)(b: Int): Int = {
      a + b
    }

    println("柯里化：" + addCurry(10)(23))

    // 递归
    def defMyself(n: Int): Int = {
      if (n == 1) {
        return 0
      }
      if (n == 2) {
        return 1
      }
      defMyself(n - 1) + defMyself(n - 2)
    }

    println("递归:" + defMyself(15))

    // 尾递归
    def defMyselfDef(n: Int): Int = {
      @tailrec
      def fibonacci(n: Int, current: Int, next: Int): Int = {
        if (n == 1) {
          current
        } else if (n == 2) {
          next
        } else {
          fibonacci(n - 1, next, current + next)
        }
      }

      fibonacci(n, 0, 1)
    }

    println("尾递归:" + defMyselfDef(15))

    // 控制抽象实现自定义while
    // 首先是测试常规while循环
    var testN = 10;
    println("---------------------")
    while (testN >= 1) {
      println(testN)
      testN -= 1
    }

    // 自定义while循环
    // 用闭包实现一个函数，将代码块作为参数传入，递归调用
    def myWhile(condition: => Boolean): (=> Unit) => Unit = {
      // 内层函数递归调用，参数就是循环体，不需要返参
      def doLoop(operation: => Unit): Unit = {
        if (condition) {
          operation
          myWhile(condition)(operation)
        }
      }
      // 返回函数
      doLoop _
    }
    // 测试自定义while循环
    testN = 6
    println("---------------------")
    myWhile(testN >= 1)({
      println(testN)
      testN -= 1
    })

    // 用匿名函数简化自定义while循环
    def myWhileSimple(condition: => Boolean): (=> Unit) => Unit = {
      doLoop => {
        if (condition) {
          doLoop
          myWhileSimple(condition)(doLoop)
        }
      }
    }
    // 测试用匿名函数简化自定义while循环
    testN = 8
    println("---------------------")
    myWhileSimple(testN >= 1)({
      println(testN)
      testN -= 1
    })

    // 用柯里化简化自定义while循环
    def myWhileCurry(condition: => Boolean)(doLoop: => Unit): Unit = {
      if (condition) {
        doLoop
        myWhileCurry(condition)(doLoop)
      }
    }
    // 测试用柯里化简化自定义while循环
    testN = 3
    println("---------------------")
    myWhileCurry(testN >= 1)({
      println(testN)
      testN -= 1
    })

    lazy val result: Int = sum(10, 23)
    println("1. 函数调用")
    println("2. result = " + result)
    println("4. result = " + result)

    def sum(a: Int, b: Int): Int = {
      println("3. sum调用！")
      a + b
    }
  }
}
