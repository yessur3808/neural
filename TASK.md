#微服务神经元(Neural)

###概要介绍
微服务神经元

###目标定位
微服务神经元是用于解决分布式环境下的以下两个模块：  
+ 本地服务包装器：用于将本地对外的服务进行包装，可以作为本地服务的保护障  
+ 服务依赖包装器：用于将对外依赖的服务进行虚拟包装，可以作为外调服务的虚拟保障  


###基本开发与设计要求
必须：轻量级依赖、注释、插件隔离式开发、单元测试、文档

1.依赖轻量级  
不要轻易依赖第三方库，如：就为了一个小功能，需要依赖第三方的多个jar，会让项目越来越臃肿，得不偿失。但也不是说完全不能依赖第三方的jar，我们的原则是：能不依赖就不要依赖。如果功能确实很吸引人，那可以酌情考虑。

2.代码命名规范  
这是一个最基本的要求了，但很多开发人员依然我行我素，Neural开发取名必须要求:“合理使用驼峰”、“见名思意”、“简单”、“直接”、“高大上”、“神秘感”...

3.注释
必须要注明:模块注释、功能注释、数据结构注释、参数注释....  
但不代表每一个行都得注释，而是该有的地方必须有注释，要做到注释合理而不泛滥。

4.必须要有单元测试
必须要有单元测试，不然别人怎么玩都不知道(尽可能涵盖每一个功能的测试)。

5.模块插件式开发
要求每一个模块的代码必须遵守模块隔离规则，不能出现这个模块依赖另一个模块的情况。

6.依赖深度
不能出现依赖层级超过5层的情况，一般情况都是3层依赖。  
合理场景:A→B→C	(“→”表示依赖)  
不合理场景:A→B→C→D→E→F  
依赖层级太深，对导致模块拆分、代码拆分特别困难，然而好的设计是不会出现这么多层的依赖，依赖层级太深，只能说明设计不合理。  

7.设计文档
不要求设计文档有多么详细，但是至少能介绍清楚即可。如果设计图画出来都不好看、甚至画不出来，那只能说明，设计不合理或思路都不清楚。

###源码介绍
1.源码模块包介绍：  
cn.ms.neural.common：功能模块  
cn.ms.neural.common.bloomfilter：布隆过滤  
cn.ms.neural.common.concurrent：并发计数  
cn.ms.neural.common.exception：异常中心  
cn.ms.neural.common.logger：日志中心  
cn.ms.neural.common.spi：SPI  
cn.ms.neural.common.utils：工具  

cn.ms.neural.moduler：功能模块中心  
cn.ms.neural.moduler.conf：公共配置中心  
cn.ms.neural.moduler.engine：核心引擎功能(暂不介绍)  
cn.ms.neural.moduler.extension：扩展功能  
cn.ms.neural.moduler.extension.blackwhite：黑白名单  
cn.ms.neural.moduler.extension.degrade：服务降级  
cn.ms.neural.moduler.extension.echosound：回声探测  
cn.ms.neural.moduler.extension.flowrate：流量控制  
cn.ms.neural.moduler.extension.gracestop：优雅停机  
cn.ms.neural.moduler.extension.grayroute：灰色路由  
cn.ms.neural.moduler.extension.idempotent：幂等保障  
cn.ms.neural.moduler.extension.pipescaling：管道缩放  

cn.ms.neural.moduler.senior：高级功能  
cn.ms.neural.type：类型模块  

2.顶级接口/类介绍  
Adaptor.java：适配器  
INotify.java：监听器  
IProcessor.java：处理器  
Neural.java：微服务神经元  
NeuralFactory.java：微服务神经元工厂  

3.模块内结构介绍  
以黑白名单模块为例，其余每个模块都参考该模块的结构,可做适当调整：  
cn.ms.neural.moduler.extension.blackwhite.conf：用于定义该模块的公共配置字段和默认值等信息  
cn.ms.neural.moduler.extension.blackwhite.core：用于实现该模块核心接口的具体实现类  
cn.ms.neural.moduler.extension.blackwhite.entity：该模块的实体对象  
cn.ms.neural.moduler.extension.blackwhite.handler：该模块的支持类或Handle类处理  
cn.ms.neural.moduler.extension.blackwhite.processor：用于定义外调接口的定义 
cn.ms.neural.moduler.extension.blackwhite.type：用于定义各类枚举或类型的定义  
cn.ms.neural.moduler.extension.blackwhite.IBlackWhite.java：用于定义该模块的核心接口  

###分期规划
已完成:泛化引用、泛化实现、优雅停机、黑白名单、管道缩放、超时控制、舱壁隔离、慢性尝试  
第一期:回声探测、流量控制、服务降级、幂等保障、服务容错  
第二期:灰度路由、透明监控、链路追踪  
第三期:容量规划、资源鉴权  

V4.0.0版本功能规划:
返回引用、泛化实现
优雅停机
黑白名单
管道缩放
服务降级
容错内核(熔断拒绝→超时控制→舱壁隔离→服务容错→慢性尝试)


###功能规格(已完成)
泛化引用  
泛化实现  
优雅停机  
黑白名单  
管道缩放  
超时控制  
舱壁隔离  
慢性尝试  

###功能进化(待提升、完善)


###功能规划(待实现)
回声探测:用于探测两个应用之间的通道是否正常，类似于模拟业务请求发送来探测通道是否正常  
流量控制:基于JDK的Semaphore控制并发，基于Ratelimiter控制流速  
服务降级:(已完成80%)  
幂等保障:反复提交来保障服务响应的质量；反复提交对服务的影响都一样  
服务容错:内核，基于Hystrix来实现服务容错  
灰度路由:用于配置灰度发布平台进行请求的灰度路由，灰度发布平台推送灰度规则到每一个节点，实现请求到后按照灰度路由规则进行路由转发  
透明监控:需要实现整个架构的安全数据收集方案  
链路追踪:用于收集一笔交易在各个节点的执行情况，如路由至哪一台机器，失败后怎么处理等的，实现交易的轨迹透明化  
容量规划:根据实时流量来计算当前的容量是否合理，如不合理，计算出相应的数据量  
资源鉴权:实现请求的访问权限控制  

