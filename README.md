# SBOL Stack Java Client

This project provides a programatic client to the [SBOL Stack](http://sbolstack.org).
It allows you to populate the [libSBOLj](https://github.com/SynBioDex/libSBOLj) data model directly from a stack instance.

## Building

This project is built using maven. First, check out the source:

```
git clone https://github.com/ICO2S/sbolstack-client-java.git
```

Then install locally using maven:

```
maven install
```

## Adding the client as a dependency

The maven dependency can be used in another maven project as follows:

```xml
<dependency>
    <groupId>org.sbolstack.frontend</groupId>
    <artifactId>sbolstack-frontend</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

## Documentation

Javadoc can be found [here](http://ico2s.github.io/sbolstack-client-java/).

# Client examples

Several examples can be found in the [`org.sbolstack.example`](https://github.com/ICO2S/sbolstack-client-java/tree/master/src/main/java/org/sbolstack/example) package.

All interatcion with the stack is through the [`StackFrontend`](http://ico2s.github.io/sbolstack-client-java/) facade.
Instances are created with the URL of the stack that they will bind against.

```java
StackFrontend frontend = new StackFrontend("http://localhost:9090");
```

This instance can then be used to count, search, fetch and upload SBOL data to the remote stack instance.

Firstly, create an `SBOLDocument` instance to act as a template for interacting with the stack.

```java
SBOLDocument template = new SBOLDocument();
template.setDefaultURIPrefix("http://www.bacillondex.org/");
```

This can then be used to make an SBOL entity to be used for searching, fetching or uploading.
For example:

```java
template.createComponentDefinition("BO_10050", ComponentDefinition.PROTEIN);
int count = frontend.countMatchingComponents(template);
```

This will query the stack for all component definition instances with the displayId "BO_10050" and of type Protein.
Rather than counting the result, we can fetch them:

 ```java
 ```