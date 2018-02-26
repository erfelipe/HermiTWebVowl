package br.com.erfelipe.hermitwebvowl;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import org.semanticweb.HermiT.Configuration;
import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.HermiT.Reasoner.ReasonerFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDisjointClassesAxiom;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLIndividualAxiom;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.OWLSubObjectPropertyOfAxiom;
import org.semanticweb.owlapi.reasoner.ConsoleProgressMonitor;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.util.InferredAxiomGenerator;
import org.semanticweb.owlapi.util.InferredClassAssertionAxiomGenerator;
import org.semanticweb.owlapi.util.InferredDataPropertyCharacteristicAxiomGenerator;
import org.semanticweb.owlapi.util.InferredDisjointClassesAxiomGenerator;
import org.semanticweb.owlapi.util.InferredEquivalentClassAxiomGenerator;
import org.semanticweb.owlapi.util.InferredEquivalentDataPropertiesAxiomGenerator;
import org.semanticweb.owlapi.util.InferredEquivalentObjectPropertyAxiomGenerator;
import org.semanticweb.owlapi.util.InferredIndividualAxiomGenerator;
import org.semanticweb.owlapi.util.InferredInverseObjectPropertiesAxiomGenerator;
import org.semanticweb.owlapi.util.InferredObjectPropertyAxiomGenerator;
import org.semanticweb.owlapi.util.InferredObjectPropertyCharacteristicAxiomGenerator;
import org.semanticweb.owlapi.util.InferredOntologyGenerator;
import org.semanticweb.owlapi.util.InferredSubClassAxiomGenerator;
import org.semanticweb.owlapi.util.InferredSubDataPropertyAxiomGenerator;
import org.semanticweb.owlapi.util.InferredSubObjectPropertyAxiomGenerator;

public class FXMLController implements Initializable {

    private File arquivo;

    @FXML
    private Label lblSelectFile;
    @FXML
    private Button btnAbrir;
    @FXML
    private Button btnReasoner;
    @FXML
    private TextArea txtAreaLog;
    @FXML
    private TextArea txtArea1;
    @FXML
    private TextArea txtArea2;
    @FXML
    private TextArea txtArea3;
    @FXML
    private Button btnValidacao;
    @FXML
    private Tab tabArea1;
    @FXML
    private Tab TabArea2;
    @FXML
    private Tab TabArea3;
    @FXML
    private Button btnReasoner2;
    @FXML
    private Button btnReasonerProp;

    public FXMLController() {
        arquivo = null;
    }

    @FXML
    private void evtAbrirArquivo(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("OWL Files", "*.owl"));
        arquivo = fileChooser.showOpenDialog(null);

