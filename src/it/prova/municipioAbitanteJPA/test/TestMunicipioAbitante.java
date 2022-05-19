package it.prova.municipioAbitanteJPA.test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.hibernate.LazyInitializationException;

import it.prova.municipioAbitanteJPA.dao.EntityManagerUtil;
import it.prova.municipioAbitanteJPA.model.Abitante;
import it.prova.municipioAbitanteJPA.model.Municipio;
import it.prova.municipioAbitanteJPA.service.MyServiceFactory;
import it.prova.municipioAbitanteJPA.service.abitante.AbitanteService;
import it.prova.municipioAbitanteJPA.service.municipio.MunicipioService;

public class TestMunicipioAbitante {

	public static void main(String[] args) {

		MunicipioService municipioService = MyServiceFactory.getMunicipioServiceInstance();
		AbitanteService abitanteService = MyServiceFactory.getAbitanteServiceInstance();

		try {

			// ora con il service posso fare tutte le invocazioni che mi servono
			System.out.println(
					"In tabella Municipio ci sono " + municipioService.listAllMunicipi().size() + " elementi.");
			System.out
					.println("In tabella Abitante ci sono " + abitanteService.listAllAbitanti().size() + " elementi.");

			testInserisciMunicipio(municipioService);
			System.out.println(
					"In tabella Municipio ci sono " + municipioService.listAllMunicipi().size() + " elementi.");

			testInserisciAbitante(municipioService, abitanteService);
			System.out
					.println("In tabella Abitante ci sono " + abitanteService.listAllAbitanti().size() + " elementi.");

			testRimozioneMunicipio(municipioService, abitanteService);
			System.out.println(
					"In tabella Municipio ci sono " + municipioService.listAllMunicipi().size() + " elementi.");

			testRimozioneAbitante(municipioService, abitanteService);
			System.out
					.println("In tabella Abitante ci sono " + abitanteService.listAllAbitanti().size() + " elementi.");

			testCercaMunicipioPerId(municipioService, abitanteService);
			System.out.println(
					"In tabella Municipio ci sono " + municipioService.listAllMunicipi().size() + " elementi.");

			testCercaAbitantePerId(municipioService, abitanteService);
			System.out
					.println("In tabella Abitante ci sono " + abitanteService.listAllAbitanti().size() + " elementi.");

			testModificaMunicipio(municipioService, abitanteService);
			System.out.println(
					"In tabella Municipio ci sono " + municipioService.listAllMunicipi().size() + " elementi.");

			testModificaAbitante(municipioService, abitanteService);
			System.out
					.println("In tabella Abitante ci sono " + abitanteService.listAllAbitanti().size() + " elementi.");

			testCercaTuttiMunicipiPerInizialeDescrizione(municipioService, abitanteService);
			System.out.println(
					"In tabella Municipio ci sono " + municipioService.listAllMunicipi().size() + " elementi.");

			testCercaTuttiMunicipiConMinorenni(municipioService, abitanteService);
			System.out.println(
					"In tabella Municipio ci sono " + municipioService.listAllMunicipi().size() + " elementi.");

			testCercaTuttiGliAbitantiConNome(municipioService, abitanteService);
			System.out
					.println("In tabella Abitante ci sono " + abitanteService.listAllAbitanti().size() + " elementi.");

			testCercaTuttiGliAbitantiConCognome(municipioService, abitanteService);
			System.out
					.println("In tabella Abitante ci sono " + abitanteService.listAllAbitanti().size() + " elementi.");

			testCercaTuttiGliAbitantiConCodiceMunicipio(municipioService, abitanteService);
			System.out
					.println("In tabella Abitante ci sono " + abitanteService.listAllAbitanti().size() + " elementi.");

			testLazyInitExc(municipioService, abitanteService);

		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			// questa è necessaria per chiudere tutte le connessioni quindi rilasciare il
			// main
			EntityManagerUtil.shutdown();
		}

	}

