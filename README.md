# XLogger

A non-intrusive logging framework implemented using AspectJ. 

This API is implemented to minimize the need to customize or update the existing code base.

## Usage

Extends the abstract aspect org.beanone.xlogger.AbstractMethodLogger and in the extension, defines the around advice that invokes the handle() for method logging and afterThrowing advice that invokes the handleThrow() methods for method exception logging. 

By default, method logging level is trace and method exception logging level is error.

The handle() method logs two messages, one before the method invocation and includes all the arguments, the other after the method invocation and includes the result. The ArgumentSpecRegistry registers ArgumentSpec for each class type. And ArgumentSpec defines how to render a class type when it is rendered in a logging message.

An ArgumentSpecRegistry can register more than one way to render an argument type. To do that, simply register to a different registry partition.

The default logging behavior can be customized by annotating the method with LoggerSpec annotation or through configuration. For usage of the annotation LoggerSpec, see the javadoc for the annotation. For usage of the configuration, see the below section.

### Usage of configuration

