Just Sett Plugin for IntelliJ IDEA 
===

If you sometimes find yourself writing (not implementing!) lots of setter method calls and 
assignment statements and find it boring and time consuming, this IntelliJ IDEA plugin may be just for you!

## What does it do?

It expands shorthand for setter method calls or assignment statements, so you 
don't have to write them manually.

For example:

```java
Person person = new Person();
// set:person:firstName,age 

// ^ press `Ctrl + Alt + ;` on the line above to get the output below ^

person.setFirstName(/* TODO: set value here */);
person.setAge(/* TODO: set value here */);
```

Trigger it with *Ctrl + Alt + ;* or `Generate > Just Sett` when the plugin is 
installed.

> *NOTE:* 
> This plugin is a Work In Progress, and a stable version has not been published yet,
> it's quality is questionable but it works :)

Get the alpha jar [here](https://github.com/nndi-oss/intellij-just-sett/releases)

## The Shorthand

The shorthand is written as a comment with the object instance name and the fields
you want the setter/assignment statements generated for.

* `// <obj>:<fields>` - Expands to Setter method calls
* `// set:<obj>:<fields>` - Expands to Setter method calls
* `// setf:<obj>:<fields>` - Expands to assignment statements on an object 

## Examples

```java
public class Main {
    public static void main(String... args) {
        Person person = new Person();

        // set:person:firstName
        person.setFirstName(/* TODO: set value here */);
        
        // person:lastName,middleName
        person.setLastName(/* TODO: set value here */);
        person.setMiddleName(/* TODO: set value here */);
                
        // Assignment statements on fields
        // setf:person:firstName,lastName,middleName
        person.firstName = /* TODO: set value here */;
        person.lastName = /* TODO: set value here */;
        person.middleName = /* TODO: set value here */;

        // Within an IntelliJ IDEA editor, just press `Ctrl + Alt + ;` 
        // on the line with the expression 
    }
}
```

## ROADMAP

This stuff will be possible in the near future.

```java
public class Main {
    public static void main(String... args) {
        Person person = new Person();
        // v0.3
        // Set directly on fields with require non null
        // setf:person:firstName!, lastName!
        // Set directly on fields with require non null
        person.firstName = Objects.requireNonNull(/* TODO: set value here */, "firstName");
        person.lastName = Objects.requireNonNull(/* TODO: set value here */, "lastName");
        
        // v0.4
        // Set directly on fields with custom method
        // setf:person:middleName*checkNotEmpty
        // Set directly on fields with custom method 
        person.middleName = checkNotEmpty(/* TODO: set value here */);

        // v0.5 
        // Set on a builder
        // setb:person:firstName,lastName,middleName
        person = new Person.Builder()
                .firstName(/* TODO: set value here */)
                .lastName(/* TODO: set value here */)
                .middleName(/* TODO: set value here */)
                .build();

    }
}
```

## Current/Known limitations

* Probably contains lots of bugs due to numerous edge cases that haven't been
tested.
* The plugin currently doesn't read/parse your Java classes, as a result:
    * the generated code is based on whatever you type. Watch out for typos :)
    * It does not actually know about your object instance or it's type.
* Does not support multi-caret selections

---

Copyright (c) NNDI