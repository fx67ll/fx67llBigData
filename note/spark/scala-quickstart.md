# Scala快速入门 🕹️0.1.0

.scala -> .class -> jvm
scala同样可以直接调用java类库

jdk1.8 -> scala2.12
spark3 依赖 scala2.12

[官方下载](https://www.scala-lang.org/download/2.12.11.html)  
[安装教程](https://www.runoob.com/scala/scala-install.html)  
安装和JDK基本一致，非常简单快速  

单例模式是指：保证在整个的软件系统中，某个类只能存在一个对象实例
Scala中没有静态的概念，所以为了实现Java中单例模式的功能，可以直接采用类对象(即伴生对象)方式构建单例对象
1. 懒汉式：创建一个实例前要先判断是否为空，若为空则创建，不为空则直接返回
2. 饿汉式：不管实例是否为空，直接返回

object 声明一个单例对象（伴生对象）
main 方法，外部可以直接调用执行的方法，在class里可以访问伴生对象的方法与属性  
def 方法名称(参数名称: 参数类型): 方法返回值类型 = { 方法体 }  

```
单行注释  //
多行注释  /* */
文档注释  /**
		   * 
		   */
```

不需要分号分割
运算符两边习惯性空格
一行最好不超过80个字符


Scala标识符命名规范
1. 以字母或者下划线开头，后接字母、数字、下划线  
2. 以操作符开头，且只包含操作符（+ - * / # !等）  
3. 用反引号``包括的任意字符串，即使是 Scala 关键字（39 个）也可以  

var 变量
val 常量
1. 在声明变量时，类型可以省略（编译器会自动类型推到）  
2. 类型确定后，就不能修改，Scala是强类型语言  
3. 在声明变量时，必须由初始值  

Scala定义字符串的方式
1. 使用双引号 `val/var 变量名 = “字符串”`  
2. 使用插值表达式 `val/var 变量名 = s"${变量/表达式}字符串"`  
3. 使用三引号，可以应用写sql 
	```
	val/var 变量名 = """字符串1
	字符串2"""
	```
	
格式化字符串模板
```
val testNum = 2.3456

// 表示输出变量
println(s"The testNum is ${testNum}")

// f"${变量}%2.2f" 2.2表示各位整个数长度2不够则补空格，小数位表示保留2位小数字
println(f"The testNum is ${testNum}%2.2f")

// raw 表示不使用格式化模板字符串原样输出
println(raw"The testNum is ${testNum}%2.2f")
```

Java基本类型：char、byte、short、int、long、float、double、boolean  
Java基本类型包装类：Character、Byte、Short、Integer、Long、Float、Double、Boolean
Java引用类型：对象类型
因为Java有基本数据类型，而非全部都是包装类型和引用类型，所以Java语言并不是真正意思的面向对象
Java中基本类型和引用类型没有共同的祖先  

Scala中的数据类型
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

Scala数据类型与Java的区别
1. Scala中所有的类型都使用大写字母开头
2. 整形使用Int而不是Integer
3. Scala中定义变量可以不写类型，让Scala编译器自动推断
4. Scala中默认的整型是Int，默认的浮点型是: Double

Scala数据类型层次
+ Any		所有类型的父类，它有两个子类`AnyRef`与`AnyVal`  
+ AnyVal	所有数值类型的父类  
+ AnyRef	所有对象类型（引用类型）的父类  
+ Unit		表示空，`Unit`是`AnyVal`的子类，它只有一个的实例，写成`()`，它类似于Java中的`void`，但Scala要比Java更加面向对象，`void`是关键字，`Unit`是数据类型  
+ Null		`Null`是`AnyRef`的子类，也就是说它是所有引用类型的子类，它也是只有一个实例值`null`，它可以将`null`赋值给任何对象类型  
+ Nothing	所有类型的子类，能直接创建该类型实例，某个方法抛出异常时，返回的就是`Nothing`类型，因为`Nothing`是所有类的子类，那么它可以赋值为任何类型  

自动类型转换：范围小的数据类型和范围大的数据类型进行计算的时候，范围小的数据类型值会自动转换为大的数据类型值这过程叫自动类型换行  
强制类型转换：范围小的数据类型和范围大的数据类型进行计算的时候，将范围大的数据类型通过一定的格式将其转换为小的数据类型，这个过程就叫做强制类型转换  
`val/var 变量名:数据类型 = 具体的值.toXxx  // Xxx表示你要转换到的数据类型`
值类型和String类型之间的相互转换 
`toString`和`toXxx`  

键盘输入
使用`scala.io.StdIn`包
`StdIn.readLine()、StdIn.readShort()、StdIn.readDouble()`

从文件读取数据
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

将数据写入文件
可以直接使用java下的工具即可

[Scala 运算符](https://www.runoob.com/scala/scala-operators.html)  
[Scala IF...ELSE 语句](https://www.runoob.com/scala/scala-if-else.html)  

for循环 `for(i <- 表达式/数组/集合) { 表达式 }`
直接拿`Array`、`List`、`Set`
嵌套循环 `for(i <- 表达式/数组/集合; i <- 表达式/数组/集合) { 表达式 }`
循环守卫 `for(i <- 表达式/数组/集合 if 表达式) { 表达式 }`
循环步长 `for(i <- 表达式/数组/集合 by 表达式) { 表达式 }`
for推导式（返回值），在for循环体中，可以使用`yield`表达式构建出一个集合，我们把使用`yield`的for表达式称之为推导式，也就是将遍历过程过会给一个新集合中，开发中很少用  

while循环
do while循环
不推荐使用，for循环都可以替代，而且需要在外部定义变量，导致循环内部变量影响外部变量，大数据环境下并行计算不可以内部变量影响外部变量  

循环中断
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
	
函数式编程，重点中的重点