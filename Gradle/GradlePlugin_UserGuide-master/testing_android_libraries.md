# 测试Android库工程
测试Android库工程和普通应用工程的方法类似。

唯一的不同在于整个库(包含它的依赖)都是作为依赖库被自动添加到测试应用中的。结果就是测试APK不仅仅包含了自己的代码，还包含了自己的库和它所有的依赖。
库的manifest被合并到测试应用manifest文件中(作为
一些项目引用这个库的壳)。

**<font color='green'>androidTest </font>** task只是安装(或者卸载)测试APK(因为没有其他APK被安装)

其它的都是普通工程类似的。


