package org.sonar.ide.api;

import org.sonar.wsclient.services.Metric;

/**
 * @author Evgeny Mandrikov
 * @since 0.2
 */
public interface IMeasure {

  Metric getMetricDef();

  String getValue();

}
