package com.google.assistant.embedded.v1alpha2;

import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;

/**
 * <pre>
 * Service that implements the Google Assistant API.
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.3.0)",
    comments = "Source: google/assistant/embedded/v1alpha2/embedded_assistant.proto")
public final class EmbeddedAssistantGrpc {

  private EmbeddedAssistantGrpc() {}

  public static final String SERVICE_NAME = "google.assistant.embedded.v1alpha2.EmbeddedAssistant";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<com.google.assistant.embedded.v1alpha2.AssistRequest,
      com.google.assistant.embedded.v1alpha2.AssistResponse> METHOD_ASSIST =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING,
          generateFullMethodName(
              "google.assistant.embedded.v1alpha2.EmbeddedAssistant", "Assist"),
          io.grpc.protobuf.ProtoUtils.marshaller(com.google.assistant.embedded.v1alpha2.AssistRequest.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(com.google.assistant.embedded.v1alpha2.AssistResponse.getDefaultInstance()));

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static EmbeddedAssistantStub newStub(io.grpc.Channel channel) {
    return new EmbeddedAssistantStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static EmbeddedAssistantBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new EmbeddedAssistantBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary and streaming output calls on the service
   */
  public static EmbeddedAssistantFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new EmbeddedAssistantFutureStub(channel);
  }

  /**
   * <pre>
   * Service that implements the Google Assistant API.
   * </pre>
   */
  public static abstract class EmbeddedAssistantImplBase implements io.grpc.BindableService {

    /**
     * <pre>
     * Initiates or continues a conversation with the embedded Assistant Service.
     * Each call performs one round-trip, sending an audio request to the service
     * and receiving the audio response. Uses bidirectional streaming to receive
     * results, such as the `END_OF_UTTERANCE` event, while sending audio.
     * A conversation is one or more gRPC connections, each consisting of several
     * streamed requests and responses.
     * For example, the user says *Add to my shopping list* and the Assistant
     * responds *What do you want to add?*. The sequence of streamed requests and
     * responses in the first gRPC message could be:
     * *   AssistRequest.config
     * *   AssistRequest.audio_in
     * *   AssistRequest.audio_in
     * *   AssistRequest.audio_in
     * *   AssistRequest.audio_in
     * *   AssistResponse.event_type.END_OF_UTTERANCE
     * *   AssistResponse.speech_results.transcript "add to my shopping list"
     * *   AssistResponse.dialog_state_out.microphone_mode.DIALOG_FOLLOW_ON
     * *   AssistResponse.audio_out
     * *   AssistResponse.audio_out
     * *   AssistResponse.audio_out
     * The user then says *bagels* and the Assistant responds
     * *OK, I've added bagels to your shopping list*. This is sent as another gRPC
     * connection call to the `Assist` method, again with streamed requests and
     * responses, such as:
     * *   AssistRequest.config
     * *   AssistRequest.audio_in
     * *   AssistRequest.audio_in
     * *   AssistRequest.audio_in
     * *   AssistResponse.event_type.END_OF_UTTERANCE
     * *   AssistResponse.dialog_state_out.microphone_mode.CLOSE_MICROPHONE
     * *   AssistResponse.audio_out
     * *   AssistResponse.audio_out
     * *   AssistResponse.audio_out
     * *   AssistResponse.audio_out
     * Although the precise order of responses is not guaranteed, sequential
     * `AssistResponse.audio_out` messages will always contain sequential portions
     * of audio.
     * </pre>
     */
    public io.grpc.stub.StreamObserver<com.google.assistant.embedded.v1alpha2.AssistRequest> assist(
        io.grpc.stub.StreamObserver<com.google.assistant.embedded.v1alpha2.AssistResponse> responseObserver) {
      return asyncUnimplementedStreamingCall(METHOD_ASSIST, responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            METHOD_ASSIST,
            asyncBidiStreamingCall(
              new MethodHandlers<
                com.google.assistant.embedded.v1alpha2.AssistRequest,
                com.google.assistant.embedded.v1alpha2.AssistResponse>(
                  this, METHODID_ASSIST)))
          .build();
    }
  }

  /**
   * <pre>
   * Service that implements the Google Assistant API.
   * </pre>
   */
  public static final class EmbeddedAssistantStub extends io.grpc.stub.AbstractStub<EmbeddedAssistantStub> {
    private EmbeddedAssistantStub(io.grpc.Channel channel) {
      super(channel);
    }

    private EmbeddedAssistantStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected EmbeddedAssistantStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new EmbeddedAssistantStub(channel, callOptions);
    }

    /**
     * <pre>
     * Initiates or continues a conversation with the embedded Assistant Service.
     * Each call performs one round-trip, sending an audio request to the service
     * and receiving the audio response. Uses bidirectional streaming to receive
     * results, such as the `END_OF_UTTERANCE` event, while sending audio.
     * A conversation is one or more gRPC connections, each consisting of several
     * streamed requests and responses.
     * For example, the user says *Add to my shopping list* and the Assistant
     * responds *What do you want to add?*. The sequence of streamed requests and
     * responses in the first gRPC message could be:
     * *   AssistRequest.config
     * *   AssistRequest.audio_in
     * *   AssistRequest.audio_in
     * *   AssistRequest.audio_in
     * *   AssistRequest.audio_in
     * *   AssistResponse.event_type.END_OF_UTTERANCE
     * *   AssistResponse.speech_results.transcript "add to my shopping list"
     * *   AssistResponse.dialog_state_out.microphone_mode.DIALOG_FOLLOW_ON
     * *   AssistResponse.audio_out
     * *   AssistResponse.audio_out
     * *   AssistResponse.audio_out
     * The user then says *bagels* and the Assistant responds
     * *OK, I've added bagels to your shopping list*. This is sent as another gRPC
     * connection call to the `Assist` method, again with streamed requests and
     * responses, such as:
     * *   AssistRequest.config
     * *   AssistRequest.audio_in
     * *   AssistRequest.audio_in
     * *   AssistRequest.audio_in
     * *   AssistResponse.event_type.END_OF_UTTERANCE
     * *   AssistResponse.dialog_state_out.microphone_mode.CLOSE_MICROPHONE
     * *   AssistResponse.audio_out
     * *   AssistResponse.audio_out
     * *   AssistResponse.audio_out
     * *   AssistResponse.audio_out
     * Although the precise order of responses is not guaranteed, sequential
     * `AssistResponse.audio_out` messages will always contain sequential portions
     * of audio.
     * </pre>
     */
    public io.grpc.stub.StreamObserver<com.google.assistant.embedded.v1alpha2.AssistRequest> assist(
        io.grpc.stub.StreamObserver<com.google.assistant.embedded.v1alpha2.AssistResponse> responseObserver) {
      return asyncBidiStreamingCall(
          getChannel().newCall(METHOD_ASSIST, getCallOptions()), responseObserver);
    }
  }

  /**
   * <pre>
   * Service that implements the Google Assistant API.
   * </pre>
   */
  public static final class EmbeddedAssistantBlockingStub extends io.grpc.stub.AbstractStub<EmbeddedAssistantBlockingStub> {
    private EmbeddedAssistantBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private EmbeddedAssistantBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected EmbeddedAssistantBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new EmbeddedAssistantBlockingStub(channel, callOptions);
    }
  }

  /**
   * <pre>
   * Service that implements the Google Assistant API.
   * </pre>
   */
  public static final class EmbeddedAssistantFutureStub extends io.grpc.stub.AbstractStub<EmbeddedAssistantFutureStub> {
    private EmbeddedAssistantFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private EmbeddedAssistantFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected EmbeddedAssistantFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new EmbeddedAssistantFutureStub(channel, callOptions);
    }
  }

  private static final int METHODID_ASSIST = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final EmbeddedAssistantImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(EmbeddedAssistantImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_ASSIST:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.assist(
              (io.grpc.stub.StreamObserver<com.google.assistant.embedded.v1alpha2.AssistResponse>) responseObserver);
        default:
          throw new AssertionError();
      }
    }
  }

  private static final class EmbeddedAssistantDescriptorSupplier implements io.grpc.protobuf.ProtoFileDescriptorSupplier {
    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.google.assistant.embedded.v1alpha2.AssistantProto.getDescriptor();
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (EmbeddedAssistantGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new EmbeddedAssistantDescriptorSupplier())
              .addMethod(METHOD_ASSIST)
              .build();
        }
      }
    }
    return result;
  }
}
