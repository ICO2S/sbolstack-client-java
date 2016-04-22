package org.sbolstack.example;

import java.net.URI;
import java.net.URISyntaxException;

import org.sbolstack.frontend.StackException;
import org.sbolstack.frontend.StackFrontend;
import org.sbolstandard.core2.ComponentDefinition;

public class RetrieveComponentExample
{
    public static void main(String[] args) throws StackException, URISyntaxException
    {
        StackFrontend frontend = new StackFrontend("http://localhost:9090");
        
        ComponentDefinition componentDefinition =
                frontend.fetchComponent(null, new URI("http://www.bacillondex.org/BO_10050"));
        
        System.out.println(componentDefinition.getSequences().toArray()[0]);
    }
}