	private static void testInserisciMunicipio(MunicipioService municipioService) throws Exception {
		System.out.println(".......testInserisciMunicipio inizio.............");
		// creo nuovo municipio
		Municipio nuovoMunicipio = new Municipio("Municipio III", "III", "Via dei Nani");
		if (nuovoMunicipio.getId() != null)
			throw new RuntimeException("testInserisciMunicipio fallito: record già presente ");

		// salvo
		municipioService.inserisciNuovo(nuovoMunicipio);
		// da questa riga in poi il record, se correttamente inserito, ha un nuovo id
		// (NOVITA' RISPETTO AL PASSATO!!!)
		if (nuovoMunicipio.getId() == null)
			throw new RuntimeException("testInserisciMunicipio fallito ");

		System.out.println(".......testInserisciMunicipio fine: PASSED.............");
	}

	private static void testInserisciAbitante(MunicipioService municipioService, AbitanteService abitanteService)
			throws Exception {
		System.out.println(".......testInserisciAbitante inizio.............");

		// creo nuovo abitante ma prima mi serve un municipio
		List<Municipio> listaMunicipiPresenti = municipioService.listAllMunicipi();
		if (listaMunicipiPresenti.isEmpty())
			throw new RuntimeException("testInserisciAbitante fallito: non ci sono municipi a cui collegarci ");

		Abitante nuovoAbitante = new Abitante("Pluto", "Plutorum", 77, "Via Lecce");
		// lo lego al primo municipio che trovo
		nuovoAbitante.setMunicipio(listaMunicipiPresenti.get(0));

		// salvo il nuovo abitante
		abitanteService.inserisciNuovo(nuovoAbitante);

		// da questa riga in poi il record, se correttamente inserito, ha un nuovo id
		// (NOVITA' RISPETTO AL PASSATO!!!)
		if (nuovoAbitante.getId() == null)
			throw new RuntimeException("testInserisciAbitante fallito ");

		// il test fallisce anche se non è riuscito a legare i due oggetti
		if (nuovoAbitante.getMunicipio() == null)
			throw new RuntimeException("testInserisciAbitante fallito: non ha collegato il municipio ");

		System.out.println(".......testInserisciAbitante fine: PASSED.............");
	}

	private static void testRimozioneAbitante(MunicipioService municipioService, AbitanteService abitanteService)
			throws Exception {
		System.out.println(".......testRimozioneAbitante inizio.............");

		// inserisco un abitante che rimuoverò
		// creo nuovo abitante ma prima mi serve un municipio
		List<Municipio> listaMunicipiPresenti = municipioService.listAllMunicipi();
		if (listaMunicipiPresenti.isEmpty())
			throw new RuntimeException("testRimozioneAbitante fallito: non ci sono municipi a cui collegarci ");

		Abitante nuovoAbitante = new Abitante("Pietro", "Mitraglia", 33, "Via del Mare");
		// lo lego al primo municipio che trovo
		nuovoAbitante.setMunicipio(listaMunicipiPresenti.get(0));

		// salvo il nuovo abitante
		abitanteService.inserisciNuovo(nuovoAbitante);

		Long idAbitanteInserito = nuovoAbitante.getId();
		abitanteService.rimuovi(idAbitanteInserito);
		// proviamo a vedere se è stato rimosso
		if (abitanteService.caricaSingoloAbitante(idAbitanteInserito) != null)
			throw new RuntimeException("testRimozioneAbitante fallito: record non cancellato ");
		System.out.println(".......testRimozioneAbitante fine: PASSED.............");
	}

	private static void testRimozioneMunicipio(MunicipioService municipioService, AbitanteService abitanteService)
			throws Exception {
		System.out.println(".......testRimozioneMunicipio inizio.............");

		Municipio nuovoMunicipio = new Municipio("IV", "Municipio IV", "via Liguria");
		municipioService.inserisciNuovo(nuovoMunicipio);

		Long idMunicipioInserito = nuovoMunicipio.getId();
		municipioService.rimuovi(nuovoMunicipio);

		if (municipioService.caricaSingoloMunicipio(idMunicipioInserito) != null)
			throw new RuntimeException("testRimozioneMunicipio fallito: record non cancellato ");
		System.out.println(".......testRimozioneMunicipio fine: PASSED.............");
	}

