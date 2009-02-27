package org.springframework.flex.messaging.security;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.security.ConfigAttributeDefinition;
import org.springframework.security.intercept.ObjectDefinitionSource;
import org.springframework.security.intercept.web.DefaultFilterInvocationDefinitionSource;
import org.springframework.security.intercept.web.RequestKey;
import org.springframework.security.util.UrlMatcher;
import org.springframework.util.Assert;

import flex.messaging.endpoints.Endpoint;

/**
 * 
 * Implementation of {@link ObjectDefinitionSource} for BlazeDS {@link Endpoint}s.  
 * 
 * @author Jeremy Grelle
 */

public class EndpointDefinitionSource extends DefaultFilterInvocationDefinitionSource {

	private Map<String, ConfigAttributeDefinition> endpointMap = new HashMap<String, ConfigAttributeDefinition>();

	public EndpointDefinitionSource(UrlMatcher urlMatcher,
			LinkedHashMap<RequestKey, ConfigAttributeDefinition> requestMap) {
		super(urlMatcher, requestMap);
	}
	
	public EndpointDefinitionSource(UrlMatcher urlMatcher,
			LinkedHashMap<RequestKey, ConfigAttributeDefinition> requestMap, HashMap<String, ConfigAttributeDefinition> endpointMap) {
		super(urlMatcher, requestMap);
		Assert.notNull(endpointMap, "endpointMap cannot be null");
		this.endpointMap = endpointMap;
	}

	public ConfigAttributeDefinition getAttributes(Object object)
			throws IllegalArgumentException {
		if ((object == null) || !this.supports(object.getClass())) {
            throw new IllegalArgumentException("Object must be an Endpoint");
        }
		
		Endpoint endpoint = (Endpoint) object;
		ConfigAttributeDefinition attributes = null;
		
		if (endpointMap.containsKey(endpoint.getId())) {
			attributes = endpointMap.get(endpoint.getId());
		} else {
			attributes = lookupAttributes(endpoint.getUrlForClient(), null);
		}
        return attributes;
	}

	@SuppressWarnings("unchecked")
	public boolean supports(Class clazz) {
		return Endpoint.class.isAssignableFrom(clazz);
	}
}