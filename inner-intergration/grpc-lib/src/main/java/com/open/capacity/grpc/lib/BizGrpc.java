package com.open.capacity.grpc.lib;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.15.1)",
    comments = "Source: biz.proto")
public final class BizGrpc {

  private BizGrpc() {}

  public static final String SERVICE_NAME = "Biz";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.open.capacity.grpc.lib.BizOuterClass.GprcRequest,
      com.open.capacity.grpc.lib.BizOuterClass.GprcReply> getProcessMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Process",
      requestType = com.open.capacity.grpc.lib.BizOuterClass.GprcRequest.class,
      responseType = com.open.capacity.grpc.lib.BizOuterClass.GprcReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.open.capacity.grpc.lib.BizOuterClass.GprcRequest,
      com.open.capacity.grpc.lib.BizOuterClass.GprcReply> getProcessMethod() {
    io.grpc.MethodDescriptor<com.open.capacity.grpc.lib.BizOuterClass.GprcRequest, com.open.capacity.grpc.lib.BizOuterClass.GprcReply> getProcessMethod;
    if ((getProcessMethod = BizGrpc.getProcessMethod) == null) {
      synchronized (BizGrpc.class) {
        if ((getProcessMethod = BizGrpc.getProcessMethod) == null) {
          BizGrpc.getProcessMethod = getProcessMethod = 
              io.grpc.MethodDescriptor.<com.open.capacity.grpc.lib.BizOuterClass.GprcRequest, com.open.capacity.grpc.lib.BizOuterClass.GprcReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "Biz", "Process"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.open.capacity.grpc.lib.BizOuterClass.GprcRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.open.capacity.grpc.lib.BizOuterClass.GprcReply.getDefaultInstance()))
                  .setSchemaDescriptor(new BizMethodDescriptorSupplier("Process"))
                  .build();
          }
        }
     }
     return getProcessMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static BizStub newStub(io.grpc.Channel channel) {
    return new BizStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static BizBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new BizBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static BizFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new BizFutureStub(channel);
  }

  /**
   */
  public static abstract class BizImplBase implements io.grpc.BindableService {

    /**
     */
    public void process(com.open.capacity.grpc.lib.BizOuterClass.GprcRequest request,
        io.grpc.stub.StreamObserver<com.open.capacity.grpc.lib.BizOuterClass.GprcReply> responseObserver) {
      asyncUnimplementedUnaryCall(getProcessMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getProcessMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.open.capacity.grpc.lib.BizOuterClass.GprcRequest,
                com.open.capacity.grpc.lib.BizOuterClass.GprcReply>(
                  this, METHODID_PROCESS)))
          .build();
    }
  }

  /**
   */
  public static final class BizStub extends io.grpc.stub.AbstractStub<BizStub> {
    private BizStub(io.grpc.Channel channel) {
      super(channel);
    }

    private BizStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected BizStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new BizStub(channel, callOptions);
    }

    /**
     */
    public void process(com.open.capacity.grpc.lib.BizOuterClass.GprcRequest request,
        io.grpc.stub.StreamObserver<com.open.capacity.grpc.lib.BizOuterClass.GprcReply> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getProcessMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class BizBlockingStub extends io.grpc.stub.AbstractStub<BizBlockingStub> {
    private BizBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private BizBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected BizBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new BizBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.open.capacity.grpc.lib.BizOuterClass.GprcReply process(com.open.capacity.grpc.lib.BizOuterClass.GprcRequest request) {
      return blockingUnaryCall(
          getChannel(), getProcessMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class BizFutureStub extends io.grpc.stub.AbstractStub<BizFutureStub> {
    private BizFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private BizFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected BizFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new BizFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.open.capacity.grpc.lib.BizOuterClass.GprcReply> process(
        com.open.capacity.grpc.lib.BizOuterClass.GprcRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getProcessMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_PROCESS = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final BizImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(BizImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_PROCESS:
          serviceImpl.process((com.open.capacity.grpc.lib.BizOuterClass.GprcRequest) request,
              (io.grpc.stub.StreamObserver<com.open.capacity.grpc.lib.BizOuterClass.GprcReply>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class BizBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    BizBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.open.capacity.grpc.lib.BizOuterClass.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("Biz");
    }
  }

  private static final class BizFileDescriptorSupplier
      extends BizBaseDescriptorSupplier {
    BizFileDescriptorSupplier() {}
  }

  private static final class BizMethodDescriptorSupplier
      extends BizBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    BizMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (BizGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new BizFileDescriptorSupplier())
              .addMethod(getProcessMethod())
              .build();
        }
      }
    }
    return result;
  }
}
