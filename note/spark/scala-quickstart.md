# Scala快速入门 🕹️0.1.1

*关于本文阅读之前的提示*  
**需要您有一定的Java、Python、JavaScript基础，本文就是为了帮助您快速过一遍Scala中的主要语法和知识点**

## 基本介绍
### 编译调用
.scala -> .class -> jvm
scala同样可以直接调用java类库

### 常用版本
jdk1.8 -> scala2.12
spark3 依赖 scala2.12

### 下载安装
[官方下载](https://www.scala-lang.org/download/2.12.11.html)  
[安装教程](https://www.runoob.com/scala/scala-install.html)  
安装和JDK基本一致，非常简单快速  


## 单例模式
单例模式是指：保证在整个的软件系统中，某个类只能存在一个对象实例  
Scala中没有静态的概念，所以为了实现Java中单例模式的功能，可以直接采用类对象(即伴生对象)方式构建单例对象  
1. 懒汉式：创建一个实例前要先判断是否为空，若为空则创建，不为空则直接返回  
2. 饿汉式：不管实例是否为空，直接返回  

伴生类与伴生对象  
同一个文件中同名的`object`和`class`，可以相互访问彼此的私有变量（但object中若使用`private this`标注，则伴生类中无法访问）  
伴生对象中可定义`apply`方法，目的是通过伴生类的构造函数功能，来实现伴生对象的构造函数功能  

object 声明一个单例对象（伴生对象）  
main 方法，外部可以直接调用执行的方法，在class里可以访问伴生对象的方法与属性  
`def 方法名称(参数名称: 参数类型): 方法返回值类型 = { 方法体 }`    


## 语法规则
### 注释
与Java基本一致
```
单行注释  //
多行注释  /* */
文档注释  /**
		   * 
		   */
```

### 语法习惯
不需要分号分割  
运算符两边习惯性空格  
一行最好不超过80个字符  

### 标识符命名规范  
1. 以字母或者下划线开头，后接字母、数字、下划线  
2. 以操作符开头，且只包含操作符（+ - * / # !等）  
3. 用反引号``包括的任意字符串，即使是 Scala 关键字（39 个）也可以  

### 变量
var 变量  
val 常量  
1. 在声明变量时，类型可以省略（编译器会自动类型推到）  
2. 类型确定后，就不能修改，Scala是强类型语言  
3. 在声明变量时，必须由初始值  

### 字符串
#### Scala定义字符串的方式  
1. 使用双引号 `val/var 变量名 = "字符串"`    
2. 使用插值表达式 `val/var 变量名 = s"${变量/表达式}字符串"`  
3. 使用三引号，可以应用写sql  
	```
	val/var 变量名 = """字符串1
	字符串2"""
	```
#### 格式化字符串模板  
```
val testNum = 2.3456

// 表示输出变量
println(s"The testNum is ${testNum}")

// f"${变量}%2.2f" 2.2表示各位整个数长度2不够则补空格，小数位表示保留2位小数字
println(f"The testNum is ${testNum}%2.2f")

// raw 表示不使用格式化模板字符串原样输出
println(raw"The testNum is ${testNum}%2.2f")
```

### 数据类型
#### Java中的数据类型  
Java基本类型：char、byte、short、int、long、float、double、boolean  
Java基本类型包装类：Character、Byte、Short、Integer、Long、Float、Double、Boolean  
Java引用类型：对象类型  
因为Java有基本数据类型，而非全部都是包装类型和引用类型，所以Java语言并不是真正意思的面向对象  
Java中基本类型和引用类型没有共同的祖先  
#### Scala中的数据类型  
+ Scala和Java都有相同的数据类型，在Scala中数据类型都是对象，也就是说Scala没有原生的数据类型  
+ Scala中数据类型分为: `AnyVal(值类型)`和`AnyRef(引用类型)`，不管是`AnyVal`还是`AnyRef`都是对象  
+ 相对于Java的类型系统，Scala要更复杂一点  
	> Byte		8位带符号整数  
	> Short		16位带符号整数  
	> Int		32位带符号整数  
	> Long		64位带符号整数  
	> Char		16位无符号Unicode字符  
	> String	Char类型的序列（字符串），StringOps是Java中的String增强  
	> Float		32位单精度浮点数  
	> Double	64位双精度浮点数  
	> Boolean	true或false  
#### Scala中的数据类型与Java的区别  
1. Scala中所有的类型都使用大写字母开头  
2. 整形使用`Int`而不是`Integer`  
3. Scala中定义变量可以不写类型，让Scala编译器自动推断  
4. Scala中默认的整型是`Int`，默认的浮点型是: `Double`  
#### Scala中的数据类型层次  
+ Any		所有类型的父类，它有两个子类`AnyRef`与`AnyVal`  
+ AnyVal	所有数值类型的父类  
+ AnyRef	所有对象类型（引用类型）的父类  
+ Unit		表示空，`Unit`是`AnyVal`的子类，它只有一个的实例，写成`()`，它类似于Java中的`void`，但Scala要比Java更加面向对象，`void`是关键字，`Unit`是数据类型  
+ Null		`Null`是`AnyRef`的子类，也就是说它是所有引用类型的子类，它也是只有一个实例值`null`，它可以将`null`赋值给任何对象类型  
+ Nothing	所有类型的子类，能直接创建该类型实例，某个方法抛出异常时，返回的就是`Nothing`类型，因为`Nothing`是所有类的子类，那么它可以赋值为任何类型  
#### Scala中的类型转换
自动类型转换：范围小的数据类型和范围大的数据类型进行计算的时候，范围小的数据类型值会自动转换为大的数据类型值这过程叫自动类型换行  
强制类型转换：范围小的数据类型和范围大的数据类型进行计算的时候，将范围大的数据类型通过一定的格式将其转换为小的数据类型，这个过程就叫做强制类型转换  
`val/var 变量名:数据类型 = 具体的值.toXxx  // Xxx表示你要转换到的数据类型`  
值类型和String类型之间的相互转换 
`toString`和`toXxx`  


## 键盘输入、文件读取和写入
### 键盘输入  
使用`scala.io.StdIn`包  
`StdIn.readLine()、StdIn.readShort()、StdIn.readDouble()`  

### 从文件读取数据  
使用`scala.io.Source`包  
1. 按行读取  
	```
	// 1.创建Source对象，关联数据源文件
	val source = Source.fromFile("./dataTest01.txt")

	// 2.以行为单位，来读取数据
	val lines:Iterator[String] = source.getLines()

	// 3.将读取到的数据封装到List列表中
	val list:List[String] = lines.toList

	// 4.打印结果
	for(data <- list ) println(data)

	// 5.关闭Source对象
	source.close()
	```
2. 按字符串读取  
	```
	//1.创建source对象
	val source = Source.fromFile("./dataTest02.txt")
	val iter:BufferedIterator[Char] = source.buffered
	while(iter.hasNext){
	  print(iter.next())
	}
	source.close()
	```
3. 读取词法单元和数字  
	```
	val source = Source.fromFile("./dataTest03.txt")
	val str = source.mkString
	//3. \s表示空白字符，即空格，\t等
	val strArray:Array[String] = str.split("\\s+")
	val intArray:Array[Int] = strArray.map(_.toInt)
	for(num <- intArray) println(num + 1)
	source.close()
	```

### 将数据写入文件  
可以直接使用java下的工具即可  


## 运算符
### 逻辑运算符
[Scala 运算符](https://www.runoob.com/scala/scala-operators.html)  
[Scala IF...ELSE 语句](https://www.runoob.com/scala/scala-if-else.html)  

### 循环
#### for循环
1. for循环 `for(i <- 表达式/数组/集合) { 表达式 }`  
2. 直接拿`Array`、`List`、`Set`  
	```
	// 直接拿`Array`、`List`、`Set`
	for (i <- Array(1, 3, 5)) println(s"${i}")
	for (i <- List(11, 23, 35)) println(s"${i}")
	for (i <- Set(15, 43, 65)) println(s"${i}")
	```
3. 嵌套循环 `for(i <- 表达式/数组/集合; i <- 表达式/数组/集合) { 表达式 }`  
4. 循环守卫 `for(i <- 表达式/数组/集合 if 表达式) { 表达式 }`  
5. 循环步长 `for(i <- 表达式/数组/集合 by 表达式) { 表达式 }`  
6. for推导式（返回值），在for循环体中，可以使用`yield`表达式构建出一个集合，我们把使用`yield`的for表达式称之为推导式，也就是将遍历过程过会给一个新集合中  
	```
	// for推导式
	val v = for (i <- 1 to 10) yield i * 10
	```
#### while和do while循环
while循环  
do while循环  
不推荐使用，for循环都可以替代，而且需要在外部定义变量，导致循环内部变量影响外部变量，大数据环境下并行计算不可以内部变量影响外部变量  
#### 循环中断  
1. 采用异常的方式来退出循环  
	```
	try {
	  for(i<-1 to 10){
		println(i)
		if(i==8)
		  throw new RuntimeException
	  }
	} catch {
	  case exception: Exception=>
	}
	```
2. 采用Scala自带的函数，退出循环  
	```
	// 方式一
	import scala.util.control.Breaks
	Breaks.breakable(
	  for(i<-1 to 10){
		println(i)
		if(i==8)
		  Breaks.break()
	  }
	)
	// 方式二
	import scala.util.control.Breaks._
	breakable(
	  for(i<-1 to 10){
		println(i)
		if(i==8)
		  break
	  }
	)
	// 方式三
	val loop = new Breaks
	loop.breakable{
	  for(i<-1 to 10)
		{
		  if(i==8)
			loop.break()
		  println(i)
		}
	}
	```

## 函数式编程
面向对象编程，解决问题，分解对象行为属性  
函数式编程，完成某一功能的程序语句集合，成为函数，类中的函数成为方法  

### 传参与返回值
1. 可变参数，如果有多个参数，可变参数一般放置在最后  
	```
	def f1(str1: String, str2: String*): Unit = {
	  println(str1, str2)
	}
	f1("aaa", "bbb", "ccc")

	(aaa,WrappedArray(bbb, ccc))
	```
2. 参数默认值，有默认值的参数一般放在参数列表后面  
	```
	def f2(str1: String, str2: String = "fx67ll"): Unit = {
	  println(str1, str2)
	}
	f2("ll76xf")

	(ll76xf,fx67ll)
	```
3. 带名参数，如果函数的多个参数都指定了默认值，调用时传递的参数，到底是覆盖哪个默认参数就不能确定了，这个时候可以采用带名参数  
	```
	// 带名参数就是传参时候带上参数名，继续使用上面的f2函数
	f2(str2 = "ll76xf-1", str1 = "ll76xf-2")
	```
4. 如果明确函数无返回值或不确定返回值类型，那么返回值类型可以省略，或声明为`Any`类型  

### 函数至简原则  
1. `return` 可以省略，Scala 会使用函数体的最后一行代码作为返回值  
2. 如果函数体只有一行代码，可以省略`花括号`  
3. 返回值类型如果能够推断出来，那么可以省略（:和返回值类型一起省略）  
4. 如果有 `return`，则不能省略返回值类型，必须指定  
5. 如果函数明确声明 `unit`，那么即使函数体中使用 `return` 关键字也不起作用  
6. Scala 如果期望是无返回值类型，可以省略等号  
7. 如果函数无参，但是声明了参数列表，那么调用时，`()小括号`，可加可不加  
8. 如果函数没有参数列表，那么`()小括号`可以省略，调用时`()小括号`必须省略  
9. 如果不关心名称，只关心逻辑处理，那么`函数名（def）`可以省略

### 匿名函数
`lambda`表达式，主要用于实现高阶应用，直接类比参考js即可  
```
val triple = (x: Double) => 3 * x 
println(triple(3))
```
#### 匿名函数至简原则  
1. 参数的类型可以省略，会根据形参进行自动的推导  
2. 类型省略之后，发现只有一个参数，则圆括号可以省略；其他情况：没有参数和参数超过 1 的永远不能省略圆括号  
3. 匿名函数如果只有一行，则大括号也可以省略  
4. 如果参数只出现一次，则参数省略且后面参数可以用_代替  
	```
	println(f2((x: Int, y: Int) => {
	  x + y
	}))
	println(f2((x: Int, y: Int) => x + y))
	println(f2((x, y) => x + y))
	println(f2(_ + _))
	println(f2(_ - _)) //x-y
	println(f2(-_ + _)) //y-x=-x+y
	```
5. 如果可以推断出当前传入的println是一个函数体而不是调用语句，可以直接省略下划线  

### 高阶函数  
1. 函数作为值进行传递  
2. 函数作为参数进行传递  
3. 函数作为返回值返回  
#### 高阶函数示例  
```
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
```
#### 高阶函数简单应用  
```
// 对集合数组中的元素进行处理
val testArr: Array[Int] = Array(12, 45, 75, 98)
def arrayOperation(arr: Array[Int], op: Int => Int): Array[Int] = {
  for (i <- arr) yield op(i)
}
def addOne(a: Int): Int = {
  a + 1
}
println(arrayOperation(testArr, addOne).mkString(","))
```
#### 常用高阶函数
其实我们在工作中自己定义高阶函数的场景不多，大部分场景都是去使用已有的高阶函数，下面是几个常见的高阶函数：
1. `map`：对传入的每个元素都进行处理，返回一个元素  
2. `flatMap`：对传入的每个元素都进行处理，返回一个或者多个元素  
3. `foreach`：对传入的每个元素都进行处理，但是没有返回值  
4. `filter`：对传入的每个元素都进行条件判断，如果返回`true`，则保留该元素，否则过滤掉该元素  
5. `reduceLeft`：从左侧元素开始，进行`reduce`操作  

### 柯里化和闭包
柯里化：把一个参数列表的参数变成多个参数列表  
闭包：如果一个函数访问了他的外部（局部变量）的值，那么这个函数和他所处的环境，称为闭包  
函数柯里化，其实就是将复杂的参数逻辑变得简单化，函数柯里化一定存在闭包  
```
def addByFour(): Int => Int = {
  val a = 4
  def addB(b: Int): Int = {
	a + b
  }
  addB
}
// 在调用时，addByFour函数执行完毕后，局部变量a应该随着栈空间释放掉
val f = addByFour()
// 但是在此处，变量a其实并没有释放，而是包含在了f2函数的内部，形成了闭合的效果
println(addByFour(3))
// 4+3
println("结果是：" + addByFour()(3))

def addCurry(a: Int)(b: Int): Int = {
  a + b
}
// 10+23
println("柯里化：" + addCurry(10)(23))
```
#### 闭包在堆栈中的原理
在Scala中，函数也是对象，一个函数会将其依赖的其他函数一起打包成一个对象存在堆空间中  
1. 堆（heap）的线程是共享的  
	+ 先进先出，后进后出，函数和所依赖的函数打包成一个对象存在堆中  
	+ 堆的优势是可以动态地分配内存大小，生存期也不必事先告诉编译器，Java的垃圾收集器会自动收走这些不再使用的数据  
	+ 但缺点是，由于要在运行时动态分配内存，存取速度较慢  
2. 栈（stack）的线程是独享的  
	+ 先进后出，后进先出，每创建或者调用函数，会存进来  
	+ 栈存取速度比堆要快，仅次于直接位于CPU中的寄存器  
	+ 但缺点是，存在栈中的数据大小与生存期必须是确定的  
#### JVM的堆、栈和常量池介绍  
1. 寄存器：最快的存储区，由编译器根据需求进行分配，我们在程序中无法控制  
2. 栈：存放基本类型的变量数据和对象的引用，但对象本身不存放在栈中，而是存放在堆（new出来的对象）或者常量池中（字符串常量对象存放在常量池中）  
3. 堆：存放所有`new`出来的对象  
4. 静态域：存放静态成员（`static`定义的）  
5. 常量池：存放字符串常量和基本类型常量（`public`/`static`/`final`）  
6. 非RAM存储：硬盘等永久存储空间  

### 递归调用  
复杂的递归会对栈（stack）空间资源会造成压力，调用次数过多还会出现栈溢出的问题  
尾递归：递归函数的返回值是递归函数表达式  
尾递归的核心思想：通过参数来传递每一次的调用结果，达到不压栈，它维护着一个迭代器和一个累加器  
其实与循环的思想类似，每次调用此函数（进栈），调用完成不用在栈里面等着，可以直接出栈，调用结果在累加器被记录，不存在栈溢出的情况  
尾递归依赖编译器，需要使用`@tailrec`注释  
```
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
println(defMyself(15))

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
println(defMyselfDef(15))
```

### 控制抽象
控制抽象也是函数的一种，它可以让我们更加灵活的使用函数  
传值参数（call-by-value），当参数通过传值调用函数时，它会在调用函数之前计算一次传入的表达式或参数值  
传名参数（call-by-name），是使用传名调用函数时，在函数内部访问参数时会重新计算传入表达式的值  
可以参考上面高阶函数示例的那个函数，它同时包含了传值参数和传名参数  
```
// 传值参数
def callByValue(x: Int)
// 传名参数
def callByName(x: => Int)
```
#### Scala下划线用法总结  
1. 用于变量初始化，在Java中，可以声明变量而不必给出初始值，在Scala中，变量在声明时必须显示指定，可以使用下划线对变量进行初始化，只适用于成员变量，不适用于局部变量  
2. 用于导包引入，`_`表示导入该包下所有内容，类比Java中的`*`  
3. 用于将方法转变为函数，在Scala中方法不是值，而函数是  
4. 用于模式匹配，模式匹配中可以用下划线来作为Java中`default`的类比使用，也可以在匹配集合类型时，用于代表集合中元素  
5. 用于访问`tuple`元素  
6. 用于简写函数，如果函数的参数在函数体只出现一次，则可以用下划线代替  
7. 定义偏函数，对某个多参数函数进行部分函数调用，没有传入的参数使用_代替，返回结果即为偏函数  
	+ 偏函数被包在花括号内`没有match的一组case语句`是一个偏函数  
	+ 偏函数是`PartialFunction[A, B]`的一个实例，A代表输入参数类型，B代表返回结果类型  
#### 控制抽象以及柯里化应用示例：自定义while循环  
```
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
```

### 惰性加载  
当函数返回值被声明为`lazy`时，函数的执行将被推迟，直到我们首次对此取值，该函数才会执行，这种函数我们称之为惰性函数，这种方式就叫做懒加载  
```
lazy val result: Int = sum(10, 23)
println("1. 函数调用")
println("2. result = " + result)
println("4. result = " + result)

def sum(a: Int, b: Int): Int = {
  println("3. sum调用！")
  a + b
}

1. 函数调用
3. sum调用！
2. result = 25
4. result = 25
```

## 面向对象


我是 [fx67ll.com](https://fx67ll.com)，如果您发现本文有什么错误，欢迎在评论区讨论指正，感谢您的阅读！  
如果您喜欢这篇文章，欢迎访问我的 [本文github仓库地址](https://github.com/fx67ll/fx67llBigData/blob/main/note/spark/scala-quickstart.md)，为我点一颗Star，Thanks~ :)  
***转发请注明参考文章地址，非常感谢！！！***