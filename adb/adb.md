﻿# Adb

截图直接保存到电脑  http://blog.csdn.net/wirelessqa/article/details/29187339

$ adb shell screencap -p | sed 's/\r$//' > screen.png

如果直接当命令用还可以用 alias 包裝装起來：
$ alias and-screencap="adb shell screencap -p | sed 's/\r$//'"

$ and-screencap > screen.png 




[Android]Android的常用adb命令
http://blog.csdn.net/cubuntu/article/details/6727623
http://blog.csdn.net/dull_boy2/article/details/23826649