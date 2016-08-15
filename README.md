# XLogger

A non-intrusive logging framework implemented using AspectJ. 

The goal of this API is to remove the need to add any logging statement into your application with minimum configuration.

## Usage

Extends the abstract aspect org.beanone.xlogger.AbstractMethodLogger and in the extension, defines the around advice that invokes the handle() method for method logging and afterThrowing advice that invokes the handleThrow() methods for method exception logging. 

The handle() method logs two messages, one before the method invocation and includes all the arguments, the other after the method invocation and includes the result. The ArgumentSpecRegistry can be invoked to register ArgumentSpec for each method argument class type. And ArgumentSpec defines how to render a class type when it is rendered in a logging message.

An ArgumentSpecRegistry can register more than one way to render an argument type. To do that, simply register to a different registry partition. For example, you can define a verbose partition and register some argument types in that partition. Besides the default partition, all other partitions must be initialized before use.

The default logging behavior can be customized by annotating the method with LoggerSpec annotation and combining the default LoggerLevel with different advice expressions in the aspect implementation. For exceptions, the logger level for each type of exception can be easily customized. Refer to the MockMethodLogger as a reference implementation. 
