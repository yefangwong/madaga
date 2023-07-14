package dhf.system.chat;

import com.dhf.hrsys.service.impl.Synthesizer;
import edu.stanford.nlp.ie.machinereading.structure.MachineReadingAnnotations;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class StanfordTest {
    private static final Logger log = LoggerFactory.getLogger(StanfordTest.class);

    public static void main(String[] args){
        String text = "I want to find Miss Mary in finance department.";

        Properties props = new Properties();
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,parse,gender");

        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        Annotation document = new Annotation(text);

        pipeline.annotate(document);
        for (CoreMap sentence : document.get(CoreAnnotations.SentencesAnnotation.class)) {
            for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                log.info(token.value());
                log.info(", Gender: ");
                log.info(token.get(MachineReadingAnnotations.GenderAnnotation.class));
            }
        }
    }
}