	private static void testCercaTuttiGliAbitantiConNome(MunicipioService municipioService,
			AbitanteService abitanteService) throws Exception {
		System.out.println(".......testCercaTuttiGliAbitantiConNome inizio.............");

		// inserisco un paio di abitanti di test
		// prima mi serve un municipio
		List<Municipio> listaMunicipiPresenti = municipioService.listAllMunicipi();
		if (listaMunicipiPresenti.isEmpty())
			throw new RuntimeException(
					"testCercaTuttiGliAbitantiConNome fallito: non ci sono municipi a cui collegarci ");

		Abitante nuovoAbitante = new Abitante("Mariotto", "Bassi", 27, "Via Lucca");
		Abitante nuovoAbitante2 = new Abitante("Mariotto", "Nato", 37, "Via Roma");
		// lo lego al primo municipio che trovo
		nuovoAbitante.setMunicipio(listaMunicipiPresenti.get(0));
		nuovoAbitante2.setMunicipio(listaMunicipiPresenti.get(0));

		// salvo i nuovi abitante
		abitanteService.inserisciNuovo(nuovoAbitante);
		abitanteService.inserisciNuovo(nuovoAbitante2);

		// ora mi aspetto due 'Mario'
		if (abitanteService.cercaTuttiGliAbitantiConNome("Mariotto").size() < 2)
			throw new RuntimeException("testCercaTuttiGliAbitantiConNome fallito: numero record inatteso ");

		// clean up code
		abitanteService.rimuovi(nuovoAbitante.getId());
		abitanteService.rimuovi(nuovoAbitante2.getId());

		System.out.println(".......testCercaTuttiGliAbitantiConNome fine: PASSED.............");
	}

	private static void testCercaAbitantePerId(MunicipioService municipioService, AbitanteService abitanteService)
			throws Exception {
		System.out.println(".......testCercaAbitantePerId inizio.............");

		Abitante nuovoAbitante = new Abitante("Mariotto", "Bassi", 27, "Via Lucca");
		abitanteService.inserisciNuovo(nuovoAbitante);

		if (!abitanteService.caricaSingoloAbitante(nuovoAbitante.getId()).equals(nuovoAbitante))
			throw new RuntimeException("testCercaAbitantePerId fallito: numero record inatteso ");

		abitanteService.rimuovi(nuovoAbitante.getId());
		System.out.println(".......testCercaAbitantePerId fine: PASSED.............");
	}

	private static void testCercaMunicipioPerId(MunicipioService municipioService, AbitanteService abitanteService)
			throws Exception {
		System.out.println(".......testCercaMunicipioPerId inizio.............");

		Municipio nuovoMunicipio = new Municipio("Municipio V", "V", "Via Lucca");
		municipioService.inserisciNuovo(nuovoMunicipio);

		if (!municipioService.caricaSingoloMunicipio(nuovoMunicipio.getId()).equals(nuovoMunicipio))
			throw new RuntimeException("testCercaMunicipioPerId fallito: numero record inatteso ");

		municipioService.rimuovi(nuovoMunicipio);
		System.out.println(".......testCercaMunicipioPerId fine: PASSED.............");
	}

	private static void testModificaAbitante(MunicipioService municipioService, AbitanteService abitanteService)
			throws Exception {
		System.out.println(".......testCercaAbitantePerId inizio.............");
		Abitante abitanteModificato = abitanteService.listAllAbitanti().get(0);

		String nome = "Costanza";
		int eta = 15;
		abitanteModificato.setNome(nome);
		abitanteModificato.setEta(eta);

		abitanteService.aggiorna(abitanteModificato);
		if (!abitanteService.caricaSingoloAbitante(abitanteModificato.getId()).getNome().equals(nome)
				&& abitanteService.caricaSingoloAbitante(abitanteModificato.getId()).getEta() != eta) {
			throw new RuntimeException("testCercaAbitantePerId fallito: numero record inatteso ");
		}

		System.out.println(".......testCercaAbitantePerId fine: PASSED.............");
	}

