/*
 * Copyright DataStax, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.datastax.oss.quarkus.runtime;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.quarkus.config.CassandraClientConfig;
import io.quarkus.arc.Arc;
import io.quarkus.arc.runtime.BeanContainerListener;
import io.quarkus.runtime.RuntimeValue;
import io.quarkus.runtime.annotations.Recorder;
import javax.enterprise.inject.Default;
import javax.enterprise.util.AnnotationLiteral;

@Recorder
public class CassandraClientRecorder {

  public BeanContainerListener addCassandraClient(
      Class<? extends AbstractCassandraClientProducer> cqlSessionProducerClass) {
    return beanContainer -> beanContainer.instance(cqlSessionProducerClass);
  }

  public void configureRuntimeProperties(CassandraClientConfig config) {
    AbstractCassandraClientProducer producer =
        Arc.container().instance(AbstractCassandraClientProducer.class).get();
    producer.setCassandraClientConfig(config);
  }

  private AnnotationLiteral<Default> defaultName() {
    return Default.Literal.INSTANCE;
  }

  public RuntimeValue<CqlSession> getClient() {
    return new RuntimeValue<>(Arc.container().instance(CqlSession.class, defaultName()).get());
  }
}
