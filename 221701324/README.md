# InfectStatistic

## 如何运行
1.命令行（win+r cmd）cd 到项目的src下
2.输入命令行 javac -encoding utf-8 xxx.java 编译Java文件
3.输入Java xxx list-参数的命令，运行代码
## 功能简介
对日志文件进行处理，能够通过命令来列出所需要知道的全国和各省在某日的感染情况。
list命令 支持以下命令行参数：
-log 指定日志目录的位置，该项必会附带，请直接使用传入的路径，而不是自己设置路径
-out 指定输出文件路径和文件名，该项必会附带，请直接使用传入的路径，而不是自己设置路径
-date 指定日期，不设置则默认为所提供日志最新的一天。你需要确保你处理了指定日期以及之前的所有log文件
-type 可选择[ip： infection patients 感染患者，sp： suspected patients 疑似患者，cure：治愈 ，dead：死亡患者]，使用缩写选择，如 -type ip 表示只列出感染患者的情况，-type sp cure则会按顺序【sp, cure】列出疑似患者和治愈患者的情况，不指定该项默认会列出所有情况。
-province 指定列出的省，如-province 福建，则只列出福建，-province 全国 浙江则只会列出全国、浙江

## 作业链接
https://edu.cnblogs.com/campus/fzu/2020SPRINGS/homework/10287

## 博客链接
https://www.cnblogs.com/cslj/p/12336799.html