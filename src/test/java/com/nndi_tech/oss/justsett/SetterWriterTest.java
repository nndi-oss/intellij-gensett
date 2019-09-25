package com.nndi_tech.oss.justsett;

import org.junit.Assert;
import org.junit.Test;

public class SetterWriterTest {
    @Test
    public void testSetterOnSingleField() throws Exception {
        String expr = "// set:person:firstName";
        String expected = "person.setFirstName(/* TODO: set value here */);\n";

        SetterWriter sw = new SetterWriter(expr);
        Assert.assertEquals(expected, sw.generateCode());
    }

    @Test
    public void testSetterMultipleFields() throws Exception {
        String expr = "// set:person:firstName,lastName";
        String expected = "person.setFirstName(/* TODO: set value here */);\n" +
                "person.setLastName(/* TODO: set value here */);\n";

        SetterWriter sw = new SetterWriter(expr);
        Assert.assertEquals(expected, sw.generateCode());
    }

    @Test
    public void testCanSetMultipleFields() throws Exception {
        String expr = "// setf:person:firstName,lastName";
        String expected = "person.firstName = /* TODO: set value here */;\n" +
                "person.lastName = /* TODO: set value here */;\n";

        SetterWriter sw = new SetterWriter(expr);
        Assert.assertEquals(expected, sw.generateCode());
    }
}
