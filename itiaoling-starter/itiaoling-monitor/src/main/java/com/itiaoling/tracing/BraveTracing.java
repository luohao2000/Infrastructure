package com.itiaoling.tracing;

import brave.Tracing;
import brave.baggage.BaggageField;
import brave.baggage.BaggagePropagation;
import brave.baggage.BaggagePropagationConfig;
import brave.context.slf4j.MDCScopeDecorator;
import brave.handler.SpanHandler;
import brave.propagation.B3Propagation;
import brave.propagation.ThreadLocalCurrentTraceContext;
import brave.sampler.Sampler;
import io.micrometer.tracing.Tracer;
import io.micrometer.tracing.brave.bridge.BraveBaggageManager;
import io.micrometer.tracing.brave.bridge.BraveCurrentTraceContext;
import io.micrometer.tracing.brave.bridge.BraveTracer;

/**
 * @author charles
 * @since 2023/12/26
 */
public class BraveTracing {

    public void test() {
        // [Brave component] Example of using a SpanHandler. SpanHandler is a component
        // that gets called when a span is finished. Here we have an example of setting it
        // up with sending spans
        // in a Zipkin format to the provided location via the UrlConnectionSender
        // (through the <io.zipkin.reporter2:zipkin-sender-urlconnection> dependency)
        // Another option could be to use a TestSpanHandler for testing purposes.
        SpanHandler spanHandler = SpanHandler.NOOP;

        // [Brave component] CurrentTraceContext is a Brave component that allows you to
        // retrieve the current TraceContext.
        // Example of Brave's automatic MDC setup
        ThreadLocalCurrentTraceContext braveCurrentTraceContext = ThreadLocalCurrentTraceContext.newBuilder()
                .addScopeDecorator(MDCScopeDecorator.get())
                .build();

        // [Micrometer Tracing component] A Micrometer Tracing wrapper for Brave's
        // CurrentTraceContext
        BraveCurrentTraceContext bridgeContext = new BraveCurrentTraceContext(braveCurrentTraceContext);

        // [Brave component] Tracing is the root component that allows to configure the
        // tracer, handlers, context propagation etc.
        Tracing tracing = Tracing.newBuilder()
                .currentTraceContext(braveCurrentTraceContext)
                .supportsJoin(false)
                .traceId128Bit(true)
                // For Baggage to work you need to provide a list of fields to propagate
                .propagationFactory(BaggagePropagation.newFactoryBuilder(B3Propagation.FACTORY)
                        .add(BaggagePropagationConfig.SingleBaggageField.remote(BaggageField.create("from_span_in_scope 1")))
                        .add(BaggagePropagationConfig.SingleBaggageField.remote(BaggageField.create("from_span_in_scope 2")))
                        .add(BaggagePropagationConfig.SingleBaggageField.remote(BaggageField.create("from_span")))
                        .build())
                .sampler(Sampler.ALWAYS_SAMPLE)
                .addSpanHandler(spanHandler)
                .build();


        // [Brave component] Tracer is a component that handles the life-cycle of a span
        brave.Tracer braveTracer = tracing.tracer();

        // [Micrometer Tracing component] A Micrometer Tracing wrapper for Brave's Tracer
        Tracer tracer = new BraveTracer(braveTracer, bridgeContext, new BraveBaggageManager());
    }
}