        if (arquivo != null) {
            lblSelectFile.setText(arquivo.getAbsolutePath());
            setLog("Arquivo: " + arquivo.getPath() + " selecionado.");
            setTextArea1(abrirArqOWL(arquivo));
            setTabArea1(arquivo);
        } else {
            lblSelectFile.setText("Nenhum arquivo OWL selecionado.");
        }
    }

    @FXML
    private void evtValidacao(ActionEvent event) throws OWLOntologyCreationException {

        // First, we create an OWLOntologyManager object. The manager will load and save ontologies.
        OWLOntologyManager m = OWLManager.createOWLOntologyManager();

        if (arquivo != null) {
            //File f = new File("/Users/eduardofelipe/netbeansprojects/libs/hermit/project/examples/ontologies/pizza.owl");
            // We use the OWL API to load the Pizza ontology.
            OWLOntology o = m.loadOntologyFromOntologyDocument(arquivo);
            // Now, we instantiate HermiT by creating an instance of the Reasoner class in the package org.semanticweb.HermiT.
            Reasoner hermit = new Reasoner(o);
            // Finally, we output whether the ontology is consistent.
            //System.out.println(hermit.isConsistent());
            setLog("A consistência do arquivo pelo HermiT é: " + hermit.isConsistent());
        }

    }

    @FXML
    private void evtReasoner(ActionEvent event) throws OWLOntologyCreationException, IOException {

        //File inputOntologyFile = new File("/Users/eduardofelipe/netbeansprojects/libs/hermit/project/examples/ontologies/pizza.owl");
        if (arquivo != null) {
            // First, we create an OWLOntologyManager object. The manager will load and
            // save ontologies.
            OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
            setLog("Manager criardo.");
            // Now, we create the file from which the ontology will be loaded.
            // Here the ontology is stored in a file locally in the ontologies subfolder
            // of the examples folder.

            // We use the OWL API to load the ontology.
            OWLOntology ontology = manager.loadOntologyFromOntologyDocument(arquivo);
            setLog("Ontologia carregada para o objeto OWL.");
            // Now we can start and create the reasoner. Here we create an instance of HermiT
            // without any particular configuration given, which means HermiT uses default
            // parameters for blocking etc.
            Reasoner hermit = new Reasoner(ontology);
            // Now we create an output stream that HermiT can use to write the axioms. The output stream is
            // a wrapper around the file into which the axioms are written.
            String arquivoSemExtensao = arquivo.getName().replaceFirst("[.][^.]+$", "");
            String diretorioSemArquivo = arquivo.getPath().substring(0, arquivo.getPath().lastIndexOf(File.separator));

            String arquivoPretty = diretorioSemArquivo + File.separator + arquivoSemExtensao + "-prettyPrint.owl";
            File prettyPrintHierarchyFile = new File(arquivoPretty);
            if (!prettyPrintHierarchyFile.exists()) {
                prettyPrintHierarchyFile.createNewFile();
            }

            File arquivoDump = new File(diretorioSemArquivo + File.separator + arquivoSemExtensao + "-dump.owl");
            if (!arquivoDump.exists()) {
                arquivoDump.createNewFile();
            }
            // turn to an absolute file, so that we can write to it
            prettyPrintHierarchyFile = prettyPrintHierarchyFile.getAbsoluteFile();
            arquivoDump = arquivoDump.getAbsoluteFile();
            BufferedOutputStream prettyPrintHierarchyStreamOut = new BufferedOutputStream(new FileOutputStream(prettyPrintHierarchyFile));
            BufferedOutputStream dumpStreamOut = new BufferedOutputStream(new FileOutputStream(arquivoDump));
            // The output stream is wrapped into a print write with autoflush.
            PrintWriter output = new PrintWriter(prettyPrintHierarchyStreamOut, true);
            PrintWriter dumpOutput = new PrintWriter(dumpStreamOut, true);

            // Now we let HermiT pretty print the hierarchies. Since all parameters are set to true,
            // HermiT will print the class and the object property and the data property hierarchy.
            long tHierar = System.currentTimeMillis();
            hermit.printHierarchies(output, true, true, true);
            tHierar = System.currentTimeMillis() - tHierar;
            setLog("HermiT - Saída de hierarquias: " + output);

            // Now we let HermiT just dump out the axioms (faster, but not as pretty). Since all 
            // parameters are set to true, HermiT will print the axioms for class, object property, 
            // and the data property subsumptions and equivalences.
            long tDump = System.currentTimeMillis();
            hermit.dumpHierarchies(dumpOutput, true, true, true);
            tDump = System.currentTimeMillis() - tDump;
            setLog("HermiT - Saída de axiomas: " + dumpOutput);

            // Now that file contain an ontology with the inferred axioms and should be in the ontologies
            // subfolder (you Java IDE, e.g., Eclipse, might have to refresh its view of files in the file system)
            // before the file is visible.
            setLog("The ontology in " + prettyPrintHierarchyFile.getAbsolutePath() + " should now contain all subclass relationships between named classes as SubClassOf axioms pretty printed in functional-style syntax. ");
            setLog("The ontology in " + arquivoDump.getAbsolutePath() + " should now contain all relevant axioms for class and property subsumptions in functional-style syntax. ");
            setLog("Pretty printing took: " + tHierar + "ms, dumping took: " + tDump + "ms.");

            setTextArea2(abrirArqOWL(prettyPrintHierarchyFile));
            setTabArea2(prettyPrintHierarchyFile);

            setTextArea3(abrirArqOWL(arquivoDump));
            setTabArea3(arquivoDump);
        }
    }

    private void setTextArea1(String str) {
        txtArea1.setText(str);
    }

    private void setTabArea1(File f) {
        tabArea1.setText(f.getName());
    }

    private void setTextArea2(String str) {
        txtArea2.setText(str);
    }

    private void setTabArea2(File f) {
        TabArea2.setText(f.getName());
    }

    private void setTextArea3(String str) {
        txtArea3.setText(str);
    }

    private void setTabArea3(File f) {
        TabArea3.setText(f.getName());
    }

    private String abrirArqOWL(File arq) throws FileNotFoundException, IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(arq))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            return sb.toString();
        }
    }

    private void setLog(String str) {
        txtAreaLog.setText(txtAreaLog.getText() + "\n" + str);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void evtReasoner2(ActionEvent event) throws OWLOntologyCreationException, IOException, OWLOntologyStorageException, InterruptedException {
        if (arquivo != null) {
            // First, we create an OWLOntologyManager object. The manager will load and 
            // save ontologies. 
            OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
            // Now, we create the file from which the ontology will be loaded. 
            // Here the ontology is stored in a file locally in the ontologies subfolder
            // of the examples folder.
            File inputOntologyFile = arquivo;//new File("examples/ontologies/pizza.owl");
            // We use the OWL API to load the ontology. 
            OWLOntology ontology = manager.loadOntologyFromOntologyDocument(inputOntologyFile);
            // Now we can start and create the reasoner. Since materialisation of axioms is controlled 
            // by OWL API classes and is not natively supported by HermiT, we need to instantiate HermiT 
            // as an OWLReasoner. This is done via a ReasonerFactory object. 
            ReasonerFactory factory = new ReasonerFactory();
            // The factory can now be used to obtain an instance of HermiT as an OWLReasoner. 
            Configuration c = new Configuration();
            c.reasonerProgressMonitor = new ConsoleProgressMonitor();
            OWLReasoner reasoner = factory.createReasoner(ontology, c);
            // The following call causes HermiT to compute the class, object, 
            // and data property hierarchies as well as the class instances. 
            // Hermit does not yet support precomputation of property instances. 
            //reasoner.precomputeInferences(InferenceType.CLASS_HIERARCHY, InferenceType.CLASS_ASSERTIONS, InferenceType.OBJECT_PROPERTY_HIERARCHY, InferenceType.DATA_PROPERTY_HIERARCHY, InferenceType.OBJECT_PROPERTY_ASSERTIONS);
            // We now have to decide which kinds of inferences we want to compute. For different types 
            // there are different InferredAxiomGenerator implementations available in the OWL API and 
            // we use the InferredSubClassAxiomGenerator and the InferredClassAssertionAxiomGenerator 
            // here. The different generators are added to a list that is then passed to an 
            // InferredOntologyGenerator. 
            List<InferredAxiomGenerator<? extends OWLAxiom>> generators = new ArrayList<InferredAxiomGenerator<? extends OWLAxiom>>();
            generators.add(new InferredSubClassAxiomGenerator());
            generators.add(new InferredClassAssertionAxiomGenerator());
            // We dynamically overwrite the default disjoint classes generator since it tries to 
            // encode the reasoning problem itself instead of using the appropriate methods in the 
            // reasoner. That bypasses all our optimisations and means there is not progress report :-( 
            // We don't want that!
            generators.add(new InferredDisjointClassesAxiomGenerator() {
                boolean precomputed = false;

                protected void addAxioms(OWLClass entity, OWLReasoner reasoner, OWLDataFactory dataFactory, Set<OWLDisjointClassesAxiom> result) {
                    if (!precomputed) {
                        reasoner.precomputeInferences(InferenceType.DISJOINT_CLASSES);
                        precomputed = true;
                    }
                    for (OWLClass cls : reasoner.getDisjointClasses(entity).getFlattened()) {
                        result.add(dataFactory.getOWLDisjointClassesAxiom(entity, cls));
                    }
                }
            });
            // We can now create an instance of InferredOntologyGenerator. 
            InferredOntologyGenerator iog = new InferredOntologyGenerator(reasoner, generators);
            // Before we actually generate the axioms into an ontology, we first have to create that ontology. 
            // The manager creates the for now empty ontology for the inferred axioms for us. 
            OWLOntology inferredAxiomsOntology = manager.createOntology();
            // Now we use the inferred ontology generator to fill the ontology. That might take some 
            // time since it involves possibly a lot of calls to the reasoner.  
            //OWLDataFactory datafactory = manager.getOWLDataFactory();
            iog.fillOntology(manager, inferredAxiomsOntology);

            // Now the axioms are computed and added to the ontology, but we still have to save 
            // the ontology into a file. Since we cannot write to relative files, we have to resolve the 
            // relative path to an absolute one in an OS independent form. We do this by (virtually) creating a 
            // file with a relative path from which we get the absolute file.  
            String arquivoSemExtensao = arquivo.getName().replaceFirst("[.][^.]+$", "");
            String diretorioSemArquivo = arquivo.getPath().substring(0, arquivo.getPath().lastIndexOf(File.separator));

            String arquivoInferred = diretorioSemArquivo + File.separator + arquivoSemExtensao + "-inferred.owl";
            File inferredOntologyFile = new File(arquivoInferred);  //"examples/ontologies/pizza-inferred.owl"
            if (!inferredOntologyFile.exists()) {
                inferredOntologyFile.createNewFile();
            }
            inferredOntologyFile = inferredOntologyFile.getAbsoluteFile();
            // Now we create a stream since the ontology manager can then write to that stream. 
            OutputStream outputStream = new FileOutputStream(inferredOntologyFile);
            // We use the same format as for the input ontology.

            manager.saveOntology(inferredAxiomsOntology, manager.getOntologyFormat(ontology), outputStream);
            // Now that ontology that contains the inferred axioms should be in the ontologies subfolder 
            // (you Java IDE, e.g., Eclipse, might have to refresh its view of files in the file system) 
            // before the file is visible.  

            setLog("The ontology in " + inferredOntologyFile + " should now contain all inferred axioms. ");
            setTextArea2(abrirArqOWL(inferredOntologyFile));
            setTabArea2(inferredOntologyFile);

            GeraJson(inferredOntologyFile);
        }
    }

    private void GeraJson(File arq) throws IOException, InterruptedException {

        // Run a java app in a separate system process
        String arquivo = arq.getPath();
        Process proc = Runtime.getRuntime().exec("java -jar /Users/eduardofelipe/NetBeansProjects/HermiTWebVowl/owl2vowl.jar -file " + arquivo);
        // Then retreive the process output
        proc.waitFor(60, TimeUnit.SECONDS);
        InputStream in = proc.getInputStream();
        InputStream err = proc.getErrorStream();

        byte inMessages[] = new byte[in.available()];
        in.read(inMessages, 0, inMessages.length);
        setLog(new String(inMessages));

        byte errorMessages[] = new byte[err.available()];
        err.read(errorMessages, 0, errorMessages.length);
        setLog(new String(errorMessages));
    }

    private void InfereObjProp() throws OWLOntologyCreationException, IOException, OWLOntologyStorageException {
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        File inputOntologyFile = arquivo;

        OWLOntology ontology = manager.loadOntologyFromOntologyDocument(inputOntologyFile);
        ReasonerFactory rf = new ReasonerFactory();
        Configuration c = new Configuration();
        c.reasonerProgressMonitor = new ConsoleProgressMonitor();
        c.ignoreUnsupportedDatatypes = true;
        OWLReasoner reasoner = rf.createReasoner(ontology, c);

        OWLDataFactory df = manager.getOWLDataFactory();

        boolean consistencyCheck = reasoner.isConsistent();
        if (consistencyCheck) {
            reasoner.precomputeInferences(InferenceType.CLASS_HIERARCHY,
                    InferenceType.CLASS_ASSERTIONS, InferenceType.OBJECT_PROPERTY_HIERARCHY,
                    InferenceType.DATA_PROPERTY_HIERARCHY, InferenceType.OBJECT_PROPERTY_ASSERTIONS);

            List<InferredAxiomGenerator<? extends OWLAxiom>> generators = new ArrayList<>();
            generators.add(new InferredSubClassAxiomGenerator());
            generators.add(new InferredClassAssertionAxiomGenerator());
            generators.add(new InferredDataPropertyCharacteristicAxiomGenerator());
            generators.add(new InferredEquivalentClassAxiomGenerator());
            generators.add(new InferredEquivalentDataPropertiesAxiomGenerator());
            generators.add(new InferredEquivalentObjectPropertyAxiomGenerator());
            generators.add(new InferredInverseObjectPropertiesAxiomGenerator());
            generators.add(new InferredObjectPropertyCharacteristicAxiomGenerator());

            // NOTE: InferredPropertyAssertionGenerator significantly slows down
            // inference computation
            generators.add(new org.semanticweb.owlapi.util.InferredPropertyAssertionGenerator());

            generators.add(new InferredSubClassAxiomGenerator());
            generators.add(new InferredSubDataPropertyAxiomGenerator());
            generators.add(new InferredSubObjectPropertyAxiomGenerator());
            List<InferredIndividualAxiomGenerator<? extends OWLIndividualAxiom>> individualAxioms
                    = new ArrayList<>();
            generators.addAll(individualAxioms);

            generators.add(new InferredDisjointClassesAxiomGenerator());
            InferredOntologyGenerator iog = new InferredOntologyGenerator(reasoner, generators);
            OWLOntology inferredAxiomsOntology = manager.createOntology();
            iog.fillOntology(manager, inferredAxiomsOntology);

            String arquivoSemExtensao = arquivo.getName().replaceFirst("[.][^.]+$", "");
            String diretorioSemArquivo = arquivo.getPath().substring(0, arquivo.getPath().lastIndexOf(File.separator));

            String arquivoInferred = diretorioSemArquivo + File.separator + arquivoSemExtensao + "-inferredProperty.owl";
            File inferredOntologyFile = new File(arquivoInferred);
            if (!inferredOntologyFile.exists()) {
                inferredOntologyFile.createNewFile();
            }

            inferredOntologyFile = inferredOntologyFile.getAbsoluteFile();

            try (OutputStream outputStream = new FileOutputStream(inferredOntologyFile)) {
                // We use the same format as for the input ontology.
                manager.saveOntology(inferredAxiomsOntology, outputStream);
            }
        } // End if consistencyCheck
        else {
            System.out.println("Inconsistent input Ontology, Please check the OWL File");
        }

    }

    @FXML
    private void evtReasonerProp(ActionEvent event) throws OWLOntologyCreationException, IOException, OWLOntologyStorageException {
        InfereObjProp();
    }

}
