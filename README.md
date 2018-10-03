# javaxyq-im

#### 项目介绍

原项目在github上：https://github.com/kylixs/javaxyq。使用ant打包、并且没有使用步骤，所以我就自己整理出一个maven版的。
现已可运行。

#### 运行步骤
1. 安装依赖！在根目录下执行`mvn clean install -DskipTests`
2. 打包！再`cd game`后执行`mvn clean package -DskipTests assembly:single`
3. 玩！去game/target/dist/game-1.5下双击javaxyq.bat即可


#### 运行resource-manage & UIMaker
- 运行com.javaxyq.tools.ResourceManager即可（请全局搜索类名）
- 运行com.javaxyq.tools.UIMaker即可（请全局搜索类名）

2018-09-30
