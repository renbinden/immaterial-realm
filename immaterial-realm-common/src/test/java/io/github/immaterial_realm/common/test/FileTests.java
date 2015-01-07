package io.github.immaterial_realm.common.test;

import com.insightfullogic.lambdabehave.JunitSuiteRunner;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.insightfullogic.lambdabehave.Suite.describe;
import static io.github.alyphen.immaterial_realm.common.util.FileUtils.loadMetadata;
import static io.github.alyphen.immaterial_realm.common.util.FileUtils.saveMetadata;

@RunWith(JunitSuiteRunner.class)
public class FileTests {{
    describe("metadata", it -> {
        it.should("deserialise to produce the same map as was serialised", expect -> {
            Map<String, Object> originalMetadata = new HashMap<>();
            originalMetadata.put("test-string", "test");
            originalMetadata.put("test-int", 5);
            originalMetadata.put("test-double", 6.2D);
            List<String> list = new ArrayList<>();
            list.add("test 1");
            list.add("test 2");
            originalMetadata.put("test-list", list);
            File testFile = new File("./file-metadata-test.json");
            saveMetadata(originalMetadata, testFile);
            Map<String, Object> deserialisedMetadata = loadMetadata(testFile);
            expect.that((String) deserialisedMetadata.get("test-string")).isEqualTo((String) originalMetadata.get("test-string"));
            expect.that((int) ((double) deserialisedMetadata.get("test-int"))).isEqualTo((int) originalMetadata.get("test-int"));
            expect.that((double) deserialisedMetadata.get("test-double")).isEqualTo((double) originalMetadata.get("test-double"));
            expect.that((List<String>) deserialisedMetadata.get("test-list")).isEqualTo((List<String>) originalMetadata.get("test-list"));
        });
    });
}}
