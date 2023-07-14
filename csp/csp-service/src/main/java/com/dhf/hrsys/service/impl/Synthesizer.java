package com.dhf.hrsys.service.impl;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLPClient;
import edu.stanford.nlp.util.CoreMap;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class Synthesizer {
    private static final Logger log = LoggerFactory.getLogger(Synthesizer.class);

    private String prompt;
    private static Synthesizer instance;

    public static Synthesizer getInstance() {
        if (instance == null)
            instance = new Synthesizer();
        return instance;
    }

    public String synthesize(String text) {
        return (genderPrompt(namePrompt(text)));
    }

    private String namePrompt(String text) {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,parse,coref");

        StanfordCoreNLPClient pipeline = new StanfordCoreNLPClient(props, "http://localhost", 9000, 2);

        Annotation document = new Annotation(extractQuestion(text));

        pipeline.annotate(document);
        for (CoreMap sentence : document.get(CoreAnnotations.SentencesAnnotation.class)) {
            for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                log.info(token.value());
                log.info(", Name: ");
                String NER = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);
                log.info(NER);
                if (StringUtils.equals(NER, "PERSON"))
                    text = text + ",姓名：" + token.value();
            }
        }
        return text;
    }

    private String genderPrompt(String text) {
        JiebaServiceImpl jiebaService = new JiebaServiceImpl();
        jiebaService.setRawText(text);
        for(Term t : jiebaService.getTerm()) {
            if(";先生;Mr.;".contains(t.getTerm())) {
                text = text + ",性別：男";
            } else if (";小姐;Miss;".contains(t.getTerm())) {
                text = text + ",性別：女";
            }
        }
        return text;
    }

    private String extractQuestion(String text) {
        // Define the regular expression pattern
        String pattern = "\\[question\\](.*?)\\[/question\\]";

        // Create a Pattern object
        Pattern regex = Pattern.compile(pattern);

        // Create a Matcher object
        Matcher matcher = regex.matcher(text);

        // Find the first match
        if (matcher.find()) {
            // Extract the text between [question] and [/question]
            text = matcher.group(1);
            //text = "I want to find Miss Mary in finance department.";
            //text = "Annie goes to school";
            log.debug("question={}: " + text);
        } else {
            System.out.println("No match found.");
        }
        return text;
    }
}
