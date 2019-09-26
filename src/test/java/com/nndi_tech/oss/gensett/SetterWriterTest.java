package com.nndi_tech.oss.gensett;

import org.junit.Assert;
import org.junit.Test;

public class SetterWriterTest {
    @Test
    public void testSetterOnSingleField() throws Exception {
        String expr = "// set:person:firstName";
        String expected = "person.setFirstName(/* TODO: set it! */);\n";

        SetterWriter sw = new SetterWriter(expr);
        Assert.assertEquals(expected, sw.generateCode());
    }

    @Test
    public void testSetterMultipleFields() throws Exception {
        String expr = "// set:person:firstName,lastName";
        String expected = "person.setFirstName(/* TODO: set it! */);\n" +
                "person.setLastName(/* TODO: set it! */);\n";

        SetterWriter sw = new SetterWriter(expr);
        Assert.assertEquals(expected, sw.generateCode());
    }

    @Test
    public void testCanSetMultipleFields() throws Exception {
        String expr = "// setf:person:firstName,lastName";
        String expected = "person.firstName = /* TODO: set it! */;\n" +
                "person.lastName = /* TODO: set it! */;\n";

        SetterWriter sw = new SetterWriter(expr);
        Assert.assertEquals(expected, sw.generateCode());
    }

    @Test
    public void testCanSetWithRequireNonNull() throws Exception {
        String expr = "// setf:person:firstName!,lastName!";
        String expected = "person.firstName = Objects.requireNonNull(/* TODO: set it! */, \"firstName\");\n" +
                "person.lastName = Objects.requireNonNull(/* TODO: set it! */, \"lastName\");\n";

        SetterWriter sw = new SetterWriter(expr);
        Assert.assertEquals(expected, sw.generateCode());
    }
    @Test
    public void testCanSetWithCustomMethodSetter() throws Exception {
        String expr = "// set:person:firstName*checkFirstName,lastName*checkLastName";
        String expected = "person.setFirstName(checkFirstName(/* TODO: set it! */));\n" +
                "person.setLastName(checkLastName(/* TODO: set it! */));\n";

        SetterWriter sw = new SetterWriter(expr);
        Assert.assertEquals(expected, sw.generateCode());
    }

    @Test
    public void testCanSetWithCustomMethod() throws Exception {
        String expr = "// setf:person:firstName*checkFirstName,lastName*checkLastName";
        String expected = "person.firstName = checkFirstName(/* TODO: set it! */);\n" +
                "person.lastName = checkLastName(/* TODO: set it! */);\n";

        SetterWriter sw = new SetterWriter(expr);
        Assert.assertEquals(expected, sw.generateCode());
    }
}
