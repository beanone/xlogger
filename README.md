# XLogger

XLogger is based on the idea that any system is consist of a number of nested and/or inter-mingled life cycles. Logging the life cycle events of these life cycles is really most what you need to do in terms of application logging and audit logging. For example, in a typical web application, you would have application life cycle, session life cycle, request life cycle, and data life cycle.

Combining this with an Aspect based implementation, XLogger is created.

XLogger makes it possible to treat logging as an application policy. The outcome is a system that has consistent but configurable and predictable logging behavior. This has numerous benefits. First, it relieves the developers from the chore of writing logging statements. Second, since it records the life cycle events, debugging and troubleshooting is a lot simpler. Third, logging behavior can be controled in a more preditable way.

## Usage

Extends the abstract aspect org.beanone.xlogger.AbstractMethodLogger and in the extension, defines the around advice that invokes the handle() method for method logging and afterThrowing advice that invokes the handleThrow() methods for method exception logging. 

The handle() method logs two messages, one before the method invocation and includes all the arguments, the other after the method invocation and includes the result. The ArgumentSpecRegistry can be invoked to register ArgumentSpec for each method argument class type. And ArgumentSpec defines how to render a class type when it is rendered in a logging message.

An ArgumentSpecRegistry can register more than one way to render an argument type. To do that, simply register to a different registry profile. For example, you can define a verbose profile and register some argument types in that profile. Besides the default profile, all other profiles must be initialized before use.

The default logging behavior can be customized by annotating the method with LoggerSpec annotation and combining the default LoggerLevel with different advice expressions in the aspect implementation. For exceptions, the logger level for each type of exception can be easily customized. Refer to the MockMethodLogger as a reference implementation. 
