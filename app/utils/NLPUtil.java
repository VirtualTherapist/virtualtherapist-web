package utils;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by bas on 9-10-14.
 */
public class NLPUtil {

    public static final String POS_TAG_FILE_NAME = "lib/opennlp-bin/nl-pos-maxent.bin";
    public static final String TOKEN_FILE_NAME = "lib/opennlp-bin/nl-token.bin";
    public static final String SENTENCE_FILE_NAME = "lib/opennlp-bin/nl-sent.bin";
    private static NLPUtil instance;
    private POSTaggerME tagger;
    private Tokenizer tokenizer;
    private SentenceDetectorME sentenceDetector;

    private NLPUtil() {
        POSModel tagModel;
        TokenizerModel tokenModel;
        SentenceModel sentenceModel;
        InputStream taggerIn = null;
        InputStream tokenIn;
        InputStream sentenceIn;
        try {
            tokenIn = new FileInputStream(TOKEN_FILE_NAME);
            taggerIn = new FileInputStream(POS_TAG_FILE_NAME);
            sentenceIn = new FileInputStream(SENTENCE_FILE_NAME);

            tokenModel = new TokenizerModel(tokenIn);
            tagModel = new POSModel(taggerIn);
            sentenceModel = new SentenceModel(sentenceIn);

            tokenizer = new TokenizerME(tokenModel);
            tagger = new POSTaggerME(tagModel);
            sentenceDetector = new SentenceDetectorME(sentenceModel);
        }catch(IOException e) {
            e.printStackTrace();
        }finally {
            if(taggerIn != null) {
                try {
                    taggerIn.close();
                }catch(IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static NLPUtil getInstance() {
        if(instance == null) {
            instance = new NLPUtil();
        }
        return instance;
    }

    private String[] tag(String[] sentenceTokens) {
        return tagger.tag(sentenceTokens);
    }

    private String[] tokenize(String sentence) {
        return tokenizer.tokenize(sentence);
    }

    private String[] detectSentences(String message) {
        return sentenceDetector.sentDetect(message);
    }

    public SortedMap<String, String>[] tagMessage(String message) {
        String[] sentences = detectSentences(message);
        SortedMap<String, String>[] tokensAndTags = new SortedMap[sentences.length];
        String[] tokens;
        String[] tags;
        for(int i = 0; i < sentences.length; i++) {
            tokens = tokenize(sentences[i]);
            tags = tagger.tag(tokens);
            TreeMap<String, String> sortedMap = new TreeMap<>();
            for(int j = 0; j < tags.length; j++) {
                sortedMap.put(tokens[j], tags[j]);
                //System.out.println("Token:\t" + tokens[j] + "\t\t\tTag:\t" + tags[j]);
            }
            tokensAndTags[i] = sortedMap;
        }
        return tokensAndTags;
    }

}
