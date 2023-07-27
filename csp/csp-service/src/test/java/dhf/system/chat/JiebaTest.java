package dhf.system.chat;

import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.WordDictionary;
import junit.framework.TestCase;
import org.junit.Test;

import java.nio.file.Paths;

public class JiebaTest extends TestCase {
    private JiebaSegmenter segmenter;

    String sentence = "我想查詢公司財務部有沒有范小華小姐";

    @Override
    protected void setUp() throws Exception {
        segmenter = new JiebaSegmenter();
        WordDictionary.getInstance().init(Paths.get("conf"));
    }
    @Test
    public void testCutForSearch() {
        System.out.println(segmenter.sentenceProcess(sentence));
    }
}
