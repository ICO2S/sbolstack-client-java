package org.sbolstack.example;

import org.sbolstack.frontend.StackFrontend;
import org.sbolstandard.core2.ComponentDefinition;
import org.sbolstandard.core2.SBOLDocument;
import org.sbolstandard.core2.SBOLWriter;

public class SearchExample
{
	public static void main(String[] args) throws Exception
	{
		StackFrontend frontend = new StackFrontend("http://localhost:9090");
		
		SBOLDocument template = new SBOLDocument();
		
		template.setDefaultURIprefix("http://www.bacillondex.org/");		
		template.createComponentDefinition("BO_10050", ComponentDefinition.PROTEIN);

		SBOLDocument results = frontend.searchComponents(template, 0, 1000);
		
		SBOLWriter.write(results, System.out);
	}
}
