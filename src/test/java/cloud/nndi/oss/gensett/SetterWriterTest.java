package cloud.nndi.oss.gensett;

import org.junit.Assert;
import org.junit.Test;

public class SetterWriterTest {
    @Test
    public void testSetterOnSingleFieldWithoutSpaces() throws Exception {
        String expr = "//set:person:firstName";
        String expected = "person.setFirstName(/* TODO: set it! */);\n";

        SetterWriter sw = new SetterWriter(expr);
        Assert.assertEquals(expected, sw.generateCode());

        expr = "//setf:person:firstName,lastName";
        expected = "person.firstName = /* TODO: set it! */;\n" +
                "person.lastName = /* TODO: set it! */;\n";

        sw = new SetterWriter(expr);
        Assert.assertEquals(expected, sw.generateCode());
    }

    @Test
    public void testChainedSetters() throws Exception {
        String expr = "//setc:p:firstName,lastName";
        String expected = "p.setFirstName(/* TODO: set it! */).setLastName(/* TODO: set it! */);";

        SetterWriter sw = new SetterWriter(expr);
        Assert.assertEquals(expected, sw.generateCode());
    }

    @Test
    public void testSetterOnObjectWithOneLetter() throws Exception {
        String expr = "//set:p:firstName";
        String expected = "p.setFirstName(/* TODO: set it! */);\n";

        SetterWriter sw = new SetterWriter(expr);
        Assert.assertEquals(expected, sw.generateCode());

        expr = "//setf:p:firstName,lastName";
        expected = "p.firstName = /* TODO: set it! */;\n" +
                "p.lastName = /* TODO: set it! */;\n";

        sw = new SetterWriter(expr);
        Assert.assertEquals(expected, sw.generateCode());
    }

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