	private static void testModificaMunicipio(MunicipioService municipioService, AbitanteService abitanteService)
			throws Exception {
		System.out.println(".......testModificaMunicipio inizio.............");

		Municipio municipioModificato = municipioService.listAllMunicipi().get(0);
		if (!municipioService.caricaSingoloMunicipio(municipioModificato.getId()).equals(municipioModificato))
			throw new RuntimeException("testModificaMunicipio fallito: numero record inatteso ");

		String codice = "L";
		String descrizione = "Municipio L";
		municipioModificato.setCodice(codice);
		municipioModificato.setDescrizione(descrizione);

		municipioService.aggiorna(municipioModificato);
		if (!municipioService.caricaSingoloMunicipio(municipioModificato.getId()).getCodice().equals(codice)
				&& !municipioService.caricaSingoloMunicipio(municipioModificato.getId()).getDescrizione()
						.equals(descrizione)) {
			throw new RuntimeException("testModificaMunicipio fallito: numero record inatteso ");
		}

		System.out.println(".......testModificaMunicipio fine: PASSED.............");
	}

	private static void testCercaTuttiGliAbitantiConCognome(MunicipioService municipioService,
			AbitanteService abitanteService) throws Exception {
		System.out.println("\n.......testCercaTuttiGliAbitantiConCognome inizio.............");
		List<Municipio> listaMunicipiPresenti = municipioService.listAllMunicipi();
		if (listaMunicipiPresenti.isEmpty())
			throw new RuntimeException(
					"testCercaTuttiGliAbitantiConCognome fallito: non ci sono municipi a cui collegarci ");

		String cognome = "Paoli";
		Abitante nuovoAbitante = new Abitante("Filippo", cognome, 27, "Via Lucca");
		Abitante nuovoAbitante2 = new Abitante("Sara", cognome, 37, "Via Roma");

		nuovoAbitante.setMunicipio(listaMunicipiPresenti.get(0));
		nuovoAbitante2.setMunicipio(listaMunicipiPresenti.get(0));

		abitanteService.inserisciNuovo(nuovoAbitante);
		abitanteService.inserisciNuovo(nuovoAbitante2);

		if (abitanteService.cercaTuttiGliAbitantiConCognome(cognome).size() != 2)
			throw new RuntimeException("testCercaTuttiGliAbitantiConCognome fallito: numero record inatteso ");

		abitanteService.rimuovi(nuovoAbitante.getId());
		abitanteService.rimuovi(nuovoAbitante2.getId());

		System.out.println(".......testCercaTuttiGliAbitantiConCognome fine: PASSED.............");
	}

	private static void testCercaTuttiGliAbitantiConCodiceMunicipio(MunicipioService municipioService,
			AbitanteService abitanteService) throws Exception {
		System.out.println("\n.......testCercaTuttiGliAbitantiConCodiceMunicipio inizio.............");
		List<Municipio> listaMunicipiPresenti = municipioService.listAllMunicipi();
		if (listaMunicipiPresenti.isEmpty())
			throw new RuntimeException(
					"testCercaTuttiGliAbitantiConCodiceMunicipio fallito: non ci sono municipi a cui collegarci ");

		Municipio primoMunicipio = listaMunicipiPresenti.get(0);

		Abitante nuovoAbitante = new Abitante("Filippo", "Manca", 27, "Via Lucca");
		Abitante nuovoAbitante2 = new Abitante("Sara", "Lippi", 37, "Via Roma");

		nuovoAbitante.setMunicipio(primoMunicipio);
		nuovoAbitante2.setMunicipio(primoMunicipio);

		abitanteService.inserisciNuovo(nuovoAbitante);
		abitanteService.inserisciNuovo(nuovoAbitante2);

		if (abitanteService.cercaTuttiGliAbitantiPerCodiceMunicipio(primoMunicipio.getCodice()).size() < 2)
			throw new RuntimeException("testCercaTuttiGliAbitantiConCodiceMunicipio fallito: numero record inatteso ");

		abitanteService.rimuovi(nuovoAbitante.getId());
		abitanteService.rimuovi(nuovoAbitante2.getId());

		System.out.println(".......testCercaTuttiGliAbitantiConCodiceMunicipio fine: PASSED.............");
	}

