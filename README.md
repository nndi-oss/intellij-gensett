Gensett Plugin for IntelliJ IDEA 
===

This plugin expands shorthand for setter method calls or assignment statements,
so you don't have to write them manually. 

Within an IntelliJ IDEA editor, just press `Ctrl + Alt + ;`  on the line with the expression.
It also adds a `Gensett: Setters/assignments` to the `Generate` menu when the plugin is installed.

For example:

```java
Person person = new Person();
// set:person:firstName,lastName 
// ^ press `Ctrl + Alt + ;` on the line above to get the output below _
person.setFirstName(/* TODO: set value here */);
person.setLastName(/* TODO: set value here */);

// setf:person:age,otherThing
person.age = /* TODO: set value here */;
person.otherThing = /* TODO: set value here */;
```

## Installation

Get the alpha jar [here](https://github.com/nndi-oss/intellij-gensett/releases)

> *NOTE:* 
> This plugin is a Work In Progress, and a stable version has not been published yet,
> it's quality is questionable but it works :)


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
        person.firstName = Objects.requireNonNull(/* TODO: set value here */, "firstName");
        person.lastName = Objects.requireNonNull(/* TODO: set value here */, "lastName");
        
        // v0.4
        // Set directly on fields with custom method
        // setf:person:middleName*checkNotEmpty
        person.middleName = checkNotEmpty(/* TODO: set value here */);

        // 0.5 When it's smarter, it will know the fields of the class and be
        // able to set them all using *, also enable exclusions using !<field>
        // person:*
        // person:*,!lastName

        // 0.6? Set with values
        // person:firstName="Zikani",lastName="John",what="Jaja"

        // v0.7 
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