// package com.itiaoling.metric.boot;
//
// import io.micrometer.core.lang.NonNullApi;
// import org.springframework.context.annotation.ImportSelector;
// import org.springframework.core.type.AnnotationMetadata;
//
// /**
//  * 选择器
//  *
//  * @author gary fu
//  */
// public class ImportMonitorSelector implements ImportSelector {
//     @Override
//     public String[] selectImports(AnnotationMetadata annotationMetadata) {
//         return new String[]{
//                 // "io.micrometer.core.aop.CountedAspect",
//                 // "io.micrometer.core.aop.TimedAspect",
//                 "com.itiaoling.metric.auto.ItiaolingMonitorAutoConfiguration"
//         };
//     }
// }
