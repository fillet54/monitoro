package com.fiftyfour.collectors.jmx;

import java.io.IOException;
import java.net.MalformedURLException;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

public class JmxService implements JmxAttributeService{
	
	private MBeanServerConnection mbsc;
	
	public JmxService(String jmxUrl) throws IOException {
		JMXServiceURL url = new JMXServiceURL(jmxUrl);
		JMXConnector jmxc = JMXConnectorFactory.connect(url, null);
		mbsc = jmxc.getMBeanServerConnection();
	}
	
	public double asDouble(JmxAttribute attribute) {
		try {
			ObjectName operatingSystem = new ObjectName(attribute.objectName)
			return Double.parseDouble(mbsc.getAttribute(operatingSystem, attribute.attributeName).toString());
		}
		catch (Exception e) {	
		}
		return Double.NaN;
	}
}
