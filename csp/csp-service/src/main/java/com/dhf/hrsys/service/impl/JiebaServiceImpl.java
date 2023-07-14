package com.dhf.hrsys.service.impl;

import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.WordDictionary;
import org.springframework.stereotype.Service;

import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

@Service
public class JiebaServiceImpl extends WordSegmentationService {
    private JiebaSegmenter segmenter;

    public JiebaServiceImpl() {
        segmenter = new JiebaSegmenter();
        WordDictionary.getInstance().init(Paths.get("conf"));
    }
    @Override public List<Term> getTerm() {
        List<String> segments = segmenter.sentenceProcess(this.getRawText());
        List<Term> result = null;
        if (segments.size() > 0) {
            result = segments.stream()
                .map(term -> {
                    Term t = new Term();
                    t.setTerm(term);
                    return t;
                })
                .toList();
        } else {
            return Collections.emptyList();
        }
        return result;
    }

    @Override public void send() throws Exception {
        throw new Exception("not implement yet!");
    }
}