	private static void testCercaTuttiMunicipiPerInizialeDescrizione(MunicipioService municipioService,
			AbitanteService abitanteService) throws Exception {
		System.out.println(".......testCercaTuttiGliAbitantiConNome inizio.............");

		String iniziale = "Des";
		Municipio nuovoMunicipio = new Municipio(iniziale + "crizione", "MM", "Via Lucca");
		Municipio nuovoMunicipio2 = new Municipio(iniziale + "uetto", "MM", "Via Lucca");

		municipioService.inserisciNuovo(nuovoMunicipio);
		municipioService.inserisciNuovo(nuovoMunicipio2);
		System.out.println(municipioService.cercaTuttiIMunicipiConInizialeDescrizione(iniziale).size());
		if (municipioService.cercaTuttiIMunicipiConInizialeDescrizione(iniziale).size() < 2)
			throw new RuntimeException("testCercaTuttiMunicipiPerInizialeDescrizione fallito: numero record inatteso ");

		municipioService.rimuovi(nuovoMunicipio);
		municipioService.rimuovi(nuovoMunicipio2);

		System.out.println(".......testCercaTuttiMunicipiPerInizialeDescrizione fine: PASSED.............");
	}

	private static void testCercaTuttiMunicipiConMinorenni(MunicipioService municipioService,
			AbitanteService abitanteService) throws Exception {
		System.out.println(".......testCercaTuttiMunicipiConMinorenni inizio.............");

		Municipio primoMunicipio = municipioService.listAllMunicipi().get(0);
		Municipio secondoMunicipio = municipioService.listAllMunicipi().get(1);
		Abitante minorenne = new Abitante("nome1", "cognome1", 15, "via Roma");
		minorenne.setMunicipio(primoMunicipio);
		Abitante minorenne2 = new Abitante("nome2", "cognome2", 12, "via Romagna");
		minorenne2.setMunicipio(secondoMunicipio);
		
		abitanteService.inserisciNuovo(minorenne);
		abitanteService.inserisciNuovo(minorenne2);

		if (municipioService.cercaTuttiIMunicipiConMinorenni().size() < 2)
			throw new RuntimeException("testCercaTuttiMunicipiConMinorenni fallito: numero record inatteso ");

		System.out.println(".......testCercaTuttiMunicipiConMinorenni fine: PASSED.............");
	}

	private static void testLazyInitExc(MunicipioService municipioService, AbitanteService abitanteService)
			throws Exception {
		System.out.println(".......testLazyInitExc inizio.............");

		// prima mi serve un municipio
		List<Municipio> listaMunicipiPresenti = municipioService.listAllMunicipi();
		if (listaMunicipiPresenti.isEmpty())
			throw new RuntimeException("testLazyInitExc fallito: non ci sono municipi a cui collegarci ");

		Municipio municipioSuCuiFareIlTest = listaMunicipiPresenti.get(0);
		// se interrogo la relazione devo ottenere un'eccezione visto che sono LAZY
		try {
			municipioSuCuiFareIlTest.getAbitanti().size();
			// se la riga sovrastante non da eccezione il test fallisce
			throw new RuntimeException("testLazyInitExc fallito: eccezione non lanciata ");
		} catch (LazyInitializationException e) {
			// 'spengo' l'eccezione per il buon fine del test
		}
		// una LazyInitializationException in quanto il contesto di persistenza è chiuso
		// se usiamo un caricamento EAGER risolviamo...dipende da cosa ci serve!!!
		// municipioService.caricaSingoloMunicipioConAbitanti(...);
		System.out.println(".......testLazyInitExc fine: PASSED.............");
	}

}
