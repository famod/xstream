/*
 * Copyright (C) 2004, 2005 Joe Walnes.
 * Copyright (C) 2006, 2007, 2008, 2013, 2014, 2017, 2018 XStream Committers.
 * All rights reserved.
 *
 * The software in this package is published under the terms of the BSD
 * style license a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 *
 * Created on 09. May 2004 by Joe Walnes
 */
package com.thoughtworks.acceptance;

import com.thoughtworks.acceptance.objects.StandardObject;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.reflection.ObjectAccessException;
import com.thoughtworks.xstream.converters.reflection.PureJavaReflectionProvider;
import com.thoughtworks.xstream.core.JVM;


public class FinalFieldsTest extends AbstractAcceptanceTest {

    static class ThingWithFinalField extends StandardObject {
        private static final long serialVersionUID = 200405L;
        final int number = 9;
    }

    public void testSerializeFinalFieldsIfSupported() {
        xstream = new XStream(JVM.newReflectionProvider());
        setupSecurity(xstream);
        xstream.alias("thing", ThingWithFinalField.class);

        assertBothWays(new ThingWithFinalField(), ""//
            + "<thing>\n"
            + "  <number>9</number>\n"
            + "</thing>");
    }

    public void testExceptionThrownUponSerializationIfNotSupport() {
        xstream = new XStream(new PureJavaReflectionProvider());
        xstream.alias("thing", ThingWithFinalField.class);

        try {
            xstream.toXML(new ThingWithFinalField());
        } catch (final ObjectAccessException expectedException) {
            assertEquals("Invalid final field " + ThingWithFinalField.class.getName() + ".number", expectedException
                .getMessage());
        }
    }
}
