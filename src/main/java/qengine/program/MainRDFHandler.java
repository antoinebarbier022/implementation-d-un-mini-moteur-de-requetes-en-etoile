package qengine.program;

import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.rio.helpers.AbstractRDFHandler;



/**
 * Le RDFHandler intervient lors du parsing de données et permet d'appliquer un traitement pour chaque élément lu par le parseur.
 * 
 * <p>
 * Ce qui servira surtout dans le programme est la méthode {@link #handleStatement(Statement)} qui va permettre de traiter chaque triple lu.
 * </p>
 * <p>
 * À adapter/réécrire selon vos traitements.
 * </p>
 */

 public final class MainRDFHandler extends AbstractRDFHandler {


			
	@Override
	public void handleStatement(Statement st) {

		String subject = st.getSubject().stringValue();
		String predicate = st.getPredicate().stringValue();
		String object = st.getObject().stringValue();
		Dictionnaire dico = dico.getInstance();
		

		// Ajout dans le dictionnaire
		dico.add(subject, predicate, object);

		// Ajout dans les index
		int S = dico.get(subject);
		int P = dico.get(predicate);
		int O = dico.get(object);

		for(Triplet triplet : Triplet.values()) {
			Index.getInstance(triplet).add(S, P, O);
		}
		
	
	
	
	
	
	
	
	
	
	
	
	
	
	}
}