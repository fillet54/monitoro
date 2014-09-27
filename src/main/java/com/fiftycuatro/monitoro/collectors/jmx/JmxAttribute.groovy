package com.fiftycuatro.monitoro.collectors.jmx

import groovy.transform.EqualsAndHashCode;

@EqualsAndHashCode
class JmxAttribute {
	String objectName;
	String attributeName;
}
