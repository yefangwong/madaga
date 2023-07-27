package dhf.system.chat;

import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.SegToken;
import com.huaban.analysis.jieba.WordDictionary;
import junit.framework.TestCase;
import org.junit.Test;

import java.nio.file.Paths;
import java.util.List;
import java.util.Locale;

public class JiebaSegTest extends TestCase {
    private JiebaSegmenter segmenter;

    String[] sentences = {"我想查詢公司財務部有沒有翁藝芳先生"};

    @Override
    protected void setUp() throws Exception {
        segmenter = new JiebaSegmenter();
        WordDictionary.getInstance().init(Paths.get("conf"));
    }
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    public void testCutForSearch() {
        for (String sentence : sentences) {
            List<SegToken> tokens = segmenter.process(sentence, JiebaSegmenter.SegMode.SEARCH, true);
            System.out.print(String.format(Locale.getDefault(), "\n%s\n%s", sentence, tokens.toString()));
        }
    }


    @Test
    public void testCutForIndex() {
        for (String sentence : sentences) {
            List<SegToken> tokens = segmenter.process(sentence, JiebaSegmenter.SegMode.INDEX, false);
            System.out.print(String.format(Locale.getDefault(), "\n%s\n%s", sentence, tokens.toString()));
        }
    }
}

