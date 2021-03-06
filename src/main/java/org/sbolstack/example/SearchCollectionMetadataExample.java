package org.sbolstack.example;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;

import org.sbolstack.frontend.IdentifiedMetadata;
import org.sbolstack.frontend.StackException;
import org.sbolstack.frontend.StackFrontend;
import org.sbolstandard.core2.SequenceOntology;

public class SearchCollectionMetadataExample {

	public static void main(String[] args) throws StackException
	{
        StackFrontend frontend = new StackFrontend("http://synbiohub.org:9090");
      
        ArrayList<IdentifiedMetadata> results = frontend.searchCollectionMetadata(null, 0, 1000);
        
        System.out.println(results.size() + " result(s)");
        
        for(IdentifiedMetadata result : results)
        {
        	System.out.println(result.name + ": " + result.uri);
        }
	}

}
