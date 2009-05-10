/*
 * Copyright 2002-2009 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.flex.security;

import org.springframework.flex.core.MessageInterceptor;
import org.springframework.flex.core.MessageProcessingContext;
import org.springframework.security.Authentication;
import org.springframework.security.context.SecurityContext;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.util.Assert;

import flex.messaging.FlexContext;
import flex.messaging.messages.Message;

/**
 * {@link MessageInterceptor} implementation that handles setup and teardown of the {@link SecurityContext} for the
 * current request when using per-client authentication.
 * 
 * @author Jeremy Grelle
 */
public class PerClientAuthenticationInterceptor implements MessageInterceptor {

    /**
     * 
     * {@inheritDoc}
     */
    public Message postProcess(MessageProcessingContext context, Message inputMessage, Message outputMessage) {
        SecurityContextHolder.clearContext();
        return outputMessage;
    }

    /**
     * 
     * {@inheritDoc}
     */
    public Message preProcess(MessageProcessingContext context, Message inputMessage) {
        if (FlexContext.getUserPrincipal() != null) {
            Assert.isInstanceOf(Authentication.class, FlexContext.getUserPrincipal(), "FlexContext.getUserPrincipal returned an unexpected type.  "
                + "Expected instance of " + Authentication.class.getName() + "but was " + FlexContext.getUserPrincipal().getName());
            SecurityContextHolder.getContext().setAuthentication((Authentication) FlexContext.getUserPrincipal());
        }
        return inputMessage;
    }

}